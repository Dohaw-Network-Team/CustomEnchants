package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.utils.Chat;
import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import org.bukkit.*;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HeadlessEnchant extends CustomEnchant{

    public HeadlessEnchant(){
        super(EnchantmentKeys.headless, ChatColor.STRIKETHROUGH, Particle.SPELL);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){

       if(e.getEntity() instanceof Player){
           Player p = (Player) e.getEntity();
           if(p.getKiller() instanceof Player){
               Player killer = p.getKiller();
               ItemStack itemInHand = killer.getItemInHand();
               double chance;

               if(itemInHand.hasItemMeta()){
                   ItemMeta im = itemInHand.getItemMeta();
                   if(im.hasEnchant(this)){

                       chance = getChance(im.getEnchantLevel(this));
                       int i = rnd.nextInt(100);

                       if(i <= chance){
                           ItemStack head = getPlayerHead(p);
                           killer.getInventory().addItem(head);
                           Chat.sendPlayerMessage("&d&l[Headless Enchant]&r - You have acquired &c&l" + p.getName() + "\'s head!", false, killer, "");
                       }else{
                           return;
                       }
                   }
               }else{
                   return;
               }
           }
       }

    }

    public ItemStack getPlayerHead(Player deadPlayer){
        //Puts all the Materials in a collection and sees if player head is in there
        boolean isNewVersion = Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD");
        //? MEANS IF IS TRUE.
       /*
       IF isNewVersion if true, then set it to PLAYER_HEAD. If it's false then set it to SKULL_ITEM
        */
        Material type = Material.matchMaterial(isNewVersion ? "PLAYER_HEAD" : "SKULL_ITEM");
        ItemStack playerSkull = new ItemStack(type, 1);

        if(!isNewVersion){
            playerSkull.setDurability((short) 3);
        }

        SkullMeta meta = (SkullMeta) playerSkull.getItemMeta();
        meta.setOwningPlayer(deadPlayer);

        playerSkull.setItemMeta(meta);

        return playerSkull;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.WEAPON;
    }
}
