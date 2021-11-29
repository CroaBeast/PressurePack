package me.croabeast.pressurepack.command;

import me.croabeast.pressurepack.*;
import org.bukkit.command.*;
import org.bukkit.util.*;

import java.util.*;

public class Complete {

    private final Application main;
    private String[] args;

    public Complete(Application main) {
        this.main = main;
        Arrays.asList("pressure-pack", "pack").
                forEach(this::registerCmptr);
    }

    private void registerCmptr(String name) {
        PluginCommand cmd = main.getCommand(name);
        if (cmd != null) cmd.setTabCompleter(mainCompleter());
    }

    private List<String> resultTab(List<?>... lists) {
        List<String> tab = new ArrayList<>();
        for (List<?> list : lists)
            list.forEach(e -> tab.add((String) e));
        return StringUtil.copyPartialMatches(
                args[args.length - 1],
                tab, new ArrayList<>()
        );
    }

    private List<String> resultTab(String... args) {
        return resultTab(Arrays.asList(args));
    }

    private TabCompleter mainCompleter() {
        return (sender, command, alias, args) -> {
            this.args = args;
            if (args.length == 1) return resultTab("reload");
            return new ArrayList<>();
        };
    }
}
