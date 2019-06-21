package com.pang.myPractice.dao;

import com.pang.myPractice.entity.DealRecord;

import java.util.List;

public interface DealRecordDao {
    void insert(DealRecord dealRecord);

    List<DealRecord> selectByClaimVoucher(int cvid);
}
