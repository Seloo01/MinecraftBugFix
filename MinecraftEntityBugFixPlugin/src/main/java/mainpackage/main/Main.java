package mainpackage.main;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin implements Listener, CommandExecutor {

    private static boolean isStarted = false;

    /*
    Minecraft'da çoğu zaman bir şekilde iskeletler birbirlerine hedef almaya başlarlar. Bu sanırım bir
    Minecraft hatası ve bu hatayı bu kod sayesinde ortadan kaldırabiliyoruz.
     */

    /*
    In Minecraft, sometimes skeletons target each other in some way. I think this is a bug of Minecraft
    and this code eliminates this bug.
     */

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        System.out.println("Plugin is activated!");
        getCommand("plugin").setExecutor(this);
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin is deactivated!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equals("plugin")) {
                if (args[0].equals("baslat")) {
                    if (!isStarted) {
                        isStarted = true;
                        player.sendMessage(ChatColor.GREEN + "Plugin is Started");
                    } else {
                        player.sendMessage(ChatColor.BLUE + "This plugin is already activated?");
                    }
                    return true;
                } else if (args[0].equals("kapat")) {
                    if (isStarted) {
                        isStarted = false;
                        player.sendMessage(ChatColor.RED + "Plugin deactivated now!");
                    } else {
                        player.sendMessage(ChatColor.BLUE + "This plugin is already deactivated?");
                    }
                    return false;
                } else {
                    player.sendMessage("You entered the wrong command!");
                }
            }
        }
        return false;
    }

    @EventHandler
    public void moveEvent(EntityTargetEvent e) {
        if (isStarted) {
            if (e.getTarget() instanceof Player) {
                Entity targetEnttiy = e.getEntity();
                if (targetEnttiy instanceof Skeleton) {
                    Skeleton skeleton = (Skeleton) e.getEntity();
                    Player player = (Player) e.getTarget();

                    Location loc = skeleton.getLocation();

                    if (player.getGameMode() != GameMode.CREATIVE) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (isStarted) {
                                    for (Entity ent : loc.getWorld().getNearbyEntities(loc, 16, 16, 16)) {
                                        if (skeleton.getTarget() != player) {
                                            if (player.getGameMode() != GameMode.CREATIVE) {
                                                skeleton.setTarget(player);
                                            } else {
                                                skeleton.setTarget(null);
                                            }
                                        }
                                    }
                                } else {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(this, 0, 5);
                    }
                }
            }
        }
    }
}
