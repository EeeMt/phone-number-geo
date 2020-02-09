package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.algorithm.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * @author xq.h
 * on 2019/10/18 22:30
 **/
@Slf4j
@RunWith(Parameterized.class)
public class PhoneNumberLookupAlgorithmTest {
    public LookupAlgorithm algorithm;

    public PhoneNumberLookupAlgorithmTest(LookupAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Test
    public void invalid() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        Assert.assertNull(phoneNumberLookup.lookup(null).orElse(null));
        Assert.assertNull(phoneNumberLookup.lookup("000").orElse(null));
        Assert.assertNull(phoneNumberLookup.lookup("-1").orElse(null));
        Assert.assertNull(phoneNumberLookup.lookup("130898976761").orElse(null));
    }

    @Test
    public void lookup() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        PhoneNumberInfo found = phoneNumberLookup.lookup("18798896741").orElseThrow(RuntimeException::new);
        Assert.assertNotNull(found.getAttribution());
        Assert.assertEquals(found.getNumber(), "18798896741");
        Assert.assertEquals(found.getAttribution().getProvince(), "贵州");
        Assert.assertEquals(found.getAttribution().getCity(), "贵阳");
        Assert.assertEquals(found.getAttribution().getZipCode(), "550000");
        Assert.assertEquals(found.getAttribution().getAreaCode(), "0851");
        Assert.assertEquals(found.getIsp(), ISP.CHINA_MOBILE);
        Assert.assertNotNull(found.getIsp().getCnName());
    }

    @Test
    public void lookupVirtual() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        PhoneNumberInfo found = phoneNumberLookup.lookup("17048978123").orElseThrow(RuntimeException::new);
        Assert.assertNotNull(found.getAttribution());
        Assert.assertEquals(found.getNumber(), "17048978123");
        Assert.assertEquals(found.getAttribution().getProvince(), "陕西");
        Assert.assertEquals(found.getAttribution().getCity(), "西安");
        Assert.assertEquals(found.getAttribution().getZipCode(), "710000");
        Assert.assertEquals(found.getAttribution().getAreaCode(), "029");
        Assert.assertEquals(found.getIsp(), ISP.CHINA_UNICOM_VIRTUAL);
    }

    @Test
    public void concurrencyLookup() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        //noinspection ResultOfMethodCallIgnored
        Stream.generate(() -> {
            long phoneNumber = (long) (ThreadLocalRandom.current().nextDouble(1D, 2D) * 1000_000_000_0L);
            return String.valueOf(phoneNumber);
        }).limit(2_000)
                .parallel()
                .forEach(v -> phoneNumberLookup.lookup(v).isPresent());
    }

    @Test
    public void lookupFirst() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        Assert.assertNotNull(phoneNumberLookup.lookup("13000000000"));
    }

    @Test
    public void lookupLast() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        Assert.assertNotNull(phoneNumberLookup.lookup("19999790000"));
    }

    @Test
    public void lookupEqual() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        Assert.assertEquals(phoneNumberLookup.lookup("19999790000"), phoneNumberLookup.lookup("19999790000"));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
        return Arrays.asList(
                new Object[]{new BinarySearchAlgorithmImpl()},
                new Object[]{new ProspectBinarySearchAlgorithmImpl()},
                new Object[]{new SequenceLookupAlgorithmImpl()},
                new Object[]{new AnotherBinarySearchAlgorithmImpl()}
        );
    }
}
