package de.amin.trading;

import de.amin.trading.command.TradeCommand;
import de.amin.trading.core.TradingManager;
import de.amin.trading.core.inventory.InventoryListener;
import de.amin.trading.core.inventory.InventoryManager;
import de.amin.trading.core.request.TradingRequestManager;
import de.amin.trading.utils.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public class TradingPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Hello World");

        TradingManager tradingManager = new TradingManager(this);
        InventoryManager inventoryManager = new InventoryManager(this, tradingManager);
        TradingRequestManager tradingRequestManager = new TradingRequestManager();

        saveResource("messages.yml", false);
        Messages.init(this);

        getCommand("trade").setExecutor(new TradeCommand(tradingManager,inventoryManager,tradingRequestManager));
        getServer().getPluginManager().registerEvents(new InventoryListener(tradingManager), this);
    }
}
