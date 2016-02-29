package ru.nukkit.dblib.util;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.SimpleConfig;

public class DbLibCfg extends SimpleConfig {
    public DbLibCfg(Plugin plugin) {
        super(plugin);
    }

    @Path(value = "DbLib.use-MySQL")
    public  boolean dbUseMySQL;

    @Path(value = "SQLite.file-name")
    public  String dbFileName;

    @Path(value = "MySQL.host")
    public  String dbMySqlUrl;

    @SimpleConfig.Path(value ="MySQL.port")
    public  int dbMySqlPort;

    @SimpleConfig.Path(value ="MySQL.database")
    public  String dbMySqlDatabase;

    @SimpleConfig.Path(value ="MySQL.username")
    public String dbMySqlUsername;

    @SimpleConfig.Path(value = "MySQL.password")
    public  String dbMySqlPassword;

    @SimpleConfig.Path(value ="DbLib.ORMLite-debug")
    public boolean debugLog;

}
