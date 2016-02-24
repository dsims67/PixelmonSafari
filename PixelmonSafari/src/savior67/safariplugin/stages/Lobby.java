package savior67.safariplugin.stages;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import savior67.safariplugin.enums.EnumStages;

public class Lobby extends StageBase{
	
	public static void init() {
		Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+"Lobby Phase Has Begun!");
		teleportAllTo(EnumStages.Lobby);
		
	}
	
	

}
