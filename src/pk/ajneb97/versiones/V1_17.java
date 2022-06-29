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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;



public class V1_17 {
	
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
		if(item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			meta.setUnbreakable(true);
			item.setItemMeta(meta);
		}
		return item;
	}
	
	public boolean getUnbreakable(ItemStack item){
		if(item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			return meta.isUnbreakable();
		}
		return false;
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

	public void guardarSkull(ItemStack item, String path, FileConfiguration config, String nombreJugador) {
		net.minecraft.world.item.ItemStack cabeza = org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack.asNMSCopy(item);
		try {
			net.minecraft.nbt.NBTTagCompound cabezaTag = (net.minecraft.nbt.NBTTagCompound) cabeza.getClass().getMethod("getTag").invoke(cabeza);
			if((boolean) cabeza.getClass().getMethod("hasTag").invoke(cabeza)){
				if((boolean) cabezaTag.getClass().getMethod("hasKey",String.class).invoke(cabezaTag,"SkullOwner")){
					net.minecraft.nbt.NBTTagCompound skullOwner = (net.minecraft.nbt.NBTTagCompound) cabezaTag.getClass().getMethod("getCompound",String.class).invoke(cabezaTag,"SkullOwner");
					
					if((boolean) skullOwner.getClass().getMethod("hasKey",String.class).invoke(skullOwner,"Properties")){
						net.minecraft.nbt.NBTTagCompound propiedades = (net.minecraft.nbt.NBTTagCompound) skullOwner.getClass().getMethod("getCompound",String.class).invoke(skullOwner,"Properties");
						
						if((boolean) propiedades.getClass().getMethod("hasKey",String.class).invoke(propiedades,"textures")){
							net.minecraft.nbt.NBTTagList texturas = (NBTTagList) propiedades.getClass().getMethod("getList",String.class,int.class).invoke(propiedades,"textures",10);
							net.minecraft.nbt.NBTTagCompound compound = (NBTTagCompound) texturas.getClass().getMethod("getCompound",int.class).invoke(texturas,0);
							config.set(path+".skull-texture", (String) compound.getClass().getMethod("getString",String.class).invoke(compound,"Value"));
						}
						
					}
					if((boolean) skullOwner.getClass().getMethod("hasKey",String.class).invoke(skullOwner,"Name")){
						String name = (String) skullOwner.getClass().getMethod("getString",String.class).invoke(skullOwner,"Name");
						if(name.equals("%player%")){
							config.set(path+".skull-owner", nombreJugador);
						}else{
							config.set(path+".skull-owner", (String)skullOwner.getClass().getMethod("getString",String.class).invoke(skullOwner,"Name"));
						}
						
					}
					if((boolean) skullOwner.getClass().getMethod("hasKey",String.class).invoke(skullOwner,"Id")){
						config.set(path+".skull-id", (String)skullOwner.getClass().getMethod("getString",String.class).invoke(skullOwner,"Id"));
					}
				}
			}	
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void guardarSkullDisplay(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.world.item.ItemStack cabeza = org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack.asNMSCopy(item);
		try {
			net.minecraft.nbt.NBTTagCompound cabezaTag = (net.minecraft.nbt.NBTTagCompound) cabeza.getClass().getMethod("getTag").invoke(cabeza);
			if((boolean) cabeza.getClass().getMethod("hasTag").invoke(cabeza)){
				if((boolean) cabezaTag.getClass().getMethod("hasKey",String.class).invoke(cabezaTag,"SkullOwner")){
					net.minecraft.nbt.NBTTagCompound skullOwner = (net.minecraft.nbt.NBTTagCompound) cabezaTag.getClass().getMethod("getCompound",String.class).invoke(cabezaTag,"SkullOwner");
					String skullmeta = "";
					if((boolean) skullOwner.getClass().getMethod("hasKey",String.class).invoke(skullOwner,"Id")){
						skullmeta = (String)skullOwner.getClass().getMethod("getString",String.class).invoke(skullOwner,"Id");
						
					}
					if((boolean) skullOwner.getClass().getMethod("hasKey",String.class).invoke(skullOwner,"Properties")){
						net.minecraft.nbt.NBTTagCompound propiedades = (net.minecraft.nbt.NBTTagCompound) skullOwner.getClass().getMethod("getCompound",String.class).invoke(skullOwner,"Properties");
						if((boolean) propiedades.getClass().getMethod("hasKey",String.class).invoke(propiedades,"textures")){
							net.minecraft.nbt.NBTTagList texturas = (NBTTagList) propiedades.getClass().getMethod("getList",String.class,int.class).invoke(propiedades,"textures",10);
							net.minecraft.nbt.NBTTagCompound compound = (NBTTagCompound) texturas.getClass().getMethod("getCompound",int.class).invoke(texturas,0);
							skullmeta = skullmeta+";"+(String) compound.getClass().getMethod("getString",String.class).invoke(compound,"Value");
						}
						
					}
					if(skullmeta.contains(";")) {
						config.set(path+".display_item_skulldata", skullmeta);
					}
				}
			}	
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	@SuppressWarnings("unchecked")
	public void guardarNBT(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.world.item.ItemStack itemModificado = org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack.asNMSCopy(item);
		try {
			net.minecraft.nbt.NBTTagCompound itemTag = (NBTTagCompound) itemModificado.getClass().getMethod("getTag").invoke(itemModificado);
			if((boolean) itemModificado.getClass().getMethod("hasTag").invoke(itemModificado)){
				
				Set<String> tags = (Set<String>) itemTag.getClass().getMethod("getKeys").invoke(itemTag);
				List<String> listaNBT = new ArrayList<String>();
				for(String t : tags) {
					if(!t.equals("ench") && !t.equals("HideFlags") && !t.equals("display")
							&& !t.equals("SkullOwner") && !t.equals("AttributeModifiers") 
							&& !t.equals("Enchantments") && !t.equals("Damage") && !t.equals("CustomModelData") && !t.equals("Potion")
							&& !t.equals("StoredEnchantments") && !t.equals("CustomPotionColor") && !t.equals("CustomPotionEffects") && !t.equals("Fireworks")
							&& !t.equals("Explosion")&& !t.equals("pages") && !t.equals("title") && !t.equals("author") && !t.equals("resolved")
							&& !t.equals("generation")) {
						
						if((boolean)itemTag.getClass().getMethod("hasKeyOfType",String.class,int.class).invoke(itemTag,t,1)) {
							//boolean
							listaNBT.add(t+";"+itemTag.getClass().getMethod("getBoolean",String.class).invoke(itemTag,t)+";boolean");
							
						}else if((boolean)itemTag.getClass().getMethod("hasKeyOfType",String.class,int.class).invoke(itemTag,t,3)) {
							//int
							listaNBT.add(t+";"+itemTag.getClass().getMethod("getInt",String.class).invoke(itemTag,t)+";int");
						}else if((boolean)itemTag.getClass().getMethod("hasKeyOfType",String.class,int.class).invoke(itemTag,t,6)) {
							//double
							listaNBT.add(t+";"+itemTag.getClass().getMethod("getDouble",String.class).invoke(itemTag,t)+";double");
						}else if((boolean)itemTag.getClass().getMethod("hasKeyOfType",String.class,int.class).invoke(itemTag,t,10)) {
							//Compound
							listaNBT.add(t+";"+itemTag.getClass().getMethod("getCompound",String.class).invoke(itemTag,t)+";compound");
						}else if((boolean)itemTag.getClass().getMethod("hasKeyOfType",String.class,int.class).invoke(itemTag,t,8)) {
							//String
							listaNBT.add(t+";"+itemTag.getClass().getMethod("getString",String.class).invoke(itemTag,t));
						}else {
							//compound
							listaNBT.add(t+";"+itemTag.getClass().getMethod("get",String.class).invoke(itemTag,t));
						}
					}	
				}
				if(!listaNBT.isEmpty()) {
					config.set(path+".nbt", listaNBT);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public ItemStack setNBT(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.world.item.ItemStack nuevoItem = org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack.asNMSCopy(item);
		try {
			boolean hasTag = (boolean) nuevoItem.getClass().getMethod("hasTag").invoke(nuevoItem);
			NBTTagCompound getTag = (NBTTagCompound) nuevoItem.getClass().getMethod("getTag").invoke(nuevoItem);
			
			net.minecraft.nbt.NBTTagCompound tag = hasTag ? getTag : new net.minecraft.nbt.NBTTagCompound(); 
			List<String> listaNBT = config.getStringList(path+".nbt");
			for(int i=0;i<listaNBT.size();i++) {
				
				String nbt = listaNBT.get(i);
				String[] sep = nbt.split(";");
				String id = sep[0];
				String type = sep[sep.length-1];

				if(type.equals("boolean")) {
					tag.getClass().getMethod("setBoolean",String.class,boolean.class).invoke(tag,sep[0],Boolean.valueOf(sep[1]));
				}else if(type.equals("double")) {
					tag.getClass().getMethod("setDouble",String.class,double.class).invoke(tag,sep[0],Double.valueOf(sep[1]));
				}else if(type.equals("int")) {
					tag.getClass().getMethod("setInt",String.class,int.class).invoke(tag,sep[0],Integer.valueOf(sep[1]));
				}else if(type.equals("compound")) {
					try {
						String finalNBT = nbt.replace(id+";", "").replace(";compound", "");
						NBTTagCompound tagNew = (NBTTagCompound) net.minecraft.nbt.MojangsonParser.class.getMethod("parse", String.class).invoke(null, finalNBT);
						tag.getClass().getMethod("set",String.class,NBTBase.class).invoke(tag,sep[0],tagNew);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					tag.getClass().getMethod("setString",String.class,String.class).invoke(tag,sep[0],nbt.replace(id+";", ""));
				}
				
			}
			nuevoItem.getClass().getMethod("setTag",NBTTagCompound.class).invoke(nuevoItem,tag);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack.asBukkitCopy(nuevoItem);
	}
}
