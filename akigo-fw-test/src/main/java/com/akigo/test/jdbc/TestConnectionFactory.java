package com.akigo.test.jdbc;

import com.akigo.dao.query.ConnectionFactory;
import com.akigo.test.seeder.dbsetup.DatabaseConfigration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 機能名 : 単体テスト用コネクションファクトリ<br>
 * <br>
 *
 * @author 作成者：chenhao
 * @since 作成日：2019/3/20
 */
public class TestConnectionFactory implements ConnectionFactory {

    private int refCount = 0;

    private final Connection connection;

    private boolean error;

    public TestConnectionFactory(Connection connection) {
        this.connection = connection;
        incrementRefAndGet();
    }

    public TestConnectionFactory() {
        Path seedsPath = Paths.get("test-data");
        if (!Files.exists(seedsPath)) {
            throw new RuntimeException("Directory '" + seedsPath.toString() + "' is missing.");
        }

        Path configPath = seedsPath.resolve("db/database.yml");
        if (!Files.exists(configPath)) {
            throw new RuntimeException("Config-file '" + configPath.toString() + "' is missing.");
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        DatabaseConfigration config;
        try {
            config = mapper.readValue(configPath.toFile(), DatabaseConfigration.class);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
        Destination destination = new DriverManagerDestination(config.getUrl(), config.getUser(),
                config.getPass());
        try {
            Connection connection = destination.getConnection();
            connection.setAutoCommit(false);
            this.connection = connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        incrementRefAndGet();
    }

    @Override
    public Connection create() {
        return this.connection;
    }

    public int getRefCount() {
        return this.refCount;
    }

    public int incrementRefAndGet() {
        this.refCount++;
        return this.refCount;
    }

    public int decrementRefAndGet() {
        this.refCount--;
        return this.refCount;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean hasError() {
        return this.error;
    }

}
