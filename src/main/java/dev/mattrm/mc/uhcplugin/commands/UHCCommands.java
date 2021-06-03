package dev.mattrm.mc.uhcplugin.commands;

import dev.mattrm.mc.gametools.CommandGroup;
import dev.mattrm.mc.uhcplugin.commands.impl.SetupCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class UHCCommands extends CommandGroup {
    @Override
    public void registerCommands(JavaPlugin plugin) {
        plugin.getCommand("uhcsetup").setExecutor(new SetupCommand());
    }
}
