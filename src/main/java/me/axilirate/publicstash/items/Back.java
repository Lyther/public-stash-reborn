package me.axilirate.publicstash.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Back {
    public static ItemStack getItem() {
        ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta ItemMeta = itemStack.getItemMeta();
        if (ItemMeta != null) {
            ItemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "Back");
            itemStack.setItemMeta(ItemMeta);
        }
        return itemStack;
    }
}
