package com.sense.svcdmobile.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sense.svcdmobile.util.Base64;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class SvcdUserOrgRestClient {
  private String tokenUrl;
  
  private String dataUrl;
  
  private String accountNo;
  
  private String accountPwd;
  
  public SvcdUserOrgRestClient() {
    this.tokenUrl = "http://auth-center.testapps.ocp.csvw.com/oauth/token";
    this.dataUrl = "http://svcd-zuul-prod.testapps.ocp.csvw.com/mobile-service/cmit/mobile";
    this.accountNo = "f5admin";
    this.accountPwd = "f5admin";
  }
  
  public SvcdUserOrgRestClient(String env) {
    this.tokenUrl = "http://auth-center.testapps.ocp.csvw.com/oauth/token";
    if ("dev".equalsIgnoreCase(env)) {
      this.dataUrl = "http://svcd-zuul.devapps.ocp.csvw.com/cmit-service/getData";
    } else if ("test".equalsIgnoreCase(env)) {
      this.dataUrl = "http://svcd-zuul.testapps.ocp.csvw.com/cmit-service/getData";
    } else {
      this.dataUrl = "http://svcd-zuul-prod.testapps.ocp.csvw.com/cmit-service/getData";
    } 
    this.accountNo = "f5admin";
    this.accountPwd = "f5admin";
  }
  
  public SvcdUserOrgRestClient(String tokenUrl, String dataUrl, String accountNo, String accountPwd) {
    this.tokenUrl = tokenUrl;
    this.dataUrl = dataUrl;
    this.accountNo = accountNo;
    this.accountPwd = accountPwd;
  }
  
  public String getAccessToken(String userName, String password) {
    String access_token = null;
    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpPost httpPost = new HttpPost(this.tokenUrl);
    httpPost.setHeader("Authorization", "Basic " + new String(Base64.encode((String.valueOf(userName) + ":" + password).getBytes())));
    List<BasicNameValuePair> list = new ArrayList<>();
    list.add(new BasicNameValuePair("grant_type", "client_credentials"));
    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
    httpPost.setEntity((HttpEntity)entity);
    HttpResponse resp = null;
    try {
      CloseableHttpResponse closeableHttpResponse = client.execute((HttpUriRequest)httpPost);
      String res = EntityUtils.toString(closeableHttpResponse.getEntity());
      Logger.getLogger(SvcdUserOrgRestClient.class).info(res);
      JSONObject jsonstr = JSONObject.parseObject(res);
      if (jsonstr.containsKey("access_token"))
        access_token = jsonstr.get("access_token").toString(); 
      httpPost.completed();
    } catch (ClientProtocolException e) {
      Logger.getLogger(SvcdUserOrgRestClient.class).error(e);
    } catch (IOException e) {
      Logger.getLogger(SvcdUserOrgRestClient.class).error(e);
    } finally {
      if (client != null)
        try {
          client.close();
        } catch (IOException e) {
          Logger.getLogger(SvcdUserOrgRestClient.class).error(e);
        }  
    } 
    return access_token;
  }
  
  public String getUserMobile(String access_token, String samaccountname, String uid, String mail) {
    String mobile = null;
    String data = null;
    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpPost httpPost = new HttpPost(this.dataUrl);
    httpPost.setHeader("appauthz", new String(Base64.encode((String.valueOf(this.accountNo) + ":" + this.accountPwd).getBytes())));
    List<BasicNameValuePair> list = new ArrayList<>();
    list.add(new BasicNameValuePair("access_token", access_token));
    list.add(new BasicNameValuePair("samaccountname", samaccountname));
    list.add(new BasicNameValuePair("uid", uid));
    list.add(new BasicNameValuePair("mail", mail));
    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, Consts.UTF_8);
    httpPost.setEntity((HttpEntity)entity);
    HttpResponse resp = null;
    try {
      CloseableHttpResponse closeableHttpResponse = client.execute((HttpUriRequest)httpPost);
      data = EntityUtils.toString(closeableHttpResponse.getEntity());
      System.out.println(data);
      JSONObject jo = (JSONObject)JSONArray.parse(data);
      mobile = jo.get("moble").toString();
      httpPost.completed();
    } catch (ClientProtocolException e) {
      Logger.getLogger(SvcdUserOrgRestClient.class).error(e);
    } catch (IOException e) {
      Logger.getLogger(SvcdUserOrgRestClient.class).error(e);
    } 
    return mobile;
  }
  
  public static void main(String[] args) {
    SvcdUserOrgRestClient client = new SvcdUserOrgRestClient("pro");
    String token = client.getAccessToken("svcd", "111111");
    System.out.println(client.getUserMobile(token, "huli", null, null));
    System.out.println(client.getUserMobile(token, null, "ext51558", null));
  }
}
