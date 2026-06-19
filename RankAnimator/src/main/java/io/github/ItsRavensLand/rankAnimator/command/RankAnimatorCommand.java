package io.github.ItsRavensLand.rankAnimator.command;

import io.github.ItsRavensLand.rankAnimator.RankAnimator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class RankAnimatorCommand implements CommandExecutor, TabCompleter {

    private final RankAnimator plugin;
    private static final String PREFIX = ChatColor.GOLD + "[RA] " + ChatColor.RESET;

    public RankAnimatorCommand(RankAnimator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("rankanimator.admin")) {
            sender.sendMessage(PREFIX + ChatColor.RED + "No permission.");
            return true;
        }

        if (args.length == 0) { sendHelp(sender); return true; }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                plugin.reload();
                sender.sendMessage(PREFIX + ChatColor.GREEN + "Reloaded.");
            }
            case "refresh" -> {
                if (args.length < 2) {
                    Bukkit.getOnlinePlayers().forEach(plugin.getAnimationManager()::refreshPlayer);
                    sender.sendMessage(PREFIX + ChatColor.GREEN + "Refreshed all players.");
                } else {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(PREFIX + ChatColor.RED + "Player not found.");
                        return true;
                    }
                    plugin.getAnimationManager().refreshPlayer(target);
                    sender.sendMessage(PREFIX + ChatColor.GREEN + "Refreshed " + target.getName() + ".");
                }
            }
            case "info" -> {
                sender.sendMessage(PREFIX + ChatColor.YELLOW + "Players: " + ChatColor.WHITE + plugin.getAnimationManager().getPlayerStates().size());
                sender.sendMessage(PREFIX + ChatColor.YELLOW + "Animations: " + ChatColor.WHITE + plugin.getConfigManager().getAnimations().size());
                sender.sendMessage(PREFIX + ChatColor.YELLOW + "Groups: " + ChatColor.WHITE + plugin.getConfigManager().getGroupAnimationMap().size());
            }
            default -> sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(PREFIX + ChatColor.YELLOW + "/ra reload " + ChatColor.GRAY + "- reload configs");
        sender.sendMessage(PREFIX + ChatColor.YELLOW + "/ra refresh [player] " + ChatColor.GRAY + "- refresh animation");
        sender.sendMessage(PREFIX + ChatColor.YELLOW + "/ra info " + ChatColor.GRAY + "- show stats");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return Arrays.asList("reload", "refresh", "info");
        if (args.length == 2 && args[0].equalsIgnoreCase("refresh"))
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        return List.of();
    }
}
