package com.pang.myPractice.controller;

import com.pang.myPractice.biz.GlobalBiz;
import com.pang.myPractice.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class GlobalController {

    private GlobalBiz globalBiz;

    @Autowired
    public void setGlobalBiz(GlobalBiz globalBiz) {
        this.globalBiz = globalBiz;
    }

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(HttpSession session, @RequestParam String sn, @RequestParam String password) {
        Employee employee = globalBiz.login(sn, password);
        if (employee == null)
            return "redirect:to_login";
        else {
            session.setAttribute("employee", employee);
            return "redirect:self";
        }
    }

    @RequestMapping("/self")
    public String self() {
        return "self";
    }
}
