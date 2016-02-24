package savior67.safariplugin;


import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import savior67.safariplugin.enums.EnumBiomes;
import savior67.safariplugin.enums.EnumStages;
import savior67.safariplugin.listeners.ChatListener;
import savior67.safariplugin.listeners.LoginListener;
import savior67.safariplugin.listeners.ScoreboardListener;
import savior67.safariplugin.stages.Battle;
import savior67.safariplugin.stages.Capture;
import savior67.safariplugin.stages.Finish;
import savior67.safariplugin.stages.Initialize;
import savior67.safariplugin.stages.Lobby;
import savior67.safariplugin.stages.Preparation;
import savior67.safariplugin.stages.StageBase;

public final class SafariPlugin extends JavaPlugin implements Listener{
	public static Location lobbyCoords;
	public static EnumStages currentStage; //Lobby, Capture, Preparation, Battle
	public static Date timeLeftInStage;
	public static boolean betweenRounds = false;
	public static FileConfiguration config;
	public static boolean usePointSystem = false;
	public static int totalBattleRounds = 3;
	public StageBase stageBase;
	public static ArrayList<String> currentPlayers = new ArrayList<String>();
	public static ArrayList<String> changedStarter = new ArrayList<String>();
	public static String serverDirectory;
	public static int finishedBattles = 0;
	
	@Override
	public void onEnable() {
		getLogger().info("Safari Plugin has been enabled!");
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new LoginListener(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		
		config = this.getConfig();
		
		//Variables
		config.addDefault("Safari.Options.PointSystem", false);
		config.addDefault("Safari.Options.BattleRounds", 3);

		//Phase Time Lengths
		config.addDefault("Safari.Phase_Time.Initialize", 1);
		config.addDefault("Safari.Phase_Time.Lobby", 10);
		config.addDefault("Safari.Phase_Time.Capture", 25);
		config.addDefault("Safari.Phase_Time.Preparation", 4);
		config.addDefault("Safari.Phase_Time.Battle", 8); //for each round
		
		//Biome Coordinates
		config.addDefault("Safari.Coordinates.Ocean.X", 0);
		config.addDefault("Safari.Coordinates.Ocean.Y", 65);
		config.addDefault("Safari.Coordinates.Ocean.Z", 0);
		config.addDefault("Safari.Coordinates.Ocean.Pitch", 0);
		config.addDefault("Safari.Coordinates.Ocean.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Plains.X", 0);
		config.addDefault("Safari.Coordinates.Plains.Y", 65);
		config.addDefault("Safari.Coordinates.Plains.Z", 0);
		config.addDefault("Safari.Coordinates.Plains.Pitch", 0);
		config.addDefault("Safari.Coordinates.Plains.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Desert.X", 0);
		config.addDefault("Safari.Coordinates.Desert.Y", 65);
		config.addDefault("Safari.Coordinates.Desert.Z", 0);
		config.addDefault("Safari.Coordinates.Desert.Pitch", 0);
		config.addDefault("Safari.Coordinates.Desert.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Hills.X", 0);
		config.addDefault("Safari.Coordinates.Hills.Y", 65);
		config.addDefault("Safari.Coordinates.Hills.Z", 0);
		config.addDefault("Safari.Coordinates.Hills.Pitch", 0);
		config.addDefault("Safari.Coordinates.Hills.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Swamp.X", 0);
		config.addDefault("Safari.Coordinates.Swamp.Y", 65);
		config.addDefault("Safari.Coordinates.Swamp.Z", 0);
		config.addDefault("Safari.Coordinates.Swamp.Pitch", 0);
		config.addDefault("Safari.Coordinates.Swamp.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Forest.X", 0);
		config.addDefault("Safari.Coordinates.Forest.Y", 65);
		config.addDefault("Safari.Coordinates.Forest.Z", 0);
		config.addDefault("Safari.Coordinates.Forest.Pitch", 0);
		config.addDefault("Safari.Coordinates.Forest.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Jungle.X", 0);
		config.addDefault("Safari.Coordinates.Jungle.Y", 65);
		config.addDefault("Safari.Coordinates.Jungle.Z", 0);
		config.addDefault("Safari.Coordinates.Jungle.Pitch", 0);
		config.addDefault("Safari.Coordinates.Jungle.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Snow.X", 0);
		config.addDefault("Safari.Coordinates.Snow.Y", 65);
		config.addDefault("Safari.Coordinates.Snow.Z", 0);
		config.addDefault("Safari.Coordinates.Snow.Pitch", 0);
		config.addDefault("Safari.Coordinates.Snow.Yaw", 0);
		
		//Lobby Coordinates
		config.addDefault("Safari.Coordinates.Lobby.X", 0);
		config.addDefault("Safari.Coordinates.Lobby.Y", 65);
		config.addDefault("Safari.Coordinates.Lobby.Z", 0);
		config.addDefault("Safari.Coordinates.Lobby.Pitch", 0);
		config.addDefault("Safari.Coordinates.Lobby.Yaw", 0);
		
		//Capture Start Point Coordinates
		config.addDefault("Safari.Coordinates.Capture.X", 0);
		config.addDefault("Safari.Coordinates.Capture.Y", 65);
		config.addDefault("Safari.Coordinates.Capture.Z", 0);
		config.addDefault("Safari.Coordinates.Capture.Pitch", 0);
		config.addDefault("Safari.Coordinates.Capture.Yaw", 0);
		
		//Team Prep Coordinates
		config.addDefault("Safari.Coordinates.Preparation.X", 0);
		config.addDefault("Safari.Coordinates.Preparation.Y", 65);
		config.addDefault("Safari.Coordinates.Preparation.Z", 0);
		config.addDefault("Safari.Coordinates.Preparation.Pitch", 0);
		config.addDefault("Safari.Coordinates.Preparation.Yaw", 0);
		
		//Spectator Coordinates
		config.addDefault("Safari.Coordinates.Spectate.X", 0);
		config.addDefault("Safari.Coordinates.Spectate.Y", 65);
		config.addDefault("Safari.Coordinates.Spectate.Z", 0);
		config.addDefault("Safari.Coordinates.Spectate.Pitch", 0);
		config.addDefault("Safari.Coordinates.Spectate.Yaw", 0);
		
		//Finish Coordinates
		config.addDefault("Safari.Coordinates.Finish.X", 0);
		config.addDefault("Safari.Coordinates.Finish.Y", 65);
		config.addDefault("Safari.Coordinates.Finish.Z", 0);
		config.addDefault("Safari.Coordinates.Finish.Pitch", 0);
		config.addDefault("Safari.Coordinates.Finish.Yaw", 0);
		
		//BattlePos1 Coordinates
		config.addDefault("Safari.Coordinates.Battle1A.X", 0);
		config.addDefault("Safari.Coordinates.Battle1A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle1A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle1A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle1A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle1B.X", 0);
		config.addDefault("Safari.Coordinates.Battle1B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle1B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle1B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle1B.Yaw", 0);
		//BattlePos2 Coordinates	
		config.addDefault("Safari.Coordinates.Battle2A.X", 0);
		config.addDefault("Safari.Coordinates.Battle2A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle2A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle2A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle2A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle2B.X", 0);
		config.addDefault("Safari.Coordinates.Battle2B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle2B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle2B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle2B.Yaw", 0);
		//BattlePos3 Coordinates	
		config.addDefault("Safari.Coordinates.Battle3A.X", 0);
		config.addDefault("Safari.Coordinates.Battle3A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle3A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle3A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle3A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle3B.X", 0);
		config.addDefault("Safari.Coordinates.Battle3B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle3B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle3B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle3B.Yaw", 0);
		//BattlePos4 Coordinates	
		config.addDefault("Safari.Coordinates.Battle4A.X", 0);
		config.addDefault("Safari.Coordinates.Battle4A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle4A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle4A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle4A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle4B.X", 0);
		config.addDefault("Safari.Coordinates.Battle4B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle4B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle4B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle4B.Yaw", 0);
		//BattlePos5 Coordinates	
		config.addDefault("Safari.Coordinates.Battle5A.X", 0);
		config.addDefault("Safari.Coordinates.Battle5A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle5A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle5A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle5A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle5B.X", 0);
		config.addDefault("Safari.Coordinates.Battle5B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle5B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle5B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle5B.Yaw", 0);
		//BattlePos6 Coordinates	
		config.addDefault("Safari.Coordinates.Battle6A.X", 0);
		config.addDefault("Safari.Coordinates.Battle6A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle6A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle6A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle6A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle6B.X", 0);
		config.addDefault("Safari.Coordinates.Battle6B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle6B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle6B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle6B.Yaw", 0);
		//BattlePos7 Coordinates	
		config.addDefault("Safari.Coordinates.Battle7A.X", 0);
		config.addDefault("Safari.Coordinates.Battle7A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle7A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle7A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle7A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle7B.X", 0);
		config.addDefault("Safari.Coordinates.Battle7B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle7B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle7B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle7B.Yaw", 0);
		//BattlePos8 Coordinates	
		config.addDefault("Safari.Coordinates.Battle8A.X", 0);
		config.addDefault("Safari.Coordinates.Battle8A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle8A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle8A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle8A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle8B.X", 0);
		config.addDefault("Safari.Coordinates.Battle8B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle8B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle8B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle8B.Yaw", 0);
		//BattlePos9 Coordinates	
		config.addDefault("Safari.Coordinates.Battle9A.X", 0);
		config.addDefault("Safari.Coordinates.Battle9A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle9A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle9A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle9A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle9B.X", 0);
		config.addDefault("Safari.Coordinates.Battle9B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle9B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle9B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle9B.Yaw", 0);
		//BattlePos10 Coordinates	
		config.addDefault("Safari.Coordinates.Battle10A.X", 0);
		config.addDefault("Safari.Coordinates.Battle10A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle10A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle10A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle10A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle10B.X", 0);
		config.addDefault("Safari.Coordinates.Battle10B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle10B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle10B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle10B.Yaw", 0);
		//BattlePos11 Coordinates	
		config.addDefault("Safari.Coordinates.Battle11A.X", 0);
		config.addDefault("Safari.Coordinates.Battle11A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle11A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle11A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle11A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle11B.X", 0);
		config.addDefault("Safari.Coordinates.Battle11B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle11B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle11B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle11B.Yaw", 0);
		//BattlePos12 Coordinates	
		config.addDefault("Safari.Coordinates.Battle12A.X", 0);
		config.addDefault("Safari.Coordinates.Battle12A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle12A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle12A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle12A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle12B.X", 0);
		config.addDefault("Safari.Coordinates.Battle12B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle12B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle12B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle12B.Yaw", 0);
		//BattlePos13 Coordinates	
		config.addDefault("Safari.Coordinates.Battle13A.X", 0);
		config.addDefault("Safari.Coordinates.Battle13A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle13A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle13A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle13A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle13B.X", 0);
		config.addDefault("Safari.Coordinates.Battle13B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle13B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle13B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle13B.Yaw", 0);
		//BattlePos14 Coordinates	
		config.addDefault("Safari.Coordinates.Battle14A.X", 0);
		config.addDefault("Safari.Coordinates.Battle14A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle14A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle14A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle14A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle14B.X", 0);
		config.addDefault("Safari.Coordinates.Battle14B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle14B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle14B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle14B.Yaw", 0);
		//BattlePos15 Coordinates	
		config.addDefault("Safari.Coordinates.Battle15A.X", 0);
		config.addDefault("Safari.Coordinates.Battle15A.Y", 65);
		config.addDefault("Safari.Coordinates.Battle15A.Z", 0);
		config.addDefault("Safari.Coordinates.Battle15A.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle15A.Yaw", 0);
		
		config.addDefault("Safari.Coordinates.Battle15B.X", 0);
		config.addDefault("Safari.Coordinates.Battle15B.Y", 65);
		config.addDefault("Safari.Coordinates.Battle15B.Z", 0);
		config.addDefault("Safari.Coordinates.Battle15B.Pitch", 0);
		config.addDefault("Safari.Coordinates.Battle15B.Yaw", 0);
		
		
		config.options().copyDefaults(true);
		saveConfig();
		
		//Initialize Variables
		serverDirectory = getDataFolder().getParent();
		lobbyCoords = getLocation(EnumStages.Lobby);
		usePointSystem = config.getBoolean("Safari.Options.PointSystem");
		totalBattleRounds = config.getInt("Safari.Options.BattleRounds");
		
		//Start at beginning
		ScoreboardListener.init();
		setStage(EnumStages.Initialize);
		
		
		//Changes the phase when the timer runs down (on tick listener)
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run()
			{
				//Draw scoreboard with timer
				if(Bukkit.getOnlinePlayers().length>0) {
					ScoreboardListener.draw(getCurrentStage().getName(), getRemainingTimeInStage().getMinutes(), 
							getRemainingTimeInStage().getSeconds());
				}
				
				//@ 5 seconds battles end, @ 4 seconds pokes are healed, @ 2 seconds players are teleported.
				if(finishedBattles/2 == Battle.getTeamSize() && betweenRounds == false && getCurrentStage() == EnumStages.Battle)
				{
					finishedBattles=0;
					Battle.endRound(); //end the round early if everyone is finished
				}
				else if(betweenRounds && (getRemainingTimeInStage().getSeconds()<=10)) {
					Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+"Round "+Battle.getRound()+" Starting in "+getRemainingTimeInStage().getSeconds()+" seconds");
					if(getRemainingTimeInStage().getSeconds() == 4) {
				    	StageBase.healAllPokes();
				    } 
				    else if(getRemainingTimeInStage().getSeconds() == 2) {
				    	betweenRounds = false;
				    	Bukkit.broadcastMessage(ChatColor.RED+"FIGHT!");
				    	Battle.startRound();
				    }
				    StageBase.delay(1);
				}
				else if(getRemainingTimeInStage().getMinutes() == 0 && getRemainingTimeInStage().getSeconds() <= 2) 
				{
					if(getCurrentStage().getName() == "Lobby" && Bukkit.getOnlinePlayers().length<=3) 
					{
						int numRealPlayers = 0;
						for (Player p: Bukkit.getOnlinePlayers()) 
						{
							if(!p.hasPermission("SafariPlugin.exempt"))
								numRealPlayers+=1;
						}
						if(numRealPlayers < 4) 
						{
							extendLobbyTime(); //extend time if there is <= 3 players
						}
					}
					else if(getCurrentStage().name() == "Battle") {
						Battle.endRound(); //handles stage swapping in battle due to rounds
					}
					else	
						setStage(getCurrentStage().next());
				}
				//Announcements every 30 seconds (replaced by timer)
/*				else if((getRemainingTimeInStage().getMinutes()*60 + getRemainingTimeInStage().getSeconds())%30 == 0) 
				{
					if( getCurrentStage() == EnumStages.Battle )
						Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+getRemainingTimeInStage().getMinutes()+"m "+getRemainingTimeInStage().getSeconds()+"s left in Round "+Battle.getRound());
					else
						Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+getRemainingTimeInStage().getMinutes()+"m "+getRemainingTimeInStage().getSeconds()+"s left in "+getCurrentStage().getName()+" Phase");
				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
				else if((getRemainingTimeInStage().getMinutes() == 0 && getRemainingTimeInStage().getSeconds() == 3) && getCurrentStage().getName() == "Finish") {
					Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.RED+"Restarting Safari Games");
				    StageBase.delay(3);
				}
			
			}
		}, 1, 1);
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Safari Plugin has been disabled!");
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		//resets safari and starts at lobby stage
		if(cmd.getName().equalsIgnoreCase("resetsafari") && sender.hasPermission("SafariPlugin.reset")) {
			setStage(EnumStages.Lobby);
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("stuck") && sender.hasPermission("SafariPlugin.stuck")) {
			StageBase.teleportPlayerTo((Player) sender, getCurrentStage());
		}
		else if(cmd.getName().equalsIgnoreCase("timeleft")) {
			sender.sendMessage(getRemainingTimeInStage().getMinutes()+"m "+getRemainingTimeInStage().getSeconds()+"s Remaining");
			return true;
		}
		//teleports player to biome args[0]
		else if(cmd.getName().equalsIgnoreCase("biome") && sender.hasPermission("SafariPlugin.biome")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can use this command");
				return true;
			}
			else if(args.length != 1) {
				sender.sendMessage("Possible biomes: Plains, Hills, Ocean, Desert, Swamp, Forest, Jungle, Snow");
				return true;
			}
			
			Player s = (Player) sender;
			//convert first char of biome to upper case
			String biomeName = Character.toUpperCase(args[0].charAt(0))+args[0].substring(1);
			s.teleport(EnumBiomes.valueOf(biomeName).getLocation());
			return true;
			
		}
		//Setpos command - sets coordinates of argument in config
		else if(cmd.getName().equalsIgnoreCase("setpos") && sender.hasPermission("SafariPlugin.setpos")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can use this command");
				return true;
			}
			else if( args.length < 1 || args.length > 2) {
				sender.sendMessage("Usage: /setpos <place>");
				return true;
			}
			
			Player s = (Player) sender;
			saveLocation(args[0],s.getLocation());
			saveConfig();
			sender.sendMessage(args[0]+" has been set to your coordinates.");
			return true;
		}
		//teleports sender or target to the lobby coordinates
		else if(cmd.getName().equalsIgnoreCase("lobby") && sender.hasPermission("SafariPlugin.lobby")) {
			lobbyCoords = getLocation(EnumStages.Lobby);
			if( args.length < 1 && sender instanceof Player ) {
				Player s = (Player) sender;
				s.teleport(lobbyCoords);
			}
			else if( args.length == 1 ) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage("Player " + args[0] + " is not online.");
					return true;
				}
				target.teleport(lobbyCoords);
				sender.sendMessage(args[0]+" has been teleported to the lobby.");
			}
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("safarireload")) {
			if (!(sender instanceof Player)) {
				this.reloadConfig();
				this.saveConfig();
				Bukkit.getLogger().info("[SafariPlugin] Config Reloaded! ");
				return true;
			} 
			else if (sender.hasPermission("SafariPlugin.reload")) {
				this.reloadConfig();
				this.saveConfig();
				sender.sendMessage(ChatColor.GREEN+"[SafariPlugin] Config reloaded! ");
				System.out.println("[SafariPlugin] Config reloaded! ");
				return true;
			} 
			else {
				return false;
			}
		}
		return false; 
	}
	
	public void saveLocation(String place, Location playerLoc) {
		String prefix = "Safari.Coordinates.";
		
		config.set(prefix+place+".X", playerLoc.getX());
		config.set(prefix+place+".Y", playerLoc.getY());
		config.set(prefix+place+".Z", playerLoc.getZ());
		config.set(prefix+place+".Pitch", playerLoc.getPitch());
		config.set(prefix+place+".Yaw", playerLoc.getYaw());
		saveConfig();
	}
	

	public static Location getLocation(EnumStages stage) {
		String place = stage.getName();
		Location loc;
		try {
			Double x = config.getDouble("Safari.Coordinates."+place+".X");
			Double y = config.getDouble("Safari.Coordinates."+place+".Y");
			Double z = config.getDouble("Safari.Coordinates."+place+".Z");
			Float pitch = (float) config.getDouble("Safari.Coordinates."+place+".Pitch");
			Float yaw = (float) config.getDouble("Safari.Coordinates."+place+".Yaw");
			loc = new Location(Bukkit.getWorld("world"), x, y, z);
			loc.setPitch(pitch);
			loc.setYaw(yaw);
						
			
		} catch(Exception e) {
			Logger.getLogger("Minecraft").info(e+" Error Caught While Retrieving "+place+" Coordinates");
			loc = new Location(Bukkit.getWorld("world"), 0, 65, 0);
		}
		
		return loc;
	}
	
	public static void setSpawnLocation(EnumStages stage) {
		String place = stage.getName();
		try {
			int x = (int) config.getDouble("Safari.Coordinates."+place+".X");
			int y = (int) config.getDouble("Safari.Coordinates."+place+".Y");
			int z = (int) config.getDouble("Safari.Coordinates."+place+".Z");
			Bukkit.getServer().getWorld("world").setSpawnLocation(x, y, z);
			
		} catch(Exception e) {
			Logger.getLogger("Minecraft").info(e+" Error Caught While Setting Spawn to "+place);
			return;
		}
		
	}
	


	
	@SuppressWarnings("deprecation")
	@EventHandler
	public static void setMOTD(ServerListPingEvent event) {
        //DateFormat formatter = SimpleDateFormat.getTimeInstance();
        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        //SimpleDateFormat ft = new SimpleDateFormat("mm:ss");
		
		
		String codeRed = "\u00A7c"; // In-game
		String codeCyan = "\u00A7b"; //Time
		String codeGreen = "\u00A7a"; //Join Now!
		String div = "\u00A76"; //Color for dividing line (orange)
		String codePink = "\u00A7d"; //Stage name
		
		
		if(getCurrentStage().getName() == "Initialize") {
			event.setMotd(codeGreen+"Preparing world for the next game");
		}
		else if(getCurrentStage().getName() == "Lobby") {
			event.setMotd(codeGreen+"Join Now! "+div+"|"+codePink+" Lobby Phase "+div+"| "+codeCyan+getRemainingTimeInStage().getMinutes()+"m "+getRemainingTimeInStage().getSeconds()+"s Remaining");
		}
		else if(getCurrentStage().getName() == "Capture" && getRemainingTimeInStage().getMinutes() >= 10) {
			event.setMotd(codeGreen+"Join Now! "+div+"|"+codePink+" Capture Phase "+div+"| "+codeCyan+getRemainingTimeInStage().getMinutes()+"m "+getRemainingTimeInStage().getSeconds()+"s Remaining");
		}
		else if(getCurrentStage().getName() == "Capture" && getRemainingTimeInStage().getMinutes() < 10) {
			event.setMotd(codeRed+"In-Game "+div+"|"+codePink+" Capture Phase "+div+"| "+codeCyan+getRemainingTimeInStage().getMinutes()+"m "+getRemainingTimeInStage().getSeconds()+"s Remaining");
		}
		else if(getCurrentStage().getName() == "Preparation") {
			event.setMotd(codeRed+"In-Game "+div+"|"+codePink+" Preparation Phase "+div+"| "+codeCyan+getRemainingTimeInStage().getMinutes()+"m "+getRemainingTimeInStage().getSeconds()+"s Remaining");
		}
		else if(getCurrentStage().getName() == "Battle" || getCurrentStage().getName() == "Finish" ) {
			event.setMotd(codeRed+"In-Game "+div+"|"+codePink+" Battle Phase Round "+Battle.getRound()+div+" | "+codeCyan+getRemainingTimeInStage().getMinutes()+"m "+getRemainingTimeInStage().getSeconds()+"s Remaining");
		}

	}
	
	//Returns time remaining in current stage
	public static Date getRemainingTimeInStage() {
		Date currentTime = new Date();
		long milliDelta =  timeLeftInStage.getTime() - currentTime.getTime();
		Date dateDelta = new Date(Math.abs(milliDelta));
		return dateDelta;
	}
	
	public static EnumStages getCurrentStage() {
		return currentStage;
	}
	
	//Changes the stage
	public static void setStage(EnumStages s) {
		setSpawnLocation(s);
		currentStage = s;
		
		if(s.getName() == "Initialize") {
			timeLeftInStage = DateUtils.addSeconds(new Date(), 15);
			currentPlayers.clear(); //clears player list
			Initialize.init();
		}
		else if(s.getName() == "Lobby") {
			timeLeftInStage = DateUtils.addMinutes(new Date(), s.getPhaseTime());
			Lobby.init();
		}
		else if(s.getName() == "Capture") {
			timeLeftInStage = DateUtils.addMinutes(new Date(), s.getPhaseTime());
			for(Player p:Bukkit.getOnlinePlayers()) 
			{
				addPlayerToGame(p);
			}
			Capture.init();
		}
		else if(s.getName() == "Preparation") {
			Preparation.init();
			timeLeftInStage = DateUtils.addMinutes(new Date(), s.getPhaseTime());
		}
		else if(s.getName() == "Battle") {
			StageBase.clearAllInventory();
			Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.LIGHT_PURPLE+"Battle phase has started!");
			Battle.init();
		}
		else if(s.getName() == "Finish") {
			Finish.init();
			timeLeftInStage = DateUtils.addSeconds(new Date(), 30);
		}
	

	}
	
	public static void extendLobbyTime() {
		Bukkit.broadcastMessage(ChatColor.AQUA+"[Safari] "+ChatColor.RED+"Lobby Time Extended 4 Minutes");
		Bukkit.getLogger().info("[SafariPlugin] Lobby Time Extended by 4 minutes");
		timeLeftInStage = DateUtils.addMinutes(new Date(), 4);
	    
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//SAFARIMOD ASSETS
	
	public static void addSpectator(String pName) {
		StageBase.addSpectator(pName);
		
	}
	
	public static String getOpponent(String player) {
		return StageBase.getOpponent(player);
	}
	
	public static void addPoints(String pName, int amt) {
		StageBase.addPoints(pName, amt);
	}
	
	public static boolean isCompetitor(String pName) {
		return Battle.isCompetitor(pName);
	}
	
	public static void removeCompetitor(String pName) {
		Battle.removeCompetitor(pName);
	}
	
	public static void addPlayerToGame(Player p) {
		currentPlayers.add(p.getName());
	}
	
	public static boolean isInGame(Player player) {
		return currentPlayers.contains(player.getName());
	}
	
	//checks if the player has the permission needed to change his starter.
	public static boolean hasStarterPermission(String pName, String pokemon) {
		Player player = Bukkit.getPlayer(pName);
		
		if(getCurrentStage() != EnumStages.Lobby) {
			player.sendMessage("This command can only be used during the Lobby phase");
			return false;
		}
		
		if(player.hasPermission("SafariPlugin.starter."+pokemon.toLowerCase())) {
			if(changedStarter.contains(pName)) {
				player.sendMessage("You have used this command once already this round.");
				return false;
			}
			else {
				changedStarter.add(pName);
				return true;
			}
		}
		else {
			player.sendMessage("You do not have permission for this command.");
			return false;
		}
		
	}

}