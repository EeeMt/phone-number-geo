package me.ihxq.projects.pna.algorithm;

import me.ihxq.projects.pna.PhoneNumberInfo;

import java.util.Optional;

/**
 * @author xq.h
 * on 2019/10/18 21:24
 **/
public interface LookupAlgorithm {

    /**
     * 转载数据库
     */
    void loadData(byte[] data);

    /**
     * 根据电话号码查找归属地
     */
    Optional<PhoneNumberInfo> lookup(String phoneNumber);

}
