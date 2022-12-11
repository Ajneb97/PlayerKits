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

import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;
import pk.ajneb97.api.PlayerKitsAPI;


public class V1_18_R2 {
	
	private String sepChar;
	public V1_18_R2() {
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
		net.minecraft.world.item.ItemStack cabeza = org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack.asNMSCopy(item);
		try {
			net.minecraft.nbt.NBTTagCompound cabezaTag = (net.minecraft.nbt.NBTTagCompound) cabeza.getClass().getMethod("t").invoke(cabeza);
			if(cabeza.s()){
				if(cabezaTag.e("SkullOwner")){
					net.minecraft.nbt.NBTTagCompound skullOwner = cabezaTag.p("SkullOwner");
					if(skullOwner.e("Properties")){
						net.minecraft.nbt.NBTTagCompound propiedades = skullOwner.p("Properties");
						if(propiedades.e("textures")){
							net.minecraft.nbt.NBTTagList texturas = propiedades.c("textures", 10);
							config.set(path+".skull-texture", texturas.a(0).l("Value"));
						}
						
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
		net.minecraft.world.item.ItemStack cabeza = org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack.asNMSCopy(item);

		try {
			net.minecraft.nbt.NBTTagCompound cabezaTag = (net.minecraft.nbt.NBTTagCompound) cabeza.getClass().getMethod("t").invoke(cabeza);
			if(cabeza.s()){
				if(cabezaTag.e("SkullOwner")){
					net.minecraft.nbt.NBTTagCompound skullOwner = cabezaTag.p("SkullOwner");
					String skullmeta = "";
					if(skullOwner.e("Id")){
						skullmeta = skullOwner.l("Id");
						
					}
					if(skullOwner.e("Properties")){
						net.minecraft.nbt.NBTTagCompound propiedades = skullOwner.p("Properties");
						if(propiedades.e("textures")){
							net.minecraft.nbt.NBTTagList texturas = propiedades.c("textures", 10);
							skullmeta = skullmeta+";"+texturas.a(0).l("Value");
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
	
	public void guardarNBT(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.world.item.ItemStack itemModificado = org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack.asNMSCopy(item);
		try {
			net.minecraft.nbt.NBTTagCompound itemTag = (NBTTagCompound) itemModificado.getClass().getMethod("t").invoke(itemModificado);
			if(itemModificado.s()){
				Set<String> tags = null;
				try {
					tags = (Set<String>) itemTag.getClass().getMethod("d").invoke(itemTag);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<String> listaNBT = new ArrayList<String>();
				for(String t : tags) {
					if(!t.equals("ench") && !t.equals("HideFlags") && !t.equals("display")
							&& !t.equals("SkullOwner") && !t.equals("AttributeModifiers") 
							&& !t.equals("Enchantments") && !t.equals("Damage") && !t.equals("CustomModelData") && !t.equals("Potion")
							&& !t.equals("StoredEnchantments") && !t.equals("CustomPotionColor") && !t.equals("CustomPotionEffects") && !t.equals("Fireworks")
							&& !t.equals("Explosion")&& !t.equals("pages") && !t.equals("title") && !t.equals("author") && !t.equals("resolved")
							&& !t.equals("generation")) {
						if(itemTag.b(t, 1)) {
							//boolean
							listaNBT.add(t+sepChar+itemTag.q(t)+sepChar+"boolean");
						}else if(itemTag.b(t, 3)) {
							//int
							listaNBT.add(t+sepChar+itemTag.h(t)+sepChar+"int");
						}else if(itemTag.b(t, 6)) {
							//double
							listaNBT.add(t+sepChar+itemTag.k(t)+sepChar+"double");
						}else if(itemTag.b(t, 10)){
							//Compound
							listaNBT.add(t+sepChar+itemTag.p(t)+sepChar+"compound");
						}else if(itemTag.b(t, 8)) {
							//String
							listaNBT.add(t+sepChar+itemTag.l(t));
						}else {
							//compound
							listaNBT.add(t+sepChar+itemTag.c(t));
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
		};
		
		
	}
	
	public ItemStack setNBT(ItemStack item, String path, FileConfiguration config) {
		net.minecraft.world.item.ItemStack nuevoItem = org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack.asNMSCopy(item);
		
		try {
			boolean hasTag = (boolean) nuevoItem.getClass().getMethod("s").invoke(nuevoItem);
			NBTTagCompound getTag = (NBTTagCompound) nuevoItem.getClass().getMethod("t").invoke(nuevoItem);
			net.minecraft.nbt.NBTTagCompound tag = hasTag ? getTag : new net.minecraft.nbt.NBTTagCompound(); 
			List<String> listaNBT = config.getStringList(path+".nbt");
			for(int i=0;i<listaNBT.size();i++) {
				
				String nbt = listaNBT.get(i);
				String[] sep = nbt.split("\\"+sepChar);
				String id = sep[0];
				String type = sep[sep.length-1];
				if(type.equals("boolean")) {
					tag.a(sep[0], Boolean.valueOf(sep[1]));
				}else if(type.equals("double")) {
					tag.a(sep[0], Double.valueOf(sep[1]));
				}else if(type.equals("int")) {
					tag.a(sep[0], Integer.valueOf(sep[1]));
				}else if(type.equals("compound")) {
					try {
						String finalNBT = nbt.replace(id+sepChar, "").replace(sepChar+"compound", "");
						NBTTagCompound tagNew = MojangsonParser.a(finalNBT);
						tag.a(sep[0], tagNew);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else {
					tag.a(sep[0], nbt.replace(id+sepChar, ""));
				}
				
			}
			nuevoItem.c(tag);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack.asBukkitCopy(nuevoItem);
	}
}
