package dev.mattrm.mc.uhcplugin.settings.enums;

import dev.mattrm.mc.gametools.settings.IGameSettingEnum;
import dev.mattrm.mc.gametools.settings.SettingOption;
import org.bukkit.Material;

public enum TimeCycle implements IGameSettingEnum {
    @SettingOption(name = "Normal day/night cycle", item = Material.CLOCK)
    NORMAL,

    @SettingOption(name = "Day only", item = Material.SUNFLOWER)
    DAY_ONLY,

    @SettingOption(name = "Night only", item = Material.COBWEB)
    NIGHT_ONLY
}
