package me.c10coding.CustomEnchants.enchants;

import me.c10coding.CustomEnchants.runnables.ShuffleShotCooldown;
import me.c10coding.CustomEnchants.utils.Chat;
import me.c10coding.CustomEnchants.utils.EnchantmentKeys;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShuffleShotEnchant extends CustomEnchant{

    HashMap<UUID, Integer> shuffleShotEnablers = new HashMap<UUID, Integer>();

    public ShuffleShotEnchant(){
        super(EnchantmentKeys.shuffle_shot, ChatColor.MAGIC, Particle.SPELL_WITCH);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        new ShuffleShotCooldown(plugin).runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onBowLeftClick(PlayerInteractEvent e){

        Action a = e.getAction();
        Player p = e.getPlayer();

        if(!p.hasMetadata("Shuffle_Shot_Cooldown")){
            if(a.equals(Action.LEFT_CLICK_AIR) || a.equals(Action.LEFT_CLICK_BLOCK)){
                if(p.getItemInHand().getType().equals(Material.BOW)){
                    ItemStack bow = p.getItemInHand();
                    if(bow.hasItemMeta() && bow.getItemMeta().hasEnchant(this)){
                        if(shuffleShotEnablers.containsKey(p.getUniqueId())){
                            shuffleShotEnablers.remove(p.getUniqueId());
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&lShuffle Shot &r&l has been &c&ldisabled!&b&l]")));
                        }else{
                            int lvl = bow.getItemMeta().getEnchantLevel(this);
                            shuffleShotEnablers.put(p.getUniqueId(), lvl);
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&lShuffle Shot &r&l has been &a&lenabled!&b&l]")));
                        }
                    }
                }
            }
        }else{
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&lShuffle Shot &r&lis on cooldown for another &c&l" + p.getMetadata("Shuffle_Shot_Cooldown").get(0).asInt() +"&r&l seconds!&b&l]")));
        }

    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e){

        Entity hit = e.getEntity();
        Entity damager = e.getDamager();

        if(!e.isCancelled()) {
            if (damager instanceof Arrow) {
                Arrow arrow = (Arrow) damager;
                if (arrow.getShooter() instanceof Player) {
                    Player p = (Player) arrow.getShooter();
                    if (shuffleShotEnablers.containsKey(p.getUniqueId())) {

                        double cooldown = config.getDouble(configPath + ".CooldownPerLevel." + shuffleShotEnablers.get(p.getUniqueId()));
                        p.setMetadata("Shuffle_Shot_Cooldown", new FixedMetadataValue(plugin, cooldown));

                        Location damagerLocation = p.getLocation();
                        Location hitEnityLocation = hit.getLocation();

                    /*
                    Solved the problem that happened when you also had Tempest on your bow
                     */
                        hitEnityLocation.setX(hitEnityLocation.getX() + 1.5);
                        hitEnityLocation.setZ(hitEnityLocation.getZ() + 1.5);

                        p.teleport(hitEnityLocation);
                        hit.teleport(damagerLocation);

                        p.getWorld().spawnParticle(enchantParticle, p.getLocation(), 50);
                        p.getWorld().spawnParticle(enchantParticle, hit.getLocation(), 50);
                        shuffleShotEnablers.remove(p.getUniqueId());

                    }
                }
            }
        }

    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.BOW;
    }
}
