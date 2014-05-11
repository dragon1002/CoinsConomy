package cc.commands;

import cc.main.main;
import java.io.IOException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddCoinsCommand 
  implements CommandExecutor
{
  private main plugin;

  public AddCoinsCommand(main plugin)
  {
    this.plugin = plugin;
  }

  public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
  {
    if (cs.hasPermission("hub.admin")) {
      if (args.length == 2) {
        this.plugin.addCoins(args[0], Integer.parseInt(args[1]));
        try {
          this.plugin.sendServerMessage(args[0], main.Prefix + 
            "§aDu hast §6" + args[1] + " §aCoins erhalten.");
          cs.sendMessage(main.Prefix + "§6" +  args[0] + "§ahat§6" + args[1] + "§aCoins erhalten");
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        cs.sendMessage("§c/addcoins <spieler> <menge>");
      }
    } else {
    	cs.sendMessage("§cNope!");
    }
    return true;
  }
}