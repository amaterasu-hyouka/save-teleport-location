package jp.amaterasu_hyouka.saveteleportlocation.db.mybatis;

import jp.amaterasu_hyouka.saveteleportlocation.PluginFile;
import jp.amaterasu_hyouka.saveteleportlocation.exception.FileCreationException;
import jp.amaterasu_hyouka.saveteleportlocation.repository.LocationCategoryMapper;
import jp.amaterasu_hyouka.saveteleportlocation.repository.PlayerMapper;
import jp.amaterasu_hyouka.saveteleportlocation.repository.SaveTeleportLocationMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public enum DatabaseRegistry {
    SAVE_TELEPORT_LOCATION("save_teleport_location", PlayerMapper.class, LocationCategoryMapper.class, SaveTeleportLocationMapper.class);

    private final String dbName;
    private final Class<?>[] mappers;
    private SqlSessionFactory factory;
    public SqlSessionFactory getSqlSessionFactory(){
        return factory;
    }

    private static File getDatabaseDir(){return new File(PluginFile.getPluginPath(), "database");}
    private static File getDatabaseFile(String dbName){return new File(getDatabaseDir(), dbName + ".db");}
    private static String getSqlFileName(String dbName){return "sql" + "/" + dbName + ".sql";}

    DatabaseRegistry(String dbName, Class<?>... mappers){
        this.dbName = dbName;
        this.mappers = mappers;
    }

    public static void initializeAll() throws FileCreationException{
        for (DatabaseRegistry registry : values()) {
            registry.initialize();
        }
    }
    private void initialize() throws FileCreationException{
        ensureDatabaseDirectory();
        ensureDatabaseFile();
        executeSqlFileIfExists();
        this.factory = createFactory();
    }
    private void ensureDatabaseDirectory() throws FileCreationException {
        File dbDir = getDatabaseDir();
        if(dbDir.exists())return;
        if(!dbDir.mkdirs()){
            throw new FileCreationException("ディレクトリの作成に失敗しました: " + dbDir.getPath());
        }
    }
    private void ensureDatabaseFile() throws FileCreationException {
        File dbFile = getDatabaseFile(dbName);
        if(dbFile.exists())return;
        try{
            if(!dbFile.createNewFile()){
                throw new FileCreationException("ファイルの作成に失敗しました: " + dbFile.getPath());
            }
        }catch(Exception e){
            throw new FileCreationException(e);
        }
    }
    private void executeSqlFileIfExists() throws FileCreationException {
        String resourcePath = getSqlFileName(dbName);
        if(DatabaseRegistry.class.getClassLoader().getResource(resourcePath) == null)return;

        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:" + getDatabaseFile(dbName).getAbsolutePath());
            InputStream inputStream = DatabaseRegistry.class.getClassLoader().getResourceAsStream(resourcePath)){
            if(inputStream == null)throw new FileNotFoundException("SQLファイルが見つかりません: " + resourcePath);
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
                StringBuilder sql = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                    sql.append(line).append("\n");
                    if (line.trim().endsWith(";")) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(sql.toString());
                            sql.setLength(0);
                        }
                    }
                }
            }
        }catch(Exception e){
            throw new FileCreationException("SQLファイルの実行に失敗しました: " + resourcePath, e);
        }
    }

    private SqlSessionFactory createFactory(){
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:" + getDatabaseFile(dbName).getAbsolutePath() + "?busy_timeout=10000");

        Environment env = new Environment("env", new JdbcTransactionFactory(), dataSource);
        Configuration config = new Configuration(env);

        config.setMapUnderscoreToCamelCase(true);

        for (Class<?> mapper : mappers)config.addMapper(mapper);
        return new SqlSessionFactoryBuilder().build(config);
    }
}
