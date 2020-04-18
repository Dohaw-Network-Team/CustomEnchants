package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class FrozenTouchEnchant extends CustomEnchant{

    public FrozenTouchEnchant() {
        super(EnchantmentKeys.frozen_touch, ChatColor.DARK_AQUA, Particle.DRIP_WATER);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){

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

            if(playerHit.getInventory().getArmorContents().length != 0){

                List<ItemStack> armorContents = Arrays.asList(playerHit.getInventory().getArmorContents());
                armorContents.removeAll(Collections.singletonList(null));

                int chance = 0;
                for (ItemStack i : armorContents) {
                    if (i.getItemMeta().hasEnchant(this)) {
                        chance += getChance(i.getItemMeta().getEnchantLevel(this));
                    }
                }

                int i = rnd.nextInt(100);

                if (i < chance) {
                    Bukkit.broadcastMessage("I am here2");
                    /*
                        Here, we get the level of a random armor piece that has the enchantment. We use that level to choose what
                        percentage the player gets slowed
                     */
                    AttributeManager am = new AttributeManager(Main.getInstance(), playerDamager);
                    int randomArmorNumber = rnd.nextInt(armorContents.size());
                    int level = armorContents.get(randomArmorNumber).getItemMeta().getEnchantLevel(this);

                    double lengthOfEnchant = config.getDouble(configPath + ".LengthPerLevel." + level);
                    double percentageDecrease = config.getDouble(configPath + ".DecreasedPercentagePerLevel." + level);

                    double currentSpeedValue = am.getSpeedValue();
                    double newSpeedValue = currentSpeedValue - (currentSpeedValue * percentageDecrease);

                    Collection<AttributeModifier> playerSpeedModifiers = playerDamager.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getModifiers();

                    am.removeSpeedModifiers();
                    am.setSpeedValue(newSpeedValue);

                    new BukkitRunnable(){
                        int counter = 0;
                        @Override
                        public void run() {
                            if(counter == lengthOfEnchant){
                                am.restoreSpeedModifiers(playerSpeedModifiers);
                                this.cancel();
                            }else{
                                counter++;
                            }
                        }
                    };
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
