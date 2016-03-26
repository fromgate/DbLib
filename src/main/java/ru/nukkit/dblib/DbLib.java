package ru.nukkit.dblib;

import cn.nukkit.plugin.Plugin;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import ru.nukkit.dblib.util.Message;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbLib {

    /**
     * Get DbLib's default ORMLite connection source.
     * All plugins used this method will share same database
     *
     * @return         - ConnectionSource (ORMlite)
     */
    public static ConnectionSource getConnectionSource(){
        return DbLibPlugin.getPlugin().getDefaultConnection();
    }


    /**
     * Get new ORMLite connections source.
     * Allows plugins to use custom connection source
     *
     * @param url      - database url (including database protocol). Examples:
     *                   jdbc:sqlite:c:\server\plugins\MyPugin\data.db
     *                   jdbc:mysql://localhost:3306/db
     *
     * @param userName - Database user name, will be ignored for sqlite
     * @param password - Database user password, will be ignored for sqlite
     * @return         - ConnectionSource (ORMlite)
     */
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

    /**
     * Get custom SQLite ORMLite connection source.
     *
     * @param plugin    - plugin executing this method, database file will be located in plugin's data folder
     * @param fileName  - database file name
     * @return          - ConnectionSource (ORMlite)
     */
    public static ConnectionSource getConnectionSourceSQLite (Plugin plugin, String fileName){
        File f = new File(plugin.getDataFolder()+File.separator+fileName);
        File dir = new File(f.getParent());
        dir.mkdirs();
        String url = "jdbc:sqlite:"+f.getAbsolutePath();
        return getConnectionSource (url,"nukkit","tikkun");
    }

    /**
     * Deprecated (because of typo in name), please use getConnectionSourceMySql
     */
    @Deprecated
    public static ConnectionSource getConnectionSourceSQLite (String host, int port, String database, String user, String password){
        return getConnectionSourceMySql(host,port,database,user,password);
    }

    /**
     * Get custom MySQL ORMLite connection source.
     *
     * @param host        - MySQL server host name
     * @param port        - MySQL server port (use -1 to default value)
     * @param database    - MySQL database
     * @param user        - MySQL user name
     * @param password    - MySQL password
     * @return            - ConnectionSource (ORMlite)
     */
    public static ConnectionSource getConnectionSourceMySql(String host, int port, String database, String user, String password){
        return getConnectionSource (new StringBuilder("jdbc:mysql://").append(host).append(":").append(port).append("/").append(database).toString(),
                user,password);
    }

    /**
     * Get new MySQL Connection
     * This connections is not related to ORMLite, you must prepare and execute queries manually
     *
     * @param url         - MySQL url, example: localhost:3306/db
     * @param user        - MySQL user name
     * @param password    - MySQL password
     * @return            - Connection (SQL)
     */
    public static Connection getMySqlConnection(String url, String user, String password){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url.startsWith("jdbc:mysql://") ? url : "jdbc:mysql://"+url, user,password);
        } catch (Exception ignore) {
        }
        return null;
    }

    /**
     * Get new MySQL Connection
     * This connections is not related to ORMLite, you must prepare and execute queries manually
     *
     * @param host        - MySQL server host name
     * @param port        - MySQL server port (use -1 to default value)
     * @param database    - MySQL database
     * @param user        - MySQL user name
     * @param password    - MySQL password
     * @return            - Connection (SQL)
     */
    public static Connection getMySqlConnection(String host, int port, String database, String user, String password){
        StringBuilder sb = new StringBuilder(host);
        if (port>=0) sb.append(":").append(port);
        sb.append("/").append(database);
        return getMySqlConnection(sb.toString(), user, password);
    }

    /**
     * Get new SQLite Connection
     * This connections is not related to ORMLite, you must prepare and execute queries manually
     *
     * @param plugin      - Your plugin (only data folder value will be accessed)
     * @param fileName    - file name
     * @return            - Connection (SQL)
     */
    public static Connection getSQLiteConnection(Plugin plugin, String fileName){
        plugin.getDataFolder().mkdirs();
        return getSQLiteConnection(new File(plugin.getDataFolder()+File.separator+fileName));
    }


    /**
     * Get new SQLite Connection
     * This connections is not related to ORMLite, you must prepare and execute queries manually
     *
     * @param file       - File variable
     * @return           - Connection (SQL)
     */
    public static Connection getSQLiteConnection(File file){
        String url = "jdbc:sqlite:"+file.getAbsolutePath();
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(url);
        } catch (Exception ignore){
        }
        return null;
    }
}
