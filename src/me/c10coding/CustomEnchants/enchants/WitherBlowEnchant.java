package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WitherBlowEnchant extends CustomEnchant{

    private final double length = config.getDouble(configPath + ".WitherLength");

    public WitherBlowEnchant(){
        super(EnchantmentKeys.wither_blow, ChatColor.GRAY, Particle.SPELL_WITCH);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e){

        if(!e.isCancelled()) {
            if (e.getEntity() instanceof LivingEntity && e.getDamager() instanceof Player) {
                LivingEntity le = (LivingEntity) e.getEntity();
                Player damager = (Player) e.getDamager();

                if (damager.getItemInHand().getType().name().toLowerCase().contains("sword") || damager.getItemInHand().getType().name().toLowerCase().contains("axe")) {
                    ItemStack weapon = damager.getItemInHand();
                    if (weapon.hasItemMeta() && weapon.getItemMeta().hasEnchant(this)) {
                        int lvl = weapon.getItemMeta().getEnchantLevel(this);
                        double chance = getChance(lvl);

                        int i = rnd.nextInt(100);
                        if (i < chance) {
                            le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int) (length * 20), 0));
                            le.getWorld().spawnParticle(enchantParticle, le.getLocation(), 50);
                        }
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
        return EnchantmentTarget.WEAPON;
    }
}
