package com.sense.auth;

import com.sense.dao.AuthDao;
import com.sense.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    AuthDao authDao;

    public boolean isAuth(String authInfo){
        boolean authResult = false;
        String validateString = authDao.getAuthInfo();
//        String authString = Base64.encode(validateString.getBytes()).toString();
        if(null!=authInfo&&authInfo.equals(validateString)){
            authResult = true;
        }
        return authResult;
    }
}
