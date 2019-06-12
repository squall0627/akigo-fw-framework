package com.akigo.test.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akigo.core.util.ClassUtils;
import com.akigo.core.util.DateTimes;
import com.akigo.core.util.Strings;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 機能名 : 単体テスト支援ツールEXCELリソース読込機能クラス<br>
 * <br>
 *
 * <pre>
 * 使用例：
 * {@code
 *  TestResourceReader reader = TestResourceReader.of("XXXX/expectedResult/予想結果.xlsx");
 *  List<XXXX_Dto> expectedResults = reader.readList(XXXX_Dto.class);
 * }
 * </pre>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/25
 */
public class ExcelResourceReader implements TestResourceReader {

    private static final Logger logger = LoggerFactory.getLogger(ExcelResourceReader.class);

    private static final int ITEM_NAME_ROW_INDEX = 0;
    private static final int PROPERTY_NAME_ROW_INDEX = 1;
    private static final int DATA_START_ROW_INDEX = 2;

    private final Workbook workbook;

    public ExcelResourceReader(File file) {
        try {
            this.workbook = WorkbookFactory.create(file);
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T read(Class<T> clazz) {
        Sheet sheet = this.workbook.getSheetAt(0);

        // プロパティーを読む
        List<Field> propertyFields = getPropertyFields(clazz);

        for (int i = DATA_START_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                return readSingleRow(clazz, propertyFields, row);
            }
        }
        return null;
    }

    @Override
    public <T> List<T> readList(Class<T> clazz) {
        Sheet sheet = this.workbook.getSheetAt(0);

        List<T> results = new ArrayList<>();
        // プロパティーを読む
        List<Field> propertyFields = getPropertyFields(clazz);

        for (int i = DATA_START_ROW_INDEX; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                results.add(readSingleRow(clazz, propertyFields, row));
            }
        }
        return results;
    }

    @Override
    public List<ResourceProperty> getResourceProperties() {
        List<ResourceProperty> resourceProperties = new ArrayList<>();
        Row propertyNameRow = this.workbook.getSheetAt(0).getRow(PROPERTY_NAME_ROW_INDEX);
        Row itemNameRow = this.workbook.getSheetAt(0).getRow(ITEM_NAME_ROW_INDEX);
        short lastCellNum = propertyNameRow.getLastCellNum();
        for (int i = 0; i < lastCellNum; i++) {
            resourceProperties.add(
                    new ResourceProperty(
                            propertyNameRow.getCell(i).getStringCellValue(),
                            itemNameRow.getCell(i).getStringCellValue()));
        }
        return resourceProperties;
    }

    private <T> T readSingleRow(Class<T> clazz, List<Field> propertyFields, Row row) {
        T t = ClassUtils.newInstance(clazz);
        int index = 0;
        for (Field f : propertyFields) {
            Cell cell = row.getCell(index);
            if (cell != null) {
                f.setAccessible(true);
                try {
                    f.set(t, getValue(f.getType(), cell));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            index++;
        }
        return t;
    }

    private Object getValue(Class<?> type, Cell cell) {
        if (cell == null) {
            return null;
        }

        if (CellType.ERROR == cell.getCellTypeEnum()) {
            throw new RuntimeException("数式エラー");
        }

        if (CellType.FORMULA == cell.getCellTypeEnum()) {
            throw new RuntimeException(String.format(
                    "数式以外で入力してください。([%s]シート [%s]行 [%s]列)",
                    cell.getSheet().getSheetName(),
                    cell.getRowIndex() + 1,
                    cell.getColumnIndex() + 1));
        }

        Object valueObject = null;
        Object cellValue = null;
        try {
            // まずセルタイプを判定して、セルタイプによって値を取得
            if (CellType.STRING == cell.getCellTypeEnum()) {
                cellValue = cell.getStringCellValue();
            } else if (CellType.NUMERIC == cell.getCellTypeEnum()) {
                if (isCellDateFormatted(cell)) {
                    // 日付場合
                    cellValue = cell.getDateCellValue().toString();
                } else {
                    // 数値場合
                    cellValue = cell.getNumericCellValue();
                    if (Boolean.class == type) {
                        cellValue = Boolean.valueOf(String.valueOf(cellValue));
                    }
                }
            } else if (CellType.BOOLEAN == cell.getCellTypeEnum()) {
                cellValue = cell.getBooleanCellValue();
            } else if (CellType.BLANK == cell.getCellTypeEnum()) {
                if (String.class == type) {
                    cellValue = cell.getStringCellValue();
                } else if (Boolean.class == type || boolean.class == type) {
                    cellValue = cell.getBooleanCellValue();
                } else {
                    cellValue = cell.getNumericCellValue();
                }
            }

            // フィールドの型によって、セルの値の型を変換する
            if (cellValue != null) {
                if (String.class == type) {
                    if (cellValue instanceof Double) {
                        valueObject = removeLastZero(
                                BigDecimal.valueOf((Double) cellValue).toPlainString());
                    } else {
                        valueObject = cellValue.toString();
                    }
                } else if (Long.class == type || long.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = new BigDecimal(removeLastZero(cellValue.toString()))
                                .longValueExact();
                    }
                } else if (Integer.class == type || int.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = new BigDecimal(removeLastZero(cellValue.toString()))
                                .intValueExact();
                    }
                } else if (Short.class == type || short.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = new BigDecimal(removeLastZero(cellValue.toString()))
                                .shortValueExact();
                    }
                } else if (Byte.class == type || byte.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = new BigDecimal(removeLastZero(cellValue.toString()))
                                .byteValueExact();
                    }
                } else if (Double.class == type || double.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = Double.valueOf(cellValue.toString());
                    }
                } else if (Float.class == type || float.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = Double.valueOf(cellValue.toString()).floatValue();
                    }
                } else if (Boolean.class == type || boolean.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = Boolean.valueOf(cellValue.toString());
                    }
                } else if (BigDecimal.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = new BigDecimal(removeLastZero(cellValue.toString()));
                    }
                } else if (BigInteger.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        valueObject = BigInteger.valueOf(
                                Double.valueOf(cellValue.toString()).longValue());
                    }
                } else if (Date.class.isAssignableFrom(type)) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        if (cellValue instanceof String) {
                            if (java.sql.Date.class == type) {
                                try {
                                    valueObject = java.sql.Date.valueOf(cellValue.toString());
                                } catch (IllegalArgumentException e) {
                                    logger.error(
                                            "日付フォーマットが間違っています ({}行目{}列目)",
                                            cell.getRowIndex() + 1,
                                            cell.getColumnIndex() + 1);
                                    logger.error("正しいフォーマット：{}", "2007-12-03");
                                }
                            } else if (java.sql.Timestamp.class == type) {
                                try {
                                    valueObject = java.sql.Timestamp.valueOf(cellValue.toString());
                                } catch (IllegalArgumentException e) {
                                    logger.error(
                                            "日付フォーマットが間違っています ({}行目{}列目)",
                                            cell.getRowIndex() + 1,
                                            cell.getColumnIndex() + 1);
                                    logger.error("正しいフォーマット：{}", "2007-12-03 10:15:30.123");
                                }
                            } else if (java.sql.Time.class == type) {
                                try {
                                    valueObject = java.sql.Time.valueOf(cellValue.toString());
                                } catch (IllegalArgumentException e) {
                                    logger.error(
                                            "日付フォーマットが間違っています ({}行目{}列目)",
                                            cell.getRowIndex() + 1,
                                            cell.getColumnIndex() + 1);
                                    logger.error("正しいフォーマット：{}", "10:15:30");
                                }
                            } else {
                                throw new RuntimeException("Unsupported date type:" + type
                                        .getTypeName());
                            }
                        } else {
                            checkDate(cellValue, cell);
                            Date date = (Date) cellValue;
                            if (java.sql.Date.class == type) {
                                valueObject = new java.sql.Date(date.getTime());
                            } else if (java.sql.Timestamp.class == type) {
                                valueObject = new java.sql.Timestamp(date.getTime());
                            } else if (java.sql.Time.class == type) {
                                valueObject = new java.sql.Time(date.getTime());
                            } else {
                                valueObject = date;
                            }
                        }
                    }
                } else if (LocalDate.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        if (cellValue instanceof String) {
                            try {
                                valueObject = LocalDate.parse(cellValue.toString());
                            } catch (DateTimeParseException e) {
                                logger.error(
                                        "日付フォーマットが間違っています ({}行目{}列目)",
                                        cell.getRowIndex() + 1,
                                        cell.getColumnIndex() + 1);
                                logger.error("正しいフォーマット：{}", "2007-12-03");
                            }
                        } else {
                            checkDate(cellValue, cell);
                            valueObject = DateTimes.toLocalDateFrom((Date) cellValue);
                        }
                    }
                } else if (LocalTime.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        if (cellValue instanceof String) {
                            try {
                                valueObject = LocalTime.parse(cellValue.toString());
                            } catch (DateTimeParseException e) {
                                logger.error(
                                        "日付フォーマットが間違っています ({}行目{}列目)",
                                        cell.getRowIndex() + 1,
                                        cell.getColumnIndex() + 1);
                                logger.error("正しいフォーマット：{}", "10:15:30");
                            }
                        } else {
                            checkDate(cellValue, cell);
                            valueObject = DateTimes.toLocalTimeFrom((Date) cellValue);
                        }
                    }
                } else if (LocalDateTime.class == type) {
                    if (!Strings.isNullOrEmpty(cellValue.toString())) {
                        if (cellValue instanceof String) {
                            try {
                                valueObject = LocalDateTime.parse(cellValue.toString());
                            } catch (DateTimeParseException e) {
                                logger.error(
                                        "日付フォーマットが間違っています ({}行目{}列目)",
                                        cell.getRowIndex() + 1,
                                        cell.getColumnIndex() + 1);
                                logger.error("正しいフォーマット：{}", "2007-12-03T10:15:30");
                            }
                        } else {
                            checkDate(cellValue, cell);
                            valueObject = DateTimes.toLocalDateTimeFrom((Date) cellValue);
                        }
                    }
                }
            }
        } catch (ArithmeticException e) {
            logger.warn(
                    "整数で入力してください。({}行目{}列目)",
                    cell.getRowIndex() + 1,
                    cell.getColumnIndex() + 1);
            logger.warn("ArithmeticException 不正な値", e);
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            logger.error("数値変換失敗 対象セル[{},{}]", cell.getRowIndex() + 1, cell.getColumnIndex() + 1);
            logger.error("NumberFormatException 数値変換失敗", e);
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            logger.error("不正な引数 対象フィールド[{},{}]", cell.getRowIndex() + 1, cell.getColumnIndex() + 1);
            logger.error("IllegalArgumentException 不正な引数", e);
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            logger.error(
                    "セルの型が違います ({}行目{}列目)",
                    cell.getRowIndex() + 1,
                    cell.getColumnIndex() + 1);
            logger.error("IllegalStateException 不正な値", e);
            throw new RuntimeException(e);
        }
        return valueObject;
    }

    /**
     * セル値型のDate判定<br>
     *
     * @param cellValue セル値
     * @param cell      セル
     */
    private void checkDate(Object cellValue, Cell cell) {
        if (!(cellValue instanceof Date)) {
            logger.error(
                    "checkDate セルの型が間違っています ({}行目{}列目)",
                    cell.getRowIndex() + 1,
                    cell.getColumnIndex() + 1);
            logger.error("不正な値", cellValue);
            throw new RuntimeException();
        }
    }

    private String removeLastZero(String str) {
        if (str != null && str.indexOf(".") != -1 && str.endsWith("0")) {
            // 小数かつ小数部が０で終了する場合、最後の0を消す
            String strWithoutZero = removeLastZero(str.substring(0, str.length() - 1));
            if (strWithoutZero.endsWith(".")) {
                return strWithoutZero.substring(0, strWithoutZero.length() - 1);
            } else {
                return strWithoutZero;
            }
        } else {
            return str;
        }
    }

    /**
     * セルの表示値が日付形式であるか判定するメソッド
     * <p>
     * DateUtilのisCellDateFormattedでは漢字を含むフォーマットが日付形式として認識されないため、
     * 年、月、日、時、分、秒のいずれかを含む場合も日付形式であると判定するように拡張
     * </p>
     *
     * @param cell 判定対象のセル
     * @return 日付形式であればTrue、日付形式でなければfalse
     */
    private boolean isCellDateFormatted(Cell cell) {
        String dataFormatString = cell.getCellStyle().getDataFormatString();

        if (null == dataFormatString) {
            dataFormatString = "";
        }

        if (DateUtil.isCellDateFormatted(cell) || dataFormatString.contains("年")
                || dataFormatString.contains("月") || dataFormatString.contains("日")
                || dataFormatString.contains("時") || dataFormatString.contains("分")
                || dataFormatString.contains("秒")) {
            return true;
        }
        return false;

    }

}
