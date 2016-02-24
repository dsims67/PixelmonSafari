package savior67.safariplugin.enums;

import savior67.safariplugin.SafariPlugin;

public enum EnumStages 
{
	Initialize("Initialize"),
	Lobby("Lobby"),
	Capture("Capture"),
	Preparation("Preparation"),
	Battle("Battle"),
	Spectate("Spectate"),
	Finish("Finish");
	
	private String name;
	//SafariPlugin plugin;
	private int phaseTime;
	
	private EnumStages(String s) 
	{
		name = s;
		if( name != "Spectate" && name != "Finish")
			phaseTime = SafariPlugin.config.getInt("Safari.Phase_Time."+this.name);
	}
	
	public String getName() 
	{
		return name;
	}
	
	//Returns the length in minutes of the phase, from the config
	public int getPhaseTime()
	{
		return phaseTime;
	}
	
	public EnumStages next() 
	{
		if(this.getName() == "Initialize")
			return EnumStages.Lobby;
		else if(this.getName()=="Lobby")
			return EnumStages.Capture;
		else if(this.getName()=="Capture")
			return EnumStages.Preparation;
		else if(this.getName()=="Preparation")
			return EnumStages.Battle;
		else if(this.getName()=="Battle")
			return EnumStages.Finish;
		else
			return EnumStages.Initialize;
		
	}

}
