package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.algorithm.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xq.h
 * on 2019/10/18 22:30
 **/
@Slf4j
public class PhoneNumberLookupAlgorithmTest {

    @ParameterizedTest
    @MethodSource("algorithms")
    public void invalid(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        assertNull(phoneNumberLookup.lookup(null).orElse(null));
        assertNull(phoneNumberLookup.lookup("000").orElse(null));
        assertNull(phoneNumberLookup.lookup("-1").orElse(null));
        assertNull(phoneNumberLookup.lookup("130898976761").orElse(null));
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void lookup(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        PhoneNumberInfo found = phoneNumberLookup.lookup("18798896741").orElseThrow(RuntimeException::new);
        assertNotNull(found.getAttribution());
        assertEquals(found.getNumber(), "18798896741");
        assertEquals(found.getAttribution().getProvince(), "贵州");
        assertEquals(found.getAttribution().getCity(), "贵阳");
        assertEquals(found.getAttribution().getZipCode(), "550000");
        assertEquals(found.getAttribution().getAreaCode(), "0851");
        assertEquals(found.getIsp(), ISP.CHINA_MOBILE);
        assertNotNull(found.getIsp().getCnName());
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void lookupVirtual(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        PhoneNumberInfo found = phoneNumberLookup.lookup("17048978123").orElseThrow(RuntimeException::new);
        assertNotNull(found.getAttribution());
        assertEquals(found.getNumber(), "17048978123");
        assertEquals(found.getAttribution().getProvince(), "陕西");
        assertEquals(found.getAttribution().getCity(), "西安");
        assertEquals(found.getAttribution().getZipCode(), "710000");
        assertEquals(found.getAttribution().getAreaCode(), "029");
        assertEquals(found.getIsp(), ISP.CHINA_UNICOM_VIRTUAL);
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void concurrencyLookup(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        //noinspection ResultOfMethodCallIgnored
        Stream.generate(() -> {
            long phoneNumber = (long) (ThreadLocalRandom.current().nextDouble(1D, 2D) * 1000_000_000_0L);
            return String.valueOf(phoneNumber);
        }).limit(2_000)
                .parallel()
                .forEach(v -> phoneNumberLookup.lookup(v).isPresent());
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void lookupFirst(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        assertNotNull(phoneNumberLookup.lookup("13000000000"));
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void lookupLast(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        assertNotNull(phoneNumberLookup.lookup("19999790000"));
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void lookupEqual(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(algorithm);
        assertEquals(phoneNumberLookup.lookup("19999790000"), phoneNumberLookup.lookup("19999790000"));
    }

    public static Stream<LookupAlgorithm> algorithms() {
        return Stream.of(
                new BinarySearchAlgorithmImpl(),
                new ProspectBinarySearchAlgorithmImpl(),
                new SequenceLookupAlgorithmImpl(),
                new AnotherBinarySearchAlgorithmImpl()
        );
    }
}
