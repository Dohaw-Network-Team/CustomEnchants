package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MuscleSapEnchant extends CustomEnchant {

    public MuscleSapEnchant(){
        super(EnchantmentKeys.muscle_sap, ChatColor.GREEN, Particle.BARRIER);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){

        Player playerHit = null;

        if(e.getEntity() instanceof Player){

            Entity damager = e.getDamager();

            if (damager instanceof Arrow || damager instanceof Snowball || damager instanceof Fireball) {
                Projectile en = (Projectile) e.getDamager();
                if(!(en.getShooter() instanceof Player)){
                    return;
                }else{
                    damager = (Player) en.getShooter();
                }
            }else{
                if(!(e.getDamager() instanceof Player)){
                    return;
                }else{
                    playerHit = (Player) e.getDamager();
                }
            }

            if(playerHit.getInventory().getLeggings().hasItemMeta()){
                ItemStack leggings = playerHit.getInventory().getLeggings();
                if(leggings.getItemMeta().hasEnchant(this)){

                    int lvl = leggings.getItemMeta().getEnchantLevel(this);
                    double cooldown = config.getDouble(configPath + ".CooldownPerLevel." + lvl);
                    double percentage = config.getDouble(configPath + ".DecreasePercentagePerLevel." + lvl);

                    int i = getRandInt();
                    double chance = getChance(lvl);

                    if(i < chance){

                        AttributeManager am = new AttributeManager(Main.getInstance(), playerHit);

                    }
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_LEGS;
    }
}
