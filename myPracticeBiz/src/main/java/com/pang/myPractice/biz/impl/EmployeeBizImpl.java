package com.pang.myPractice.biz.impl;

import com.pang.myPractice.biz.EmployeeBiz;
import com.pang.myPractice.dao.EmployeeDao;
import com.pang.myPractice.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("employeeBiz")
public class EmployeeBizImpl implements EmployeeBiz {

    @Autowired
    @Qualifier(value = "employeeDao")
    private EmployeeDao employeeDao;

    @Override
    public void add(Employee employee) {
        employee.setPassword("123");
        employeeDao.insert(employee);
    }

    @Override
    public void edit(Employee employee) {
        employeeDao.update(employee);
    }

    @Override
    public void remove(String sn) {
        employeeDao.delete(sn);
    }

    @Override
    public Employee get(String sn) {
        return employeeDao.select(sn);
    }

    @Override
    public List<Employee> getAll() {
        return employeeDao.selectAll();
    }
}
