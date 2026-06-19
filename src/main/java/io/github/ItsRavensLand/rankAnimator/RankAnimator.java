package io.github.ItsRavensLand.rankAnimator;

import io.github.ItsRavensLand.rankAnimator.command.RankAnimatorCommand;
import io.github.ItsRavensLand.rankAnimator.config.ConfigManager;
import io.github.ItsRavensLand.rankAnimator.hook.LuckPermsHook;
import io.github.ItsRavensLand.rankAnimator.hook.RankAnimatorExpansion;
import io.github.ItsRavensLand.rankAnimator.hook.TABHook;
import io.github.ItsRavensLand.rankAnimator.listener.PlayerListener;
import io.github.ItsRavensLand.rankAnimator.manager.AnimationManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RankAnimator extends JavaPlugin {

    private static RankAnimator instance;

    private ConfigManager configManager;
    private AnimationManager animationManager;
    private LuckPermsHook luckPermsHook;
    private TABHook tabHook;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("animations.yml", false);

        configManager = new ConfigManager(this);
        configManager.load();

        luckPermsHook = new LuckPermsHook(this);
        if (!luckPermsHook.init()) {
            getLogger().severe("LuckPerms not found, disabling.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        tabHook = new TABHook(this);
        tabHook.init();

        animationManager = new AnimationManager(this);
        animationManager.start();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new RankAnimatorExpansion(this).register();
            getLogger().info("Registered placeholder: %rankanimator_prefix%");
        } else {
            getLogger().severe("PlaceholderAPI not found! Install it and set tabprefix to %rankanimator_prefix% in TAB's groups.yml.");
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        RankAnimatorCommand cmd = new RankAnimatorCommand(this);
        getCommand("rankanimator").setExecutor(cmd);
        getCommand("rankanimator").setTabCompleter(cmd);

        getLogger().info("RankAnimator ready.");
    }

    @Override
    public void onDisable() {
        if (animationManager != null) animationManager.stop();
    }

    public void reload() {
        reloadConfig();
        configManager.load();
        animationManager.reload();
    }

    public static RankAnimator getInstance() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public AnimationManager getAnimationManager() { return animationManager; }
    public LuckPermsHook getLuckPermsHook() { return luckPermsHook; }
    public TABHook getTabHook() { return tabHook; }
}
