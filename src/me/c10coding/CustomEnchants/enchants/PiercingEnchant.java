package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.HashMap;

public class PiercingEnchant extends CustomEnchant{

    HashMap<Arrow, Integer> arrowMap = new HashMap<Arrow, Integer>();

    public PiercingEnchant(){
        super(EnchantmentKeys.piercing, ChatColor.DARK_BLUE, Particle.CRIT_MAGIC);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onArrowHit(EntityDamageByEntityEvent e){

        if(!e.isCancelled()){
            if(e.getDamager() instanceof Arrow){
                Arrow arrow = (Arrow) e.getDamager();
                if(arrow.getShooter() instanceof Player){
                    Player p = (Player) arrow.getShooter();
                    if(arrowMap.containsKey(arrow)){

                        int i = rnd.nextInt(100);
                        int lvl = arrowMap.get(arrow);
                        double chance = getChance(lvl);

                        if(i < chance){
                            double dmg = e.getDamage();
                            AttributeManager am = new AttributeManager(Main.getInstance(), p);
                            double newDmg = am.getNewRangedDmg(dmg) * 1.5;
                            e.setDamage(newDmg);
                            p.getWorld().spawnParticle(enchantParticle, p.getLocation() ,10);
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }else{
            return;
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
        return EnchantmentTarget.BOW;
    }
}
