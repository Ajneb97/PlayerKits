package pk.ajneb97;




import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import pk.ajneb97.managers.InventarioEditar;
import pk.ajneb97.managers.InventarioManager;
import pk.ajneb97.managers.InventarioPreview;
import pk.ajneb97.managers.JugadorManager;
import pk.ajneb97.managers.KitManager;
import pk.ajneb97.otros.MensajesUtils;
import pk.ajneb97.otros.Utilidades;




public class Comando implements CommandExecutor,TabCompleter {
	
	private PlayerKits plugin;
	public Comando(PlayerKits plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		FileConfiguration config = plugin.getConfig();
		FileConfiguration configKits = plugin.getKits();
		String prefix = config.getString("Messages.prefix");
		if (!(sender instanceof Player)){
		   if(args.length > 0) {
			   if(args[0].equalsIgnoreCase("reload")) {
				   plugin.reloadConfig();
				   plugin.reloadKits();
				   plugin.reloadPlayerDataSaveTask();
				   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.configReload")));
			   }else if(args[0].equalsIgnoreCase("give")) {
				   //kit give <kit> <jugador> 
				   give(sender,args,prefix,config,configKits);
			   }else if(args[0].equalsIgnoreCase("open")) {
				   //Abrir inventario
				   // /kit open <jugador> <pagina>
				   open(sender,args,prefix,config,configKits);	   
			   }else if(args[0].equalsIgnoreCase("reset")) {
				   reset(sender,args,prefix,config,configKits);
			   }
		   }
		   return false;   	
	   }
	   final Player jugador = (Player)sender;
	   
	   if(args.length > 0) {
		   if(config.getBoolean("Config.claim_kit_short_command")) {
			   // /kit <kit>
			   String kit = getKit(configKits,args[0]);
			   if(kit != null) {
				   if(configKits.contains("Kits."+kit+".slot") || jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
					   KitManager.claimKit(jugador, kit, plugin, true, false, false);
				   }else {
					   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[0]))); 
				   }
				   return true;
			   }
		   }
		   
		   if(args[0].equalsIgnoreCase("create")) {
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
				   if(args.length >= 2) {	
					   String kit = getKit(configKits,args[1]);
					   if(kit == null) {
						   if(KitManager.save(args[1],configKits,config,jugador)) {
							   plugin.saveKits();
							   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitCreated").replace("%name%", args[1]))); 
						   }else {
							   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.inventoryEmpty"))); 
						   }	   
					   }else {   
						   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitAlreadyExists").replace("%name%", kit))); 
					   }
				   }else {
					   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandCreateError"))); 
				   }
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }
		   }else if(args[0].equalsIgnoreCase("delete")) {
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
				   if(args.length >= 2) {
					   String kit = getKit(configKits,args[1]);
					   if(kit != null) {
						   configKits.set("Kits."+kit, null);
						   plugin.saveKits();
						   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitRemoved").replace("%name%", kit))); 
					   }else {
						   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[1])));  
					   }
				   }else {
					   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandDeleteError"))); 
				   }
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }
		   }else if(args[0].equalsIgnoreCase("open")) {
			   //Abrir inventario
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
				   open(sender,args,prefix,config,configKits);	  
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }	   
		   }else if(args[0].equalsIgnoreCase("reset")) {
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
				   reset(sender,args,prefix,config,configKits);  
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }
		   }
		   else if(args[0].equalsIgnoreCase("claim") && !config.getBoolean("Config.claim_kit_short_command")) {
			   if(args.length >= 2) {
				   String kit = getKit(configKits,args[1]);
				   if(kit != null) {
					   if(configKits.contains("Kits."+kit+".slot") || jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
						   KitManager.claimKit(jugador, kit, plugin, true, false, false);
					   }else {
						   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[1]))); 
					   }
				   }else {
					   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[1]))); 
				   }
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandClaimError"))); 
			   }
		   }else if(args[0].equalsIgnoreCase("preview")) {
			   if(args.length >= 2) {
				   String kit = getKit(configKits,args[1]);
				   if(kit != null) {
					   if(configKits.contains("Kits."+kit+".slot") || jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
						   InventarioPreview.abrirInventarioPreview(plugin, jugador, configKits, config, kit, 1);
					   }else {
						   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[1]))); 
					   }	   
				   }else {
					   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[1]))); 
				   }
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandPreviewError"))); 
			   }
		   }
		   else if(args[0].equalsIgnoreCase("list")) {
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin") || jugador.hasPermission("playerkits.list")) {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandList"))); 
				   int c = 1;
				   JugadorManager jManager = plugin.getJugadorManager();
				   if(configKits.contains("Kits")) {
					   for(String key : configKits.getConfigurationSection("Kits").getKeys(false)) {
						   if(configKits.contains("Kits."+key+".slot") || jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
							   if(configKits.contains("Kits."+key+".permission") && !jugador.hasPermission(configKits.getString("Kits."+key+".permission"))) {
								   jugador.sendMessage(MensajesUtils.getMensajeColor(config.getString("Messages.commandListKitNoPermissions").replace("%number%", c+"").replace("%kit%", key))); 
								}else {
									if(configKits.contains("Kits."+key+".one_time") && configKits.getString("Kits."+key+".one_time").equals("true")
											&& jManager.isOneTime(jugador, key)) {
										jugador.sendMessage(MensajesUtils.getMensajeColor(config.getString("Messages.commandListKitOneTime").replace("%number%", c+"").replace("%kit%", key))); 
									}else {
										boolean cooldownReady = true;
										if(configKits.contains("Kits."+key+".cooldown")) {
											String cooldown = Utilidades.getCooldown(key, jugador, configKits, config, jManager);
											if(!cooldown.equals("ready")) {
												cooldownReady = false;
												jugador.sendMessage(MensajesUtils.getMensajeColor(config.getString("Messages.commandListKitInCooldown").replace("%number%", c+"").replace("%kit%", key)
														.replace("%time%", cooldown))); 
											}
										}
										if(cooldownReady) {
											jugador.sendMessage(MensajesUtils.getMensajeColor(config.getString("Messages.commandListKit").replace("%number%", c+"").replace("%kit%", key))); 
										}
									}	
								}
							   c++;
						   }
						   
					   }
				   }
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }
		   }
		   else if(args[0].equalsIgnoreCase("reload")) {
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
				   plugin.reloadConfig();
				   plugin.reloadKits();
				   plugin.reloadPlayerDataSaveTask();
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.configReload"))); 
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }
		   }else if(args[0].equalsIgnoreCase("edit")) {
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
				   if(args.length >= 2) {
					   String kit = getKit(configKits,args[1]);
					   if(kit != null) {
						   InventarioEditar.crearInventario(jugador,kit,plugin);
					   }else {
						   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[1]))); 
					   }
				   }else {
					   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandEditError"))); 
				   }
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }
		   }
		   else if(args[0].equalsIgnoreCase("give")) {
			   //kit give <kit> <jugador>
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
				   give(sender,args,prefix,config,configKits);
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }
		   }
		   else {
			   if(jugador.isOp() || jugador.hasPermission("playerkits.admin")) {
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[ [ &8[&4PlayerKits&8] &7] ]"));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',""));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit &8Open the Kits GUI."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit open <player> <page> &8Opens the Kits GUI to a player."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit create <kit> &8Creates a new kit."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit delete <kit> &8Removes a created kit."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit edit <kit> &8Edits properties of a kit."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit list &8Shows all kits."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit claim <kit> &8Claims a kit."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit preview <kit> &8Previews a kit."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit give <kit> <player> &8Gives a kit to the player."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit reset <kit> <player> &8Resets a kit data from the player."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6/kit reload &8Reloads the config."));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',""));
				   jugador.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[ [ &8[&4PlayerKits&8] &7] ]"));
			   }else {
				   jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPermissions"))); 
			   }
		   }
	   }else {
		   //Abrir inventario
		   if(!Checks.checkTodo(plugin, jugador)) {
			   return true;
		   }
		   InventarioManager.abrirInventarioMain(config, plugin, jugador, 1);
	   }
	   
	   
	   return true;
	   
	}
	
	public void open(CommandSender sender,String[] args,String prefix,FileConfiguration config,FileConfiguration configKits) {
		if(args.length >= 2) {
			   Player player = Bukkit.getPlayer(args[1]);
			   if(!Checks.checkTodo(plugin, sender)) {
				   return;
			   }
			   if(player != null) {
				   int pag = 1;
				   if(args.length >= 3) {
					   try {
						   pag = Integer.valueOf(args[2]);
						   int pagsTotales = InventarioManager.getPaginasTotales(configKits);
						   if(pag > pagsTotales) {
							   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.errorPage"))); 
							   return;
						   }
					   }catch(NumberFormatException e) {
						   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.errorPage"))); 
						   return;
					   }
				   }
				   InventarioManager.abrirInventarioMain(config, plugin, player, pag);
				   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitInventoryOpen").replace("%player%", args[1]))); 
			   }else {
				   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.playerNotOnline").replace("%player%", args[1]))); 
			   }
		   }else {
			   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandOpenError"))); 
		   }
	}
	
	public void give(CommandSender sender,String[] args,String prefix,FileConfiguration config,FileConfiguration configKits) {
		if(args.length >= 3) {	
			   String kit = getKit(configKits,args[1]);
			   if(kit != null) {
				   Player player = Bukkit.getPlayer(args[2]);
				   if(player != null) {
					   KitManager.claimKit(player, kit, plugin, true, true, false);
					   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitGive").replace("%player%", args[2]).replace("%kit%", kit))); 
				   }else {
					   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.playerNotOnline").replace("%player%", args[2]))); 
				   }
			   }else {
				   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[1])));
			   }
		   }else {
			   sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandGiveError")));
		   }
	}
	
	public void reset(CommandSender sender,String[] args,String prefix,FileConfiguration config,FileConfiguration configKits) {
		// /kits reset <kit> <player>
		if(args.length < 3) {
			sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.commandResetError")));
			return;
		}
		
		String nombreJugador = args[2];
		String kit = getKit(configKits,args[1]);
		if(kit == null) {
			sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitDoesNotExists").replace("%name%", args[1])));
			return;
		}
		
		if(plugin.getJugadorManager().reiniciarKit(nombreJugador, kit)) {
			sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitResetCorrect")
			.replace("%kit%", args[1]).replace("%player%", nombreJugador)));
		}else {
			sender.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.kitResetFail")
			.replace("%kit%", args[1]).replace("%player%", nombreJugador)));
		}
	}
	
	public String getKit(FileConfiguration kits,String kit) {
		if(kits.contains("Kits")) {
			for(String key : kits.getConfigurationSection("Kits").getKeys(false)) {
				if(key.toLowerCase().equals(kit.toLowerCase())) {
					return key;
				}
			}
		}
		return null;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> completions = new ArrayList<String>();
		FileConfiguration config = plugin.getConfig();
		FileConfiguration kits = plugin.getKits();
		
		if(args.length == 1) {
			//Mostrar todos los comandos
			List<String> commands = new ArrayList<String>();
			commands.add("preview");
			if(config.getBoolean("Config.claim_kit_short_command")) {
				String argKit = args[0];
				for(String key : kits.getConfigurationSection("Kits").getKeys(false)) {
					if(kits.contains("Kits."+key+".slot") || sender.isOp() || sender.hasPermission("playerkits.admin")) {
						if(argKit.toLowerCase().isEmpty() || key.toLowerCase().startsWith(argKit.toLowerCase())) {
							completions.add(key);
						}
					}
				}
			}else {
				commands.add("claim");
			}
			
			for(String c : commands) {
				if(args[0].isEmpty() || c.startsWith(args[0].toLowerCase())) {
					completions.add(c);
				}
			}
		}else {
			if((args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("preview")) && args.length == 2) {
				String argKit = args[1];
				for(String key : kits.getConfigurationSection("Kits").getKeys(false)) {
					if(argKit.toLowerCase().isEmpty() || key.toLowerCase().startsWith(argKit.toLowerCase())) {
						if(kits.contains("Kits."+key+".slot") || sender.isOp() || sender.hasPermission("playerkits.admin")) {
							completions.add(key);
						}
					}
				}
			}
		}
		
		
		if(sender.isOp() || sender.hasPermission("playerkits.admin") || sender.hasPermission("playerkits.list")) {
			if(args.length == 1) {
				String c = "list";
				if(args[0].isEmpty() || c.startsWith(args[0].toLowerCase())) {
					completions.add(c);
				}
			}
		}
		if(sender.isOp() || sender.hasPermission("playerkits.admin")) {
			if(args.length == 1) {
				//Mostrar todos los comandos
				List<String> commands = new ArrayList<String>();
				commands.add("open");commands.add("create");commands.add("delete");
				commands.add("edit");commands.add("give");
				commands.add("reset");commands.add("reload");
				for(String c : commands) {
					if(args[0].isEmpty() || c.startsWith(args[0].toLowerCase())) {
						completions.add(c);
					}
				}
			}else {
				if((args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("edit")
						 || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("reset"))
						&& args.length == 2) {
					String argKit = args[1];
					for(String key : kits.getConfigurationSection("Kits").getKeys(false)) {
						if(argKit.toLowerCase().isEmpty() || key.toLowerCase().startsWith(argKit.toLowerCase())) {
							completions.add(key);
						}
					}
				}
			}	
		}
		
		if(completions.isEmpty()) {
			return null;
		}
		
		
		return completions;
	}
}
