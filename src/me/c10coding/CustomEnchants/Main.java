package me.c10coding.CustomEnchants;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import me.c10coding.CustomEnchants.enchants.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	private static Main instance;
	
	public static CustomEnchant lifeSteal;
	public static CustomEnchant deepWounds;
	public static CustomEnchant headless;
	public static CustomEnchant tempest;
	public static CustomEnchant swiftFoot;

	private ArrayList<Enchantment> enchantList = new ArrayList<Enchantment>();
	
	public void onEnable() {
		
		File[] files = {new File(this.getDataFolder(),"config.yml")};
		
		for(File f : files) {
			if(!f.exists()) {
				this.saveResource(f.getName(), false);
				Bukkit.getConsoleSender().sendMessage("[CustomEnchants] Loading " + f.getName());
			}
		}
		
		instance = this;
	
		lifeSteal = new LifeStealEnchant();
		deepWounds = new DeepWoundsEnchant();
		headless = new HeadlessEnchant();
		tempest = new TempestEnchant();
		swiftFoot = new SwiftFootEnchant();
		
		enchantList.add(lifeSteal);
		enchantList.add(deepWounds);
		enchantList.add(headless);
		enchantList.add(tempest);
		enchantList.add(swiftFoot);

		for(Enchantment e : enchantList) {
			registerEnchantment(e);
		}
		
	}
	
	public void onDisable() {
		try {
		    Field keyField = Enchantment.class.getDeclaredField("byKey");
		 
		    keyField.setAccessible(true);
		    @SuppressWarnings("unchecked")
		    HashMap<NamespacedKey, Enchantment> byKey = (HashMap<NamespacedKey, Enchantment>) keyField.get(null);
		 
		    for(Enchantment e : enchantList) {
		    	 if(byKey.containsKey(e.getKey())) {
			        byKey.remove(e.getKey());
	    	 	 }
		    }

		    Field nameField = Enchantment.class.getDeclaredField("byName");

		    nameField.setAccessible(true);
		    @SuppressWarnings("unchecked")
		    HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) nameField.get(null);

		    for(Enchantment e : enchantList) {
		    	if(byName.containsKey(e.getName())) {
			        byName.remove(e.getName());
			    }
		    }
		    
		} catch (Exception ignored) { }
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static void registerEnchantment(Enchantment ench) {
		boolean registered = true;
		//Using Reflection
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
			Enchantment.registerEnchantment(ench);
		}catch(Exception e) {
			registered = false;
			e.printStackTrace();
		}
		
		if(registered) {
			Bukkit.broadcastMessage(ench.getName());
		}
	}
	
}
