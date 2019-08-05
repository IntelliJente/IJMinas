package pt.intellijente.mina;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public class Main extends JavaPlugin {

    private static String[] suffix = new String[0];
    public static String format(double value) {
        int index = 0;
        while (value / 1000.0D >= 1.0D) {
            value /= 1000.0D;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return String.format("%s %s", new Object[] { decimalFormat.format(value), suffix[index] });
    }
    private Economy econ;
    public static Main main;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new Event(), this);
        main = this;
        getCommand("ijminas").setExecutor(new ReloadConfig());
        if (!setupEconomy()) {
            this.getLogger().severe("Vault não encontrado!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        setupEconomy();
        saveDefaultConfig();
        suffix = (String[])getConfig().getStringList("Decimal").toArray(new String[0]);
    }

    public static Main getPlugin() {
        return (Main) getPlugin(Main.class);
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }

}
