package pk.ajneb97.managers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import pk.ajneb97.InventarioJugador;
import pk.ajneb97.PlayerKits;
import pk.ajneb97.otros.MensajesUtils;

public class InventarioPreview implements Listener{
	
	private PlayerKits plugin;
	public InventarioPreview(PlayerKits plugin) {
		this.plugin = plugin;
	}

	public static void abrirInventarioPreview(PlayerKits plugin,Player jugador,FileConfiguration kits,FileConfiguration config,String kit,int pagina) {
		int slots = Integer.valueOf(config.getString("Config.previewInventorySize"));
		Inventory inv = Bukkit.createInventory(null, slots, MensajesUtils.getMensajeColor(config.getString("Messages.previewInventoryName")));
		if(config.getString("Config.kit_preview_back_item").equals("true")) {
			ItemStack item = new ItemStack(Material.ARROW);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(MensajesUtils.getMensajeColor(config.getString("Messages.backItemName")));
			item.setItemMeta(meta);
			inv.setItem(Integer.valueOf(config.getString("Config.preview_inventory_back_item_slot")), item);
		}
		
		
		
		int slot = 0;
		if(!kits.contains("Kits."+kit+".Items")) {
			//No tiene items, solo comandos?
			String prefix = config.getString("Messages.prefix");
			jugador.sendMessage(MensajesUtils.getMensajeColor(prefix+config.getString("Messages.noPreviewError")));
			return;
		}
		for(String n : kits.getConfigurationSection("Kits."+kit+".Items").getKeys(false)) {
			String path = "Kits."+kit+".Items."+n;
			ItemStack item = KitManager.getItem(kits, path, config, jugador);
			try {
				if(kits.contains(path+".preview_slot")) {
					inv.setItem(Integer.valueOf(kits.getString(path+".preview_slot")), item);
				}else {
					inv.setItem(slot, item);
					slot++;
				}
			}catch(ArrayIndexOutOfBoundsException e) {
				jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', PlayerKits.nombrePlugin+"&cThere is an error. Items for this kits are set on an invalid slot of the preview inventory. Change the &7previewInventorySize &coption in the config.yml file!"));
				return;
			}
		}
		
		jugador.openInventory(inv);
		
		plugin.agregarInventarioJugador(new InventarioJugador(jugador,pagina,null,"preview"));
	}
	
	@EventHandler
	public void clickInventario(InventoryClickEvent event){
		FileConfiguration config = plugin.getConfig();
		Player jugador = (Player) event.getWhoClicked();
		InventarioJugador inv = plugin.getInventarioJugador(jugador.getName());
		if(inv != null) {
			if(event.getCurrentItem() == null){
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
					if(tipoInventario.equals("preview")) {
						int slotAClickear = Integer.valueOf(config.getString("Config.preview_inventory_back_item_slot"));
						if(slot == slotAClickear && !event.getCurrentItem().getType().name().contains("AIR")) {
							InventarioManager.abrirInventarioMain(config, plugin, jugador, inv.getPagina());
						}
					}
				}
			}
		}
	}
}
