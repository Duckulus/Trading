package de.amin.trading.core;

import de.amin.trading.core.data.Trade;
import de.amin.trading.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class TradingManager {

    private final HashSet<Trade> trades;

    public TradingManager() {
        trades = new HashSet<>();
    }

    public void createTrade(Player player1, Player player2) {
        Trade trade = new Trade(player1, player2);
        trades.stream().filter(t -> t.hasPlayer(player1) || t.hasPlayer(player2)).forEach(this::endTrade);
        initTrade(trade);
    }

    public Trade getTrade(Player player) {
        return trades.stream().filter(trade -> trade.hasPlayer(player)).findAny().orElse(null);
    }

    private void initTrade(Trade trade) {
        trades.add(trade);
    }

    public void endTrade(Trade trade) {
        if(trade==null)return;
        trades.remove(trade);
        for (int i = 0; i < trade.getPlayers().length; i++) {
            //give each player the items back he placed, validate if null for each item
            trade.getPlayers()[i].getInventory().addItem(Arrays.stream(trade.getTradeData(trade.getPlayers()[i]).getItemStacks()).filter(Objects::nonNull).toArray(ItemStack[]::new));
        }

    }

    public void finishTrade(Trade trade) {
        if(trade==null)return;
        trades.remove(trade);
        Player player1 = trade.getPlayers()[0].getPlayer();
        Player player2 = trade.getPlayers()[1].getPlayer();
        player1.getInventory().addItem(Arrays.stream(trade.getTradeData(player2).getItemStacks()).filter(Objects::nonNull).toArray(ItemStack[]::new));
        player2.getInventory().addItem(Arrays.stream(trade.getTradeData(player1).getItemStacks()).filter(Objects::nonNull).toArray(ItemStack[]::new));

        for (Player player : trade.getPlayers()) {
            player.closeInventory();
            player.sendMessage(Messages.prefixed("trade.finished"));
        }
    }


}
