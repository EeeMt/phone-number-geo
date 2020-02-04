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
public class BinarySearchAlgorithmImpl implements LookupAlgorithm {
    private ByteBuffer byteBuffer;
    private int indicesStartOffset;
    private int indicesEndOffset;
    private int recordTotal;

    @Override
    public void loadData(byte[] data) {
        byteBuffer = ByteBuffer.wrap(data)
                .asReadOnlyBuffer()
                .order(ByteOrder.LITTLE_ENDIAN);
        int dataVersion = byteBuffer.getInt();
        System.out.println(dataVersion);
        indicesStartOffset = byteBuffer.getInt(4);
        indicesEndOffset = byteBuffer.capacity();
        recordTotal = (byteBuffer.capacity() - indicesStartOffset) / 9;
    }

    @Override
    public Optional<PhoneNumberInfo> lookup(String phoneNo) {
        log.trace("try to resolve attribution of: {}", phoneNo);
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
        int left = indicesStartOffset;
        int right = indicesEndOffset;
        int mid = (left + right) / 2;
        while (mid >= left && mid <= right) {
            if (mid == right) {
                return Optional.empty();
            }
            int compare = compare(mid, attributionIdentity);
            if (compare == 0) {
                break;
            }
            if (mid == left) {
                return Optional.empty();
            }

            if (compare > 0) {
                int tempMid = (mid + left) / 2;
                right = mid;
                int remain = (tempMid - indicesStartOffset) % 9;
                if (tempMid - indicesStartOffset < 9) {
                    mid = tempMid - remain;
                    continue;
                }
                if (remain != 0) {
                    mid = tempMid + 9 - remain;
                } else {
                    mid = tempMid;
                }
            } else {
                int tempMid = (mid + right) / 2;
                left = mid;
                int remain = (tempMid - indicesStartOffset) % 9;
                if (tempMid - indicesStartOffset < 9) {
                    mid = tempMid - remain;
                    continue;
                }
                if (remain != 0) {
                    mid = tempMid + 9 - remain;
                } else {
                    mid = tempMid;
                }
            }
        }

        byteBuffer.position(mid);
        int prefix = byteBuffer.getInt();
        int infoStartIndex = byteBuffer.getInt();
        byte ispMark = byteBuffer.get();
        Optional<ISP> isp = ISP.of(ispMark);
        byteBuffer.position(infoStartIndex);
        //noinspection StatementWithEmptyBody
        while ((byteBuffer.get()) != 0) {
        }
        int infoEnd = byteBuffer.position() - 1;
        byteBuffer.position(infoStartIndex);
        int length = infoEnd - infoStartIndex;
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
        return Optional.of(new PhoneNumberInfo(phoneNo, build, isp.orElse(ISP.UNKNOWN)));
    }

    private int compare(int position, int key) {
        byteBuffer.position(position);
        int phonePrefix = 0;
        try {
            phonePrefix = byteBuffer.getInt();
        } catch (Exception e) {
            System.out.println("position: " + position);
            throw new RuntimeException(e);
        }
        return Integer.compare(phonePrefix, key);
    }
}
