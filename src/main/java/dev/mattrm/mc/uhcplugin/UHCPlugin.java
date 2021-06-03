package dev.mattrm.mc.uhcplugin;

import dev.mattrm.mc.gametools.Service;
import dev.mattrm.mc.uhcplugin.commands.UHCCommands;
import dev.mattrm.mc.uhcplugin.transition.GameManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name = "UHCPlugin", version = "1.0")
@Author("ZekNikZ")
@Dependency("GameToolsLibrary")
@Dependency("WorldEdit")
public final class UHCPlugin extends JavaPlugin {
    public static boolean test = false;

    @Override
    public void onEnable() {
        registerServices();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerServices() {
        Service[] services = new Service[]{
            GameManager.getInstance()
        };

        PluginManager pluginManager = this.getServer().getPluginManager();
        for (Service service : services) {
            service.setup(this);
            pluginManager.registerEvents(service, this);
        }
    }

    private void registerCommands() {
        new UHCCommands().registerCommands(this);
    }
}
