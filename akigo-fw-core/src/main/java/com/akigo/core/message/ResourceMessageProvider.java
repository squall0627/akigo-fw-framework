package com.akigo.core.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ResourceMessageProvider implements MessageProvider {

    private static Logger logger = LoggerFactory.getLogger(ResourceMessageProvider.class);

    //システム、業務共通等
    private MergeResourceBundle mr = new MergeResourceBundle();

    //app.ymlで指定されたDBMessageProvider
    private MessageProvider messageProvider;

    /**
     * app-messagesを基底名としてリソースバンドルを取得し、システム、業務共通用のリソースバンドルに追加します。<br />
     */
    public ResourceMessageProvider() {
        try {
            mr.addResource(ResourceBundle.getBundle("app-messages"));
        } catch (MissingResourceException e) {
            // LOG app-messagesを基底名とするリソース・バンドルが見つからない場合に出力されます
            logger.info("app-messages.propertiesがみつかりません");
        }
    }

    /**
     * アプリ用MessageProviderを追加する。
     *
     * @param messageProvider メッセージモジュールで指定されたもの
     */
    public void addMessageProvider(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    /**
     * アプリ用MessageProviderを取得する。
     *
     * @return MessageProvider
     */
    public MessageProvider getMessageProvider() {
        return this.messageProvider;
    }

    /**
     * リーソースを追加する。</br>
     * 同一キーの物があった場合、上書きされる。
     *
     * @param resource 追加のリソース
     */
    public void addResource(final ResourceBundle resource) {
        mr.addResource(resource);
    }

    /**
     * 既にリソースを保持しているかどうか
     *
     * @param name リソース名
     * @return 保持している場合はtrue
     */
    public boolean contains(String name) {
        return this.mr.contains(name);
    }

    /**
     * メッセージプロバイダが存在する場合は、メッセージプロバイダからメッセージIDをキーにメッセージを取得します。
     * メッセージプロバイダからメッセージを取得できない場合は、システム、業務共通のリソースバンドルからメッセージIDをキーにメッセージを取得します。
     *
     * @param messageId メッセージID
     * @return メッセージ
     */
    @Override
    public Optional<String> get(String messageId) {
        try {
            Optional<String> mes = Optional.empty();

            if (null != messageProvider) {
                mes = messageProvider.get(messageId);
            }
            if (!mes.isPresent()) {
                mes = Optional.ofNullable(mr.get(messageId));
            }
            return mes;
        } catch (MissingResourceException | ClassCastException e) {
            // LOG メッセージIDを利用しリソースファイルからメッセージ内容を取得できなかった時に出力されます。
            logger.warn(messageId + " is missing in app-messages.properties.");
        }
        return Optional.empty();
    }

    /**
     * メッセージプロバイダが存在する場合は、メッセージプロバイダからメッセージIDをキーに{@link AppMessage}を取得します。
     * メッセージプロバイダからメッセージを取得できない場合は、システム、業務共通のリソースバンドルからメッセージIDをキーに{@link AppMessage}を取得します。
     * 業務共通のリソースバンドルからメッセージを取得できない場合は、空の{@link Optional}を返します。
     *
     * @param messageId メッセージID
     * @return Optional<AppMessage>{@link AppMessage}
     */
    @Override
    public Optional<AppMessage> getMessage(String messageId) {

        Optional<AppMessage> mes = Optional.empty();
        if (null != messageProvider) {
            // app.ymlで指定されたMessageProviderから取得
            mes = messageProvider.getMessage(messageId);
        }

        // 値が取れなかった場合、
        if (!mes.isPresent()) {
            String message = getSystem(messageId).orElse(null);
            if (null != message) {
                AppMessage msg = new AppMessage();
                msg.setKey(messageId);
                msg.setPlainMessage(message);
                return Optional.of(msg);
            }
            return Optional.empty();
        } else {
            return mes;
        }
    }

    /**
     * 追加メッセージのリソースからメッセージを取得する。
     *
     * @param messageId メッセージID
     * @return メッセージ文言
     */
    private Optional<String> getSystem(String messageId) {
        try {
            return Optional.ofNullable(mr.get(messageId));
        } catch (MissingResourceException | ClassCastException e) {
            // LOG メッセージIDを利用しリソースファイルからメッセージ内容を取得できなかった時に出力されます。
            logger.warn(messageId + " is missing in app-messages.propeeties.");
        }
        return Optional.empty();
    }
}
