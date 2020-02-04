package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.algorithm.BinarySearchAlgorithmImpl;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * @author xq.h
 * on 2019/10/18 22:30
 **/
@Slf4j
public class PhoneNumberLookupTest {

    @Test
    public void lookup() throws IOException, URISyntaxException {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(new BinarySearchAlgorithmImpl());
        //phoneNumberLookup =  new PhoneNumberLookup(new SequenceLookupAlgorithmImpl());
        phoneNumberLookup.lookup("16431182745").ifPresent(System.out::println);
        phoneNumberLookup.lookup("13678186961").ifPresent(System.out::println);
        phoneNumberLookup.lookup("18798896741").ifPresent(System.out::println);
        phoneNumberLookup.lookup("13699057030").ifPresent(System.out::println);
    }

    @Test
    public void concurrencyLookup() {
        PhoneNumberLookup phoneNumberLookup = new PhoneNumberLookup(new BinarySearchAlgorithmImpl());
        Stream.generate(() -> {
            long phoneNumber = (long) (ThreadLocalRandom.current().nextDouble(1D, 2D) * 1000_000_000_0L);
            return String.valueOf(phoneNumber);
        }).limit(200_000)
                .parallel()
                .forEach(v -> phoneNumberLookup.lookup(v).ifPresent(System.out::println));
    }
}
