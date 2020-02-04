package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.algorithm.BinarySearchAlgorithmImpl;
import org.junit.Test;
import org.junit.runner.Description;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * @author xq.h
 * on 2019/10/18 22:30
 **/
@Slf4j
public class PhoneNumberLookupTest {
    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        log.info(String.format("Test %s %s, spent %d microseconds",
                testName, status, TimeUnit.NANOSECONDS.toMicros(nanos)));
    }

    @Test
    public void lookup() throws IOException, URISyntaxException {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(new BinarySearchAlgorithmImpl());
        //phoneNumberLookup = new PhoneNumberLookup(new SimpleLookupAlgorithmImpl());
        phoneNumberLookup.lookup("16431182745").ifPresent(System.out::println);
        phoneNumberLookup.lookup("13678186961").ifPresent(System.out::println);
        phoneNumberLookup.lookup("18798896741").ifPresent(System.out::println);
        phoneNumberLookup.lookup("13699057030").ifPresent(System.out::println);
    }
}
