package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import sun.security.ssl.Debug;

import java.util.Collection;

public class BerserkerEnchant extends CustomEnchant {

    public BerserkerEnchant() {
        super(EnchantmentKeys.berserker, ChatColor.DARK_RED, Particle.FLAME);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void hasHealed(EntityRegainHealthEvent e){
        if (!(e.getEntity() instanceof Player)){
            return;
        }
        Player player = (Player) e.getEntity();
        double newHealth = player.getHealth() + e.getAmount();
        if(player.hasMetadata("berserk")){
            int enchLvl = (int) player.getMetadata("berserk").get(0).value();
            int healthNeeded = config.getInt(configPath + ".HealthActivationPerLevel." + enchLvl);
            if (healthNeeded < newHealth){
                AttributeManager am = new AttributeManager(Main.getInstance(), player);
                am.reApplyClassStrengthModifier();
                player.removeMetadata("berserk", this.plugin);
                Bukkit.getConsoleSender().sendMessage(String.valueOf(am.getNewHitDmg(1)));
            }
        }
    }
    @EventHandler
    public void onPlayerHit(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();

        double newHealth = player.getHealth() - e.getDamage();
        if (newHealth <= 0) { //OnDeath
            AttributeManager am = new AttributeManager(Main.getInstance(), player);
            am.reApplyClassStrengthModifier();
            return;
        }
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        if (handItem == null && offhandItem == null) {
            return;
        }
        if (handItem.hasItemMeta() || offhandItem.hasItemMeta()) {
            ItemMeta handMeta = handItem.getItemMeta();
            ItemMeta offhandMeta = offhandItem.getItemMeta();
            boolean offhandHasEnch = false;
            boolean handHasEnch = false;
            if (!(offhandMeta == null)){
                if(offhandMeta.hasEnchant(this)){
                    offhandHasEnch = true;
                }
            }
            if (!(handMeta == null)){
                if(handMeta.hasEnchant(this)){
                    handHasEnch = true;
                }
            }
            if (handHasEnch || offhandHasEnch) {
                int enchlvl = 1;
                if (handHasEnch && offhandHasEnch) {
                    int handLvl = handMeta.getEnchantLevel(this);
                    int offHanLvl = handItem.getEnchantmentLevel(this);
                    if (handLvl >= offHanLvl) {
                        enchlvl = handLvl;
                    } else {
                        enchlvl = offHanLvl;
                    }
                } else {
                    if(handHasEnch){
                        enchlvl = handMeta.getEnchantLevel(this);
                    }else{
                        enchlvl = offhandMeta.getEnchantLevel(this);
                    }
                }
                int healthNeeded = config.getInt(configPath + ".HealthActivationPerLevel." + enchlvl);
                if (newHealth <= healthNeeded) {
                    AttributeManager am = new AttributeManager(Main.getInstance(), player);
                    int StrengthIncreasePerLevel = config.getInt(configPath+".StrengthIncreasePerLevel."+ enchlvl);
                    am.setStrengthModifier(StrengthIncreasePerLevel);
                    FixedMetadataValue dataValue = new FixedMetadataValue(this.plugin,enchlvl);
                    player.setMetadata("berserk",dataValue);
                    Bukkit.getConsoleSender().sendMessage(String.valueOf(am.getNewHitDmg(1)));
                }
                return;
            }
        } else {
            return;
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
