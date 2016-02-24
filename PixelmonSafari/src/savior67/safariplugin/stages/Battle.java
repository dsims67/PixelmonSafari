package savior67.safariplugin.stages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import savior67.safariplugin.SafariPlugin;
import savior67.safariplugin.enums.EnumStages;

public class Battle extends StageBase{
	
	//split into 3 rounds, 8 mins each
	private static int roundNum = 1;
	public static boolean oddNumPlayers;
	public static boolean isBattling = false;
	private static int numCompetitors; //does not include spectator
	private static List<Player> team1 = new ArrayList<Player>();
	private static List<Player> team2 = new ArrayList<Player>();
	private static List<Player> competitors = new ArrayList<Player>();
	
	
	
	public static void init() {
		//initialize variables
		team1.clear();
		team2.clear();
		competitors.clear();
		Spectate.clearSpectators();
		numCompetitors = 0;
		
		//countdown and then start the round
		countDown();
	}
	
	public static void endRound() {
		Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+"Round "+roundNum+" has finished!");
		if(roundNum < SafariPlugin.totalBattleRounds) {
			StageBase.healAllPokes();
			roundNum += 1;
			init();
		}
		else {
			roundNum = 1;
			StageBase.clearAllInventory();
			Bukkit.broadcastMessage(ChatColor.GREEN+"Thank you for participating in the "+ChatColor.RED+"Fire Red"+ChatColor.GREEN+" Safari Games!");
			SafariPlugin.setStage(EnumStages.Battle.next());
		}
		
	}
	
	public static void startRound() {
		SafariPlugin.timeLeftInStage = DateUtils.addMinutes(new Date(), EnumStages.Battle.getPhaseTime());

		getOpponents();
		startBattles();
		
		//winner is determined in BattleListener
		
		//when a player finishes battling, announcer <name> has defeated <name>!, teleport them to the spectator area.
		//add points, 10 for win, 5 for loss, + 20 bonus if 3 wins
		//make a battle listener, doesn't listen until isBattling = true;
	}
	
	//teleports all players to their battle positions
	public static void teleportToBattlePositions() {
		for(int spot=0; spot<team1.size(); spot++) {
				team1.get(spot).teleport(getBattleSpot(spot+1,"A"));
				team2.get(spot).teleport(getBattleSpot(spot+1,"B"));
		}
	}
	
	private static Location getBattleSpot(int numSpot, String side) {
		FileConfiguration config = SafariPlugin.config;
		String sNum = Integer.toString(numSpot);
		Double x = config.getDouble("Safari.Coordinates.Battle"+sNum+side+".X");
		Double y = config.getDouble("Safari.Coordinates.Battle"+sNum+side+".Y"+1); //add one so that people don't glitch through floor
		Double z = config.getDouble("Safari.Coordinates.Battle"+sNum+side+".Z");
		Float pitch = (float) config.getDouble("Safari.Coordinates.Battle"+sNum+side+".Pitch");
		Float yaw = (float) config.getDouble("Safari.Coordinates.Battle"+sNum+side+".Yaw");
		Location battleLoc = new Location(Bukkit.getWorld("world"), x, y, z);
		battleLoc.setPitch(pitch);
		battleLoc.setYaw(yaw);
		return(battleLoc);
	}
	
	
	//pairs each player to each other, announces competitors
	private static void getOpponents() {
		competitors.clear();
		Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+"Round "+roundNum+" Competitors");
		for(Player p:Bukkit.getOnlinePlayers()) {
			if(!p.hasPermission("SafariPlugin.exempt"))
			{
					competitors.add(p);
			}
		}
		List<Player> players = shuffle(competitors);
	    numCompetitors = players.size();
	    
		//On the first round, the last player sits out
		if(players.size()%2 == 1) {
			numCompetitors = players.size()-1;
			oddNumPlayers = true;
			Spectate.setSpectator(competitors.get(numCompetitors).getName()); //Player is already subtracted at the top
		}
		else {
			oddNumPlayers = false;
		}
		
		//puts the last player in the competitor box
			
		
		for(int i=0; i < numCompetitors; i+=2) {
			team1.add(players.get(i));
			team2.add(players.get(i+1));
			Bukkit.broadcastMessage(ChatColor.RED+players.get(i).getDisplayName()+ChatColor.LIGHT_PURPLE+" vs "+ChatColor.BLUE+players.get(i+1).getDisplayName());
		}
	}
	
	//Randomly Shuffles an array of players (Fisher-Yates Shuffle)
	private static List<Player> shuffle(List<Player> competitors2) {
		int currentIndex = competitors2.size();
		Player tempValue;
		int randomIndex;

		// While there remain elements to shuffle...
		while (0 != currentIndex) {

			// Pick a remaining element...
			randomIndex = (int) Math.floor(Math.random() * currentIndex);
			currentIndex -= 1;

			// And swap it with the current element.
			tempValue = competitors2.get(currentIndex);
			competitors2.set(currentIndex, competitors2.get(randomIndex));
			competitors2.set(randomIndex, tempValue);
		}
		
		return competitors2;
	}
	
	//counts down from 10.. 5.. 4..3..2...1
	//Heals pokes
	private static void countDown() {
		//teleportAllTo(EnumStages.Spectate);
		SafariPlugin.timeLeftInStage = DateUtils.addSeconds(new Date(), 10);
		SafariPlugin.betweenRounds = true;
	}
	
	//starts the battles between each player
	private static void startBattles() {
		
    	Battle.teleportToBattlePositions();
    	delay(1);
    	for(int i=0; i < team1.size(); i++) {
    		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pokebattle "+team1.get(i).getDisplayName()+" "+team2.get(i).getDisplayName());
    	}
    	
		isBattling = true;
		
	}
	
	public static int getTeamSize() {
		return team1.size();
	}
	
	public static int getTotalCompetitors() {
		return competitors.size();
	}
	
	public static boolean isCompetitor(String pName) {
		return( competitors.contains(Bukkit.getPlayer(pName)) );
	}
	
	public static void removeCompetitor(String pName) {
		Player p = Bukkit.getPlayer(pName);
		if( competitors.contains(p)) {
			competitors.remove(p);
		}
	}
	
	public static String getOpponentOf(String pName) {
		Player p = Bukkit.getPlayer(pName);
		if(team2.contains(p))
			return team1.get(team2.indexOf(p)).getName();
		else if(team1.contains(p))
			return team2.get(team1.indexOf(p)).getName();
		else
			return p.getName();
	}
	
	//with player object
	public static String getOpponentOf(Player p) {
		if(team2.contains(p))
			return team1.get(team2.indexOf(p)).getName();
		else if(team1.contains(p))
			return team2.get(team1.indexOf(p)).getName();
		else
			return p.getName();
	}
	
	//returns the current battle round number
	public static int getRound() {
		return roundNum;
	}
	

}
