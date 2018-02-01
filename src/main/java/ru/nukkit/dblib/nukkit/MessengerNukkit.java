package ru.nukkit.dblib.nukkit;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.dblib.core.Messenger;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.nukkit.dblib.core.Message.LNG_SAVE_FAIL;
import static ru.nukkit.dblib.core.Message.debugException;

public class MessengerNukkit implements Messenger {


    PluginBase plugin;


    public MessengerNukkit(PluginBase plugin) {
        this.plugin = plugin;
    }

    @Override
    public String colorize(String text) {
        return TextFormat.colorize(text);
    }

    @Override
    public boolean broadcast(String text) {
        Server.getInstance().broadcastMessage(text);
        return true;
    }

    @Override
    public boolean log(String text) {
        plugin.getLogger().info(text);
        return true;
    }

    @Override
    public String clean(String text) {
        return TextFormat.clean(text);
    }

    @Override
    public boolean tip(int seconds, Object sender, final String text) {
        final Player player = toPlayer(sender);
        if (player != null) {
            for (int i = 0; i < seconds; i++) {
                Server.getInstance().getScheduler().scheduleDelayedTask(DbLibPlugin.getPlugin(), () -> {
                    if (player.isOnline()) player.sendPopup(text);
                }, 20 * i);
            }
        } else {
            CommandSender commandSender = toSender(sender);
            if (commandSender != null) commandSender.sendMessage(text);
        }
        return true;
    }

    @Override
    public boolean tip(Object sender, String text) {
        Player player = toPlayer(sender);
        if (player != null) {
            player.sendTip(text);
        } else {
            CommandSender commandSender = toSender(sender);
            if (commandSender != null) commandSender.sendMessage(text);
        }
        return true;

    }

    @Override
    public boolean print(Object obj, String text) {
        CommandSender sender = toSender(obj);
        if (sender != null) {
            sender.sendMessage(text);
        }
        return true;
    }

    @Override
    public boolean broadcast(String permission, String text) {
        List<Player> playerList = new ArrayList<Player>();
        for (Player player : Server.getInstance().getOnlinePlayers().values()) {
            if (permission == null || permission.isEmpty() || player.hasPermission(permission)) {
                player.sendMessage(text);
            }
        }
        return true;
    }

    @Override
    public String toString(Object obj, boolean fullFloat) {
        if (obj == null) return "'null'";
        String s = obj.toString();
        DecimalFormat fmt = new DecimalFormat("####0.##");
        if (obj instanceof Location) {
            Location loc = (Location) obj;
            if (fullFloat)
                s = loc.getLevel() + "[" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "]";
            else
                s = loc.getLevel() + "[" + fmt.format(loc.getX()) + ", " + fmt.format(loc.getY()) + ", " + fmt.format(loc.getZ()) + "]";
        }
        return s;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Map<String, String> load(String language) {
        File f = new File(plugin.getDataFolder() + File.separator + language + ".lng");
        Config lng = null;
        if (!f.exists()) {
            lng = new Config(f, Config.YAML);
            InputStream is = plugin.getClass().getResourceAsStream("/lang/" + language + ".lng");
            lng.load(is);
            if (!f.delete()) {
                System.gc();
                f.delete();
            }
        } else lng = new Config(f, Config.YAML);

        Map<String, String> msg = new HashMap<String, String>();
        for (String key : lng.getKeys(true)) {
            if (lng.isSection(key)) continue;
            msg.put(key, lng.getString(key));
        }
        return msg;
    }

    @Override
    public void save(String language, Map<String, String> messages) {
        File f = new File(plugin.getDataFolder() + File.separator + language + ".lng");
        Config lng = new Config(f, Config.YAML);
        for (String key : messages.keySet())
            lng.set(key.toLowerCase(), messages.get(key));
        try {
            lng.save();
        } catch (Exception e) {
            LNG_SAVE_FAIL.log();
            debugException(e);
        }
    }

    @Override
    public boolean isValidSender(Object send) {
        return (toSender(send) != null);
    }

    public CommandSender toSender(Object sender) {
        return sender instanceof CommandSender ? (CommandSender) sender : null;
    }

    public Player toPlayer(Object sender) {
        return sender instanceof Player ? (Player) sender : null;
    }


}
