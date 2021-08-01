package dev.mattrm.mc.uhcplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ChatUtils {
    public static final String PREFIX = "[" + ChatColor.DARK_RED + ChatColor.BOLD + "UHC" + ChatColor.RESET + "] ";

    public static void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendMessage(player, message));
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(PREFIX + message);
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
    }
}
