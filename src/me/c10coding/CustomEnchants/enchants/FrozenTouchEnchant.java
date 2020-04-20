package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.Chat;
import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
                int chance = 0;
                List<ItemStack> armorWithEnchant = new ArrayList<ItemStack>();
                for(ItemStack i : playerHit.getInventory().getArmorContents()){
                    if(i != null){
                        if (i.getItemMeta().hasEnchant(this)) {
                            chance += getChance(i.getItemMeta().getEnchantLevel(this));
                            armorWithEnchant.add(i);
                        }
                    }
                }

                int i = rnd.nextInt(100);

                if (i < chance) {
                    /*
                        Here, we get the level of a random armor piece that has the enchantment. We use that level to choose what
                        percentage the player gets slowed
                     */
                    AttributeManager am = new AttributeManager(Main.getInstance(), playerDamager);
                    int randomArmorNumber = rnd.nextInt(armorWithEnchant.size());
                    int level = armorWithEnchant.get(randomArmorNumber).getItemMeta().getEnchantLevel(this);

                    double lengthOfEnchant = config.getDouble(configPath + ".LengthPerLevel." + level);
                    double percentageDecrease = config.getDouble(configPath + ".DecreasedPercentagePerLevel." + level);

                    Collection<AttributeModifier> playerSpeedModifiers = playerDamager.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getModifiers();
                    am.setSpeedModifier(level, "FT");
                    playerDamager.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&lYou have been slowed down! &f&l(Frozen Touch)&b&l]")));


                    new BukkitRunnable(){
                        int counter = 0;
                        @Override
                        public void run() {
                            if(counter == lengthOfEnchant){
                                am.removeSpeedModifiers();
                                am.restoreSpeedModifiers(playerSpeedModifiers);
                                this.cancel();
                            }else{
                                playerDamager.getWorld().spawnParticle(enchantParticle, playerDamager.getLocation(), 10);
                                counter++;
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 20L);
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
