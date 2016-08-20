package ru.nukkit.dblib.util;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.SimpleConfig;
import ru.nukkit.dblib.DbLibPlugin;

import java.io.File;

public class DbLibCfg extends SimpleConfig {
    public DbLibCfg(Plugin plugin) {
        super(plugin);
    }

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

}
