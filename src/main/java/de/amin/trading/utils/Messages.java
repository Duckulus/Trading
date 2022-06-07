package de.amin.trading.utils;

import de.amin.trading.TradingPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Messages {

    private static YamlConfiguration messageConfig;

    public static void init(TradingPlugin plugin) {
        messageConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "messages.yml"));
        plugin.getLogger().info("Succesfully loaded Messages from messages.yml");
    }

    public static Component get(String path, String... replacements) {
        String message = messageConfig.getString(path);
        if (message == null) {
            return Component.text(ChatColor.RED + "<Missing: " + path + ">");
        }

        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", replacements[i]);
        }

        return LegacyComponentSerializer.legacy('&').deserialize(message);
    }

    public static Component prefixed(String path, String... replacements) {
        return get("prefix").append(get(path, replacements));
    }

}
