package com.wanted.cqrs.common.utils.excel;

import com.wanted.cqrs.common.utils.CommonUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@UtilityClass
public class ExcelUtils {

    // todo : 복잡한 설정 가져오는 케이스도 추가 작업 필요

    /**
     * vo -> excel 생성
     * @param dataList
     * @param clazz
     * @return
     * @param <T>
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> ByteArrayOutputStream createExcelFile(Collection<T> dataList, Class<T> clazz) throws IOException, IllegalArgumentException, InvocationTargetException, IllegalAccessException {
        if(dataList == null || dataList.isEmpty()) throw new IllegalArgumentException("데이터 없음");

        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            // sheet 설정 조회
            ExcelBasic.ExcelSheet sheetAnnotation = clazz.getAnnotation(ExcelBasic.ExcelSheet.class);
            String sheetName = sheetAnnotation != null ? sheetAnnotation.name() : "Sheet1";
            XSSFSheet sheet = workbook.createSheet(sheetName);

            int startColIndex = sheetAnnotation == null || sheetAnnotation.startColIndex() < 0 ? 0 : sheetAnnotation.startColIndex();
            int headerRowIndex = sheetAnnotation == null || sheetAnnotation.headerRowIndex() < 0 ? 0 : sheetAnnotation.headerRowIndex();

            // 필드 정보 추출
            List<ExcelFieldInfo> sortedFieldInfos = ExcelUtils.getSortedExcelFields(clazz);

            // header and data style
            CellStyle headerStyle = ExcelUtils.createDefaultHeaderStyle(workbook);
            CellStyle dataStyle = ExcelUtils.createDefaultDataStyle(workbook);

            // 헤더 생성 & 데이터 생성
            ExcelUtils.createHeader(sheet, sortedFieldInfos, startColIndex, headerRowIndex, headerStyle);

            int startDataRowIndex = sheetAnnotation == null ? sheet.getLastRowNum() : sheetAnnotation.headerRowIndex();
            ExcelUtils.createDataRows(sheet, dataList, sortedFieldInfos, startColIndex, startDataRowIndex, dataStyle);

            // 컬럼 너비 조정
            int index = startColIndex;
            for(final ExcelFieldInfo fieldInfo : sortedFieldInfos) {
                if (fieldInfo.excelColumn.width() > 0) sheet.setColumnWidth(index, fieldInfo.excelColumn.width());
                else sheet.autoSizeColumn(index);
                index++;
            }

            // stream 반환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream;
        }
    }

    /**
     * excel -> vo 추출
     * @param inputStream
     * @param clazz
     * @return
     * @param <T>
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> List<T> parseExcelFile(InputStream inputStream, Class<T> clazz)
            throws IOException, IllegalArgumentException, IllegalAccessException {
        List<T> result = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);

            // sheet 설정 조회 및 데이터 설정 시작 범위 조회
            ExcelBasic.ExcelSheet sheetAnno = clazz.getAnnotation(ExcelBasic.ExcelSheet.class);
            int dataStartRowIndex = sheetAnno != null ? sheetAnno.dataStartRowIndex() : 1;

            // 필드 정보 추출
            List<ExcelFieldInfo> sortedFieldInfos = ExcelUtils.getSortedExcelFields(clazz);

            // 데이터 행 처리
            for (int rowIndex = dataStartRowIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if(row == null) continue;

                // 인스턴스 new 생성
                T instance = null;

                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                    log.error(e.getMessage());
                    throw new IllegalAccessException("인스턴스 생성 에러");
                }

                int colIndex = sheetAnno == null || sheetAnno.startColIndex() < 0 ? 0 : sheetAnno.startColIndex();
                for(ExcelFieldInfo fieldInfo : sortedFieldInfos) {
                    if(fieldInfo.setter == null) continue;

                    Object value = ExcelUtils.getCellTypeValue(row.getCell(colIndex), fieldInfo);
                    if(value == null && fieldInfo.excelColumn.required()) {
                        throw new IllegalArgumentException(
                                String.format("행 %d, 컬럼 %s: 필수 값이 누락되었습니다.", rowIndex + 1, fieldInfo.excelColumn.headerNm()));
                    }
                    try {
                        fieldInfo.setter.invoke(instance, value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage());
                        throw new IllegalAccessException("setter 접근 오류 " + fieldInfo.excelColumn.headerNm());
                    }

                    colIndex++;
                }

                result.add(instance);
            }

            return result;
        }
    }

    /**
     * excel column 필드 정보 추출
     * @param clazz
     * @return
     * @param <T>
     */
    private static <T> List<ExcelFieldInfo> getSortedExcelFields(Class<T> clazz) {

        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelBasic.ExcelColumn.class))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(ExcelBasic.ExcelColumn.class).order()))
                .map(field -> {
                    ExcelBasic.ExcelColumn excelColumnAnnotation = field.getAnnotation(ExcelBasic.ExcelColumn.class);

                    /* fortify 정책상 이름으로 getter, setter를 직접 가져오는 방식으로 처리 */

                    // getter 조회 vo -> excel에서 사용
                    String fieldName = field.getName();
                    String getterName = "get" + CommonUtils.capitalizeFirstLetter(fieldName);

                    // boolean 필드의 경우 is~ 로 생성할 수 있음
                    String booleanGetterName = null;
                    if(field.getType() == Boolean.class || field.getType() == boolean.class) {
                        booleanGetterName = "is" + CommonUtils.capitalizeFirstLetter(fieldName);
                    }

                    Method getter = null;
                    try {
                        getter = clazz.getMethod(getterName);
                    } catch (NoSuchMethodException e) {
                        if (booleanGetterName != null) {
                            try {
                                getter = clazz.getMethod(booleanGetterName);
                            } catch (NoSuchMethodException ignored) {
                                log.error("getter field 찾을 수 없음 : {}", fieldName);
                                log.error(e.getMessage());
                            }
                        }
                    }

                    // setter 조회 excel -> vo 에서 사용
                    String setterName = "set" + CommonUtils.capitalizeFirstLetter(fieldName);
                    Method setter = null;
                    try {
                        setter = clazz.getMethod(setterName, field.getType());
                    } catch (NoSuchMethodException e) {
                        log.error("setter field 찾을 수 없음 : {}", fieldName);
                        log.error(e.getMessage());
                    }

                    return new ExcelFieldInfo(field, excelColumnAnnotation, getter, setter);
                }).toList();
    }

    /**
     * 기본 header 스타일 생성
     * @param workbook
     * @return
     */
    private static CellStyle createDefaultHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // 폰트
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);

        // 채우기
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 테두리
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // 정렬
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * 기본 데이터 셀 스타일 생성
     * @param workbook
     * @return
     */
    private CellStyle createDefaultDataStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // 폰트
        Font font = workbook.createFont();
        font.setBold(false);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        // 테두리
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // 정렬
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * 헤더 생성
     * @param sheet
     * @param sortedFields
     * @param startColIndex
     * @param startRowIndex
     * @param headerStyle
     */
    private static void createHeader(Sheet sheet, List<ExcelFieldInfo> sortedFields, int startColIndex, int startRowIndex, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(startRowIndex);

        int colIndex = startColIndex;
        for(final ExcelFieldInfo fieldInfo : sortedFields) {
            Cell cell = headerRow.createCell(colIndex++);
            String headerNm = StringUtils.isNotEmpty(fieldInfo.excelColumn.headerNm())
                    ? fieldInfo.excelColumn.headerNm() : fieldInfo.field.getName();

            cell.setCellValue(headerNm);
            cell.setCellStyle(headerStyle);
        }
    }

    /**
     * 데이터 로우 생성
     * @param sheet
     * @param dataList
     * @param sortedFields
     * @param startColIndex
     * @param startRowIndex
     * @param style
     * @param <T>
     * @throws IllegalAccessException
     */
    private static <T> void createDataRows(Sheet sheet, Collection<T> dataList, List<ExcelFieldInfo> sortedFields, int startColIndex, int startRowIndex, CellStyle style) throws IllegalAccessException {
        int rowIndex = startRowIndex;

        for(final T item : dataList) {
            Row row = sheet.createRow(rowIndex++);

            int colIndex = startColIndex;
            for(ExcelFieldInfo fieldInfo : sortedFields) {
                Cell cell = row.createCell(colIndex++);
                cell.setCellStyle(style);

                // Method Relfect 정보를 직접 가져옴
                Object value = null;
                try {
                    value = fieldInfo.getter == null ? fieldInfo.excelColumn.defaultValue() : fieldInfo.getter.invoke(item);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error(e.getMessage());
                    throw new IllegalAccessException("getter 접근 오류 " + fieldInfo.excelColumn.headerNm());
                }

                if(value == null) {
                    cell.setCellValue("");
                    continue;
                }

                ExcelUtils.setCellTypeValue(cell, value, fieldInfo);
            }
        }
    }

    /**
     * java type에 따른 cell 값 설정
     * @param cell
     * @param value
     * @param sortedFields
     */
    private static void setCellTypeValue(Cell cell, Object value, ExcelFieldInfo sortedFields) {

        String defaultDateTimeFormat = StringUtils.isNotBlank(sortedFields.excelColumn.datetimeFormat())
                ? sortedFields.excelColumn.datetimeFormat() : "yyyy-MM-dd HH:mm:ss";
        String defaultDateFormat = StringUtils.isNotBlank(sortedFields.excelColumn.datetimeFormat())
                ? sortedFields.excelColumn.datetimeFormat() : "yyyy-MM-dd";

        // string
        if (sortedFields.field.getType() == String.class) cell.setCellValue((String) value);

        // number
        else if (value instanceof Double number) cell.setCellValue(number);
        else if (value instanceof BigDecimal decimal) cell.setCellValue(decimal.doubleValue());
        else if (value instanceof Number number ) cell.setCellValue(number.doubleValue());

        // date (todo : Date, Timestamp 추가)
        else if(value instanceof LocalDate localDate) cell.setCellValue(localDate.format(DateTimeFormatter.ofPattern(defaultDateFormat)));
        else if(value instanceof LocalDateTime localDateTime) cell.setCellValue(localDateTime.format(DateTimeFormatter.ofPattern(defaultDateTimeFormat)));

        else if (value instanceof Boolean bool) cell.setCellValue(bool);
        else cell.setCellValue(value.toString());
    }

    private static Object getCellTypeValue(Cell cell, ExcelFieldInfo fieldInfo) {
        if(cell == null) {
            String defaultValue = fieldInfo.excelColumn.defaultValue();
            return defaultValue.isEmpty() ? null : ExcelUtils.convertStringValue(defaultValue, fieldInfo);
        }

        switch (cell.getCellType()) {
            case STRING:
                String stringValue = cell.getStringCellValue();
                if(StringUtils.isNotBlank(stringValue)) return convertStringValue(stringValue.trim(), fieldInfo);
                else return stringValue;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) { // date todo : Date, Timestamp 추가
                    Class<?> fieldType = fieldInfo.field.getType();
                    if (fieldType == LocalDate.class) return cell.getLocalDateTimeCellValue().toLocalDate();
                    else if (fieldType == LocalDateTime.class) return cell.getLocalDateTimeCellValue();
                }
                return convertNumericValue(cell.getNumericCellValue(), fieldInfo);
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
//                return ExcelUtils.getCellTypeValue(cell, fieldInfo); // 재귀호출
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    /**
     * 문자열 값을 지정된 타입으로 변환
     */
    private static Object convertStringValue(String value, ExcelFieldInfo fieldInfo) {
        Class<?> fieldType = fieldInfo.field.getType();
        String defaultDateTimeFormat = StringUtils.isNotBlank(fieldInfo.excelColumn.datetimeFormat()) ? fieldInfo.excelColumn.datetimeFormat() : "yyyy-MM-dd HH:mm:ss";
        String defaultDateFormat = StringUtils.isNotBlank(fieldInfo.excelColumn.datetimeFormat()) ? fieldInfo.excelColumn.datetimeFormat() : "yyyy-MM-dd";

        if (fieldType == String.class) return value;

        // number
        else if (fieldType == Integer.class || fieldType == int.class) return Integer.valueOf(value);
        else if (fieldType == Long.class || fieldType == long.class) return Long.valueOf(value);
        else if (fieldType == Double.class || fieldType == double.class) return Double.valueOf(value);
        else if (fieldType == BigDecimal.class) return new BigDecimal(value);

        // date todo : Date, Timestamp 추가
        if (fieldType == LocalDate.class) return LocalDate.parse(value, DateTimeFormatter.ofPattern(defaultDateFormat));
        else if (fieldType == LocalDateTime.class) return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(defaultDateTimeFormat));

        // bool
        else if (fieldType == Boolean.class || fieldType == boolean.class) return Boolean.valueOf(value);

        return value;
    }

    /**
     * 숫자 값을 지정된 타입으로 변환
     */
    private static Object convertNumericValue(double value, ExcelFieldInfo fieldInfo) {
        Class<?> fieldType = fieldInfo.field.getType();

        if (fieldType == String.class) return String.valueOf(value);
        else if (fieldType == Integer.class || fieldType == int.class) return (int) value;
        else if (fieldType == Long.class || fieldType == long.class) return (long) value;
        else if (fieldType == Double.class || fieldType == double.class) return value;
        else if (fieldType == BigDecimal.class) return BigDecimal.valueOf(value);
        else if (fieldType == Boolean.class || fieldType == boolean.class) return value != 0;
        return value;
    }

    /**
     * excel 설정 필드 정보
     */
    private static class ExcelFieldInfo {
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
}
