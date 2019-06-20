package com.pang.myPractice.biz.impl;

import com.pang.myPractice.biz.EmployeeBiz;
import com.pang.myPractice.biz.GlobalBiz;
import com.pang.myPractice.dao.EmployeeDao;
import com.pang.myPractice.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("globalBiz")
public class GlobalBizImpl implements GlobalBiz {

    private EmployeeDao employeeDao;

    @Autowired
    @Qualifier("employeeDao")
    public void setEmployeeDao(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public Employee login(String sn, String password) {
        Employee employee = employeeDao.select(sn);
        if (employee != null && employee.getPassword().equals(password))
            return employee;
        return null;
    }

    @Override
    public void changePassword(Employee employee) {
        employeeDao.update(employee);
    }
}
