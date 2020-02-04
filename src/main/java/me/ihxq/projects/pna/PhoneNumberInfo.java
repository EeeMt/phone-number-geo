package me.ihxq.projects.pna;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xq.h
 * 2019/10/18 20:54
 **/
@Data
@AllArgsConstructor
public class PhoneNumberInfo {
    private String number;
    private Attribution attribution;
    private ISP isp;
}
