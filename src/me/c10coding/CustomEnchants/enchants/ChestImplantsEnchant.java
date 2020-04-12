package me.c10coding.CustomEnchants.enchants;


import me.c10coding.CustomEnchants.runnables.ChestImplantsRunnable;
import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class ChestImplantsEnchant extends CustomEnchant{

    final long timeBetweenFeeding = 200L;

    public ChestImplantsEnchant(){
        super(EnchantmentKeys.chest_implants, ChatColor.DARK_GREEN, Particle.VILLAGER_HAPPY);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        new ChestImplantsRunnable().runTaskTimer(plugin, 0L, timeBetweenFeeding);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_TORSO;
    }
}
