package com.wanted.cqrs.common.utils.excel;

import lombok.experimental.UtilityClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@UtilityClass
public class ExcelBasic {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ExcelSheet {

        String name() default "Sheet1";

        int startColIndex() default 0;
        int headerRowIndex() default 0;
        int dataStartRowIndex() default 1;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface ExcelColumn {

        /* basic options */

        boolean required() default true;
        String defaultValue() default "";

        String headerNm() default "";
        int order();

        int width() default 2000;
        String datetimeFormat() default "yyyy-MM-dd";
    }
}
