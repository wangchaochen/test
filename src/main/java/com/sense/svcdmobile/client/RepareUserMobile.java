package com.svw.client;

import com.sense.svcdmobile.bean.EmpUser;
import com.sense.svcdmobile.connector.SapConnector;
import com.sense.svcdmobile.connector.SvcdDbClient;

import java.util.Iterator;
import java.util.Map;

public class RepareUserMobile {
  public static void main(String[] args) {
    SapConnector sapconn = new SapConnector("R3", "Mfa123456", "10.122.4.139", "01", "RFC_MFA");
    sapconn.connection();
    Map<String, String> sapmap = sapconn.getAllUserMobile(true);
    sapconn.close();
    SvcdDbClient client = new SvcdDbClient("jdbc:oracle:thin:@racapscan.csvw.com:1521/svwop", "cmit", "Svw12345");
    client.connect();
    Map<String, EmpUser> svcddbmap = client.getAllUser();
    Iterator<String> it = sapmap.keySet().iterator();
    String uid = null;
    int count = 0;
    while (it.hasNext()) {
      uid = it.next();
      if (!svcddbmap.containsKey(uid)) {
        System.out.println("不存在，建议创建>"+ uid + ">" + (String)sapmap.get(uid));
        continue;
      }
      String sapmobile = sapmap.get(uid);
      EmpUser empUser = svcddbmap.get(uid);
      String svcdmobie = empUser.getMobile();
      if (sapmobile != null && sapmobile.trim().length() > 0 && !sapmobile.equals(svcdmobie))
        if (((EmpUser)svcddbmap.get(uid)).getSamaccountname() != null) {
          System.out.println("存在，但是电话发生，建议修改>"+ uid + ">新电话:"+ (String)sapmap.get(uid) + "旧电话:"+ svcdmobie + "域账号:"+ ((EmpUser)svcddbmap.get(uid)).getSamaccountname());
          empUser.setMobile(sapmap.get(uid));
          count++;
        }
    }
    System.out.println(count);
    client.close();
  }
}
