package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.runnables.MuscleSapCooldown;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuscleSapEnchant extends CustomEnchant {

    public HashMap<UUID, HashMap<String, Double>> playerAttributesMap = new HashMap<UUID, HashMap<String, Double>>();
    final int MUSCLE_SAP_LENGTH = 5;

    public MuscleSapEnchant(){
        super(EnchantmentKeys.muscle_sap, ChatColor.GREEN, Particle.BARRIER);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        new MuscleSapCooldown(plugin).runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){

        Entity enHit = e.getEntity();
        Entity enDamager = e.getDamager() instanceof Player ? (Player) e.getDamager() : e.getDamager();

        if(enHit instanceof Player){

            if (enDamager instanceof Arrow || enDamager instanceof Snowball || enDamager instanceof Fireball) {
                Projectile en = (Projectile) enDamager;
                if(!(en.getShooter() instanceof Player)){
                    return;
                }else{
                    enDamager = (Player) en.getShooter();
                }
            }else{
                if(!(enDamager instanceof Player)){
                    return;
                }else{
                    enDamager = (Player) e.getDamager();
                }
            }

            Player playerHit = (Player) enHit;
            Player playerDamager = (Player) enDamager;

            if(!playerHit.hasMetadata("Muscle_Sap_Cooldown")) {
                if (playerHit.getInventory().getLeggings() != null && playerHit.getInventory().getLeggings().hasItemMeta()) {
                    ItemStack leggings = playerHit.getInventory().getLeggings();
                    if (leggings.getItemMeta().hasEnchant(this)) {

                        int lvl = leggings.getItemMeta().getEnchantLevel(this);
                        double cooldown = config.getDouble(configPath + ".CooldownPerLevel." + lvl);

                        AttributeManager am = new AttributeManager(Main.getInstance(), playerDamager);
                        HashMap<String, Double> playerAttributes = am.getPlayerAttributes();
                        final HashMap<String, Double> temp = (HashMap<String, Double>) playerAttributes.clone();

                        playerAttributesMap.put(playerDamager.getUniqueId(), playerAttributes);
                        int lvlDecrease = lvl;

                        double newSpellPower = 1, newStrength = 1, newRangeDmg = 1, newToughness = 1;

                        if (temp.get("SpellPower") - lvlDecrease >= 1) {
                            newSpellPower = playerAttributes.get("SpellPower") - lvlDecrease;
                        }

                        if (temp.get("Strength") - lvlDecrease >= 1) {
                            newStrength = playerAttributes.get("Strength") - lvlDecrease;
                        }

                        if (temp.get("RangedDamage") - lvlDecrease >= 1) {
                            newRangeDmg = playerAttributes.get("RangedDamage") - lvlDecrease;
                        }

                        if (temp.get("Toughness") - lvlDecrease >= 1) {
                            newToughness = playerAttributes.get("Toughness") - lvlDecrease;
                        }

                        playerAttributes.replace("SpellPower", newSpellPower);
                        playerAttributes.replace("Strength", newStrength);
                        playerAttributes.replace("RangedDamage", newRangeDmg);
                        playerAttributes.replace("Toughness", newToughness);

                        am.setNewAttributes(playerAttributes, playerDamager);

                        playerHit.setMetadata("Muscle_Sap_Cooldown", new FixedMetadataValue(plugin, cooldown));

                        playerDamager.getWorld().spawnParticle(enchantParticle, playerDamager.getLocation(), 50);

                        playerHit.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&lMuscle Sap Applied!&b&l]")));

                        new BukkitRunnable() {
                            int counter = 1;

                            @Override
                            public void run() {
                                if (counter == MUSCLE_SAP_LENGTH) {
                                    am.restoreAttributes(temp, playerDamager);
                                    playerAttributesMap.remove(playerDamager.getUniqueId());
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
