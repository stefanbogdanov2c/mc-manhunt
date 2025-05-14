package org.mystuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    private UUID hunterId = null;

    @Override
    public void onEnable() {
        getLogger().info("HunterHardcore plugin enabled.");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("starthunt")) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

            if (players.size() < 2) {
                sender.sendMessage("At least two players are required to start the hunt.");
                return true;
            }

            Player hunter = players.remove(new Random().nextInt(players.size()));
            hunterId = hunter.getUniqueId();
            hunter.sendMessage("You are the HUNTER! Unlimited respawns.");

            for (Player p : players) {
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("You are being hunted. One life only!");
            }

            Bukkit.broadcastMessage("The hunt has begun!");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null); // disable death message

        Player player = event.getEntity();
        Bukkit.getScheduler().runTask(this, () -> {
            if (player.getUniqueId().equals(hunterId)) {
                // respawn hunter
                player.spigot().respawn();
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage("Respawned as hunter.");
            } else {
                // hunted player goes into spectator
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage("You are dead. Spectator mode enabled.");
            }
        });
    }
}
