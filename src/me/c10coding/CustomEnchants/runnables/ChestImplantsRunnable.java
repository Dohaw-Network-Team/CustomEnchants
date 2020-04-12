package me.c10coding.CustomEnchants.runnables;

import me.c10coding.CustomEnchants.Main;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestImplantsRunnable extends BukkitRunnable {

    public ChestImplantsRunnable(){ }

    @Override
    public void run() {
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(p.getInventory().getChestplate() != null){
                ItemStack chestplate = p.getInventory().getChestplate();
                if(chestplate.hasItemMeta()){
                    if(chestplate.getItemMeta().hasEnchant(Main.chestImplants)){
                        int newFoodLevel = p.getFoodLevel() + 1;
                        p.setFoodLevel(newFoodLevel);
                        p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation(), 10);
                    }
                }
            }
        }
    }
}
