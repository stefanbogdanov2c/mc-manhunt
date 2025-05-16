package org.mystuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HunterHardcorePlugin extends JavaPlugin implements Listener {

    private final Set<UUID> hunterIds = new HashSet<>();
    private boolean huntRunning = false;

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
            }

            Bukkit.broadcastMessage("The hunt has ended. All players reset.");
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
