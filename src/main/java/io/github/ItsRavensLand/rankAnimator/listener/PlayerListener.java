package io.github.ItsRavensLand.rankAnimator.listener;

import io.github.ItsRavensLand.rankAnimator.RankAnimator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final RankAnimator plugin;

    public PlayerListener(RankAnimator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        // small delay so LuckPerms finishes loading the user
        plugin.getServer().getScheduler().runTaskLater(plugin,
                () -> plugin.getAnimationManager().registerPlayer(event.getPlayer()), 5L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getAnimationManager().unregisterPlayer(event.getPlayer());
    }
}
