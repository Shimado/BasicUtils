package org.shimado.basicutils.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class MainConfigManager {

    public MainConfigManager(Plugin plugin){
        this.plugin = plugin;
        initConfig();
        initMessageConfig();
        update();
    }

    private Plugin plugin;
    private YamlConfiguration config;
    private YamlConfiguration messages;

    private File configFile;
    private File messageFile;

    public YamlConfiguration getConfig(){
        return config;
    }
    public YamlConfiguration getMessages(){
        return messages;
    }


    private void initConfig() {
        this.configFile = new File(this.plugin.getDataFolder(), "config.yml");

        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdir();
        }

        if (!this.configFile.exists()) {
            try {
                Files.copy(this.plugin.getResource("config.yml"), this.configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }


    private void initMessageConfig() {
        File folder = new File(this.plugin.getDataFolder() + File.separator + "Messages");
        String language = this.config.getString("Language");

        if (!folder.exists()) {
            folder.mkdir();
        }

        File fileLanguage = new File(folder, language + ".yml");

        if (!fileLanguage.exists()) {
            try {
                Files.copy(this.plugin.getResource("langs/" + ((this.plugin.getResource("langs/" + language + ".yml") != null) ? language : "en") + ".yml"), fileLanguage.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.messageFile = fileLanguage;
        this.messages = YamlConfiguration.loadConfiguration(this.messageFile);
    }


    private void update(){
        FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.plugin.getResource("config.yml"), StandardCharsets.UTF_8));
        String oldVersion = this.config.getString("Version");
        String newVersion = newConfig.getString("Version");

        if(!oldVersion.equals(newVersion)){

            for(String key : newConfig.getKeys(true)){
                if(!this.config.contains(key)){
                    this.config.set(key, newConfig.get(key));
                }
            }

            this.config.set("Version", newVersion);
            try {
                this.config.save(this.configFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            FileConfiguration newMessages = YamlConfiguration.loadConfiguration(new InputStreamReader(this.plugin.getResource("langs/" + config.getString("Language") + ".yml"), StandardCharsets.UTF_8));

            for(String key : newMessages.getKeys(false)){
                if(!this.messages.contains(key)){
                    this.messages.set(key, newMessages.get(key));
                }
            }
            try {
                this.messages.save(this.messageFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public void reload() {
        initConfig();
        initMessageConfig();
        update();
    }

}
