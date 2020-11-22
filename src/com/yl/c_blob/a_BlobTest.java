package com.yl.c_blob;

import com.yl.utils.util.JDBCUtils2;
import org.junit.Test;

import java.io.*;
import java.sql.*;

public class a_BlobTest {

    @Test
    public void test() throws IOException {
//        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
//        FileInputStream fis = new FileInputStream(new File("2.PNG"));
//        System.out.println(insertBlob(sql, "didi","aaa@.qq.com", "1997-12-21", fis));
//        fis.close();
        String sql = "select id,name,email,birth,photo from customers where id = ?";
        getBlob(sql,16);
    }

    public boolean insertBlob(String sql,Object...args){
        Connection conn = null;
        PreparedStatement ps = null;

        try{
            conn = JDBCUtils2.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            boolean result = ps.execute();
            return result;

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return false;
    }

    public void getBlob(String sql,Object...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try{
            conn = JDBCUtils2.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();

            if (rs.next()){
                Blob blob = rs.getBlob("photo");
                try {
                    is = blob.getBinaryStream();//要是没有数据会报NULLPointerException
                }catch (NullPointerException e){
                    System.out.println("没有对应的id");
                    return;
                }
                System.out.println("output");
                fos = new FileOutputStream(new File("out.png"));
                byte[] bytes = new byte[1024];
                int len;
                while ((len = is.read(bytes))!=-1){
                    fos.write(bytes,0,len);
                }

				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date birth = rs.getDate("birth");



            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (fos != null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            JDBCUtils2.rsClose(conn,ps,rs);
        }
    }

}


