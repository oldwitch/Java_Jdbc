package com.yl.a_jdbc1;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.yl.utils.util.JDBCUtils;
import com.yl.utils.util.JDBCUtils2;
import org.junit.Test;


/**
 * 使用PreparedStatement来替换Statement,实现对数据表的增删改操作
 * 增删改；查
 */

/**
 *
 * @Description 演示使用PreparedStatement替换Statement，解决SQL注入问题
 * 因为preparedStatement会预编译sql，即预编译时是 XX and XX，则实际执行时会直接将传入的内容作为参数执行，而不会重新改变sql逻辑
 *
 * 除了解决Statement的拼串、sql注入问题之外，PreparedStatement还有哪些好处呢？
 * 1.PreparedStatement操作Blob的数据，而Statement做不到。
 * 2.PreparedStatement可以实现更高效的批量操作。
 *
 */
public class c_PreparedStatementUpdateTest {

	@Test
	public void testCommonUpdate(){
//		String sql = "delete from customers where id = ?";
//		update(sql,3);

		String sql = "update `order` set order_name = ? where order_id = ?";
		update(sql,"DD","2");

	}

	//通用的增删改操作
	public void update(String sql,Object ...args){//sql中占位符的个数与可变形参的长度相同！
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//1.获取数据库的连接
			conn = JDBCUtils2.getConnection();
			//2.预编译sql语句，返回PreparedStatement的实例
			ps = conn.prepareStatement(sql);
			//3.填充占位符
			for(int i = 0;i < args.length;i++){
				ps.setObject(i + 1, args[i]);//小心参数声明错误！！
			}
			//4.执行
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//5.资源的关闭
			JDBCUtils2.close(conn, ps);
		}
	}

	//修改customers表的一条记录
	@Test
	public void testUpdate(){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//1.获取数据库的连接
			conn = JDBCUtils.getConnection();
			//2.预编译sql语句，返回PreparedStatement的实例
			String sql = "update customers set name = ? where id = ?";
			ps = conn.prepareStatement(sql);
			//3.填充占位符
			ps.setObject(1,"莫扎特");
			ps.setObject(2, 18);
			//4.执行
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//5.资源的关闭
			JDBCUtils.closeResource(conn, ps);

		}
	}


	// 向customers表中添加一条记录
	@Test
	public void testInsert() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// 1.读取配置文件中的4个基本信息
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

			Properties pros = new Properties();
			pros.load(is);

			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");

			// 2.加载驱动
			Class.forName(driverClass);

			// 3.获取连接
			conn = DriverManager.getConnection(url, user, password);

//		System.out.println(conn);

			//4.预编译sql语句，返回PreparedStatement的实例
			String sql = "insert into customers(name,email,birth)values(?,?,?)";//?:占位符
			ps = conn.prepareStatement(sql);
			//5.填充占位符
			ps.setString(1, "哪吒");
			ps.setString(2, "nezha@gmail.com");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("1000-01-01");
			ps.setDate(3, new Date(date.getTime()));

			//6.执行操作
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//7.资源的关闭
			try {
				if(ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

}
