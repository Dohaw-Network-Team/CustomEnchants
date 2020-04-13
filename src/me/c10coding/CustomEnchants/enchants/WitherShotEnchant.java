package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class WitherShotEnchant extends CustomEnchant {

    private HashMap<Arrow, Integer> arrowMap = new HashMap<Arrow, Integer>();
    private double length = config.getDouble(configPath + "WitherLength");

    public WitherShotEnchant(){
        super(EnchantmentKeys.wither_shot, ChatColor.DARK_AQUA, Particle.SPELL);
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
                            double chance = getChance(lvl);

                            int i = rnd.nextInt(100);
                            if(i < chance){
                                le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int)(length * 20), 0));
                                le.getWorld().spawnParticle(enchantParticle, le.getLocation(), 50);
                            }

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
        return 3;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }
}
