package ru.nukkit.dblib;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.support.ConnectionSource;
import org.sql2o.Sql2o;
import ru.nukkit.dblib.core.DbLibConfig;
import ru.nukkit.dblib.core.Message;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbLib {

    private static DbLibConfig config;
    private static ConnectionSource connectionSource = null;
    private static Sql2o sql2o = null;
    private static File folder;


    public static void init(DbLibConfig cfg, File dataFolder) {
        config = cfg;
        folder = dataFolder;
        System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, config.debugLog() ? "DEBUG" : "ERROR");
        String dbUrl = config.getDbUrl();
        Message.URL_LOG.log("NOCOLOR", dbUrl, config.dbMySqlUsername());
        String url = config.getDbUrl();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
        }
        connectionSource = DbLib.getConnectionSource(url, config.dbMySqlUsername(), config.dbMySqlPassword());
        sql2o = config.dbUseMySQL() ? DbLib.getSql2o(url, config.dbMySqlUsername(), config.dbMySqlPassword()) :
                DbLib.getSql2o(url, config.dbMySqlUsername(), "");
    }

    /**
     * Get DbLib's default ORMLite connection source.
     * All plugins used this method will share same database
     *
     * @return - ConnectionSource (ORMlite)
     */
    public static ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * Get new ORMLite connections source.
     * Allows plugins to use custom connection source
     *
     * @param url      - database url (including database protocol). Examples:
     *                 jdbc:sqlite:c:\server\plugins\MyPugin\data.db
     *                 jdbc:mysql://localhost:3306/db
     * @param userName - Database user name, will be ignored for sqlite
     * @param password - Database user password, will be ignored for sqlite
     * @return - ConnectionSource (ORMlite)
     */
    public static ConnectionSource getConnectionSource(String url, String userName, String password) {
        try {
            if (config.ormLiteKeepAlive() <= 0) {
                return new JdbcConnectionSource(url, userName, password);
            } else {
                JdbcPooledConnectionSource jdbcCon = new JdbcPooledConnectionSource(url, userName, password);
                jdbcCon.setCheckConnectionsEveryMillis(config.ormLiteKeepAlive());
                return jdbcCon;
            }
        } catch (SQLException e) {
            Message.ERR_FAIL_TO_CONNECT.log(url, userName, 'c');
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get custom SQLite ORMLite connection source.
     *
     * @param fileName - database file name
     * @return - ConnectionSource (ORMlite)
     */
    public static ConnectionSource getConnectionSourceSQLite(String fileName) {
        File f = new File(folder + File.separator + fileName);
        File dir = new File(f.getParent());
        dir.mkdirs();
        String url = "jdbc:sqlite:" + f.getAbsolutePath();
        return getConnectionSource(url, "nukkit", "tikkun");
    }

    /**
     * Get custom MySQL ORMLite connection source.
     *
     * @param host     - MySQL server host name
     * @param port     - MySQL server port (use -1 to default value)
     * @param database - MySQL database
     * @param user     - MySQL user name
     * @param password - MySQL password
     * @return - ConnectionSource (ORMlite)
     */
    public static ConnectionSource getConnectionSourceMySql(String host, int port, String database, String user, String password) {
        return getConnectionSource(getMySqlUrl(host, port, database), user, password);
    }

    /**
     * Create and return default JDBC Connection
     *
     * @return - Connection (SQL)
     */
    public static Connection getDefaultConnection() {
        return config.dbUseMySQL() ? DbLib.getMySqlConnection(config.dbMySqlUrl(), config.dbMySqlPort(),
                config.dbMySqlDatabase(), config.dbMySqlUsername(), config.dbMySqlPassword())
                : DbLib.getSQLiteConnection(new File(config.dbFileName()));
    }

    /**
     * Get new MySQL Connection
     * This connections is not related to ORMLite, you must prepare and execute queries manually
     *
     * @param url      - MySQL url, example: localhost:3306/db
     * @param user     - MySQL user name
     * @param password - MySQL password
     * @return - Connection (SQL)
     */
    public static Connection getMySqlConnection(String url, String user, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(url.startsWith("jdbc:mysql://") ? url : "jdbc:mysql://" + url, user, password);
        } catch (Exception ignore) {
        }
        return null;
    }

    /**
     * Get new MySQL Connection
     * This connections is not related to ORMLite, you must prepare and execute queries manually
     *
     * @param host     - MySQL server host name
     * @param port     - MySQL server port (use -1 to default value)
     * @param database - MySQL database
     * @param user     - MySQL user name
     * @param password - MySQL password
     * @return - Connection (SQL)
     */
    public static Connection getMySqlConnection(String host, int port, String database, String user, String password) {
        StringBuilder sb = new StringBuilder(host);
        if (port >= 0) sb.append(":").append(port);
        sb.append("/").append(database);
        sb.append("?useSSL=false");
        return getMySqlConnection(sb.toString(), user, password);
    }

    /**
     * Get new SQLite Connection
     * This connections is not related to ORMLite, you must prepare and execute queries manually
     *
     * @param fileName - file name
     * @return - Connection (SQL)
     */
    public static Connection getSQLiteConnection(String fileName) {
        folder.mkdirs();
        return getSQLiteConnection(new File(folder + File.separator + fileName));
    }


    /**
     * Get new SQLite Connection
     * This connections is not related to ORMLite, you must prepare and execute queries manually
     *
     * @param file - File variable
     * @return - Connection (SQL)
     */
    public static Connection getSQLiteConnection(File file) {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(getSqliteUrl(file));
        } catch (Exception ignore) {
        }
        return null;
    }

    /**
     * Get Sql2o object for MySQL database
     *
     * @param host     - host (ip)
     * @param port     - port, if lesser than 1 will used default value 3306
     * @param database - MySQL database name
     * @param userName - user name
     * @param password - password
     * @return - Sql2o object
     */
    public static Sql2o getSql2oMySql(String host, int port, String database, String userName, String password) {
        return getSql2o(getMySqlUrl(host, port, database), userName, password);
    }

    /**
     * Get Sql2o object using jdbc-url and username password
     *
     * @param url      - jdbc-url
     * @param userName - user name
     * @param password - password
     * @return - Sql2o object
     */
    public static Sql2o getSql2o(String url, String userName, String password) {
        return new Sql2o(url, userName, password);
    }

    /**
     * Get default Sql2o object
     *
     * @return
     */
    public static Sql2o getSql2o() {
        return sql2o;
    }

    /**
     * Get jdbc-url for MySQL file
     *
     * @param host     - MySQL host
     * @param port     - MySQL port
     * @param database - MySQL database name
     * @return - URL
     */
    public static String getMySqlUrl(String host, int port, String database) {
        StringBuilder sb = new StringBuilder("jdbc:mysql://").append(host);
        if (port >= 0) sb.append(":").append(port);
        sb.append("/").append(database).append("?useSSL=false");
        return sb.toString();
    }

    /**
     * Get jdbc-url for SQlite file
     *
     * @param fileName - file name
     * @return - URL
     */
    public static String getSqliteUrl(String fileName) {
        return "jdbc:sqlite:" + fileName;
    }

    /**
     * Get jdbc-url for SQlite file
     *
     * @param file - file object
     * @return - URL
     */
    public static String getSqliteUrl(File file) {
        return "jdbc:sqlite:" + file.getAbsolutePath();
    }
}
