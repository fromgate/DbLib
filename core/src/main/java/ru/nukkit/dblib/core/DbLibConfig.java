package ru.nukkit.dblib.core;

public interface DbLibConfig {

    String language();

    boolean saveLanguage();

    boolean debugMode();

    boolean dbUseMySQL();

    String dbFileName();

    String dbMySqlUrl();

    int dbMySqlPort();

    String dbMySqlDatabase();

    String dbMySqlUsername();

    String dbMySqlPassword();

    boolean debugLog();

    int ormLiteKeepAlive();

    default String getDbUrl() {
        StringBuilder sb = new StringBuilder("jdbc:");
        if (dbUseMySQL()) {
            sb.append("mysql://")
                    .append(dbMySqlUrl())
                    .append(":").append(dbMySqlPort())
                    .append("/").append(dbMySqlDatabase())
                    .append("?useSSL=false");
        } else {
            sb.append("sqlite:")
                    .append(dbFileName());
        }
        return sb.toString();
    }

}
