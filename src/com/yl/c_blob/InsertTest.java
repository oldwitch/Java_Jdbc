package com.yl.c_blob;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.yl.utils.util.JDBCUtils2;
import org.junit.Test;


/*
 * 使用PreparedStatement实现批量数据的操作
 *
 * update、delete本身就具有批量操作的效果(利用where条件)。
 * 此时的批量操作，主要指的是批量插入。使用PreparedStatement如何实现更高效的批量插入？
 *
 * 题目：向goods表中插入20000条数据
 * CREATE TABLE goods(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(25)
   );
 * 方式一：使用Statement，因为不会预编译sql，每次执行都要对sql语句进行重新编译，耗时长，且存在sql注入的问题
 * Connection conn = JDBCUtils.getConnection();
 * Statement st = conn.createStatement();
 * for(int i = 1;i <= 20000;i++){
 * 		String sql = "insert into goods(name)values('name_" + i + "')";
 * 		st.execute(sql);
 * }
 *
 */


public class InsertTest {
    @Test
    public void testBatch1(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            long start = System.currentTimeMillis();
            conn = JDBCUtils2.getConnection();
            String sql = "insert into goods (name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < 20000; i++) {
                ps.setString(1,"name"+i);
                ps.execute();
            }

            long end = System.currentTimeMillis();
            System.out.println("cost time: "+(end-start));//110219
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils2.close(conn,ps);
        }
    }

    @Test
    public void testBatch2(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            long start = System.currentTimeMillis();
            conn = JDBCUtils2.getConnection();
            String sql = "insert into goods (name) values (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i <= 20000; i++) {
                ps.setString(1,"name"+i);
                ps.addBatch();
                if (i % 500 == 0){//如果用while，会进入死循环，因为0除以500始终为0
                    ps.executeBatch();//由于每次执行都会自动commit，也会消耗时间，因此设置不自动commit，最后统一commit
                    ps.clearBatch();
                }
            }

            long end = System.currentTimeMillis();
            System.out.println("cost time: "+(end-start));//1385
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils2.close(conn,ps);
        }
    }

	@Test
	public void testBatch3(){
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			long start = System.currentTimeMillis();
			conn = JDBCUtils2.getConnection();
			String sql = "insert into goods (name) values (?)";
			ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);
			for (int i = 0; i <= 20000; i++) {
				ps.setString(1,"name"+i);
				ps.addBatch();
				if (i % 500 == 0){//如果用while，会进入死循环，因为0除以500始终为0
					ps.executeBatch();//由于每次执行都会自动commit，也会消耗时间，因此设置不自动commit，最后统一commit
					ps.clearBatch();
				}
			}
			conn.commit();
			long end = System.currentTimeMillis();
			System.out.println("cost time: "+(end-start));//1002
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			JDBCUtils2.close(conn,ps);
		}
	}

	//批量插入的方式二：使用PreparedStatement
	@Test
	public void testInsert1() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {

			long start = System.currentTimeMillis();

			conn = JDBCUtils2.getConnection();
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i = 1;i <= 20000;i++){
				ps.setObject(1, "name_" + i);

				ps.execute();
			}

			long end = System.currentTimeMillis();

			System.out.println("花费的时间为：" + (end - start));//20000:83065
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
            JDBCUtils2.close(conn, ps);

		}

	}

	/*
	 * 批量插入的方式三：
	 * 1.addBatch()、executeBatch()、clearBatch()
	 * 2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
	 * 		 ?rewriteBatchedStatements=true 写在配置文件的url后面
	 * 3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
	 */
	@Test
	public void testInsert2() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {

			long start = System.currentTimeMillis();

			conn = JDBCUtils2.getConnection();
			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i = 1;i <= 1000000;i++){
				ps.setObject(1, "name_" + i);

				//1."攒"sql
				ps.addBatch();

				if(i % 500 == 0){
					//2.执行batch
					ps.executeBatch();

					//3.清空batch
					ps.clearBatch();
				}

			}

			long end = System.currentTimeMillis();

			System.out.println("花费的时间为：" + (end - start));//20000:83065 -- 565
		} catch (Exception e) {								//1000000:16086
			e.printStackTrace();
		}finally{
			JDBCUtils2.close(conn, ps);

		}

	}

	//批量插入的方式四：设置连接不允许自动提交数据
	@Test
	public void testInsert3() {
		Connection conn = null;
		PreparedStatement ps = null;
		try {

			long start = System.currentTimeMillis();

			conn = JDBCUtils2.getConnection();

			//设置不允许自动提交数据
			conn.setAutoCommit(false);

			String sql = "insert into goods(name)values(?)";
			ps = conn.prepareStatement(sql);
			for(int i = 1;i <= 1000000;i++){
				ps.setObject(1, "name_" + i);

				//1."攒"sql
				ps.addBatch();

				if(i % 500 == 0){
					//2.执行batch
					ps.executeBatch();

					//3.清空batch
					ps.clearBatch();
				}

			}

			//提交数据
			conn.commit();

			long end = System.currentTimeMillis();

			System.out.println("花费的时间为：" + (end - start));//20000:83065 -- 565
		} catch (Exception e) {								//1000000:16086 -- 5114
			e.printStackTrace();
		}finally{
			JDBCUtils2.close(conn, ps);

		}

	}
}
