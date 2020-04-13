package me.c10coding.CustomEnchants.runnables;

import me.c10coding.CustomEnchants.Main;
import me.c10coding.CustomEnchants.utils.Chat;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class ShuffleShotCooldown extends BukkitRunnable {

    private Main plugin;

    public ShuffleShotCooldown(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(p.hasMetadata("Shuffle_Shot_Cooldown") && !p.getMetadata("Shuffle_Shot_Cooldown").isEmpty()){
                int cooldownTime = p.getMetadata("Shuffle_Shot_Cooldown").get(0).asInt();
                if(cooldownTime != 0){
                    cooldownTime--;
                    p.setMetadata("Shuffle_Shot_Cooldown", new FixedMetadataValue(plugin, cooldownTime));
                }else if(cooldownTime == 0){
                    p.removeMetadata("Shuffle_Shot_Cooldown", plugin);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&lShuffle Shot cooldown is up!&b&l]")));
                }
            }
        }
    }
}
