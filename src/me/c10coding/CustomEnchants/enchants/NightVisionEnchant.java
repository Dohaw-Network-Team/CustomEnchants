package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.*;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.Objects;

public class NightVisionEnchant extends CustomEnchant {
    public static boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().equals(Material.AIR);
    }
    public NightVisionEnchant() {
        super(EnchantmentKeys.night_vision, ChatColor.BLUE, null);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();

        if(e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.PLAYER) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
        if((e.getSlotType() != InventoryType.SlotType.ARMOR) && (e.getSlotType() != InventoryType.SlotType.QUICKBAR) && (e.getSlotType() != InventoryType.SlotType.CONTAINER)) return;

        ItemStack cursorItem = e.getCursor();
        ItemStack currentItem = e.getCurrentItem();
        if (e.isShiftClick()){
            if (e.getCurrentItem() != null){
                boolean equip = true;
                if (e.getRawSlot() == 5) equip = false;
                if (e.getCurrentItem().getType().name().endsWith("_HELMET") && (equip == isAirOrNull(e.getWhoClicked().getInventory().getHelmet()))){
                    if (equip) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1), true);
                    }else{
                        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    }
                }
            }
            return;
        }
        if(e.getClick() == ClickType.NUMBER_KEY) {
            if (e.getRawSlot() == 5) {
                if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                    ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                    ItemStack oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                    if (!isAirOrNull(hotbarItem)) {
                        if (!hotbarItem.getType().name().endsWith("_HELMET")){
                            return;
                        }
                        if(!isAirOrNull(oldArmorPiece)) {
                            if (oldArmorPiece.getItemMeta().hasEnchant(this)) {
                                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            }
                        }
                        if (Objects.requireNonNull(hotbarItem.getItemMeta()).hasEnchant(this)) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1), true);
                        }
                    } else {
                        if(!isAirOrNull(oldArmorPiece)) {
                            if (oldArmorPiece.getItemMeta().hasEnchant(this)) {
                                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            }
                        }
                    }
                }
            }
            return;
        }
        if (e.getRawSlot() == 5) {
            if (cursorItem.getType().name().equals("AIR") || cursorItem == null) {
                if (currentItem.getItemMeta().hasEnchant(this)){
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
            }else if (cursorItem.getType().name().endsWith("_HELMET")){
                if (!isAirOrNull(currentItem)) {
                    if (currentItem.getItemMeta().hasEnchant(this)) {
                        p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    }
                }
                if (cursorItem.getItemMeta().hasEnchant(this)){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1), true);
                }
            }
        }
        return;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.useItemInHand().equals(Event.Result.DENY)) return;
        if(e.getAction() == Action.PHYSICAL) return;
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = e.getPlayer();
            ItemStack clickedItem = e.getItem();
            if (clickedItem != null){
                if (clickedItem.getType().name().endsWith("_HELMET") && isAirOrNull(e.getPlayer().getInventory().getHelmet())){
                    if(clickedItem.getItemMeta().hasEnchant(this)){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1), true);
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
        if (draggedItem != null && event.getRawSlots().stream().findFirst().orElse(0) == 5){
            if (draggedItem.getType().name().endsWith("_HELMET")){
                if (draggedItem.getItemMeta().hasEnchant(this)){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1), true);
                }
            }
        }
    }
    public void onRespawn(PlayerRespawnEvent e){
        ItemStack helmetSlot = e.getPlayer().getInventory().getHelmet();
        if (helmetSlot != null){
            if (helmetSlot.getItemMeta().hasEnchant(this)){
                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1), true);
            }
        }
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR_HEAD;
    }

}
