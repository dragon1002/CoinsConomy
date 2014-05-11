package cc.listeners;

import cc.data.MySQL;
import cc.main.main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerListener
  implements Listener
{
  private main plugin;

  public PlayerListener(main plugin)
  {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent evt)
  {
    Player p = evt.getPlayer();
    if ((evt.getAction() == Action.RIGHT_CLICK_BLOCK) && (
      (evt.getClickedBlock().getType() == Material.SIGN_POST) || 
      (evt.getClickedBlock().getType() == Material.WALL_SIGN))) {
      Sign s = (Sign)evt.getClickedBlock().getState();
      if (s.getLine(0).equalsIgnoreCase("§7[§6KAUFEN§7]")) {
        int id = Integer.parseInt(s.getLine(1).replace("Item ID: ", 
          ""));
        int amount = Integer.parseInt(s.getLine(2).replace(
          "Menge: ", ""));
        int coins = Integer.parseInt(s.getLine(3).replace(" Coins", 
          ""));
        Material m = Material.getMaterial(id);
        ItemStack is = new ItemStack(m, amount, (short)0);
        if (p.getInventory().firstEmpty() != -1) {
          if (this.plugin.getCoins(p.getName()) >= coins) {
            p.getInventory().addItem(new ItemStack[] { is });
            p.sendMessage(main.Prefix + "§aDu hast §6" + 
              is.getAmount() + " " + 
              is.getType().toString() + "§a für §6" + 
              coins + " §aCoins gekauft.");
            p.updateInventory();
            this.plugin.addCoins(p.getName(), -coins);
          } else {
            p.sendMessage(main.Prefix + 
              "§cDu hast nicht genügend Coins!");
          }
        }
        else p.sendMessage(main.Prefix + 
            "§cDu kannst keine Items kaufen, wenn dein Inventar voll ist.");
      }
    }
  }

  @EventHandler
  public void onDamage(PlayerDeathEvent evt)
  {
    Player p = evt.getEntity();
    if (p.getKiller() != null) {
      Player killer = p.getKiller();
      int i = this.plugin.getConfig().getInt("coins_per_kill");
      this.plugin.addPvPEncounter(p.getName(), killer.getName());
      if (p.hasPermission("hub.vip"))
        this.plugin.addCoins(p.getKiller().getName(), i * 2);
      else
        this.plugin.addCoins(p.getKiller().getName(), i);
    }
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent evt)
  {
    Player p = evt.getPlayer();
    String name = p.getName();
    if (this.plugin.getId(p.getName()) == 0)
      this.plugin.getMySQL().queryUpdate(
        "INSERT INTO " + main.table_players + 
        " (player, kills, deaths) VALUES ('" + name + 
        "', '0', '0')");
  }
}