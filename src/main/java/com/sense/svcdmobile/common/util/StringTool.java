package com.sense.svcdmobile.common.util;

public class StringTool {
  public static String getInvisibleMobile(String mobile) {
    if (mobile == null || mobile.trim().length() == 0)
      return mobile; 
    if (mobile.trim().length() == 11) {
      String temp = mobile.trim();
      return String.valueOf(temp.substring(0, 3)) + "****" + temp.substring(7, 11);
    } 
    return mobile;
  }
  
  public static boolean isEmpty(String Str) {
    if (Str == null || Str.trim().length() == 0)
      return true; 
    return false;
  }
}
