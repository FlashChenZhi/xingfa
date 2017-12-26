package com.wms.action;

import com.util.common.Const;
import com.util.common.HttpMessage;
import com.wms.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2016/10/14.
 */
@Controller
@RequestMapping("/users")
public class UserAction {

    UserService userService = new UserService();

    /**
     * 创建用户
     *
     * @param userName
     * @param pwd
     * @return
     */
    @ResponseBody
    @RequestMapping("/create.do")
    public HttpMessage create(String userName, String pwd) {
        HttpMessage httpMessage = new HttpMessage();
        httpMessage = userService.create(httpMessage, userName, pwd);
        return httpMessage;
    }

    /**
     * 用户登录
     *
     * @param userName
     * @param pwd
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping("/login.do")
    public HttpMessage login(String userName, String pwd, HttpSession httpSession) {
        HttpMessage httpMessage = new HttpMessage();
        httpMessage = userService.login(httpMessage, userName, pwd);
        if (httpMessage.isSuccess()) {
            httpSession.setAttribute(Const.loginUser, userName);
        }
        return httpMessage;
    }

    /**
     * 用户退出
     *
     * @param httpSession
     * @return
     */
    @RequestMapping("/logout.do")
    @ResponseBody
    public HttpMessage logout(HttpSession httpSession) {
        HttpMessage httpMessage = new HttpMessage();
        httpSession.removeAttribute(Const.loginUser);
        httpMessage.setSuccess(true);
        httpMessage.setMsg("退出成功");
        return httpMessage;
    }

    /**
     * 从session中获取用户信息
     *
     * @param httpSession
     * @return
     */
    @RequestMapping("/getUserName.do")
    @ResponseBody
    public HttpMessage getUserName(HttpSession httpSession) {
        HttpMessage httpMessage = new HttpMessage();
        String userName = (String) httpSession.getAttribute(Const.loginUser);
        if (userName!=null) {
            httpMessage.setSuccess(true);
            httpMessage.setMsg(userName);
        }else {
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
        }
        return httpMessage;
    }

    /**
     * 修改密码
     *
     * @param httpSession
     * @param oldPwd
     * @param password
     * @return
     */
    @RequestMapping("/updatePassword.do")
    @ResponseBody
    public HttpMessage updatePassword(HttpSession httpSession, String oldPwd, String password) {
        String userName = (String) httpSession.getAttribute(Const.loginUser);
        HttpMessage httpMessage = userService.updatePassword(userName, oldPwd, password);
        return httpMessage;
    }
}