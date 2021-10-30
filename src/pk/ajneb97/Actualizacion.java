package pk.ajneb97;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Actualizacion implements Listener{

	private PlayerKits plugin;
	public Actualizacion(PlayerKits plugin){		
		this.plugin = plugin;		
	}
	@EventHandler
	public void Join(PlayerJoinEvent event){
		Player jugador = event.getPlayer();	
		FileConfiguration config = plugin.getConfig();
		if(config.getString("Config.update_notify").equals("true")) {
			if(jugador.isOp() && !(plugin.version.equals(plugin.latestversion))){
				jugador.sendMessage(PlayerKits.nombrePlugin + ChatColor.RED +" There is a new version available. "+ChatColor.YELLOW+
		  				  "("+ChatColor.GRAY+plugin.latestversion+ChatColor.YELLOW+")");
		  		    jugador.sendMessage(ChatColor.RED+"You can download it at: "+ChatColor.GREEN+"https://www.spigotmc.org/resources/75185/");			 
			}
		}
	}
}
