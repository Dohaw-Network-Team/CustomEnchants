package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import me.caleb.Classes.Main;
import me.caleb.Classes.utils.managers.AttributeManager;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class NightVisionEnchant extends CustomEnchant {
    public NightVisionEnchant() {
        super(EnchantmentKeys.night_vision, ChatColor.BLUE, null);
    }
    @EventHandler
    public void OnHelmetEquip(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        InventoryAction action = e.getAction();
        if (e.getClickedInventory() == null) {
            return;
        }
        if(e.getCurrentItem() == null){
            return;
        }
        if(e.getSlotType().equals(InventoryType.SlotType.ARMOR) && e.getCurrentItem().getType().name().toLowerCase().contains("helmet")){
            if(action.equals(InventoryAction.PICKUP_ALL) || action.equals(InventoryAction.PLACE_ALL) || action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)){
                if(e.getCurrentItem().getType().name().toLowerCase().contains("helmet") || e.getCursor().getType().name().toLowerCase().contains("helmet")){
                    ItemStack helmet = e.getCurrentItem().getType().name().toLowerCase().contains("helmet") ? e.getCurrentItem() : e.getCursor();
                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(this)) {
                        AttributeManager am;
                        am = new AttributeManager(Main.getInstance(), player);
                        if (!action.equals(InventoryAction.PICKUP_ALL)) {
                            if (!action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                                if (action.equals(InventoryAction.PLACE_ALL)) {
                                    player.addPotionEffect(new PotionEffect((Map<String, Object>) PotionEffectType.NIGHT_VISION), true);
                                }
                            } else {
                                if (player.getInventory().getHelmet() == null) {
                                    player.addPotionEffect(new PotionEffect((Map<String, Object>) PotionEffectType.NIGHT_VISION), true);
                                } else {
                                    player.addPotionEffect(new PotionEffect((Map<String, Object>) PotionEffectType.NIGHT_VISION), false);
                                }
                            }
                        } else {
                            player.addPotionEffect(new PotionEffect((Map<String, Object>) PotionEffectType.NIGHT_VISION), false);
                        }
                    }
                }
            }
        }
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

}
