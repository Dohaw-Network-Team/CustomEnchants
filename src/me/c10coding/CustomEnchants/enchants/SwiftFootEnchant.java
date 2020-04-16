package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.management.Attribute;

public class SwiftFootEnchant extends CustomEnchant{

    public SwiftFootEnchant(){
        super(EnchantmentKeys.swift_foot, ChatColor.GOLD, Particle.FLASH);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();
        Inventory inv = p.getInventory();
        InventoryAction action = e.getAction();


        if(e.getClickedInventory() == null) return;

        if(e.getCurrentItem() == null) return;

        if(e.getSlotType().equals(InventoryType.SlotType.ARMOR) && e.getCurrentItem().getType().name().toLowerCase().contains("boots")){
           if(action.equals(InventoryAction.PICKUP_ALL) || action.equals(InventoryAction.PLACE_ALL) || action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                if(e.getCurrentItem().getType().name().toLowerCase().contains("boots") || e.getCursor().getType().name().toLowerCase().contains("boots")){
                    ItemStack boots = e.getCurrentItem().getType().name().toLowerCase().contains("boots") ? e.getCurrentItem() : e.getCursor();
                    if(boots.getItemMeta().hasEnchant(this)){
                        AttributeManager am = new AttributeManager(Main.getInstance(), p);
                        if(action.equals(InventoryAction.PICKUP_ALL)){
                            am.reApplyClassSpeedModifier();
                        }else if (action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                            if(p.getInventory().getBoots() == null){
                                am.setSpeedModifiers(boots.getItemMeta().getEnchantLevel(this));
                            }else{
                                am.reApplyClassSpeedModifier();
                            }
                        }else if(action.equals(InventoryAction.PLACE_ALL)){
                            am.setSpeedModifiers(boots.getItemMeta().getEnchantLevel(this));
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
        return EnchantmentTarget.ARMOR_FEET;
    }
}
