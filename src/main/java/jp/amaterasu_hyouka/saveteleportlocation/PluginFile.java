package jp.amaterasu_hyouka.saveteleportlocation;

import jp.amaterasu_hyouka.saveteleportlocation.exception.FileCreationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PluginFile {

    private static File pluginDirectory;

    public PluginFile(JavaPlugin plugin) throws FileCreationException {
        pluginDirectory = plugin.getDataFolder();
        ensurePluginDirectory();
    }

    private void ensurePluginDirectory() throws FileCreationException {
        if (pluginDirectory.exists()) {
            if (!pluginDirectory.isDirectory()) {
                throw new FileCreationException("パスがディレクトリではない: " + pluginDirectory.getAbsolutePath());
            }
            return;
        }
        if (!pluginDirectory.mkdirs()) {
            throw new FileCreationException("ディレクトリの作成に失敗しました: " + pluginDirectory.getAbsolutePath());
        }
    }

    public static String getPluginPath() {
        return pluginDirectory.getAbsolutePath();
    }
}
