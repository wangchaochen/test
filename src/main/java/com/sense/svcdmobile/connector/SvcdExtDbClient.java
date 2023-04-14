package com.sense.svcdmobile.connector;

import com.sense.svcdmobile.bean.ExtUser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class SvcdExtDbClient {
  private Connection ct = null;
  
  private String dburl = null;
  
  private String username = null;
  
  private String password = null;
  
  public SvcdExtDbClient(String dburl, String username, String password) {
    this.dburl = dburl;
    this.username = username;
    this.password = password;
  }
  
  public void connect() {
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      this.ct = DriverManager.getConnection(this.dburl, this.username, this.password);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } 
  }
  
  public void createExtUser(ExtUser extUser) {
    PreparedStatement ps = null;
    try {
      ps = this.ct.prepareStatement("insert into CMIT.tb_ext_user (ID,LOGIN_NAME,USER_NAME,DEPT_NAME,CREATOR,CREATE_DATE,UPDATOR,IS_VALID,SUR_NAME,NAME,ID_NO,EMAIL) values(?,?,?,?,?,SYSDATE,?,?,?,?,?,?)");
      String id = UUID.randomUUID().toString().replaceAll("-", "");
      ps.setString(1, id);
      ps.setString(2, extUser.getUserid());
      ps.setString(3, extUser.getUsername());
      ps.setString(4, extUser.getDept());
      ps.setString(5, "SARS-CoV-2");
      ps.setString(6, "SARS-CoV-2");
      ps.setString(7, "0");
      ps.setString(8, extUser.getUsername());
      ps.setString(9, extUser.getUsername());
      ps.setString(10, extUser.getSvwepid());
      ps.setString(11, "ci-" + extUser.getUserid() + "@csvw.com");
      System.out.println();
      if (ps.executeUpdate() == 1) {
        System.out.println("insert " + extUser.getUserid() + " into CMIT.TM_SVCD_USER_MOBILE successfully!");
      } else {
        System.out.println("insert " + extUser.getUserid() + " into CMIT.TM_SVCD_USER_MOBILE error!");
      } 
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
    } 
  }
  
  public void createExtUser2(ExtUser extUser) {
    PreparedStatement ps = null;
    try {
      ps = this.ct.prepareStatement("insert into CMIT.tb_ext_user_change_log (SERIAL_NUMBER,LOGIN_NAME,USER_NAME,DEPT_NAME,CREATOR,CREATE_DATE,UPDATOR,IS_VALID,SUR_NAME,NAME,ID_NO,EMAIL) values(SEQ_TEUCL.NEXTVAL,?,?,?,?,SYSDATE,?,?,?,?,?,?)");
      String id = UUID.randomUUID().toString().replaceAll("-", "");
      ps.setString(1, extUser.getUserid());
      ps.setString(2, extUser.getUsername());
      ps.setString(3, extUser.getDept());
      ps.setString(4, "SARS-CoV-2");
      ps.setString(5, "SARS-CoV-2");
      ps.setString(6, "0");
      ps.setString(7, extUser.getUsername());
      ps.setString(8, extUser.getUsername());
      ps.setString(9, extUser.getSvwepid());
      ps.setString(10, "ci-" + extUser.getUserid() + "@csvw.com");
      System.out.println();
      if (ps.executeUpdate() == 1) {
        System.out.println("insert " + extUser.getUserid() + " into CMIT.TM_SVCD_USER_MOBILE successfully!");
      } else {
        System.out.println("insert " + extUser.getUserid() + " into CMIT.TM_SVCD_USER_MOBILE error!");
      } 
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }  
    } 
  }
  
  public void close() {
    try {
      this.ct.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
  
  public static void main(String[] args) throws IOException {
    SvcdExtDbClient client = new SvcdExtDbClient("jdbc:oracle:thin:@racatscan.csvw.com:1521/racat", "cmit", "Svw12345");
    client.connect();
    ExtUser extUser = new ExtUser();
    extUser.setUserid("EXT98795");
    extUser.setSvwepid("411402199702126752");
    extUser.setDept("CIP");
    client.createExtUser(extUser);
    client.createExtUser2(extUser);
    client.close();
  }
}
