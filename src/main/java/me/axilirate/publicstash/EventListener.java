package me.axilirate.publicstash;

import me.axilirate.publicstash.items.Back;
import me.axilirate.publicstash.items.Clear;
import me.axilirate.publicstash.tasks.AutoClear;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
	private final PublicStash publicStash;

	public EventListener(PublicStash publicStash) {
		this.publicStash = publicStash;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory openedInventory = player.getOpenInventory().getTopInventory();

		if (event.getClickedInventory() == null) {
			return;
		}
		if (publicStash.playersOpenedStashIndex.containsKey(player)) {
			ItemStack currentItem = event.getCurrentItem();

			if (!publicStash.inventoryUpdated) {
				event.setCancelled(true);
				return;
			}
			if (currentItem != null) {
				String itemName = currentItem.getType().toString().toUpperCase();
				if (publicStash.disabledItems.contains(itemName)) {
					if (publicStash.debugModeEnabled) {
						System.out.println("Disabled item on InventoryClickEvent: " + itemName);
					}
					event.setCancelled(true);
					return;
				}
				if (currentItem.equals(Back.getItem())) {
					event.setCancelled(true);
					publicStash.openPublicStash(player);
					return;
				}
				if (currentItem.equals(Clear.getItem())) {
					event.setCancelled(true);
					publicStash.inventoryUpdated = false;
					Inventory clearedInventory = Bukkit.createInventory(null, 54);
					publicStash.dataManager.setYamlInventory(publicStash.playersOpenedStashIndex.get(player), clearedInventory);
					publicStash.openPublicStash(player);
					return;
				}
			}
			publicStash.inventoryUpdated = false;

			Bukkit.getScheduler().runTaskLater(publicStash, () -> {
				publicStash.inventoryUpdated = false;
				if (!publicStash.playersOpenedStashIndex.containsKey(player)) {
					return;
				}
				int stashIndex = publicStash.playersOpenedStashIndex.get(player);
				publicStash.dataManager.setYamlInventory(stashIndex, player.getOpenInventory().getTopInventory());
			}, 1);
		}

		if (event.getCurrentItem() == null) {
			return;
		}

		if (!publicStash.playersOpenedStash.containsKey(player)) {
			return;
		}

		event.setCancelled(true);

		if (event.getClickedInventory().equals(openedInventory)) {
			Inventory stashInventory = publicStash.dataManager.getYamlInventory(player, event.getSlot());
			player.openInventory(stashInventory);
			publicStash.playersOpenedStashIndex.put(player, event.getSlot());
		}
	}


	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();

		if (publicStash.playersOpenedStashIndex.containsKey(player)) {
			ItemStack currentItem = event.getCursor();
			if (!publicStash.inventoryUpdated) {
				event.setCancelled(true);
				return;
			}
			if (currentItem != null) {
				String itemName = currentItem.getType().toString().toUpperCase();
				if (publicStash.disabledItems.contains(itemName)) {
					if (publicStash.debugModeEnabled) {
						System.out.println("Disabled item on InventoryDragEvent: " + itemName);
					}
					event.setCancelled(true);
					return;
				}
			}

			publicStash.inventoryUpdated = false;

			Bukkit.getScheduler().runTaskLater(publicStash, () -> {
				publicStash.inventoryUpdated = false;
				if (!publicStash.playersOpenedStashIndex.containsKey(player)) {
					return;
				}
				int stashIndex = publicStash.playersOpenedStashIndex.get(player);
				publicStash.dataManager.setYamlInventory(stashIndex, player.getOpenInventory().getTopInventory());
			}, 1);
		}
	}

	@EventHandler
	public void onItemDespawn(ItemDespawnEvent event) {
		if (!publicStash.despawnedItemsToStash) {
			return;
		}
		if (publicStash.disabledDespawnedItemsToStash.contains(event.getEntity().getItemStack().getType().toString().toUpperCase())) {
			return;
		}

		ItemStack itemStack = event.getEntity().getItemStack();

		for (int stashIndex = 0; stashIndex < publicStash.stashAmount; stashIndex++) {
			Inventory stashInventory = publicStash.dataManager.getYamlInventory(null, stashIndex);
			for (int itemIndex = 0; itemIndex < 54; itemIndex++) {
				ItemStack stashItem = stashInventory.getItem(itemIndex);
				if (stashItem != null) {
					if (stashItem.getType().equals(itemStack.getType())
							&& stashItem.getAmount() + itemStack.getAmount() > stashItem.getMaxStackSize()) {
						continue;
					}
					if (!stashItem.getType().equals(itemStack.getType())) {
						continue;
					}
				}
				stashInventory.addItem(itemStack);
				publicStash.dataManager.setYamlInventory(stashIndex, stashInventory);
				return;
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (publicStash.playersOpenedStash.containsKey(player)) {
			publicStash.playersOpenedStash.remove(player);
		}
		if (publicStash.playersOpenedStashIndex.containsKey(player)) {
			publicStash.playersOpenedStashIndex.remove(player);
			player.setItemOnCursor(null);
		}
	}
}
