package savior67.safariplugin.stages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;






import savior67.safariplugin.SafariPlugin;
import savior67.safariplugin.enums.EnumStages;

public class StageBase {
	
	public static void teleportAllTo(EnumStages stage) {
		if(stage == EnumStages.Battle) {
			Battle.teleportToBattlePositions(); 
			return;
		}
		
		for(Player p:Bukkit.getOnlinePlayers()) {
			if(!p.hasPermission("SafariPlugin.exempt"))
				p.teleport(SafariPlugin.getLocation(stage));
		}
	}
	
	public static void teleportPlayerTo(Player p,EnumStages stage) {
		if(stage == EnumStages.Battle)
			stage = EnumStages.Spectate;
		p.teleport(SafariPlugin.getLocation(stage));
	}
	
	public static void addPoints(String pName, int amt) {
		SafariPlugin.finishedBattles += 1;
		Player player = Bukkit.getPlayer(pName);
		if(SafariPlugin.usePointSystem)
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "enjin addpoints "+player.getDisplayName()+" "+amt);
	}
	
	public static void clearAllInventory() {
		for(Player p:Bukkit.getOnlinePlayers()) {
			//Clears Items
			if(!p.hasPermission("SafariPlugin.exempt")) {
				p.getInventory().clear();
			}
		}
	}
	
	public static void delay(int seconds) {
	    try {
			Thread.sleep(1000*seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void kickAllPlayers(String msg) {
		for(Player p:Bukkit.getOnlinePlayers()) {
			p.kickPlayer(msg);
		}
	}
	
	public static void healAllPokes() {
		for(Player p:Bukkit.getOnlinePlayers()) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pokeheal "+p.getDisplayName());
		}
	}
	
	public static void clearInventory(Player p) {
		//Clears Items
		if(!p.hasPermission("SafariPlugin.exempt"))
			p.getInventory().clear();
	}

	public static void addSpectator(String pName) {
		Spectate.addSpectatorToList(pName);
		Player p = Bukkit.getPlayer(pName);
		if(p == null) return;
		
		teleportPlayerTo(p, EnumStages.Spectate);
		p.sendMessage("You've been teleported to the spectator lounge");
		
	}
	
	public static String getOpponent(String player) {
		return Battle.getOpponentOf(player);
	}
	
	
	
	

}
