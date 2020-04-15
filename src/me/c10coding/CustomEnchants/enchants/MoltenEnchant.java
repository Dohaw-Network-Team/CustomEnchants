package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class MoltenEnchant extends CustomEnchant{

    private final double lengthFire = config.getDouble(configPath + ".LengthFire") * 20;

    public MoltenEnchant(){
        super(EnchantmentKeys.molten, ChatColor.RED, Particle.SWEEP_ATTACK);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            Player playerHit = (Player) e.getEntity();
            if(playerHit.getInventory().getArmorContents().length == 0){
                return;
            }else{
                int numArmor = 0;
                int chance = 0;
                ItemStack[] armorContents = playerHit.getInventory().getArmorContents();
                for(ItemStack i : armorContents){
                    if(i != null){
                        if(i.getItemMeta().hasEnchant(this)){
                            numArmor++;
                            chance += getChance(i.getItemMeta().getEnchantLevel(this));
                        }
                    }
                }

                int i = rnd.nextInt(100);

                if(i < chance){
                    Entity damager = e.getDamager();

                    if(damager instanceof Arrow) {
                        Arrow arrow = (Arrow) e.getDamager();
                        damager = (Player) arrow.getShooter();
                    }else if(damager instanceof Snowball) {
                        Snowball sb = (Snowball) e.getDamager();
                        damager = (Player) sb.getShooter();
                    }else if(damager instanceof Fireball) {
                        Fireball fb = (Fireball) e.getDamager();
                        damager = (Player) fb.getShooter();
                    }

                    damager.setFireTicks((int)lengthFire);
                    damager.getWorld().spawnParticle(enchantParticle, damager.getLocation(), 10);
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }
}
