package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.algorithm.LookupAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author xq.h
 * on 2019/10/18 22:30
 **/
@Slf4j
public class AlgorithmResultDifferTest {

    private boolean isDiff(List<Optional<PhoneNumberInfo>> candidates) {
        boolean allPresent = false;
        boolean allAbsent = false;
        PhoneNumberInfo temp = null;
        for (Optional<PhoneNumberInfo> candidate : candidates) {
            if (candidate.isPresent()) {
                allPresent = true;
                if (allAbsent) {
                    return false;
                }
                if (temp == null) {
                    temp = candidate.get();
                } else {
                    if (!temp.equals(candidate.get())) {
                        return false;
                    } else {
                        temp = candidate.get();
                    }
                }
            } else {
                allAbsent = true;
                if (allPresent) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * test all {@link LookupAlgorithm} implements result whether the same or not
     */
    @Test
    public void testDiff() {
        List<PhoneNumberLookup> lookups = ReflectionUtils.findAllClassesInPackage("me.ihxq.projects.pna", v -> Arrays.asList(v.getInterfaces()).contains(LookupAlgorithm.class), v -> true)
                .stream()
                .map(c -> {
                    try {
                        LookupAlgorithm algorithm = (LookupAlgorithm) c.newInstance();
                        return new PhoneNumberLookup(algorithm);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        boolean noneMatch = Stream.generate(() -> {
            long phoneNumber = (long) (ThreadLocalRandom.current().nextDouble(1D, 2D) * 1000_000_000_0L);
            return String.valueOf(phoneNumber);
        }).limit(2_000)
                .parallel()
                .noneMatch(v -> {
                    List<Optional<PhoneNumberInfo>> results = lookups.stream()
                            .map(l -> l.lookup(v))
                            .collect(Collectors.toList());
                    return isDiff(results);
                });
        assertTrue(noneMatch);

    }
}
