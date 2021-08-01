package dev.mattrm.mc.uhcplugin.settings.enums;

import dev.mattrm.mc.gametools.settings.IGameSettingEnum;
import dev.mattrm.mc.gametools.settings.SettingOption;
import org.bukkit.Material;

public enum TeamStatus implements IGameSettingEnum {
    @SettingOption(name="Team mode", description = "Players will work together as teams to win", item = Material.IRON_CHESTPLATE)
    TEAM_GAME,

    @SettingOption(name="Individual mode", description = "Players will each be competing for themselves", item = Material.IRON_AXE)
    INDIVIDUAL_GAME
}
