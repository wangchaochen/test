package com.sense.svcdmobile.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Config {
  private Properties pp = null;
  
  private File configfile = null;
  
  private FileInputStream fis = null;
  
  public Config(String cfgfilepath) {
    this.configfile = new File(cfgfilepath);
    if (this.configfile.exists()) {
      Logger.getLogger(Config.class).info("Load config file " + this.configfile.getAbsolutePath() + "...");
      LoadProperties();
      Logger.getLogger(Config.class).info("Load config file " + this.configfile.getAbsolutePath() + " successfully!");
    } else {
      Logger.getLogger(Config.class).info("Load config file " + this.configfile.getAbsolutePath() + "failed!File not exist!");
    } 
  }
  
  public Properties getProperties() {
    return this.pp;
  }
  
  private void LoadProperties() {
    this.pp = new Properties();
    try {
      this.fis = new FileInputStream(this.configfile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } 
    InputStreamReader isr = null;
    try {
      isr = new InputStreamReader(this.fis, "utf-8");
    } catch (UnsupportedEncodingException e) {
      Logger.getLogger(Config.class).error(e);
      e.printStackTrace();
    } 
    BufferedReader br = new BufferedReader(isr, 4096);
    String temp = null;
    try {
      while ((temp = br.readLine()) != null) {
        if (temp.startsWith("#") || temp.trim().length() == 0)
          continue; 
        if (temp.indexOf("[") >= 0 && temp.indexOf("]") >= 0)
          continue; 
        int first = temp.indexOf("=");
        this.pp.put(temp.substring(0, first).trim(), temp.substring(first + 1, temp.length()).trim());
      } 
    } catch (IOException e) {
      Logger.getLogger(Config.class).error("Load config" + this.configfile.getAbsolutePath());
      e.printStackTrace();
    } finally {
      if (this.fis != null)
        try {
          this.fis.close();
        } catch (IOException e) {
          Logger.getLogger(Config.class).error(e);
          e.printStackTrace();
        }  
    } 
  }
  
  public static void main(String[] args) {
    Config cfg = new Config("D:\\test.cfg");
    cfg.LoadProperties();
    Properties pp = cfg.getProperties();
    Iterator<Object> it = pp.keySet().iterator();
    while (it.hasNext())
      System.out.println(it.next()); 
    System.out.println(pp.getProperty("name"));
  }
}
