package com.github.idimabr.utils;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ItemBuilder {
	private ItemStack is;
	private final Map<String, ItemMeta> cacheSkull = Maps.newHashMap();

	public ItemBuilder(Material m) {
		this(m, 1);
	}

	public ItemBuilder(String material) {
		boolean isSkull = material.startsWith("eyJ0");
		is = new ItemStack(isSkull ? Material.PLAYER_HEAD : Material.valueOf(material), 1);
		if (isSkull) {
			is.setDurability((short) 3);
			setSkull(material);
		}
	}

	public ItemBuilder(ItemStack is) {
		boolean isSkull = is.getType() == Material.PLAYER_HEAD;
		this.is = is;
		if (isSkull) {
			is.setDurability((short) 3);
		}
	}

	public ItemBuilder(Material m, int quantity) {
		boolean isSkull = m == Material.PLAYER_HEAD;
		is = new ItemStack(m, quantity);
		if (isSkull) {
			is.setDurability((short) 3);
		}
	}

	public ItemBuilder(Material m, int quantity, byte durability) {
		boolean isSkull = m == Material.PLAYER_HEAD;
		is = new ItemStack(m, quantity, durability);
		if (isSkull) {
			is.setDurability((short) 3);
		}
	}

	public ItemBuilder clone() {
		return new ItemBuilder(is);
	}

	public ItemBuilder addGlow() {
		ItemMeta im = is.getItemMeta();
		im.addEnchant(Enchantment.LUCK, 1, true);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder setDurability(short durability) {
		is.setDurability(durability);
		return this;
	}

	public ItemBuilder setQuantity(int number) {
		is.setAmount(number);
		return this;
	}

	public ItemBuilder addNBT(String key, Object value) {
		NBTItem nbt = new NBTItem(is);
		if (value instanceof String) nbt.setString(key, (String) value);
		else if (value instanceof Integer) nbt.setInteger(key, (Integer) value);
		else if (value instanceof Long) nbt.setLong(key, (Long) value);
		is = nbt.getItem();
		return this;
	}

	public ItemBuilder setPotion(PotionEffectType type, int duration, int amplifier) {
		PotionMeta im = (PotionMeta) is.getItemMeta();
		im.addCustomEffect(new PotionEffect(type, duration, amplifier), true);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder setName(String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder setSkullOwner(String nick) {
		if (cacheSkull.containsKey(nick)) {
			is.setItemMeta(cacheSkull.get(nick));
			return this;
		}
		SkullMeta im = (SkullMeta) is.getItemMeta();
		im.setOwningPlayer(Bukkit.getOfflinePlayer(nick));
		is.setItemMeta(im);
		cacheSkull.put(nick, im);
		is.setDurability((short) 3);
		return this;
	}

	public ItemBuilder setLore(List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder replaceLore(String key, String value) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore().stream().map(line -> line.replace(key, value)).collect(Collectors.toList());
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder setLeatherArmorColor(Color color) {
		if (is.getType().name().contains("LEATHER")) {
			LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
			if (meta != null) {
				meta.setColor(color);
				is.setItemMeta(meta);
			}
		}
		return this;
	}

	public ItemBuilder setSkull(String url) {
		if (cacheSkull.containsKey(url)) {
			is.setItemMeta(cacheSkull.get(url));
			return this;
		}
		SkullMeta headMeta = (SkullMeta) is.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), UUID.randomUUID().toString());
		profile.getProperties().put("textures", new Property("textures", url));
		try {
			Field profileField = headMeta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(headMeta, profile);
		} catch (NoSuchFieldException | IllegalAccessException ignored) {
		}
		is.setItemMeta(headMeta);
		cacheSkull.put(url, headMeta);
		return this;
	}

	public ItemStack build() {
		return is;
	}
}