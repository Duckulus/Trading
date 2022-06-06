package de.amin.trading.core.inventory;

import de.amin.trading.core.TradingManager;
import de.amin.trading.core.data.Trade;
import de.amin.trading.core.data.TradePhase;
import de.amin.trading.core.data.TradePlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class InventoryListener implements Listener {

    private final TradingManager tradingManager;

    public InventoryListener(TradingManager tradingManager) {
        this.tradingManager = tradingManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!event.getAction().equals(InventoryAction.PICKUP_ALL) && !event.getAction().equals(InventoryAction.PLACE_ALL)) {
            event.setCancelled(true);
            return;
        }
        if (event.getClickedInventory() == null || !(event.getClickedInventory().getHolder() instanceof TradingHolder)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        Trade trade = tradingManager.getTrade(player);
        if (trade == null) {
            player.closeInventory();
            player.sendMessage("Internal Error");
            return;
        }
        int slot = event.getSlot();
        if (contains(Slots.glass, slot) || contains(Slots.barrier, slot) || contains(Slots.partnerItems, slot) || Slots.partnerPhaseButton==slot) {
            event.setCancelled(true);
            return;
        }


        TradePlayerData tradeData = trade.getTradeData(player);
        TradePlayerData partnerData = trade.getPartnerData(player);
        if (contains(Slots.ownItems, event.getSlot())) {
            if (tradeData.getPhase().equals(TradePhase.EDITING)) {

                if (event.getCursor() != null && event.getClickedInventory().getItem(event.getSlot()) == null) {
                    tradeData.getItemStacks()[index(Slots.ownItems, slot)] = event.getCursor().clone();
                    event.setCursor(null);
                }else if (event.getClickedInventory().getItem(event.getSlot()) != null) {
                    remove(tradeData.getItemStacks(), index(Slots.ownItems, slot));
                    event.setCursor(event.getClickedInventory().getItem(event.getSlot()).clone());
                }
                event.setCancelled(true);
            } else if (tradeData.getPhase().equals(TradePhase.LOCKED)) {
                event.setCancelled(true);
            }
            return;
        }

        if (Slots.phaseButton==slot && !tradeData.isFinalized()) {
            if (tradeData.getPhase().equals(TradePhase.EDITING)) {
                tradeData.setPhase(TradePhase.LOCKED);
            } else if (tradeData.getPhase().equals(TradePhase.LOCKED)) {
                tradeData.setPhase(TradePhase.EDITING);
                tradeData.setFinalized(false);
                partnerData.setFinalized(false);
            }
            event.setCancelled(true);
            return;
        }

        if(Slots.finalize == slot) {
            if(tradeData.getPhase().equals(TradePhase.LOCKED) && partnerData.getPhase().equals(TradePhase.LOCKED) && !tradeData.isFinalized()) {
                tradeData.setFinalized(true);
            }
            event.setCancelled(true);
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if(event.getInventory().getHolder() instanceof TradingHolder) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getInventory().getHolder() instanceof TradingHolder)) {
            return;
        }
        Player player = (Player) event.getPlayer();
        Trade trade = tradingManager.getTrade(player);
        if(trade==null)return;
        tradingManager.endTrade(trade);
        Player partner = trade.getPartnerData(player).getPlayer();
        partner.closeInventory();
        for (Player tradePlayer : trade.getPlayers()) {
            tradePlayer.sendMessage("Trade cancelled");
        }

    }

    private boolean contains(int[] arr, int num) {
        for (int j : arr) {
            if (j == num) {
                return true;
            }
        }
        return false;
    }

    private int index(int[] arr, int t) {
        int index = Arrays.binarySearch(arr, t);
        return (index < 0) ? -1 : index;
    }

    private void remove(ItemStack[] arr, int  index) {
        arr[index] = null;
    }

}
