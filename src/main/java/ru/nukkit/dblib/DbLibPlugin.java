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
        this.dbUseMySQL = cfg.getNested("DbLib.use-MySQL",false);
        this.debugLog = cfg.getNested("DbLib.ORMLite-debug",false);

        this.dbFileName = cfg.getNested("SQLite.file-name",new File(this.getDataFolder().getParent()).getParent()+File.separator+"nukkit.db");

        this.dbMySqlUrl =  cfg.getNested("MySQL.host",cfg.getNested("MySQL.url","localhost"));
        this.dbMySqlPort = cfg.getNested("MySQL.port",3306);
        this.dbMySqlDatabase = cfg.getNested("MySQL.database","db");
        this.dbMySqlUsername = cfg.getNested("MySQL.username","nukkit");
        this.dbMySqlPassword = cfg.getNested("MySQL.password","tikkun");
    }
    private void save(){
        this.getDataFolder().mkdirs();
        File f = new File(this.getDataFolder(),"config.yml");
        if (f.exists()) f.delete();
        Config cfg = new Config(f,Config.YAML);
        cfg.setNested("DbLib.use-MySQL",this.dbUseMySQL);
        cfg.setNested("DbLib.ORMLite-debug",this.debugLog);

        cfg.setNested("SQLite.file-name",this.dbFileName);

        cfg.setNested("MySQL.host",this.dbMySqlUrl);
        cfg.setNested("MySQL.port",this.dbMySqlPort);
        cfg.setNested("MySQL.database",this.dbMySqlDatabase);
        cfg.setNested("MySQL.username",this.dbMySqlUsername);
        cfg.setNested("MySQL.password",this.dbMySqlPassword);

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
