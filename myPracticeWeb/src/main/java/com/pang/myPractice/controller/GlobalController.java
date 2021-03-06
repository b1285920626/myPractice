package com.pang.myPractice.controller;

import com.pang.myPractice.biz.GlobalBiz;
import com.pang.myPractice.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller("globalController")
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

    @RequestMapping("/quit")
    public String quit() {
        return "redirect:to_login";
    }

    @RequestMapping("/to_change_password")
    public String toChangePassword() {
        return "change_password";
    }

    @RequestMapping("/change_password")
    public String changePassword(HttpSession session, @RequestParam String old, @RequestParam String new1,
                                 @RequestParam String new2) {
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee.getPassword().equals(old)) {
            if (new1.equals(new2)) {
                employee.setPassword(new1);
                globalBiz.changePassword(employee);
                return "redirect:self";
            }
        }
        return "redirect:to_change_password";
    }
}
