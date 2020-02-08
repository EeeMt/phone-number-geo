package me.ihxq.projects.pna.algorithm;

import me.ihxq.projects.pna.PhoneNumberInfo;

import java.util.Optional;

/**
 * 查找算子
 *
 * @author xq.h
 * on 2019/10/18 21:24
 **/
public interface LookupAlgorithm {

    /**
     * 装载数据.
     */
    void loadData(byte[] data);

    /**
     * 根据电话号码查找归属地
     */
    Optional<PhoneNumberInfo> lookup(String phoneNumber);

}
