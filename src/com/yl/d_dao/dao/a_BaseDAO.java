package com.yl.d_dao.dao;

import com.yl.utils.util.JDBCUtils2;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public abstract class a_BaseDAO {
    public int update(Connection conn,String sql,Object...args){
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            return ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils2.close(null,ps);
        }
        return 0;
    }

    public <T> T getOne(Class<T> clazz, Connection conn, String sql,Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            if (rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < count; i++) {
                    String column = metaData.getColumnLabel(i + 1);
                    Object value = rs.getObject(i+1);

                    Field field = clazz.getDeclaredField(column);
                    field.setAccessible(true);
                    field.set(t,value);

                }
                return t;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils2.rsClose(null,ps,rs);
        }
        return null;
    }

    public <T> List<T> getList(Class<T> clazz, Connection conn , String sql, Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            ArrayList<T> ts = new ArrayList<>();
            while (rs.next()){
                T t = clazz.newInstance();
                for (int i = 0; i < count; i++) {
                    String column = metaData.getColumnLabel(i + 1);
                    Object value = rs.getObject(i+1);

                    Field field = clazz.getDeclaredField(column);
                    field.setAccessible(true);
                    field.set(t,value);

                }
                ts.add(t);
            }
            return ts;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils2.rsClose(null,ps,rs);
        }
        return null;
    }

    public <E> E getValue(Connection conn,String sql,Object...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()){
                return (E)rs.getObject(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils2.rsClose(null,ps,rs);
        }
        return null;
    }
}
