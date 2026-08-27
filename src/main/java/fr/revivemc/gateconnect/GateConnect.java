package fr.revivemc.gateconnect;

import com.viaversion.viaversion.api.Via;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class GateConnect extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("  _____       _        _____                            _   ");
        getLogger().info(" / ____|     | |      / ____|                          | |  ");
        getLogger().info("| |  __  __ _| |_ ___| |     ___  _ __  _ __   ___  ___| |_ ");
        getLogger().info("| | |_ |/ _` | __/ _ \\ |    / _ \\| '_ \\| '_ \\ / _ \\/ __| __|");
        getLogger().info("| |__| | (_| | ||  __/ |___| (_) | | | | | | |  __/ (__| |_ ");
        getLogger().info(" \\_____|\\__,_|\\__\\___|\\_____\\___/|_| |_|_| |_|\\___|\\___|\\__|");

        getLogger().info("");

        getLogger().info("  _____ ______ ____  _____ __      _______  ");
        getLogger().info(" / ____|  ____|  _ \\|  __ \\\\ \\    / /  __ \\ ");
        getLogger().info("| (___ | |__  | |_) | |__) |\\ \\  / /| |__) |");
        getLogger().info(" \\___ \\|  __| |  _ <|  ___/  \\ \\/ / |  ___/ ");
        getLogger().info(" ____) | |____| |_) | |       \\  / | |      ");
        getLogger().info("|_____/|______|____/|_|        \\/ |_|_|      ");

        getLogger().info("");
        getLogger().info("                GateConnect v" + getDescription().getVersion());
        getLogger().info("                ReviveMC Network");

        saveDefaultConfig();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getLogger().info("GateConnect actif.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /gateconnect <serveur> [joueur]");
            return true;
        }

        String server = args[0];
        Player target;

        if (sender instanceof Player) {
            target = (Player) sender;
        } else {
            if (args.length < 2) {
                sender.sendMessage("Usage console: gateconnect <serveur> <joueur>");
                return true;
            }
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                sender.sendMessage("Joueur introuvable : " + args[1]);
                return true;
            }
        }

        int protocol = Via.getAPI().getPlayerVersion(target.getUniqueId());
        int min = getConfig().getInt("servers." + server + ".min", 0);
        int max = getConfig().getInt("servers." + server + ".max", Integer.MAX_VALUE);

        if (protocol < min || protocol > max) {
            getLogger().info("BLOCKED " + target.getName()
                    + " (protocol " + protocol + ") -> " + server
                    + " [min=" + min + ", max=" + max + "]");
            target.sendMessage(color(getConfig().getString("messages.denied",
                    "&cCe serveur n'accepte pas ta version de Minecraft.")));
            return true;
        }

        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("Connect");
            out.writeUTF(server);

            target.sendPluginMessage(this, "BungeeCord", b.toByteArray());

            getLogger().info("CONNECT " + target.getName()
                    + " (protocol " + protocol + ") -> " + server);
        } catch (Exception e) {
            getLogger().warning("Erreur envoi Connect: " + e.getMessage());
        }

        return true;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}