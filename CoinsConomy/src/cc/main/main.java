package cc.main;

import cc.commands.AddCoinsCommand;
import cc.commands.CoinsCommand;
import cc.data.MySQL;
import cc.listeners.BlockListener;
import cc.listeners.PlayerListener;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin
{
  public static String table_kills;
  public static String table_players;
  public static String Prefix = "§7[§6Coins§7] ";
  private MySQL sql;

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
    getServer().getMessenger()
      .registerOutgoingPluginChannel(this, "BungeeCord");
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new PlayerListener(this), this);
    pm.registerEvents(new BlockListener(), this);
    getCommand("coins").setExecutor(new CoinsCommand(this));
    getCommand("addcoins").setExecutor(new AddCoinsCommand(this));
    try {
      this.sql = new MySQL();
      this.sql.openConnection();
      this.sql.queryUpdate("CREATE TABLE IF NOT EXISTS coins (id INT AUTO_INCREMENT PRIMARY KEY, player VARCHAR(16), coins INT(100))");
      this.sql.queryUpdate("CREATE TABLE IF NOT EXISTS " + 
        table_players + 
        " (id INT AUTO_INCREMENT PRIMARY KEY, player VARCHAR(16), coins INT(100))");
      this.sql.queryUpdate("CREATE TABLE IF NOT EXISTS" + 
        table_kills + 
        " (id INT AUTO_INCREMENT PRIMARY KEY, killer VARCHAR(16), player VARCHAR(16), time VARCHAR(19))");
    } catch (Exception e) {
      System.err
        .println("[Coins] MYSQL SERVICE ERROR: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void onDisable()
  {
  }

  public MySQL getMySQL() {
    return this.sql;
  }

  public void addCoins(final String p, final int amount) {
    getServer().getScheduler()
      .runTaskAsynchronously(this, new Runnable()
    {
      public void run()
      {
        int coins_ = main.this.getCoins(p);
        int coins = coins_ + amount;
        main.this.sql.queryUpdate("UPDATE coins SET coins='" + coins + 
          "' WHERE player='" + p + "'");
      }
    });
    if (Bukkit.getPlayer(p) != null) {
      Player p_ = Bukkit.getPlayer(p);
      if (amount < 0)
        p_.sendMessage(Prefix + "§6" + amount + " §aCoins");
      else
        p_.sendMessage(Prefix + "§6+" + amount + " §aCoins");
    }
  }

  public int getId(String player)
  {
    Connection conn = this.sql.getConnection();
    ResultSet rs = null;
    PreparedStatement st = null;
    int id = 0;
    try {
      st = conn.prepareStatement("SELECT * FROM " + table_players + " WHERE player=?");
      st.setString(1, player);
      rs = st.executeQuery();
      rs.last();
      if (rs.getRow() != 0) {
        rs.first();
        id = rs.getInt("id");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      this.sql.closeRessources(rs, st);
    }
    return id;
  }

  public int getCoins(String player) {
    Connection conn = this.sql.getConnection();
    ResultSet rs = null;
    PreparedStatement st = null;
    int coins = 0;
    try {
      st = conn.prepareStatement("SELECT * FROM coins WHERE player=?");
      st.setString(1, player);
      rs = st.executeQuery();
      rs.last();
      if (rs.getRow() != 0) {
        rs.first();
        coins = rs.getInt("coins");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      this.sql.closeRessources(rs, st);
    }
    return coins;
  }

  public int getKills(String player) {
    Connection conn = this.sql.getConnection();
    ResultSet rs = null;
    PreparedStatement st = null;
    int kills = 0;
    try {
      st = conn.prepareStatement("SELECT * FROM " + table_players + 
        " WHERE player=?");
      st.setString(1, player);
      rs = st.executeQuery();
      rs.last();
      if (rs.getRow() != 0) {
        rs.first();
        kills = rs.getInt("kills");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      this.sql.closeRessources(rs, st);
    }
    return kills;
  }

  public int getDeaths(String player) {
    Connection conn = this.sql.getConnection();
    ResultSet rs = null;
    PreparedStatement st = null;
    int deaths = 0;
    try {
      st = conn.prepareStatement("SELECT * FROM " + table_players + 
        " WHERE player=?");
      st.setString(1, player);
      rs = st.executeQuery();
      rs.last();
      if (rs.getRow() != 0) {
        rs.first();
        deaths = rs.getInt("deaths");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      this.sql.closeRessources(rs, st);
    }
    return deaths;
  }

  public void sendServerMessage(String p, String msg) throws IOException {
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(b);
    out.writeUTF("Message");
    out.writeUTF(p);
    out.writeUTF(msg);
    if (Bukkit.getOnlinePlayers()[0] != null) {
      Player player = Bukkit.getOnlinePlayers()[0];
      player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
    }
  }

  public void addPvPEncounter(final String p, final String killer) {
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", 
      Locale.GERMANY);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", 
      Locale.GERMANY);
    Date time = new Date();
    Date date = new Date();
    final String td = timeFormat.format(time) + " - " + 
      dateFormat.format(date);
    getServer().getScheduler()
      .runTaskAsynchronously(this, new Runnable()
    {
      public void run()
      {
        int kills = main.this.getKills(killer) + 1;
        int deaths = main.this.getDeaths(p) + 1;
        main.this.sql.queryUpdate("UPDATE " + main.table_players + " SET kills='" + kills + 
          "' WHERE player='" + killer + "'");
        main.this.sql.queryUpdate("UPDATE " + main.table_players + " SET deaths='" + deaths + 
          "' WHERE player='" + p + "'");
        main.this.sql.queryUpdate("INSERT INTO " + main.table_kills + 
          " (killer, player, time) VALUES ('" + killer + 
          "', '" + p + "', '" + td + "')");
      }
    });
  }
}