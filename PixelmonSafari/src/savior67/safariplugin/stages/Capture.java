package savior67.safariplugin.stages;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import savior67.safariplugin.enums.EnumBiomes;

public class Capture extends StageBase {
	
	public static void init() {
		clearAllInventory();
		Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+"Capture Phase Has Begun!");
		//teleportAllTo(EnumStages.Capture);
		randomTeleportToBiomes(); //randomly disperse players between available biomes.
		for(Player p:Bukkit.getOnlinePlayers()) {
			if(!p.hasPermission("SafariPlugin.exempt"))
				giveSafariInventory(p);
		}
		
		
	}
	
	//teleports all players to randomly assigned biomes
	public static void randomTeleportToBiomes() {
		Random rand = new Random();
		for(Player p:Bukkit.getOnlinePlayers()) {
			int biomeID = rand.nextInt(8)+1; //returns a number 1-8
			if(!p.hasPermission("SafariPlugin.exempt"))
				p.teleport(getBiomeFromID(biomeID).getLocation());
		}
	}
	
	public static EnumBiomes getBiomeFromID(int num) {
		for(EnumBiomes b: EnumBiomes.values()) {
			if(b.getID() == num)
			{
				return b;
			}
		}
		return(EnumBiomes.Plains); //default to plains if num not found.
		
	}
	
	public static void giveSafariInventory(Player p) {
		//Gives pokeballs and items for capture phase

		Random r = new Random();

		int safariBall = 8264;
		int potion = 10262;
		int rareCandy = 10261;
		int expShare = 12258;
		
		//Random integer between 10264 - 10268
		int stone = r.nextInt(10269-10264)+10264;
		int tm1 = r.nextInt(11352-11256)+11256;
		int tm2 = r.nextInt(11352-11256)+11256;
		int tm3 = r.nextInt(11352-11256)+11256;

		int[] captureItems = {rareCandy, expShare, stone, tm1, tm2, tm3};

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" "+Integer.toString(safariBall)+" 20");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" "+Integer.toString(potion)+" 5");

		for(int i=0; i<6; i++) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" "+Integer.toString(captureItems[i])+" 1");
		}
		
		//only given to player if permission is found
		giveMasterball(p);
		giveRareCandies(p);
		giveUltraballs(p);
		giveRandomChoice(p);
	}
	
	//one extra masterball
	public static void giveMasterball(Player p) {
		if(p.hasPermission("SafariPlugin.masterball")) 
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" 8259 1");
			p.sendMessage("You've received a masterball!");
		}
	}

	//an additional 4 rare candies
	public static void giveRareCandies(Player p) {
		if(p.hasPermission("SafariPlugin.extraCandies")) 
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" 10261 4");
			p.sendMessage("You've received 4 extra rare candies!");
		}
	}

	//an additional 5 Ultra balls
	public static void giveUltraballs(Player p) {
		if(p.hasPermission("SafariPlugin.ultraballs")) 
		{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" 8258 5");
			p.sendMessage("You've received 5 ultra balls!");
		}
	}

	//an additional choice item
	public static void giveRandomChoice(Player p) {
		if(p.hasPermission("SafariPlugin.choiceItem")) 
		{
			Random r = new Random();
			int choiceItem = r.nextInt(12272-12262)+12262;

			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" "+choiceItem+" 1");
			p.sendMessage("You've received a choice item!");
		}
	}
	
	
	

}
