package idk.plugin.transfercommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import java.net.InetSocketAddress;

public class Main extends PluginBase implements Listener {

    private String address;
    private int port;
    private Player target;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("transfer")) {
                if (args.length > 0 && args.length < 4) {
                    this.address = args[0];
                }
                if (args.length == 1) {
                    if (!p.hasPermission("transfer.self")) {
                        p.sendMessage("\u00A7cYou don't have permission to use this command");
                        return true;
                    }
                    try {
                        transfer(p, address);
                    } catch (NullPointerException ex) {
                        p.sendMessage("\u00A7cInvalid address");
                    }
                    return true;
                } else if (args.length == 2) {
                    if (!p.hasPermission("transfer.self")) {
                        p.sendMessage("\u00A7cYou don't have permission to use this command");
                        return true;
                    }
                    try {
                        this.port = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                        p.sendMessage("\u00A7cPort must be an integer");
                        return true;
                    }
                    try {
                        transfer(p, address, port);
                    } catch (NullPointerException ex) {
                        p.sendMessage("\u00A7cInvalid address");
                    }
                    return true;
                } else if (args.length == 3) {
                    if (!p.hasPermission("transfer.others")) {
                        p.sendMessage("\u00A7cYou don't have permission to transfer others");
                        return true;
                    }
                    try {
                        this.port = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                        p.sendMessage("\u00A7cPort must be an integer");
                        return true;
                    }
                    this.target = Server.getInstance().getPlayer(args[2]);
                    if (target == null) {
                        p.sendMessage("\u00A7cUnknown player");
                        return true;
                    }
                    try {
                        transfer(target, address, port);
                    } catch (NullPointerException ex) {
                        p.sendMessage("\u00A7cInvalid address");
                    }
                    return true;
                }
                return false;
            }
        } else if (cmd.getName().equalsIgnoreCase("transfer") && args.length == 3) {
            try {
                this.port = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                sender.sendMessage("\u00A7cPort must be an integer");
                return true;
            }
            this.target = Server.getInstance().getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage("\u00A7cUnknown player");
                return true;
            }
            this.address = args[0];
            try {
                transfer(target, address, port);
            } catch (NullPointerException ex) {
                sender.sendMessage("\u00A7cInvalid address");
            }
            return true;
        }
        return false;
    }

    public void transfer(Player p, String address, int port) {
        InetSocketAddress addr = new InetSocketAddress(address, port);
        p.transfer(addr);
    }

    public void transfer(Player p, String address) {
        InetSocketAddress addr = new InetSocketAddress(address, 19132);
        p.transfer(addr);
    }
}
