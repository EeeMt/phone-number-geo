package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.algorithm.BinarySearchAlgorithmImpl;
import me.ihxq.projects.pna.algorithm.LookupAlgorithm;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

/**
 * 电话号码归属信息查询
 *
 * @author xq.h
 * 2019/10/18 21:25
 **/
@Slf4j
public class PhoneNumberLookup {
    private static final String PHONE_NUMBER_GEO_PHONE_DAT = "phone.dat";
    private LookupAlgorithm lookupAlgorithm;
    /**
     * 数据版本hash值, 版本:201911
     */
    private static final int dataHash = 316259817;

    private void init() {
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PHONE_NUMBER_GEO_PHONE_DAT);
            assert inputStream != null;
            byte[] allBytes = IOUtils.toByteArray(inputStream);
            int hashCode = Arrays.hashCode(allBytes);
            if (hashCode != dataHash) {
                throw new IllegalStateException("Hash of data not match, expect: " + dataHash + ", actually: " + hashCode);
            }
            lookupAlgorithm.loadData(allBytes);
        } catch (Exception e) {
            log.error("failed to init PhoneNumberLookUp", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用默认算子
     */
    public PhoneNumberLookup() {
        lookupAlgorithm = new BinarySearchAlgorithmImpl();
        init();
    }

    /**
     * @param lookupAlgorithm 算子
     */
    public PhoneNumberLookup(LookupAlgorithm lookupAlgorithm) {
        this.lookupAlgorithm = lookupAlgorithm;
        init();
    }

    /**
     * @param phoneNumber 电话号码, 11位, 或前7位
     * @return 电话号码归属信息
     */
    public Optional<PhoneNumberInfo> lookup(String phoneNumber) {
        return lookupAlgorithm.lookup(phoneNumber);
    }
}
