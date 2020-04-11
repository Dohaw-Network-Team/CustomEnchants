package me.c10coding.CustomEnchants.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.c10coding.CustomEnchants.Main;
import me.c10coding.CustomEnchants.utils.Chat;
import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LifeStealEnchant extends CustomEnchant{
	
	final double levelOneChance = plugin.getConfig().getDouble("Enchants.Life_Steal.ChancePerLevel.1");
	final double levelTwoChance = plugin.getConfig().getDouble("Enchants.Life_Steal.ChancePerLevel.2");
	final double levelThreeChance = plugin.getConfig().getDouble("Enchants.Life_Steal.ChancePerLevel.3");
	
	public LifeStealEnchant() {
		super(EnchantmentKeys.life_steal, ChatColor.GREEN, Particle.HEART);
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent e) {
		
		if(!(e.getDamager() instanceof Player)) return;
		
		if(!(e.getEntity() instanceof LivingEntity)) return; 

		Player p = (Player) e.getDamager();	
		LivingEntity playerHit = (LivingEntity) e.getEntity();
		ItemStack itemInHand = p.getItemInHand();
		
		if(itemInHand.getItemMeta().getEnchants().containsKey(Enchantment.getByKey(plugin.lifeSteal.getKey()))) {
			
			double chance = getChance(itemInHand.getItemMeta().getEnchantLevel(this));
			
			int i = rnd.nextInt(100);
			
			if(i < chance) {
				
				double playerHealth = p.getHealth();
				double playerHitHealth = playerHit.getHealth();
				
				try {
					p.setHealth(playerHealth + 1);	
				}catch(IllegalArgumentException il) {
					double playerMaxHP = p.getMaxHealth();
					p.setHealth(playerMaxHP);
				}
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&l+1 HP!&b&l]")));
				
				playerHit.setHealth(playerHitHealth - 1);
				p.getWorld().spawnParticle(enchantParticle, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 10);
			}
			
		}

	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		ItemStack item = new ItemStack(Material.STONE_SWORD, 1);
		ItemMeta im = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		lore.add(plugin.lifeSteal.loreColor + Main.getInstance().lifeSteal.getName() + " II");
		lore.add(plugin.deepWounds.loreColor + plugin.deepWounds.getName() + " I");
		lore.add(plugin.headless.loreColor + plugin.headless.getName() + " III");
		
		im.setLore(lore);
		im.addEnchant(plugin.lifeSteal, 2, false);
		im.addEnchant(plugin.deepWounds, 1, false);
		im.addEnchant(plugin.headless, 3, false);

		item.setItemMeta(im);
		e.getPlayer().getInventory().addItem(item);
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
