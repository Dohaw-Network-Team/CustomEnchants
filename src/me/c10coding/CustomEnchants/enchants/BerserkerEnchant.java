package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;

public class BerserkerEnchant extends CustomEnchant{

    public BerserkerEnchant(){
        super(EnchantmentKeys.berserker, ChatColor.DARK_RED, Particle.FLAME);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){

        Entity enHit = e.getEntity();
        Entity enDamager = e.getDamager();
        Player playerHit = null, playerDamager = null;

        /*
        Gets the player even if they're hit with a projectile
        Projectiles are instance of themselves. You must get the projectile the player is hit with and then get the shooter.
         */
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
        }

        if(playerHit.getItemInHand() != null){
            ItemStack itemInHand = playerHit.getItemInHand();
            if(itemInHand.hasItemMeta()){
                ItemMeta im = itemInHand.getItemMeta();
                if(im.hasEnchant(this)){

                    int lvl = im.getEnchantLevel(this);
                    int healthNeeded = config.getInt(configPath + ".HealthActivationPerLevel." + lvl);
                    AttributeManager am = new AttributeManager(Main.getInstance(), playerHit);
                    Collection<AttributeModifier> strengthModifiers = playerHit.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getModifiers();

                    if(healthNeeded >= playerHit.getHealth()){
                        if(itemInHand.getType().name().toLowerCase().contains("sword") || itemInHand.getType().name().toLowerCase().contains("axe")){
                            am.setStrengthModifier(lvl);
                        }else{

                        }

                    }

                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }
}
