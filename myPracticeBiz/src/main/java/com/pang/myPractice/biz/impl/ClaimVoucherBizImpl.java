package com.pang.myPractice.biz.impl;

import com.pang.myPractice.biz.ClaimVoucherBiz;
import com.pang.myPractice.dao.ClaimVoucherDao;
import com.pang.myPractice.dao.ClaimVoucherItemDao;
import com.pang.myPractice.dao.DealRecordDao;
import com.pang.myPractice.dao.EmployeeDao;
import com.pang.myPractice.entity.ClaimVoucher;
import com.pang.myPractice.entity.ClaimVoucherItem;
import com.pang.myPractice.entity.DealRecord;
import com.pang.myPractice.entity.Employee;
import com.pang.myPractice.global.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("claimVoucherBiz")
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {

    @Autowired
    @Qualifier("claimVoucherDao")
    private ClaimVoucherDao claimVoucherDao;

    @Autowired
    @Qualifier("claimVoucherItemDao")
    private ClaimVoucherItemDao claimVoucherItemDao;

    @Autowired
    @Qualifier("dealRecordDao")
    private DealRecordDao dealRecordDao;

    @Autowired
    @Qualifier("employeeDao")
    private EmployeeDao employeeDao;

    @Override
    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setCreateTime(new Date());
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Content.CLAIM_VOUCHER_CREATED);

        claimVoucherDao.insert(claimVoucher);

        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            claimVoucherItemDao.insert(item);
        }
    }

    @Override
    public ClaimVoucher get(int id) {
        return claimVoucherDao.select(id);
    }

    @Override
    public List<ClaimVoucherItem> getItems(int cvid) {
        return claimVoucherItemDao.selectByClaimVoucher(cvid);
    }

    @Override
    public List<DealRecord> getRecords(int cvid) {
        return dealRecordDao.selectByClaimVoucher(cvid);
    }

    @Override
    public List<ClaimVoucher> getForSelf(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    @Override
    public List<ClaimVoucher> getForDeal(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    @Override
    public void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Content.CLAIM_VOUCHER_CREATED);

        claimVoucherDao.update(claimVoucher);

        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectByClaimVoucher(claimVoucher.getId());
        for (ClaimVoucherItem old : olds) {
            boolean isHave = false;
            for (ClaimVoucherItem item : items) {
                if (item.getId() == old.getId()) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                claimVoucherItemDao.delete(old.getId());
            }
        }

        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            if (item.getId() != null && item.getId() > 0) {
                claimVoucherItemDao.update(item);
            } else {
                claimVoucherItemDao.insert(item);
            }
        }
    }

    @Override
    public void submit(int id) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        Employee employee = employeeDao.select(claimVoucher.getCreateSn());

        claimVoucher.setStatus(Content.CLAIM_VOUCHER_SUBMIT);
        claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(), Content.POST_FM).get(0).getSn());

        claimVoucherDao.update(claimVoucher);

        DealRecord dealRecord = new DealRecord();
        dealRecord.setDealWay(Content.DEAL_SUBMIT);
        dealRecord.setDealSn(employee.getSn());
        dealRecord.setClaimVoucherId(id);
        dealRecord.setDealResult(Content.CLAIM_VOUCHER_SUBMIT);
        dealRecord.setDealTime(new Date());
        dealRecord.setComment("无");
        dealRecordDao.insert(dealRecord);
    }

    @Override
    public void deal(DealRecord dealRecord) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());
        Employee employee = employeeDao.select(dealRecord.getDealSn());

        if (dealRecord.getDealWay().equals(Content.DEAL_PASS)) {
            if (claimVoucher.getTotalAmount() <= Content.LIMIT_CHECK || employee.getPost().equals(Content.POST_GM)) {
                claimVoucher.setStatus(Content.CLAIM_VOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Content.POST_CASHIER).get(0).getSn());

                dealRecord.setDealTime(new Date());
                dealRecord.setDealResult(Content.CLAIM_VOUCHER_APPROVED);
            } else {
                claimVoucher.setStatus(Content.CLAIM_VOUCHER_RECHECK);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Content.POST_GM).get(0).getSn());

                dealRecord.setDealTime(new Date());
                dealRecord.setDealResult(Content.CLAIM_VOUCHER_RECHECK);
            }
        } else if (dealRecord.getDealWay().equals(Content.DEAL_BACK)) {
            claimVoucher.setStatus(Content.CLAIM_VOUCHER_BACK);
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());

            dealRecord.setDealTime(new Date());
            dealRecord.setDealResult(Content.DEAL_REJECT);
        } else if (dealRecord.getDealWay().equals(Content.CLAIM_VOUCHER_BACK)) {
            claimVoucher.setStatus(Content.CLAIM_VOUCHER_TERMINATED);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealTime(new Date());
            dealRecord.setDealResult(Content.CLAIM_VOUCHER_TERMINATED);
        } else if (dealRecord.getDealWay().equals(Content.DEAL_PAID)) {
            claimVoucher.setStatus(Content.DEAL_PAID);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealTime(new Date());
            dealRecord.setDealResult(Content.DEAL_PAID);
        }

        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);
    }
}
