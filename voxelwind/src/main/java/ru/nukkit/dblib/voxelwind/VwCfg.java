package ru.nukkit.dblib.voxelwind;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voxelwind.server.VoxelwindServer;
import ru.nukkit.dblib.Cfg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class VwCfg implements Cfg {


    @JsonProperty ("general")
    public GeneralParams generalParams = new GeneralParams();

    @JsonProperty ("DbLib")
    public DbLibParams dbLibParams = new DbLibParams();

    @JsonProperty ("ORMLite")
    public ORMLiteParams ormLiteParams = new ORMLiteParams();

    @JsonProperty ("SQLite")
    public SQLiteParams sqliteParams = new SQLiteParams();

    @JsonProperty ("MySQL")
    public MySQLParams mySqlParams = new MySQLParams();


    public static class GeneralParams {
        @JsonProperty("language")
        public String language = "default";

        @JsonProperty("save-translation")
        public boolean saveLanguage = false;

        @JsonProperty("debug-mode")
        boolean debugMode = false;
    }



    public static class DbLibParams {
        @JsonProperty("use-MySQL")
        public boolean dbUseMySQL = false;
    }

    public static class ORMLiteParams {

        @JsonProperty("debug")
        public boolean debugLog = false;

        @JsonProperty("keep-alive-interval")
        public int ormLiteKeepAlive = 0;
    }


    public static class SQLiteParams {
        @JsonProperty("file-name")
        public String dbFileName = DbLibPlugin.getFile("nukkit.db").toFile().toString();
    }

    public static class MySQLParams {

        @JsonProperty("host")
        public String dbMySqlUrl = "localhost";

        @JsonProperty("port")
        public int dbMySqlPort = 3306;

        @JsonProperty("database")
        public String dbMySqlDatabase = "db";

        @JsonProperty("username")
        public String dbMySqlUsername = "nukkit";

        @JsonProperty("password")
        public String dbMySqlPassword = "tikkun";


    }

    @JsonIgnore
    public String getDbUrl (){
        return Cfg.super.getDbUrl();
    }

    public String language() {
        return this.generalParams.language;
    }



    public boolean saveLanguage() {
        return this.generalParams.saveLanguage;
    }

    public boolean debugMode() {
        return this.generalParams.debugMode;
    }

    public boolean dbUseMySQL() {
        return this.dbLibParams.dbUseMySQL;
    }

    public String dbFileName() {
        return sqliteParams.dbFileName;
    }

    public String dbMySqlUrl() {
        return mySqlParams.dbMySqlUrl;
    }

    public int dbMySqlPort() {
        return mySqlParams.dbMySqlPort;
    }

    public String dbMySqlDatabase() {
        return mySqlParams.dbMySqlDatabase;
    }

    public String dbMySqlUsername() {
        return mySqlParams.dbMySqlUsername;
    }

    public String dbMySqlPassword() {
        return mySqlParams.dbMySqlPassword;
    }

    public boolean debugLog() {
        return ormLiteParams.debugLog;
    }

    public int ormLiteKeepAlive() {
        return ormLiteParams.ormLiteKeepAlive;
    }

    public boolean save(Path path){
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            VoxelwindServer.MAPPER.writerWithDefaultPrettyPrinter().writeValue(writer, this);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static VwCfg load(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return VoxelwindServer.MAPPER.readValue(reader, VwCfg.class);
        }
    }
}
