package org.shimado.basicutils.utils;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.MessageUtil;
import org.shimado.basicutils.BasicUtils;

import java.util.List;
import java.util.stream.Collectors;

public class DiscordUtil {

    private boolean isDiscordSRVEnabled = false;

    public DiscordUtil(boolean useDiscordSRV){
        reload(useDiscordSRV);
    }


    public boolean isActive(){
        return this.isDiscordSRVEnabled;
    }


    private void setupDiscordSRV(boolean useDiscordSRV) {
        if(!useDiscordSRV) return;

        if(BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("DiscordSRV") == null) {
            BasicUtils.getPlugin().getLogger().severe("DiscordSRV not found!");
            return;
        }

        this.isDiscordSRVEnabled = true;
    }


    public void sendMessage(String textChannel, List<String> messages){
        if(this.isDiscordSRVEnabled){
            github.scarsz.discordsrv.util.DiscordUtil.sendMessage(
                    DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(textChannel),
                    MessageUtil.translateLegacy(messages.stream().collect(Collectors.joining("\r\n")))
            );
        }
    }


    public void reload(boolean useDiscordSRV){
        setupDiscordSRV(useDiscordSRV);
    }

}
