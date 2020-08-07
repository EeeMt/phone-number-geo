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

    private PhoneNumberLookup constructLookup(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup;
        if (algorithm == null) {
            phoneNumberLookup = new PhoneNumberLookup();
        } else {
            phoneNumberLookup = new PhoneNumberLookup(algorithm);
        }
        return phoneNumberLookup;
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void invalid(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = constructLookup(algorithm);
        assertFalse(phoneNumberLookup.lookup(null).isPresent());
        assertFalse(phoneNumberLookup.lookup("000").isPresent());
        assertFalse(phoneNumberLookup.lookup("136800O0000").isPresent()); // it's 'O', a letter, which should be '0'
        assertFalse(phoneNumberLookup.lookup("-1").isPresent());
        assertFalse(phoneNumberLookup.lookup("130898976761").isPresent());
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void lookup(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = constructLookup(algorithm);
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
        PhoneNumberLookup phoneNumberLookup = constructLookup(algorithm);
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
        PhoneNumberLookup phoneNumberLookup = constructLookup(algorithm);
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
        PhoneNumberLookup phoneNumberLookup = constructLookup(algorithm);
        assertTrue(phoneNumberLookup.lookup("13000000000").isPresent());
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void lookupLast(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = constructLookup(algorithm);
        assertTrue(phoneNumberLookup.lookup("19999790000").isPresent());
    }

    @ParameterizedTest
    @MethodSource("algorithms")
    public void lookupEqual(LookupAlgorithm algorithm) {
        PhoneNumberLookup phoneNumberLookup = constructLookup(algorithm);
        assertEquals(phoneNumberLookup.lookup("19999790000"), phoneNumberLookup.lookup("19999790000"));
    }

    public static Stream<LookupAlgorithm> algorithms() {
        return Stream.of(
                null,
                new BinarySearchAlgorithmImpl(),
                new ProspectBinarySearchAlgorithmImpl(),
                new SequenceLookupAlgorithmImpl(),
                new AnotherBinarySearchAlgorithmImpl()
        );
    }
}
