package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author xq.h
 * on 2019/10/18 22:30
 **/
@Slf4j
public class PhoneNumberLookupTest {
    @Test
    public void invalid() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup();
        Assert.assertNull(phoneNumberLookup.lookup("000").orElse(null));
        Assert.assertNull(phoneNumberLookup.lookup("-1").orElse(null));
        Assert.assertNull(phoneNumberLookup.lookup("130898976761").orElse(null));
    }

}
