package com.yl.g_dataSource.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DruidTest {

	@Test
	public void getConnection() throws Exception{
		Properties pros = new Properties();
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
		pros.load(is);
		DataSource source = DruidDataSourceFactory.createDataSource(pros);
		Connection conn = source.getConnection();
		conn.close();
		System.out.println(conn);

	}
}
