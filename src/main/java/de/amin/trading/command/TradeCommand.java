package de.amin.trading.command;

import de.amin.trading.core.TradingManager;
import de.amin.trading.core.inventory.InventoryManager;
import de.amin.trading.core.request.TradingRequestManager;
import de.amin.trading.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TradeCommand implements CommandExecutor, TabCompleter {

    private final TradingManager tradingManager;
    private final InventoryManager inventoryManager;
    private final TradingRequestManager tradingRequestManager;

    public TradeCommand(TradingManager tradingManager, InventoryManager inventoryManager, TradingRequestManager tradingRequestManager) {
        this.tradingManager = tradingManager;
        this.inventoryManager = inventoryManager;
        this.tradingRequestManager = tradingRequestManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!(args.length > 0)) {
            player.sendMessage(Messages.prefixed("command.trade.usage"));
            return true;
        }

        if (args[0].equalsIgnoreCase("request")) {
            if (!(args.length > 1)) {
                player.sendMessage(Messages.prefixed("command.trade.request.usage"));
                return true;
            }
            Player receiver = Bukkit.getPlayer(args[1]);
            if (receiver == null || receiver == player) {
                player.sendMessage(Messages.prefixed("command.trade.request.error.offline"));
                return true;
            }
            tradingRequestManager.sendRequest(player, receiver);
        } else if (args[0].equalsIgnoreCase("accept")) {
            Player incoming = tradingRequestManager.getRequestingPlayer(player);
            if (incoming != null) {
                tradingManager.createTrade(player, incoming);
                player.openInventory(inventoryManager.getInventory(player));
                incoming.openInventory(inventoryManager.getInventory(incoming));
            } else {
                player.sendMessage(Messages.prefixed("command.trade.accept.error.no_request"));
            }
            tradingRequestManager.removeRequest(player);
        } else {
            player.sendMessage(Messages.get("command.trade.usage"));
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("request", "accept"), completions);
            Collections.sort(completions);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("request")) {
            return null;
        }

        return completions;
    }
}
