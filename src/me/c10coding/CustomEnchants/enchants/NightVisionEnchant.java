package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NightVisionEnchant extends CustomEnchant {
    public NightVisionEnchant() {
        super(EnchantmentKeys.night_vision, ChatColor.BLUE, null);
    }
    @EventHandler
    public void OnHelmetEquip(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        Inventory player_inv = player.getInventory();
        InventoryAction action = e.getAction();
        if (e.getClickedInventory() == null) {
            return;
        }
        if(e.getCurrentItem() == null){
            return;
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
