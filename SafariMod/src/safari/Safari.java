package safari;


import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import pixelmon.api.events.PixelmonEventHandler;
import pixelmon.battles.BattleTickHandler;
import pixelmon.client.ClientProxy;
import pixelmon.client.comm.ClientPacketHandler;
import pixelmon.client.gui.GuiChatOverlay;
import pixelmon.comm.ConnectionHandler;
import pixelmon.comm.PacketManager;
import pixelmon.comm.PixelmonPlayerTracker;
import pixelmon.commands.Spawn;
import safari.commands.CommandStarter;
import safari.listeners.BattleListener;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IFMLSidedHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "safari", name = "Safari", version = "3.0.2", dependencies = "required-after:pixelmon")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class Safari {

	@Instance("safari")
	public static Safari instance;
	
	public static MinecraftServer MCserver;

	public static StringTranslate stringtranslate = new StringTranslate();

	@SidedProxy(clientSide = "safari.CommonProxy", serverSide = "safari.CommonProxy")
	public static CommonProxy proxy;

	private static boolean preInit = false, init = false, postInit = false;

	public static File modDirectory;

	Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		AddMeta(event, "3.0.2");
		instance = this;
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		modDirectory = new File(event.getModConfigurationDirectory().getParent());


		
		//if (!Loader.isModLoaded("Pixelmon"))
		//	System.exit(1);
	
		// Achievements
		preInit = true;
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerKeyBindings();
		proxy.registerRenderers();
		proxy.registerInteractions();
		proxy.registerTickHandlers();
		
		
		PixelmonEventHandler.registerEventHandler(new BattleListener());
		//MinecraftForge.EVENT_BUS.register(new BattleListener());
		init = true;
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.registerSounds();
		postInit = true;
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		
		((ServerCommandManager) MinecraftServer.getServer().getCommandManager()).registerCommand(new CommandStarter());

		//ServerStorage.init();
		// for(WorldServer world : event.getServer().worldServers){
		// MasterAwareness.init(world);
		// }
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Block {
		/**
		 * The block's name
		 */
		String name();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Item {
		/**
		 * The name of the item
		 */
		String name();

		String modId() default "pixelmon";
	}

	/**
	 * whether or not Pixelmon has finished the
	 * {@link #preInit(FMLPreInitializationEvent) preInit} phase.
	 */
	public static boolean preInitialized() {
		return preInit;
	}

	/**
	 * whether or not Pixelmon has finished the
	 * {@link #load(FMLInitializationEvent) load} phase.
	 */
	public static boolean initialized() {
		return init;
	}

	/**
	 * whether or not Pixelmon has finished the
	 * {@link #modsLoaded(FMLPostInitializationEvent) modsLoaded} phase.
	 */
	public static boolean postInitialized() {
		return postInit;
	}

	/**
	 * Add Pack Meta Data
	 */
	private void AddMeta(FMLPreInitializationEvent event, String version) {

		ModMetadata m = event.getModMetadata(); // This is required or it will
												// not work
		m.autogenerated = false; // This is required otherwise it will not work
		m.modId = "safari";
		m.version = version;
		m.name = "Safari";
		m.url = "http://www.fireredmc.com";
		m.updateUrl = "http://www.fireredmc.com";
		m.description = "Pokemon Safari, in Minecraft!";
		m.authorList.add("savior67");

	}
	
	public static Object getSafariPlugin() {
		try
		{
			Class clazz;
			Method method;
			Object object;
			Field field;
			List list;
			Map map;
			clazz = Class.forName("org.bukkit.Bukkit");                     //get  Bukkit.java
			method = clazz.getMethod("getServer");                          //get  getServer() :  Bukkit.java
			object = method.invoke(null);                                   //call getServer() :  Bukkit.java     
			method = object.getClass().getMethod("getPluginManager");       //get  getPluginManager() : Server.java
			object = method.invoke(object);                                 //call getPluginManager() : Server.java
			method = object.getClass().getMethod("getPlugin", String.class);//get  getPlugin(String)  : PluginManager.java
			object = method.invoke(object, "SafariPlugin");                     //call getPlugin(String) : PluginManager
			//field = object.getClass().getField("instances");                //get  instances :   CraftIRC.java
			//field.setAccessible(true);                                      //Make instances public
			return object;
		}
		catch (Exception e)
		{
			FMLLog.severe("%s Something went seriously wrong with the reflection in "
					+ "the runSafarPluginMethod() method in Safari.\n"
					+ "The Exception Message: %s\nThe Cause: %s",
					"[SafariPlugin]", e.getMessage(), e.getCause());
			e.printStackTrace();
			return null;
		}
		

	}

}

