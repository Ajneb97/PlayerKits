package pk.ajneb97.otros;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import pk.ajneb97.managers.JugadorManager;
import pk.ajneb97.versiones.V1_10;
import pk.ajneb97.versiones.V1_11;
import pk.ajneb97.versiones.V1_12;
import pk.ajneb97.versiones.V1_13;
import pk.ajneb97.versiones.V1_13_R2;
import pk.ajneb97.versiones.V1_14;
import pk.ajneb97.versiones.V1_15;
import pk.ajneb97.versiones.V1_16;
import pk.ajneb97.versiones.V1_16_R2;
import pk.ajneb97.versiones.V1_16_R3;
import pk.ajneb97.versiones.V1_17;
import pk.ajneb97.versiones.V1_18_R1;
import pk.ajneb97.versiones.V1_18_R2;
import pk.ajneb97.versiones.V1_19_R1;
import pk.ajneb97.versiones.V1_8_R1;
import pk.ajneb97.versiones.V1_8_R2;
import pk.ajneb97.versiones.V1_8_R3;
import pk.ajneb97.versiones.V1_9_R1;
import pk.ajneb97.versiones.V1_9_R2;

public class Utilidades {
	
	public static boolean isLegacy() {
		if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") ||
				Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")
				|| Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
				|| Bukkit.getVersion().contains("1.19")) {
			return false;
		}else {
			return true;
		}
	}
	
	public static boolean isNew() {
		if(Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17") || Bukkit.getVersion().contains("1.18")
				|| Bukkit.getVersion().contains("1.19")) {
			return true;
		}else {
			return false;
		}
	}
	
	public static String getCooldown(String kit,Player jugador,FileConfiguration kitConfig,FileConfiguration config,JugadorManager jManager){
		//1000millis claimea un kit de 5 segundos
		//6000millis puede claimearlo otra vez (timecooldown)
		
    	long timecooldown = jManager.getCooldown(jugador, kit);
		
    	long millis = System.currentTimeMillis();
    	if(!kitConfig.contains("Kits."+kit+".cooldown")) {
    		return "no_existe";
    	}
    	String cooldownconfig = kitConfig.getString("Kits."+kit+".cooldown");
 	    long cooldown = Long.valueOf(cooldownconfig); 
 	    long cooldownmil = cooldown*1000;
 	    
 	    long espera = millis - timecooldown;
 	    long esperaDiv = espera/1000;
 	    long esperatotalseg = cooldown - esperaDiv;
 	    long esperatotalmin = esperatotalseg/60;
 	    long esperatotalhour = esperatotalmin/60;
 	    long esperatotalday = esperatotalhour/24;
 	    if(((timecooldown + cooldownmil) > millis) && (timecooldown != 0)){		    		
 		   if(esperatotalseg > 59){
 			   esperatotalseg = esperatotalseg - 60*esperatotalmin;
 		   }
 		   String time = esperatotalseg+config.getString("Messages.seconds");		    		
 		   if(esperatotalmin > 59){
 			   esperatotalmin = esperatotalmin - 60*esperatotalhour;
 		   }	
 		   if(esperatotalmin > 0){
 			   time = esperatotalmin+config.getString("Messages.minutes")+" "+time;
 		   }
 		   if(esperatotalhour > 24) {
 			  esperatotalhour = esperatotalhour - 24*esperatotalday;
 		   }
 		   if(esperatotalhour > 0){
 			   time = esperatotalhour+ config.getString("Messages.hours")+" " + time;
 		   }
 		   if(esperatotalday > 0) {
 			  time = esperatotalday+ config.getString("Messages.days")+" " + time;
 		   }
 		   
 		   return time;
 	    }else{
 	    	return "ready";
 	    }
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getItem(String id,int amount,String skulldata){
		  String[] idsplit = new String[2];
		  int DataValue = 0;
		  ItemStack stack = null;
		  if(id.contains(":")){
			  idsplit = id.split(":");
			  String stringDataValue = idsplit[1];
			  DataValue = Integer.valueOf(stringDataValue);
			  Material mat = Material.getMaterial(idsplit[0].toUpperCase()); 
			  stack = new ItemStack(mat,amount,(short)DataValue);             	  
		  }else{
			  Material mat = Material.getMaterial(id.toUpperCase());
			  stack = new ItemStack(mat,amount);  			  
		  }
		  if(!skulldata.isEmpty()) {
			  String[] sep = skulldata.split(";");
			  stack = Utilidades.setSkull(stack, sep[0], sep[1]);
		  }
		  
		  
		  return stack;
	}
	
	public static ItemStack getDisplayItem(FileConfiguration kits,String path) {
		ItemStack item = getItem(kits.getString(path+".display_item"),1,"");
		ItemMeta meta = item.getItemMeta();
		if(kits.contains(path+".display_name")) {
			meta.setDisplayName(MensajesUtils.getMensajeColor(kits.getString(path+".display_name")));
		}
		if(kits.contains(path+".display_lore")) {
			List<String> lore = kits.getStringList(path+".display_lore");
			for(int i=0;i<lore.size();i++) {
				lore.set(i, MensajesUtils.getMensajeColor(lore.get(i)));
			}
			meta.setLore(lore);
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		if(kits.contains(path+".display_item_leathercolor")) {
			LeatherArmorMeta meta2 = (LeatherArmorMeta) meta;
			int color = Integer.valueOf(kits.getString(path+".display_item_leathercolor"));
			meta2.setColor(Color.fromRGB(color));
			item.setItemMeta(meta2);
		}
		if(kits.contains(path+".display_item_custom_model_data")) {
			int customModelData = kits.getInt(path+".display_item_custom_model_data");
			meta = item.getItemMeta();
			meta.setCustomModelData(customModelData);
			item.setItemMeta(meta);
		}
		item = Utilidades.setUnbreakable(item);
		if(kits.contains(path+".display_item_skulldata")) {
			String[] skulldata = kits.getString(path+".display_item_skulldata").split(";");
			item = Utilidades.setSkull(item, skulldata[0], skulldata[1]);
		}
		
		return item;
	}
	
	public static int getSlotDisponible(FileConfiguration kitConfig,FileConfiguration config) {
		ArrayList<Integer> slotsOcupados = new ArrayList<Integer>();
		if(kitConfig.contains("Kits")) {
			for(String path : kitConfig.getConfigurationSection("Kits").getKeys(false)) {
				if(kitConfig.contains("Kits."+path+".slot")) {
					int slotOcupado = Integer.valueOf(kitConfig.getString("Kits."+path+".slot"));
					slotsOcupados.add(slotOcupado);
				}
			}
		}
		if(config.contains("Config.Inventory")) {
			for(String key : config.getConfigurationSection("Config.Inventory").getKeys(false)) {
				int slotOcupado = Integer.valueOf(key);
				slotsOcupados.add(slotOcupado);
			}
		}
		
		int slotsMaximos = Integer.valueOf(config.getString("Config.inventorySize"));
		for(int i=0;i<slotsMaximos;i++) {
			if(!slotsOcupados.contains(i)) {
				return i;
			}
		}
		return -1;
	}
	
	public static DyeColor getBannerColor(String mainColor) {
		String fixed = mainColor.replace("_BANNER", "");
		return DyeColor.valueOf(fixed);
	}
	
	public static void guardarSkullDisplay(ItemStack item, FileConfiguration config, String path) {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			u.guardarSkullDisplay(item,path,config);			
		}else if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			u.guardarSkullDisplay(item,path,config);			
		}else if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			u.guardarSkullDisplay(item,path,config);			
		}else 
		if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			u.guardarSkullDisplay(item,path,config);	
		}else 
		if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			u.guardarSkullDisplay(item,path,config);	
		}else 
		if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			u.guardarSkullDisplay(item,path,config);	
		}else 
		if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			u.guardarSkullDisplay(item,path,config);	
		}else 
		if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			u.guardarSkullDisplay(item,path,config);	
		}else 
		if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			u.guardarSkullDisplay(item,path,config);		
		}else 
		if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			u.guardarSkullDisplay(item,path,config);	
		}
	}

	public static void guardarSkull(ItemStack item, FileConfiguration config, String path, String nombreJugador) {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			u.guardarSkull(item,path,config,nombreJugador);		
		}else 
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			u.guardarSkull(item,path,config,nombreJugador);			
		}else
		if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			u.guardarSkull(item,path,config,nombreJugador);	
		}else
		if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			u.guardarSkull(item,path,config,nombreJugador);	
		}else
		if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			u.guardarSkull(item,path,config,nombreJugador);	
		}else
		if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			u.guardarSkull(item,path,config,nombreJugador);	
		}else
		if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			u.guardarSkull(item,path,config,nombreJugador);	
		}else
		if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			u.guardarSkull(item,path,config,nombreJugador);	
		}else
		if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			u.guardarSkull(item,path,config,nombreJugador);	
		}
	}

	public static void guardarAttributes(ItemStack item, FileConfiguration config, String path) {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			u.guardarAttributes(item,path,config);			
		}else
		if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			u.guardarAttributes(item,path,config);	
		}else
		if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			u.guardarAttributes(item,path,config);		
		}else
		if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			u.guardarAttributes(item,path,config);	
		}else
		if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			u.guardarAttributes(item,path,config);	
		}else
		if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			u.guardarAttributes(item,path,config);	
		}else
		if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			u.guardarAttributes(item,path,config);		
		}else
		if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			u.guardarAttributes(item,path,config);		
		}
		
	}
	
	public static void guardarNBT(ItemStack item, FileConfiguration config, String path) {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			u.guardarNBT(item,path,config);				
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			u.guardarNBT(item,path,config);				
		}else
		if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			u.guardarNBT(item,path,config);		
		}else
		if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			u.guardarNBT(item,path,config);			
		}else
		if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			u.guardarNBT(item,path,config);			
		}
		
	}
	
	public static  ItemStack setUnbreakable(ItemStack item){
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}
		else if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}
		else if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}
		else if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}
		else if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}
		else if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}
		else if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}
		else if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			ItemStack stack = u.setUnbreakable(item);			
			return stack;
		}
		else{
			return item;
		}		
	}
	
	public static boolean getUnbreakable(ItemStack item){
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else	
		if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}else
		if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			boolean bol = u.getUnbreakable(item);
			return bol;
		}
		
		return false;
	}
	
	public static ItemStack setSkull(ItemStack crafteos, String path, FileConfiguration config) {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}else
		if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			ItemStack stack = u.setSkull(crafteos,path,config);			
			return stack;
		}
		return null;
	}
	
	public static ItemStack setNBT(ItemStack item, FileConfiguration config, String key) {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			ItemStack stack = u.setNBT(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			ItemStack stack = u.setNBT(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			ItemStack stack = u.setNBT(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			ItemStack stack = u.setNBT(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			ItemStack stack = u.setNBT(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			ItemStack stack = u.setNBT(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			ItemStack stack = u.setNBT(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			ItemStack stack = u.setNBT(item,key,config);			
			return stack;
		}
		return null;
	}
	
	public static ItemStack setAttributes(ItemStack item, FileConfiguration config, String key) {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			ItemStack stack = u.setAttributes(item,key,config);		
			return stack;
		}else
		if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			ItemStack stack = u.setAttributes(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			ItemStack stack = u.setAttributes(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			ItemStack stack = u.setAttributes(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			ItemStack stack = u.setAttributes(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			ItemStack stack = u.setAttributes(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			ItemStack stack = u.setAttributes(item,key,config);				
			return stack;
		}else
		if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			ItemStack stack = u.setAttributes(item,key,config);			
			return stack;
		}
		return null;
	}
	
	public static ItemStack setSkull(ItemStack item, String id, String textura) {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		if(packageName.contains("1_19_R1")){
			V1_19_R1 u = new V1_19_R1();
			return u.setSkullSinID(item,textura);	
		}else
		if(packageName.contains("1_18_R2")){
			V1_18_R2 u = new V1_18_R2();
			return u.setSkullSinID(item,textura);	
		}else
		if(packageName.contains("1_18_R1")){
			V1_18_R1 u = new V1_18_R1();
			return u.setSkullSinID(item,textura);	
		}else
		if(packageName.contains("1_17_R1")){
			V1_17 u = new V1_17();
			return u.setSkullSinID(item,textura);	
		}else
		if(packageName.contains("1_16_R3")){
			V1_16_R3 u = new V1_16_R3();
			return u.setSkullSinID(item,textura);	
		}else
		if(packageName.contains("1_16_R2")){
			V1_16_R2 u = new V1_16_R2();
			return u.setSkullSinID(item,textura);	
		}else
		if(packageName.contains("1_16_R1")){
			V1_16 u = new V1_16();
			return u.setSkullSinID(item,textura);	
		}else
		if(packageName.contains("1_15_R1")){
			V1_15 u = new V1_15();
			return u.setSkull(item, id, textura);	
		}else
		if(packageName.contains("1_14_R1")){
			V1_14 u = new V1_14();
			return u.setSkull(item, id, textura);		
		}else
		if(packageName.contains("1_13_R2")){
			V1_13_R2 u = new V1_13_R2();
			return u.setSkull(item, id, textura);			
		}else
		if(packageName.contains("1_13_R1")){
			V1_13 u = new V1_13();
			return u.setSkull(item, id, textura);		
		}else
		if(packageName.contains("1_12_R1")){
			V1_12 u = new V1_12();
			return u.setSkull(item, id, textura);		
		}
		else if(packageName.contains("1_11_R1")){
			V1_11 u = new V1_11();
			return u.setSkull(item, id, textura);		
		}
		else if(packageName.contains("1_10_R1")){
			V1_10 u = new V1_10();
			return u.setSkull(item, id, textura);		
		}
		else if(packageName.contains("1_9_R2")){
			V1_9_R2 u = new V1_9_R2();
			return u.setSkull(item, id, textura);			
		}
		else if(packageName.contains("1_9_R1")){
			V1_9_R1 u = new V1_9_R1();
			return u.setSkull(item, id, textura);		
		}
		else if(packageName.contains("1_8_R3")){
			V1_8_R3 u = new V1_8_R3();
			return u.setSkull(item, id, textura);		
		}
		else if(packageName.contains("1_8_R2")){
			V1_8_R2 u = new V1_8_R2();
			return u.setSkull(item, id, textura);		
		}
		else if(packageName.contains("1_8_R1")){
			V1_8_R1 u = new V1_8_R1();
			return u.setSkull(item, id, textura);			
		}
		return null;
	}
}
