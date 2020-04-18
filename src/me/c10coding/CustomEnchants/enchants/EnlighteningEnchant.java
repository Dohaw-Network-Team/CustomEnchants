package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EnlighteningEnchant extends CustomEnchant{

    public EnlighteningEnchant(){
        super(EnchantmentKeys.enlightening, ChatColor.WHITE, Particle.END_ROD);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onPlayerTakeHit(EntityDamageByEntityEvent e){

        Entity enHit = e.getEntity();
        Entity enDamager = e.getDamager();
        Player playerHit, playerDamager;

        if(enHit instanceof Player) {
            playerHit = (Player) e.getEntity();
            if (enDamager instanceof Arrow || enDamager instanceof Snowball || enDamager instanceof Fireball) {
                Projectile en = (Projectile) enDamager;
                if (!(en.getShooter() instanceof Player)) {
                    return;
                } else {
                    playerDamager = (Player) en.getShooter();
                }
            } else {
                if (!(enDamager instanceof Player)) {
                    return;
                } else {
                    playerDamager = (Player) e.getDamager();
                }
            }

            if (playerHit.getInventory().getArmorContents().length == 0) {
                return;
            } else {
                ItemStack[] armorContents = playerHit.getInventory().getArmorContents();
                //Pieces of armor they have that aren't null
                int numArmor = 0;
                int chance = 0;
                for (ItemStack i : armorContents) {
                    if (i != null) {
                        if (i.getItemMeta().hasEnchant(this)) {
                            numArmor++;
                            chance += getChance(i.getItemMeta().getEnchantLevel(this));
                        }
                    }
                }
                int i = rnd.nextInt(100);

                if (i < chance) {
                    //Cancel the damage
                    playerHit.getWorld().playSound(playerHit.getLocation(), Sound.ITEM_SHIELD_BLOCK, 10, 10);
                    playerHit.getWorld().spawnParticle(enchantParticle, playerHit.getLocation(), 50);
                    e.setCancelled(true);
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
        return EnchantmentTarget.ARMOR;
    }
}
