package com.akigo.test.seeder.dbsetup;

public class DatabaseConfigration {

    private String url;

    private String user;

    private String pass;

    /**
     * DBを示すURLを取得します。
     *
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * DBを示すURLを設定します。
     *
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * ユーザー名を取得します。
     *
     * @return ユーザー名
     */
    public String getUser() {
        return user;
    }

    /**
     * ユーザー名を設定します。
     *
     * @param user ユーザー名
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * パスワードを取得します。
     *
     * @return パスワード
     */
    public String getPass() {
        return pass;
    }

    /**
     * パスワードを設定します。
     *
     * @param pass パスワード
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
}
