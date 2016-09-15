package ru.nukkit.dblib.voxelwind;

import com.voxelwind.api.plugin.Plugin;
import com.voxelwind.api.server.Server;
import com.voxelwind.api.server.event.Listener;
import com.voxelwind.api.server.event.server.ServerStartEvent;
import org.slf4j.Logger;
import ru.nukkit.dblib.DbLib;
import ru.nukkit.dblib.core.Message;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(id = "DbLib", author = "fromgate", version = "0.2.1")
public class DbLibPlugin {

    private final Server server;
    private final Logger logger;
    private Path path;

    @Inject
    public DbLibPlugin(Server server, Logger logger, Path path) {
        this.server = server;
        this.logger = logger;
        try {
            this.path = path.toRealPath();
        } catch (IOException e) {
            this.path = path;
            e.printStackTrace();
        }
    }

    private static DbLibPlugin plugin;

    public static DbLibPlugin getPlugin() {
        return plugin;
    }

    public static Path getDataFolder() {
        return getPlugin().path;
    }

    public static Path getFile(String fileName) {
        return getPlugin().path.resolve(fileName);
    }

    private ConfigVoxel cfg;


    @Listener
    public void onServerStart(ServerStartEvent event) {
        plugin = this;
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            logger.info("Failed to create plugin directory");
        }

        cfg = ConfigVoxel.load(Paths.get(path.toString(), "config.json"));
        cfg.save(Paths.get(path.toString(), "config.json"));

        Message.init("DbLib", new MessengerVoxel(), cfg.language(), cfg.debugMode(), cfg.saveLanguage());

        DbLib.init(cfg, path.toFile());
    }


    public static Server getServer() {
        return getPlugin().server;
    }

    public static Logger getLogger() {
        return getPlugin().logger;
    }
}
