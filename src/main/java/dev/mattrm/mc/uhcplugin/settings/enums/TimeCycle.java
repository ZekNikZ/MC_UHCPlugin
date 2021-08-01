package dev.mattrm.mc.uhcplugin.settings.enums;

import dev.mattrm.mc.gametools.settings.IGameSettingEnum;
import dev.mattrm.mc.gametools.settings.SettingOption;
import org.bukkit.Material;

public enum TimeCycle implements IGameSettingEnum {
    @SettingOption(name = "Normal day/night cycle", item = Material.WATCH)
    NORMAL,

    @SettingOption(name = "Day only", item = Material.DOUBLE_PLANT)
    DAY_ONLY,

    @SettingOption(name = "Night only", item = Material.WEB)
    NIGHT_ONLY
}
