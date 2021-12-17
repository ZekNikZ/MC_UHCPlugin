package dev.mattrm.mc.uhcplugin.game;

import dev.mattrm.mc.gametools.teams.TeamService;
import dev.mattrm.mc.gametools.util.EntityUtils;
import dev.mattrm.mc.gametools.util.ISB;
import dev.mattrm.mc.uhcplugin.settings.enums.CompassBehavior;
import dev.mattrm.mc.uhcplugin.settings.SettingsManager;
import dev.mattrm.mc.uhcplugin.settings.enums.TeamStatus;
import dev.mattrm.mc.uhcplugin.settings.enums.WeatherCycle;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameEventsListener implements Listener {
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        WeatherCycle weatherState = SettingsManager.getInstance().weatherCycle().get();
        boolean toRain = event.toWeatherState();

        if (weatherState == WeatherCycle.CLEAR_ONLY && toRain) {
            event.setCancelled(true);
        } else if ((weatherState == WeatherCycle.RAIN_ONLY || weatherState == WeatherCycle.STORM_ONLY) && !toRain) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        WeatherCycle weatherState = SettingsManager.getInstance().weatherCycle().get();
        boolean toStorm = event.toThunderState();

        if ((weatherState == WeatherCycle.CLEAR_ONLY || weatherState == WeatherCycle.RAIN_ONLY) && toStorm) {
            event.setCancelled(true);
        } else if (weatherState == WeatherCycle.STORM_ONLY && !toStorm) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawnMob(CreatureSpawnEvent event) {
        if (!SettingsManager.getInstance().allowHostileSpawns().get() && EntityUtils.isHostile(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private final Map<UUID, Long> fireballCooldown = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Material material = event.getMaterial();
        Player player = event.getPlayer();

        // Fireball
        if (material == Material.FIRE_CHARGE && SettingsManager.getInstance().throwableFireballs().get() && action == Action.RIGHT_CLICK_AIR) {
            if (fireballCooldown.get(player.getUniqueId()) == null || System.currentTimeMillis() - fireballCooldown.get(player.getUniqueId()) > 1000) {
                Fireball fireball = player.launchProjectile(Fireball.class);
                fireball.setVelocity(fireball.getVelocity().multiply(2));
                fireball.setYield(fireball.getYield() * 2.5f);
                if (player.getGameMode() != GameMode.CREATIVE) {
                    player.getInventory().getItemInHand().setAmount(player.getInventory().getItemInHand().getAmount() - 1);
                }
                fireballCooldown.put(player.getUniqueId(), System.currentTimeMillis());
            } else {
                player.sendMessage(String.format(ChatColor.RED + "You must wait %.1f more seconds to do that!", (1000 - (System.currentTimeMillis() - fireballCooldown.get(player.getUniqueId()))) / 1000f));
            }
        }

        // Compass
        else if (material == Material.COMPASS) {
            if (SettingsManager.getInstance().compassBehavior().get() != CompassBehavior.NORMAL) {
                Location location = null;
                double minDistance = Double.MAX_VALUE;
                for (UUID onlinePlayerUUID : GameManager.getInstance().getAliveCompetitors()) {
                    Player onlinePlayer = Bukkit.getPlayer(onlinePlayerUUID);
                    if (onlinePlayer == null || onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                        continue;
                    }

                    if (SettingsManager.getInstance().compassBehavior().get() == CompassBehavior.TRACK_ENEMIES
                        && SettingsManager.getInstance().teamGame().get() == TeamStatus.TEAM_GAME
                        && TeamService.getInstance().getPlayerTeam(player).getId().equals(TeamService.getInstance().getPlayerTeam(onlinePlayer).getId())) {
                        continue;
                    }

                    if (onlinePlayer.getLocation().getWorld() != player.getLocation().getWorld()) {
                        continue;
                    }

                    double distance = player.getLocation().distance(onlinePlayer.getLocation());
                    if (location == null || distance < minDistance) {
                        location = onlinePlayer.getLocation();
                    }
                }

                if (location == null) {
                    player.sendMessage("Could not track any player.");
                } else {
                    player.sendMessage("Updated tracking");
                    player.setCompassTarget(location);
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        // Prevent explosion damage from fireballs
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.getDamager() instanceof Fireball) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getFrom();
        switch (GameManager.getInstance().getState()) {
            case PRE_GAME:
                to.setY(event.getTo().getY());
            case PAUSED:
                to.setPitch(event.getTo().getPitch());
                to.setYaw(event.getTo().getYaw());
                event.setTo(to);
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        switch (GameManager.getInstance().getState()) {
            case WB_CLOSING:
            case WB_STOPPED:
            case SUDDEN_DEATH:
                GameManager.getInstance().handlePlayerDeath(player);
                break;
            default:
                break;
        }
        player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof Chest) && !(event.getInventory().getHolder() instanceof DoubleChest)) {
            return;
        }

        for (ItemStack stack : event.getInventory().getContents()) {
            if (stack != null && stack.getType() == Material.GOLDEN_APPLE && stack.getDurability() == 1) {
                event.getInventory().removeItem(stack);
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item.getType() == Material.GOLDEN_APPLE && item.getDurability() == 1) {
            event.setCancelled(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));
            player.setFoodLevel(Math.min(player.getFoodLevel() + 4, 20));

            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand.getAmount() > 1) {
                itemInHand.setAmount(itemInHand.getAmount() - 1);
            } else {
                player.setItemInHand(null);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (SettingsManager.getInstance().goldenHeads().get()) {
            Player player = ((Player) event.getEntity());
            Player killer = player.getKiller();
            if (killer != null) {
                Location location = player.getLocation();
                SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
                meta.setOwner(player.getName());
                location.getWorld().dropItemNaturally(
                    location,
                    ISB.material(Material.PLAYER_HEAD)
                        .meta(meta)
                        .name(player.getName() + "'s Head")
                        .build()
                );
            }
        }
    }

    @EventHandler
    public void onGhastDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Ghast)) {
            return;
        }

        if (!SettingsManager.getInstance().regenerationPotions().get()) {
            event.getDrops().clear();
        }
    }
}
