package com.pang.myPractice.biz.impl;

import com.pang.myPractice.biz.ClaimVoucherBiz;
import com.pang.myPractice.dao.ClaimVoucherDao;
import com.pang.myPractice.dao.ClaimVoucherItemDao;
import com.pang.myPractice.dao.DealRecordDao;
import com.pang.myPractice.entity.ClaimVoucher;
import com.pang.myPractice.entity.ClaimVoucherItem;
import com.pang.myPractice.entity.DealRecord;
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

    @Override
    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        claimVoucher.setCreateTime(new Date());
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Content.CLAIM_VOUCHER_CREATED);

        claimVoucherDao.insert(claimVoucher);

        for(ClaimVoucherItem item: items){
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
}
