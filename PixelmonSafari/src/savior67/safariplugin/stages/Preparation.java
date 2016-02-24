package savior67.safariplugin.stages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import savior67.safariplugin.enums.EnumStages;

public class Preparation extends StageBase{
	public static void init() {
		Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+"Team Preparation Phase Has Begun!");
		teleportAllTo(EnumStages.Preparation);
	}
}
