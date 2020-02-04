package me.ihxq.projects.pna.benchmark;

import me.ihxq.projects.pna.PhoneNumberLookup;
import me.ihxq.projects.pna.algorithm.BinarySearchAlgorithmImpl;
import me.ihxq.projects.pna.algorithm.NewBinarySearchAlgorithmImpl;
import me.ihxq.projects.pna.algorithm.SimpleLookupAlgorithmImpl;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xq.h
 * 2020/2/4 11:38
 **/
public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchmarkRunner.class.getSimpleName())
                .warmupForks(0)
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(5)
                .warmupTime(TimeValue.seconds(5))
                .measurementTime(TimeValue.seconds(5))
                .timeout(TimeValue.minutes(1))
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.NANOSECONDS)
                .build();

        new Runner(opt).run();
    }

    private static List<String> phoneNumbers = Stream.generate(() -> {
        long phoneNumber = (long) (ThreadLocalRandom.current().nextDouble(1D, 2D) * 1000_000_000_0L);
        return String.valueOf(phoneNumber);
    }).limit(200_000)
            .collect(Collectors.toList());

    static int index = 0;

    private static String getPhoneNumber() {
        if (index == phoneNumbers.size()) {
            index = 0;
        }
        return phoneNumbers.get(index++);
    }

    private static PhoneNumberLookup phoneNumberLookup1;
    private static PhoneNumberLookup phoneNumberLookup2;
    private static PhoneNumberLookup phoneNumberLookup3;
    static {
        try {
            phoneNumberLookup1 = new PhoneNumberLookup(new BinarySearchAlgorithmImpl());
            phoneNumberLookup2 = new PhoneNumberLookup(new NewBinarySearchAlgorithmImpl());
            phoneNumberLookup3 = new PhoneNumberLookup(new SimpleLookupAlgorithmImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    public void lookup1() throws IOException, URISyntaxException {
        phoneNumberLookup1.lookup(getPhoneNumber());
    }

    @Benchmark
    public void lookup2() throws IOException, URISyntaxException {
        phoneNumberLookup2.lookup(getPhoneNumber());
    }

    @Benchmark
    public void lookup3() throws IOException, URISyntaxException {
        phoneNumberLookup3.lookup(getPhoneNumber());
    }
}
