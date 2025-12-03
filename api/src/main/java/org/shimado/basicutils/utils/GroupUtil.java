package org.shimado.basicutils.utils;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.shimado.basicutils.BasicUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class GroupUtil {

    private Permission vaultPerms;
    private LuckPermsUtil luckPerms;

    public GroupUtil(){
        setPerms();
    }


    private void setPerms(){
        if(PluginsHook.isVault()) {
            vaultPerms = BasicUtils.getPlugin().getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        }
        if(PluginsHook.isLuckPerms()){
            luckPerms = new LuckPermsUtil();
        }
    }


    @Nonnull
    public List<String> getPlayerGroup(@Nonnull UUID playerUUID, boolean hasExpiry){
        if(luckPerms != null){
            return luckPerms.getPrimaryPlayerGroup(playerUUID, hasExpiry);
        }
        else if(vaultPerms != null){
            try {
                String group = vaultPerms.getPrimaryGroup(null, Bukkit.getOfflinePlayer(playerUUID));
                if(group != null) return List.of(group);
            }catch (Exception e){
                return List.of("default");
            }
        }
        return List.of("default");
    }


    public void addGroupToPlayer(@Nonnull UUID playerUUID, @Nonnull String groupName, long expiry, @Nonnull String defaultContextKeys, @Nonnull String contextValue) {
        if(luckPerms != null){
            luckPerms.addGroupToPlayer(playerUUID, groupName, expiry, defaultContextKeys, contextValue);
        }
        else if(vaultPerms != null){
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
            String currentGroup = vaultPerms.getPrimaryGroup(null, player);
            if (currentGroup != null && !currentGroup.equalsIgnoreCase("default")) {
                vaultPerms.playerRemoveGroup(null, player, currentGroup);
            }
            vaultPerms.playerAddGroup(null, player, groupName);
        }
    }


    public void removeGroupFromPlayer(@Nonnull UUID playerUUID, @Nonnull String groupName, boolean hasExpiry) {
        if(groupName.equalsIgnoreCase("default")) return;
        if(luckPerms != null){
            luckPerms.removeGroupFromPlayer(playerUUID, groupName, hasExpiry);
        }
        else if(vaultPerms != null){
            vaultPerms.playerRemoveGroup(null, Bukkit.getOfflinePlayer(playerUUID), groupName);
        }
    }


    public boolean hasGroup(@Nonnull UUID playerUUID, @Nonnull String groupName) {
        if(luckPerms != null){
            return luckPerms.hasGroup(playerUUID, groupName);
        }
        else if(vaultPerms != null){
            return vaultPerms.playerInGroup(null, Bukkit.getOfflinePlayer(playerUUID), groupName);
        }
        return groupName.equalsIgnoreCase("default");
    }

}
