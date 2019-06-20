package com.pang.myPractice.biz;

import com.pang.myPractice.entity.Employee;

public interface GlobalBiz {
    Employee login(String sn, String password);

    void changePassword(Employee employee);
}
