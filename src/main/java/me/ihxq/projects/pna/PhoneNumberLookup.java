package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.algorithm.BinarySearchAlgorithmImpl;
import me.ihxq.projects.pna.algorithm.LookupAlgorithm;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author xq.h
 * 2019/10/18 21:25
 **/
@Slf4j
public class PhoneNumberLookup {
    private static final String PHONE_NUMBER_GEO_PHONE_DAT = "phone.dat";
    private LookupAlgorithm lookupAlgorithm;

    private void init() {
        try {
            URL url = ClassLoader.getSystemResource(PHONE_NUMBER_GEO_PHONE_DAT);
            Path path = Paths.get(url.toURI());
            byte[] allBytes = Files.readAllBytes(path);
            lookupAlgorithm.loadData(allBytes);
        } catch (Exception e) {
            log.error("failed to init PhoneNumberLookUp");
            throw new RuntimeException(e);
        }
    }

    public PhoneNumberLookup() {
        lookupAlgorithm = new BinarySearchAlgorithmImpl();
        init();
    }

    public PhoneNumberLookup(LookupAlgorithm lookupAlgorithm) {
        this.lookupAlgorithm = lookupAlgorithm;
        init();
    }

    public Optional<PhoneNumberInfo> lookup(String phoneNumber) {
        return lookupAlgorithm.lookup(phoneNumber);
    }
}
