package me.axilirate.publicstash.tasks;

import me.axilirate.publicstash.PublicStash;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoClear extends BukkitRunnable {
    public PublicStash publicStash;

    public AutoClear(PublicStash publicStash) {
        this.publicStash = publicStash;
    }

    @Override
    public void run() {
        if (publicStash.debugModeEnabled) {
            publicStash.getLogger().info("Cleared stash");
        }

        for (int stashIndex = 0; stashIndex < publicStash.stashAmount; stashIndex++) {
            Inventory clearedInventory = Bukkit.createInventory(null, 54);
            publicStash.dataManager.setYamlInventory(stashIndex, clearedInventory);
        }
    }
}