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

import java.util.UUID;

public class EconomyUtil {

    public EconomyUtil(boolean useVault, boolean usePlayerPoints, boolean useVotingPlugin) {
        reload(useVault, usePlayerPoints, useVotingPlugin);
    }

    private Economy eco;
    private PlayerPointsAPI playerPointsAPI;
    private UserManager votingPluginAPI;


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
        if (BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            BasicUtils.getPlugin().getLogger().severe("Vault is not exists!");
            return;
        }
        RegisteredServiceProvider<Economy> rsp = BasicUtils.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            BasicUtils.getPlugin().getLogger().severe("Economy plugin for Vault is not exists!");
            return;
        }

        this.eco = rsp.getProvider();

        if (this.eco == null) {
            BasicUtils.getPlugin().getLogger().severe("Economy plugin for Vault is not exists!");
        }
    }


    private void initPlayerPoints(){
        if (BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("PlayerPoints") == null) {
            BasicUtils.getPlugin().getLogger().severe("Player points is not exists!");
            return;
        }

        this.playerPointsAPI = PlayerPoints.getInstance().getAPI();
    }


    private void initVotingPlugin(){
        if (BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("VotingPlugin") == null) {
            BasicUtils.getPlugin().getLogger().severe("Voting plugin is not exists!");
            return;
        }

        this.votingPluginAPI = VotingPluginMain.getPlugin().getVotingPluginUserManager();
    }


    public double getBalance(UUID playerUUID){
        if(this.eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            return offlinePlayer == null ? 0.0 : this.eco.getBalance(offlinePlayer);
        }
        else if(this.playerPointsAPI != null){
            return this.playerPointsAPI.lookAsync(playerUUID).getNumberOfDependents();
        }
        else if(this.votingPluginAPI != null){
            VotingPluginUser votingPluginUser = this.votingPluginAPI.getVotingPluginUser(playerUUID);
            return votingPluginUser == null ? 0.0 : this.votingPluginAPI.getVotingPluginUser(playerUUID).getPoints();
        }
        return 0.0;
    }


    public void setBalance(UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(this.eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                removeBalance(playerUUID, getBalance(playerUUID));
                this.eco.depositPlayer(offlinePlayer, amount);
            }
        }
        else if(this.playerPointsAPI != null){
            this.playerPointsAPI.setAsync(playerUUID, (int) Math.floor(amount));
        }
        else if(this.votingPluginAPI != null){
            VotingPluginUser votingPluginUser = this.votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.setPoints((int) Math.floor(amount));
            }
        }
    }


    public void addBalance(UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(this.eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                this.eco.depositPlayer(offlinePlayer, amount);
            }
        }
        else if(this.playerPointsAPI != null){
            this.playerPointsAPI.giveAsync(playerUUID, (int) Math.floor(amount));
        }
        else if(this.votingPluginAPI != null){
            VotingPluginUser votingPluginUser = this.votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.addPoints((int) Math.floor(amount));
            }
        }
    }


    public void removeBalance(UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(this.eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                this.eco.withdrawPlayer(offlinePlayer, amount);
            }
        }
        else if(this.playerPointsAPI != null){
            this.playerPointsAPI.takeAsync(playerUUID, (int) Math.floor(amount));
        }
        else if(this.votingPluginAPI != null){
            VotingPluginUser votingPluginUser = this.votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.removePoints((int) Math.floor(amount));
            }
        }
    }


    public boolean isHaveMoney(UUID playerUUID, double amount){
        if(amount == 0.0) return true;
        if(this.eco != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            return offlinePlayer == null ? false : this.eco.getBalance(offlinePlayer) >= amount;
        }
        else if(this.playerPointsAPI != null){
            return this.playerPointsAPI.lookAsync(playerUUID).getNumberOfDependents() >= amount;
        }
        else if(this.votingPluginAPI != null){
            VotingPluginUser votingPluginUser = this.votingPluginAPI.getVotingPluginUser(playerUUID);
            return votingPluginUser == null ? false : this.votingPluginAPI.getVotingPluginUser(playerUUID).getPoints() >= amount;
        }
        return true;
    }


    public void reload(boolean useVault, boolean usePlayerPoints, boolean useVotingPlugin){
        initEconomy(useVault, usePlayerPoints, useVotingPlugin);
    }

}
