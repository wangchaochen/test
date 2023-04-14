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
public class QueryMobileDao {
    @Autowired
    @Qualifier("primaryDataSource")
    DataSource dataSource;

    public String queryMobileByUid(String uid)  {
        String sql = "select mobile from CMIT.TM_SVCD_USER_MOBILE where status='3' and lower(USERID) = '"+uid+"'";
        return queryMobile(sql);
    }

    public String queryMobileByMail(String mail)  {
        String sql = "select mobile from CMIT.TM_SVCD_USER_MOBILE where status='3' and lower(MAIL) = '"+mail+"'";
        return queryMobile(sql);
    }

    public String queryMobileBySamAccountName(String samaccountname)  {
        String sql = "select mobile from CMIT.TM_SVCD_USER_MOBILE where status='3' and lower(SAMACCOUNTNAME) = '"+samaccountname+"'";
        return queryMobile(sql);
    }

    public String queryMobile(String sql)  {
    	System.out.println(sql);
        String mobile = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                mobile = rs.getString(1);
                System.out.println(mobile);
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
        return mobile;
    }


}
