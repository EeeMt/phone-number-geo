package me.ihxq.projects.pna.benchmark;

import me.ihxq.projects.pna.PhoneNumberLookup;
import me.ihxq.projects.pna.algorithm.AnotherBinarySearchAlgorithmImpl;
import me.ihxq.projects.pna.algorithm.BinarySearchAlgorithmImpl;
import me.ihxq.projects.pna.algorithm.SequenceLookupAlgorithmImpl;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;
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
                .threads(3)
                .build();

        new Runner(opt).run();
    }

    @State(Scope.Thread)
    public static class PhoneNumbers {
        private final List<String> phoneNumbers;
        private int index = 0;

        public PhoneNumbers() {
            this.phoneNumbers = Stream.generate(() -> {
                long phoneNumber = (long) (ThreadLocalRandom.current().nextDouble(1D, 2D) * 1000_000_000_0L);
                return String.valueOf(phoneNumber);
            }).limit(2_000_000)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        public String getPhoneNumber() {
            if (index == phoneNumbers.size()) {
                index = 0;
            }
            return phoneNumbers.get(index++);
        }
    }


    private static PhoneNumberLookup binarySearchLookup = new PhoneNumberLookup(new BinarySearchAlgorithmImpl());
    private static PhoneNumberLookup anotherBinarySearchLookup = new PhoneNumberLookup(new AnotherBinarySearchAlgorithmImpl());
    private static PhoneNumberLookup sequenceLookup = new PhoneNumberLookup(new SequenceLookupAlgorithmImpl());

    @Benchmark
    public void binarySearchLookup(PhoneNumbers phoneNumbers) {
        binarySearchLookup.lookup(phoneNumbers.getPhoneNumber());
    }

    @Benchmark
    public void anotherBinarySearchLookup(PhoneNumbers phoneNumbers) {
        anotherBinarySearchLookup.lookup(phoneNumbers.getPhoneNumber());
    }

    @Benchmark
    public void sequenceLookup(PhoneNumbers phoneNumbers) {
        sequenceLookup.lookup(phoneNumbers.getPhoneNumber());
    }
}
