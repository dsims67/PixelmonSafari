package savior67.safariplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class ChatListener implements Listener {
	
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
    	if(event.getPlayer() instanceof Player) {
    		Player p = event.getPlayer();
    		String name = p.getDisplayName();
    		String msg = ChatColor.GREEN+"<Trainer>"+name+ChatColor.GRAY+": "+event.getMessage();
    		Bukkit.getServer().broadcastMessage(msg);
    		event.setCancelled(true);
    	}
    	
    }

}

