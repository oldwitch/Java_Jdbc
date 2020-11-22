package com.yl.e_dao2.dao;

import com.yl.utils.util.JDBCUtils2;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/*
 * DAO: data(base) access object
 * 封装了针对于数据表的通用的操作
 */
public abstract class BaseDAO<T> {
	private Class<T> clazz = null;
	{
		//这里的this是所创建的对象，因此子类继承父类，创建子类对象时，会加载父类对象，当前代码块会自动调用，this为子类对象
		Type genericSuperclass = this.getClass().getGenericSuperclass();//获取父类泛型类
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;//获取带参数的类型
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取泛型参数
		clazz = (Class<T>) actualTypeArguments[0];//这个class对象可以直接在下面的方法中使用
	}

	// 通用的增删改操作---version 2.0 （考虑上事务）
	public int update(Connection conn, String sql, Object... args) {// sql中占位符的个数与可变形参的长度相同！
		PreparedStatement ps = null;
		try {
			// 1.预编译sql语句，返回PreparedStatement的实例
			ps = conn.prepareStatement(sql);
			// 2.填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// 小心参数声明错误！！
			}
			// 3.执行
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 4.资源的关闭
			JDBCUtils2.close(null, ps);

		}
		return 0;

	}

	// 通用的查询操作，用于返回数据表中的一条记录（version 2.0：考虑上事务）
	public  T getInstance(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			rs = ps.executeQuery();
			// 获取结果集的元数据 :ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// 通过ResultSetMetaData获取结果集中的列数
			int columnCount = rsmd.getColumnCount();

			if (rs.next()) {
				T t = clazz.newInstance();
				// 处理结果集一行数据中的每一个列
				for (int i = 0; i < columnCount; i++) {
					// 获取列值
					Object columValue = rs.getObject(i + 1);

					// 获取每个列的列名
					// String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// 给t对象指定的columnName属性，赋值为columValue：通过反射
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columValue);
				}
				return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils2.rsClose(null, ps, rs);

		}

		return null;
	}
	// 通用的查询操作，用于返回数据表中的多条记录构成的集合（version 2.0：考虑上事务）
	public  List<T> getForList(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			ps = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}

			rs = ps.executeQuery();
			// 获取结果集的元数据 :ResultSetMetaData
			ResultSetMetaData rsmd = rs.getMetaData();
			// 通过ResultSetMetaData获取结果集中的列数
			int columnCount = rsmd.getColumnCount();
			// 创建集合对象
			ArrayList<T> list = new ArrayList<T>();
			while (rs.next()) {
				T t = clazz.newInstance();
				// 处理结果集一行数据中的每一个列:给t对象指定的属性赋值
				for (int i = 0; i < columnCount; i++) {
					// 获取列值
					Object columValue = rs.getObject(i + 1);

					// 获取每个列的列名
					// String columnName = rsmd.getColumnName(i + 1);
					String columnLabel = rsmd.getColumnLabel(i + 1);

					// 给t对象指定的columnName属性，赋值为columValue：通过反射
					Field field = clazz.getDeclaredField(columnLabel);
					field.setAccessible(true);
					field.set(t, columValue);
				}
				list.add(t);
			}

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils2.rsClose(null, ps, rs);

		}

		return null;
	}
	//用于查询特殊值的通用的方法
	public <E> E getValue(Connection conn,String sql,Object...args){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);
				
			}
			
			rs = ps.executeQuery();
			if(rs.next()){
				return (E) rs.getObject(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtils2.rsClose(null, ps, rs);
			
		}
		return null;
		
	}	
}
