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
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static org.bukkit.event.Event.*;


public class SwiftFootEnchant extends CustomEnchant{
    public static boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().equals(Material.AIR);
    }
    public SwiftFootEnchant(){
        super(EnchantmentKeys.swift_foot, ChatColor.DARK_GRAY, Particle.FLASH);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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
                        am.setSpeedModifier(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getEnchantLevel(this), "SF");
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
                            am.setSpeedModifier(Objects.requireNonNull(hotbarItem.getItemMeta()).getEnchantLevel(this), "SF");
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
            if (cursorItem.getType().name().equals("AIR") || cursorItem == null) {
               if (currentItem.getItemMeta().hasEnchant(this)){
                   am.reApplyClassSpeedModifier();
               }
            }else if (cursorItem.getType().name().endsWith("_BOOTS")){
                if (!isAirOrNull(currentItem)) {
                    if (currentItem.getItemMeta().hasEnchant(this)) {
                        am.reApplyClassSpeedModifier();
                    }
                }
                if (cursorItem.getItemMeta().hasEnchant(this)){
                    am.setSpeedModifier(Objects.requireNonNull(cursorItem.getItemMeta()).getEnchantLevel(this), "SF");
                }
            }
        }
        return;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.useItemInHand().equals(Result.DENY)) return;
        if(e.getAction() == Action.PHYSICAL) return;
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = e.getPlayer();
            AttributeManager am = new AttributeManager(Main.getInstance(), player);
            ItemStack clickedItem = e.getItem();
            if (clickedItem != null){
                if (clickedItem.getType().name().endsWith("_BOOTS") && isAirOrNull(e.getPlayer().getInventory().getBoots())){
                    if(clickedItem.getItemMeta().hasEnchant(this)){
                        am.setSpeedModifier(Objects.requireNonNull(clickedItem.getItemMeta()).getEnchantLevel(this), "SF");
                    }
                }
            }

        }

    }
    @EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
    public void inventoryDrag(InventoryDragEvent event){
        if (event.getRawSlots().isEmpty()) return;
        ItemStack draggedItem = event.getOldCursor();
        Player p = (Player) event.getWhoClicked();
        AttributeManager am = new AttributeManager(Main.getInstance(), p);
        if (draggedItem != null && event.getRawSlots().stream().findFirst().orElse(0) == 8){
            if (draggedItem.getType().name().endsWith("_BOOTS")){
                if (draggedItem.getItemMeta().hasEnchant(this)){
                    am.setSpeedModifier(Objects.requireNonNull(draggedItem.getItemMeta()).getEnchantLevel(this), "SF");
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
