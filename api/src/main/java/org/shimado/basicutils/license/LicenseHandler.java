package org.shimado.basicutils.license;

import org.bukkit.plugin.Plugin;
import org.shimado.basicutils.utils.IPUtil;

import java.util.List;

public class LicenseHandler {

    private boolean active = false;
    private List<String> whiteList = List.of(
            "",
            "localhost",
            "127.0.0.1",
            "127.0.1.1",
            "192.168.100.0",
            "192.168.100.1",
            "192.168.100.2",
            "192.168.100.3",
            "192.168.100.4",
            "192.168.100.5",
            "192.168.100.6",
            "192.168.100.7",
            "192.168.100.8",
            "192.168.100.9",
            "192.168.100.10"
    );


    public LicenseHandler(Plugin plugin){
        plugin.getLogger().info("License check in progress...");

        String ip = IPUtil.getServerIP();
        if(this.whiteList.contains(ip)){
            plugin.getLogger().info(plugin.getName() + " plugin is activated because you are playing on the local server!");
            this.active = true;
            return;
        }

        LicenseManager licenseManager = new LicenseManager(ip, plugin.getName(), "http://194.135.20.241:8080/api/validate");

        try {
            this.active = licenseManager.validate();
            if(this.active){
                plugin.getLogger().info(plugin.getName() + " plugin license verified!");
            }else{
                List.of(
                        " ",
                        "!!! LICENSE NOT CONFIRMED !!!",
                        "Go to the discord channel and get verified! Or write a private message on the website where you downloaded it!",
                        "You will need to send the IP of the servers on which you want the plugin to work!",
                        "IP of your server you can find on the server.properties file!",
                        " "
                ).forEach(it -> plugin.getLogger().info(it));
            }
        }catch (Exception e){
            this.active = true;
            throw new RuntimeException(e);
        }
    }

    public boolean isActive(){
        return this.active;
    }

}
