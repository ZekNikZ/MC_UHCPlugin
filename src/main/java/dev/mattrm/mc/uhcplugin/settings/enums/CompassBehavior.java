package dev.mattrm.mc.uhcplugin.settings.enums;

import dev.mattrm.mc.gametools.settings.IGameSettingEnum;
import dev.mattrm.mc.gametools.settings.SettingOption;
import org.bukkit.Material;

public enum CompassBehavior implements IGameSettingEnum {
    @SettingOption(name = "Default compass behavior", description = "Compasses do not track players", item = Material.BARRIER)
    NORMAL,

    @SettingOption(name = "Compasses track nearest enemy", description = "Compasses track nearest non-teammate", item = Material.CREEPER_HEAD)
    TRACK_ENEMIES,

    @SettingOption(name = "Compasses track nearest player", description = "Compasses track nearest player", item = Material.PLAYER_HEAD)
    TRACK_PLAYERS
}
