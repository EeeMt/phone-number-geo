package me.ihxq.projects.pna.algorithm;

import me.ihxq.projects.pna.PhoneNumberInfo;

import java.util.Optional;

/**
 * @author xq.h
 * on 2019/10/18 21:24
 **/
public interface LookupAlgorithm {

    void loadData(byte[] data);

    Optional<PhoneNumberInfo> lookup(String ori);

}
