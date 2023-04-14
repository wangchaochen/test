package com.svw.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sense.svcdmobile.bean.ADUser;
import com.sense.svcdmobile.bean.EmpUser;
import com.sense.svcdmobile.connector.ADConnector;
import com.sense.svcdmobile.connector.LdapConnector;
import com.sense.svcdmobile.connector.SapConnector;
import com.sense.svcdmobile.connector.SvcdDbClient;
import org.apache.log4j.Logger;

public class SyncSapUserMobile {
  private Logger logger = Logger.getLogger(SyncSapUserMobile.class);

  private SapConnector sapconn = new SapConnector("R3", "Mfa123456", "10.122.4.139", "01", "RFC_MFA");

  private SvcdDbClient svcdclient = new SvcdDbClient("jdbc:oracle:thin:@sxmp-scan.csvw.com:1521/CMDB", "cmit", "Svw12345");

  private LdapConnector ldapconn = new LdapConnector("ldap://10.122.31.218:389", "cn=root", "paSC3wsRs$@orD#m");

  private ADConnector adconn = new ADConnector("ldap://10.122.8.242:3268", "SVCD", "2wsx#EDC");

  public void execSync(List<EmpUser> addlist, List<EmpUser> modlist) {
    this.sapconn.connection();
    List<EmpUser> sapuserlist = this.sapconn.getAllEmpUserMobile();
    this.sapconn.close();
    this.logger.info("从SAP获取到["+ sapuserlist.size() + "]条数据");
    this.ldapconn.connect();
    Map<String, String> user_mail_map = this.ldapconn.getEmpUserMail();
    this.logger.info("从LDAP获取到["+ user_mail_map.size() + "]条uid&mail映射数据");
    Map<String, String> user_acctid_map = this.ldapconn.getUserSimaccountid();
    this.logger.info("从LDAP获取到["+ user_acctid_map.size() + "]条simaccountiwnerid&simaccountid映射数据.");
    this.ldapconn.close();
    this.svcdclient.connect();
    Map<String, EmpUser> svcdusermap = this.svcdclient.getAllUser();
    this.svcdclient.close();
    this.adconn.connect();
    int sameccount = 0;
    ADUser adUser = null;
    this.logger.info("开始处理："+ sapuserlist.size() + "条uid&mail映射数据");
    for (EmpUser sapempUser : sapuserlist) {
      String sapuid = sapempUser.getUid();
      this.logger.info("开始处理："+ sapuid + "");
      if (!svcdusermap.containsKey(sapuid)) {
        this.logger.info("!svcdusermap.containsKey(sapuid)" + sapuid + "");
        if ("3".equals(sapempUser.getStatus())) {
          this.logger.info("3.equals(sapempUser.getStatus()" + sapuid + "");
          String str1 = user_mail_map.get(sapuid);
          sapempUser.setMail(user_mail_map.get(sapuid));
          String simaccountid = user_acctid_map.get(sapuid);
          adUser = this.adconn.getAdUserbyMail("DC=csvw,DC=com", str1);
          if (adUser != null) {
            sapempUser.setSamaccountname(adUser.getSamaccountname());
          } else {
            sapempUser.setSamaccountname(simaccountid);
          }
          this.logger.info("addlist.add(sapempUser);" + sapempUser + "start");
          addlist.add(sapempUser);
          this.logger.info("addlist.add(sapempUser);" + sapempUser + "end");
        }
        continue;
      }
      String ldapmail = user_mail_map.get(sapuid);
      this.logger.info("ldapmail:" + ldapmail + "");
      sapempUser.setMail(ldapmail);
      adUser = this.adconn.getAdUserbyMail("DC=csvw,DC=com", ldapmail);
      if (adUser != null) {
        this.logger.info("adUser != null");
        sapempUser.setSamaccountname(adUser.getSamaccountname());
      } else {
        this.logger.info("adUser == null");
        sapempUser.setSamaccountname(user_acctid_map.get(sapuid));
      }
      EmpUser svcdempUser = svcdusermap.get(sapuid);
      this.logger.info("svcdempUser" + svcdempUser.getUid() + "");
      if (svcdempUser.equals(sapempUser)) {
        sameccount++;
        continue;
      }
      this.logger.info("modlist.add(sapempUser);" + svcdempUser.getUid() + " start");
      modlist.add(sapempUser);
      this.logger.info("modlist.add(sapempUser);" + svcdempUser.getUid() + " end");
    }
    this.adconn.close();
    this.logger.info("需要创建的有效用户数量："+ addlist.size());
    this.logger.info("需要修改的有效用户数量："+ modlist.size());
    this.logger.info("不需要修改的有效数量："+ sameccount);
    this.svcdclient.close();
  }

  public void UpdateData(List<EmpUser> addlist, List<EmpUser> modlist) {
    this.svcdclient.connect();
    if (addlist != null)
      for (EmpUser empUser : addlist) {
        this.logger.info("Create {" + empUser.getUid() + "," + empUser.getSamaccountname() + "," + empUser.getMobile() + "}");
        this.svcdclient.createUser(empUser);
      }
    if (modlist != null)
      for (EmpUser empUser : modlist) {
        this.logger.info("Modify {" + empUser.getUid() + "," + empUser.getSamaccountname() + "," + empUser.getMobile() + "}");
        if (empUser.getMobile() == null || empUser.getMobile().trim().length() != 11)
          continue;
        this.svcdclient.modifyUser(empUser);
      }
    this.svcdclient.close();
  }

  public static void main(String[] args) {
    SyncSapUserMobile ss = new SyncSapUserMobile();
    List<EmpUser> addlist = null;
    List<EmpUser> modlist = null;
    addlist = new ArrayList<>();
    modlist = new ArrayList<>();
    ss.execSync(addlist, modlist);
    System.out.println("=========1==========");
    for (EmpUser emp : addlist)
      System.out.println(String.valueOf(emp.getUid()) + ">" + emp.getMail() + ">" + emp.getSamaccountname() + ">" + emp.getMobile());
    System.out.println("========2===========");
    for (EmpUser emp : modlist)
      System.out.println(String.valueOf(emp.getUid()) + ">" + emp.getMail() + ">" + emp.getSamaccountname() + ">" + emp.getMobile());
    ss.UpdateData(addlist, modlist);
  }
}
