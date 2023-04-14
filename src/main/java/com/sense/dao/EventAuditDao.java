package com.sense.dao;

import com.sense.entity.EventAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;

@Component
public class EventAuditDao {
    @Autowired
    @Qualifier("primaryDataSource")
    DataSource dataSource;

    public void saveEventAudit(EventAudit eventAudit) {
        String sql = "INSERT INTO CMIT.TL_SVCD_EVENT_AUDIT (EVENT_TIME,EVENT_TYPE,EVENT_NAME,EVENT_SUBJECT,EVENT_INFO) VALUES(?,?,?,?,?)";

        String mobile = null;
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setTimestamp(1,new Timestamp(new Date().getTime()));
            ps.setString(2, eventAudit.getType());
            ps.setString(3, eventAudit.getName());
            ps.setString(4, eventAudit.getSubject());
            ps.setString(5, eventAudit.getInfo());
             ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
