package ru.nukkit.dblib;

import cn.nukkit.plugin.PluginBase;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import ru.nukkit.dblib.util.Message;

import java.io.File;
import java.sql.SQLException;

public class DbLib {
    public static ConnectionSource getConnectionSource(){
        return DbLibPlugin.getPlugin().getDefaultConnection();
    }
    public static ConnectionSource getConnectionSource(String url, String userName, String password){
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource(url,userName,password);
        } catch (SQLException e) {
            Message.ERR_FAIL_TO_CONNECT.log(url,userName,'c');
            e.printStackTrace();
        }
        return connectionSource;
    }
    public static ConnectionSource getConnectionSourceSQLite (PluginBase plugin, String fileName){
        File f = new File(plugin.getDataFolder()+File.separator+fileName);
        File dir = new File(f.getParent());
        dir.mkdirs();
        String url = "jdbc:sqlite:"+f.getAbsolutePath();
        return getConnectionSource (url,"nukkit","tikkun");
    }
    public static ConnectionSource getConnectionSourceSQLite (String url, int port, String database, String user, String password){
        return getConnectionSource (new StringBuilder("jdbc:mysql://").append(url).append(":").append(port).append("/").append(database).toString(),
                user,password);
    }
}
