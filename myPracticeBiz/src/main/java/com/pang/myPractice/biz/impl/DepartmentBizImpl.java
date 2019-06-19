package com.pang.myPractice.biz.impl;

import com.pang.myPractice.biz.DepartmentBiz;
import com.pang.myPractice.dao.DepartmentDao;
import com.pang.myPractice.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("departmentBiz")
public class DepartmentBizImpl implements DepartmentBiz {
   // @Resource(name = "departmentDao")
    private DepartmentDao departmentDao;

    @Autowired
    @Qualifier("departmentDao")
    public void setDepartmentDao(DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }

    public void add(Department department) {
        departmentDao.insert(department);
    }

    public void edit(Department department) {
        departmentDao.update(department);
    }

    public void remove(String sn) {
        departmentDao.delete(sn);
    }

    public Department get(String sn) {
        return departmentDao.select(sn);
    }

    public List<Department> getAll() {
        return departmentDao.selectAll();
    }
}
