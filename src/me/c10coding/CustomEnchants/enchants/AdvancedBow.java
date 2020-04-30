package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class AdvancedBow extends CustomEnchant {
    public AdvancedBow() {
        super(EnchantmentKeys.advanced_Bow, ChatColor.DARK_RED, null);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void OnDraw(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getType() == Material.BOW) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().hasEnchant(this)) {
                        e.setCancelled(true);
                        if (!p.hasMetadata("AdvancedBowCooldown")) {
                            Arrow a = p.launchProjectile(Arrow.class);
                            a.setMetadata("AdvancedArrow", new FixedMetadataValue(plugin, 1));
                            a.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                            MetadataValue data = new FixedMetadataValue(this.plugin, ((Double.valueOf(config.getInt(configPath + ".Cooldown." + item.getItemMeta().getEnchantLevel(this))) / 20)));
                            Bukkit.getConsoleSender().sendMessage("[Init] " + data.value());
                            p.setMetadata("AdvancedBowCooldown", data);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    double cooldown = (double) p.getMetadata("AdvancedBowCooldown").get(0).value();
                                    cooldown = cooldown - 0.1;
                                    Bukkit.getConsoleSender().sendMessage("[Down One]" + cooldown);
                                    if (cooldown <= 0.0) {
                                        Bukkit.getConsoleSender().sendMessage("[Removed] AB");
                                        p.removeMetadata("AdvancedBowCooldown", plugin);
                                        this.cancel();
                                    } else {
                                        MetadataValue data = new FixedMetadataValue(plugin, cooldown);
                                        p.setMetadata("AdvancedBowCooldown", data);
                                    }
                                }
                            }.runTaskTimer(plugin, 0l, 2l);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    void OnHit(EntityDamageByEntityEvent e) {
        Entity projectile = e.getDamager();
        Entity hitEntity = e.getEntity();
        if (projectile instanceof Projectile) {
            if (projectile.hasMetadata("AdvancedArrow")) {
                double damage = config.getDouble(configPath + ".Damage." + "1");
                e.setDamage(damage);
            }
        }

    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.BOW;
    }
}
