package savior67.safariplugin.stages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import savior67.safariplugin.enums.EnumStages;

public class Finish extends StageBase {
	
	public static void init() {
		teleportAllTo(EnumStages.Finish);
		clearAllInventory();
		Bukkit.broadcastMessage(ChatColor.DARK_AQUA+"Kicking Players in 30 seconds to prepare for the next round.");
	}

}
