package com.akigo.test.jdbc.interceptor;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.EmptyStackException;

import com.akigo.core.exception.ApplicationException;
import com.akigo.dao.query.ConnectionFactory;
import com.akigo.dao.query.ConnectionUtils;
import com.akigo.test.jdbc.ConnectionStackHolder;
import com.akigo.test.jdbc.TestConnectionFactory;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

/**
 * 機能名 : 単体テスト用Txインターセプタ<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/20
 */
public class TestTxInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
            throws Throwable {

        TestConnectionFactory peekConnectionFactory = null;
        try {
            ConnectionFactory cf = ConnectionStackHolder.getInstance().pop();
            if (cf instanceof TestConnectionFactory) {
                peekConnectionFactory = (TestConnectionFactory) cf;
            } else {
                peekConnectionFactory = new TestConnectionFactory(cf.create());
            }
            peekConnectionFactory.incrementRefAndGet();
        } catch (EmptyStackException e) {
            peekConnectionFactory = new TestConnectionFactory();
        }
        ConnectionStackHolder.getInstance().push(peekConnectionFactory);

        ConnectionUtils.getInstance().setSpyConnection(peekConnectionFactory.create());

        Object result = null;

        try {
            result = proxy.invokeSuper(obj, args);
        } catch (RuntimeException | ApplicationException e) {
            peekConnectionFactory.setError(true);
            tryEndTransaction();
            throw e;
        } catch (Throwable e) {
            // DBコネンクション閉じる
            tryEndTransaction();
            throw e;
        }

        // DBコネンクション閉じる
        tryEndTransaction();

        return result;
    }

    private void tryEndTransaction() throws SQLException {
        TestConnectionFactory peekConnectionFactory = (TestConnectionFactory) ConnectionStackHolder
                .getInstance().pop();
        if (peekConnectionFactory.decrementRefAndGet() == 0) {
            if (peekConnectionFactory.hasError()) {
                peekConnectionFactory.create().rollback();
            } else {
                peekConnectionFactory.create().commit();
            }
            peekConnectionFactory.create().close();

            try {
                ConnectionUtils.getInstance().setSpyConnection(
                        ConnectionStackHolder.getInstance().peek());
            } catch (EmptyStackException e) {
                ConnectionUtils.getInstance().clearSpyConnection();
            }
        } else {
            ConnectionStackHolder.getInstance().push(peekConnectionFactory);
        }
    }

}
