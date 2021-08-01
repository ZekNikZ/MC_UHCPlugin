package dev.mattrm.mc.uhcplugin.overrides;

import org.bukkit.plugin.java.JavaPlugin;

public class AllOverrides {
    public static void override(JavaPlugin plugin) {
        RecipeOverrides.override(plugin);
    }
}
