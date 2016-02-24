package savior67.safariplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import savior67.safariplugin.SafariPlugin;
import savior67.safariplugin.enums.EnumStages;
import savior67.safariplugin.stages.Battle;
import savior67.safariplugin.stages.StageBase;

public class LoginListener implements Listener {
	private String kickMsg = "A Safari Game Is Currently In-Progress";
	
    //Kicks player if not in lobby phase
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
    	
    	event.getPlayer().setScoreboard(ScoreboardListener.sb);
    	
    	EnumStages stage = SafariPlugin.getCurrentStage();
    	Player playerJoined = event.getPlayer();
    	
    	if(playerJoined.hasPermission("SafariPlugin.exempt"))
    		return;
        else if((stage == EnumStages.Preparation || stage == EnumStages.Capture) && SafariPlugin.isInGame(playerJoined))
        	return;
    	else if(stage == EnumStages.Initialize) {
    		playerJoined.kickPlayer("Please wait until the world is finished initiliazing");
    	}
        else if(stage == EnumStages.Lobby){
        	playerJoined.teleport(SafariPlugin.lobbyCoords);
        	playerJoined.getInventory().clear();
    		//StageBase.clearInventory(playerJoined);
        }
        else if(stage == EnumStages.Capture && SafariPlugin.getRemainingTimeInStage().getMinutes() >= 10) {
        	StageBase.teleportPlayerTo(playerJoined, EnumStages.Capture);
        	return;
        }
    	else if(stage != EnumStages.Lobby) {
        	playerJoined.kickPlayer(kickMsg);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	// Called when a player leaves the server
    	Player player = event.getPlayer();
    	String pName = player.getDisplayName();
    	
    	if(SafariPlugin.currentStage == EnumStages.Battle && SafariPlugin.betweenRounds == false && Battle.isCompetitor(pName)) {
    		Bukkit.getLogger().info("[SafariPlugin] Player "+pName+" has left during a battle.");
    		String opponent = Battle.getOpponentOf(player);
    		if( opponent == pName) return;
    		SafariPlugin.addSpectator(pName);
    		SafariPlugin.addSpectator(opponent);
    		SafariPlugin.addPoints(opponent, 4);
    		SafariPlugin.addPoints(pName, 0); //add points here so that he's counted towards battlesFinished
    		Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+opponent+" has defeated "+pName);
    		Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.GREEN+opponent+" has earned 4 points, "+pName+" has earned 0 points!");
    	}
    }
    

 
//    @EventHandler(priority = EventPriority.HIGH)
//    public void highLogin(PlayerLoginEvent event) {
//        if(SafariPlugin.getCurrentStage() != EnumStages.Lobby)
//        	event.getPlayer().kickPlayer(kickMsg);
//        else
//        	event.getPlayer().teleport(SafariPlugin.lobbyCoords);
//    }
    
}
