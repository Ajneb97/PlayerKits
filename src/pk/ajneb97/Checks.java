package pk.ajneb97;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import pk.ajneb97.otros.MensajesUtils;

public class Checks {

	public static boolean checkTodo(PlayerKits plugin,CommandSender jugador){
		FileConfiguration config = plugin.getConfig();
		String nombre = config.getString("Messages.prefix");
		String mensaje = nombre+config.getString("Messages.materialNameError");
		
		//Check config.yml
		if(config.contains("Config.Inventory")) {
			for(String key : config.getConfigurationSection("Config.Inventory").getKeys(false)) {
				if(!comprobarMaterial(config.getString("Config.Inventory."+key+".id"),jugador,mensaje)){
					return false;
				}	
			}
		}
		
			
		return true;
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	public static boolean comprobarMaterial(String key,CommandSender jugador,String mensaje){
		   try{
			   if(key.contains(":")){
					  String[] idsplit = key.split(":");
					  String stringDataValue = idsplit[1];
					  short DataValue = Short.valueOf(stringDataValue);
					  Material mat = Material.getMaterial(idsplit[0].toUpperCase()); 
					  ItemStack item = new ItemStack(mat,1,(short)DataValue); 
			   }else {
				   ItemStack item = new ItemStack(Material.getMaterial(key),1);
			   }
			   
			   return true;
		   }catch(Exception e){
			   jugador.sendMessage(MensajesUtils.getMensajeColor(mensaje.replace("%material%", key)));
			   return false;
		   }
	}
	
					
		public static void modificarPath(FileConfiguration config,String path,String idNueva) {
			if(config.contains(path)) {
				config.set(path, idNueva);
			}
		}
}
