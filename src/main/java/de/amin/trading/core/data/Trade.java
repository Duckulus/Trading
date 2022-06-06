package de.amin.trading.core.data;

import org.bukkit.entity.Player;

import java.util.Arrays;

public class Trade {

    private final TradePlayerData[] players;

    public Trade(Player player1, Player player2) {
        players = new TradePlayerData[]{
                new TradePlayerData(player1),
                new TradePlayerData(player2)
        };
    }

    public Player[] getPlayers() {
        return new Player[]{
                players[0].getPlayer(),
                players[1].getPlayer()
        };
    }

    public boolean hasPlayer(Player player) {
        return Arrays.stream(players).anyMatch(tradePlayerData -> tradePlayerData.getPlayer().equals(player));
    }

    public TradePlayerData getTradeData(Player player) {
        return Arrays.stream(players).filter(tradePlayerData -> tradePlayerData.getPlayer().equals(player)).findAny().orElse(null);
    }

    public TradePlayerData getPartnerData(Player player) {
        return Arrays.stream(players).filter(tradePlayerData -> !tradePlayerData.getPlayer().equals(player)).findAny().orElse(null);
    }

}
