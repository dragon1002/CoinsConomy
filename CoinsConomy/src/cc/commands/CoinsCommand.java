package cc.commands;

import cc.main.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand
  implements CommandExecutor
{
  private main plugin;

  public CoinsCommand(main plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
  {
    if ((cs instanceof Player)) {
      Player p = (Player)cs;
      p.sendMessage(main.Prefix + "§aDu hast §6" + this.plugin.getCoins(p.getName()) + " §aCoins.");
    }
    return true;
  }
}