package dev.mattrm.mc.uhcplugin.settings.enums;

import dev.mattrm.mc.gametools.settings.IGameSettingEnum;
import dev.mattrm.mc.gametools.settings.SettingOption;
import org.bukkit.Material;

public enum SpectatorMode implements IGameSettingEnum {
    @SettingOption(name = "Default behavior", description = "Spectator mode acts like in Vanilla", item = Material.LEATHER_HELMET)
    NORMAL,

    @SettingOption(name = "Spectators can see inventories", description = "Spectators can view player's inventories while spectating them", item = Material.DIAMOND_HELMET)
    SPECTATORS_SEE_INVENTORIES,

    @SettingOption(name = "Team members can see inventories", description = "Spectators can view alive teammates' inventories while spectating them", item = Material.GOLD_HELMET)
    TEAMS_SEE_INVENTORIES
}
