package ru.nukkit.dblib.voxelwind;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.voxelwind.api.game.util.TextFormat;
import com.voxelwind.api.server.MessageRecipient;
import com.voxelwind.api.server.Player;
import ru.nukkit.dblib.Messenger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class VwMessenger implements Messenger {



    @Override
    public String colorize(String text) {
        return colorize('&', text);
    }

    @Override
    public boolean broadcast(String text) {
        DbLibPlugin.getServer().getPlayers().forEach(p -> p.sendMessage(colorize(text)));
        return true;
    }

    @Override
    public boolean log(String text) {
        DbLibPlugin.getLogger();
        return true;
    }

    @Override
    public String clean(String text) {
        return TextFormat.removeFormatting(text);
    }

    @Override
    public boolean tip(int seconds, Object sender, String text) {
        MessageRecipient recipient = toSender(sender);
        if (recipient != null) recipient.sendMessage(text);
        return true;
    }

    @Override
    public boolean tip(Object sender, String text) {
        MessageRecipient recipient = toSender(sender);
        if (recipient != null) recipient.sendMessage(text);
        return true;
    }

    @Override
    public boolean print(Object sender, String text) {
        MessageRecipient recipient = toSender(sender);
        if (recipient != null) recipient.sendMessage(text);
        return true;
    }

    @Override
    public boolean broadcast(String permission, String text) {
        DbLibPlugin.getServer().getPlayers().forEach(p -> {
            // TODO check permissions
            p.sendMessage(colorize(text));
        });
        return true;
    }

    @Override
    public String toString(Object obj, boolean fullFloat) {
        return obj.toString();
        /*
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
        return s; */
    }

    @Override
    public Map<String, String> load(String language) {
        Map<String,String> msg = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<HashMap<String,String>> typeRef = new TypeReference<HashMap<String,String>>() {};

        File f = DbLibPlugin.getFile(language+".lng").toFile();
        if (f.exists()) {
            try {
                msg = mapper.readValue(f, typeRef);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            InputStream is = DbLibPlugin.getPlugin().getClass().getResourceAsStream("/lang/" + language + ".lng");
            try {
                msg = mapper.readValue(is, typeRef);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    @Override
    public void save(String language, Map<String, String> message) {
        YAMLFactory yf = new YAMLFactory();
        File f = DbLibPlugin.getFile(language+".lng").toFile();
        ObjectMapper mapper = new ObjectMapper(yf);
        try {
            mapper.writeValue(f, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValidSender(Object send) {
        return toSender(send) != null;
    }


    public static String colorize(char altFormatChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();

        for(int i = 0; i < b.length - 1; ++i) {
            if(b[i] == altFormatChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }


    public MessageRecipient toSender(Object sender) {
        return sender instanceof MessageRecipient ? (MessageRecipient) sender : null;
    }

    public Player toPlayer (Object sender) {
        return sender instanceof Player ? (Player) sender : null;
    }

}
