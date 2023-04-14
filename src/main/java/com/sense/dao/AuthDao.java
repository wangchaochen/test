package com.sense.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthDao {
    @Autowired
    @Qualifier("primaryDataSource")
    DataSource dataSource;

    public String getAuthInfo(){
        String sql = "select * from TM_SVCD_CONSUMER_APP";
        return queryAuthInfo(sql);
    }

    public String queryAuthInfo(String sql)  {
        String client_id = null;
        String client_secret = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                client_id = rs.getString(1);
                client_secret = rs.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if(rs!=null) {
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return client_id+":"+client_secret;
    }
}
