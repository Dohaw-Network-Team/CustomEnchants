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

        ItemStack playerBootSlot = p.getInventory().getBoots();
        ItemStack playerItem = e.getCurrentItem();
        AttributeManager am = new AttributeManager(Main.getInstance(), p);
        if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
            if (playerItem.getType().name().equals("AIR") || playerItem.getType().name().endsWith("_BOOTS")){
                if (playerBootSlot != null){
                    if (Objects.requireNonNull(playerBootSlot.getItemMeta()).hasEnchant(this)) {
                        am.reApplyClassSpeedModifier();
                    }
                }else{
                        am.setSpeedModifiers(Objects.requireNonNull(playerBootSlot.getItemMeta()).getEnchantLevel(this));
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
        return EnchantmentTarget.ARMOR_FEET;
    }
}
