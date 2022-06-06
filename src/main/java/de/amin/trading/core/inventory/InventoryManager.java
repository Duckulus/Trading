package de.amin.trading.core.inventory;

import de.amin.trading.TradingPlugin;
import de.amin.trading.core.TradingManager;
import de.amin.trading.core.data.Trade;
import de.amin.trading.core.data.TradePhase;
import de.amin.trading.core.data.TradePlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryManager {


    private final TradingManager tradingManager;
    private final HashMap<Player, Inventory> inventories;

    public InventoryManager(TradingPlugin plugin, TradingManager tradingManager) {
        this.tradingManager = tradingManager;
        inventories = new HashMap<>();
        Bukkit.getScheduler().runTaskTimer(plugin, this::updateInventories, 1, 1);
    }

    public Inventory getInventory(Player player) {
        Inventory inventory = inventories.get(player);
        Trade trade = tradingManager.getTrade(player);
        Component partnerName = trade.getPartnerData(player).getPlayer().displayName();
        if (inventory == null) {
            inventory = Bukkit.createInventory(new TradingHolder(), 9 * 4, Component.text("You              | ").append(partnerName));
            inventories.put(player, inventory);
        }
        return inventory;
    }

    public void updateInventories() {

        inventories.forEach((player, inventory) -> {
            Trade trade = tradingManager.getTrade(player);
            if (trade != null) {
                TradePlayerData tradeData = trade.getTradeData(player);
                TradePlayerData partnerData = trade.getPartnerData(player);

                for (int i : Slots.glass) {
                    inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                }
                for (int i : Slots.barrier) {
                    inventory.setItem(i, new ItemStack(Material.BARRIER));
                }
                for (int i = 0; i < Slots.ownItems.length; i++) {
                    inventory.setItem(Slots.ownItems[i], tradeData.getItemStacks()[i]);
                }
                for (int i = 0; i < Slots.partnerItems.length; i++) {
                    inventory.setItem(Slots.partnerItems[i], partnerData.getItemStacks()[i]);
                }
                inventory.setItem(Slots.phaseButton, new ItemStack(getItem(tradeData.getPhase())));
                inventory.setItem(Slots.partnerPhaseButton, new ItemStack(getItem(partnerData.getPhase())));

                if (tradeData.getPhase().equals(TradePhase.LOCKED) && partnerData.getPhase().equals(TradePhase.LOCKED) && !tradeData.isFinalized()) {
                    inventory.setItem(Slots.finalize, new ItemStack(Material.DIAMOND));
                } else if (tradeData.getPhase().equals(TradePhase.LOCKED) && partnerData.getPhase().equals(TradePhase.LOCKED) && tradeData.isFinalized()) {
                    inventory.setItem(Slots.finalize, new ItemStack(Material.EMERALD));
                } else {
                    inventory.setItem(Slots.finalize, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                }

                if(tradeData.isFinalized() && partnerData.isFinalized()) {
                    tradingManager.finishTrade(trade);
                }
            }
        });


    }

    private Material getItem(TradePhase phase) {
        switch (phase) {
            case EDITING -> {
                return Material.GRAY_DYE;
            }
            case LOCKED -> {
                return Material.LIME_DYE;
            }
            default -> {
                return Material.YELLOW_DYE;
            }
        }
    }

}
