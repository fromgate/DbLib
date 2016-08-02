package ru.nukkit.dblib;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import org.sql2o.Sql2o;
import ru.nukkit.dblib.util.DbLibCfg;
import ru.nukkit.dblib.util.Message;

public class DbLibPlugin extends PluginBase {

    ConnectionSource connectionSource = null;
    Sql2o sql2o = null;

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
        String url = this.getDbUrl();
        this.connectionSource = DbLib.getConnectionSource(url,this.cfg.dbMySqlUsername,this.cfg.dbMySqlPassword);
        this.sql2o = cfg.dbUseMySQL ? DbLib.getSql2o(url,this.cfg.dbMySqlUsername,this.cfg.dbMySqlPassword) :
                DbLib.getSql2o(url,this.cfg.dbMySqlUsername,"");
    }

    ConnectionSource getDefaultORMLiteConnection() {
        return this.connectionSource;
    }

    Sql2o getDefautlSql2o(){
        return this.sql2o;

    }

    String getDbUrl() {
        StringBuilder sb = new StringBuilder("jdbc:");
        if (this.cfg.dbUseMySQL) {
            sb.append("mysql://")
            .append(this.cfg.dbMySqlUrl)
            .append(":").append(this.cfg.dbMySqlPort)
            .append("/").append(this.cfg.dbMySqlDatabase)
            .append("?useSSL=false");
        } else {
            sb.append("sqlite:")
            .append(this.cfg.dbFileName);
        }
        return sb.toString();
    }
}
