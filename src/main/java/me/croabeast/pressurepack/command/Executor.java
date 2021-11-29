package me.croabeast.pressurepack.command;

import me.croabeast.pressurepack.*;
import me.croabeast.pressurepack.utilities.*;
import org.bukkit.command.*;

import java.util.*;

public class Executor {

    private final Application main;
    private final TextUtils text;
    private final PermUtils perms;

    public Executor(Application main) {
        this.main = main;
        this.text = main.getTextUtils();
        this.perms = main.getPermUtils();

        Arrays.asList("pressure-pack", "pack").
                forEach(this::registerCmd);
        new Complete(main);
    }

    private void registerCmd(String name) {
        PluginCommand cmd = main.getCommand(name);
        if (cmd != null) cmd.setExecutor(mainCmd());
    }

    private CommandExecutor mainCmd() {
        return (sender, command, label, args) -> {
            if (!perms.hasPerm(sender, "pressure-pack.reload")) {
                text.send(sender, "not-permission", "PERM", "pressure-pack.reload");
                return true;
            }

            if (args.length == 0) {
                text.send(sender, "wrong-arg", "ARG", "");
                return true;
            }

            if (args.length == 1 && args[0].matches("(?i)reload")) {
                long start = System.currentTimeMillis();
                main.getInitializer().reloadFiles();
                text.send(sender, "reload", "TIME", (System.currentTimeMillis() - start) + "");
                return true;
            }

            text.send(sender, "wrong-arg", "ARG", args[args.length - 1]);
            return true;
        };
    }
}
