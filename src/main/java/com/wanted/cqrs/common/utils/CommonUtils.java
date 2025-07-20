package com.wanted.cqrs.common.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class CommonUtils {

    public static String capitalizeFirstLetter(String str) {
        if(StringUtils.isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
