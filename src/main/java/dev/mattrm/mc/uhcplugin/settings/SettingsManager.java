package dev.mattrm.mc.uhcplugin.settings;

import dev.mattrm.mc.gametools.Service;
import dev.mattrm.mc.gametools.data.DataService;
import dev.mattrm.mc.gametools.data.IDataManager;
import dev.mattrm.mc.gametools.settings.GameSettingsService;
import dev.mattrm.mc.gametools.settings.IGameSetting;
import dev.mattrm.mc.gametools.settings.impl.BooleanSetting;
import dev.mattrm.mc.gametools.settings.impl.EnumSetting;
import dev.mattrm.mc.gametools.settings.impl.IntRangeSetting;
import dev.mattrm.mc.gametools.util.DataMaterials;
import dev.mattrm.mc.gametools.util.ISB;
import dev.mattrm.mc.gametools.util.ListUtils;
import dev.mattrm.mc.gametools.util.Version;
import dev.mattrm.mc.gametools.world.WorldSyncService;
import dev.mattrm.mc.uhcplugin.GameState;
import dev.mattrm.mc.uhcplugin.game.GameManager;
import dev.mattrm.mc.uhcplugin.game.UHCScoreboards;
import dev.mattrm.mc.uhcplugin.settings.enums.*;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public class SettingsManager extends Service implements IDataManager {
    private static final SettingsManager INSTANCE = new SettingsManager();

    public static SettingsManager getInstance() {
        return INSTANCE;
    }

    private static final String SETTING_TIME_CYCLE = "timeCycle";
    private static final String SETTING_WEATHER_CYCLE = "weatherCycle";

    private static final String SETTING_COMPASS_BEHAVIOR = "compass";
    private static final String SETTING_THROWABLE_FIREBALLS = "fireballs";
    private static final String SETTING_REGENERATION_POTIONS = "regenPots";
    private static final String SETTING_GOLDEN_HEADS = "gheads";
    private static final String SETTING_ENCHANTED_GOLDEN_APPLES = "egaps";

    private static final String SETTING_SPAWN_PHANTOMS = "phantoms";
    private static final String SETTING_HOSTILE_MOBS = "hostile";
    private static final String SETTING_DIFFICULTY = "difficulty";

    private static final String SETTING_TEAM_GAME = "teams";
    private static final String SETTING_TEAMS_SPAWN_TOGETHER = "teamSpawns";

    private static final String SETTING_SPECTATOR_INVENTORIES = "spectatorInventories";

    private static final String SETTING_WORLDBORDER_DISTANCE_1 = "worldborderDistance1";
    private static final String SETTING_WORLDBORDER_TIME_1 = "worldborderTime1";
    private static final String SETTING_WORLDBORDER_DISTANCE_2 = "worldborderDistance2";
    private static final String SETTING_WORLDBORDER_TIME_2 = "worldborderTime2";
    private static final String SETTING_WORLDBORDER_DISTANCE_3 = "worldborderDistance3";
    private static final String SETTING_WORLDBORDER_TIME_3 = "worldborderTime3";
    private static final String SETTING_SUDDEN_DEATH_PARLAY_TIME = "suddenDeathParlayTime";

    @Override
    protected void setupService() {
        // Time and weather
        setting(SETTING_TIME_CYCLE, new EnumSetting<>(
            "Time Cycle",
            ListUtils.of("Determines how time works in the game."),
            ISB.stack(Material.CLOCK),
            12,
            TimeCycle.class,
            TimeCycle.NORMAL
        ).hook(tc -> {
            if (GameManager.getInstance().getState().isInGame()) {
                WorldSyncService.getInstance().setGameRuleValue("doDaylightCycle", "" + (tc == TimeCycle.NORMAL));

                if (tc == TimeCycle.DAY_ONLY) {
                    WorldSyncService.getInstance().setTime(6000);
                } else if (tc == TimeCycle.NIGHT_ONLY) {
                    WorldSyncService.getInstance().setTime(18000);
                }
            }
        }));
        setting(SETTING_WEATHER_CYCLE, new EnumSetting<>(
            "Weather Cycle",
            ListUtils.of("Determines how weather works in the game."),
            ISB.stack(Material.DEAD_BUSH),
            13,
            WeatherCycle.class,
            WeatherCycle.NORMAL
        ).hook(tc -> {
            if (GameManager.getInstance().getState().isInGame()) {
                if (tc == WeatherCycle.CLEAR_ONLY) {
                    WorldSyncService.getInstance().setWeatherClear();
                } else if (tc == WeatherCycle.RAIN_ONLY) {
                    WorldSyncService.getInstance().setWeatherRain();
                } else if (tc == WeatherCycle.STORM_ONLY) {
                    WorldSyncService.getInstance().setWeatherStorm();
                }
            }
        }));

        // Items
        setting(SETTING_COMPASS_BEHAVIOR, new EnumSetting<>(
            "Compass Tracking",
            ListUtils.of("Determines which players compasses track, if any."),
            ISB.stack(Material.COMPASS),
            14,
            CompassBehavior.class,
            CompassBehavior.NORMAL
        ));
        setting(SETTING_THROWABLE_FIREBALLS, new BooleanSetting(
            "Throwable Fireballs",
            ListUtils.of("Determines if fireballs should be throwable."),
            ISB.stack(Material.FIRE_CHARGE),
            true,
            15
        ));
        setting(SETTING_REGENERATION_POTIONS, new BooleanSetting(
            "Regeneration Potions",
            ListUtils.of("Determines if regeneration potions will be allowed."),
            ISB.stack(Material.POTION, (short) 8193),
            false,
            16
        ));
        setting(SETTING_ENCHANTED_GOLDEN_APPLES, new BooleanSetting(
            "Enchanted Golden Apples",
            ListUtils.of("Determines if enchanted golden apples will be allowed."),
            ISB.stack(Material.GOLDEN_APPLE, (short) 1),
            false,
            17
        ));
        setting(SETTING_GOLDEN_HEADS, new BooleanSetting(
            "Golden Heads",
            ListUtils.of("Determines if golden heads will be craftable."),
            ISB.stack(Material.PLAYER_HEAD),
            true,
            18
        ));

        // Mobs
        if (Version.is17()) {
            setting(SETTING_SPAWN_PHANTOMS, new BooleanSetting(
                "Allow Phantom Spawning",
                ListUtils.of("Determines if phantoms will be allowed to spawn"),
                ISB.stack(Material.FEATHER),
                false
            ));
        }
        setting(SETTING_HOSTILE_MOBS, new BooleanSetting(
            "Allow Hostile Mob Spawning",
            ListUtils.of("Determines if hostile mobs will be allowed to spawn"),
            ISB.stack(Material.ZOMBIE_HEAD),
            true,
            11
        ));
        setting(SETTING_DIFFICULTY, new EnumSetting<>(
            "Game Difficulty",
            ListUtils.of("Determines the game difficulty"),
            ISB.stack(Material.DIAMOND_SWORD),
            10,
            GameDifficulty.class,
            GameDifficulty.HARD
        ));

        // Teams
        setting(SETTING_TEAM_GAME, new EnumSetting<>(
            "Team Mode",
            ListUtils.of("Determines if players will compete with or against each other"),
            ISB.stack(Material.LEATHER_CHESTPLATE),
            7,
            TeamStatus.class,
            TeamStatus.TEAM_GAME
        ).hook(status -> {
            if (GameManager.getInstance().getState() == GameState.SETUP) {
                UHCScoreboards.updateCompetitors();
            }
        }));
        setting(SETTING_TEAMS_SPAWN_TOGETHER, new BooleanSetting(
            "Teams Spawn Together",
            ListUtils.of("Determines if teams will spawn together at the beginning of the game"),
            ISB.stack(Material.LEATHER_LEGGINGS),
            true,
            8
        ));

        // Spectating
        setting(SETTING_SPECTATOR_INVENTORIES, new EnumSetting<>(
            "Spectator Inventories",
            ListUtils.of("Determines if spectators can see the inventories of players that they spectate."),
            ISB.stack(Material.CHAINMAIL_HELMET),
            9,
            SpectatorMode.class,
            SpectatorMode.SPECTATORS_SEE_INVENTORIES
        ));

        // Worldborder
        setting(SETTING_WORLDBORDER_DISTANCE_1, new IntRangeSetting(
            "Worldborder Size (Phase 1)",
            ListUtils.of("This determines the initial size of the worldborder."),
            ISB.stack(DataMaterials.get().limeDye()),
            0,
            2000,
            5000,
            250,
            3000
        ));
        setting(SETTING_WORLDBORDER_TIME_1, new IntRangeSetting(
            "Worldborder Time (Phase 1)",
            ListUtils.of("This determines the time it takes to get from worldborder phase 1 to phase 2."),
            ISB.stack(Material.CLOCK),
            1,
            1, //10,
            120,
            1, //5,
            60
        ));
        setting(SETTING_WORLDBORDER_DISTANCE_2, new IntRangeSetting(
            "Worldborder Size (Phase 2)",
            ListUtils.of("This determines the second size of the worldborder."),
            ISB.stack(DataMaterials.get().limeDye()),
            2,
            100,
            3000,
            100,
            200
        ));
        setting(SETTING_WORLDBORDER_TIME_2, new IntRangeSetting(
            "Worldborder Time (Phase 2)",
            ListUtils.of("This determines the time it takes to get from worldborder phase 2 to phase 3."),
            ISB.stack(Material.CLOCK),
            3,
            0,
            60,
            5,
            30
        ));
        setting(SETTING_WORLDBORDER_DISTANCE_3, new IntRangeSetting(
            "Worldborder Size (Phase 3)",
            ListUtils.of("This determines the third size of the worldborder.", "Set to 0 to not use a third phase."),
            ISB.stack(DataMaterials.get().limeDye()),
            4,
            100,
            2000,
            100,
            100
        ));
        setting(SETTING_WORLDBORDER_TIME_3, new IntRangeSetting(
            "Worldborder Time (Phase 3)",
            ListUtils.of("This determines the time it takes to get from worldborder phase 2 to phase 3.", "Set to 0 to not use a third phase."),
            ISB.stack(Material.CLOCK),
            5,
            0,
            60,
            5,
            0
        ));
        setting(SETTING_SUDDEN_DEATH_PARLAY_TIME, new IntRangeSetting(
            "Worldborder Parlay Time",
            ListUtils.of("This determines the time between the worldborder", "stopping moving and sudden beginning"),
            ISB.stack(Material.CLOCK),
            6,
            0,
            20,
            1,
            10
        ));

        DataService.getInstance().registerDataManager(this);
    }

    private void setting(String id, IGameSetting<?> setting) {
        GameSettingsService.getInstance().addGameSetting(id, setting);
    }

    @Override
    public String getDataFileName() {
        return "gamesettings";
    }

    @Override
    public void onLoad(ConfigurationSection configurationSection) {
        ConfigurationSection settings = configurationSection.getConfigurationSection("settings");

        GameSettingsService.getInstance().loadSettingsFrom(configurationSection);
    }

    @Override
    public void onSave(ConfigurationSection configurationSection) {
        GameSettingsService.getInstance().saveSettingsTo(configurationSection);
    }

    @SuppressWarnings("unchecked")
    public EnumSetting<TimeCycle> timeCycle() {
        return (EnumSetting<TimeCycle>) GameSettingsService.getInstance().getSetting(SETTING_TIME_CYCLE);
    }

    @SuppressWarnings("unchecked")
    public EnumSetting<WeatherCycle> weatherCycle() {
        return (EnumSetting<WeatherCycle>) GameSettingsService.getInstance().getSetting(SETTING_WEATHER_CYCLE);
    }

    @SuppressWarnings("unchecked")
    public EnumSetting<CompassBehavior> compassBehavior() {
        return (EnumSetting<CompassBehavior>) GameSettingsService.getInstance().getSetting(SETTING_COMPASS_BEHAVIOR);
    }

    public BooleanSetting throwableFireballs() {
        return (BooleanSetting) GameSettingsService.getInstance().getSetting(SETTING_THROWABLE_FIREBALLS);
    }

    public BooleanSetting regenerationPotions() {
        return (BooleanSetting) GameSettingsService.getInstance().getSetting(SETTING_REGENERATION_POTIONS);
    }

    public BooleanSetting goldenHeads() {
        return (BooleanSetting) GameSettingsService.getInstance().getSetting(SETTING_GOLDEN_HEADS);
    }

    public BooleanSetting enchantedGoldenApples() {
        return (BooleanSetting) GameSettingsService.getInstance().getSetting(SETTING_ENCHANTED_GOLDEN_APPLES);
    }

    public BooleanSetting allowPhantoms() {
        return (BooleanSetting) GameSettingsService.getInstance().getSetting(SETTING_SPAWN_PHANTOMS);
    }

    public BooleanSetting allowHostileSpawns() {
        return (BooleanSetting) GameSettingsService.getInstance().getSetting(SETTING_HOSTILE_MOBS);
    }

    @SuppressWarnings("unchecked")
    public EnumSetting<GameDifficulty> difficulty() {
        return (EnumSetting<GameDifficulty>) GameSettingsService.getInstance().getSetting(SETTING_DIFFICULTY);
    }

    @SuppressWarnings("unchecked")
    public EnumSetting<TeamStatus> teamGame() {
        return (EnumSetting<TeamStatus>) GameSettingsService.getInstance().getSetting(SETTING_TEAM_GAME);
    }

    public BooleanSetting teamsSpawnTogether() {
        return (BooleanSetting) GameSettingsService.getInstance().getSetting(SETTING_TEAMS_SPAWN_TOGETHER);
    }

    @SuppressWarnings("unchecked")
    public EnumSetting<SpectatorMode> spectatorInventories() {
        return (EnumSetting<SpectatorMode>) GameSettingsService.getInstance().getSetting(SETTING_SPECTATOR_INVENTORIES);
    }

    public List<IntRangeSetting> worldborderDistances() {
        return ListUtils.of(
            GameSettingsService.getInstance().getSetting(SETTING_WORLDBORDER_DISTANCE_1),
            GameSettingsService.getInstance().getSetting(SETTING_WORLDBORDER_DISTANCE_2),
            GameSettingsService.getInstance().getSetting(SETTING_WORLDBORDER_DISTANCE_3)
        ).stream().map(s -> (IntRangeSetting) s).collect(Collectors.toList());
    }

    public List<IntRangeSetting> worldborderTimes() {
        return ListUtils.of(
            GameSettingsService.getInstance().getSetting(SETTING_WORLDBORDER_TIME_1),
            GameSettingsService.getInstance().getSetting(SETTING_WORLDBORDER_TIME_2),
            GameSettingsService.getInstance().getSetting(SETTING_WORLDBORDER_TIME_3)
        ).stream().map(s -> (IntRangeSetting) s).collect(Collectors.toList());
    }

    public IntRangeSetting suddenDeathParlayTime() {
        return (IntRangeSetting) GameSettingsService.getInstance().getSetting(SETTING_SUDDEN_DEATH_PARLAY_TIME);
    }
}
