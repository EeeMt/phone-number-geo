package me.ihxq.projects.pna;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.algorithm.LookupAlgorithm;
import me.ihxq.projects.pna.algorithm.SimpleLookupAlgorithmImpl;

import java.io.IOException;
import java.net.URISyntaxException;
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

    private void init() throws IOException, URISyntaxException {
        URL url = ClassLoader.getSystemResource(PHONE_NUMBER_GEO_PHONE_DAT);
        Path path = Paths.get(url.toURI());
        byte[] allBytes = Files.readAllBytes(path);
        lookupAlgorithm.loadData(allBytes);
    }

    public PhoneNumberLookup() throws IOException, URISyntaxException {
        lookupAlgorithm = new SimpleLookupAlgorithmImpl();
        init();
    }

    public PhoneNumberLookup(LookupAlgorithm lookupAlgorithm) throws IOException, URISyntaxException {
        this.lookupAlgorithm = lookupAlgorithm;
        init();
    }

    public Optional<PhoneNumberInfo> lookup(String phoneNumber) {
        return lookupAlgorithm.lookup(phoneNumber);
    }
}
