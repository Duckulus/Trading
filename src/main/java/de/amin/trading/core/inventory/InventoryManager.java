package de.amin.trading.core.inventory;

import de.amin.trading.TradingPlugin;
import de.amin.trading.core.TradingManager;
import de.amin.trading.core.data.Trade;
import de.amin.trading.core.data.TradePhase;
import de.amin.trading.core.data.TradePlayerData;
import de.amin.trading.utils.ItemBuilder;
import de.amin.trading.utils.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
            inventory = Bukkit.createInventory(new TradingHolder(), 9 * 4, Messages.get("inventory.title", PlainTextComponentSerializer.plainText().serialize(partnerName)));
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
                    inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(Messages.get("inventory.item.empty")).build());
                }
                for (int i : Slots.barrier) {
                    inventory.setItem(i, new ItemBuilder(Material.BARRIER).setDisplayName(Messages.get("inventory.item.empty")).build());
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
                    inventory.setItem(Slots.finalize, new ItemBuilder(Material.DIAMOND).setDisplayName(Messages.get("inventory.item.finalize")).build());
                } else if (tradeData.getPhase().equals(TradePhase.LOCKED) && partnerData.getPhase().equals(TradePhase.LOCKED) && tradeData.isFinalized()) {
                    inventory.setItem(Slots.finalize, new ItemBuilder(Material.EMERALD).setDisplayName(Messages.get("inventory.item.finalized")).build());
                } else {
                    inventory.setItem(Slots.finalize, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(Messages.get("inventory.item.waiting")).build());
                }

                if(trade.isTimerRunning()) {
                    inventory.setItem(Slots.finalize, new ItemBuilder(Material.CLOCK).setDisplayName(Component.text(trade.getTimer())).setAmount(trade.getTimer()).build());
                }

                if(tradeData.isFinalized() && partnerData.isFinalized() && !trade.isTimerRunning()) {
                    tradingManager.startTimer(trade);
                }
            }
        });


    }

    private ItemStack getItem(TradePhase phase) {
        switch (phase) {
            case EDITING -> {
                return new ItemBuilder(Material.GRAY_DYE).setDisplayName(Messages.get("inventory.item.editing")).build();
            }
            case LOCKED -> {
                return new ItemBuilder(Material.LIME_DYE).setDisplayName(Messages.get("inventory.item.locked")).build();
            }
            default -> {
                return new ItemStack(Material.YELLOW_DYE);
            }
        }
    }

}
