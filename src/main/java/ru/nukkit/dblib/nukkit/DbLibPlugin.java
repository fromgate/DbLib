package ru.nukkit.dblib.nukkit;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.dblib.DbLib;
import ru.nukkit.dblib.core.M;

public class DbLibPlugin extends PluginBase {


    private static DbLibPlugin plugin;

    public static DbLibPlugin getPlugin() {
        return plugin;
    }

    private ConfigNukkit cfg;

    private boolean debugLog;

    @Override
    public void onLoad() {
        plugin = this;
        this.saveDefaultConfig();
        this.cfg = new ConfigNukkit(this);
        this.cfg.load();
        M.init("DbLib", new MessengerNukkit(this), cfg.language, cfg.debugMode, cfg.saveLanguage);
        DbLib.init(cfg, this.getDataFolder());
        getLogger().info(TextFormat.colorize("&eDbLib " + this.getDescription().getVersion() + " created by fromgate for nukkit.ru"));
    }


}
