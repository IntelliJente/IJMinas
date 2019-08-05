package pt.intellijente.mina;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Random;

public class Fortune {

    public static int getFortune(Player p) {
        if (p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            int level = p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
            Random random = new Random();
            int Item = random.nextInt(4) + 1;
            if (level >= 30) {
                int max = level / 6;
                int least = level / 7;
                Item = random.nextInt(max) + least;
            }
            return Item;
        }
        return 1;
    }

}
