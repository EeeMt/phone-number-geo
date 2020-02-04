package me.ihxq.projects.pna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author xq.h
 * 2019/10/18 20:58
 **/
@Data
@AllArgsConstructor
@Builder
public class Attribution {
    private String province;
    private String city;
    private String zipCode;
    private String areaCode;
}
