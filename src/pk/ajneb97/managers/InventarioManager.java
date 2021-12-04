package pk.ajneb97.managers;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitScheduler;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import pk.ajneb97.InventarioJugador;
import pk.ajneb97.PlayerKits;
import pk.ajneb97.otros.MensajesUtils;
import pk.ajneb97.otros.Utilidades;

public class InventarioManager{

	private int taskID;
	private PlayerKits plugin;
	public InventarioManager(PlayerKits plugin) {
		this.plugin = plugin;
	}
	
	public int getTaskID() {
		return this.taskID;
	}
	
	public void actualizarInventario(final Player jugador, final int pagina) {
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		taskID = sh.scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				if(!update(jugador,pagina)) {
					Bukkit.getScheduler().cancelTask(taskID);
					return;
				}
			}
		}, 0L, 20L);
	}
	
	protected boolean update(Player jugador,int pagina) {
		FileConfiguration config = plugin.getConfig();
		FileConfiguration configKits = plugin.getKits();
		
		String pathInventory = MensajesUtils.getMensajeColor(config.getString("Messages.inventoryName"));
		String pathInventoryM = ChatColor.stripColor(pathInventory);
		Inventory inv = jugador.getOpenInventory().getTopInventory();
		int paginasTotales = getPaginasTotales(configKits);
		if(inv != null && ChatColor.stripColor(jugador.getOpenInventory().getTitle()).equals(pathInventoryM)) {
			if(config.contains("Config.Inventory")) {
				for(String key : config.getConfigurationSection("Config.Inventory").getKeys(false)) {
					int slot = Integer.valueOf(key);
					
					ItemStack item = Utilidades.getItem(config.getString("Config.Inventory."+key+".id"), 1,"");
					ItemMeta meta = item.getItemMeta();
					if(config.contains("Config.Inventory."+key+".name")) {
						String name = config.getString("Config.Inventory."+key+".name");
						if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
							name = PlaceholderAPI.setPlaceholders(jugador, name);
						}
						meta.setDisplayName(MensajesUtils.getMensajeColor(name));
					}
					if(config.contains("Config.Inventory."+key+".lore")) {
						List<String> lore = config.getStringList("Config.Inventory."+key+".lore");
						for(int i=0;i<lore.size();i++) {
							String linea = lore.get(i);
							if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
								linea = PlaceholderAPI.setPlaceholders(jugador, linea);
							}
							lore.set(i, MensajesUtils.getMensajeColor(linea));
						}
						meta.setLore(lore);
					}
					meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
					if(Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")
							 || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
		    			  if(config.contains("Config.Inventory."+key+".custom_model_data")){
		    				  int customModelData = Integer.valueOf(config.getString("Config.Inventory."+key+".custom_model_data"));
		    				  meta.setCustomModelData(customModelData);
		    			  }
		    		}
					item.setItemMeta(meta);
					if(config.contains("Config.Inventory."+key+".skulldata")) {
						String[] skulldata = config.getString("Config.Inventory."+key+".skulldata").split(";");
						item = Utilidades.setSkull(item, skulldata[0], skulldata[1]);
					}
					
					if(config.contains("Config.Inventory."+key+".type")) {
						if(config.getString("Config.Inventory."+key+".type").equals("previous_page")) {
							if(pagina == 1) {
								continue;
							}
						}else if(config.getString("Config.Inventory."+key+".type").equals("next_page")) {
							if(paginasTotales <= pagina) {
								continue;
							}
						}
					}
					inv.setItem(slot, item);
				}
			}
			JugadorManager jManager = plugin.getJugadorManager();
			if(configKits.contains("Kits")) {
				for(String key : configKits.getConfigurationSection("Kits").getKeys(false)) {
					if(configKits.contains("Kits."+key+".slot")) {
						int slot = Integer.valueOf(configKits.getString("Kits."+key+".slot"));
						int page = 1;
						if(configKits.contains("Kits."+key+".page")) {
							page = Integer.valueOf(configKits.getString("Kits."+key+".page"));
						}
						if(page == pagina) {
							if(configKits.contains("Kits."+key+".permission") && !jugador.hasPermission(configKits.getString("Kits."+key+".permission"))){
								if(config.getString("Config.hide_kits_with_permissions").equals("true")) {
									continue;
								}
							}
							if(configKits.contains("Kits."+key+".permission") && !jugador.hasPermission(configKits.getString("Kits."+key+".permission"))
									&& configKits.contains("Kits."+key+".noPermissionsItem")) {
								ItemStack item = crearItemBase("Kits."+key+".noPermissionsItem",key,configKits);
								inv.setItem(slot, item);
							}else if(configKits.contains("Kits."+key+".one_time_buy") && configKits.getString("Kits."+key+".one_time_buy").equals("true") && !jManager.isBuyed(jugador, key)
									&& configKits.contains("Kits."+key+".noBuyItem")) {
								ItemStack item = crearItemBase("Kits."+key+".noBuyItem",key,configKits);
								inv.setItem(slot, item);
							}
							else {
								if(configKits.contains("Kits."+key+".display_item")) {
									ItemStack item = crearItemBase("Kits."+key,key,configKits);
									ItemMeta meta = item.getItemMeta();
									if(configKits.contains("Kits."+key+".one_time") && configKits.getString("Kits."+key+".one_time").equals("true")
											&& jManager.isOneTime(jugador, key)) {
										List<String> lore = config.getStringList("Messages.kitOneTimeLore");
										for(int i=0;i<lore.size();i++) {
											lore.set(i, MensajesUtils.getMensajeColor(lore.get(i)));
										}
										meta.setLore(lore);
									}else {
										if(configKits.contains("Kits."+key+".cooldown")) {
											String cooldown = Utilidades.getCooldown(key, jugador, configKits, config, jManager);
											if(!cooldown.equals("ready")) {
												List<String> lore = config.getStringList("Messages.kitInCooldownLore");
												for(int i=0;i<lore.size();i++) {
													lore.set(i, MensajesUtils.getMensajeColor(lore.get(i).replace("%time%", cooldown)));
												}
												meta.setLore(lore);
											}
										}
									}
									item.setItemMeta(meta);
									
									if(configKits.contains("Kits."+key+".display_item_leathercolor")) {
										LeatherArmorMeta meta2 = (LeatherArmorMeta) meta;
										int color = Integer.valueOf(configKits.getString("Kits."+key+".display_item_leathercolor"));
										meta2.setColor(Color.fromRGB(color));
										item.setItemMeta(meta2);
									}
									
									inv.setItem(slot, item);
								}
				
							}	
						}

					}
				}
			}
			return true;
		}else {
			return false;
		}
	}
	
	public static int getPaginasTotales(FileConfiguration kitsConfig) {
		//Deberia retornar la pagina maxima desde el archivo de kits
		int paginaMaxima = 1;
		if(kitsConfig.contains("Kits")) {
			for(String key : kitsConfig.getConfigurationSection("Kits").getKeys(false)) {
				if(kitsConfig.contains("Kits."+key+".page")) {
					int paginaActual = Integer.valueOf(kitsConfig.getString("Kits."+key+".page"));
					if(paginaActual > paginaMaxima) {
						paginaMaxima = paginaActual;
					}
				}
			}
		}
		return paginaMaxima;
	}
	
	public static void abrirInventarioMain(FileConfiguration config,PlayerKits plugin,Player jugador,int pagina) {
		int size = Integer.valueOf(config.getString("Config.inventorySize"));
		Inventory inv = Bukkit.createInventory(null, size, MensajesUtils.getMensajeColor(config.getString("Messages.inventoryName")));
		jugador.openInventory(inv);
		InventarioManager invM = new InventarioManager(plugin);
		plugin.agregarInventarioJugador(new InventarioJugador(jugador,pagina,invM,"main"));
		   
		invM.actualizarInventario(jugador,pagina);		
	}
	
	public ItemStack crearItemBase(String path,String kit,FileConfiguration configKits) {
		//paths:
		// Kits.kit.noPermissionsItem
		// Kits.kit
		// Kits.kit.noBuyItem
		ItemStack item = Utilidades.getItem(configKits.getString(path+".display_item"), 1,"");
		ItemMeta meta = item.getItemMeta();
		if(configKits.contains(path+".display_name")) {
			meta.setDisplayName(MensajesUtils.getMensajeColor(configKits.getString(path+".display_name")));
		}else {
			if(configKits.contains("Kits."+kit+".display_name")) {
				meta.setDisplayName(MensajesUtils.getMensajeColor(configKits.getString("Kits."+kit+".display_name")));
			}
			
		}
		if(configKits.contains(path+".display_lore")) {
			List<String> lore = configKits.getStringList(path+".display_lore");
			for(int i=0;i<lore.size();i++) {
				lore.set(i, MensajesUtils.getMensajeColor(lore.get(i)));
			}
			meta.setLore(lore);
		}else {
			if(configKits.contains("Kits."+kit+".display_lore")) {
				List<String> lore = configKits.getStringList("Kits."+kit+".display_lore");
				for(int i=0;i<lore.size();i++) {
					lore.set(i, MensajesUtils.getMensajeColor(lore.get(i)));
				}
				meta.setLore(lore);
			}
			
		}
		if(configKits.contains(path+".display_item_glowing") && configKits.getString(path+".display_item_glowing").equals("true")) {
			meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		if(configKits.contains(path+".display_item_skulldata")) {
			String[] skulldata = configKits.getString(path+".display_item_skulldata").split(";");
			item = Utilidades.setSkull(item, skulldata[0], skulldata[1]);
		}
//		else {
//			if(configKits.contains("Kits."+kit+".display_item_skulldata")) {
//				String[] skulldata = configKits.getString("Kits."+kit+".display_item_skulldata").split(";");
//				item = Utilidades.setSkull(item, skulldata[0], skulldata[1]);
//			}
//		}
		return item;
	}
}
