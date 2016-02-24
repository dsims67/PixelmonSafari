package safari.listeners;

import java.lang.reflect.Field;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ModLoader;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import pixelmon.api.events.EventType;
import pixelmon.api.events.IPixelmonEventHandler;
import pixelmon.battles.BattleRegistry;
import pixelmon.config.PixelmonEntityList;
import pixelmon.entities.pixelmon.EntityPixelmon;
import pixelmon.storage.PixelmonStorage;
import safari.Safari;
//import savior67.safariplugin.SafariPlugin;
//import savior67.safariplugin.enums.EnumStages;
//import savior67.safariplugin.stages.Battle;
//import savior67.safariplugin.stages.Spectate;
//import savior67.safariplugin.stages.StageBase;

public class BattleListener implements IPixelmonEventHandler {
	
	private static Object StageBase;

	//Listener for PlayerBattleEnded event
	@Override
	public void eventFired(EventType eventType, EntityPlayer player,
			Object... data) 
	{
		
		String currentStage = "Initialize";
		try {
			Object EnumStages = Safari.getSafariPlugin().getClass().getField("currentStage").get(Safari.getSafariPlugin());
			currentStage = (String) EnumStages.getClass().getMethod("getName").invoke(EnumStages);
		}
		catch (Exception e){
			FMLLog.severe("%s Something went seriously wrong with the reflection in "
					+ "the eventFired()-1 method in BattleListener.\n"
					+ "The Exception Message: %s\nThe Cause: %s",
					"[SafariMod]", e.getMessage(), e.getCause());
			e.printStackTrace();
		}
		
/*		try {
			Field field = Safari.getSafariPlugin().getClass().getField("stageBase");//.get(Safari.getSafariPlugin());
			field.setAccessible(true);
			StageBase = field.get(Safari.getSafariPlugin());
		}
		catch (Exception e){
			FMLLog.severe("%s Something went seriously wrong with the reflection in "
					+ "the eventFired()-2 method in BattleListener.\n"
					+ "The Exception Message: %s\nThe Cause: %s",
					"[SafariPlugin]", e.getMessage(), e.getCause());
			e.printStackTrace();
		} 
*/
		
		
		if ((eventType == EventType.PlayerBattleEnded || eventType == EventType.PlayerBattleEndedAbnormal) && currentStage == "Battle") 
		{
			//Logger.getLogger("Minecraft").info("A Player Battle Has Ended!");
			String pName = null;
			try {
				pName = player.getCommandSenderName();
			} catch (Exception e) {
				FMLLog.severe("BattleListener:EventFired; Player name could not be obtained!");
			}
			
			//if Player left in the middle of a battle.
			if(pName == null || MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(pName) == null)
				return;
			
			try {
				Safari.getSafariPlugin().getClass().getMethod("addSpectator", String.class).setAccessible(true);
				Safari.getSafariPlugin().getClass().getMethod("addSpectator", String.class).invoke(null, new Object[] {pName});
			}
			catch (Exception e){
				FMLLog.severe("%s Something went seriously wrong with the reflection in "
						+ "the eventFired()-3 method in BattleListener.\n"
						+ "The Exception Message: %s\nThe Cause: %s",
						"[SafariMod]", e.getMessage(), e.getCause());
				e.printStackTrace();
			}
			
				determineWinner(pName);
			
			
		}

		
	}
	
	//Determines the winner
		public static void determineWinner(String p1) {
			String p2 = p1;
			String winner = "default";
			String loser = "default";
			int winnersPoints = 0;
			int losersPoints = 0;
			try {
				Safari.getSafariPlugin().getClass().getMethod("getOpponent", String.class).setAccessible(true);
				Safari.getSafariPlugin().getClass().getMethod("isCompetitor", String.class).setAccessible(true);
				Safari.getSafariPlugin().getClass().getMethod("removeCompetitor", String.class).setAccessible(true);
				Safari.getSafariPlugin().getClass().getMethod("addPoints", new Class[] {String.class, Integer.TYPE}).setAccessible(true);
				
				p2 = ((String) Safari.getSafariPlugin().getClass().getMethod("getOpponent", String.class).invoke(Safari.getSafariPlugin(), new Object[] {p1}));
				boolean isCompP1 = (Boolean) (Safari.getSafariPlugin().getClass().getMethod("isCompetitor", String.class).invoke(Safari.getSafariPlugin(), new Object[] {p1}));
				boolean isCompP2 = (Boolean) (Safari.getSafariPlugin().getClass().getMethod("isCompetitor", String.class).invoke(Safari.getSafariPlugin(), new Object[] {p2}));
				
				EntityPlayerMP p1Entity = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(p1);
				EntityPlayerMP p2Entity = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(p2);
				if(p2 == p1) 
					return;
				else if(isCompP2 == false || isCompP1 == false)
					return;
				else if(getPointsFromHealth(p1)>=getPointsFromHealth(p2)) {
					winner = p1;
					loser = p2;
					winnersPoints = getPointsFromHealth(p1)+2;
					losersPoints = 6 - getPointsFromHealth(p1);
					announceWinner(p1, p2);
				}
				else {
					winner = p2;
					loser = p1;
					winnersPoints = getPointsFromHealth(p2)+2;
					losersPoints = 6 - getPointsFromHealth(p2);
					announceWinner(p2, p1);
				}
				
				
				Safari.getSafariPlugin().getClass().getMethod("removeCompetitor", String.class).invoke(Safari.getSafariPlugin(), new Object[] {p1});
				Safari.getSafariPlugin().getClass().getMethod("removeCompetitor", String.class).invoke(Safari.getSafariPlugin(), new Object[] {p2});
				
				
				if( winnersPoints<4 ) winnersPoints = 4;
				if(losersPoints>4) losersPoints = 4;
				
				Safari.getSafariPlugin().getClass().getMethod("addPoints", new Class[] {String.class, Integer.TYPE}).invoke(null, new Object[] {winner, winnersPoints});
				Safari.getSafariPlugin().getClass().getMethod("addPoints", new Class[] {String.class, Integer.TYPE}).invoke(null, new Object[] {loser, losersPoints});
			}
			catch (Exception e){
				FMLLog.severe("%s Something went seriously wrong with the reflection in "
						+ "the determineWinner() method in BattleListener.\n"
						+ "The Exception Message: %s\nThe Cause: %s",
						"[SafariMod]", e.getMessage(), e.getCause());
				e.printStackTrace();
			}
			

			//StageBase.addPoints(p1.username, getPointsFromHealth(p1));
			//StageBase.addPoints(p2.username, getPointsFromHealth(p2));
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(
		            ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.announcement", new Object[]
		                    {
		            			EnumChatFormatting.AQUA + "Safari" + EnumChatFormatting.RESET,
		                        EnumChatFormatting.GREEN + winner + " has earned "+Integer.toString(winnersPoints)+" points, "+loser+" has earned "+Integer.toString(losersPoints)+" points!"
		                    }));
			//Bukkit.broadcastMessage(ChatColor.GREEN+p1.getDisplayName()+" has earned "+getPointsFromHealth(p1)+" points,"+p2.getDisplayName()+" has earned "+getPointsFromHealth(p2)+" points!");
			//if the other person has already won, don't repeat
			//calculate total health of pokes for both players and current health
			//for every 1/5th of total health taken from the opponent, that player earns 1 point 
			//5 points to win total, bonus of 2

		}
		
		private static int getPointsFromHealth(String p1) {
			
			EntityPlayerMP p1Entity = null;
			try {
				p1Entity = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(p1);
			}
			catch (Exception e){
				FMLLog.severe("%s Error getting EntityPlayer from server "
						+ "in BattleListener:getPointsFromHealth\n"
						+ "The Exception Message: %s\nThe Cause: %s",
						"[SafariMod]", e.getMessage(), e.getCause());
				e.printStackTrace();
			}

			if (p1Entity == null) 
				return 0;
			else
				return (int) (((getCurrentHealthOfParty(p1Entity)/getMaxHealthOfParty(p1Entity))*6)+0.5);
		}

		public static double getMaxHealthOfParty(EntityPlayerMP p) {
			int totalMaxHealth = 0;
			try {
				for(NBTTagCompound n:PixelmonStorage.PokeballManager.getPlayerStorage(p).getList()) {
					if(n != null)
						totalMaxHealth += n.getInteger("StatsHP");
					//EntityPixelmon e = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(n, p.getEntityWorld());
					//totalHealth += e.getMaxHealth();
				}
				//PixelmonStorage.ComputerManager.getPlayerStorage(player).addToComputer(p);
			} catch(Exception e) {
				FMLLog.severe("[SafariMod] Error retrieving total health in battle");
				return totalMaxHealth;
			}
			return totalMaxHealth;		
		}
		
		//announces who won/lost the battle
		private static void announceWinner(String winner, String loser) {
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(
		            ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.announcement", new Object[]
		                    {
		                        EnumChatFormatting.AQUA + "Safari" + EnumChatFormatting.RESET,
		                        EnumChatFormatting.LIGHT_PURPLE + winner + " has defeated " + loser
		                    }));
		}

		public static double getCurrentHealthOfParty(EntityPlayerMP p) {
			
			int totalCurrHealth = 0;
			try {
				for(NBTTagCompound n:PixelmonStorage.PokeballManager.getPlayerStorage(p).getList()) {
					if(n != null)
						totalCurrHealth += n.getShort("Health");
					//EntityPixelmon e = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(n, p.getEntityWorld());
					//totalHealth += e.getHealth();
				}
				//PixelmonStorage.ComputerManager.getPlayerStorage(player).addToComputer(p);
			} catch(Exception e) {
				FMLLog.severe("[SafariMod] Error retrieving current health in battle");
				return totalCurrHealth;
			}
			return totalCurrHealth;
		}
		

}
