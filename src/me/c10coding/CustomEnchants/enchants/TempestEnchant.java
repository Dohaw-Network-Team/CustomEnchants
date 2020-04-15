package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.*;

public class TempestEnchant extends CustomEnchant {

    final double levelOneStrikes = plugin.getConfig().getDouble(configPath + ".StrikesPerLevel.1");
    final double levelTwoStrikes = plugin.getConfig().getDouble(configPath + ".StrikesPerLevel.2");
    final double percLightningIncrease = plugin.getConfig().getDouble(configPath + ".PercentageLightningDmgIncrease");

    Map<Arrow, Integer> arrowMap = new HashMap<Arrow, Integer>();
    //Entities that are getting struck via Tempest enchant
    ArrayList<Entity> gettingStruck = new ArrayList<Entity>();

    public TempestEnchant() {
        super(EnchantmentKeys.tempest, ChatColor.BLUE, Particle.SPELL);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e) {
        Entity en = e.getEntity();

        if(!e.isCancelled()){
            if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    Iterator<Map.Entry<Arrow, Integer>> itr = arrowMap.entrySet().iterator();
                    while (itr.hasNext()) {
                        Map.Entry me = (Map.Entry) itr.next();
                        if (me.getKey().equals(arrow)) {
                            //This is the arrow that was shot earlier and has the lightning enchant on it
                            gettingStruck.add(en);
                            for (int x = 0; x < (int) me.getValue(); x++) {
                                en.getWorld().strikeLightning(en.getLocation());
                            }
                            //Removes arrow after it's done with its business
                            itr.remove();
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onPlayerLightningStrike(EntityDamageByEntityEvent e){
        Entity en = e.getEntity();
        //Doesn't matter if it's a player or not
        if(gettingStruck.contains(en)){
            if(e.getDamager() instanceof LightningStrike){
                double initDmg = e.getDamage();
                final double finalDmg = (initDmg * percLightningIncrease) + initDmg;
                e.setDamage(finalDmg);
                en.getWorld().spawnParticle(enchantParticle, en.getLocation(), 10);
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
