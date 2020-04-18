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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public class SwiftFootEnchant extends CustomEnchant{
    public static boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().equals(Material.AIR);
    }
    public SwiftFootEnchant(){
        super(EnchantmentKeys.swift_foot, ChatColor.DARK_GRAY, Particle.FLASH);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();


        if(e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.PLAYER) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
        if((e.getSlotType() != SlotType.ARMOR) && (e.getSlotType() != SlotType.QUICKBAR) && (e.getSlotType() != SlotType.CONTAINER)) return;
        AttributeManager am = new AttributeManager(Main.getInstance(), p);

        ItemStack cursorItem = e.getCursor();
        ItemStack currentItem = e.getCurrentItem();
        if (e.isShiftClick()){
            if (e.getCurrentItem() != null){
                boolean equip = true;
                if (e.getRawSlot() == 8) equip = false;
                if (e.getCurrentItem().getType().name().endsWith("_BOOTS") && (equip == isAirOrNull(e.getWhoClicked().getInventory().getBoots()))){
                    if (equip) {
                        am.setSpeedModifiers(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getEnchantLevel(this));
                    }else{
                        am.reApplyClassSpeedModifier();
                    }
                }
            }
            return;
        }
        if(e.getClick() == ClickType.NUMBER_KEY) {
            if (e.getRawSlot() == 8) {
                if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                    ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                    ItemStack oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                    if (!isAirOrNull(hotbarItem)) {
                        if (!hotbarItem.getType().name().endsWith("_BOOTS")){
                            return;
                        }
                        if(!isAirOrNull(oldArmorPiece)) {
                            if (oldArmorPiece.getItemMeta().hasEnchant(this)) {
                                am.reApplyClassSpeedModifier();
                            }
                        }
                        if (Objects.requireNonNull(hotbarItem.getItemMeta()).hasEnchant(this)) {
                            am.setSpeedModifiers(Objects.requireNonNull(hotbarItem.getItemMeta()).getEnchantLevel(this));
                        }
                    } else {
                        if(!isAirOrNull(oldArmorPiece)) {
                            if (oldArmorPiece.getItemMeta().hasEnchant(this)) {
                                am.reApplyClassSpeedModifier();
                            }
                        }
                    }
                }
            }
            return;
        }
        if (e.getRawSlot() == 8) {
            if (cursorItem.getType().name().equals("AIR") || cursorItem.getType().name().endsWith("_BOOTS")) {
                if (!cursorItem.getType().name().equals("AIR") && !(currentItem == null)){
                    if (Objects.requireNonNull(cursorItem.getItemMeta()).hasEnchant(this)) {
                        am.setSpeedModifiers(Objects.requireNonNull(cursorItem.getItemMeta()).getEnchantLevel(this));
                    }
                }else {
                    am.reApplyClassSpeedModifier();
                }
            }
        }
        return;
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
