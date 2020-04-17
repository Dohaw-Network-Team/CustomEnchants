package me.c10coding.CustomEnchants.runnables;

import me.c10coding.CustomEnchants.Main;
import me.c10coding.CustomEnchants.utils.Chat;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class MuscleSapCooldown extends BukkitRunnable {

    private Main plugin;

    public MuscleSapCooldown(Main plugin){
        this.plugin = plugin;
    }


    @Override
    public void run() {
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            if(p.hasMetadata("Muscle_Sap_Cooldown") && !p.getMetadata("Muscle_Sap_Cooldown").isEmpty()){
                int timeLeft = p.getMetadata("Muscle_Sap_Cooldown").get(0).asInt();
                timeLeft--;
                if(timeLeft != 0){
                    p.setMetadata("Muscle_Sap_Cooldown", new FixedMetadataValue(plugin, timeLeft));
                }else{
                    p.removeMetadata("Muscle_Sap_Cooldown", plugin);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Chat.chat("&b&l[&a&lMuscle Sap (enchant) cooldown is up!&b&l]")));
                }
            }
        }
    }
}
