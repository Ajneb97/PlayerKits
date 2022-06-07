package pk.ajneb97.versiones;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_13_R2.NBTTagCompound;

public class V1_13_R2 {

	public void guardarSkullDisplay(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.server.v1_13_R2.ItemStack cabeza = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound cabezaTag = cabeza.getTag();
		if(cabeza.hasTag()){
			if(cabezaTag.hasKey("SkullOwner")){
				net.minecraft.server.v1_13_R2.NBTTagCompound skullOwner = cabezaTag.getCompound("SkullOwner");
				String skullmeta = "";
				if(skullOwner.hasKey("Id")){
					skullmeta = skullOwner.getString("Id");
					
				}
				if(skullOwner.hasKey("Properties")){
					net.minecraft.server.v1_13_R2.NBTTagCompound propiedades = skullOwner.getCompound("Properties");
					if(propiedades.hasKey("textures")){
						net.minecraft.server.v1_13_R2.NBTTagList texturas = propiedades.getList("textures", 10);
						skullmeta = skullmeta+";"+texturas.getCompound(0).getString("Value");
					}
				}
				if(skullmeta.contains(";")) {
					config.set(path+".display_item_skulldata", skullmeta);
				}
			}
		}		
	}
	
	public ItemStack setSkull(ItemStack item, String id, String textura) {
		net.minecraft.server.v1_13_R2.ItemStack cabeza = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound tag = cabeza.hasTag() ? cabeza.getTag() : new net.minecraft.server.v1_13_R2.NBTTagCompound(); 
		net.minecraft.server.v1_13_R2.NBTTagCompound skullOwnerCompound = new net.minecraft.server.v1_13_R2.NBTTagCompound();
		net.minecraft.server.v1_13_R2.NBTTagCompound propiedades = new net.minecraft.server.v1_13_R2.NBTTagCompound();
		
		
		net.minecraft.server.v1_13_R2.NBTTagList texturas = new net.minecraft.server.v1_13_R2.NBTTagList();
		net.minecraft.server.v1_13_R2.NBTTagCompound texturasObjeto = new net.minecraft.server.v1_13_R2.NBTTagCompound();
		texturasObjeto.setString("Value", textura);
		texturas.add(texturasObjeto);
		propiedades.set("textures", texturas);
		skullOwnerCompound.set("Properties", propiedades);
		skullOwnerCompound.setString("Id", id);
		tag.set("SkullOwner", skullOwnerCompound);
		cabeza.setTag(tag);
		
		
		return org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asBukkitCopy(cabeza);
	}
	
	public ItemStack setUnbreakable(ItemStack item){
		net.minecraft.server.v1_13_R2.ItemStack stack = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new net.minecraft.server.v1_13_R2.NBTTagCompound(); //Create the NMS Stack's NBT (item data)		
		tag.set("Unbreakable", new net.minecraft.server.v1_13_R2.NBTTagByte((byte)1)); //Set unbreakable value to true
		stack.setTag(tag); //Apply the tag to the item
		item = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asBukkitCopy(stack); //Get the bukkit version of the stack
		return item;
	}
	
	public boolean getUnbreakable(ItemStack item){
		net.minecraft.server.v1_13_R2.ItemStack stack = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound tag = new net.minecraft.server.v1_13_R2.NBTTagCompound(); //Create the NMS Stack's NBT (item data)
		tag = stack.getTag();
		if(tag.getBoolean("Unbreakable") == true){
			return true;
		}else{
			return false;
		}
	}

	public ItemStack setSkull(ItemStack crafteos, String path, FileConfiguration config) {
		net.minecraft.server.v1_13_R2.ItemStack cabeza = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(crafteos);
		net.minecraft.server.v1_13_R2.NBTTagCompound tag = cabeza.hasTag() ? cabeza.getTag() : new net.minecraft.server.v1_13_R2.NBTTagCompound(); 
		net.minecraft.server.v1_13_R2.NBTTagCompound skullOwnerCompound = new net.minecraft.server.v1_13_R2.NBTTagCompound();
		net.minecraft.server.v1_13_R2.NBTTagCompound propiedades = new net.minecraft.server.v1_13_R2.NBTTagCompound();
		
		
		net.minecraft.server.v1_13_R2.NBTTagList texturas = new net.minecraft.server.v1_13_R2.NBTTagList();
		net.minecraft.server.v1_13_R2.NBTTagCompound texturasObjeto = new net.minecraft.server.v1_13_R2.NBTTagCompound();
		String pathTextura = path+".skull-texture";

		if(config.contains(pathTextura)){
			String textura = config.getString(pathTextura);
			texturasObjeto.setString("Value", textura);
			texturas.add(texturasObjeto);
			propiedades.set("textures", texturas);
			skullOwnerCompound.set("Properties", propiedades);
		}
		String pathId = path+".skull-id";

		if(config.contains(pathId)){
			String id = config.getString(pathId);
			skullOwnerCompound.setString("Id", id);
		}
		
		
		String pathOwner = path+".skull-owner";

		if(config.contains(pathOwner)){
			String owner = config.getString(pathOwner);
			skullOwnerCompound.setString("Name", owner);
		}
		tag.set("SkullOwner", skullOwnerCompound);
		cabeza.setTag(tag);
		
		
		return org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asBukkitCopy(cabeza);
	}

	public void guardarSkull(ItemStack item, String path, FileConfiguration config, String nombreJugador) {
		net.minecraft.server.v1_13_R2.ItemStack cabeza = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound cabezaTag = cabeza.getTag();
		if(cabeza.hasTag()){
			if(cabezaTag.hasKey("SkullOwner")){
				net.minecraft.server.v1_13_R2.NBTTagCompound skullOwner = cabezaTag.getCompound("SkullOwner");
				if(skullOwner.hasKey("Properties")){
					net.minecraft.server.v1_13_R2.NBTTagCompound propiedades = skullOwner.getCompound("Properties");
					if(propiedades.hasKey("textures")){
						net.minecraft.server.v1_13_R2.NBTTagList texturas = propiedades.getList("textures", 10);
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
	
	public ItemStack setAttributes(ItemStack item, String path, FileConfiguration config){
		net.minecraft.server.v1_13_R2.ItemStack nuevoItem = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound tag = nuevoItem.hasTag() ? nuevoItem.getTag() : new net.minecraft.server.v1_13_R2.NBTTagCompound(); 
		net.minecraft.server.v1_13_R2.NBTTagList attributesList = new net.minecraft.server.v1_13_R2.NBTTagList();
		path = path+".attributes";
		if(config.contains(path)){
			List<String> atributos = config.getStringList(path);
			for(int i=0;i<atributos.size();i++){
				net.minecraft.server.v1_13_R2.NBTTagCompound atributo = new net.minecraft.server.v1_13_R2.NBTTagCompound();
				
				String[] datos = atributos.get(i).split(";");
				String attributeName = datos[0];
				String name = datos[1];
				double amount = Double.valueOf(datos[2]);
				int operation = Integer.valueOf(datos[3]);
				int uuidLeast = Integer.valueOf(datos[4]);
				int uuidMost = Integer.valueOf(datos[5]);
				if(datos.length > 6) {
					String slot = datos[6];
					atributo.setString("Slot", slot);
				}
				
				atributo.setString("AttributeName", attributeName);
				atributo.setString("Name", name);
				atributo.setDouble("Amount", amount);
				atributo.setInt("Operation", operation);
				atributo.setInt("UUIDLeast", uuidLeast);
				atributo.setInt("UUIDMost", uuidMost);
				
				attributesList.add(atributo);
			}
		}
		
		
		tag.set("AttributeModifiers", attributesList);
		nuevoItem.setTag(tag);
		
		return org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asBukkitCopy(nuevoItem);
	}

	public void guardarAttributes(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.server.v1_13_R2.ItemStack itemModificado = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound itemTag =  itemModificado.getTag();
		if(itemModificado.hasTag()){
			if(itemTag.hasKey("AttributeModifiers")){
				net.minecraft.server.v1_13_R2.NBTTagList atributos = itemTag.getList("AttributeModifiers", 10);
				List<String> atributosGuardados = new ArrayList<String>();
				for(int i=0;i<atributos.size();i++){
					String attributeName = atributos.getCompound(i).getString("AttributeName");
					String name = atributos.getCompound(i).getString("Name");
					double amount = atributos.getCompound(i).getDouble("Amount");
					int operation = atributos.getCompound(i).getInt("Operation");
					int uuidLeast = atributos.getCompound(i).getInt("UUIDLeast");
					int uuidMost = atributos.getCompound(i).getInt("UUIDMost");
					String slot = atributos.getCompound(i).getString("Slot");
					
					String juntos = attributeName+";"+name+";"+amount+";"+operation+";"+uuidLeast+";"+uuidMost+";"+slot;
					atributosGuardados.add(juntos);
					
				}
				config.set(path+".attributes", atributosGuardados);
			}
		}
	}

	public void guardarNBT(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.server.v1_13_R2.ItemStack itemModificado = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound itemTag =  itemModificado.getTag();
		if(itemModificado.hasTag()){
			Set<String> tags = itemTag.getKeys();
			List<String> listaNBT = new ArrayList<String>();
			for(String t : tags) {
				if(!t.equals("ench") && !t.equals("HideFlags") && !t.equals("display")
						&& !t.equals("SkullOwner") && !t.equals("AttributeModifiers") 
						&& !t.equals("Enchantments") && !t.equals("Damage") && !t.equals("CustomModelData") && !t.equals("Potion")
						&& !t.equals("StoredEnchantments") && !t.equals("CustomPotionColor") && !t.equals("CustomPotionEffects") && !t.equals("Fireworks")
						&& !t.equals("Explosion")&& !t.equals("pages") && !t.equals("title") && !t.equals("author") && !t.equals("resolved")
						&& !t.equals("generation") && !t.equals("BlockEntityTag")) {
					if(itemTag.hasKeyOfType(t, 1)) {
						//boolean
						listaNBT.add(t+";"+itemTag.getBoolean(t)+";boolean");
					}else if(itemTag.hasKeyOfType(t, 3)) {
						//int
						listaNBT.add(t+";"+itemTag.getInt(t)+";int");
					}else if(itemTag.hasKeyOfType(t, 6)) {
						//double
						listaNBT.add(t+";"+itemTag.getDouble(t)+";double");
					}else if(itemTag.hasKeyOfType(t, 8)) {
						//String
						listaNBT.add(t+";"+itemTag.getString(t));
					}else {
						//compound
						listaNBT.add(t+";"+itemTag.get(t));
					}
				}	
			}
			if(!listaNBT.isEmpty()) {
				config.set(path+".nbt", listaNBT);
			}
		}
		
	}
	
	public ItemStack setNBT(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.server.v1_13_R2.ItemStack nuevoItem = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_13_R2.NBTTagCompound tag = nuevoItem.hasTag() ? nuevoItem.getTag() : new net.minecraft.server.v1_13_R2.NBTTagCompound(); 
		List<String> listaNBT = config.getStringList(path+".nbt");
		for(int i=0;i<listaNBT.size();i++) {
			
			String nbt = listaNBT.get(i);
			String[] sep = nbt.split(";");
			String id = sep[0];
			String type = sep[sep.length-1];
			if(sep[1].startsWith("{")) {
				try {
					net.minecraft.server.v1_13_R2.NBTTagCompound tagNew = net.minecraft.server.v1_13_R2.MojangsonParser.parse(nbt.replace(id+";", ""));
					tag.set(sep[0], tagNew);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else {
				if(type.equals("boolean")) {
					tag.setBoolean(sep[0], Boolean.valueOf(sep[1]));
				}else if(type.equals("double")) {
					tag.setDouble(sep[0], Double.valueOf(sep[1]));
				}else if(type.equals("int")) {
					tag.setInt(sep[0], Integer.valueOf(sep[1]));
				}else {
					tag.setString(sep[0], nbt.replace(id+";", ""));
				}
			}
			
		}
		nuevoItem.setTag(tag);
		return org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asBukkitCopy(nuevoItem);
	}
	
}
