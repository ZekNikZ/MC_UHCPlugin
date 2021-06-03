package dev.mattrm.mc.uhcplugin.commands.impl;

import dev.mattrm.mc.uhcplugin.lobby.LobbySetup;
import dev.mattrm.mc.uhcplugin.transition.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Commands;

@Commands(@org.bukkit.plugin.java.annotation.command.Command(
    name = "uhcsetup",
    aliases = {"setupuhc"},
    desc = "Setup UHC lobby",
    usage = "/uhcsetup"
))
public class SetupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(LobbySetup.setupLobby() ? "success" : "failed");
        GameManager.getInstance().transitionToSetupPhase();
        return true;
    }
}
