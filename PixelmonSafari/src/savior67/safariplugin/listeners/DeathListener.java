package savior67.safariplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import savior67.safariplugin.SafariPlugin;
import savior67.safariplugin.stages.StageBase;

public class DeathListener implements Listener {
	
    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
    	if(event.getEntity() instanceof Player) {
    		Player player = (Player) event.getEntity();
    		StageBase.teleportPlayerTo(player, SafariPlugin.getCurrentStage());
    	}
    	
    }

}
