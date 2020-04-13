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

		if(itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasEnchants() && itemInHand != null){
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


	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		ItemStack item = new ItemStack(Material.STONE_SWORD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);

		ItemMeta im = item.getItemMeta();
		ItemMeta bowMeta = bow.getItemMeta();
		ItemMeta bootsMeta = boots.getItemMeta();

		ItemMeta chestplateMeta = chestplate.getItemMeta();

		List<String> lore = new ArrayList<String>();
		List<String> bowLore = new ArrayList<String>();
		List<String> bootsLore = new ArrayList<String>();

		List<String> chestplateLore = new ArrayList<String>();

		lore.add(plugin.lifeSteal.loreColor + Main.getInstance().lifeSteal.getName() + " II");
		lore.add(plugin.deepWounds.loreColor + plugin.deepWounds.getName() + " I");
		lore.add(plugin.witherBlow.loreColor + plugin.witherBlow.getName() + " III");
		lore.add(plugin.headless.loreColor + plugin.headless.getName() + " III");


		bowLore.add(plugin.tempest.loreColor + plugin.tempest.getName() + " I");
		bowLore.add(plugin.cripplingShot.loreColor + plugin.cripplingShot.getName() + " I");
		bowLore.add(plugin.shuffleShot.loreColor + plugin.shuffleShot.getName() + " II");
		bootsLore.add(plugin.swiftFoot.loreColor + plugin.swiftFoot.getName() + " I");

		chestplateLore.add(plugin.chestImplants.loreColor + plugin.chestImplants.getName() + " I");

		im.setLore(lore);
		im.addEnchant(plugin.lifeSteal, 2, false);
		im.addEnchant(plugin.deepWounds, 1, false);
		im.addEnchant(plugin.headless, 3, false);
		im.addEnchant(plugin.witherBlow, 3, false);

		bowMeta.setLore(bowLore);
		bowMeta.addEnchant(plugin.tempest, 1, false);
		bowMeta.addEnchant(plugin.cripplingShot, 1, false);
		bowMeta.addEnchant(plugin.shuffleShot, 2, false);

		bootsMeta.setLore(bootsLore);
		bootsMeta.addEnchant(plugin.swiftFoot, 1, false);

		chestplateMeta.setLore(chestplateLore);
		chestplateMeta.addEnchant(plugin.chestImplants, 1, false);

		item.setItemMeta(im);
		bow.setItemMeta(bowMeta);
		boots.setItemMeta(bootsMeta);
		chestplate.setItemMeta(chestplateMeta);

		e.getPlayer().getInventory().addItem(item);
		//e.getPlayer().getInventory().addItem(bow);
		//e.getPlayer().getInventory().addItem(boots);
		//e.getPlayer().getInventory().addItem(chestplate);
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
