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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shimado.basicutils.BasicUtils;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

import java.util.UUID;

public class EconomyUtil {

    private Economy vaultAPI;
    private Currency coinsEngineCurrency;
    private PlayerPointsAPI playerPointsAPI;
    private UserManager votingPluginAPI;

    public EconomyUtil(boolean useVault, @Nullable String coinsEngineCurrencyName, boolean usePlayerPoints, boolean useVotingPlugin) {
        reload(useVault, coinsEngineCurrencyName, usePlayerPoints, useVotingPlugin);
    }


    public boolean isVaultEnabled(){
        return vaultAPI != null;
    }


    public boolean isCoinsEngineEnabled(){
        return coinsEngineCurrency != null;
    }


    public boolean isPlayerPointsEnabled(){
        return playerPointsAPI != null;
    }


    public boolean isVotingPluginEnabled(){
        return votingPluginAPI != null;
    }


    private void initEconomy(boolean useVault, @Nullable String coinsEngineCurrencyName, boolean usePlayerPoints, boolean useVotingPlugin){
        vaultAPI = null;
        coinsEngineCurrency = null;
        playerPointsAPI = null;
        votingPluginAPI = null;

        if(useVault){
            initVault(coinsEngineCurrencyName);
        }
        if(usePlayerPoints){
            initPlayerPoints();
        }
        if(useVotingPlugin){
            initVotingPlugin();
        }
    }


    private void initVault(@Nullable String coinsEngineCurrencyName) {
        if (!PluginsHook.isVault()) {
            BasicUtils.getPlugin().getLogger().severe("Vault is not exists!");
            return;
        }

        if (PluginsHook.isCoinsEngine() && coinsEngineCurrencyName != null && !coinsEngineCurrencyName.isEmpty()) {
            coinsEngineCurrency = CoinsEngineAPI.getCurrency(coinsEngineCurrencyName);
            if(coinsEngineCurrency != null) return;
        }

        RegisteredServiceProvider<Economy> rsp = BasicUtils.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            BasicUtils.getPlugin().getLogger().severe("Economy plugin for Vault is not exists!");
            return;
        }

        vaultAPI = rsp.getProvider();

        if (vaultAPI == null) {
            BasicUtils.getPlugin().getLogger().severe("Economy plugin for Vault is not exists!");
        }
    }


    private void initPlayerPoints(){
        if (!PluginsHook.isPlayerPoints()) {
            BasicUtils.getPlugin().getLogger().severe("Player points is not exists!");
            return;
        }

        playerPointsAPI = PlayerPoints.getInstance().getAPI();
    }


    private void initVotingPlugin(){
        if (!PluginsHook.isVotingPlugin()) {
            BasicUtils.getPlugin().getLogger().severe("Voting plugin is not exists!");
            return;
        }

        votingPluginAPI = VotingPluginMain.getPlugin().getVotingPluginUserManager();
    }


    public double getBalance(@NotNull UUID playerUUID){
        if(coinsEngineCurrency != null){
            return CoinsEngineAPI.getBalance(playerUUID, coinsEngineCurrency);
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            return offlinePlayer == null ? 0.0 : vaultAPI.getBalance(offlinePlayer);
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


    public void setBalance(@NotNull UUID playerUUID, double amount) {
        if(coinsEngineCurrency != null){
            CoinsEngineAPI.setBalance(playerUUID, coinsEngineCurrency, amount);
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                removeBalance(playerUUID, getBalance(playerUUID));
                vaultAPI.depositPlayer(offlinePlayer, amount);
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


    public void addBalance(@NotNull UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(coinsEngineCurrency != null){
            CoinsEngineAPI.addBalance(playerUUID, coinsEngineCurrency, amount);
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                vaultAPI.depositPlayer(offlinePlayer, amount);
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


    public void removeBalance(@NotNull UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(coinsEngineCurrency != null){
            CoinsEngineAPI.removeBalance(playerUUID, coinsEngineCurrency, amount);
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                vaultAPI.withdrawPlayer(offlinePlayer, amount);
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


    public boolean isHaveMoney(@NotNull UUID playerUUID, double amount){
        if(coinsEngineCurrency != null){
            return CoinsEngineAPI.getBalance(playerUUID, coinsEngineCurrency) >= amount;
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            return offlinePlayer == null ? false : vaultAPI.getBalance(offlinePlayer) >= amount;
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


    public void reload(boolean useVault, @Nullable String coinsEngineCurrencyName, boolean usePlayerPoints, boolean useVotingPlugin){
        initEconomy(useVault, coinsEngineCurrencyName, usePlayerPoints, useVotingPlugin);
    }

}
