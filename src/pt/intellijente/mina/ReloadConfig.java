package pt.intellijente.mina;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadConfig implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ijminas")) {
            if (!(sender instanceof Player)) {
                Main.main.reloadConfig();
                sender.sendMessage("§aConfiguration file reloaded");
                return true;
            }
            Player player = (Player) sender;
            if (player.hasPermission("ijminas.admin")) {
                if (args.length == 0) {
                    player.sendMessage("§7Usage: /ijminas reload");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    player.sendMessage("§aConfiguration file reloaded");
                    Main.main.reloadConfig();
                    return true;
                }
            } else {
                player.sendMessage("§cSem permissão.");
                return true;
            }
        }
        return false;
    }
}
