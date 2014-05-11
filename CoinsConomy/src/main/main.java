package main;

import commands.AddCoinsCommand;
import commands.CoinsCommand;
import data.MySQL;
import listeners.BlockListener;
import listeners.PlayerListener;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;

public class main extends JavaPlugin
{

    public main()
    {
    }

    public void onEnable()
    {
        getConfig().addDefault("db_table_kills", "kills_pvp");
        getConfig().addDefault("db_table_players", "players_pvp");
        getConfig().addDefault("scoreboard", Boolean.valueOf(false));
        getConfig().addDefault("coins_per_kill", Integer.valueOf(1));
        getConfig().options().copyDefaults(true);
        saveConfig();
        table_kills = getConfig().getString("db_table_kills");
        table_players = getConfig().getString("db_table_players");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new BlockListener(), this);
        getCommand("coins").setExecutor(new CoinsCommand(this));
        getCommand("addcoins").setExecutor(new AddCoinsCommand(this));
        try
        {
            sql = new MySQL();
            sql.openConnection();
            sql.queryUpdate("CREATE TABLE IF NOT EXISTS coins (id INT AUTO_INCREMENT PRIMARY KEY, player VARCHAR(16), coins INT(100))");
            sql.queryUpdate((new StringBuilder("CREATE TABLE IF NOT EXISTS ")).append(table_players).append(" (id INT AUTO_INCREMENT PRIMARY KEY, player VARCHAR(16), coins INT(100))").toString());
            sql.queryUpdate((new StringBuilder("CREATE TABLE IF NOT EXISTS")).append(table_kills).append(" (id INT AUTO_INCREMENT PRIMARY KEY, killer VARCHAR(16), player VARCHAR(16), time VARCHAR(19))").toString());
        }
        catch(Exception e)
        {
            System.err.println((new StringBuilder("[Coins] MYSQL SERVICE ERROR: ")).append(e.getMessage()).toString());
            e.printStackTrace();
        }
    }

    public void onDisable()
    {
    }

    public MySQL getMySQL()
    {
        return sql;
    }

    public void addCoins(final String p, final int amount)
    {
        getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {

            public void run()
            {
                int coins_ = getCoins(p);
                int coins = coins_ + amount;
                sql.queryUpdate((new StringBuilder("UPDATE coins SET coins='")).append(coins).append("' WHERE player='").append(p).append("'").toString());
            }

            final main this$0;
            private final String val$p;
            private final int val$amount;

            
            {
                this$0 = main.this;
                p = s;
                amount = i;
                super();
            }
        }
);
        if(Bukkit.getPlayer(p) != null)
        {
            Player p_ = Bukkit.getPlayer(p);
            if(amount < 0)
                p_.sendMessage((new StringBuilder(String.valueOf(Prefix))).append("\2476").append(amount).append(" \247aCoins").toString());
            else
                p_.sendMessage((new StringBuilder(String.valueOf(Prefix))).append("\2476+").append(amount).append(" \247aCoins").toString());
        }
    }

    public int getId(String player)
    {
        Connection conn;
        ResultSet rs;
        PreparedStatement st;
        int id;
        conn = sql.getConnection();
        rs = null;
        st = null;
        id = 0;
        try
        {
            st = conn.prepareStatement((new StringBuilder("SELECT * FROM ")).append(table_players).append(" WHERE player=?").toString());
            st.setString(1, player);
            rs = st.executeQuery();
            rs.last();
            if(rs.getRow() != 0)
            {
                rs.first();
                id = rs.getInt("id");
            }
            break MISSING_BLOCK_LABEL_136;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        sql.closeRessources(rs, st);
        break MISSING_BLOCK_LABEL_146;
        Exception exception;
        exception;
        sql.closeRessources(rs, st);
        throw exception;
        sql.closeRessources(rs, st);
        return id;
    }

    public int getCoins(String player)
    {
        Connection conn;
        ResultSet rs;
        PreparedStatement st;
        int coins;
        conn = sql.getConnection();
        rs = null;
        st = null;
        coins = 0;
        try
        {
            st = conn.prepareStatement("SELECT * FROM coins WHERE player=?");
            st.setString(1, player);
            rs = st.executeQuery();
            rs.last();
            if(rs.getRow() != 0)
            {
                rs.first();
                coins = rs.getInt("coins");
            }
            break MISSING_BLOCK_LABEL_115;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        sql.closeRessources(rs, st);
        break MISSING_BLOCK_LABEL_125;
        Exception exception;
        exception;
        sql.closeRessources(rs, st);
        throw exception;
        sql.closeRessources(rs, st);
        return coins;
    }

    public int getKills(String player)
    {
        Connection conn;
        ResultSet rs;
        PreparedStatement st;
        int kills;
        conn = sql.getConnection();
        rs = null;
        st = null;
        kills = 0;
        try
        {
            st = conn.prepareStatement((new StringBuilder("SELECT * FROM ")).append(table_players).append(" WHERE player=?").toString());
            st.setString(1, player);
            rs = st.executeQuery();
            rs.last();
            if(rs.getRow() != 0)
            {
                rs.first();
                kills = rs.getInt("kills");
            }
            break MISSING_BLOCK_LABEL_136;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        sql.closeRessources(rs, st);
        break MISSING_BLOCK_LABEL_146;
        Exception exception;
        exception;
        sql.closeRessources(rs, st);
        throw exception;
        sql.closeRessources(rs, st);
        return kills;
    }

    public int getDeaths(String player)
    {
        Connection conn;
        ResultSet rs;
        PreparedStatement st;
        int deaths;
        conn = sql.getConnection();
        rs = null;
        st = null;
        deaths = 0;
        try
        {
            st = conn.prepareStatement((new StringBuilder("SELECT * FROM ")).append(table_players).append(" WHERE player=?").toString());
            st.setString(1, player);
            rs = st.executeQuery();
            rs.last();
            if(rs.getRow() != 0)
            {
                rs.first();
                deaths = rs.getInt("deaths");
            }
            break MISSING_BLOCK_LABEL_136;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        sql.closeRessources(rs, st);
        break MISSING_BLOCK_LABEL_146;
        Exception exception;
        exception;
        sql.closeRessources(rs, st);
        throw exception;
        sql.closeRessources(rs, st);
        return deaths;
    }

    public void sendServerMessage(String p, String msg)
        throws IOException
    {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        out.writeUTF("Message");
        out.writeUTF(p);
        out.writeUTF(msg);
        if(Bukkit.getOnlinePlayers()[0] != null)
        {
            Player player = Bukkit.getOnlinePlayers()[0];
            player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
        }
    }

    public void addPvPEncounter(final String p, final String killer)
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        Date time = new Date();
        Date date = new Date();
        final String td = (new StringBuilder(String.valueOf(timeFormat.format(time)))).append(" - ").append(dateFormat.format(date)).toString();
        getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {

            public void run()
            {
                int kills = getKills(killer) + 1;
                int deaths = getDeaths(p) + 1;
                sql.queryUpdate((new StringBuilder("UPDATE ")).append(main.table_players).append(" SET kills='").append(kills).append("' WHERE player='").append(killer).append("'").toString());
                sql.queryUpdate((new StringBuilder("UPDATE ")).append(main.table_players).append(" SET deaths='").append(deaths).append("' WHERE player='").append(p).append("'").toString());
                sql.queryUpdate((new StringBuilder("INSERT INTO ")).append(main.table_kills).append(" (killer, player, time) VALUES ('").append(killer).append("', '").append(p).append("', '").append(td).append("')").toString());
            }

            final main this$0;
            private final String val$killer;
            private final String val$p;
            private final String val$td;

            
            {
                this$0 = main.this;
                killer = s;
                p = s1;
                td = s2;
                super();
            }
        }
);
    }

    public static String table_kills;
    public static String table_players;
    public static String Prefix = "\2477[\2476Coins\2477] ";
    private MySQL sql;


}
