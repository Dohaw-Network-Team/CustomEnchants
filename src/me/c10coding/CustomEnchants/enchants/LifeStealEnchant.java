package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.Main;
import me.c10coding.CustomEnchants.utils.Chat;
import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
				if (itemInHand.getItemMeta().getEnchants().containsKey(Enchantment.getByKey(Main.lifeSteal.getKey()))) {

					double chance = getChance(itemInHand.getItemMeta().getEnchantLevel(this));

					int i = rnd.nextInt(100);

					if (i < chance) {

						double playerHealth = p.getHealth();
						double playerHitHealth = playerHit.getHealth();

						if((playerHealth + 1) > p.getMaxHealth()){
							p.setHealth(p.getMaxHealth());
						}else{
							p.setHealth(playerHealth + 1);
						}

						if((playerHitHealth - 1) < 0){
							playerHit.setHealth(0);
						}else{
							playerHit.setHealth(playerHitHealth - 1);
						}

						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&l+1 HP!&b&l]")));

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
		ItemStack Abow = new ItemStack(Material.BOW, 1);
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
		ItemMeta AbowMeta = Abow.getItemMeta();

		List<String> lore = new ArrayList<>();
		List<String> bowLore = new ArrayList<>();
		List<String> bootsLore = new ArrayList<>();
		List<String> chestplateLore = new ArrayList<>();
		List<String> leggingsLore = new ArrayList<>();
		List<String> helmetLore = new ArrayList<>();
		List<String> AbowLore = new ArrayList<>();

		lore.add(Main.lifeSteal.loreColor + Main.lifeSteal.getName() + " II");
		lore.add(Main.deepWounds.loreColor + Main.deepWounds.getName() + " I");
		lore.add(Main.witherBlow.loreColor + Main.witherBlow.getName() + " III");
		lore.add(Main.headless.loreColor + Main.headless.getName() + " III");
		lore.add(Main.berserker.loreColor + Main.berserker.getName() + " I");

		bowLore.add(Main.tempest.loreColor + Main.tempest.getName() + " I");
		bowLore.add(Main.cripplingShot.loreColor + Main.cripplingShot.getName() + " I");
		bowLore.add(Main.shuffleShot.loreColor + Main.shuffleShot.getName() + " II");
		bowLore.add(Main.witherShot.loreColor + Main.witherShot.getName() + " I");
		bowLore.add(Main.piercing.loreColor + Main.piercing.getName() + " I");

		bootsLore.add(Main.swiftFoot.loreColor + Main.swiftFoot.getName() + " I");
		bootsLore.add(Main.enlightening.loreColor + Main.enlightening.getName() + " I");
		bootsLore.add(Main.molten.loreColor + Main.molten.getName() + " I");

		chestplateLore.add(Main.chestImplants.loreColor + Main.chestImplants.getName() + " I");
		chestplateLore.add(Main.molten.loreColor + Main.molten.getName() + " I");
		chestplateLore.add(Main.frozenTouch.loreColor + Main.frozenTouch.getName() + " I");

		leggingsLore.add(Main.muscleSap.loreColor + Main.muscleSap.getName() + " I");

		helmetLore.add(Main.nightVision.loreColor + Main.nightVision.getName() + " I");

		AbowLore.add(Main.advancedBow.loreColor + Main.advancedBow.getName() + " I");

		im.setLore(lore);
		bowMeta.setLore(bowLore);
		bootsMeta.setLore(bootsLore);
		chestplateMeta.setLore(chestplateLore);
		leggingsMeta.setLore(leggingsLore);
		helmetMeta.setLore(helmetLore);
		AbowMeta.setLore(AbowLore);

		im.addEnchant(Main.lifeSteal, 2, false);
		im.addEnchant(Main.deepWounds, 1, false);
		im.addEnchant(Main.headless, 3, false);
		im.addEnchant(Main.witherBlow, 3, false);
		im.addEnchant(Main.berserker, 1, false);

		bowMeta.addEnchant(Main.tempest, 1, false);
		bowMeta.addEnchant(Main.cripplingShot, 1, false);
		bowMeta.addEnchant(Main.shuffleShot, 2, false);
		bowMeta.addEnchant(Main.witherShot, 1, false);
		bowMeta.addEnchant(Main.piercing, 1, false);

		bootsMeta.addEnchant(Main.swiftFoot, 1, false);
		bootsMeta.addEnchant(Main.enlightening, 1, false);
		bootsMeta.addEnchant(Main.molten, 1, false);

		chestplateMeta.addEnchant(Main.chestImplants, 1, false);
		chestplateMeta.addEnchant(Main.molten, 1, false);
		chestplateMeta.addEnchant(Main.frozenTouch, 1, false);

		leggingsMeta.addEnchant(Main.muscleSap, 1, false);

		helmetMeta.addEnchant(Main.nightVision, 1, false);
		AbowMeta.addEnchant(Main.advancedBow, 1, false);

		item.setItemMeta(im);
		bow.setItemMeta(bowMeta);
		boots.setItemMeta(bootsMeta);
		chestplate.setItemMeta(chestplateMeta);
		leggings.setItemMeta(leggingsMeta);
		helmet.setItemMeta(helmetMeta);
		Abow.setItemMeta(AbowMeta);

		e.getPlayer().getInventory().addItem(item);
		e.getPlayer().getInventory().addItem(bow);
		e.getPlayer().getInventory().addItem(boots);
		e.getPlayer().getInventory().addItem(chestplate);
		e.getPlayer().getInventory().addItem(leggings);
		e.getPlayer().getInventory().addItem(helmet);
		e.getPlayer().getInventory().addItem(Abow);
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
