package com.sense.svcdmobile.connector;

import java.util.Hashtable;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InvalidSearchFilterException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import com.sense.svcdmobile.bean.ADUser;
import com.sense.svcdmobile.common.util.StringTool;
import org.apache.log4j.Logger;

public class ADConnector {
  private InitialLdapContext ldapconn;
  
  private Hashtable<String, Object> env;
  
  private Logger logger = Logger.getLogger(ADConnector.class);
  
  public ADConnector(String ldapurl, String userdn, String userpwd) {
    this.env = new Hashtable<>(8);
    this.env.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
    this.env.put("java.naming.security.authentication", "simple");
    this.env.put("java.naming.provider.url", ldapurl);
    this.env.put("java.naming.security.principal", userdn);
    this.env.put("java.naming.security.credentials", userpwd);
  }
  
  public String getSamaccountnamebyMail(String basedn, String mail) {
    if (StringTool.isEmpty(basedn) || StringTool.isEmpty(mail))
      return null; 
    String samaccountname = null;
    SearchControls cons = new SearchControls();
    cons.setSearchScope(2);
    cons.setReturningAttributes(new String[] { "samaccountname" });
    NamingEnumeration<SearchResult> res = null;
    SearchResult sr = null;
    try {
      res = this.ldapconn.search(basedn, "(mail=" + mail.trim() + ")", cons);
      while (res.hasMore()) {
        sr = res.next();
        samaccountname = (sr.getAttributes().get("samaccountname") == null) ? null : sr.getAttributes().get("samaccountname").get().toString();
      } 
    } catch (NamingException e) {
      e.printStackTrace();
      this.logger.error(e);
    } 
    return samaccountname;
  }
  
  public ADUser getAdUserbyMail(String basedn, String mail) {
    ADUser adUser = null;
    if (StringTool.isEmpty(basedn) || StringTool.isEmpty(mail))
      return null; 
    SearchControls cons = new SearchControls();
    cons.setSearchScope(2);
    cons.setReturningAttributes(new String[] { "samaccountname" });
    NamingEnumeration<SearchResult> res = null;
    SearchResult sr = null;
    try {
      res = this.ldapconn.search(basedn, "(&(objectclass=organizationalPerson)(mail=" + mail.trim() + "))", cons);
      while (res.hasMore()) {
        sr = res.next();
        Attributes attrs = sr.getAttributes();
        String samaccountname = (attrs.get("samaccountname") == null) ? null : attrs.get("samaccountname").get().toString();
        String department = (attrs.get("department") == null) ? null : attrs.get("department").get().toString();
        String info = (attrs.get("info") == null) ? null : attrs.get("info").get().toString();
        adUser = new ADUser();
        adUser.setMail(mail);
        adUser.setSamaccountname(samaccountname);
        adUser.setDepartment(department);
        adUser.setInfo(info);
      } 
    } catch (InvalidSearchFilterException e) {
      this.logger.error(e);
      this.logger.warn("(&(objectclass=organizationalPerson)(mail=" + mail.trim() + "))");
    } catch (NamingException e) {
      e.printStackTrace();
      this.logger.error(e);
    } 
    return adUser;
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
    ADConnector conn = new ADConnector("ldap://10.122.8.242:3268", "SVCD", "2wsx#EDC");
    conn.connect();
    String x = conn.getSamaccountnamebyMail("DC=csvw,DC=com", "denghan@csvw.com");
    System.out.println(x);
    conn.close();
  }
}
