package com.yl.utils.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JDBCUtils2 {
    public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
        InputStream rs = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(rs);
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String driverClass = properties.getProperty("driverClass");
        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
    public static void rsClose(Connection conn, Statement ps,ResultSet rs){
        try {
        if (rs!=null)
            rs.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (ps!=null)
                ps.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn!=null)
                conn.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void close(Connection conn, Statement ps){
        try {
            if (ps!=null)
                ps.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (conn!=null)
                conn.close();
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
