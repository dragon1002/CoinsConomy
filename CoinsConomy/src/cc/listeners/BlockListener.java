package cc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class BlockListener
  implements Listener
{
  @EventHandler
  public void onChange(SignChangeEvent evt)
  {
    if ((evt.getLine(0).equalsIgnoreCase("[shop]")) && 
      (evt.getPlayer().hasPermission("hub.admin"))) {
      evt.setLine(0, "§7[§6KAUFEN§7]");
      evt.setLine(1, "Item ID: " + evt.getLine(1));
      evt.setLine(2, "Menge: " + evt.getLine(2));
      evt.setLine(3, evt.getLine(3) + " Coins");
    }
  }
}