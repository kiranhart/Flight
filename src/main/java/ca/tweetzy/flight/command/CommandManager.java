/*
 * Flight
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.flight.command;

import ca.tweetzy.flight.comp.enums.ServerProject;
import ca.tweetzy.flight.comp.enums.ServerVersion;
import ca.tweetzy.flight.utils.Common;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Date Created: April 09 2022
 * Time Created: 11:44 p.m.
 *
 * @author Kiran Hart
 */
public final class CommandManager implements CommandExecutor, TabCompleter {

    private final HashMap<String, NestedCommand> commands = new HashMap<>();

    @Setter
    private String playerOnlyMessage = "&cOnly players can use this command!";

    @Setter
    private String consoleOnlyMessage = "&cThis can only be ran by the console!";

    @Setter
    private String noPermissionMessage = "&cYou do not have permission to use that command!";

    @Setter
    private String unknownCommandMessage = "&cThat command does not exist!";

    @Setter
    private List<String> syntaxErrorMessages = Arrays.asList(
            "&8&m-----------------------------------------------------",
            "<center>%pl_name%",
            "<center>&cSeems like you entered that command incorrectly.",
            "",
            "<center>&6<> &f- &7Required arguments",
            "<center>&8[] &f- &7Optional arguments",
            "",
            "<center>&aHere is the correct usage&F:",
            "<center>&f/&e%syntax%",
            "",
            "&8&m-----------------------------------------------------"
    );


    private final JavaPlugin plugin;
    private boolean allowLooseCommands = false;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Set<String> getCommands() {
        return Collections.unmodifiableSet(commands.keySet());
    }

    public List<String> getSubCommands(String command) {
        final NestedCommand nested = command == null ? null : commands.get(command.toLowerCase());

        if (nested == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(nested.children.keySet());
    }

    public Set<Command> getAllCommands() {
        final HashSet<Command> all = new HashSet<>();

        commands.values().stream()
                .filter(c -> c.parent != null && !all.contains(c.parent))
                .forEach(c -> {
                    all.add(c.parent);
                    c.children.values().stream().filter(s -> !all.contains(s)).forEach(all::add);
                });

        return all;
    }

    public CommandManager registerCommandDynamically(String command) {
        CommandManager.registerCommandDynamically(plugin, command, this, this);
        return this;
    }

    public NestedCommand registerCommandDynamically(Command command) {
        final NestedCommand nested = new NestedCommand(command);

        command.getSubCommands().forEach(cmd -> {
            CommandManager.registerCommandDynamically(plugin, cmd, this, this);
            commands.put(cmd.toLowerCase(), nested);
            final PluginCommand pluginCommand = plugin.getCommand(cmd);

            if (pluginCommand != null) {
                pluginCommand.setExecutor(this);
                pluginCommand.setTabCompleter(this);
            } else {
                Common.log("&cFailed to register command: &f/&e" + cmd);
            }
        });

        return nested;
    }

    public NestedCommand addCommand(Command command) {
        final NestedCommand nested = new NestedCommand(command);

        command.getSubCommands().forEach(cmd -> {
            commands.put(cmd.toLowerCase(), nested);
            final PluginCommand pluginCommand = plugin.getCommand(cmd);

            if (pluginCommand == null) {
                Common.log("&cFailed to register command: &f/&e" + cmd);
                return;
            }

            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        });

        return nested;
    }

    public MainCommand addMainCommand(String command) {
        final MainCommand nested = new MainCommand(plugin, command);
        commands.put(command.toLowerCase(), nested.nestedCommands);

        final PluginCommand pluginCommand = plugin.getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
            pluginCommand.setTabCompleter(this);
        } else {
            Common.log("&cFailed to register command: &f/&e" + command);
        }

        return nested;
    }

    public MainCommand getMainCommand(String command) {
        final NestedCommand nested = command == null ? null : commands.get(command.toLowerCase());

        if (nested != null && nested.parent instanceof MainCommand) {
            return (MainCommand) nested.parent;
        }

        return null;
    }

    public CommandManager addCommands(Command... commands) {
        for (Command command : commands) {
            addCommand(command);
        }

        return this;
    }

    public CommandManager setExecutor(String command) {
        final PluginCommand pluginCommand = command == null ? null : plugin.getCommand(command);

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        } else {
            Common.log("&cFailed to register command: &f/&e" + command);
        }

        return this;
    }

    public CommandManager setUseClosestCommand(boolean bool) {
        this.allowLooseCommands = bool;
        return this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] args) {
        // grab the specific command that's being called
        final NestedCommand nested = commands.get(command.getName().toLowerCase());

        if (nested != null) {
            // check to see if we're trying to call a sub-command
            if (args.length != 0 && !nested.children.isEmpty()) {
                final String subCommand = getSubCommand(nested, args);

                if (subCommand != null) {
                    // we have a subcommand to use!
                    final Command sub = nested.children.get(subCommand);
                    // adjust the arguments to match - BREAKING!!
                    int i = subCommand.indexOf(' ') == -1 ? 1 : 2;
                    String[] newArgs = new String[args.length - i];
                    System.arraycopy(args, i, newArgs, 0, newArgs.length);

                    // now process the command
                    processRequirements(sub, commandSender, newArgs);
                    return true;
                }
            }

            // if we've gotten this far, then just use the command we have
            if (nested.parent != null) {
                processRequirements(nested.parent, commandSender, args);
                return true;
            }
        }

        Common.tell(commandSender, unknownCommandMessage);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        // grab the specific command that's being called
        final NestedCommand nested = commands.get(command.getName().toLowerCase());

        if (nested != null) {
            if (args.length == 0 || nested.children.isEmpty()) {
                return nested.parent != null ? nested.parent.tab(sender, args) : null;
            }

            // check for each sub-command that they have access to
            final boolean op = sender.isOp();
            final boolean console = !(sender instanceof Player);

            if (args.length == 1) {
                // suggest sub-commands that this user has access to
                final String arg = args[0].toLowerCase();
                return nested.children.entrySet().stream()
                        .filter(e -> !console || !e.getValue().isNoConsole())
                        .filter(e -> e.getKey().startsWith(arg))
                        .filter(e -> op || e.getValue().getPermissionNode() == null || sender.hasPermission(e.getValue().getPermissionNode()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
            }

            // more than one arg, let's check to see if we have a command here
            final String subCmd = getSubCommand(nested, args);
            Command sub;
            if (subCmd != null && (sub = nested.children.get(subCmd)) != null && (!console || !sub.isNoConsole()) && (op || sub.getPermissionNode() == null || sender.hasPermission(sub.getPermissionNode()))) {
                // adjust the arguments to match - BREAKING!!
                int i = subCmd.indexOf(' ') == -1 ? 1 : 2;
                final String[] newArgs = new String[args.length - i];
                System.arraycopy(args, i, newArgs, 0, newArgs.length);

                // we're good to go!
                return fetchList(sub, newArgs, sender);
            }
        }

        return Collections.emptyList();
    }

    private String getSubCommand(NestedCommand nested, String[] args) {
        final String cmd = args[0].toLowerCase();
        if (nested.children.containsKey(cmd)) {
            return cmd;
        }

        String match = null;
        // support for mulit-argument subcommands
        if (args.length >= 2 && nested.children.keySet().stream().anyMatch(k -> k.indexOf(' ') != -1)) {
            for (int len = args.length; len > 1; --len) {
                final String cmd2 = String.join(" ", Arrays.copyOf(args, len)).toLowerCase();
                if (nested.children.containsKey(cmd2)) {
                    return cmd2;
                }
            }
        }

        // if we don't have a subcommand, should we search for one?
        if (allowLooseCommands) {
            // do a "closest match"
            int count = 0;
            for (String c : nested.children.keySet()) {
                if (c.startsWith(cmd)) {
                    match = c;
                    if (++count > 1) {
                        // there can only be one!
                        match = null;
                        break;
                    }
                }
            }
        }

        return match;
    }

    private void processRequirements(Command command, CommandSender sender, String[] args) {
        if (sender instanceof Player && command.getAllowedExecutor() == AllowedExecutor.CONSOLE) {
            Common.tell(sender, consoleOnlyMessage);
            return;
        }

        if (!(sender instanceof Player) && command.isNoConsole()) {
            Common.tell(sender, playerOnlyMessage);
            return;
        }

        if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
            final ReturnType returnType = command.execute(sender, args);

            if (returnType == ReturnType.REQUIRES_PLAYER) {
                Common.tell(sender, playerOnlyMessage);
                return;
            }

            if (returnType == ReturnType.REQUIRES_CONSOLE) {
                Common.tell(sender, consoleOnlyMessage);
                return;
            }

            if (returnType == ReturnType.INVALID_SYNTAX) {
                for (String s : syntaxErrorMessages) {
                    Common.tellNoPrefix(sender, s
                            .replace("%pl_name%", Common.PLUGIN_NAME)
                            .replace("%syntax%", command.getSyntax()));
                }
            }

            return;
        }

        Common.tell(sender, noPermissionMessage);
    }

    private List<String> fetchList(Command Command, String[] args, CommandSender sender) {
        final List<String> list = Command.tab(sender, args);

        if (args.length != 0) {
            final String str = args[args.length - 1];

            if (list != null && str != null && str.length() >= 1) {
                try {
                    list.removeIf(s -> !s.toLowerCase().startsWith(str.toLowerCase()));
                } catch (UnsupportedOperationException ignore) {
                }
            }
        }

        return list;
    }

    public static void registerCommandDynamically(Plugin plugin, String command, CommandExecutor executor, TabCompleter tabManager) {
        try {
            // Retrieve the SimpleCommandMap from the server
            Class<?> clazzCraftServer = Bukkit.getServer().getClass();
            Object craftServer = clazzCraftServer.cast(Bukkit.getServer());
            SimpleCommandMap commandMap = (SimpleCommandMap) craftServer.getClass()
                    .getDeclaredMethod("getCommandMap").invoke(craftServer);

            // Construct a new Command object
            Constructor<PluginCommand> constructorPluginCommand = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructorPluginCommand.setAccessible(true);
            PluginCommand commandObject = constructorPluginCommand.newInstance(command, plugin);

            // Handle timings for Paper 1.8 more safely
            handlePaperTimings(plugin, commandObject);

            // Set command action
            commandObject.setExecutor(executor);

            // Set tab complete
            commandObject.setTabCompleter(tabManager);

            // Register the command
            Field fieldKnownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            fieldKnownCommands.setAccessible(true);
            Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) fieldKnownCommands.get(commandMap);
            knownCommands.put(command, commandObject);
        } catch (ReflectiveOperationException ex) {
            plugin.getLogger().severe("Error registering command dynamically: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void handlePaperTimings(Plugin plugin, PluginCommand commandObject) {
        try {
            if (ServerProject.isServer(ServerProject.PAPER, ServerProject.TACO) && ServerVersion.isServerVersionBelow(ServerVersion.V1_9)) {
                Class<?> timingsManagerClass = Class.forName("co.aikar.timings.TimingsManager");
                Method getCommandTiming = timingsManagerClass.getMethod("getCommandTiming", String.class, Command.class);
                Field timingsField = PluginCommand.class.getDeclaredField("timings");
                timingsField.setAccessible(true);

                Object timing = getCommandTiming.invoke(null, plugin.getName().toLowerCase(), commandObject);
                if (timing != null) {
                    timingsField.set(commandObject, timing);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to set up command timings: " + e.getMessage());
            // Continue execution even if timings setup fails
        }
    }


    /// special case for things like shops
    public static boolean unregisterCommands(Collection<? extends org.bukkit.command.Command> cmds) {
        boolean changed = false;
        CommandMap commandMap = getCommandMap();
        Map<String, org.bukkit.command.Command> knownCmds = getKnownCommands(commandMap);

        HashMap<String, org.bukkit.command.Command> commandsToCheck = new HashMap<>();

        for (org.bukkit.command.Command c : cmds) {
            commandsToCheck.put(c.getLabel().toLowerCase(), c);
            commandsToCheck.put(c.getName().toLowerCase(), c);
            c.getAliases().forEach(a -> commandsToCheck.put(a.toLowerCase(), c));
        }

        for (Map.Entry<String, org.bukkit.command.Command> check : commandsToCheck.entrySet()) {
            org.bukkit.command.Command mappedCommand = knownCmds.get(check.getKey());
            if (check.getValue().equals(mappedCommand)) {
                mappedCommand.unregister(commandMap);
                knownCmds.remove(check.getKey());
                changed = true;
            } else if (check.getValue() instanceof PluginCommand) {
                PluginCommand checkPCmd = (PluginCommand) check.getValue();

                if (mappedCommand instanceof PluginCommand) {

                    PluginCommand mappedPCmd = (PluginCommand) mappedCommand;
                    CommandExecutor mappedExec = mappedPCmd.getExecutor();

                    if (mappedExec != null && mappedExec.equals(checkPCmd.getExecutor())) {
                        mappedPCmd.setExecutor(null);
                        mappedPCmd.setTabCompleter(null);
                    }
                }
                checkPCmd.setExecutor(emptyExec);
                checkPCmd.setTabCompleter(emptyExec);
            }
        }
        return changed;
    }

    public static CommandMap getCommandMap() {
        Server server = Bukkit.getServer();
        try {
            Method m = server.getClass().getDeclaredMethod("getCommandMap");
            m.setAccessible(true);
            return (CommandMap) m.invoke(Bukkit.getServer());
        } catch (Exception ignored) {
        }
        try {
            Field commandMapField = server.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(server);
        } catch (Exception e) {
            throw new RuntimeException("Could not get commandMap", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, org.bukkit.command.Command> getKnownCommands(CommandMap m) {
        try {
            Method me = m.getClass().getDeclaredMethod("getKnownCommands");
            me.setAccessible(true);
            return (Map<String, org.bukkit.command.Command>) me.invoke(m);
        } catch (Exception ignored) {
        }
        try {
            Field knownCommandsField = m.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            return (Map<String, org.bukkit.command.Command>) knownCommandsField.get(m);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not get knownCommands", e);
        }
    }

    public static final TabExecutor emptyExec = new TabExecutor() {
        @Nullable
        @Override
        public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
            return null;
        }

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] strings) {
            return false;
        }
    };

}
