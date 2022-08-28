package com.loix.crm.settings.web.controller;

import com.loix.crm.commons.constant.Constants;
import com.loix.crm.commons.domain.ReturnObject;
import com.loix.crm.commons.utils.DateUtils;
import com.loix.crm.settings.domain.User;
import com.loix.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    public @ResponseBody Object login(String loginAct, String loginPwd, String isRemPwd,
                                      HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = null;
        try {
            user = userService.queryUserByLoginActAndPwd(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReturnObject returnObject = new ReturnObject();

        if(user == null){
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("Username or password is not correct.");
        } else{

            if(DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime()) > 0){
                // fail, expired account
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("The account is expired.");
            } else if("0".equals(user.getLockState())){
                // fail, state locked
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("The state is locked.");
            } else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                // fail, ip is abnormal
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("The IP address is limited.");
            } else{
                //success
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("Success login.");

                session.setAttribute(Constants.SESSION_USER, user);

                if(isRemPwd.equals("true")) {
                    Cookie cookie1 = new Cookie("loginAct", user.getLoginAct());
                    Cookie cookie2 = new Cookie("loginPwd", user.getLoginPwd());
                    cookie1.setMaxAge(10*24*3600);
                    cookie2.setMaxAge(10*24*3600);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                } else {
                    Cookie cookie1 = new Cookie("loginAct", "1");
                    Cookie cookie2 = new Cookie("loginPwd", "1");
                    cookie1.setMaxAge(0);
                    cookie2.setMaxAge(0);
                    response.addCookie(cookie1);
                    response.addCookie(cookie2);
                }
            }
        }

        return returnObject;

    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        Cookie cookie1 = new Cookie("loginAct", "1");
        Cookie cookie2 = new Cookie("loginPwd", "1");
        cookie1.setMaxAge(0);
        cookie2.setMaxAge(0);
        response.addCookie(cookie1);
        response.addCookie(cookie2);

        session.invalidate();

        return "redirect:/";
    }

}
