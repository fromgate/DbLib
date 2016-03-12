package ru.nukkit.dblib;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import ru.nukkit.dblib.util.DbLibCfg;
import ru.nukkit.dblib.util.Message;

public class DbLibPlugin extends PluginBase {

    ConnectionSource connectionSource = null;

    private static DbLibPlugin plugin;
    public static DbLibPlugin getPlugin() {
        return plugin;
    }

    private DbLibCfg cfg;

    private boolean debugLog;

    @Override
    public void onEnable(){
        plugin = this;
        this.cfg = new DbLibCfg(this);
        this.cfg.load();
        this.cfg.save();
        Message.init(this);
        getLogger().info(TextFormat.YELLOW+"DbLib "+this.getDescription().getVersion()+" created by fromgate for nukkit.ru");
        initDb();
    }

    private void initDb(){
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, this.debugLog ? "DEBUG" : "ERROR");
        String dbUrl = this.getDbUrl();
        Message.URL_LOG.log("NOCOLOR",dbUrl,this.cfg.dbMySqlUsername);
        this.connectionSource = DbLib.getConnectionSource(this.getDbUrl(),this.cfg.dbMySqlUsername,this.cfg.dbMySqlPassword);
    }

    ConnectionSource getDefaultConnection() {
        return  this.connectionSource;
    }

    String getDbUrl() {
        StringBuilder sb = new StringBuilder("jdbc:");
        if (this.cfg.dbUseMySQL) {
            sb.append("mysql://");
            sb.append(this.cfg.dbMySqlUrl);
            sb.append(":").append(this.cfg.dbMySqlPort);
            sb.append("/").append(this.cfg.dbMySqlDatabase);
        } else {
            sb.append("sqlite:");
            sb.append(this.cfg.dbFileName);
        }
        return sb.toString();
    }
}
