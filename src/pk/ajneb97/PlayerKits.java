package pk.ajneb97;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import pk.ajneb97.api.ExpansionPlayerKits;
import pk.ajneb97.api.PlayerKitsAPI;
import pk.ajneb97.managers.InventarioConfirmacionDinero;
import pk.ajneb97.managers.InventarioEditar;
import pk.ajneb97.managers.InventarioListener;
import pk.ajneb97.managers.InventarioManager;
import pk.ajneb97.managers.InventarioPreview;
import pk.ajneb97.managers.JugadorManager;
import pk.ajneb97.managers.PlayerDataSaveTask;
import pk.ajneb97.managers.PlayerListener;
import pk.ajneb97.model.JugadorDatos;
import pk.ajneb97.model.KitJugador;
import pk.ajneb97.mysql.ConexionMySQL;
import pk.ajneb97.mysql.MySQL;
import pk.ajneb97.otros.Utilidades;



public class PlayerKits extends JavaPlugin {
  
	PluginDescriptionFile pdfFile = getDescription();
	public String version = pdfFile.getVersion();
	private FileConfiguration kits = null;
	private File kitsFile = null;
	private FileConfiguration players = null;
	private File playersFile = null;
	public String latestversion;
	private String rutaConfig;
	private KitEditando kitEditando;
	public static String nombrePlugin = ChatColor.translateAlternateColorCodes('&', "&8[&4PlayerKits&8] ");
	public boolean primeraVez = false;
	RegisteredServiceProvider<Economy> rsp = null;
	private static Economy econ = null;	
	public boolean primeraVezKits = false;
	private ArrayList<InventarioJugador> inventarioJugadores;
	
	private JugadorManager jugadorManager;
	
	private ConexionMySQL conexionDatabase;
	
	private PlayerDataSaveTask playerDataSaveTask;
	
	public void onEnable(){
	   this.inventarioJugadores = new ArrayList<InventarioJugador>();
	   registerEvents();
	   registerCommands();
	   registerConfig();
	   registerKits();
	   registerPlayers();
	   setupEconomy();
	   if(primeraVez) {
		   rellenarInventarioConfig();
	   }
	   if(this.primeraVezKits) {
		   rellenarKitsConfig();
	   }
	   if(MySQL.isEnabled(getConfig())) {
		   conexionDatabase = new ConexionMySQL();
		   conexionDatabase.setupMySql(this, getConfig());
	   }
	   jugadorManager = new JugadorManager(this);
	   PlayerKitsAPI api = new PlayerKitsAPI(this);
	   if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
		   new ExpansionPlayerKits(this).register();
	   }
	   checkMessagesUpdate();
	   
	   reloadPlayerDataSaveTask();
	   
	   Bukkit.getConsoleSender().sendMessage(nombrePlugin+ChatColor.YELLOW + "Has been enabled! " + ChatColor.WHITE + "Version: " + version);
	   Bukkit.getConsoleSender().sendMessage(nombrePlugin+ChatColor.YELLOW + "Thanks for using my plugin!  " + ChatColor.WHITE + "~Ajneb97");
	   updateChecker();
	}
	  
	public void onDisable(){
		jugadorManager.guardarJugadores();
		if(kits != null) {
			saveKits();
		}
		Bukkit.getConsoleSender().sendMessage(nombrePlugin+ChatColor.YELLOW + "Has been disabled! " + ChatColor.WHITE + "Version: " + version);
	}
	public void registerCommands(){
		this.getCommand("kit").setExecutor(new Comando(this));
	}
	
	public void registerEvents(){
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new InventarioListener(this), this);
		pm.registerEvents(new InventarioPreview(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new Actualizacion(this), this);
		pm.registerEvents(new InventarioEditar(this), this);
		pm.registerEvents(new InventarioConfirmacionDinero(this), this);
	}
	
	public void agregarInventarioJugador(InventarioJugador inv) {
		this.inventarioJugadores.add(inv);
	}
	
	public InventarioJugador getInventarioJugador(String jugador) {
		for(InventarioJugador inv : inventarioJugadores) {
			if(inv.getJugador().getName().equals(jugador)) {
				return inv;
			}
		}
		return null;
	}
	
	public void removerInventarioJugador(String jugador) {
		for(int i=0;i<inventarioJugadores.size();i++) {
			if(inventarioJugadores.get(i).getJugador().getName().equals(jugador)) {
				inventarioJugadores.remove(i);
			}
		}
	}
	
	public void reloadPlayerDataSaveTask() {
		if(playerDataSaveTask != null) {
			playerDataSaveTask.end();
		}
		playerDataSaveTask = new PlayerDataSaveTask(this);
		playerDataSaveTask.start();
	}
	
	public void registerConfig(){		
		File config = new File(this.getDataFolder(), "config.yml");
		rutaConfig = config.getPath();
	    if(!config.exists()){
	    	this.primeraVez = true;
	    	this.getConfig().options().copyDefaults(true);
			saveConfig();  
	    }
  }
	
	 public void registerKits(){
		  kitsFile = new File(this.getDataFolder(), "kits.yml");
		  if(!kitsFile.exists()){
			  primeraVezKits = true;
		    	this.getKits().options().copyDefaults(true);
				saveKits();
		    }
	  }
	  public void saveKits() {
		 try {
			 kits.save(kitsFile);
		 } catch (IOException e) {
			 e.printStackTrace();
	 	}
	 }
	  
	  public FileConfiguration getKits() {
		    if (kits == null) {
		        reloadKits();
		    }
		    return kits;
		}
	  
	  public void reloadKits() {
		    if (kits == null) {
		    	kitsFile = new File(getDataFolder(), "kits.yml");
		    }
		    kits = YamlConfiguration.loadConfiguration(kitsFile);

		    Reader defConfigStream;
			try {
				defConfigStream = new InputStreamReader(this.getResource("kits.yml"), "UTF8");
				if (defConfigStream != null) {
			        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			        kits.setDefaults(defConfig);
			    }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	    
		}
	  
	  public void registerPlayers(){
		  playersFile = new File(this.getDataFolder(), "players.yml");
		  if(!playersFile.exists()){
		    	this.getPlayers().options().copyDefaults(true);
				savePlayers();
		    }
	  }
	  public void savePlayers() {
		 try {
			 players.save(playersFile);
		 } catch (IOException e) {
			 e.printStackTrace();
	 	}
	 }
	  
	  public FileConfiguration getPlayers() {
		    if (players == null) {
		        reloadPlayers();
		    }
		    return players;
		}
	  
	  public void reloadPlayers() {
		    if (players == null) {
		    playersFile = new File(getDataFolder(), "players.yml");
		    }
		    players = YamlConfiguration.loadConfiguration(playersFile);

		    Reader defConfigStream;
			try {
				defConfigStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
				if (defConfigStream != null) {
			        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			        players.setDefaults(defConfig);
			    }
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	    
		}
	  
	  public void setKitEditando(KitEditando p) {
			this.kitEditando = p;
		}
		
		public void removerKitEditando() {
			this.kitEditando = null;
		}
		
		public KitEditando getKitEditando() {
			return this.kitEditando;
		}
		
		private boolean setupEconomy() {
			  if (getServer().getPluginManager().getPlugin("Vault") == null) {
		          return false;
		      }
		      RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		      if (rsp == null) {
		          return false;
		      }
		      econ = rsp.getProvider();
		      return econ != null;
		  }
		  
		public Economy getEconomy(){	
			return econ;
		}
		
		public Connection getConnection() {
			return this.conexionDatabase.getConnection();
		}
	  
	  public void updateChecker(){
		  
		  try {
			  HttpURLConnection con = (HttpURLConnection) new URL(
	                  "https://api.spigotmc.org/legacy/update.php?resource=75185").openConnection();
	          int timed_out = 1250;
	          con.setConnectTimeout(timed_out);
	          con.setReadTimeout(timed_out);
	          latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
	          if (latestversion.length() <= 7) {
	        	  if(!version.equals(latestversion)){
	        		  Bukkit.getConsoleSender().sendMessage(ChatColor.RED +"There is a new version available. "+ChatColor.YELLOW+
	        				  "("+ChatColor.GRAY+latestversion+ChatColor.YELLOW+")");
	        		  Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"You can download it at: "+ChatColor.WHITE+"https://www.spigotmc.org/resources/75185/");  
	        	  }      	  
	          }
	      } catch (Exception ex) {
	    	  Bukkit.getConsoleSender().sendMessage(nombrePlugin + ChatColor.RED +"Error while checking update.");
	      }
	  }
	  
	  
	  public JugadorManager getJugadorManager() {
		return jugadorManager;
	}

	public void checkMessagesUpdate(){
		  Path archivo = Paths.get(rutaConfig);
		  try{
			  String texto = new String(Files.readAllBytes(archivo));
			  if(!texto.contains("claim_kit_short_command:")){
				  getConfig().set("Config.claim_kit_short_command", false);
				  getConfig().set("Config.inventory_pages_names.1", "&9Kits");
				  getConfig().set("Config.inventory_pages_names.2", "&bVIP Kits");
				  saveConfig();
			  }
			  if(!texto.contains("cantPreviewError:")){
				  getConfig().set("Messages.cantPreviewError", "&cYou can't preview this kit.");
				  getConfig().set("Config.preview_inventory_requires_permission", false);
				  saveConfig();
			  }
			  if(!texto.contains("player_data_save_time:")){
				  getConfig().set("Config.player_data_save_time", 300);
				  getConfig().set("Messages.noPreviewError", "&cThere is nothing to preview for this kit.");
				  saveConfig();
			  }
			  if(!texto.contains("commandResetError:")){
				  getConfig().set("Messages.commandResetError", "&cYou need to use: &7/kit reset <player> <kit>");
				  getConfig().set("Messages.kitResetCorrect", "&aKit &7%kit% &areset for &7%player%&a!");
				  getConfig().set("Messages.kitResetFail", "&cNot possible to reset kit &7%kit% &cfor &7%player%&c!");
				  saveConfig();
			  }
			  if(!texto.contains("mysql_database:")){
				    getConfig().set("Config.mysql_database.enabled", false);
					getConfig().set("Config.mysql_database.host", "localhost");
					getConfig().set("Config.mysql_database.port", 3306);
					getConfig().set("Config.mysql_database.username", "root");
					getConfig().set("Config.mysql_database.password", "root");
					getConfig().set("Config.mysql_database.database", "database");
					getConfig().set("Config.update_notify", true);
				  saveConfig();
			  }
			  if(!texto.contains("preview_inventory_back_item_slot:")){
				  getConfig().set("Config.preview_inventory_back_item_slot", 45);
				  getConfig().set("Config.previewInventorySize", 54);
				  saveConfig();
			  }
			  if(!texto.contains("cooldownPlaceholderReady:")){
				  getConfig().set("Messages.cooldownPlaceholderReady", "&a&lReady!");
				  saveConfig();
			  }
			  if(!texto.contains("drop_items_if_full_inventory:")){
				  getConfig().set("Config.drop_items_if_full_inventory", false);
				  getConfig().set("Config.commands_before_items", false);
				  saveConfig();
			  }
			  if(!texto.contains("errorPage:")){
				  getConfig().set("Messages.errorPage", "&cWrite a valid page.");
				  saveConfig();
			  }
			  if(!texto.contains("close_inventory_on_claim")){
				  getConfig().set("Config.close_inventory_on_claim", false);
				  getConfig().set("Config.hide_kits_with_permissions", false);
				  saveConfig();
			  }
			  if(!texto.contains("kit_page_sound")){
				  getConfig().set("Config.kit_page_sound", "BLOCK_LAVA_POP;10;1");
				  
				  saveConfig();
			  }
			  if(!texto.contains("kit_error_sound:")){
				  getConfig().set("Config.kit_error_sound", "BLOCK_NOTE_BLOCK_PLING;10;0.1");
				  getConfig().set("Config.kit_claim_sound", "ENTITY_PLAYER_LEVELUP;10;1.5");
				  saveConfig();
			  }
			  if(!texto.contains("kit_preview_back_item:")){
				  getConfig().set("Config.kit_preview_back_item", true);
				  getConfig().set("Messages.commandPreviewError", "&cYou need to use: &7/kit preview <kit>");
				  saveConfig();
			  }
			  if(!texto.contains("commandEditError:")){
				  getConfig().set("Messages.commandEditError", "&cYou need to use: &7/kit edit <kit>");
				  saveConfig();
			  }
			  if(!texto.contains("playerNotOnline:")){
				  getConfig().set("Messages.playerNotOnline", "&cPlayer &7%player% &cis not online.");
				  getConfig().set("Messages.commandGiveError", "&cYou need to use: &7/kit give <kit> <player>");
				  getConfig().set("Messages.kitGive", "&aKit &7%kit% &agiven to &e%player%&a!");
				  saveConfig();
			  } 
			  if(!texto.contains("previewInventoryName:")){
				  getConfig().set("Messages.previewInventoryName", "&9Kit Preview");
				  getConfig().set("Messages.backItemName", "&7Back");
				  getConfig().set("Config.kit_preview", true);
				  saveConfig();
			  } 
			  if(!texto.contains("oneTimeError:")){
				  getConfig().set("Messages.oneTimeError", "&cYou can't claim this kit again.");
				  List<String> lista = new ArrayList<String>();
				  lista.add("&cYou can't claim this kit again.");
				  getConfig().set("Messages.kitOneTimeLore", lista);
				  saveConfig();
			  }  
		  }catch(IOException e){
			  e.printStackTrace();
		  }
	  }
	  
	  public void rellenarInventarioConfig() {
		  FileConfiguration config = getConfig();
		  if(!Utilidades.isLegacy()) {
			  config.set("Config.Inventory.0.id", "BLACK_STAINED_GLASS_PANE");
			  config.set("Config.Inventory.8.id", "BLACK_STAINED_GLASS_PANE");
			  config.set("Config.Inventory.36.id", "BLACK_STAINED_GLASS_PANE");
			  config.set("Config.Inventory.44.id", "BLACK_STAINED_GLASS_PANE");
			  config.set("Config.Inventory.18.id", "PLAYER_HEAD");
			  config.set("Config.Inventory.26.id", "PLAYER_HEAD");
		  }else {
			  config.set("Config.Inventory.0.id", "STAINED_GLASS_PANE:15");
			  config.set("Config.Inventory.8.id", "STAINED_GLASS_PANE:15");
			  config.set("Config.Inventory.36.id", "STAINED_GLASS_PANE:15");
			  config.set("Config.Inventory.44.id", "STAINED_GLASS_PANE:15");
			  config.set("Config.Inventory.18.id", "SKULL_ITEM:3");
			  config.set("Config.Inventory.26.id", "SKULL_ITEM:3");
		  }
		  config.set("Config.Inventory.0.name", " ");
		  config.set("Config.Inventory.8.name", " ");
		  config.set("Config.Inventory.36.name", " ");
		  config.set("Config.Inventory.44.name", " ");
		  config.set("Config.Inventory.18.name", "&6Previous Page" );
		  config.set("Config.Inventory.18.skulldata", "2391d533-ab09-434d-9980-adafde4057a3;eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==");
		  config.set("Config.Inventory.18.type", "previous_page");
		  config.set("Config.Inventory.26.name", "&6Next Page" );
		  config.set("Config.Inventory.26.skulldata", "d513d666-0992-42c7-9aa6-e518a83e0b38;eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19");
		  config.set("Config.Inventory.26.type", "next_page");
		  
		  this.saveConfig();
	  }
	  
	  //Esto arregla bugs
		public void rellenarKitsConfig() {
			//Comprobar cuando se carga por primera vez la config
			FileConfiguration kits = getKits();
			
			kits.set("Kits.iron.Items.1.id", "IRON_AXE");
			kits.set("Kits.iron.Items.1.amount", 1);
			kits.set("Kits.iron.Items.2.id", "IRON_PICKAXE");
			kits.set("Kits.iron.Items.2.amount", 1);
			kits.set("Kits.iron.Items.3.id", "IRON_SWORD");
			kits.set("Kits.iron.Items.3.amount", 1);
			kits.set("Kits.iron.slot", 10);
			kits.set("Kits.iron.display_item", "IRON_AXE");
			kits.set("Kits.iron.display_name", "&6&lIron &aKit");
			List<String> lore = new ArrayList<String>();
			lore.add("&eThis kit includes:");
			lore.add("&8- &7x1 Iron Axe");
			lore.add("&8- &7x1 Iron Pickaxe");
			lore.add("&8- &7x1 Iron Sword");
			lore.add("");
			lore.add("&7Cooldown: &c3 hours");
			lore.add("");
			lore.add("&aLeft Click to claim!");
			lore.add("&bRight Click to preview!");
			kits.set("Kits.iron.display_lore", lore);
			kits.set("Kits.iron.cooldown", 10800);
			
			kits.set("Kits.diamond.Items.1.id", "DIAMOND_AXE");
			kits.set("Kits.diamond.Items.1.amount", 1);
			kits.set("Kits.diamond.Items.2.id", "DIAMOND_PICKAXE");
			kits.set("Kits.diamond.Items.2.amount", 1);
			kits.set("Kits.diamond.Items.3.id", "DIAMOND_SWORD");
			kits.set("Kits.diamond.Items.3.name", "&4Super Sword");
			lore = new ArrayList<String>();
			lore.add("&7Best sword on the server.");
			lore.add("");
			lore.add("&7Owner: &6%player%");
			kits.set("Kits.diamond.Items.3.lore", lore);
			lore = new ArrayList<String>();
			lore.add("DAMAGE_ALL;5");
			kits.set("Kits.diamond.Items.3.enchants", lore);
			kits.set("Kits.diamond.Items.3.amount", 1);
			lore = new ArrayList<String>();
			lore.add("bc &6%player% &ejust claimed a &aDIAMOND KIT&e!");
			kits.set("Kits.diamond.Commands", lore);
			kits.set("Kits.diamond.slot", 11);
			kits.set("Kits.diamond.display_item", "DIAMOND_SWORD");
			kits.set("Kits.diamond.display_name", "&6&lDiamond &aKit");
			lore = new ArrayList<String>();
			lore.add("&eThis kit includes:");
			lore.add("&8- &7x1 Diamond Axe");
			lore.add("&8- &7x1 Diamond Pickaxe");
			lore.add("&8- &7x1 Diamond Sword");
			lore.add("");
			lore.add("&7Cooldown: &c12 hours");
			lore.add("&7Price: &a$5000");
			lore.add("");
			lore.add("&aLeft Click to buy!");
			lore.add("&bRight Click to preview!");
			kits.set("Kits.diamond.display_lore", lore);
			kits.set("Kits.diamond.display_item_glowing", true);
			kits.set("Kits.diamond.cooldown", 43200);
			kits.set("Kits.diamond.price", 5000);
			kits.set("Kits.diamond.permission", "playerkits.kit.diamond");
			kits.set("Kits.diamond.noPermissionsItem.display_item", "BARRIER");
			kits.set("Kits.diamond.noPermissionsItem.display_name", "&6&lDiamond &aKit");
			lore = new ArrayList<String>();
			lore.add("&cYou don't have permissions to claim");
			lore.add("&cthis kit.");
			lore.add("");
			lore.add("&7You need: &bVIP&6+ &7rank.");
			kits.set("Kits.diamond.noPermissionsItem.display_lore", lore);
			
			this.saveKits();
		}
}
