package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public class SwiftFootEnchant extends CustomEnchant{

    public SwiftFootEnchant(){
        super(EnchantmentKeys.swift_foot, ChatColor.DARK_GRAY, Particle.FLASH);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();


        if(e.getClickedInventory() == null) return;

        if(e.getCurrentItem() == null) return;

        ItemStack playerItem = e.getCurrentItem();
        AttributeManager am = new AttributeManager(Main.getInstance(), p);
        if (playerBoots != null){
            if (playerBoots.getItemMeta().hasEnchant(this)){
                am.setSpeedModifiers(playerBoots.getItemMeta().getEnchantLevel(this));
            }
        }else if (playerBoots == null || !(playerBoots.getItemMeta().hasEnchant(this))){
            am.reApplyClassSpeedModifier();
        }
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_FEET;
    }
}
