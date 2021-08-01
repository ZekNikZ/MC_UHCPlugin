package dev.mattrm.mc.uhcplugin.settings.enums;

import dev.mattrm.mc.gametools.settings.IGameSettingEnum;
import dev.mattrm.mc.gametools.settings.SettingOption;
import org.bukkit.Material;

public enum WeatherCycle implements IGameSettingEnum {
    @SettingOption(name = "No weather", item = Material.INK_SACK, damage = 8)
    CLEAR_ONLY,

    @SettingOption(name = "Normal weather cycle", item = Material.INK_SACK, damage = 11)
    NORMAL,

    @SettingOption(name = "Always raining", item = Material.INK_SACK, damage = 14)
    RAIN_ONLY,

    @SettingOption(name = "Always storming", item = Material.INK_SACK, damage = 1)
    STORM_ONLY
}
