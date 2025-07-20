package com.wanted.cqrs.common.utils.excel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * excel 설정 필드 정보
 */
public class ExcelFieldInfo {
    final Field field;
    final ExcelBasic.ExcelColumn excelColumn;

    // fortify setAccessible 사용 불가, PropertyDescriptor 사용시 BeanInfo에 보안 경고 가능성을 고려하여 사용 제외
    final Method getter; // vo -> excel 에서 사용
    final Method setter; // excel -> vo 에서 사용

    ExcelFieldInfo(Field field, ExcelBasic.ExcelColumn excelColumn, Method getter, Method setter) {
        this.field = field;
        this.excelColumn = excelColumn;
        this.getter = getter;
        this.setter = setter;
    }
}