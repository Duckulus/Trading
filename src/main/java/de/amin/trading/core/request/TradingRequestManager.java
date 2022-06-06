package de.amin.trading.core.request;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class TradingRequestManager {

    private final HashMap<Player, Player> requests;

    public TradingRequestManager() {
        requests = new HashMap<>();
    }

    public void sendRequest(Player sender, Player receiver) {
        sender.sendMessage("Request sent");
        receiver.sendMessage("Request received");

        requests.put(receiver, sender);
    }

    public Player getRequestingPlayer(Player player) {
        return requests.get(player);
    }

    public void removeRequest(Player player) {
        requests.remove(player);
    }

}
