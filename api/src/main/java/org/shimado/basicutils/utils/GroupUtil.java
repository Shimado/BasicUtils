package org.shimado.basicutils.utils;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.shimado.basicutils.BasicUtils;

import java.util.List;
import java.util.UUID;

public class GroupUtil {

    private Permission vaultPerms;
    private LuckPermsUtil luckPerms;

    public GroupUtil(){
        setPerms();
    }


    private void setPerms(){
        if(BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("Vault") != null) {
            this.vaultPerms = BasicUtils.getPlugin().getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        }
        if(BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("LuckPerms") != null){
            this.luckPerms = new LuckPermsUtil();
        }
    }


    public List<String> getPlayerGroup(UUID playerUUID, boolean hasExpiry){
        if(this.luckPerms != null){
            return this.luckPerms.getPrimaryPlayerGroup(playerUUID, hasExpiry);
        }
        else if(this.vaultPerms != null){
            try {
                String group = this.vaultPerms.getPrimaryGroup(null, Bukkit.getOfflinePlayer(playerUUID));
                if(group != null) return List.of(group);
            }catch (Exception e){
                return List.of("default");
            }
        }
        return List.of("default");
    }


    public void addGroupToPlayer(UUID playerUUID, String groupName, long expiry, String defaultContextKeys, String contextValue) {
        if(this.luckPerms != null){
            this.luckPerms.addGroupToPlayer(playerUUID, groupName, expiry, defaultContextKeys, contextValue);
        }
        else if(this.vaultPerms != null){
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
            String currentGroup = this.vaultPerms.getPrimaryGroup(null, player);
            if (currentGroup != null && !currentGroup.equalsIgnoreCase("default")) {
                this.vaultPerms.playerRemoveGroup(null, player, currentGroup);
            }
            this.vaultPerms.playerAddGroup(null, player, groupName);
        }
    }


    public void removeGroupFromPlayer(UUID playerUUID, String groupName, boolean hasExpiry) {
        if(groupName.equalsIgnoreCase("default")) return;
        if(this.luckPerms != null){
            this.luckPerms.removeGroupFromPlayer(playerUUID, groupName, hasExpiry);
        }
        else if(this.vaultPerms != null){
            this.vaultPerms.playerRemoveGroup(null, Bukkit.getOfflinePlayer(playerUUID), groupName);
        }
    }


    public boolean hasGroup(UUID playerUUID, String groupName) {
        if(this.luckPerms != null){
            return this.luckPerms.hasGroup(playerUUID, groupName);
        }
        else if(this.vaultPerms != null){
            return this.vaultPerms.playerInGroup(null, Bukkit.getOfflinePlayer(playerUUID), groupName);
        }
        return groupName.equalsIgnoreCase("default");
    }

}
