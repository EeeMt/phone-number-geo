package me.ihxq.projects.pna.algorithm;

import lombok.extern.slf4j.Slf4j;
import me.ihxq.projects.pna.Attribution;
import me.ihxq.projects.pna.ISP;
import me.ihxq.projects.pna.PhoneNumberInfo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

/**
 * @author xq.h
 * 2019/10/19 00:12
 **/
@Slf4j
public class SimpleLookupAlgorithmImpl implements LookupAlgorithm {
    private ByteBuffer byteBuffer;
    private int indicesOffset;

    @Override
    public void loadData(byte[] data) {
        byteBuffer = ByteBuffer.wrap(data).asReadOnlyBuffer();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        int dataVersion = byteBuffer.getInt();
        //System.out.println(dataVersion);
        indicesOffset = byteBuffer.getInt(4);
    }

    /**
     * @param phoneNo length: 7 to 11
     * @return
     */
    @Override
    public Optional<PhoneNumberInfo> lookup(String phoneNo) {
        log.trace("try resolve attribution of: {}", phoneNo);
        if (phoneNo == null) {
            log.debug("phoneNo is null");
            return Optional.empty();
        }
        int phoneNoLength = phoneNo.length();
        if (phoneNoLength < 7 || phoneNoLength > 11) {
            log.debug("phoneNo {} is not acceptable, length invalid, length should range 7 to 11, actual: {}",
                    phoneNo, phoneNoLength);
            return Optional.empty();
        }

        int attributionIdentity;
        try {
            attributionIdentity = Integer.parseInt(phoneNo.substring(0, 7));
        } catch (NumberFormatException e) {
            log.debug("phoneNo {} is invalid, is it numeric?", phoneNo);
            return Optional.empty();
        }

        for (int i = indicesOffset; i < byteBuffer.limit(); i = i + 8 + 1) {

            byteBuffer.position(i);
            int phonePrefix = byteBuffer.getInt();
            int infoStart = byteBuffer.getInt();
            byte ispMark = byteBuffer.get();
            if (phonePrefix == attributionIdentity) {
                Optional<ISP> isp = ISP.of(ispMark);
                byteBuffer.position(infoStart);
                while ((byteBuffer.get()) != 0) {
                    // just
                }
                int infoEnd = byteBuffer.position() - 1;
                byteBuffer.position(infoStart);
                int length = infoEnd - infoStart;
                byte[] bytes = new byte[length];
                byteBuffer.get(bytes, 0, length);
                String oriString = new String(bytes);
                String[] split = oriString.split("\\|");
                Attribution build = Attribution.builder()
                        .province(split[0])
                        .city(split[1])
                        .zipCode(split[2])
                        .areaCode(split[3])
                        .build();
                return Optional.of(new PhoneNumberInfo(phoneNo, build, isp.get()));
            }
        }
        return Optional.empty();
    }
}
