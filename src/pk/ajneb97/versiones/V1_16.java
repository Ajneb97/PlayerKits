package pk.ajneb97.versiones;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import pk.ajneb97.api.PlayerKitsAPI;

public class V1_16 {
	
	private String sepChar;
	public V1_16() {
		sepChar = PlayerKitsAPI.getNBTSeparationCharacter();
	}

	
	public ItemStack setSkullSinID(ItemStack item, String textura) {
		if (textura.isEmpty()) return item;

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", textura));

        try {
            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(skullMeta, profile);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        item.setItemMeta(skullMeta);
        return item;
	}
	
	public ItemStack setUnbreakable(ItemStack item){
		net.minecraft.server.v1_16_R1.ItemStack stack = org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R1.NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new net.minecraft.server.v1_16_R1.NBTTagCompound(); //Create the NMS Stack's NBT (item data)		
		tag.setByte("Unbreakable", (byte)1); //Set unbreakable value to true
		stack.setTag(tag); //Apply the tag to the item
		item = org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asBukkitCopy(stack); //Get the bukkit version of the stack
		return item;
	}
	
	public boolean getUnbreakable(ItemStack item){
		net.minecraft.server.v1_16_R1.ItemStack stack = org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R1.NBTTagCompound tag = new net.minecraft.server.v1_16_R1.NBTTagCompound(); //Create the NMS Stack's NBT (item data)
		tag = stack.getTag();
		if(tag.getBoolean("Unbreakable") == true){
			return true;
		}else{
			return false;
		}
	}

	public ItemStack setSkull(ItemStack crafteos, String path, FileConfiguration config) {
		String pathTextura = path+".skull-texture";

		if(config.contains(pathTextura)){
			String textura = config.getString(pathTextura);
			if (textura.isEmpty()) return crafteos;

	        SkullMeta skullMeta = (SkullMeta) crafteos.getItemMeta();
	        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

	        profile.getProperties().put("textures", new Property("textures", textura));

	        try {
	            Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
	            mtd.setAccessible(true);
	            mtd.invoke(skullMeta, profile);
	        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
	            ex.printStackTrace();
	        }

	        crafteos.setItemMeta(skullMeta);
		}
		return crafteos;
	}

	public void guardarSkull(ItemStack item, String path, FileConfiguration config,String nombreJugador) {
		net.minecraft.server.v1_16_R1.ItemStack cabeza = org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R1.NBTTagCompound cabezaTag = cabeza.getTag();
		if(cabeza.hasTag()){
			if(cabezaTag.hasKey("SkullOwner")){
				net.minecraft.server.v1_16_R1.NBTTagCompound skullOwner = cabezaTag.getCompound("SkullOwner");
				if(skullOwner.hasKey("Properties")){
					net.minecraft.server.v1_16_R1.NBTTagCompound propiedades = skullOwner.getCompound("Properties");
					if(propiedades.hasKey("textures")){
						net.minecraft.server.v1_16_R1.NBTTagList texturas = propiedades.getList("textures", 10);
						config.set(path+".skull-texture", texturas.getCompound(0).getString("Value"));
					}
					
				}
				if(skullOwner.hasKey("Name")){
					if(skullOwner.getString("Name").equals("%player%")){
						config.set(path+".skull-owner", nombreJugador);
					}else{
						config.set(path+".skull-owner", skullOwner.getString("Name"));
					}
					
				}
				if(skullOwner.hasKey("Id")){
					config.set(path+".skull-id", skullOwner.getString("Id"));
				}
				
			}
		}	
		
	}
	
	public void guardarSkullDisplay(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.server.v1_16_R1.ItemStack cabeza = org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R1.NBTTagCompound cabezaTag = cabeza.getTag();
		if(cabeza.hasTag()){
			if(cabezaTag.hasKey("SkullOwner")){
				net.minecraft.server.v1_16_R1.NBTTagCompound skullOwner = cabezaTag.getCompound("SkullOwner");
				String skullmeta = "";
				if(skullOwner.hasKey("Id")){
					skullmeta = skullOwner.getString("Id");
					
				}
				if(skullOwner.hasKey("Properties")){
					net.minecraft.server.v1_16_R1.NBTTagCompound propiedades = skullOwner.getCompound("Properties");
					if(propiedades.hasKey("textures")){
						net.minecraft.server.v1_16_R1.NBTTagList texturas = propiedades.getList("textures", 10);
						skullmeta = skullmeta+";"+texturas.getCompound(0).getString("Value");
					}
				}
				if(skullmeta.contains(";")) {
					config.set(path+".display_item_skulldata", skullmeta);
				}
			}
		}		
	}
	
	public ItemStack setAttributes(ItemStack item, String path, FileConfiguration config){
		path = path+".attributes";
		ItemMeta meta = item.getItemMeta();
		if(config.contains(path)){
			for(String attribute : config.getConfigurationSection(path).getKeys(false)) {
				List<String> modifiers = config.getStringList(path+"."+attribute+".modifiers");
				for(String linea : modifiers) {
					String[] sep = linea.split(";");
					String m = sep[0];
					double amount = Double.valueOf(sep[1]);
					AttributeModifier.Operation op = AttributeModifier.Operation.valueOf(sep[2]);
					UUID uuid = UUID.fromString(sep[3]);
					AttributeModifier modifier = null;
					if(sep.length >= 5) {
						EquipmentSlot slot = EquipmentSlot.valueOf(sep[4]);
						modifier = new AttributeModifier(uuid,m,amount,op,slot);
					}else {
						modifier = new AttributeModifier(uuid,m,amount,op);
					}
	
					meta.addAttributeModifier(Attribute.valueOf(attribute), modifier);
				}
			}
			
			item.setItemMeta(meta);
		}
		
		return item;
	}

	public void guardarAttributes(ItemStack item, String path, FileConfiguration config) {
		ItemMeta meta = item.getItemMeta();
		if(meta != null && meta.hasAttributeModifiers()) {
			Multimap<Attribute,AttributeModifier> atributos = meta.getAttributeModifiers();
			Set<Attribute> set = atributos.keySet();
			for(Attribute a : set) {
				Collection<AttributeModifier> listaModifiers = atributos.get(a);
				List<String> lista = new ArrayList<String>();
				for(AttributeModifier m : listaModifiers) {
					String linea = m.getName()+";"+m.getAmount()+";"+m.getOperation().name()+";"+m.getUniqueId().toString();
					if(m.getSlot() != null) {
						linea=linea+";"+m.getSlot().name();
					}
					lista.add(linea);
				}
				config.set(path+".attributes."+a.name()+".modifiers", lista);
			}
		}
	}
	
	public void guardarNBT(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.server.v1_16_R1.ItemStack itemModificado = org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R1.NBTTagCompound itemTag =  itemModificado.getTag();
		if(itemModificado.hasTag()){
			Set<String> tags = itemTag.getKeys();
			List<String> listaNBT = new ArrayList<String>();
			for(String t : tags) {
				if(!t.equals("ench") && !t.equals("HideFlags") && !t.equals("display")
						&& !t.equals("SkullOwner") && !t.equals("AttributeModifiers") 
						&& !t.equals("Enchantments") && !t.equals("Damage") && !t.equals("CustomModelData") && !t.equals("Potion")
						&& !t.equals("StoredEnchantments") && !t.equals("CustomPotionColor") && !t.equals("CustomPotionEffects") && !t.equals("Fireworks")
						&& !t.equals("Explosion")&& !t.equals("pages") && !t.equals("title") && !t.equals("author") && !t.equals("resolved")
						&& !t.equals("generation")) {
					if(itemTag.hasKeyOfType(t, 1)) {
						//boolean
						listaNBT.add(t+sepChar+itemTag.getBoolean(t)+sepChar+"boolean");
					}else if(itemTag.hasKeyOfType(t, 3)) {
						//int
						listaNBT.add(t+sepChar+itemTag.getInt(t)+sepChar+"int");
					}else if(itemTag.hasKeyOfType(t, 6)) {
						//double
						listaNBT.add(t+sepChar+itemTag.getDouble(t)+sepChar+"double");
					}else if(itemTag.hasKeyOfType(t, 10)){
						//Compound
						listaNBT.add(t+sepChar+itemTag.getCompound(t)+sepChar+"compound");
					}else if(itemTag.hasKeyOfType(t, 8)) {
						//String
						listaNBT.add(t+sepChar+itemTag.getString(t));
					}else {
						//compound
						listaNBT.add(t+sepChar+itemTag.get(t));
					}
				}	
			}
			if(!listaNBT.isEmpty()) {
				config.set(path+".nbt", listaNBT);
			}
		}
		
	}
	
	public ItemStack setNBT(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.server.v1_16_R1.ItemStack nuevoItem = org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_16_R1.NBTTagCompound tag = nuevoItem.hasTag() ? nuevoItem.getTag() : new net.minecraft.server.v1_16_R1.NBTTagCompound(); 
		List<String> listaNBT = config.getStringList(path+".nbt");
		for(int i=0;i<listaNBT.size();i++) {
			
			String nbt = listaNBT.get(i);
			String[] sep = nbt.split("\\"+sepChar);
			String id = sep[0];
			String type = sep[sep.length-1];
			if(type.equals("boolean")) {
				tag.setBoolean(sep[0], Boolean.valueOf(sep[1]));
			}else if(type.equals("double")) {
				tag.setDouble(sep[0], Double.valueOf(sep[1]));
			}else if(type.equals("int")) {
				tag.setInt(sep[0], Integer.valueOf(sep[1]));
			}else if(type.equals("compound")) {
				try {
					String finalNBT = nbt.replace(id+sepChar, "").replace(sepChar+"compound", "");
					net.minecraft.server.v1_16_R1.NBTTagCompound tagNew = net.minecraft.server.v1_16_R1.MojangsonParser.parse(finalNBT);
					tag.set(sep[0], tagNew);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				tag.setString(sep[0], nbt.replace(id+sepChar, ""));
			}
			
		}
		nuevoItem.setTag(tag);
		return org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack.asBukkitCopy(nuevoItem);
	}
}
