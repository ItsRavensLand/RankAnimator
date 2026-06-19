package io.github.ItsRavensLand.rankAnimator.manager;

import io.github.ItsRavensLand.rankAnimator.RankAnimator;
import io.github.ItsRavensLand.rankAnimator.animation.Animation;
import io.github.ItsRavensLand.rankAnimator.animation.PlayerAnimationState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AnimationManager {

    private final RankAnimator plugin;
    private final Map<UUID, PlayerAnimationState> playerStates = new ConcurrentHashMap<>();
    private BukkitTask task;

    public AnimationManager(RankAnimator plugin) {
        this.plugin = plugin;
    }

    public void start() {
        int interval = plugin.getConfigManager().getUpdateInterval();
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 0L, interval);
        Bukkit.getOnlinePlayers().forEach(this::registerPlayer);
        plugin.getLogger().info("Animation engine started.");
    }

    public void stop() {
        if (task != null) { task.cancel(); task = null; }
        playerStates.clear();
    }

    public void reload() {
        stop();
        start();
    }

    private void tick() {
        if (!plugin.getConfigManager().isEnabled()) return;
        playerStates.values().forEach(PlayerAnimationState::tick);
    }

    public void registerPlayer(Player player) {
        Animation anim = plugin.getConfigManager()
                .getAnimationForGroup(plugin.getLuckPermsHook().getPrimaryGroup(player));

        if (anim == null) anim = plugin.getConfigManager().getFallbackAnimation();
        if (anim == null) return;

        playerStates.put(player.getUniqueId(), new PlayerAnimationState(player.getUniqueId(), anim));
    }

    public void unregisterPlayer(Player player) {
        playerStates.remove(player.getUniqueId());
    }

    public void refreshPlayer(Player player) {
        unregisterPlayer(player);
        registerPlayer(player);
    }

    public Map<UUID, PlayerAnimationState> getPlayerStates() { return playerStates; }
}
