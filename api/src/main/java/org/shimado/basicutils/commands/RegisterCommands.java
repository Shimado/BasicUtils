package org.shimado.basicutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class RegisterCommands<T extends CommandExecutor & TabCompleter> {

    public RegisterCommands(@Nonnull Plugin plugin, @Nonnull T clazz, @Nonnull List<String> commands){
        try {
            for(String cmd : commands){
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

                Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
                constructor.setAccessible(true);
                PluginCommand command = constructor.newInstance(cmd, plugin);

                command.setExecutor(clazz);
                command.setTabCompleter(clazz);
                command.setUsage("/" + cmd);
                commandMap.register(plugin.getDescription().getName(), command);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
