package dev.mattrm.mc.uhcplugin.transition;

import dev.mattrm.mc.gametools.Service;
import dev.mattrm.mc.gametools.data.SharedReference;
import dev.mattrm.mc.gametools.scoreboards.GameScoreboard;
import dev.mattrm.mc.gametools.scoreboards.ScoreboardService;
import dev.mattrm.mc.gametools.teams.GameTeam;
import dev.mattrm.mc.gametools.teams.TeamService;
import dev.mattrm.mc.gametools.world.WorldSyncService;
import dev.mattrm.mc.uhcplugin.GameState;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

public class GameManager extends Service {
    private static final GameManager INSTANCE = new GameManager();

    public static GameManager getInstance() {
        return INSTANCE;
    }

    private static final Vector tpLocation = new Vector(0, 201, 0);

    private final SharedReference<GameState> stateRef = new SharedReference<>(GameState.UNKNOWN);
    private boolean scoreboardTitleColor = false;

    public void setState(GameState newState) {
        this.stateRef.setAndNotify(newState);
    }

    public GameState getState() {
        return this.stateRef.get();
    }

    public void transitionToSetupPhase() {
        this.setState(GameState.SETUP);

        // Teleport players and set their spawns
        World world = Bukkit.getWorlds().get(0);
        Bukkit.getOnlinePlayers().forEach(player -> this.setupLobbyPlayer(player, world));

        // Scoreboard
        GameScoreboard scoreboard = ScoreboardService.getInstance().createNewScoreboard("RFP UHC");
        scoreboard.addEntry(ChatColor.BLUE + "Hello, world!");
        scoreboard.addEntry(ChatColor.RED + "My name is Matthew");
        ScoreboardService.getInstance().setGlobalScoreboard(scoreboard);
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
            if (scoreboardTitleColor) {
                scoreboard.setTitle("" + ChatColor.WHITE + ChatColor.BOLD + "RFP UHC");
            } else {
                scoreboard.setTitle("" + ChatColor.YELLOW + ChatColor.BOLD + "RFP UHC");
            }
            scoreboardTitleColor = !scoreboardTitleColor;
        }, 10, 10);

        // Time and weather
        world.setTime(6000);
        world.setStorm(false);

        // Spawnpoint
        world.setSpawnLocation(tpLocation.getBlockX(), tpLocation.getBlockY(), tpLocation.getBlockZ());

        // Gamerules
        WorldSyncService.getInstance()
            .setGameRuleValue("naturalRegeneration", "false")
            .setGameRuleValue("spectatorsGenerateChunks", "false")
            .setGameRuleValue("doDaylightCycle", "false")
            .setGameRuleValue("doWeatherCycle", "false")
            .setGameRuleValue("doImmediateRespawn", "true");

        // Worldborder
        WorldSyncService.getInstance()
            .setWorldBorderCenter(0, 0)
            .setWorldBorderSize(3000)
            .setWorldBorderWarningTime(60);

        // Difficulty
        WorldSyncService.getInstance()
            .setDifficulty(Difficulty.PEACEFUL);

        // Clear teams
        TeamService.getInstance().clearTeams();

        // Create spectator team if it does not exist
        if (TeamService.getInstance().getTeam("spectators") == null) {
            TeamService.getInstance().newTeam("spectators", "Spectators", "SPEC");
            GameTeam specTeam = TeamService.getInstance().getTeam("spectators");
            specTeam.setFormatCode(ChatColor.GRAY);
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        switch (this.getState()) {
            case SETUP:
                this.setupLobbyPlayer(event.getPlayer(), Bukkit.getWorlds().get(0));
                break;
            default:
                break;
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        switch (this.getState()) {
            case SETUP:
            case PAUSED:
            case POST_GAME:
                event.setCancelled(true);
                break;
            default:
                break;
        }
    }

    @EventHandler
    private void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        switch (this.getState()) {
            case SETUP:
            case PAUSED:
            case POST_GAME:
                event.setCancelled(true);
                break;
            default:
                break;
        }
    }

    private void setupLobbyPlayer(Player player, World world) {
        // Teleport
        Location loc = tpLocation.toLocation(world);
        loc.setYaw(90);
        player.teleport(loc);

        // Spawnpoint
        player.setBedSpawnLocation(loc, true);

        // Game mode
        player.setGameMode(GameMode.ADVENTURE);

        // Clear inventory
        player.getInventory().clear();

        // Health & saturation
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
    }
}
