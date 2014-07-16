package uk.co.teddytheteddy.bukkit.minis.uppercut;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Thomas on 15/07/2014.
 */
public class Main extends JavaPlugin implements CommandExecutor, Listener {

    private List<UUID> noFall = new ArrayList<UUID>();
    private Random random = new Random();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getCommand("uppercut").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("uppercut")){
            if(sender.hasPermission("uppercut.do")){
                if(args.length != 1){
                    sender.sendMessage(ChatColor.RED + "Please specify a player to uppercut");
                } else{
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

                    if(offlinePlayer.isOnline()){
                        Player p = (Player) offlinePlayer;
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "The player " + ChatColor.GOLD + sender.getName() + ChatColor.YELLOW + " has upercut " + ChatColor.GRAY + p.getName() + ChatColor.YELLOW + "!");
                        if(p.hasPermission("uppercut.fly")){
                            p.setVelocity(new Vector(random.nextInt(2)+0.5, random.nextInt(3)+2, random.nextInt(2)+0.5));
                            noFall.add(p.getUniqueId());
                        }

                        p.getWorld().createExplosion(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 0, false, false);
                    } else{
                        sender.sendMessage(ChatColor.RED + "That player is not online!");
                    }
                }
            } else{
                sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
            }
        }
        return false;
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event){
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL && noFall.contains(event.getEntity().getUniqueId())){
            event.setCancelled(true);
            noFall.remove(event.getEntity().getUniqueId());
        }
    }
}
