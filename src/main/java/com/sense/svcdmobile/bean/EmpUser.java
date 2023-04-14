package com.sense.svcdmobile.bean;

import com.sense.svcdmobile.common.util.StringTool;
import org.apache.log4j.Logger;

public class EmpUser {
  private String uid;
  
  private String status;
  
  private String mobile;
  
  private String mail;
  
  private String name;
  
  private Logger logger = Logger.getLogger(EmpUser.class);
  
  private String samaccountname;
  
  public String getUid() {
    return this.uid;
  }
  
  public void setUid(String uid) {
    this.uid = uid;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getMobile() {
    return this.mobile;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  public String getMail() {
    return this.mail;
  }
  
  public void setMail(String mail) {
    this.mail = mail;
  }
  
  public String getSamaccountname() {
    return this.samaccountname;
  }
  
  public void setSamaccountname(String samaccountname) {
    this.samaccountname = samaccountname;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (obj instanceof EmpUser) {
      EmpUser ex = (EmpUser)obj;
      boolean b1 = false;
      if (getUid() == null) {
        if (ex.getUid() == null)
          b1 = true; 
      } else if (getUid().equals(ex.getUid())) {
        b1 = true;
      } 
      boolean b2 = false;
      if (getSamaccountname() == null) {
        if (ex.getSamaccountname() == null)
          b2 = true; 
      } else if (getSamaccountname().equals(ex.getSamaccountname())) {
        b2 = true;
      } 
      boolean b3 = false;
      if (getMail() == null) {
        if (ex.getMail() == null)
          b3 = true; 
      } else if (getMail().equals(ex.getMail())) {
        b3 = true;
      } 
      boolean b4 = false;
      if (getMobile() == null) {
        if (ex.getMobile() == null)
          b4 = true; 
      } else if (getMobile().equals(ex.getMobile())) {
        b4 = true;
      } 
      boolean b5 = false;
      if (getStatus() == null) {
        if (ex.getStatus() == null)
          b5 = true; 
      } else if (getStatus().equals(ex.getStatus())) {
        b5 = true;
      } 
      if (b1 && b2 && b3 && b4 && b5)
        return true; 
      this.logger.info("[b1]uid:{" + getUid() + "}{" + ex.getUid() + "}");
      this.logger.info("[b2]samaccountname:{" + getSamaccountname() + "}{" + ex.getSamaccountname() + "}");
      this.logger.info("[b3]mail:{" + getMail() + "}{" + ex.getMail() + "}");
      this.logger.info("[b4]mobile:{" + StringTool.getInvisibleMobile(getMobile()) + "}{" + StringTool.getInvisibleMobile(ex.getMobile()) + "}");
      this.logger.info("[b5]status:{" + getStatus() + "}{" + ex.getStatus() + "}");
      return false;
    } 
    return false;
  }
}
