package me.axilirate.publicstash.commands;

import me.axilirate.publicstash.PublicStash;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class ps implements CommandExecutor {

    public PublicStash publicStash;

    public ps(PublicStash publicStash) {
        this.publicStash = publicStash;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;


            if (args.length == 0) {
                publicStash.openPublicStash(player);
                return true;
            }


            if (args.length == 1) {
                if (!player.hasPermission("publicstash.version.use")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use that command");
                    return true;
                }

                if (args[0].equalsIgnoreCase("version")) {
                    PluginDescriptionFile pdf = publicStash.getDescription();
                    player.sendMessage(pdf.getVersion());
                }
            }


        }


        return true;
    }

}
