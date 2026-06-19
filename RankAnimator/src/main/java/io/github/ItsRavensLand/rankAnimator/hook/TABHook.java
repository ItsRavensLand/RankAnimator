package io.github.ItsRavensLand.rankAnimator.hook;

import io.github.ItsRavensLand.rankAnimator.RankAnimator;

// Only checks TAB presence; actual prefix is delivered via %rankanimator_prefix%
public class TABHook {

    private final RankAnimator plugin;
    private boolean present = false;

    public TABHook(RankAnimator plugin) {
        this.plugin = plugin;
    }

    public boolean init() {
        present = plugin.getServer().getPluginManager().getPlugin("TAB") != null;
        if (present) plugin.getLogger().info("TAB detected.");
        else plugin.getLogger().warning("TAB not found.");
        return present;
    }

    public boolean isPresent() { return present; }
}
