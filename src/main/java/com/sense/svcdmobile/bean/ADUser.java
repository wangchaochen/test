package com.sense.svcdmobile.bean;

public class ADUser {
  private String samaccountname;
  
  private String mail;
  
  private String department;
  
  private String info;
  
  public String getSamaccountname() {
    return this.samaccountname;
  }
  
  public void setSamaccountname(String samaccountname) {
    this.samaccountname = samaccountname;
  }
  
  public String getMail() {
    return this.mail;
  }
  
  public void setMail(String mail) {
    this.mail = mail;
  }
  
  public String getDepartment() {
    return this.department;
  }
  
  public void setDepartment(String department) {
    this.department = department;
  }
  
  public String getInfo() {
    return this.info;
  }
  
  public void setInfo(String info) {
    this.info = info;
  }
}
