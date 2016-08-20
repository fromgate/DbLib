# DbLib
DbLib a library for nukkit, that include ORMLite and drivers for MySQL and SQLite.

[Download at nukkit.ru](http://nukkit.ru/resources/dblib.14/)

Example project: [DbExample](https://github.com/fromgate/DbExample)


## Features

* Provides acces to popular database engines: SQLite and MySQL;
* Includes popular library ORMLite, that provides access to database without using SQL-queries;
* Organizes universal data storage for all plugins, that uses DbLib. Server owner must configure DbLib once and all plugins that use DbLib will work fine!

## Configuration
```
general:
# Messages language
# Supported languages: russian, english
  language: english 
# Debug mode. Usually you don't need to enable this parameter
  debug-mode: false
DbLib:
# ORMLite specific parameters
  ORMLite:
# ORMLite debug mode. Set true to see more messages :)
    debug: false
# Keep-alive time interval (ms) for ORMLite connections
# Increase this value if your database server timeout connections is too low
# Set 0 to disable this mode (usually it's ok)
    keep-alive-interval: 0

# Database type:
#  true - MySQL
#  false - SQLite
  use-MySQL: false

# # Full path to sqlite file
SQLite:
  file-name: C:\NukkitServer\nukkit.db
  
# MySQL connections parameters
MySQL:
  host: localhost
  port: 3306
  database: db
  username: nukkit
  password: tikkun
```

## How to use plugin (Server owners must read this)
If any plugin requires a DbLib you just need to download and install it. Usually it will be enough. But if you going to use to MySQL or another sqlite file you need to configure plugin (Read at Config section)

## Note for plugin developers
**ORMLite** is popular engine, that provides access to databases using ORM technique. Practically it means that you can read and save data into dabase without writing SQL-queries.

DbLib also include database drivers:

* SQLite driver
* MySQL driver

If you would not like to use ORMLite, you can use MySQL (or SQLite) drivers as usually, using JDBC provided by Java.


## How to connect to universal DbLib storage

DbLib suggests to use universal database (by default it is a file nukkit.db, located in sever folder or MySQL database defined by user) for all plugins. If DbLib installed on server you don't need to think about database drivers, urls, names and passwords.

Here is example of creating new table:
```
  public boolean connectToDbLib(){
    if (this.getServer().getPluginManager().getPlugin("DbLib") == null){
      this.getLogger().info(TextFormat.RED+"DbLib plugin not found");
      return false;
    }
    connectionSource = DbLib.getConnectionSource();
    if (connectionSource == null) return false;
    try {
      passDao =  DaoManager.createDao(connectionSource, PasswordsTable.class);
      TableUtils.createTableIfNotExists(connectionSource, PasswordsTable.class);
    } catch (Exception e) {
      return false;
    }
    return true; //  Table created!
  }
```

But if you going to use any other database you must use this method:
```
  ConnectionSource connectionSource = DbLib.getConnectionSoruce(String url, String userName, String password);
```

