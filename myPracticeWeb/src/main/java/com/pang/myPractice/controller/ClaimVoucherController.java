package com.pang.myPractice.controller;

import com.pang.myPractice.biz.ClaimVoucherBiz;
import com.pang.myPractice.dto.ClaimVoucherInfo;
import com.pang.myPractice.entity.Employee;
import com.pang.myPractice.global.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller("claimVoucherController")
@RequestMapping("/claim_voucher")
public class ClaimVoucherController {

    @Autowired
    private ClaimVoucherBiz claimVoucherBiz;

    @RequestMapping("/to_add")
    public String toAdd(Map<String, Object> map) {
        map.put("items", Content.getItems());
        map.put("info", new ClaimVoucherInfo());
        return "claim_voucher_add";
    }

    @RequestMapping("/add")
    public String add(HttpSession session, ClaimVoucherInfo info) {
        Employee employee = (Employee) session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn());
        claimVoucherBiz.save(info.getClaimVoucher(), info.getItems());

        return "redirect:detail?id=" + info.getClaimVoucher().getId();
    }

    @RequestMapping("/detail")
    public String detail(int id, Map<String, Object> map) {
        map.put("claimVoucher", claimVoucherBiz.get(id));
        map.put("items", claimVoucherBiz.getItems(id));
        map.put("records", claimVoucherBiz.getRecords(id));

        return "claim_voucher_detail";
    }
}