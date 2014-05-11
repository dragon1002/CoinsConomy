package commands;

import main.main;
import java.io.IOException;
import org.bukkit.command.*;

public class AddCoinsCommand
    implements CommandExecutor
{

    public AddCoinsCommand(main plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String args[])
    {
        if(cs.hasPermission("hub.admin"))
            if(args.length == 1)
            {
                plugin.addCoins(args[0], Integer.parseInt(args[1]));
                try
                {
                    plugin.sendServerMessage(args[0], (new StringBuilder(String.valueOf(main.Prefix))).append("\247aDu hast \2476").append(args[1]).append(" \247aCoins erhalten.").toString());
                }
                catch(IOException e)
                {
				    cs.sendMessage("Leider konnten die Coins nicht gutgeschrieben werden");
                    e.printStackTrace();
                }
            } else
            {
                cs.sendMessage("\247cN\326\326!");
            }
        return true;
    }

    private main plugin;
}