package ru.nukkit.dblib.util;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.SimpleConfig;
import ru.nukkit.dblib.DbLibPlugin;

import java.io.File;

public class DbLibCfg extends SimpleConfig {
    public DbLibCfg(Plugin plugin) { super(plugin);
    }

    @Path(value = "DbLib.use-MySQL")
    public  boolean dbUseMySQL=false;

    @Path(value = "SQLite.file-name")
    public  String dbFileName = DbLibPlugin.getPlugin().getDataFolder().getParentFile().getParent() + File.separator + "nukkit.db";

    @Path(value = "MySQL.host")
    public  String dbMySqlUrl = "localhost";

    @SimpleConfig.Path(value ="MySQL.port")
    public  int dbMySqlPort=3306;

    @SimpleConfig.Path(value ="MySQL.database")
    public  String dbMySqlDatabase="db";

    @SimpleConfig.Path(value ="MySQL.username")
    public String dbMySqlUsername="nukkit";

    @SimpleConfig.Path(value = "MySQL.password")
    public  String dbMySqlPassword="tikkun";

    @SimpleConfig.Path(value ="DbLib.ORMLite-debug")
    public boolean debugLog=false;

}
