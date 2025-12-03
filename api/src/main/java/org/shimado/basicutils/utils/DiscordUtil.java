package org.shimado.basicutils.utils;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.MessageUtil;
import org.shimado.basicutils.BasicUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class DiscordUtil {

    private boolean isDiscordSRVEnabled = false;

    public DiscordUtil(boolean useDiscordSRV){
        reload(useDiscordSRV);
    }


    public boolean isActive(){
        return isDiscordSRVEnabled;
    }


    private void setupDiscordSRV(boolean useDiscordSRV) {
        isDiscordSRVEnabled = useDiscordSRV;
        if(!useDiscordSRV) return;

        if(!PluginsHook.isDiscordSRV()) {
            BasicUtils.getPlugin().getLogger().severe("DiscordSRV not found!");
            isDiscordSRVEnabled = false;
        }
    }


    public void sendMessage(@Nonnull String textChannel, @Nonnull List<String> messages){
        if(isDiscordSRVEnabled){
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
