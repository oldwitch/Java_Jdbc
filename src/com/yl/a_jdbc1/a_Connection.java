package com.yl.a_jdbc1;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// different methods to get jdbc connections

public class a_Connection {
    //method1
    @Test
    public  void testConnection1() throws SQLException {
        Driver driver=new com.mysql.jdbc.Driver();

        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","110110");

        Connection connection=driver.connect(url,info);
        System.out.println(connection);
        connection.close();

    }

    @Test
    public void testConnection2() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //通过反射创建对象
        Class aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","110110");

        Connection connection=driver.connect(url,info);
        System.out.println(connection);
        connection.close();
    }

    @Test
    public void testConnection3() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //通过反射创建对象
        Class aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //用于管理driver的类
        DriverManager.registerDriver(driver);

        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","110110");

        Connection connection = DriverManager.getConnection(url, info);
        System.out.println(connection);
        connection.close();
    }

    @Test
    public void testConnection4() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //通过反射创建对象
        Class.forName("com.mysql.jdbc.Driver");

        //方法四不需要注册driver，因为在Driver类中的静态代码块中已经进行了注册
//        static {
//            try {
//                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//            } catch (SQLException var1) {
//                throw new RuntimeException("Can't register driver!");
//            }
//        }


//        Driver driver = (Driver) aClass.newInstance();

        //用于管理driver的类
//        DriverManager.registerDriver(driver);

        String url="jdbc:mysql://localhost:3306/test";
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","110110");

        Connection connection = DriverManager.getConnection(url, info);
        System.out.println(connection);
        connection.close();
    }


    //最终方法，通过配置文件获取
    @Test
    public void testConnection5() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {
        InputStream rs = a_Connection.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pro = new Properties();
        pro.load(rs);
        String url = pro.getProperty("url");
        String user = pro.getProperty("user");
        String password = pro.getProperty("password");
        String driverClass = pro.getProperty("driverClass");

        //通过反射创建对象
        Class.forName(driverClass);
        //方法四不需要注册driver，因为在Driver类中的静态代码块中已经进行了注册
//        static {
//            try {
//                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//            } catch (SQLException var1) {
//                throw new RuntimeException("Can't register driver!");
//            }
//        }

        Connection connection = DriverManager.getConnection(url, user,password);
        System.out.println(connection);
        connection.close();
    }
}
