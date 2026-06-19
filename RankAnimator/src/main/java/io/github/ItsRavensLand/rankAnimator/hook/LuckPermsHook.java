package io.github.ItsRavensLand.rankAnimator.hook;

import io.github.ItsRavensLand.rankAnimator.RankAnimator;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class LuckPermsHook {

    private final RankAnimator plugin;
    private LuckPerms luckPerms;

    public LuckPermsHook(RankAnimator plugin) {
        this.plugin = plugin;
    }

    public boolean init() {
        RegisteredServiceProvider<LuckPerms> provider =
                Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) return false;
        luckPerms = provider.getProvider();
        plugin.getLogger().info("Hooked into LuckPerms.");
        return true;
    }

    public String getPrimaryGroup(Player player) {
        return getPrimaryGroup(player.getUniqueId());
    }

    public String getPrimaryGroup(UUID uuid) {
        User user = luckPerms.getUserManager().getUser(uuid);
        return user == null ? "default" : user.getPrimaryGroup();
    }

    public LuckPerms getApi() { return luckPerms; }
}
