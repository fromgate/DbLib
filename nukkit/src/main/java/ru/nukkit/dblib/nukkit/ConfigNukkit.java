package ru.nukkit.dblib.nukkit;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.SimpleConfig;
import ru.nukkit.dblib.core.DbLibConfig;

import java.io.File;

public class ConfigNukkit extends SimpleConfig implements DbLibConfig {
    public ConfigNukkit(Plugin plugin) {
        super(plugin);
    }

    @Path("general.language")
    public String language = "default";

    @Path("general.save-translation")
    public boolean saveLanguage = false;

    @Path("general.debug-mode")
    boolean debugMode = false;

    @Path("DbLib.use-MySQL")
    public boolean dbUseMySQL = false;

    @Path("SQLite.file-name")
    public String dbFileName = DbLibPlugin.getPlugin().getDataFolder().getParentFile().getParent() + File.separator + "nukkit.db";

    @Path("MySQL.host")
    public String dbMySqlUrl = "localhost";

    @Path("MySQL.port")
    public int dbMySqlPort = 3306;

    @Path("MySQL.database")
    public String dbMySqlDatabase = "db";

    @Path("MySQL.username")
    public String dbMySqlUsername = "nukkit";

    @Path("MySQL.password")
    public String dbMySqlPassword = "tikkun";

    @Path("DbLib.ORMLite.debug")
    public boolean debugLog = false;

    @Path("DbLib.ORMLite.keep-alive-interval")
    public int ormLiteKeepAlive = 0;

    @Override
    public String language() {
        return language;
    }

    @Override
    public boolean saveLanguage() {
        return saveLanguage;
    }

    @Override
    public boolean debugMode() {
        return debugMode;
    }

    @Override
    public boolean dbUseMySQL() {
        return dbUseMySQL;
    }

    @Override
    public String dbFileName() {
        return dbFileName;
    }

    @Override
    public String dbMySqlUrl() {
        return dbMySqlUrl;
    }

    @Override
    public int dbMySqlPort() {
        return dbMySqlPort;
    }

    @Override
    public String dbMySqlDatabase() {
        return dbMySqlDatabase;
    }

    @Override
    public String dbMySqlUsername() {
        return dbMySqlUsername;
    }

    @Override
    public String dbMySqlPassword() {
        return dbMySqlPassword;
    }

    @Override
    public boolean debugLog() {
        return debugLog;
    }

    @Override
    public int ormLiteKeepAlive() {
        return ormLiteKeepAlive;
    }
}
