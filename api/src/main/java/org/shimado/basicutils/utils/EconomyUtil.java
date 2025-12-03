package org.shimado.basicutils.utils;

import com.bencodez.votingplugin.VotingPluginMain;
import com.bencodez.votingplugin.user.UserManager;
import com.bencodez.votingplugin.user.VotingPluginUser;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.shimado.basicutils.BasicUtils;

import javax.annotation.Nonnull;
import java.util.UUID;

public class EconomyUtil {

    private Economy eco;
    private PlayerPointsAPI playerPointsAPI;
    private UserManager votingPluginAPI;

    public EconomyUtil(boolean useVault, boolean usePlayerPoints, boolean useVotingPlugin) {
        reload(useVault, usePlayerPoints, useVotingPlugin);
    }


    public boolean isVaultEnabled(){
        return eco != null;
    }


    public boolean isPlayerPointsEnabled(){
        return playerPointsAPI != null;
    }


    public boolean isVotingPluginEnabled(){
        return votingPluginAPI != null;
    }


    private void initEconomy(boolean useVault, boolean usePlayerPoints, boolean useVotingPlugin){
        eco = null;
        playerPointsAPI = null;
        votingPluginAPI = null;

        if(useVault){
            initVault();
        }
        if(usePlayerPoints){
            initPlayerPoints();
        }
        if(useVotingPlugin){
            initVotingPlugin();
        }
    }


    private void initVault() {
        if (!PluginsHook.isVault()) {
            BasicUtils.getPlugin().getLogger().severe("Vault is not exists!");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = BasicUtils.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            BasicUtils.getPlugin().getLogger().severe("Economy plugin for Vault is not exists!");
            return;
        }

        eco = rsp.getProvider();

        if (eco == null) {
            BasicUtils.getPlugin().getLogger().severe("Economy plugin for Vault is not exists!");
        }
    }


    private void initPlayerPoints(){
        if (BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("PlayerPoints") == null) {
            BasicUtils.getPlugin().getLogger().severe("Player points is not exists!");
            return;
        }

        playerPointsAPI = PlayerPoints.getInstance().getAPI();
    }


    private void initVotingPlugin(){
        if (BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("VotingPlugin") == null) {
            BasicUtils.getPlugin().getLogger().severe("Voting plugin is not exists!");
            return;
        }

        votingPluginAPI = VotingPluginMain.getPlugin().getVotingPluginUserManager();
    }


    public double getBalance(@Nonnull UUID playerUUID){
        if(eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            return offlinePlayer == null ? 0.0 : eco.getBalance(offlinePlayer);
        }
        else if(playerPointsAPI != null){
            return playerPointsAPI.lookAsync(playerUUID).getNumberOfDependents();
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            return votingPluginUser == null ? 0.0 : votingPluginAPI.getVotingPluginUser(playerUUID).getPoints();
        }
        return 0.0;
    }


    public void setBalance(@Nonnull UUID playerUUID, double amount) {
        if(eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                removeBalance(playerUUID, getBalance(playerUUID));
                eco.depositPlayer(offlinePlayer, amount);
            }
        }
        else if(playerPointsAPI != null){
            playerPointsAPI.setAsync(playerUUID, (int) Math.floor(amount));
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.setPoints((int) Math.floor(amount));
            }
        }
    }


    public void addBalance(@Nonnull UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                eco.depositPlayer(offlinePlayer, amount);
            }
        }
        else if(playerPointsAPI != null){
            playerPointsAPI.giveAsync(playerUUID, (int) Math.floor(amount));
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.addPoints((int) Math.floor(amount));
            }
        }
    }


    public void removeBalance(@Nonnull UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                eco.withdrawPlayer(offlinePlayer, amount);
            }
        }
        else if(playerPointsAPI != null){
            playerPointsAPI.takeAsync(playerUUID, (int) Math.floor(amount));
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.removePoints((int) Math.floor(amount));
            }
        }
    }


    public boolean isHaveMoney(@Nonnull UUID playerUUID, double amount){
        if(eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            return offlinePlayer == null ? false : eco.getBalance(offlinePlayer) >= amount;
        }
        else if(playerPointsAPI != null){
            return playerPointsAPI.lookAsync(playerUUID).getNumberOfDependents() >= amount;
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            return votingPluginUser == null ? false : votingPluginAPI.getVotingPluginUser(playerUUID).getPoints() >= amount;
        }
        return true;
    }


    public void reload(boolean useVault, boolean usePlayerPoints, boolean useVotingPlugin){
        initEconomy(useVault, usePlayerPoints, useVotingPlugin);
    }

}
