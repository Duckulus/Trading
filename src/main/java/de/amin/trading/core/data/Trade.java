package de.amin.trading.core.data;

import de.amin.trading.utils.Constants;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public class Trade {

    private final TradePlayerData[] playerData;
    private BukkitTask tradeTask;
    private boolean isTimerRunning = false;
    private int timer = Constants.TRADE_TIMER+1;

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

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public BukkitTask getTradeTask() {
        return tradeTask;
    }

    public void setTradeTask(BukkitTask tradeTask) {
        this.tradeTask = tradeTask;
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        isTimerRunning = timerRunning;
    }
}
