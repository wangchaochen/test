package com.sense.svcdmobile.connector;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import org.apache.log4j.Logger;

public class LdapConnector {
  private InitialLdapContext ldapconn;
  
  private Hashtable<String, Object> env;
  
  private Logger logger = Logger.getLogger(LdapConnector.class);
  
  public LdapConnector() {
    this.env = new Hashtable<>(8);
    this.env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
    this.env.put("java.naming.security.authentication", "simple");
    this.env.put("java.naming.provider.url", "ldap://svldap00.csvw.com:389");
    this.env.put("java.naming.security.principal", "SVCD");
    this.env.put("java.naming.security.credentials", "2wsx#EDC");
  }
  
  public LdapConnector(String ldapurl, String userdn, String userpwd) {
    this.env = new Hashtable<>(8);
    this.env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
    this.env.put("java.naming.security.authentication", "simple");
    this.env.put("java.naming.provider.url", ldapurl);
    this.env.put("java.naming.security.principal", userdn);
    this.env.put("java.naming.security.credentials", userpwd);
  }
  
  public Map<String, String> getUserMail(String basedn) {
    Map<String, String> map = null;
    SearchControls cons = new SearchControls();
    cons.setSearchScope(1);
    cons.setReturningAttributes(new String[] { "uid", "mail" });
    NamingEnumeration<SearchResult> res = null;
    try {
      res = this.ldapconn.search(basedn, "(&(objectclass=inetorgPerson)(uid=*)(mail=*))", cons);
      if (res.hasMore()) {
        map = new HashMap<>();
        while (res.hasMore()) {
          SearchResult sr = res.next();
          String uid = (sr.getAttributes().get("uid") == null) ? null : sr.getAttributes().get("uid").get().toString().toUpperCase();
          if (uid == null || uid.trim().length() == 0)
            continue; 
          String mail = (sr.getAttributes().get("mail") == null) ? null : sr.getAttributes().get("mail").get().toString().toLowerCase();
          map.put(uid, mail);
        } 
        this.logger.info("Init HashMap<uid,mail> finished!{" + map.size() + "}");
      } 
    } catch (NamingException e1) {
      this.logger.error(e1);
    } finally {
      if (res != null)
        try {
          res.close();
        } catch (NamingException e) {
          this.logger.error(e);
        }  
    } 
    return map;
  }
  
  public Map<String, String> getEmpUserMail() {
    return getUserMail("cn=users, cn=employees, DC=CSVW,DC=COM");
  }
  
  public Map<String, String> getUserSimaccountid() {
    Map<String, String> map = null;
    SearchControls cons = new SearchControls();
    cons.setSearchScope(1);
    cons.setReturningAttributes(new String[] { "simaccountownerid", "simaccountid" });
    NamingEnumeration<SearchResult> res = null;
    try {
      res = this.ldapconn.search("cn=accounts, DC=CSVW,DC=COM", "(&(objectclass=SIMAccounts)(simappid=app_089)(simaccountownerid=*)(simaccountid=*))", cons);
      if (res.hasMore()) {
        map = new HashMap<>();
        while (res.hasMore()) {
          SearchResult sr = res.next();
          String simaccountownerid = (sr.getAttributes().get("simaccountownerid") == null) ? null : sr.getAttributes().get("simaccountownerid").get().toString().toUpperCase();
          if (simaccountownerid == null || simaccountownerid.trim().length() == 0)
            continue; 
          String simaccountid = (sr.getAttributes().get("simaccountid") == null) ? null : sr.getAttributes().get("simaccountid").get().toString().toLowerCase();
          map.put(simaccountownerid, simaccountid);
        } 
        this.logger.info("Init HashMap<simaccountownerid,simaccountid> finished!{" + map.size() + "}");
      } 
    } catch (NamingException e1) {
      this.logger.error(e1);
    } finally {
      if (res != null)
        try {
          res.close();
        } catch (NamingException e) {
          this.logger.error(e);
        }  
    } 
    return map;
  }
  
  public void connect() {
    try {
      this.logger.info("Connect to " + this.env.get("java.naming.provider.url") + " ......");
      this.ldapconn = new InitialLdapContext(this.env, null);
      this.logger.info("Connect to " + this.env.get("java.naming.provider.url") + " successfully!");
    } catch (NamingException e) {
      this.logger.error(e);
    } 
  }
  
  public void close() {
    if (this.ldapconn != null)
      try {
        this.logger.info("Disconnect to " + this.env.get("java.naming.provider.url") + " ......");
        this.ldapconn.close();
        this.logger.info("Disconnect to " + this.env.get("java.naming.provider.url") + " successfully!");
      } catch (NamingException e) {
        this.logger.error(e);
      }  
  }
  
  public static void main(String[] args) throws NamingException {
    LdapConnector conn = new LdapConnector("ldap://10.122.31.218:389/", "uid=ADDadmin, cn=appadmins, cn=apps, DC=CSVW,DC=COM", "ADDadmin");
    conn.connect();
    Map<String, String> map = conn.getEmpUserMail();
    Iterator<String> it = map.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      System.out.println(String.valueOf(key) + ":" + (String)map.get(key));
    } 
    conn.close();
  }
}
