package de.amin.trading.core.data;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TradePlayerData {

    private final Player player;
    private final ItemStack[] itemStacks;
    private TradePhase phase;
    private boolean finalized;

    public TradePlayerData(Player player) {
        this.player = player;
        itemStacks = new ItemStack[12];
        phase = TradePhase.EDITING;
        finalized = false;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack[] getItemStacks() {
        return itemStacks;
    }

    public TradePhase getPhase() {
        return phase;
    }

    public void setPhase(TradePhase phase) {
        this.phase = phase;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }
}
