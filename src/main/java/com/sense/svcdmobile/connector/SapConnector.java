package com.sense.svcdmobile.connector;

import com.csvw.sap.SapClient;
import com.sap.mw.jco.JCO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sense.svcdmobile.bean.EmpUser;
import org.apache.log4j.Logger;

public class SapConnector {
  SapClient sc = null;

  private String sid;

  private String pwd;

  private String host;

  private String sn;

  private String uid;

  private Logger logger;

  public SapConnector(String sid, String pwd, String host, String sn, String uid) {
    this.sid = sid;
    this.pwd = pwd;
    this.host = host;
    this.sn = sn;
    this.uid = uid;
    this.logger = Logger.getLogger(SapConnector.class);
  }

  public void connection() {
    try {
      this.sc = new SapClient(this.sid, 10, "200", this.uid, this.pwd, "ZH", this.host, this.sn);
      this.logger.info("Connect to sap +{" + this.host + "} successfully!");
    } catch (Exception e) {
      this.logger.error("Connect to sap +{" + this.host + "} failed!");
      e.printStackTrace();
    }
  }

  private List<Map<String, String>> search(String functionName, String tableName, HashMap<String, String> params) {
    List<Map<String, String>> list = new ArrayList<>();
    JCO.Table table = null;
    try {
      table = this.sc.execute(functionName, tableName, params);
      if (table != null && table.getNumRows() > 0)
        do {
          Map<String, String> map = new HashMap<>();
          for (JCO.FieldIterator e = table.fields(); e.hasMoreElements(); ) {
            JCO.Field field = e.nextField();
            if (field.getValue() instanceof String) {
              map.put(field.getName(), (String)field.getValue());
              continue;
            }
            if (field.getValue() instanceof java.util.Date) {
              map.put(field.getName(), (new SimpleDateFormat("yyyy-MM-dd")).format(field.getValue()));
              continue;
            }
            map.put(field.getName(), "unknown type");
          }
          list.add(map);
        } while (table.nextRow());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (table != null)
        table.clear();
    }
    return list;
  }

  public void close() {
    if (this.sc != null)
      this.sc.cleanUp();
  }

  public List<EmpUser> getEmpUserMobile(String uid_prefix, int uid_length) {
    String functionName = "ZHRS_MFA_SYNC_PERSON";
    String tableName = "ZHRSPERSON_INFO";
    if (uid_prefix == null || uid_prefix.trim().length() == 0)
      return null;
    List<EmpUser> userlist = null;
    HashMap<String, String> hashmap = new HashMap<>();
    hashmap.put("I_PERNR", uid_prefix.trim());
    hashmap.put("I_PERNR_NUM", String.valueOf(uid_length));
    List<Map<String, String>> sapuserlist = search(functionName, tableName, hashmap);
    if (sapuserlist != null && sapuserlist.size() > 0) {
      userlist = new ArrayList<>();
      for (Map<String, String> map : sapuserlist) {
        String uid = ((String)map.get("PERNR")).replaceAll("^0*", "");
        String mobile = map.get("NMOBILE");
        String status = map.get("STAT2");
        String name = map.get("NAME");
        EmpUser empuser = new EmpUser();
        empuser.setUid(uid);
        empuser.setMobile(mobile);
        empuser.setStatus(status);
        empuser.setName(name);
        userlist.add(empuser);
      }
    }
    this.logger.info("通过("+ uid_prefix + "," + uid_length + ")获取到"+ userlist.size() + "条手机数据");
    return userlist;
  }

  public List<EmpUser> getAllEmpUserMobile() {
    String functionName = "ZHRS_MFA_SYNC_PERSON";
    String tableName = "ZHRSPERSON_INFO";
    HashMap<String, String> params = new HashMap<>();
    List<EmpUser> usermobilelist = null;
    List<Map<String, String>> templist = null;
    List<Map<String, String>> totallist = null;
    totallist = new ArrayList<>(81920);
    for (int i = 1; i <= 9; i++) {
      params.put("I_PERNR", String.valueOf(i));
      params.put("I_PERNR_NUM", "4");
      templist = search(functionName, tableName, params);
      this.logger.info("通过("+ String.valueOf(i) + "," + '\004' + ")获取到"+ templist.size() + "条手机数据");
      totallist.addAll(templist);
      params.put("I_PERNR", String.valueOf(i));
      params.put("I_PERNR_NUM", "5");
      templist = search(functionName, tableName, params);
      this.logger.info("通过("+ String.valueOf(i) + "," + '\005' + ")获取到"+ templist.size() + "条手机数据");
      totallist.addAll(templist);
      params.put("I_PERNR", String.valueOf(i));
      params.put("I_PERNR_NUM", "6");
      templist = search(functionName, tableName, params);
      this.logger.info("通过("+ String.valueOf(i) + "," + '\006' + ")获取到"+ templist.size() + "条手机数据");
      totallist.addAll(templist);
    }
    usermobilelist = new ArrayList<>(81920);
    for (Map<String, String> userentry : totallist) {
      String uid = ((String)userentry.get("PERNR")).replaceAll("^0*", "");
      String mobile = (userentry.get("NMOBILE") == null) ? "" : ((String)userentry.get("NMOBILE")).trim();
      String name = (userentry.get("NAME") == null) ? "" : ((String)userentry.get("NAME")).trim();
      String status = (userentry.get("STAT2") == null) ? "" : ((String)userentry.get("STAT2")).trim();
      EmpUser empuser = new EmpUser();
      empuser.setUid(uid);
      empuser.setMobile(mobile);
      empuser.setName(name);
      empuser.setStatus(status);
      usermobilelist.add(empuser);
    }
    return usermobilelist;
  }

  public Map<String, String> getAllUserMobile(boolean isActive) {
    Map<String, String> usermobilemap = new HashMap<>();
    String functionName = "ZHRS_MFA_SYNC_PERSON";
    String tableName = "ZHRSPERSON_INFO";
    HashMap<String, String> params = new HashMap<>();
    List<Map<String, String>> templist = null;
    List<Map<String, String>> tatallist = new ArrayList<>(81920);
    for (int i = 1; i <= 9; i++) {
      params.put("I_PERNR", String.valueOf(i));
      params.put("I_PERNR_NUM", "4");
      templist = search(functionName, tableName, params);
      tatallist.addAll(templist);
      params.put("I_PERNR", String.valueOf(i));
      params.put("I_PERNR_NUM", "5");
      templist = search(functionName, tableName, params);
      tatallist.addAll(templist);
      params.put("I_PERNR", String.valueOf(i));
      params.put("I_PERNR_NUM", "6");
      templist = search(functionName, tableName, params);
      tatallist.addAll(templist);
    }
    for (Map<String, String> userentry : tatallist) {
      if (isActive) {
        String str1 = ((String)userentry.get("PERNR")).replaceAll("^0*", "").trim();
        String str2 = (userentry.get("NMOBILE") == null) ? "" : ((String)userentry.get("NMOBILE")).trim();
        String stat2 = (userentry.get("STAT2") == null) ? "" : ((String)userentry.get("STAT2")).trim();
        if ("3".equals(stat2))
          usermobilemap.put(str1, str2);
        continue;
      }
      String uid = ((String)userentry.get("PERNR")).replaceAll("^0*", "").trim();
      String mobile = (userentry.get("NMOBILE") == null) ? "" : ((String)userentry.get("NMOBILE")).trim();
      usermobilemap.put(uid, mobile);
    }
    if (isActive) {
      this.logger.info("获取"+ usermobilemap.size() + "条stat2=3的数据");
    } else {
      this.logger.info("获取"+ usermobilemap.size() + "条所有的数据");
    }
    return usermobilemap;
  }

  public static void main(String[] args) {
    SapConnector sapconn = new SapConnector("R3", "Mfa123456", "10.122.4.139", "01", "RFC_MFA");
    sapconn.connection();
    List<EmpUser> list1 = sapconn.getEmpUserMobile("13378", 5);
    List<EmpUser> list2 = sapconn.getEmpUserMobile("6652", 5);
    sapconn.close();
    for (EmpUser empuser : list1)
      System.out.println(String.valueOf(empuser.getUid()) + " | " + empuser.getName() + " | " + empuser.getMobile() + " | " + empuser.getStatus());
    System.out.println("=========================");
    for (EmpUser empuser : list2)
      System.out.println(String.valueOf(empuser.getUid()) + " | " + empuser.getName() + " | " + empuser.getMobile() + " | " + empuser.getStatus());
    sapconn.close();
  }
}
