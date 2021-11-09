package me.axilirate.publicstash;

import me.axilirate.publicstash.items.Back;
import me.axilirate.publicstash.items.Clear;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataManager {
	public PublicStash publicStash;

	public DataManager(PublicStash publicStash) {
		this.publicStash = publicStash;
	}

	public void setYamlInventory(int index, Inventory inventory) {
		File file = new File(publicStash.getDataFolder() + "/data.yml");
		YamlConfiguration yaml_file = YamlConfiguration.loadConfiguration(file);
		yaml_file.set(Integer.toString(index), inventory.getStorageContents());
		saveYamlFile(file, yaml_file);
		publicStash.updateStashInventory(index);
	}

	public void setYamlItemStack(int stashIndex, int slotIndex, ItemStack itemStack) {
		File file = new File(publicStash.getDataFolder() + "/data.yml");
		YamlConfiguration yaml_file = YamlConfiguration.loadConfiguration(file);
		ArrayList<ItemStack> itemStackArrayList = (ArrayList<ItemStack>) yaml_file.get(Integer.toString(stashIndex));

		if (itemStackArrayList == null) {
			return;
		}
		itemStackArrayList.set(slotIndex, itemStack);
		yaml_file.set(Integer.toString(stashIndex), itemStackArrayList);
		saveYamlFile(file, yaml_file);

		publicStash.updateStashItem(stashIndex, slotIndex, itemStack);
	}

	public Inventory getYamlInventory(Player player, int index) {
		File file = new File(publicStash.getDataFolder() + "/data.yml");
		YamlConfiguration yaml_file = YamlConfiguration.loadConfiguration(file);
		ArrayList<ItemStack> itemStackArrayList = (ArrayList<ItemStack>) yaml_file.get(Integer.toString(index));
		String stashTitle = "Stash";

		if (player != null) {
			String language = player.getLocale();
			stashTitle = publicStash.translationYml.getString(language + ".stash-name");
			if (stashTitle == null) {
				stashTitle = publicStash.translationYml.getString("default.stash-name");
			}
			stashTitle = ChatColor.translateAlternateColorCodes('&', stashTitle);
		}

		Inventory inventory = Bukkit.createInventory(player, 54, stashTitle + " " + (index + 1));

		if (itemStackArrayList != null) {
			for (int i = 0; i < itemStackArrayList.size(); i++) {
				inventory.setItem(i, itemStackArrayList.get(i));
			}
		}

		inventory.setItem(45, Back.getItem());
		inventory.setItem(46, Clear.getItem());

		return inventory;
	}

	public String getYamlTranslation(String language, String translated) {
		File file = new File(publicStash.getDataFolder() + "/language.yml");
		YamlConfiguration yaml_file = YamlConfiguration.loadConfiguration(file);

		if (yaml_file.getKeys(false).contains(language)) {
			return yaml_file.getString(language + "." + translated);
		}
		return yaml_file.getString("default." + translated);
	}

	public void saveYamlFile(File file, YamlConfiguration yaml_file) {
		try {
			yaml_file.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
