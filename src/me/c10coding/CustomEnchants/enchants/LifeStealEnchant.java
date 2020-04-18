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


		if(!e.isCancelled()) {
			if (!(e.getDamager() instanceof Player)) return;

			if (!(e.getEntity() instanceof LivingEntity)) return;

			Player p = (Player) e.getDamager();
			LivingEntity playerHit = (LivingEntity) e.getEntity();
			ItemStack itemInHand = p.getItemInHand();

			if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasEnchants() && itemInHand != null) {
				if (itemInHand.getItemMeta().getEnchants().containsKey(Enchantment.getByKey(plugin.lifeSteal.getKey()))) {

					double chance = getChance(itemInHand.getItemMeta().getEnchantLevel(this));

					int i = rnd.nextInt(100);

					if (i < chance) {

						double playerHealth = p.getHealth();
						double playerHitHealth = playerHit.getHealth();

						try {
							p.setHealth(playerHealth + 1);
						} catch (IllegalArgumentException il) {
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

	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		ItemStack item = new ItemStack(Material.STONE_SWORD, 1);
		ItemStack bow = new ItemStack(Material.BOW, 1);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
		ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);

		ItemMeta im = item.getItemMeta();
		ItemMeta bowMeta = bow.getItemMeta();
		ItemMeta bootsMeta = boots.getItemMeta();
		ItemMeta chestplateMeta = chestplate.getItemMeta();
		ItemMeta leggingsMeta = leggings.getItemMeta();
		ItemMeta helmetMeta = helmet.getItemMeta();

		List<String> lore = new ArrayList<String>();
		List<String> bowLore = new ArrayList<String>();
		List<String> bootsLore = new ArrayList<String>();
		List<String> chestplateLore = new ArrayList<String>();
		List<String> leggingsLore = new ArrayList<String>();
		List<String> helmetLore = new ArrayList<>();

		lore.add(plugin.lifeSteal.loreColor + Main.getInstance().lifeSteal.getName() + " II");
		lore.add(plugin.deepWounds.loreColor + plugin.deepWounds.getName() + " I");
		lore.add(plugin.witherBlow.loreColor + plugin.witherBlow.getName() + " III");
		lore.add(plugin.headless.loreColor + plugin.headless.getName() + " III");
		bowLore.add(plugin.tempest.loreColor + plugin.tempest.getName() + " I");
		bowLore.add(plugin.cripplingShot.loreColor + plugin.cripplingShot.getName() + " I");
		bowLore.add(plugin.shuffleShot.loreColor + plugin.shuffleShot.getName() + " II");
		bowLore.add(plugin.witherShot.loreColor + plugin.witherShot.getName() + " I");
		bowLore.add(plugin.piercing.loreColor + plugin.piercing.getName() + " I");
		bootsLore.add(plugin.swiftFoot.loreColor + plugin.swiftFoot.getName() + " I");
		bootsLore.add(plugin.enlightening.loreColor + plugin.enlightening.getName() + " I");
		bootsLore.add(plugin.molten.loreColor + plugin.molten.getName() + " I");
		chestplateLore.add(plugin.chestImplants.loreColor + plugin.chestImplants.getName() + " I");
		chestplateLore.add(plugin.molten.loreColor + plugin.molten.getName() + " I");
		leggingsLore.add(plugin.muscleSap.loreColor + plugin.muscleSap.getName() + " I");
		helmetLore.add(plugin.nightVision.loreColor + plugin.nightVision.getName() + " I");

		im.setLore(lore);
		bowMeta.setLore(bowLore);
		bootsMeta.setLore(bootsLore);
		chestplateMeta.setLore(chestplateLore);
		leggingsMeta.setLore(leggingsLore);
		helmetMeta.setLore(helmetLore);

		im.addEnchant(plugin.lifeSteal, 2, false);
		im.addEnchant(plugin.deepWounds, 1, false);
		im.addEnchant(plugin.headless, 3, false);
		im.addEnchant(plugin.witherBlow, 3, false);
		bowMeta.addEnchant(plugin.tempest, 1, false);
		bowMeta.addEnchant(plugin.cripplingShot, 1, false);
		bowMeta.addEnchant(plugin.shuffleShot, 2, false);
		bowMeta.addEnchant(plugin.witherShot, 1, false);
		bowMeta.addEnchant(plugin.piercing, 1, false);
		bootsMeta.addEnchant(plugin.swiftFoot, 1, false);
		bootsMeta.addEnchant(plugin.enlightening, 1, false);
		bootsMeta.addEnchant(plugin.molten, 1, false);
		chestplateMeta.addEnchant(plugin.chestImplants, 1, false);
		chestplateMeta.addEnchant(plugin.molten, 1, false);
		leggingsMeta.addEnchant(plugin.muscleSap, 1, false);
		helmetMeta.addEnchant(plugin.nightVision, 1, false);

		item.setItemMeta(im);
		bow.setItemMeta(bowMeta);
		boots.setItemMeta(bootsMeta);
		chestplate.setItemMeta(chestplateMeta);
		leggings.setItemMeta(leggingsMeta);
		helmet.setItemMeta(helmetMeta);

		e.getPlayer().getInventory().addItem(item);
		e.getPlayer().getInventory().addItem(bow);
		e.getPlayer().getInventory().addItem(boots);
		e.getPlayer().getInventory().addItem(chestplate);
		e.getPlayer().getInventory().addItem(leggings);
		e.getPlayer().getInventory().addItem(helmet);
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
