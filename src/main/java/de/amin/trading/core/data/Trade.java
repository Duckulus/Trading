package de.amin.trading.core.data;

import org.bukkit.entity.Player;

import java.util.Arrays;

public class Trade {

    private final TradePlayerData[] playerData;

    public Trade(Player player1, Player player2) {
        playerData = new TradePlayerData[]{
                new TradePlayerData(player1),
                new TradePlayerData(player2)
        };
    }

    public Player[] getPlayers() {
        return new Player[]{
                playerData[0].getPlayer(),
                playerData[1].getPlayer()
        };
    }

    public boolean hasPlayer(Player player) {
        return Arrays.stream(playerData).anyMatch(tradePlayerData -> tradePlayerData.getPlayer().equals(player));
    }

    public TradePlayerData getTradeData(Player player) {
        return Arrays.stream(playerData).filter(tradePlayerData -> tradePlayerData.getPlayer().equals(player)).findAny().orElse(null);
    }

    public TradePlayerData getPartnerData(Player player) {
        return Arrays.stream(playerData).filter(tradePlayerData -> !tradePlayerData.getPlayer().equals(player)).findAny().orElse(null);
    }

}
