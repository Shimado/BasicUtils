package org.shimado.basicutils.license;

import org.shimado.basicutils.BasicUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LicenseHandler {

    private boolean active = false;
    private List<String> whiteList = List.of(
            "",
            "localhost",
            "127.0.0.1",
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


    public LicenseHandler(){
        String ip = BasicUtils.getPlugin().getServer().getIp();
        if(this.whiteList.contains(ip)){
            this.active = true;
            return;
        }

        LicenseManager licenseManager = new LicenseManager(ip, "https://194.135.20.241:8080/validate");

        CompletableFuture.runAsync(() -> {
            try {
                this.active = licenseManager.validate();
            }catch (Exception e){
                this.active = true;
            }
        });
    }

    public boolean isActive(){
        return this.active;
    }

}
