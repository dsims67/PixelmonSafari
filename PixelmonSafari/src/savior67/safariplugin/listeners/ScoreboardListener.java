package savior67.safariplugin.listeners;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;



//not a real listener, called from tickhandler in SafariPlugin
public class ScoreboardListener {
	
	private static Objective obj;
	public static Objective timerObj;
	public static Scoreboard sb;
	
	public static void init() {
		sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		obj = sb.registerNewObjective("timer", "dummy"); //Registering the objective needed for the timer
		obj.setDisplayName(ChatColor.RED + "Initialize Stage");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		timerObj = obj;
		
	}
	
	public static void draw(String phaseName, int min, int sec ) {
		if(phaseName == "Battle")
			phaseName = ChatColor.RED+phaseName+" Phase";
		else if(phaseName == "Capture")
			phaseName = ChatColor.AQUA+phaseName+" Phase";
		else if(phaseName == "Lobby")
			phaseName = ChatColor.YELLOW+phaseName+" Phase";
		else if(phaseName == "Finish")
			phaseName = ChatColor.GREEN+"Come again soon!";
		else if(phaseName == "Preparation")
			phaseName = ChatColor.WHITE+"Prep Phase";
		
		timerObj.setDisplayName(phaseName);
		 //Making a offline player called "Time:" with a green name and adding it to the scoreboard
		String unit;
		if (min>0) { 
			sb.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Time Left(s):"));
			unit = "m"; 
		}
		else {
			sb.resetScores(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Time Left(m):"));
			unit = "s";
		}

		final Score score = timerObj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Time Left("+unit+"):"));

		if(min>0)
			score.setScore(min);
		else
			score.setScore(sec);
	
	}
	

}
