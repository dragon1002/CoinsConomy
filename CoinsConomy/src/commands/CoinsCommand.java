package commands;

import main.main;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class CoinsCommand
    implements CommandExecutor
{

    public CoinsCommand(main plugin)
    {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String args[])
    {
        if(cs instanceof Player)
        {
            Player p = (Player)cs;
            p.sendMessage((new StringBuilder(String.valueOf(main.Prefix))).append("\247aDu hast \2476").append(plugin.getCoins(p.getName())).append(" \247aCoins.").toString());
        }
        return true;
    }

    private main plugin;
}