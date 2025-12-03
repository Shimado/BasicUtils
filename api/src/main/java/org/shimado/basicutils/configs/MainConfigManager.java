package org.shimado.basicutils.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class MainConfigManager {

    private Plugin plugin;
    private YamlConfiguration config;
    private YamlConfiguration messages;
    private File configFile;
    private File messageFile;
    private ConfigRunnable configRunnable;
    private ConfigRunnable messagesRunnable;

    public MainConfigManager(Plugin plugin, @Nullable ConfigRunnable configRunnable, @Nullable ConfigRunnable messagesRunnable, @Nullable ConfigUpdateRunnable configUpdateRunnable){
        this.plugin = plugin;
        this.configRunnable = configRunnable;
        this.messagesRunnable = messagesRunnable;
        initConfig(configRunnable, messagesRunnable);
        update(configUpdateRunnable);
    }


    @Nonnull
    public YamlConfiguration getConfig(){
        return config;
    }

    @Nonnull
    public YamlConfiguration getMessages(){
        return messages;
    }


    @FunctionalInterface
    private interface ConfigRunnable {
        int run(@Nonnull YamlConfiguration config, @Nonnull YamlConfiguration messages);
    }

    @FunctionalInterface
    private interface ConfigUpdateRunnable {
        int run(@Nonnull YamlConfiguration config, @Nonnull YamlConfiguration messages, String oldVersion, String newVersion);
    }


    private void initConfig(@Nullable ConfigRunnable configRunnable, @Nullable ConfigRunnable messagesRunnable) {
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        if (!configFile.exists()) {
            try {
                Files.copy(plugin.getResource("config.yml"), configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        initMessageConfig(messagesRunnable);

        // Если не null то выполнит задачу и сохранит
        if(configRunnable != null){
            configRunnable.run(config, messages);
            save();
        }
    }


    private void initMessageConfig(@Nullable ConfigRunnable configRunnable) {
        File folder = new File(plugin.getDataFolder() + File.separator + "Messages");
        String language = config.getString("Language");

        if (!folder.exists()) {
            folder.mkdir();
        }

        File fileLanguage = new File(folder, language + ".yml");

        if (!fileLanguage.exists()) {
            try {
                Files.copy(plugin.getResource("langs/" + ((plugin.getResource("langs/" + language + ".yml") != null) ? language : "en") + ".yml"), fileLanguage.toPath());
                messageFile = fileLanguage;
                messages = YamlConfiguration.loadConfiguration(messageFile);

                // Если не null то выполнит задачу и сохранит
                if(configRunnable != null){
                    configRunnable.run(config, messages);
                    save();
                }

                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        messageFile = fileLanguage;
        messages = YamlConfiguration.loadConfiguration(messageFile);
    }


    private void save(){
        try {
            config.save(configFile);
            messages.save(messageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void update(@Nullable ConfigUpdateRunnable configUpdateRunnable){
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml"), StandardCharsets.UTF_8));
        String oldVersion = config.getString("Version");
        String newVersion = newConfig.getString("Version");

        if(!oldVersion.equals(newVersion)){

            for(String key : newConfig.getKeys(true)){
                if(!config.contains(key)){
                    config.set(key, newConfig.get(key));
                }
            }

            config.set("Version", newVersion);


            FileConfiguration newMessages = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("langs/" + config.getString("Language") + ".yml"), StandardCharsets.UTF_8));

            for(String key : newMessages.getKeys(false)){
                if(!messages.contains(key)){
                    messages.set(key, newMessages.get(key));
                }
            }

            // Если не null то выполнит задачу
            if(configUpdateRunnable != null){
                configUpdateRunnable.run(config, messages, oldVersion, newVersion);
            }

            save();
        }

    }

    public void reload() {
        initConfig(configRunnable, messagesRunnable);
    }

}
