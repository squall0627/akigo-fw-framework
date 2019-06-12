package com.akigo.dao.query;

import com.akigo.core.AkigoException;
import com.akigo.core.App;
import com.akigo.core.exception.SystemException;
import com.akigo.dao.config.DatabaseSetting;
import com.akigo.dao.config.JndiRef;
import com.akigo.dao.exception.AkigoDBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionUtils {

    private static final ConnectionUtils INSTANCE = new ConnectionUtils();
    private Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);

    private static final ThreadLocal<String> currentDataSourceName = new ThreadLocal<>();

    private static String DEFAULT_DATASOURCE = "__DEFAULT__";

    private List<String> jndiNames = null;

    private Map<String, String> jndiRefMap = null;

    private boolean isJndiRef = false;

    private ConnectionUtils() {

    }

    /**
     * 接続先のデータソース名を設定します。
     *
     * @param dataSource データソース名
     */
    public void setCurrentDataSourceName(String dataSource) {
        currentDataSourceName.set(dataSource);
    }

    /**
     * 接続先のデータソース名をクリアします。
     */
    public void clearCurrentDataSourceName() {
        currentDataSourceName.remove();
    }

    /**
     * 本クラスのインスタンスを取得します。
     *
     * @return ConnectionUtilsのインスタンス
     */
    public static ConnectionUtils getInstance() {
        return INSTANCE;
    }

    private HashMap<String, Connection> spyMap = null;

    /**
     * 現在設定されているデータソース名からDB接続を返します。
     *
     * @return 接続オブジェクト
     */
    public Connection getConnection() {
        String dsName = null;
        try {

            // 接続先の取得
            dsName = currentDataSourceName.get();

            // テスト用のコネクションがあるならそちらを優先。
            if (spyMap != null) {
                if (dsName == null) {
                    dsName = DEFAULT_DATASOURCE;
                }
                return spyMap.get(dsName);
            }

            getSettings();

            // データソース名が取得できない場合はデフォルトで定義の0番目を使う
            if (dsName == null) {
                if (isJndiRef) {
                    dsName = DEFAULT_DATASOURCE;
                } else if (jndiNames.size() != 0) {
                    dsName = jndiNames.get(0);
                } else {
                    // LOG app.ymlのdatabaseSettingが空の場合に出力します。
                    logger.error("app.ymlのdatabaseSettingが空です");
                    throw new SystemException("app.ymlのdatabaseSettingが空です");
                }
            }

            // 設定ファイルに定義されていないデータソース名は許可しない
            if (!jndiNames.contains(dsName)) {
                // LOG databaseSettingに含まれないデータソース名が指定されている場合に出力します。 dsName:データソース名
                logger.error("jndiNameの定義に存在しないデータソース名です：{}", dsName);
                throw new AkigoException("jndiNameの定義に存在しないデータソース名です：'" + dsName + "'");
            }

            //keyからjndi名を取得
            if (isJndiRef) {
                dsName = jndiRefMap.get(dsName);
            }

            // LOG 接続先のデータソース名を出力します。dsName:データソース名
            logger.debug("■■■ 接続先: " + dsName + " ■■■");

            InitialContext ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx.lookup(dsName);
            return dataSource.getConnection();

        } catch (NamingException e) {
            // LOG コネクションを取得しようとした際、Exceptionを検知すると出力します。dsName:データソース名
            logger.error("コネクションの取得に失敗しました(データソース名が誤りの可能性があります):{}", dsName);
            throw new SystemException("コネクションの取得に失敗しました(データソース名が誤りの可能性があります):" + dsName, e);
        } catch (SQLException e) {
            // LOG コネクションを取得しようとした際、Exceptionを検知すると出力します。dsName:データソース名
            logger.error("コネクションの取得に失敗しました(DBが停止している可能性があります):{}", dsName);
            throw new AkigoDBException("コネクションの取得に失敗しました(DBが停止している可能性があります):" + dsName, e);
        } catch (Exception e) {
            // LOG コネクション取得処理が失敗した場合に出力します。e:Exception
            logger.error("コネクション取得処理が失敗しました", e);
            throw new SystemException("コネクション取得処理が失敗しました", e);
        }
    }

    private void getSettings() {
        // 初回のみ設定ファイルからデータソース一覧を取得

        if (jndiNames == null) {
            App.run();
            DatabaseSetting databaseSetting = App.setting(DatabaseSetting.class);
            List<JndiRef> jndiRef = databaseSetting.getJndiRef();
            if (jndiRef == null || jndiRef.isEmpty()) {
                jndiNames = App.setting(DatabaseSetting.class).getJndiNames();
            } else {
                isJndiRef = true;
                jndiNames = new ArrayList<>();
                jndiRefMap = new HashMap<>();
                jndiRef.stream()
                        .forEach(j -> {
                            jndiNames.add(j.getKey());
                            jndiRefMap.put(j.getKey(), j.getJndiName());
                        });
            }
        }
    }

    /**
     * テストで使用するコネクションを設定します。
     *
     * @param spyConnection DB接続
     */
    public void setSpyConnection(Connection spyConnection) {
        setSpyConnection(DEFAULT_DATASOURCE, spyConnection);
    }

    /**
     * データソース名を指定してテストで使用するコネクションを設定します。
     *
     * @param dsName        データソース名
     * @param spyConnection DB接続
     */
    public void setSpyConnection(String dsName, Connection spyConnection) {
        if (spyMap == null) {
            spyMap = new HashMap<>();
        }
        spyMap.put(dsName, spyConnection);
    }

    /**
     * テストで使用したDB接続の参照を開放します。<br/>
     * Connection.closeはコールしないので自前で実装してください。
     */
    public void clearSpyConnection() {
        this.spyMap.clear();
        this.spyMap = null;
    }
}
