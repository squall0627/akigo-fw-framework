package com.akigo.test.jdbc;

import com.akigo.dao.exception.AkigoDBException;
import com.akigo.dao.query.ConnectionFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.util.Stack;


public class ConnectionStackHolder implements Closeable {
    private static final ConnectionStackHolder INSTANCE = new ConnectionStackHolder();

    private static ThreadLocal<Stack<ConnectionFactory>> factories = new ThreadLocal<>();

    private Stack<ConnectionFactory> getStack() {
        Stack<ConnectionFactory> stack = factories.get();
        if (stack == null) {
            stack = new Stack<ConnectionFactory>();
            factories.set(stack);
        }
        return stack;
    }

    public static ConnectionStackHolder getInstance() {
//        ResourceCleaningFilter.setCloseable(INSTANCE);
        return INSTANCE;
    }

    public void push(ConnectionFactory factory) {
        getStack().push(factory);
    }

    public ConnectionFactory pop() {

        ConnectionFactory factory = getStack().pop();
        if (getStack().isEmpty()) {
            factories.remove();
        }
        return factory;
    }

    public Connection peek() {

        ConnectionFactory factory = getStack().peek();
        if (factory == null) {
            throw new AkigoDBException(
                    "ConnectionFactory is null. Did you call ConnectionStackHolder#push method?");
        }

        Connection con = factory.create();
        if (con == null) {
            throw new AkigoDBException(
                    "Connection is null. Did you call ConnectionStackHolder#push method?");
        }
        return con;
    }

    @Override
    public void close() throws IOException {
        factories.remove();
    }
}
