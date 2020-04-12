package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class CripplingShotEnchant extends CustomEnchant{

    Map<Arrow, Integer> arrowMap = new HashMap<Arrow, Integer>();
    final double slownessLength = config.getDouble(configPath + ".SlownessLength") * 20;

    public CripplingShotEnchant(){
        super(EnchantmentKeys.crippling_shot, ChatColor.YELLOW, Particle.SPELL);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e){
        Entity en = e.getEntity();

        if(e.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getDamager();
            if (arrow.getShooter() instanceof Player) {
                if(en instanceof LivingEntity){
                    LivingEntity le = (LivingEntity) en;
                    for (Map.Entry<Arrow, Integer> a : arrowMap.entrySet()) {
                        if (a.getKey().equals(arrow)) {
                            int lvl = a.getValue();
                            Bukkit.broadcastMessage(configPath + ".slownessLength");
                            if(lvl == 1)
                                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) slownessLength, 0));
                            else
                                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) slownessLength, 1));
                            le.getWorld().spawnParticle(enchantParticle, le.getLocation(), 10);
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent e){
        if(e.getEntity() instanceof Player){
            if(e.getBow().hasItemMeta()){
                if(e.getBow().getItemMeta().hasEnchant(this)){
                    if(e.getProjectile() instanceof Arrow){
                        arrowMap.put((Arrow) e.getProjectile(), e.getBow().getItemMeta().getEnchantLevel(this));
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
        return EnchantmentTarget.BOW;
    }
}
