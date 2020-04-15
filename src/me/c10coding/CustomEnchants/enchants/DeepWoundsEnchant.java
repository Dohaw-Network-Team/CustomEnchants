package me.c10coding.CustomEnchants.enchants;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;

public class DeepWoundsEnchant extends CustomEnchant{
	
	private double damagePercentage = config.getDouble(configPath + ".PercentageDamageToBleeding");
	private double lengthBleeding = config.getDouble(configPath + ".LengthBleeding");
	
	public DeepWoundsEnchant() {
		super(EnchantmentKeys.deep_wounds, ChatColor.DARK_RED, Particle.REDSTONE);
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerHit(final EntityDamageByEntityEvent e) {

		if(!e.isCancelled()){
			if(!(e.getDamager() instanceof Player)) return;

			if(!(e.getEntity() instanceof LivingEntity)) return;

			Player p = (Player) e.getDamager();
			final LivingEntity entityHit = (LivingEntity) e.getEntity();
			ItemStack itemInHand = p.getItemInHand();
			ItemMeta im = itemInHand.getItemMeta();

			/*
			 * Bleeding will be <damagePercentage> (variable) of the damage done
			 */
			if(itemInHand != null && itemInHand.hasItemMeta() && im.hasEnchants()){
				if(im.getEnchants().containsKey(Enchantment.getByKey(this.getKey()))) {
					double chance = getChance(im.getEnchantLevel(this));
					int i = rnd.nextInt(100);
					if(i < chance){
						if(entityHit.hasMetadata("hasDeepWounds")){
							return;
						}else{

							entityHit.setMetadata("hasDeepWounds", new FixedMetadataValue(plugin, true));

							new BukkitRunnable() {
								int counter = 0;
								double dmgDone = e.getDamage();
								double dmgBleeding = dmgDone * damagePercentage;
								@Override
								public void run() {
									double entityHitHealth = entityHit.getHealth();
									if(counter < lengthBleeding) {
										entityHit.getWorld().spawnParticle(enchantParticle, entityHit.getLocation(), 10, new Particle.DustOptions(Color.RED, 20));
										try{
											entityHit.setHealth(entityHitHealth - dmgBleeding);
											entityHit.playEffect(EntityEffect.HURT);
										}catch(IllegalArgumentException e){
											entityHit.setHealth(0);
											this.cancel();
										}
									}else {
										entityHit.removeMetadata("hasDeepWounds", plugin);
										this.cancel();
									}
									counter++;
								}
							}.runTaskTimer(plugin, 0L, 20L);
						}
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
		return EnchantmentTarget.WEAPON;
	}

}
