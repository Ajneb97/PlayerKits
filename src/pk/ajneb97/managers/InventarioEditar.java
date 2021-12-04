package pk.ajneb97.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.md_5.bungee.api.ChatColor;
import pk.ajneb97.KitEditando;
import pk.ajneb97.PlayerKits;
import pk.ajneb97.otros.Utilidades;

public class InventarioEditar implements Listener{

	private PlayerKits plugin;
	public InventarioEditar(PlayerKits plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public static void crearInventario(Player jugador,String kit,PlayerKits plugin) {
		FileConfiguration kits = plugin.getKits();
		Inventory inv = Bukkit.createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', "&9Editing Kit"));
		
		ItemStack item = new ItemStack(Material.DROPPER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lSlot"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the position of the display"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7item of this kit in the Inventory."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		String slot = "none";
		if(kits.contains("Kits."+kit+".slot")) {
			slot = kits.getString("Kits."+kit+".slot");
		}
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Slot: &a"+slot));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(10, item);
		
		item = new ItemStack(Material.GHAST_TEAR);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lCooldown"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the cooldown of"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7the kit."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		int cooldown = 0;
		if(kits.contains("Kits."+kit+".cooldown")) {
			cooldown = Integer.valueOf(kits.getString("Kits."+kit+".cooldown"));
		}
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Cooldown: &a"+cooldown+"(s)"));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(11, item);
		
		item = new ItemStack(Material.REDSTONE_BLOCK);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lPermission"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the permission of"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7the kit."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		String permission = "none";
		if(kits.contains("Kits."+kit+".permission")) {
			permission = kits.getString("Kits."+kit+".permission");
		}
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Permission: &a"+permission));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(12, item);
		
		String firstJoin = "false";
		if(kits.contains("Kits."+kit+".first_join")) {
			firstJoin = kits.getString("Kits."+kit+".first_join");
		}
		if(firstJoin.equals("true")) {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16")  || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.LIME_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 10);
			}
		}else {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16")  || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.GRAY_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 8);
			}
		}	
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lFirst Join"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define if players should"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7receive this kit when they join for"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7the first time."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Status: &a"+firstJoin));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(19, item);
		
		
		String oneTime = "false";
		if(kits.contains("Kits."+kit+".one_time")) {
			oneTime = kits.getString("Kits."+kit+".one_time");
		}
		if(oneTime.equals("true")) {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16")  || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.LIME_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 10);
			}
		}else {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.GRAY_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 8);
			}
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lOne Time"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define if players should"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7claim this kit just one time."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Status: &a"+oneTime));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(20, item);
		
		String autoArmor = "false";
		if(kits.contains("Kits."+kit+".auto_armor")) {
			autoArmor = kits.getString("Kits."+kit+".auto_armor");
		}
		if(autoArmor.equals("true")) {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.LIME_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 10);
			}
		}else {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.GRAY_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 8);
			}
		}	
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lAuto Armor"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to set if kit armor should"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7be equipped automatically when"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7claiming the kit."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Status: &a"+autoArmor));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(28, item);
		
		item = new ItemStack(Material.BEACON);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lNo Buy Item"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to edit the kit display item when"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7player has not buyed it."));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(41, item);
		
		item = new ItemStack(Material.BARRIER);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lNo Permission Item"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to edit the kit display item when"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7player doesn't have permissions to claim"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7the kit."));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(40, item);
		
		item = new ItemStack(Material.IRON_SWORD);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lDisplay Item"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to edit the kit display item."));
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		inv.setItem(39, item);
		
		List<String> items = new ArrayList<String>();
		FileConfiguration config = plugin.getConfig();
		if(kits.contains("Kits."+kit+".Items")) {
			for(String n : kits.getConfigurationSection("Kits."+kit+".Items").getKeys(false)) {
				String path = "Kits."+kit+".Items."+n;
				ItemStack itemN = KitManager.getItem(kits, path, config, jugador);
				items.add("x"+itemN.getAmount()+" "+itemN.getType());
			}
		}
		
		item = new ItemStack(Material.DIAMOND);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lKit Items"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to edit the kit items."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Items:"));
		for(int i=0;i<items.size();i++) {
			lore.add(ChatColor.translateAlternateColorCodes('&', "&8- &a")+items.get(i));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(14, item);
		
		List<String> comandos = new ArrayList<String>();
		if(kits.contains("Kits."+kit+".Commands")) {
			comandos = kits.getStringList("Kits."+kit+".Commands");
		}
		item = new ItemStack(Material.IRON_INGOT);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lKit Commands"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to edit which commands should"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7the kit execute to the player when"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7receiving it."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Commands:"));
		if(comandos.isEmpty()) {
			lore.add(ChatColor.translateAlternateColorCodes('&', "&cNONE"));
		}else {
			for(int i=0;i<comandos.size();i++) {
				lore.add(ChatColor.translateAlternateColorCodes('&', "&8- &a")+comandos.get(i));
			}
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(15, item);
		
		item = new ItemStack(Material.PAPER);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lPrice"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the price of"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7the kit."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		String price = "none";
		if(kits.contains("Kits."+kit+".price")) {
			price = kits.getString("Kits."+kit+".price");
		}
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Price: &a"+price));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(23, item);
		
		item = new ItemStack(Material.ENDER_PEARL);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lPage"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the page of the of"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7this kit in the Inventory."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		String page = "1";
		if(kits.contains("Kits."+kit+".page")) {
			page = kits.getString("Kits."+kit+".page");
		}
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Page: &a"+page));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(16, item);
		
		String oneTimeBuy = "false";
		if(kits.contains("Kits."+kit+".one_time_buy")) {
			oneTimeBuy = kits.getString("Kits."+kit+".one_time_buy");
		}
		if(oneTimeBuy.equals("true")) {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.LIME_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 10);
			}
		}else {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.GRAY_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 8);
			}
		}	
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lOne Time Buy"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to set if the kit should be"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7buyed just one time. This option"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7requires a price for the kit."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Status: &a"+oneTimeBuy));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(21, item);
		
		jugador.openInventory(inv);
		
		plugin.setKitEditando(new KitEditando(jugador,kit,""));
	}
	
	@SuppressWarnings("deprecation")
	public static void crearInventarioItems(Player jugador,String kit,PlayerKits plugin) {
		FileConfiguration kits = plugin.getKits();
//		FileConfiguration config = plugin.getConfig();
//		int slots = Integer.valueOf(config.getString("Config.previewInventorySize"));
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&9Editing Kit Items"));
		
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Go Back"));
		item.setItemMeta(meta);
		inv.setItem(45, item);
		
		item = new ItemStack(Material.EMERALD);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aSave Items"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7If you made any changes in this inventory"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7it is very important to click this item"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7before closing it or going back."));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(53, item);
		
		if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
				|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
			item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		}else {
			item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short) 8);
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', " "));
		item.setItemMeta(meta);
		for(int i=46;i<=52;i++) {
			inv.setItem(i, item);
		}
		
		if(!Bukkit.getVersion().contains("1.8")) {
			item = new ItemStack(Material.BOOK);
			meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aInformation"));
			lore = new ArrayList<String>();
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7If you want to set an item on the offhand"));
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7just right click it."));
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(49, item);
		}
		
		
		int slot = 0;
		if(kits.contains("Kits."+kit+".Items")) {
			for(String n : kits.getConfigurationSection("Kits."+kit+".Items").getKeys(false)) {
				String path = "Kits."+kit+".Items."+n;
				item = KitManager.getItem(kits, path, plugin.getConfig(), jugador);
				if(!Bukkit.getVersion().contains("1.8")) {
					List<String> loreNuevo = new ArrayList<String>();
					ItemMeta metaNuevo = item.getItemMeta();
					if(kits.contains(path+".offhand") && kits.getString(path+".offhand").equals("true")) {
						loreNuevo.add(ChatColor.translateAlternateColorCodes('&', " "));
						loreNuevo.add(ChatColor.translateAlternateColorCodes('&', "&8[&cRight Click to remove from OFFHAND&8]"));
					}
					metaNuevo.setLore(loreNuevo);
					item.setItemMeta(metaNuevo);
				}
				
				if(kits.contains(path+".preview_slot")) {
					inv.setItem(Integer.valueOf(kits.getString(path+".preview_slot")), item);
				}else {
					inv.setItem(slot, item);
					slot++;
				}
			}
		}
		
		
		jugador.openInventory(inv);
		
		plugin.setKitEditando(new KitEditando(jugador,kit,""));
	}
	
	@EventHandler
	public void clickInventarioItems(InventoryClickEvent event){
		String pathInventory = ChatColor.translateAlternateColorCodes('&',  "&9Editing Kit Items");
		String pathInventoryM = ChatColor.stripColor(pathInventory);
		
		if(ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)){
			if((event.getSlotType() == null)){
				event.setCancelled(true);
				return;
			}else{
				final Player jugador = (Player) event.getWhoClicked();
				int slot = event.getSlot();
				if(event.getClickedInventory() != null && event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
					final KitEditando kitEditando = plugin.getKitEditando();
					FileConfiguration kits = plugin.getKits();
					if(kitEditando != null && kitEditando.getJugador().getName().equals(jugador.getName())) {
						if(slot == 45) {
							event.setCancelled(true);
							InventarioEditar.crearInventario(jugador, kitEditando.getKit(), plugin);
						}else if(slot == 53) {
							//Guardar items
							event.setCancelled(true);
							kits.set("Kits."+kitEditando.getKit()+".Items", null);
							ItemStack[] contents = jugador.getOpenInventory().getTopInventory().getContents();
							int c = 1;
							FileConfiguration config = plugin.getConfig();
							for(int i=0;i<44;i++) {
							    if(contents[i] != null && !contents[i].getType().equals(Material.AIR)) {
							       String path = "Kits."+kitEditando.getKit()+".Items."+c;
							       ItemStack contentsClone = contents[i].clone();
							       if(!Bukkit.getVersion().contains("1.8")) {
							    		ItemMeta meta = contentsClone.getItemMeta();
							    		List<String> lore = meta.getLore();
							    		if(lore != null && !lore.isEmpty()) {
							    			String ultimaLinea = ChatColor.stripColor(lore.get(lore.size()-1));
											if(ultimaLinea.equals("[Right Click to remove from OFFHAND]")) {
												lore.remove(lore.size()-1);lore.remove(lore.size()-1);
												kits.set(path+".offhand", true);
												if(lore.isEmpty()) {
									    			meta.setLore(null);
									    		}else {
									    			meta.setLore(lore);
									    		}
											}
								    		contentsClone.setItemMeta(meta);
							    		}
							       }
								   
								   KitManager.saveItem(contentsClone, kits, path, config);
								   kits.set(path+".preview_slot", i);
								   c++;
								}
							}	
							plugin.saveKits();
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aKit Items saved."));
						}else if(slot >= 46 && slot <= 52) {
							event.setCancelled(true);
						}else if(event.getClick().equals(ClickType.RIGHT)) {
							ItemStack item = event.getCurrentItem();
							if(item != null && !item.getType().equals(Material.AIR)
									&& !Bukkit.getVersion().contains("1.8")) {
								event.setCancelled(true);
								ItemMeta meta = item.getItemMeta();
								List<String> lore = meta.getLore();
								if(lore != null) {
									String ultimaLinea = ChatColor.stripColor(lore.get(lore.size()-1));
									if(ultimaLinea.equals("[Right Click to remove from OFFHAND]")) {
										lore.remove(lore.size()-1);lore.remove(lore.size()-1);
										if(lore.isEmpty()) {
											lore = null;
										}
									}else {
										lore.add(ChatColor.translateAlternateColorCodes('&', " "));
										lore.add(ChatColor.translateAlternateColorCodes('&', "&8[&cRight Click to remove from OFFHAND&8]"));
									}
								}else {
									lore = new ArrayList<String>();
									lore.add(ChatColor.translateAlternateColorCodes('&', " "));
									lore.add(ChatColor.translateAlternateColorCodes('&', "&8[&cRight Click to remove from OFFHAND&8]"));
								}
								meta.setLore(lore);
								item.setItemMeta(meta);
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void crearInventarioDisplayItem(Player jugador,String kit,PlayerKits plugin,String tipoDisplay) {
		//El tipoDisplay puede ser: normal o nopermission
		FileConfiguration kits = plugin.getKits();
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&9Editing Display Item"));
		
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Go Back"));
		item.setItemMeta(meta);
		inv.setItem(18, item);
		
		if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
				|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
			item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		}else {
			item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short) 8);
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', " "));
		item.setItemMeta(meta);
		for(int i=19;i<=26;i++) {
			inv.setItem(i, item);
		}
		for(int i=0;i<=8;i++) {
			inv.setItem(i, item);
		}
		inv.setItem(9, item);inv.setItem(13, item);;inv.setItem(17, item);
		
		if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
				|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
			item = new ItemStack(Material.PLAYER_HEAD);
		}else {
			item = new ItemStack(Material.valueOf("SKULL_ITEM"),1,(short) 3);
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Place item here &6>>"));
		item.setItemMeta(meta);
		item = Utilidades.setSkull(item, "d513d666-0992-42c7-9aa6-e518a83e0b38", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19");
		inv.setItem(10, item);
		
		if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
				|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
			item = new ItemStack(Material.PLAYER_HEAD);
		}else {
			item = new ItemStack(Material.valueOf("SKULL_ITEM"),1,(short) 3);
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6<< &7Place item here"));
		item.setItemMeta(meta);
		item = Utilidades.setSkull(item, "2391d533-ab09-434d-9980-adafde4057a3", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==");
		inv.setItem(12, item);
		
		String name = "none";
		if(tipoDisplay.equals("normal")) {
			if(kits.contains("Kits."+kit+".display_name")) {
				name = kits.getString("Kits."+kit+".display_name");
			}
		}else {
			if(kits.contains("Kits."+kit+"."+tipoDisplay+".display_name")) {
				name = kits.getString("Kits."+kit+"."+tipoDisplay+".display_name");
			}else {
				if(kits.contains("Kits."+kit+".display_name")) {
					name = kits.getString("Kits."+kit+".display_name");
				}
			}
		}
		item = new ItemStack(Material.NAME_TAG);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lDisplay Name"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the display item name."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Name: &a"+name));
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(14, item);
		
		List<String> displayLore = new ArrayList<String>();
		if(tipoDisplay.equals("normal")) {
			if(kits.contains("Kits."+kit+".display_lore")) {
				displayLore = kits.getStringList("Kits."+kit+".display_lore");
			}
		}else {
			if(kits.contains("Kits."+kit+"."+tipoDisplay+".display_lore")) {
				displayLore = kits.getStringList("Kits."+kit+"."+tipoDisplay+".display_lore");
			}else {
				if(kits.contains("Kits."+kit+".display_lore")) {
					displayLore = kits.getStringList("Kits."+kit+".display_lore");
				}
			}
		}
		item = new ItemStack(Material.PAPER);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lDisplay Lore"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define the display item lore."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Lore:"));
		if(displayLore.isEmpty()) {
			lore.add(ChatColor.translateAlternateColorCodes('&', "&cNONE"));
		}else {
			for(int i=0;i<displayLore.size();i++) {
				lore.add(ChatColor.translateAlternateColorCodes('&', displayLore.get(i)));
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(15, item);
		
		String glowing = "false";
		if(tipoDisplay.equals("normal")) {
			if(kits.contains("Kits."+kit+".display_item_glowing")) {
				glowing = kits.getString("Kits."+kit+".display_item_glowing");
			}
		}else {
			if(kits.contains("Kits."+kit+"."+tipoDisplay+".display_item_glowing")) {
				glowing = kits.getString("Kits."+kit+"."+tipoDisplay+".display_item_glowing");
			}
		}
		if(glowing.equals("true")) {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.LIME_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 10);
			}
		}else {
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
				item = new ItemStack(Material.GRAY_DYE);
			}else {
				item = new ItemStack(Material.valueOf("INK_SACK"),1,(short) 8);
			}
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eSet &6&lDisplay Item Glowing"));
		lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to define if the display item"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7should display an enchantment."));
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Current Status: &a"+glowing));
		meta.setLore(lore);
		if(glowing.equals("true")) {
			meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		item.setItemMeta(meta);
		inv.setItem(16, item);
		
		ItemStack displayItem = null;
		if(tipoDisplay.equals("normal")) {
			if(kits.contains("Kits."+kit+".display_item")) {
				displayItem = Utilidades.getDisplayItem(kits, "Kits."+kit);
			}
		}else {
			if(kits.contains("Kits."+kit+"."+tipoDisplay+".display_item")) {
				displayItem = Utilidades.getDisplayItem(kits, "Kits."+kit+"."+tipoDisplay);
			}
		}
		
		if(displayItem != null) {
			meta = displayItem.getItemMeta();
			meta.setDisplayName(null);
			meta.setLore(null);
			displayItem.setItemMeta(meta);
			inv.setItem(11, displayItem);
		}
		
		jugador.openInventory(inv);
		
		plugin.setKitEditando(new KitEditando(jugador,kit,tipoDisplay));
	}
	
	@EventHandler
	public void clickInventarioDisplayItem(InventoryClickEvent event){
		String pathInventory = ChatColor.translateAlternateColorCodes('&',  "&9Editing Display Item");
		String pathInventoryM = ChatColor.stripColor(pathInventory);
		
		if(ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)){
			if(event.getCursor() == null){
				event.setCancelled(true);
				return;
			}
			if((event.getSlotType() == null)){
				event.setCancelled(true);
				return;
			}else{
				final Player jugador = (Player) event.getWhoClicked();
				int slot = event.getSlot();
				if(event.getClickedInventory() != null && event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
					final KitEditando kitEditando = plugin.getKitEditando();
					FileConfiguration kits = plugin.getKits();
					if(slot != 11) {
						event.setCancelled(true);
						if(kitEditando != null && kitEditando.getJugador().getName().equals(jugador.getName())) {
							final String tipoDisplay = kitEditando.getTipoDisplay();
							if(slot == 18) {
								guardarDisplayItem(event.getClickedInventory(),tipoDisplay,kits,kitEditando.getKit());
								InventarioEditar.crearInventario(jugador, kitEditando.getKit(), plugin);
							}else if(slot == 14) {
								//set display name
								jugador.closeInventory();
								KitEditando kit = new KitEditando(jugador,kitEditando.getKit(),tipoDisplay);
								kit.setPaso("display_name");
								plugin.setKitEditando(kit);
								jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the display name of the Kit."));
								jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7You can use color codes&8)"));
							}else if(slot == 16) {
								//set glowing
								guardarDisplayItem(event.getClickedInventory(),tipoDisplay,kits,kitEditando.getKit());
								String path = "";
								if(tipoDisplay.equals("normal")) {
									path = "Kits."+kitEditando.getKit()+".display_item_glowing";
									if(kits.contains("Kits."+kitEditando.getKit()+".display_item_glowing") && kits.getString("Kits."+kitEditando.getKit()+".display_item_glowing").equals("true")) {
										kits.set(path, false);
									}else {
										kits.set(path, true);
									}
								}else {
									path = "Kits."+kitEditando.getKit()+"."+tipoDisplay+".display_item_glowing";
									if(kits.contains("Kits."+kitEditando.getKit()+"."+tipoDisplay+".display_item_glowing") && kits.getString("Kits."+kitEditando.getKit()+"."+tipoDisplay+".display_item_glowing").equals("true")) {
										kits.set(path, false);
									}else {
										kits.set(path, true);
									}
								}
								
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									@Override
									public void run() {
										InventarioEditar.crearInventarioDisplayItem(jugador, kitEditando.getKit(), plugin, tipoDisplay);
									}
								}, 3L);
							}else if(slot == 15) {
								guardarDisplayItem(event.getClickedInventory(),tipoDisplay,kits,kitEditando.getKit());
								InventarioEditar.crearInventarioDisplayItemLore(jugador, kitEditando.getKit(), plugin, tipoDisplay);
							}
						}
					}
				}
			}
		}
	}
	
	//Guardar display item
	public static void guardarDisplayItem(Inventory inv,String tipoDisplay,FileConfiguration kits,String kit) {
		ItemStack item = inv.getItem(11);
		String path = "";
		if(tipoDisplay.equals("normal")) {
			path = "Kits."+kit;
		}else {
			path = "Kits."+kit+"."+tipoDisplay;
		}
		if(item != null) {
			Material id = item.getType();
			int datavalue = 0;
			if(!Bukkit.getVersion().contains("1.13") && !Bukkit.getVersion().contains("1.14") && !Bukkit.getVersion().contains("1.15")
					&& !Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")){
				if(id == Material.POTION){
					datavalue = item.getDurability();
				}else{
					datavalue = item.getData().getData();
				}
			}
			kits.set(path+".display_item_skulldata", null);
			if(datavalue != 0) {
				kits.set(path+".display_item", item.getType()+":"+datavalue);
			}else {
				kits.set(path+".display_item", item.getType()+"");
			}
			
			kits.set(path+".display_item_leathercolor", null);
			if(id.equals(Material.LEATHER_BOOTS) || id.equals(Material.LEATHER_CHESTPLATE)
					|| id.equals(Material.LEATHER_HELMET) || id.equals(Material.LEATHER_LEGGINGS)) {
				LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
				kits.set(path+".display_item_leathercolor", meta.getColor().asRGB()+"");
			}
			
			if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
					|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")){
				if(id == Material.getMaterial("PLAYER_HEAD")){
					Utilidades.guardarSkullDisplay(item,kits,path);				
				}
			}else {
				if(id == Material.valueOf("SKULL_ITEM") && datavalue == 3){
					Utilidades.guardarSkullDisplay(item,kits,path);	
				}
				
			}
		}else {
			kits.set(path+".display_item", null);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void crearInventarioDisplayItemLore(Player jugador,String kit,PlayerKits plugin,String tipoDisplay) {
		FileConfiguration kits = plugin.getKits();
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&9Editing Display Item Lore"));
		
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Go Back"));
		item.setItemMeta(meta);
		inv.setItem(45, item);
		
		item = new ItemStack(Material.EMERALD_BLOCK);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aAdd new Lore Line"));
		item.setItemMeta(meta);
		inv.setItem(53, item);
		
		if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
				|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
			item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		}else {
			item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short) 8);
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', " "));
		item.setItemMeta(meta);
		for(int i=46;i<=52;i++) {
			inv.setItem(i, item);
		}
		
		List<String> lore = new ArrayList<String>();
		if(tipoDisplay.equals("normal")) {
			if(kits.contains("Kits."+kit+".display_lore")) {
				lore = kits.getStringList("Kits."+kit+".display_lore");
			}
		}else {
			if(kits.contains("Kits."+kit+"."+tipoDisplay+".display_lore")) {
				lore = kits.getStringList("Kits."+kit+"."+tipoDisplay+".display_lore");
			}
		}
		for(int i=0;i<lore.size();i++) {
			item = new ItemStack(Material.PAPER,1);
			meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9Line &e#"+(i+1)));
			List<String> lore2 = new ArrayList<String>();
			lore2.add(ChatColor.translateAlternateColorCodes('&', "&7")+lore.get(i));
			lore2.add(ChatColor.translateAlternateColorCodes('&', ""));
			lore2.add(ChatColor.translateAlternateColorCodes('&', "&8[&cRight Click to remove&8]"));
			meta.setLore(lore2);
			item.setItemMeta(meta);
			inv.setItem(i, item);
		}
		
		jugador.openInventory(inv);
		
		plugin.setKitEditando(new KitEditando(jugador,kit,tipoDisplay));
	}
	
	@EventHandler
	public void clickInventarioDisplayLore(InventoryClickEvent event){
		String pathInventory = ChatColor.translateAlternateColorCodes('&',  "&9Editing Display Item Lore");
		String pathInventoryM = ChatColor.stripColor(pathInventory);
		
		if(ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)){
			if(event.getCurrentItem() == null){
				event.setCancelled(true);
				return;
			}
			if((event.getSlotType() == null)){
				event.setCancelled(true);
				return;
			}else{
				final Player jugador = (Player) event.getWhoClicked();
				int slot = event.getSlot();
				event.setCancelled(true);
				if(event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
					final KitEditando kitEditando = plugin.getKitEditando();
					FileConfiguration kits = plugin.getKits();
					if(kitEditando != null && kitEditando.getJugador().getName().equals(jugador.getName())) {
						final String tipoDisplay = kitEditando.getTipoDisplay();
						if(slot == 45) {
							InventarioEditar.crearInventarioDisplayItem(jugador, kitEditando.getKit(), plugin,tipoDisplay);
						}else if(slot == 53) {
							//Agregar lore
							jugador.closeInventory();
							KitEditando kit = new KitEditando(jugador,kitEditando.getKit(),tipoDisplay);
							kit.setPaso("lore");
							plugin.setKitEditando(kit);
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the lore line to add."));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7Write 'empty' to add an empty line&8)"));
						}else if(slot >= 0 && slot <= 44 && event.getClick().equals(ClickType.RIGHT)) {
							List<String> lore = new ArrayList<String>();
							if(tipoDisplay.equals("normal")) {
								if(kits.contains("Kits."+kitEditando.getKit()+".display_lore")) {
									lore = kits.getStringList("Kits."+kitEditando.getKit()+".display_lore");
								}
							}else {
								if(kits.contains("Kits."+kitEditando.getKit()+"."+tipoDisplay+".display_lore")) {
									lore = kits.getStringList("Kits."+kitEditando.getKit()+"."+tipoDisplay+".display_lore");
								}
							}
							for(int i=0;i<lore.size();i++) {
								if(i == slot) {
									lore.remove(i);
									break;
								}
							}
							if(tipoDisplay.equals("normal")) {
								kits.set("Kits."+kitEditando.getKit()+".display_lore", lore);
							}else {
								kits.set("Kits."+kitEditando.getKit()+"."+tipoDisplay+".display_lore", lore);
							}
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									InventarioEditar.crearInventarioDisplayItemLore(jugador, kitEditando.getKit(), plugin, tipoDisplay);
								}
							}, 3L);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void crearInventarioComandos(Player jugador,String kit,PlayerKits plugin) {
		FileConfiguration kits = plugin.getKits();
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&9Editing Kit Commands"));
		
		ItemStack item = new ItemStack(Material.ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Go Back"));
		item.setItemMeta(meta);
		inv.setItem(45, item);
		
		item = new ItemStack(Material.EMERALD_BLOCK);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aAdd new Command"));
		item.setItemMeta(meta);
		inv.setItem(53, item);
		
		if(Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15")
				|| Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")|| Bukkit.getVersion().contains("1.18")) {
			item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		}else {
			item = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"),1,(short) 8);
		}
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', " "));
		item.setItemMeta(meta);
		for(int i=46;i<=52;i++) {
			inv.setItem(i, item);
		}
		
		List<String> comandos = new ArrayList<String>();
		if(kits.contains("Kits."+kit+".Commands")) {
			comandos = kits.getStringList("Kits."+kit+".Commands");
		}
		for(int i=0;i<comandos.size();i++) {
			item = new ItemStack(Material.PAPER,1);
			meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9Command &e#"+(i+1)));
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7")+comandos.get(i));
			lore.add(ChatColor.translateAlternateColorCodes('&', ""));
			lore.add(ChatColor.translateAlternateColorCodes('&', "&8[&cRight Click to remove&8]"));
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(i, item);
		}
		
		jugador.openInventory(inv);
		
		plugin.setKitEditando(new KitEditando(jugador,kit,""));
	}
	
	@EventHandler
	public void clickInventarioComandos(InventoryClickEvent event){
		String pathInventory = ChatColor.translateAlternateColorCodes('&',  "&9Editing Kit Commands");
		String pathInventoryM = ChatColor.stripColor(pathInventory);
		
		if(ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)){
			if(event.getCurrentItem() == null){
				event.setCancelled(true);
				return;
			}
			if((event.getSlotType() == null)){
				event.setCancelled(true);
				return;
			}else{
				final Player jugador = (Player) event.getWhoClicked();
				int slot = event.getSlot();
				event.setCancelled(true);
				if(event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
					final KitEditando kitEditando = plugin.getKitEditando();
					FileConfiguration kits = plugin.getKits();
					if(kitEditando != null && kitEditando.getJugador().getName().equals(jugador.getName())) {
						if(slot == 45) {
							InventarioEditar.crearInventario(jugador, kitEditando.getKit(), plugin);
						}else if(slot == 53) {
							//Agregar comando
							jugador.closeInventory();
							KitEditando kit = new KitEditando(jugador,kitEditando.getKit(),"");
							kit.setPaso("comando");
							plugin.setKitEditando(kit);
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the command to add."));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7This command will be executed from console&8)"));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7Player variable is: &e%player%&8)"));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7Write the command without the '/'&8)"));
						}else if(slot >= 0 && slot <= 44 && event.getClick().equals(ClickType.RIGHT)) {
							List<String> comandos = new ArrayList<String>();
							if(kits.contains("Kits."+kitEditando.getKit()+".Commands")) {
								comandos = kits.getStringList("Kits."+kitEditando.getKit()+".Commands");
							}
							for(int i=0;i<comandos.size();i++) {
								if(i == slot) {
									comandos.remove(i);
									break;
								}
							}
							kits.set("Kits."+kitEditando.getKit()+".Commands", comandos);
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									InventarioEditar.crearInventarioComandos(jugador, kitEditando.getKit(), plugin);
								}
							}, 3L);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void cerrarInventarioDisplay(InventoryCloseEvent event) {
		String pathInventory = ChatColor.stripColor(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',  "&9Editing Display Item")));
		Player jugador = (Player)event.getPlayer();
		KitEditando kitEditando = plugin.getKitEditando();
		if(kitEditando != null && kitEditando.getJugador().getName().equals(jugador.getName())) {
			if(ChatColor.stripColor(event.getView().getTitle()).equals(pathInventory)){
				InventarioEditar.guardarDisplayItem(event.getView().getTopInventory(), kitEditando.getTipoDisplay(), plugin.getKits(), kitEditando.getKit());
				plugin.removerKitEditando();
				plugin.saveKits();
			}
		}
	}
	
	@EventHandler
	public void cerrarInventario(InventoryCloseEvent event) {
		String pathInventory = ChatColor.stripColor(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&',  "&9Editing Kit")));
		Player jugador = (Player)event.getPlayer();
		KitEditando kitEditando = plugin.getKitEditando();
		if(kitEditando != null && kitEditando.getJugador().getName().equals(jugador.getName())) {
			if(ChatColor.stripColor(event.getView().getTitle()).startsWith(pathInventory)){
				plugin.removerKitEditando();
				plugin.saveKits();
			}
		}
	}
	
	@EventHandler
	public void alSalir(PlayerQuitEvent event) {
		KitEditando kit = plugin.getKitEditando();
		Player jugador = event.getPlayer();
		if(kit != null && kit.getJugador().getName().equals(jugador.getName())) {
			plugin.removerKitEditando();
			plugin.saveKits();
		}
	}
	
	@EventHandler
	public void clickInventario(InventoryClickEvent event){
		FileConfiguration config = plugin.getConfig();
		String pathInventory = ChatColor.translateAlternateColorCodes('&',  "&9Editing Kit");
		String pathInventoryM = ChatColor.stripColor(pathInventory);
		
		if(ChatColor.stripColor(event.getView().getTitle()).equals(pathInventoryM)){
			if(event.getCurrentItem() == null){
				event.setCancelled(true);
				return;
			}
			if((event.getSlotType() == null)){
				event.setCancelled(true);
				return;
			}else{
				final Player jugador = (Player) event.getWhoClicked();
				int slot = event.getSlot();
				event.setCancelled(true);
				if(event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
					final KitEditando kitEditando = plugin.getKitEditando();
					FileConfiguration kits = plugin.getKits();
					if(kitEditando != null && kitEditando.getJugador().getName().equals(jugador.getName())) {
						if(slot == 10) {
							//set slot
							jugador.closeInventory();
							KitEditando kit = new KitEditando(jugador,kitEditando.getKit(),"");
							kit.setPaso("slot");
							plugin.setKitEditando(kit);
							int max = Integer.valueOf(config.getString("Config.inventorySize"))-1;
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the new slot of the Kit."));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7Use a number between 0 and "+max+"&8)"));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7Write 'none' to not show the kit&8)"));
						}else if(slot == 11) {
							//set cooldown
							jugador.closeInventory();
							KitEditando kit = new KitEditando(jugador,kitEditando.getKit(),"");
							kit.setPaso("cooldown");
							plugin.setKitEditando(kit);
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the cooldown of the Kit."));
						}else if(slot == 12) {
							//set permission
							jugador.closeInventory();
							KitEditando kit = new KitEditando(jugador,kitEditando.getKit(),"");
							kit.setPaso("permission");
							plugin.setKitEditando(kit);
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the permission of the Kit."));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7Write 'none' to not have a permission&8)"));
						}else if(slot == 19) {
							//set first join
							if(kits.contains("Kits."+kitEditando.getKit()+".first_join") && kits.getString("Kits."+kitEditando.getKit()+".first_join").equals("true")) {
								kits.set("Kits."+kitEditando.getKit()+".first_join", false);
							}else {
								kits.set("Kits."+kitEditando.getKit()+".first_join", true);
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									InventarioEditar.crearInventario(jugador, kitEditando.getKit(), plugin);
								}
							}, 3L);
						}else if(slot == 20) {
							//set one time
							if(kits.contains("Kits."+kitEditando.getKit()+".one_time") && kits.getString("Kits."+kitEditando.getKit()+".one_time").equals("true")) {
								kits.set("Kits."+kitEditando.getKit()+".one_time", false);
							}else {
								kits.set("Kits."+kitEditando.getKit()+".one_time", true);
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									InventarioEditar.crearInventario(jugador, kitEditando.getKit(), plugin);
								}
							}, 3L);
						}else if(slot == 28) {
							//set auto armor
							if(kits.contains("Kits."+kitEditando.getKit()+".auto_armor") && kits.getString("Kits."+kitEditando.getKit()+".auto_armor").equals("true")) {
								kits.set("Kits."+kitEditando.getKit()+".auto_armor", false);
							}else {
								kits.set("Kits."+kitEditando.getKit()+".auto_armor", true);
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									InventarioEditar.crearInventario(jugador, kitEditando.getKit(), plugin);
								}
							}, 3L);
						}else if(slot == 15) {
							//set commands
							InventarioEditar.crearInventarioComandos(jugador, kitEditando.getKit(), plugin);
						}else if(slot == 14) {
							//set items
							InventarioEditar.crearInventarioItems(jugador, kitEditando.getKit(), plugin);
						}else if(slot == 23) {
							//set price
							jugador.closeInventory();
							KitEditando kit = new KitEditando(jugador,kitEditando.getKit(),"");
							kit.setPaso("price");
							plugin.setKitEditando(kit);
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the price of the Kit."));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7Write 'none' to not have a price&8)"));
						}else if(slot == 16) {
							//set page
							jugador.closeInventory();
							KitEditando kit = new KitEditando(jugador,kitEditando.getKit(),"");
							kit.setPaso("page");
							plugin.setKitEditando(kit);
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWrite the new page of the Kit."));
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&7Use a number greater than 0&8)"));
						}else if(slot == 21) {
							//set one time buy
							if(kits.contains("Kits."+kitEditando.getKit()+".one_time_buy") && kits.getString("Kits."+kitEditando.getKit()+".one_time_buy").equals("true")) {
								kits.set("Kits."+kitEditando.getKit()+".one_time_buy", false);
							}else {
								kits.set("Kits."+kitEditando.getKit()+".one_time_buy", true);
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									InventarioEditar.crearInventario(jugador, kitEditando.getKit(), plugin);
								}
							}, 3L);
						}
						else if(slot == 40) {
							InventarioEditar.crearInventarioDisplayItem(jugador, kitEditando.getKit(), plugin, "noPermissionsItem");
						}else if(slot == 39) {
							InventarioEditar.crearInventarioDisplayItem(jugador, kitEditando.getKit(), plugin, "normal");
						}else if(slot == 41) {
							InventarioEditar.crearInventarioDisplayItem(jugador, kitEditando.getKit(), plugin, "noBuyItem");
						}
						
					}
					
				}
			}
		}
	}
	
	@EventHandler
	public void capturarChat(AsyncPlayerChatEvent event) {
		final KitEditando kit = plugin.getKitEditando();
		final Player jugador = event.getPlayer();
		String message = ChatColor.stripColor(event.getMessage());
		if(kit != null && kit.getJugador().getName().equals(jugador.getName())) {
			FileConfiguration kits = plugin.getKits();
			event.setCancelled(true);
			FileConfiguration config = plugin.getConfig();
			String prefix = ChatColor.translateAlternateColorCodes('&', config.getString("Messages.prefix"));
			String paso = kit.getPaso();
			if(paso.equals("slot")) {
				int max = Integer.valueOf(config.getString("Config.inventorySize"))-1;
				try {
					int num = Integer.valueOf(message);
					if(num >= 0 && num <= max) {
						jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aSlot defined to: &e"+num));
						kits.set("Kits."+kit.getKit()+".slot", num);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								InventarioEditar.crearInventario(jugador, kit.getKit(), plugin);
							}
						}, 3L);
					}else {
						jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cUse a valid number or write 'none'."));
					}
				}catch(NumberFormatException e) {
					if(message.equalsIgnoreCase("none")) {
						jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aSlot defined to: &e"+message));
						kits.set("Kits."+kit.getKit()+".slot", null);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								InventarioEditar.crearInventario(jugador, kit.getKit(), plugin);
							}
						}, 3L);
					}else {
						jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cUse a valid number or write 'none'."));
					}
				}
			}else if(paso.equals("page")) {
				try {
					int num = Integer.valueOf(message);
					if(num >= 1) {
						jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aPage defined to: &e"+num));
						kits.set("Kits."+kit.getKit()+".page", num);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								InventarioEditar.crearInventario(jugador, kit.getKit(), plugin);
							}
						}, 3L);
					}else {
						jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cUse a valid number."));
					}
				}catch(NumberFormatException e) {
					jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cUse a valid number."));
				}
			}
			else if(paso.equals("cooldown")) {
				try {
					int num = Integer.valueOf(message);
					if(num >= 0) {
						jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aCooldown defined to: &e"+num));
						kits.set("Kits."+kit.getKit()+".cooldown", num);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								InventarioEditar.crearInventario(jugador, kit.getKit(), plugin);
							}
						}, 3L);
					}else {
						jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cUse a valid number."));
					}
				}catch(NumberFormatException e) {
					jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cUse a valid number."));
				}
			}else if(paso.equals("permission")) {
				if(message.equalsIgnoreCase("none")) {
					kits.set("Kits."+kit.getKit()+".permission", null);	
				}else {
					kits.set("Kits."+kit.getKit()+".permission", message);	
				}
				jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aPermission defined to: &e"+message));
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						InventarioEditar.crearInventario(jugador, kit.getKit(), plugin);
					}
				}, 3L);
			}else if(paso.equals("comando")) {
				List<String> comandos = new ArrayList<String>();
				if(kits.contains("Kits."+kit.getKit()+".Commands")) {
					comandos = kits.getStringList("Kits."+kit.getKit()+".Commands");
				}
				comandos.add(message);
				kits.set("Kits."+kit.getKit()+".Commands", comandos);
				jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aCommand added: &e"+message));
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						InventarioEditar.crearInventarioComandos(jugador, kit.getKit(), plugin);
					}
				}, 3L);
			}else if(paso.equals("price")) {
				if(message.equalsIgnoreCase("none")) {
					kits.set("Kits."+kit.getKit()+".price", null);	
				}else {
					kits.set("Kits."+kit.getKit()+".price", message);	
				}
				jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aPrice defined to: &e"+message));
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						InventarioEditar.crearInventario(jugador, kit.getKit(), plugin);
					}
				}, 3L);
			}else if(paso.equals("display_name")) {
				final String tipoDisplay = kit.getTipoDisplay();
				if(tipoDisplay.equals("normal")) {
					kits.set("Kits."+kit.getKit()+".display_name", message);
				}else {
					kits.set("Kits."+kit.getKit()+"."+tipoDisplay+".display_name", message);
				}
				jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aDisplay Name defined to: &e"+message));
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						InventarioEditar.crearInventarioDisplayItem(jugador, kit.getKit(), plugin, tipoDisplay);
					}
				}, 3L);
			}else if(paso.equals("lore")) {
				List<String> lore = new ArrayList<String>();
				final String tipoDisplay = kit.getTipoDisplay();
				if(tipoDisplay.equals("normal")) {
					if(kits.contains("Kits."+kit.getKit()+".display_lore")) {
						lore = kits.getStringList("Kits."+kit.getKit()+".display_lore");
					}
				}else {
					if(kits.contains("Kits."+kit.getKit()+"."+tipoDisplay+".display_lore")) {
						lore = kits.getStringList("Kits."+kit.getKit()+"."+tipoDisplay+".display_lore");
					}
				}
				if(message.equals("empty")) {
					lore.add("");
				}else {
					lore.add(message);
				}
				if(tipoDisplay.equals("normal")) {
					kits.set("Kits."+kit.getKit()+".display_lore", lore);
				}else {
					kits.set("Kits."+kit.getKit()+"."+tipoDisplay+".display_lore", lore);
				}
				
				jugador.sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&aLore line added: &e"+message));
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						InventarioEditar.crearInventarioDisplayItemLore(jugador, kit.getKit(), plugin,tipoDisplay);
					}
				}, 3L);
			}
		}
	}
}
