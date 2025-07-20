package com.wanted.cqrs.common.utils.excel;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.annotation.*;

public class ExcelFormat {


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(ExcelMerges.class)
    public @interface ExcelMerge {
        MergeType type() default MergeType.RANGE;
        String startRange() default "";  // "A1"
        String endRange() default "";    // "C3"
        String[] conditionalFields() default {}; // 조건부 (동일데이터 or 비어있는 데이터) 병합 시 참조할 필드명들
        MergeCondition condition() default MergeCondition.EQUAL_VALUES;
        MergeDirection direction() default MergeDirection.HORIZONTAL;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelMerges {
        ExcelMerge[] value();
    }

    @Target({ElementType.TYPE, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(ExcelStyles.class)
    public @interface ExcelStyle {
        StyleType type() default StyleType.RANGE;
        String startRange() default "";
        String endRange() default "";
        String[] conditionalFields() default {};
        StyleCondition condition() default StyleCondition.NOT_EMPTY;
        String conditionValue() default "";

        // 스타일 속성들
        String fontName() default "";
        short fontSize() default -1;
        boolean bold() default false;
        boolean italic() default false;
        String fontColor() default "";
        String backgroundColor() default "";
        BorderStyle borderTop() default BorderStyle.NONE;
        BorderStyle borderBottom() default BorderStyle.NONE;
        BorderStyle borderLeft() default BorderStyle.NONE;
        BorderStyle borderRight() default BorderStyle.NONE;
        String borderColor() default "";
        HorizontalAlignment horizontalAlign() default HorizontalAlignment.GENERAL;
        VerticalAlignment verticalAlign() default VerticalAlignment.BOTTOM;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcelStyles {
        ExcelStyle[] value();
    }

    // 3. 열거형 정의
    public enum MergeType {
        RANGE,                    // 범위 지정 병합
        CONDITIONAL_COLUMN,       // 조건부 데이터범위 열 병합
        CONDITIONAL_ROW          // 조건부 데이터범위 행 병합
    }

    public enum MergeCondition {
        EQUAL_VALUES,               // 같은 값일 때 + 뒤의 데이터가 비어있을때
        ONLY_EQUAL_VALUES,          // 같은 값일 때 병합
        ONLY_EMPTY,                 // 비어있는 영역만
        // CUSTOM                  // 커스텀 조건
    }

    public enum MergeDirection {
        HORIZONTAL,             // 수평 병합
        VERTICAL               // 수직 병합
    }

    public enum StyleType {
        RANGE,                  // 범위 지정 스타일
        CONDITIONAL_COLUMN,     // 조건부 데이터범위 열 스타일
        CONDITIONAL_ROW,        // 조건부 데이터범위 행 스타일
        FIELD                   // 필드별 스타일
    }

    public enum StyleCondition {
        EQUAL,                  // 값이 같을 때
        NOT_EQUAL,             // 값이 다를 때
        GREATER_THAN,          // 값이 클 때
        LESS_THAN,             // 값이 작을 때
        NOT_EMPTY,             // 비어있지 않을 때
        EMPTY,                 // 비어있을 때
        CONTAINS,              // 포함할 때
        // CUSTOM                 // 커스텀 조건
    }
}
