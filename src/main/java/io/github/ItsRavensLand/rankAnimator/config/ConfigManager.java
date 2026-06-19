package io.github.ItsRavensLand.rankAnimator.config;

import io.github.ItsRavensLand.rankAnimator.RankAnimator;
import io.github.ItsRavensLand.rankAnimator.animation.Animation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class ConfigManager {

    private final RankAnimator plugin;
    private final Logger log;

    private final Map<String, Animation> animations = new HashMap<>();
    private final Map<String, String> groupAnimationMap = new LinkedHashMap<>();

    private String fallbackPrefix;
    private int updateInterval;
    private boolean enabled;

    public ConfigManager(RankAnimator plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
    }

    public void load() {
        animations.clear();
        groupAnimationMap.clear();
        loadAnimations();
        loadConfig();
        log.info("Loaded " + animations.size() + " animations, " + groupAnimationMap.size() + " group mappings.");
    }

    private void loadAnimations() {
        File file = new File(plugin.getDataFolder(), "animations.yml");
        if (!file.exists()) plugin.saveResource("animations.yml", false);

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if (!cfg.isConfigurationSection("animations")) {
            log.warning("No 'animations' section found in animations.yml.");
            return;
        }

        Set<String> keys = cfg.getConfigurationSection("animations").getKeys(false);
        if (keys.isEmpty()) {
            log.info("animations.yml has no animations yet.");
            return;
        }

        for (String key : keys) {
            String path = "animations." + key;
            int interval = cfg.getInt(path + ".interval", 10);
            List<String> frames = cfg.getStringList(path + ".frames");

            if (frames.isEmpty()) {
                log.warning("Animation '" + key + "' has no frames, skipping.");
                continue;
            }

            List<String> colored = new ArrayList<>();
            for (String frame : frames) colored.add(colorize(frame));

            animations.put(key, new Animation(key, colored, interval));
        }
    }

    private void loadConfig() {
        FileConfiguration cfg = plugin.getConfig();

        enabled = cfg.getBoolean("enabled", true);
        updateInterval = cfg.getInt("update-interval", 1);
        fallbackPrefix = colorize(cfg.getString("fallback-prefix", ""));

        if (!cfg.isConfigurationSection("groups")) return;

        for (String group : cfg.getConfigurationSection("groups").getKeys(false)) {
            String animName = cfg.getString("groups." + group + ".animation");
            if (animName == null || animName.isEmpty()) continue;

            if (!animations.containsKey(animName)) {
                log.warning("Group '" + group + "' references unknown animation '" + animName + "'.");
                continue;
            }

            groupAnimationMap.put(group.toLowerCase(), animName);
        }
    }

    private String colorize(String text) {
        return text == null ? "" : text.replace("&", "\u00a7");
    }

    public Animation getAnimationForGroup(String group) {
        String name = groupAnimationMap.get(group.toLowerCase());
        if (name == null) return null;
        Animation base = animations.get(name);
        return base == null ? null : base.copy();
    }

    public Animation getFallbackAnimation() {
        if (fallbackPrefix == null || fallbackPrefix.isEmpty()) return null;
        return new Animation("fallback", List.of(fallbackPrefix), 20);
    }

    public Map<String, Animation> getAnimations() { return animations; }
    public Map<String, String> getGroupAnimationMap() { return groupAnimationMap; }
    public String getFallbackPrefix() { return fallbackPrefix; }
    public int getUpdateInterval() { return updateInterval; }
    public boolean isEnabled() { return enabled; }
}
