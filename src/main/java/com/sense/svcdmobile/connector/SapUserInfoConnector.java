package com.sense.svcdmobile.connector;

import com.csvw.sap.SapClient;
import com.sap.mw.jco.JCO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SapUserInfoConnector {
  SapClient sc = null;
  
  private String sid;
  
  private String pwd;
  
  private String host;
  
  private String sn;
  
  private String uid;
  
  public SapUserInfoConnector(String sid, String pwd, String host, String sn, String uid) {
    this.sid = sid;
    this.pwd = pwd;
    this.host = host;
    this.sn = sn;
    this.uid = uid;
  }
  
  public void connection() {
    try {
      this.sc = new SapClient(this.sid, 10, "200", this.uid, this.pwd, "ZH", this.host, this.sn);
    } catch (Exception e) {
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
  
  public void printEmpUsers(String uid_prefix, int uid_length) {
    String functionName = "ZHRS_SYNC_PERSON";
    String tableName = "ZHRSPERSON_INFO";
    HashMap<String, String> hashmap = new HashMap<>();
    hashmap.put("I_PERNR", uid_prefix.trim());
    hashmap.put("I_PERNR_NUM", String.valueOf(uid_length));
    List<Map<String, String>> sapuserlist = search(functionName, tableName, hashmap);
    if (sapuserlist != null && sapuserlist.size() > 0)
      for (Map<String, String> map : sapuserlist) {
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
          String temp = it.next();
          System.out.println(String.valueOf(temp) + ":" + (String)map.get(temp));
        } 
      }  
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>." + sapuserlist.size());
  }
  
  public static void main(String[] args) {
    SapUserInfoConnector sapconn = new SapUserInfoConnector("R3", "cIa3)5", "10.122.4.139", "01", "rfc");
    sapconn.connection();
    sapconn.printEmpUsers("58668", 5);
    sapconn.close();
  }
}
