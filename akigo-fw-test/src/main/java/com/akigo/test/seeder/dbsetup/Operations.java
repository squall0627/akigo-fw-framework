package com.akigo.test.seeder.dbsetup;

import com.ninja_squad.dbsetup.operation.Insert.Builder;
import com.ninja_squad.dbsetup.operation.Insert.RowBuilder;
import com.ninja_squad.dbsetup.operation.Operation;

import java.util.List;
import java.util.Map;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;

public class Operations {

    /**
     * テーブルのデータをすべて削除するOperationを取得します。
     *
     * @param tableName テーブル名
     * @return Operation
     */
    public static Operation deleteAll(String tableName) {
        return deleteAllFrom(tableName);
    }

    /**
     * テーブルにデータを挿入するOperationを取得します。
     *
     * @param tableName     テーブル名
     * @param dataList      データのリスト
     * @param defaultValues すべてのレコードに追加されるデフォルトの値
     * @return Operation
     */
    public static Operation insert(String tableName, List<Map<String, Object>> dataList, Map<String, Object> defaultValues) {

        Builder builder = insertInto(tableName);

        if (defaultValues != null && !defaultValues.isEmpty()) {
            defaultValues.entrySet().stream().forEach(d -> {
                builder.withDefaultValue(d.getKey(), d.getValue());
            });
        }

        dataList.stream().forEach(s -> {
            RowBuilder row = builder.row();
            s.entrySet().stream().forEach(e -> {
                row.column(e.getKey(), e.getValue());
            });
            row.end();
        });
        return builder.build();
    }
}
