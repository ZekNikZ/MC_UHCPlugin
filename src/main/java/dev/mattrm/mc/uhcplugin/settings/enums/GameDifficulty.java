package dev.mattrm.mc.uhcplugin.settings.enums;

import dev.mattrm.mc.gametools.settings.IGameSettingEnum;
import dev.mattrm.mc.gametools.settings.SettingOption;
import org.bukkit.Difficulty;
import org.bukkit.Material;

public enum GameDifficulty implements IGameSettingEnum {
    @SettingOption(name = "Peaceful", item = Material.RED_ROSE, damage = 6)
    PEACEFUL(Difficulty.PEACEFUL),

    @SettingOption(name = "Easy", item = Material.WOOD_SWORD)
    EASY(Difficulty.EASY),

    @SettingOption(name = "Normal", item = Material.STONE_SWORD)
    NORMAL(Difficulty.NORMAL),

    @SettingOption(name = "Hard", item = Material.IRON_SWORD)
    HARD(Difficulty.HARD);

    private final Difficulty difficulty;

    GameDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }
}
