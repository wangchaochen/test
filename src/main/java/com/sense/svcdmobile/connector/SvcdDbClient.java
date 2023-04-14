package com.sense.svcdmobile.connector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sense.svcdmobile.bean.EmpUser;
import com.sense.svcdmobile.common.util.SecurityUtil;
import org.apache.log4j.Logger;

public class SvcdDbClient {
  private Connection ct = null;
  
  private Logger logger = Logger.getLogger(SvcdDbClient.class);
  
  private String dburl = null;
  
  private String username = null;
  
  private String password = null;
  
  public SvcdDbClient(String dburl, String username, String password) {
    this.dburl = dburl;
    this.username = username;
    this.password = password;
  }
  
  public void connect() {
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      this.logger.info("Connect to " + this.dburl + " ......");
      this.ct = DriverManager.getConnection(this.dburl, this.username, this.password);
      this.logger.info("Connect to " + this.dburl + " successfully!");
    } catch (SQLException var2) {
      this.logger.error(var2);
    } catch (ClassNotFoundException var3) {
      this.logger.error(var3);
    } 
  }
  
  public void createUser(EmpUser empUser) {
    PreparedStatement ps = null;
    try {
      ps = this.ct.prepareStatement("insert into CMIT.TM_SVCD_USER_MOBILE (USERID,MAIL,SAMACCOUNTNAME,MOBILE,STATUS,CREATETIME,MODIFYETIME) values(?,?,?,?,?,SYSDATE,SYSDATE)");
      ps.setString(1, empUser.getUid());
      ps.setString(2, empUser.getMail());
      ps.setString(3, empUser.getSamaccountname());
      ps.setString(4, SecurityUtil.enCode(empUser.getMobile()));
      ps.setString(5, empUser.getStatus());
      if (ps.executeUpdate() == 1) {
        this.logger.info("insert " + empUser.getUid() + " into CMIT.TM_SVCD_USER_MOBILE successfully!");
      } else {
        this.logger.info("insert " + empUser.getUid() + " into CMIT.TM_SVCD_USER_MOBILE error!");
      } 
    } catch (SQLException var12) {
      this.logger.error(var12);
    } finally {
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException var11) {
          this.logger.error(var11);
        }  
    } 
  }
  
  public void modifyUser(EmpUser empUser) {
    PreparedStatement ps = null;
    try {
      ps = this.ct.prepareStatement("update CMIT.TM_SVCD_USER_MOBILE set SAMACCOUNTNAME=?,MAIL=?,MOBILE=?,STATUS=? where USERID=?");
      ps.setString(1, empUser.getSamaccountname());
      ps.setString(2, empUser.getMail());
      ps.setString(3, SecurityUtil.enCode(empUser.getMobile()));
      ps.setString(4, empUser.getStatus());
      ps.setString(5, empUser.getUid());
      if (ps.executeUpdate() == 1) {
        this.logger.info("Update " + empUser.getUid() + " in CMIT.TM_SVCD_USER_MOBILE successfully!");
      } else {
        this.logger.info("Update " + empUser.getUid() + " in CMIT.TM_SVCD_USER_MOBILE error!");
      } 
    } catch (SQLException var12) {
      this.logger.error(var12);
    } finally {
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException var11) {
          this.logger.error(var11);
        }  
    } 
  }
  
  public void createUser(List<EmpUser> list) {
    Iterator<EmpUser> var3 = list.iterator();
    while (var3.hasNext()) {
      EmpUser empUser = var3.next();
      createUser(empUser);
    } 
  }
  
  public void modifyUser(List<EmpUser> list) {
    Iterator<EmpUser> var3 = list.iterator();
    while (var3.hasNext()) {
      EmpUser empUser = var3.next();
      modifyUser(empUser);
    } 
  }
  
  public void getUserInfo(String uid, String samaccountname, String mail) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    if (uid != null && uid.trim().length() > 0) {
      try {
        ps = this.ct.prepareStatement("select USERID,MAIL,SAMACCOUNTNAME,MOBILE,STATUS from CMIT.TM_SVCD_USER_MOBILE where lower(userid)=?");
        ps.setString(1, uid.toLowerCase());
        rs = ps.executeQuery();
        while (rs.next()) {
          String x1 = rs.getString("USERID");
          String x2 = rs.getString("MAIL");
          String x3 = rs.getString("SAMACCOUNTNAME");
          String x4en = rs.getString("MOBILE");
          String x4de = SecurityUtil.deCode(x4en);
          String x5 = rs.getString("STATUS");
          System.out.println(x1 + "," + x2 + "," + x3 + "," + x4de + "," + x5);
        } 
      } catch (SQLException var84) {
        var84.printStackTrace();
      } finally {
        if (rs != null)
          try {
            rs.close();
          } catch (SQLException var79) {
            var79.printStackTrace();
          }  
        if (ps != null)
          try {
            ps.close();
          } catch (SQLException var78) {
            var78.printStackTrace();
          }  
      } 
    } else if (samaccountname != null && samaccountname.trim().length() > 0) {
      try {
        ps = this.ct.prepareStatement("select USERID,MAIL,SAMACCOUNTNAME,MOBILE,STATUS from CMIT.TM_SVCD_USER_MOBILE where lower(samaccountname)=?");
        ps.setString(1, samaccountname.toLowerCase());
        rs = ps.executeQuery();
        while (rs.next()) {
          String x1 = rs.getString("USERID");
          String x2 = rs.getString("MAIL");
          String x3 = rs.getString("SAMACCOUNTNAME");
          String x4en = rs.getString("MOBILE");
          String x4de = SecurityUtil.deCode(x4en);
          String x5 = rs.getString("STATUS");
          System.out.println(x1 + "," + x2 + "," + x3 + "," + x4de + "," + x5);
        } 
      } catch (SQLException var82) {
        var82.printStackTrace();
      } finally {
        if (rs != null)
          try {
            rs.close();
          } catch (SQLException var75) {
            var75.printStackTrace();
          }  
        if (ps != null)
          try {
            ps.close();
          } catch (SQLException var74) {
            var74.printStackTrace();
          }  
      } 
    } else {
      if (samaccountname == null || samaccountname.trim().length() <= 0)
        return; 
      try {
        ps = this.ct.prepareStatement("select USERID,MAIL,SAMACCOUNTNAME,MOBILE,STATUS from CMIT.TM_SVCD_USER_MOBILE where lower(mail)=?");
        ps.setString(1, mail.toLowerCase());
        rs = ps.executeQuery();
        while (rs.next()) {
          String x1 = rs.getString("USERID");
          String x2 = rs.getString("MAIL");
          String x3 = rs.getString("SAMACCOUNTNAME");
          String x4en = rs.getString("MOBILE");
          String x4de = SecurityUtil.deCode(x4en);
          String x5 = rs.getString("STATUS");
          System.out.println(x1 + "," + x2 + "," + x3 + "," + x4de + "," + x5);
        } 
      } catch (SQLException var80) {
        var80.printStackTrace();
      } finally {
        if (rs != null)
          try {
            rs.close();
          } catch (SQLException var77) {
            var77.printStackTrace();
          }  
        if (ps != null)
          try {
            ps.close();
          } catch (SQLException var76) {
            var76.printStackTrace();
          }  
      } 
    } 
  }
  
  public Map<String, EmpUser> getAllUser() {
    Map<String, EmpUser> map = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      map = new HashMap<>();
      ps = this.ct.prepareStatement("select USERID,MAIL,SAMACCOUNTNAME,MOBILE,STATUS from CMIT.TM_SVCD_USER_MOBILE");
      rs = ps.executeQuery();
      while (rs.next()) {
        EmpUser empUser = new EmpUser();
        empUser.setUid(rs.getString("USERID"));
        empUser.setSamaccountname(rs.getString("SAMACCOUNTNAME"));
        empUser.setMail(rs.getString("MAIL"));
        empUser.setMobile(SecurityUtil.deCode(rs.getString("MOBILE")));
        empUser.setStatus(rs.getString("STATUS"));
        map.put(rs.getString("USERID"), empUser);
      } 
    } catch (SQLException var17) {
      this.logger.error(var17);
    } finally {
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException var16) {
          this.logger.error(var16);
        }  
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException var15) {
          this.logger.error(var15);
        }  
    } 
    return map;
  }
  
  public void sync(List<EmpUser> list) {
    Map<String, EmpUser> map = getAllUser();
    List<EmpUser> crtlist = new ArrayList<>();
    List<EmpUser> modlist = new ArrayList<>();
    Iterator<EmpUser> var6 = list.iterator();
    while (var6.hasNext()) {
      EmpUser empUser = var6.next();
      if (!map.containsKey(empUser.getUid())) {
        crtlist.add(empUser);
        System.out.println("�������û�:"+ empUser.getUid());
        continue;
      } 
      EmpUser olduser = map.get(empUser.getUid());
      if (!empUser.equals(olduser)) {
        modlist.add(empUser);
        System.out.println("���޸��û�:"+ empUser.getUid());
      } 
    } 
  }
  
  public boolean isExist(String userid) {
    boolean res = false;
    if (userid == null)
      return false; 
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = this.ct.prepareStatement("select USERID,MAIL,SAMACCOUNTNAME,MOBILE,STATUS from CMIT.TM_SVCD_USER_MOBILE where upper(USERID)= ?");
      ps.setString(1, userid.toUpperCase());
      rs = ps.executeQuery();
      if (rs.next())
        res = true; 
    } catch (SQLException var18) {
      var18.printStackTrace();
    } finally {
      if (rs != null)
        try {
          rs.close();
        } catch (SQLException var17) {
          var17.printStackTrace();
        }  
      if (ps != null)
        try {
          ps.close();
        } catch (SQLException var16) {
          var16.printStackTrace();
        }  
    } 
    return res;
  }
  
  public void sync2(List<EmpUser> list) {
    Map<String, EmpUser> map = getAllUser();
    List<EmpUser> crtlist = new ArrayList<>();
    List<EmpUser> modlist = new ArrayList<>();
    Iterator<EmpUser> var6 = list.iterator();
    while (var6.hasNext()) {
      EmpUser empUser = var6.next();
      if (!map.containsKey(empUser.getUid())) {
        crtlist.add(empUser);
        System.out.println("�������û�:"+ empUser.getUid());
        continue;
      } 
      EmpUser olduser = map.get(empUser.getUid());
      if (!empUser.equals(olduser)) {
        modlist.add(empUser);
        System.out.println("���޸��û�:"+ empUser.getUid());
      } 
    } 
  }
  
  public void close() {
    try {
      this.ct.close();
    } catch (SQLException var2) {
      this.logger.error(var2);
    } 
  }
  
  public static void main(String[] args) throws IOException {
    SvcdDbClient client = new SvcdDbClient("jdbc:oracle:thin:@sxat-scan.csvw.com:1521/cmdb", "CMIT", "Svw12345");
    client.connect();
    FileReader fr = new FileReader("E:\\temp\\ext.csv");
    BufferedReader br = new BufferedReader(fr);
    String temp = null;
    while ((temp = br.readLine()) != null) {
      String[] sx = temp.split(",");
      String uid = sx[0];
      String mobile = sx[1];
      String samaccountname = sx[2];
      String mail = sx[3].trim();
      System.out.println(uid + ">>" + mobile + ">>" + samaccountname + ">>" + mail);
      EmpUser empUser = new EmpUser();
      empUser.setUid(uid.toUpperCase());
      empUser.setMail(mail.toUpperCase() + "@csvw.com");
      empUser.setSamaccountname(samaccountname.toUpperCase().trim());
      empUser.setMobile(mobile.trim());
      empUser.setStatus("3");
      if (client.isExist(uid)) {
        client.modifyUser(empUser);
        continue;
      } 
      client.createUser(empUser);
    } 
    br.close();
    fr.close();
    client.close();
  }
}
