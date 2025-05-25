package org.mystuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class McManhunt extends JavaPlugin implements Listener {

    private final Set<UUID> hunterIds = new HashSet<>();
    private boolean huntRunning = false;

    private final Map<UUID, Long> hunterTrackCooldowns = new HashMap<>();
    private static final long TRACK_COOLDOWN_MILLIS = 30 * 1000; // 30 seconds

    @Override
    public void onEnable() {
        getLogger().info("HunterHardcore plugin enabled.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("starthunt")) {
            if (huntRunning) {
                sender.sendMessage("Hunt already running.");
                return true;
            }

            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

            if (players.size() < 2) {
                sender.sendMessage("At least two players are required to start the hunt.");
                return true;
            }

            int numHunters = 1;
            if (args.length == 1) {
                try {
                    numHunters = Math.min(Integer.parseInt(args[0]), players.size() - 1);
                    if (numHunters < 1) {
                        numHunters = 1;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage("Invalid number of hunters. Defaulting to 1.");
                }
            }

            Collections.shuffle(players);
            List<Player> hunters = players.subList(0, numHunters);
            List<Player> hunted = players.subList(numHunters, players.size());

            hunterIds.clear();
            for (Player hunter : hunters) {
                hunterIds.add(hunter.getUniqueId());
                hunter.setGameMode(GameMode.SURVIVAL);
                hunter.sendMessage("You are a HUNTER! Unlimited respawns.");
                hunter.setPlayerListName(null);
            }

            for (Player p : hunted) {
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("You are being hunted. One life only!");
                p.setPlayerListName(null);
            }

            huntRunning = true;
            Bukkit.broadcastMessage("The hunt has begun with " + numHunters + " hunter(s)!");
            return true;
        }

        if (label.equalsIgnoreCase("stophunt")) {
            if (!huntRunning) {
                sender.sendMessage("No hunt is currently running.");
                return true;
            }

            huntRunning = false;
            hunterIds.clear();

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.teleport(p.getWorld().getSpawnLocation());
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("The hunt has been stopped. Game reset.");
                p.setPlayerListName(p.getName());
            }

            Bukkit.broadcastMessage("The hunt has ended. All players reset.");
            return true;
        }

        if (label.equalsIgnoreCase("tracknearest")) {
            if (!(sender instanceof Player)) {
                if (sender != null) {
                    sender.sendMessage("Only players can use this command.");
                }
                return true;
            }

            Player hunter = (Player) sender;

            if (!huntRunning || !hunterIds.contains(hunter.getUniqueId())) {
                hunter.sendMessage("Only active hunters can use this command during a hunt.");
                return true;
            }

            long now = System.currentTimeMillis();
            Long lastUsed = hunterTrackCooldowns.get(hunter.getUniqueId());

            if (lastUsed != null && (now - lastUsed) < TRACK_COOLDOWN_MILLIS) {
                long secondsLeft = (TRACK_COOLDOWN_MILLIS - (now - lastUsed)) / 1000;
                hunter.sendMessage("You must wait " + secondsLeft + " seconds before tracking again.");
                return true;
            }

            Player nearest = null;
            double nearestDistance = Double.MAX_VALUE;

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.equals(hunter)) {
                    continue;
                }
                if (hunterIds.contains(p.getUniqueId())) {
                    continue;
                }
                if (p.getGameMode() == GameMode.SPECTATOR) {
                    continue;
                }
                if (!p.getWorld().equals(hunter.getWorld())) {
                    continue;
                }

                Location hunterLoc = hunter.getLocation();
                Location pLoc = p.getLocation();
                if (hunterLoc != null && pLoc != null) {
                    double dist = hunterLoc.distanceSquared(pLoc);
                    if (dist < nearestDistance) {
                        nearest = p;
                        nearestDistance = dist;
                    }
                }
            }

            if (nearest == null) {
                hunter.sendMessage("No hunted player found to track.");
            } else {
                Location loc = nearest.getLocation();
                if (loc != null) {
                    hunter.sendMessage("Nearest hunted: " + nearest.getName()
                            + " at X: " + loc.getBlockX()
                            + ", Y: " + loc.getBlockY()
                            + ", Z: " + loc.getBlockZ());
                    nearest.sendMessage("The hunter knows your location!");
                    hunterTrackCooldowns.put(hunter.getUniqueId(), now);
                } else {
                    hunter.sendMessage("Could not determine the location of the nearest hunted player.");
                }
            }

            return true;
        }

        return false;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!huntRunning) {
            return;
        }

        event.setDeathMessage(null);
        Player player = event.getEntity();

        Bukkit.getScheduler().runTask(this, () -> {
            if (hunterIds.contains(player.getUniqueId())) {
                // Hunters respawn normally
                player.spigot().respawn();
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage("Respawned as hunter.");
            } else {
                // Hunted go spectator permanently
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage("You are dead. Spectator mode enabled.");
            }
        });
    }
}
