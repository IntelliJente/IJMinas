package pt.intellijente.mina;

import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class Event implements Listener {

    public void sendActionBar(Player p, String text) {
        PacketPlayOutChat packet = new PacketPlayOutChat(
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }



    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material type = block.getType();
        FileConfiguration config = Main.main.getConfig();
        Economy economy = Main.main.getEconomy();
        if (player.getWorld().getName().equalsIgnoreCase(config.getString("World"))) {
            for (String key : config.getConfigurationSection("Ranks").getKeys(false)) {
                for (String blocks : config.getStringList("Ranks." + key + ".blocks")) {
                    String[] config_split = blocks.split(",");
                    String blocks1 = blocks + ",money";
                    if (type == Material.getMaterial(config_split[0])) {
                        if (player.hasPermission(config.getString("Ranks." + key + ".permission"))) {
                            config.set("Ranks." + key + ".blocks", blocks1);
                            economy.depositPlayer(player, Integer.parseInt(config_split[1].replace(" ", "")) * block.getDrops().size() * Fortune.getFortune(player));
                            String name = config.getString("Ranks." + key + ".name").replaceAll("&", "§");
                            if (config.getBoolean("Message.chat.activate")) {
                                config.getStringList("Message.chat.message").forEach(msg -> player.sendMessage(msg.replaceAll("&", "§")
                                        .replace("%group%", name)
                                        .replace("%block%", ItemName.valueOf(type).getName().toString())));
                            }
                            if (config.getBoolean("Message.actionbar.activate")) {
                                sendActionBar(player, config.getString("Message.actionbar.message")
                                        .replaceAll("&", "§")
                                        .replace("%group%", name)
                                        .replace("%block%", ItemName.valueOf(type).getName().toString()));
                            }
                        }
                        block.setType(Material.AIR);
                        if (config.getBoolean("Regeneration.activate")) {
                            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
                                @Override
                                public void run() {
                                    event.setCancelled(true);
                                    block.setType(type);
                                }
                            }, 20 * config.getInt("Regeneration.time"));
                        } else {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
