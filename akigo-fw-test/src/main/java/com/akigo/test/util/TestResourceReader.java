package com.akigo.test.util;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 機能名 : 単体テスト支援ツールリソース読込機能インタフェース<br>
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
public interface TestResourceReader {

    String TEST_DATA_ROOT = "test-data";

    static TestResourceReader of(String resPath) {
        Path seedsPath = Paths.get(TEST_DATA_ROOT);
        if (!Files.exists(seedsPath)) {
            throw new RuntimeException("Directory '" + seedsPath.toString() + "' is missing.");
        }

        Path targetFilePath = seedsPath.resolve(resPath);
        if (!Files.exists(targetFilePath)) {
            throw new RuntimeException("File '" + targetFilePath.toString() + "' is missing.");
        }

        File targetFile = targetFilePath.toFile();

        String extension = null;
        int lastDotPosition = targetFile.getName().lastIndexOf(".");
        if (lastDotPosition != -1) {
            extension = targetFile.getName().substring(lastDotPosition + 1);
        }

        if ("xlsx".equals(extension) ||
            "xlsm".equals(extension) ||
            "xls".equals(extension)) {
            return new ExcelResourceReader(targetFile);
        } else {
            throw new RuntimeException("Unsupported file type: " + extension);
        }
    }

    <T> T read(Class<T> clazz);

    <T> List<T> readList(Class<T> clazz);

    List<ResourceProperty> getResourceProperties();

    default List<Field> getPropertyFields(Class<?> clazz) {
        List<ResourceProperty> resourceProperties = getResourceProperties();
        List<String> propertyNames = resourceProperties.stream().map(
            ResourceProperty::getPropertyName).collect(Collectors.toList());
        return Arrays.stream(clazz.getDeclaredFields()).filter(
            f -> propertyNames.contains(f.getName())).collect(Collectors.toList());
    }

}
