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

package ca.tweetzy.flight.utils;

import ca.tweetzy.flight.FlightPlugin;
import ca.tweetzy.flight.utils.colors.ColorFormatter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Date Created: April 07 2022
 * Time Created: 2:30 p.m.
 *
 * @author Kiran Hart
 */
@UtilityClass
public final class Common {

    public String PREFIX = "[FlightCore]";
    public String PLUGIN_NAME = "FlightCore";

    public void setPrefix(String prefix) {
        PREFIX = prefix;
    }

    public void setPluginName(String pluginName) {
        PLUGIN_NAME = pluginName;
    }

    public void tell(CommandSender sender,  List<String> messages) {
        tell(sender, true, messages);
    }

    public void tell(CommandSender sender, String... messages) {
        tell(sender, true, messages);
    }

    public void tellNoPrefix(CommandSender sender, String... messages) {
        tell(sender, false, messages);
    }

    public void tellNoPrefix(CommandSender sender, List<String> messages) {
        tell(sender, false, messages);
    }

    public void tell(CommandSender sender, boolean addPrefix, String... messages) {
        final String prefix = (PREFIX.length() == 0 || !addPrefix) ? "" : PREFIX + " ";

        for (String message : messages) {
            message = colorize(prefix + message);
            message = message.replace("%pl_name%", PLUGIN_NAME);
            message = message.replace("%pl_prefix%", PREFIX);

            if (message.startsWith("<center>")) {
                message = message.replace("<center>", "");
                message = ChatUtil.centerMessage(message);
            }

            sender.sendMessage(message);
        }
    }

    public void tell(CommandSender sender, boolean addPrefix, List<String> messages) {
        tell(sender, addPrefix, messages.toArray(new String[0]));
    }

    public void log(String... messages) {
        tell(FlightPlugin.getInstance().getServer().getConsoleSender(), messages);
    }

    public void broadcast(String permission, boolean prefix, String... messages) {
        if (permission == null)
            Bukkit.getOnlinePlayers().forEach(online -> tell(online, prefix, messages));
        else
            Bukkit.getOnlinePlayers().stream().filter(online -> online.hasPermission(permission)).forEach(filtered -> tell(filtered, prefix, messages));
    }

    public void broadcast(String... messages) {
        broadcast(null, true, messages);
    }

    public void broadcastNoPrefix(String... messages) {
        broadcast(null, false, messages);
    }

    public String colorize(String string) {
        return ColorFormatter.process(string);
    }

    public List<String> colorize(List<String> strings) {
        return strings.stream().map(Common::colorize).collect(Collectors.toList());
    }

    /**
     * It takes a pattern and a sentence, and returns true if the pattern matches the sentence
     *
     * @param pattern  The pattern to match against.
     * @param sentence The sentence you want to check.
     *
     * @return A boolean value.
     */
    public boolean match(String pattern, String sentence) {
        Pattern patt = Pattern.compile(ChatColor.stripColor(pattern), Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = patt.matcher(sentence);
        return matcher.find();
    }
}
