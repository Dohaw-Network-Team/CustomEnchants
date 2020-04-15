package me.c10coding.CustomEnchants.enchants;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.c10coding.CustomEnchants.Main;
import me.c10coding.CustomEnchants.utils.EnchantmentKeys;

public abstract class CustomEnchant extends Enchantment implements Listener{

	protected String name = "";
	protected EnchantmentKeys ek;
	protected ChatColor loreColor;
	protected Main plugin = Main.getInstance();
	protected Particle enchantParticle;
	protected FileConfiguration config = plugin.getConfig();
	protected String configPath;
	
	protected Random rnd = new Random();
	
	public CustomEnchant(EnchantmentKeys ek, ChatColor lc, Particle p) {
		super(new NamespacedKey(Main.getInstance(), ek.toString()));
		this.ek = ek;
		this.loreColor = lc;
		this.enchantParticle = p;
		setName();
		configPath = "Enchants." + name.replace(" ", "_");
	}
	
	public void setName() {
		String[] nameArr = ek.toString().split("_");
		
		for(int x = 0; x < nameArr.length; x++) {
			if(nameArr.length == 1) {
				name += nameArr[x].substring(0,1).toUpperCase() + nameArr[x].substring(1);
			}else if(x != nameArr.length - 1) {
				name += nameArr[x].substring(0,1).toUpperCase() + nameArr[x].substring(1) + " ";
			}else {
				name += nameArr[x].substring(0,1).toUpperCase() + nameArr[x].substring(1);
			}
		}
		
	}

	public int getRandInt(){
		return rnd.nextInt(100);
	}

	public Double getChance(int lvl) {
		String configName = name.replace(" ", "_");
		return plugin.getConfig().getDouble("Enchants." + configName + ".ChancePerLevel." + lvl);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean isTreasure() {
		return false;
	}

	@Override
	public boolean isCursed() {
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}
	
	@Override
	public int getStartLevel() {
		return 1;
	}

}
