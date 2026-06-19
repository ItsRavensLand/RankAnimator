package io.github.ItsRavensLand.rankAnimator.hook;

import io.github.ItsRavensLand.rankAnimator.RankAnimator;
import io.github.ItsRavensLand.rankAnimator.animation.PlayerAnimationState;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class RankAnimatorExpansion extends PlaceholderExpansion {

    private final RankAnimator plugin;

    public RankAnimatorExpansion(RankAnimator plugin) {
        this.plugin = plugin;
    }

    @Override public @NotNull String getIdentifier() { return "rankanimator"; }
    @Override public @NotNull String getAuthor() { return "ItsRavensLand"; }
    @Override public @NotNull String getVersion() { return plugin.getDescription().getVersion(); }
    @Override public boolean persist() { return true; }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null || !player.isOnline()) return "";

        if (params.equals("prefix")) {
            PlayerAnimationState state = plugin.getAnimationManager()
                    .getPlayerStates().get(player.getUniqueId());
            return state == null ? "" : state.getCurrentPrefix();
        }

        return null;
    }
}
