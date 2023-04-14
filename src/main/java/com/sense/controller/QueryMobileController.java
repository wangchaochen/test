package com.sense.controller;


import com.sense.auth.AuthService;
import com.sense.dao.EventAuditDao;
import com.sense.dao.QueryMobileDao;
import com.sense.entity.EventAudit;
import com.sense.util.Base64;
import com.sense.util.SecurityTool;
import com.sense.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class QueryMobileController extends BaseController{
    @Autowired
    QueryMobileDao queryMobileDao;
    @Autowired
    AuthService authService;
    @Autowired
    EventAuditDao eventAuditDao;

    @RequestMapping("/cmit/mobile")
    public Object queryMobile(){
        EventAudit eventAudit = new EventAudit();
        eventAudit.setType("访问");
        eventAudit.setName("访问手机号码");
        eventAudit.setSubject("");
        eventAudit.setInfo("");

        Map<String,String> result = new HashMap<>();

        String appauthz = request.getHeader("appauthz");
        if(appauthz == null || "".equals(appauthz)){
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(401);
            eventAuditDao.saveEventAudit(eventAudit);//日志
            return null;
        }else{
            String appauthzStr =  new String(Base64.decode(appauthz.getBytes()));

            if(!authService.isAuth(appauthzStr)){
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=utf-8");
                response.setStatus(401);
                eventAuditDao.saveEventAudit(eventAudit);//日志
                return null;
            }
            eventAudit.setSubject(appauthzStr.split(":")[0]);
        }

        String uid = request.getParameter("uid");
        String mail = request.getParameter("mail");
        String samaccountname = request.getParameter("samaccountname");
        if(uid != null && !"".equals(uid)){
            String mobile =  queryMobileDao.queryMobileByUid(uid.toLowerCase());
            setMessage(result,mobile);
            eventAudit.setInfo(uid);//日志
            eventAuditDao.saveEventAudit(eventAudit);
            return result;
        }
        if(mail != null && !"".equals(mail)){
            String mobile =  queryMobileDao.queryMobileByMail(mail.toLowerCase());
            setMessage(result,mobile);
            eventAudit.setInfo(mail);//日志
            eventAuditDao.saveEventAudit(eventAudit);
            return result;
        }
        if(samaccountname != null && !"".equals(samaccountname)){
            String mobile =  queryMobileDao.queryMobileBySamAccountName(samaccountname.toLowerCase());
            setMessage(result,mobile);
            eventAudit.setInfo(samaccountname);//日志
            eventAuditDao.saveEventAudit(eventAudit);
            return result;
        }
        result.put("code","1");
        result.put("moble",null);
        result.put("info","参数错误!");
        eventAuditDao.saveEventAudit(eventAudit);//日志
        return result;
    }

    public void setMessage(Map<String,String> result,String mobile){
        if(mobile!=null&&!"".equals(mobile)){
            result.put("code","0");
            result.put("moble", SecurityUtil.deCode(mobile));
            //result.put("moble", SecurityTool.decrypt(mobile));
            result.put("info","成功!");
        }else{
            result.put("code","1");
            result.put("moble", null);
            result.put("info","信息不存在!");
        }
    }
}
