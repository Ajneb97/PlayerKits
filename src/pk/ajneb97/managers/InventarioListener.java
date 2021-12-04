package pk.ajneb97.managers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import pk.ajneb97.InventarioJugador;
import pk.ajneb97.PlayerKits;
import pk.ajneb97.otros.MensajesUtils;

public class InventarioListener implements Listener{

	private PlayerKits plugin;
	public InventarioListener(PlayerKits plugin) {
		this.plugin = plugin;
	}
	

	@EventHandler
	public void clickInventario(InventoryClickEvent event){
		Player jugador = (Player) event.getWhoClicked();
		InventarioJugador inv = plugin.getInventarioJugador(jugador.getName());
		if(inv != null) {
			if(event.getCurrentItem() == null || event.getCurrentItem().getType().name().contains("AIR")){
				event.setCancelled(true);
				return;
			}
			if((event.getSlotType() == null)){
				event.setCancelled(true);
				return;
			}else{
				int slot = event.getSlot();
				event.setCancelled(true);
				if(event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
					String tipoInventario = inv.getTipoInventario();
					if(tipoInventario.equals("main")) {
						int pagina = inv.getPagina();
						FileConfiguration configKits = plugin.getKits();
						FileConfiguration config = plugin.getConfig();
						int paginasTotales = InventarioManager.getPaginasTotales(configKits);
						if(config.contains("Config.Inventory")) {
							for(String key : config.getConfigurationSection("Config.Inventory").getKeys(false)) {
								int slotNuevo = Integer.valueOf(key);
								if(slot == slotNuevo) {
									
									if(config.contains("Config.Inventory."+key+".type")) {
										if(config.getString("Config.Inventory."+key+".type").equals("previous_page")) {
											if(pagina > 1) {
												if(!config.getString("Config.kit_page_sound").equals("none")) {
													String[] separados = config.getString("Config.kit_page_sound").split(";");
													try {
														Sound sound = Sound.valueOf(separados[0]);
														jugador.playSound(jugador.getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
													}catch(Exception ex) {
														Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PlayerKits.nombrePlugin+"&7Sound Name: &c"+separados[0]+" &7is not valid. Change the name of the sound corresponding to your Minecraft version."));
													}
												}
												
												InventarioManager.abrirInventarioMain(config, plugin, jugador, pagina-1);
												return;
											}
										}else if(config.getString("Config.Inventory."+key+".type").equals("next_page")) {
											if(paginasTotales > pagina) {
												if(!config.getString("Config.kit_page_sound").equals("none")) {
													String[] separados = config.getString("Config.kit_page_sound").split(";");
													try {
														Sound sound = Sound.valueOf(separados[0]);
														jugador.playSound(jugador.getLocation(), sound, Float.valueOf(separados[1]), Float.valueOf(separados[2]));
													}catch(Exception ex) {
														Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PlayerKits.nombrePlugin+"&7Sound Name: &c"+separados[0]+" &7is not valid. Change the name of the sound corresponding to your Minecraft version."));
													}
												}
												InventarioManager.abrirInventarioMain(config, plugin, jugador, pagina+1);
												return;
											}
										}
									}
									
									if(config.contains("Config.Inventory."+key+".command")) {
										String comando = config.getString("Config.Inventory."+key+".command");
										CommandSender console = Bukkit.getServer().getConsoleSender();
										String comandoAEnviar = comando.replaceAll("%player%", jugador.getName()); 
										Bukkit.dispatchCommand(console, comandoAEnviar);
										return;
									}
								}
							}
						}
						if(configKits.contains("Kits")) {
							for(String key : configKits.getConfigurationSection("Kits").getKeys(false)) {
								if(configKits.contains("Kits."+key+".slot")) {
									if(slot == Integer.valueOf(configKits.getString("Kits."+key+".slot"))) {
										int page = 1;
										if(configKits.contains("Kits."+key+".page")) {
											page = Integer.valueOf(configKits.getString("Kits."+key+".page"));
										}
										if(page == pagina) {
											if(event.getClick().equals(ClickType.RIGHT) && config.getString("Config.kit_preview").equals("true")) {
												//Comprobar si tiene permiso y si esta activada la opcion de requerir permiso
												boolean hasPermission = true;
												if(configKits.contains("Kits."+key+".permission")) {
													String permission = configKits.getString("Kits."+key+".permission");
													if(!jugador.isOp() && !jugador.hasPermission(permission)) {
														hasPermission = false;
													}
												}
												boolean permissionCheck = config.getBoolean("Config.preview_inventory_requires_permission");
												if(permissionCheck && !hasPermission) {
													String prefix = config.getString("Messages.prefix");
													jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.cantPreviewError")));
													return;
												}
												
												InventarioPreview.abrirInventarioPreview(plugin,jugador, configKits, config, key,inv.getPagina());
											}else {
												KitManager.claimKit(jugador, key, plugin, true, false, false);
												
											}
											return;
										}
									}
								}
							}
						}
					}
					
				}
			}
		}
	}
	
	@EventHandler
	public void alCerrar(InventoryCloseEvent event) {
		Player jugador = (Player) event.getPlayer();
		InventarioJugador inv = plugin.getInventarioJugador(jugador.getName());
		if(inv != null && inv.getInventarioManager() != null) {
			Bukkit.getScheduler().cancelTask(inv.getInventarioManager().getTaskID());
		}
		
		//remover
		plugin.removerInventarioJugador(jugador.getName());
	}
}
