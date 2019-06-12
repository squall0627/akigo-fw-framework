package com.akigo.test.seeder.dbsetup;

import java.util.List;
import java.util.Map;

public class Seed {

    private String tableName;

    private Boolean deleteFlg;

    private Map<String, Object> defaultValues;

    private List<Map<String, Object>> dataList;

    /**
     * テーブル名を取得します。
     *
     * @return テーブル名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * テーブル名を設定します。
     *
     * @param tableName テーブル名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * tableNameで指定しているテーブルの全レコードを、登録前に削除するかどうかを取得します。
     *
     * @return 削除する場合はtrue
     */
    public Boolean getDeleteFlg() {
        return deleteFlg;
    }

    /**
     * tableNameで指定しているテーブルの全レコードを、登録前に削除するかどうかを設定します。
     *
     * @param deleteFlg 削除する場合はtrue
     */
    public void setDeleteFlg(Boolean deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    /**
     * すべてのレコードに追加されるデフォルトの値を取得します。
     *
     * @return デフォルトの値
     */
    public Map<String, Object> getDefaultValues() {
        return defaultValues;
    }

    /**
     * すべてのレコードに追加されるデフォルトの値を設定します。
     *
     * @param defaultValues デフォルトの値
     */
    public void setDefaultValues(Map<String, Object> defaultValues) {
        this.defaultValues = defaultValues;
    }

    /**
     * データのリストを取得します。
     *
     * @return データのリスト
     */
    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    /**
     * データのリストを設定します。
     *
     * @param dataList データのリスト
     */
    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }
}
