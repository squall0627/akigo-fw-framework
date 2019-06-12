package com.akigo.test.seeder.dbsetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.destination.DriverManagerDestination;
import com.ninja_squad.dbsetup.operation.Operation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;

public class Seeder {

    private Consumer<String> logger;

    private Path seedsPath;

    private Destination destination;

    /**
     * 新規インスタンスを構築します。
     *
     * @param seedsDir モジュールのディレクトリパス
     * @param logger   メッセージを引数に取り、出力する関数
     * @throws IOException 指定したパスが不正な場合
     */
    public Seeder(String seedsDir, Consumer<String> logger) throws IOException {
        this.logger = Objects.requireNonNull(logger);
        seedsPath = Paths.get(seedsDir);
        if (!Files.exists(seedsPath)) {
            throw new FileNotFoundException("Directory '" + seedsPath.toString() + "' is missing.");
        }

        Path configPath = seedsPath.resolve("db/database.yml");
        if (!Files.exists(configPath)) {
            throw new FileNotFoundException("Config-file '" + configPath.toString() + "' is missing.");
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        DatabaseConfigration config = mapper.readValue(configPath.toFile(), DatabaseConfigration.class);
        destination = new DriverManagerDestination(config.getUrl(), config.getUser(), config.getPass());
    }

    /**
     * シーダーを実行します。
     *
     * @param target Seed情報ファイルを格納しているディレクトリorファイル名
     * @throws IOException Seed情報ファイル読み取りに失敗した場合
     */
    public void execute(String target) throws IOException {
        Path path = seedsPath;
        if (target != null && !target.isEmpty()) {
            path = seedsPath.resolve(target);
        }

        log("Start to seed!!");
        List<Seed> seeds = createSeeds(path);
        List<Operation> operationList = seeds.stream().map(s -> {
            String tableName = s.getTableName();
            log("INSERT:" + tableName);
            Map<String, Object> defaultValues = s.getDefaultValues();
            List<Map<String, Object>> dataList = s.getDataList();
            Operation operation;
            if (s.getDeleteFlg()) {
                operation = sequenceOf(Operations.deleteAll(tableName), Operations.insert(tableName, dataList, defaultValues));
            } else {
                operation = sequenceOf(Operations.insert(tableName, dataList, defaultValues));
            }
            return operation;
        }).collect(Collectors.toList());

        Operation operation = sequenceOf(operationList);
        DbSetup dbSetup = new DbSetup(destination, operation);
        try {
            dbSetup.launch();
            log("Seed successfully!!");
        } catch (Exception e) {
            log("Seed failed...");
            throw e;
        }
    }

    /**
     * seedsPath配下のSeed情報ファイルを読み取り、シーダーを実行します。
     *
     * @throws IOException Seed情報ファイル読み取りに失敗した場合
     */
    public void execute() throws IOException {
        execute(null);
    }

    /**
     * 向かい先のDBへのコネクションを取得します。
     *
     * @return コネクション
     * @throws SQLException コネクションが帰ってこなかった場合
     */
    public Connection getConnection() throws SQLException {
        return destination.getConnection();
    }

    private List<Seed> createSeeds(Path path) throws IOException {
        File[] files;
        if (Files.isDirectory(path)) {
            files = path.toFile().listFiles();
        } else {
            files = new File[]{path.toFile()};
        }
        List<Seed> seeds = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                try {
                    Seed seed = mapper.readValue(file, Seed.class);
                    log("Read " + file.getName() + "...[OK]");
                    seeds.add(seed);
                } catch (IOException e) {
                    log("Read" + file.getName() + "...[NG]");
                    throw e;
                }
            }
        }
        return seeds;
    }

    private void log(String message) {
        this.logger.accept(message);
    }
}
