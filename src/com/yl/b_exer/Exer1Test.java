package com.yl.b_exer;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.yl.utils.util.JDBCUtils2;
import org.junit.Test;

//课后练习1
public class Exer1Test {
	
	@Test
	public void testInsert(){
//		Scanner scanner = new Scanner(System.in);
//		System.out.print("请输入用户名：");
//		String name = scanner.next();
//		System.out.print("请输入邮箱：");
//		String email = scanner.next();
//		System.out.print("请输入生日：");
//		String birthday = scanner.next();//'1992-09-08' 是日期默认格式，执行时会将其作为日期提交
		
//		String sql = "insert into customers(name,email,birth)values(?,?,?)";
//
//		int insertCount = update(sql,name,email,birthday);
//		if(insertCount > 0){
//			System.out.println("添加成功");
//
//		}else{
//			System.out.println("添加失败");
//		}

		String sql2 = "delete from customers where name = ?";
		System.out.println(update(sql2,"lily"));
		
	}
	
	
	// 通用的增删改操作
	public int update(String sql, Object... args) {// sql中占位符的个数与可变形参的长度相同！
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 1.获取数据库的连接
			conn = JDBCUtils2.getConnection();
			// 2.预编译sql语句，返回PreparedStatement的实例
			ps = conn.prepareStatement(sql);
			// 3.填充占位符
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);// 小心参数声明错误！！
			}
			// 4.执行
			/*
			 * ps.execute():
			 * 如果执行的是查询操作，有返回结果，则此方法返回true;
			 * 如果执行的是增、删、改操作，没有返回结果，则此方法返回false.
			 */
			//方式一：
//			return ps.execute();
			//方式二：dml操作成功会返回受影响的行数，若没有影响的行数或者ddl，返回0
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5.资源的关闭
			JDBCUtils2.close(conn, ps);

		}
		return 0;
	}

}
