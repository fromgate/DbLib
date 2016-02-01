package ru.nukkit.dblib;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import ru.nukkit.dblib.util.Message;

import java.io.File;
import java.io.IOException;

public class DbLibPlugin extends PluginBase {

    ConnectionSource connectionSource = null;

    private static DbLibPlugin plugin;
    static DbLibPlugin getPlugin() {
        return plugin;
    }

    private boolean dbUseMySQL;
    private String dbFileName;

    private String dbMySqlUrl;
    private int dbMySqlPort;
    private String dbMySqlDatabase;
    private String dbMySqlUsername;
    private String dbMySqlPassword;

    private boolean debugLog;


    @Override
    public void onEnable(){
        plugin = this;
        Message.init(this);
        getLogger().info(TextFormat.YELLOW+"DbLib "+this.getDescription().getVersion()+" created by fromgate for nukkit.ru");
        initDb();
    }


    private void load(){
        this.getDataFolder().mkdirs();
        File f = new File(this.getDataFolder(),"config.yml");
        if (!f.exists()) try {
            f.createNewFile();
        } catch (IOException e) {
        }
        Config cfg = new Config(f,Config.YAML);
        this.dbUseMySQL = cfg.getBoolean("DbLib.use-MySQL",false);
        this.debugLog = cfg.getBoolean("DbLib.ORMLite-debug",false);

        this.dbFileName = cfg.getString("SQLite.file-name",new File(this.getDataFolder().getParent()).getParent()+File.separator+"nukkit.db");

        this.dbMySqlUrl =  cfg.getString("MySQL.host",cfg.getString("MySQL.url","localhost"));
        this.dbMySqlPort = cfg.getInt("MySQL.port",3306);
        this.dbMySqlDatabase = cfg.getString("MySQL.database","db");
        this.dbMySqlUsername = cfg.getString("MySQL.username","nukkit");
        this.dbMySqlPassword = cfg.getString("MySQL.password","tikkun");
    }
    private void save(){
        this.getDataFolder().mkdirs();
        File f = new File(this.getDataFolder(),"config.yml");
        if (f.exists()) f.delete();
        Config cfg = new Config(f,Config.YAML);
        cfg.set("DbLib.use-MySQL",this.dbUseMySQL);
        cfg.set("DbLib.ORMLite-debug",this.debugLog);

        cfg.set("SQLite.file-name",this.dbFileName);

        cfg.set("MySQL.host",this.dbMySqlUrl);
        cfg.set("MySQL.port",this.dbMySqlPort);
        cfg.set("MySQL.database",this.dbMySqlDatabase);
        cfg.set("MySQL.username",this.dbMySqlUsername);
        cfg.set("MySQL.password",this.dbMySqlPassword);

        cfg.save();
    }

    private void initDb(){
        load();
        save();
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, this.debugLog ? "DEBUG" : "ERROR");
        String dbUrl = this.getDbUrl();
        Message.URL_LOG.log("NOCOLOR",dbUrl,this.dbMySqlUsername);
        this.connectionSource = DbLib.getConnectionSource(this.getDbUrl(),this.dbMySqlUsername,this.dbMySqlPassword);
    }

    ConnectionSource getDefaultConnection() {
        return  this.connectionSource;
    }

    String getDbUrl() {
        StringBuilder sb = new StringBuilder("jdbc:");
        if (this.dbUseMySQL) {
            sb.append("mysql://");
            sb.append(this.dbMySqlUrl);
            sb.append(":").append(this.dbMySqlPort);
            sb.append("/").append(this.dbMySqlDatabase);
        } else {
            sb.append("sqlite:");
            sb.append(this.dbFileName);
        }
        return sb.toString();
    }
}
