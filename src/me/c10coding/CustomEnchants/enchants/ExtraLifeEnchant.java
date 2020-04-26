package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class ExtraLifeEnchant extends CustomEnchant {
    public ExtraLifeEnchant() {
        super(EnchantmentKeys.extra_life,ChatColor.GREEN, null);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            double health = (player.getHealth() - e.getDamage());
            ItemStack legs = player.getInventory().getLeggings();
            if (legs != null) {
                if (legs.hasItemMeta() && legs.getItemMeta().hasEnchant(this)) {
                    if (health <= config.getDouble(configPath + ".Health_Activation") && !player.hasMetadata("extraLifeCooldown")) {
                        AttributeManager am = new AttributeManager(Main.getInstance(), player);
                        am.setSpeedModifier(config.getInt(configPath + ".SpeedLevel." + legs.getItemMeta().getEnchantLevel(this)),"EL");
                        double max_health = player.getMaxHealth();
                        double healAmount = config.getInt(configPath+".RegenLevel."+legs.getItemMeta().getEnchantLevel(this));
                        if (healAmount > max_health-health){
                            healAmount = max_health-health;
                        }
                        health = healAmount+health;
                        player.setHealth(health);
                        MetadataValue data = new FixedMetadataValue(this.plugin, config.getInt(configPath+".Cooldown."+legs.getItemMeta().getEnchantLevel(this)));
                        player.setMetadata("extraLifeCooldown",data);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                int cooldown = (int) player.getMetadata("extraLifeCooldown").get(0).value();
                                cooldown = cooldown - 1;
                                if (cooldown == 0) {
                                    player.removeMetadata("extraLifeCooldown", plugin);
                                    this.cancel();
                                }
                                MetadataValue data = new FixedMetadataValue(plugin, cooldown);
                                player.setMetadata("extraLifeCooldown", data);
                            }
                        }.runTaskTimer(plugin, 0l, 20l);
                    }
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_LEGS;
    }
}
