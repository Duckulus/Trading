package de.amin.trading.core.request;

import de.amin.trading.utils.Messages;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class TradingRequestManager {

    private final HashMap<Player, Player> requests;

    public TradingRequestManager() {
        requests = new HashMap<>();
    }

    public void sendRequest(Player sender, Player receiver) {
        sender.sendMessage(Messages.prefixed("request.sent"));
        receiver.sendMessage(Messages.prefixed("request.received", sender.getName()));

        requests.put(receiver, sender);
    }

    public Player getRequestingPlayer(Player player) {
        return requests.get(player);
    }

    public void removeRequest(Player player) {
        requests.remove(player);
    }

}
