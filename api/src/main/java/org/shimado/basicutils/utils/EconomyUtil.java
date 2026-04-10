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

import java.lang.reflect.Method;
import java.util.UUID;

public class EconomyUtil {

    private Economy vaultAPI;
    private PlayerPointsAPI playerPointsAPI;
    private UserManager votingPluginAPI;

    private Object excellentCurrencyCurrency;
    private Method excellentCurrencyGetBalance;
    private Method excellentCurrencySetBalance;
    private Method excellentCurrencyAddBalance;
    private Method excellentCurrencyRemoveBalance;

    public EconomyUtil(boolean useVault, @Nullable String excellentEconomyCurrencyName, boolean usePlayerPoints, boolean useVotingPlugin) {
        reload(useVault, excellentEconomyCurrencyName, usePlayerPoints, useVotingPlugin);
    }


    public boolean isVaultEnabled(){
        return vaultAPI != null;
    }


    public boolean isExcellentCurrencyEnabled(){
        return excellentCurrencyCurrency != null;
    }


    public boolean isPlayerPointsEnabled(){
        return playerPointsAPI != null;
    }


    public boolean isVotingPluginEnabled(){
        return votingPluginAPI != null;
    }


    private void initEconomy(boolean useVault, @Nullable String excellentEconomyCurrencyName, boolean usePlayerPoints, boolean useVotingPlugin){
        vaultAPI = null;
        playerPointsAPI = null;
        votingPluginAPI = null;
        excellentCurrencyCurrency = null;

        if(useVault){
            initVault(excellentEconomyCurrencyName);
        }
        if(usePlayerPoints){
            initPlayerPoints();
        }
        if(useVotingPlugin){
            initVotingPlugin();
        }
    }


    private void initVault(@Nullable String excellentEconomyCurrencyName) {
        if (!PluginsHook.isVault()) {
            BasicUtils.getPlugin().getLogger().severe("Vault is not exists!");
            return;
        }

        if (PluginsHook.isCoinsEngine() && excellentEconomyCurrencyName != null && !excellentEconomyCurrencyName.isEmpty()) {
            Class<?> coinsEngineAPIClass = null;
            Class<?> coinsEngineCurrencyClass = null;

            try {
                coinsEngineAPIClass = Class.forName("su.nightexpress.coinsengine.api.CoinsEngineAPI");
                coinsEngineCurrencyClass = Class.forName("su.nightexpress.coinsengine.api.currency.Currency");
            } catch (Exception e1) {
                try {
                    coinsEngineAPIClass = Class.forName("su.nightexpress.excellenteconomy.api.ExcellentEconomyAPI");
                    coinsEngineCurrencyClass = Class.forName("su.nightexpress.excellenteconomy.api.currency.ExcellentCurrency");
                }catch (Exception e2){}
            }

            try {
                excellentCurrencyCurrency = coinsEngineAPIClass.getDeclaredMethod("getCurrency", String.class).invoke(null, excellentEconomyCurrencyName);
                excellentCurrencyGetBalance = coinsEngineAPIClass.getDeclaredMethod("getBalance", UUID.class, coinsEngineCurrencyClass);
                excellentCurrencySetBalance = coinsEngineAPIClass.getDeclaredMethod("setBalance", UUID.class, coinsEngineCurrencyClass, double.class);
                excellentCurrencyAddBalance = coinsEngineAPIClass.getDeclaredMethod("addBalance", UUID.class, coinsEngineCurrencyClass, double.class);
                excellentCurrencyRemoveBalance = coinsEngineAPIClass.getDeclaredMethod("removeBalance", UUID.class, coinsEngineCurrencyClass, double.class);
            }catch (Exception e3){

            }

            if(excellentCurrencyCurrency != null) return;
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
        if(excellentCurrencyCurrency != null){
            try {
                return (double) excellentCurrencyGetBalance.invoke(null, playerUUID, excellentCurrencyCurrency);
            } catch (Exception e) {
                return 0.0;
            }
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            return offlinePlayer == null ? 0.0 : vaultAPI.getBalance(offlinePlayer);
        }
        else if(playerPointsAPI != null){
            return playerPointsAPI.look(playerUUID);
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            return votingPluginUser == null ? 0.0 : votingPluginAPI.getVotingPluginUser(playerUUID).getPoints();
        }
        return 0.0;
    }


    public void setBalance(@NotNull UUID playerUUID, double amount) {
        if(excellentCurrencyCurrency != null){
            try {
                excellentCurrencySetBalance.invoke(null, playerUUID, excellentCurrencyCurrency, amount);
            } catch (Exception e) {}
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                removeBalance(playerUUID, getBalance(playerUUID));
                vaultAPI.depositPlayer(offlinePlayer, amount);
            }
        }
        else if(playerPointsAPI != null){
            playerPointsAPI.set(playerUUID, (int) Math.round(amount));
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.setPoints((int) Math.round(amount));
            }
        }
    }


    public void addBalance(@NotNull UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(excellentCurrencyCurrency != null){
            try {
                excellentCurrencyAddBalance.invoke(null, playerUUID, excellentCurrencyCurrency, amount);
            } catch (Exception e) {}
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                vaultAPI.depositPlayer(offlinePlayer, amount);
            }
        }
        else if(playerPointsAPI != null){
            playerPointsAPI.giveAsync(playerUUID, (int) Math.round(amount));
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.addPoints((int) Math.round(amount));
            }
        }
    }


    public void removeBalance(@NotNull UUID playerUUID, double amount) {
        if(amount == 0.0) return;
        if(excellentCurrencyCurrency != null){
            try {
                excellentCurrencyRemoveBalance.invoke(null, playerUUID, excellentCurrencyCurrency, amount);
            } catch (Exception e) {}
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            if(offlinePlayer != null){
                vaultAPI.withdrawPlayer(offlinePlayer, amount);
            }
        }
        else if(playerPointsAPI != null){
            playerPointsAPI.take(playerUUID, (int) Math.round(amount));
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            if(votingPluginUser != null){
                votingPluginUser.removePoints((int) Math.round(amount));
            }
        }
    }


    public boolean isHaveMoney(@NotNull UUID playerUUID, double amount){
        if(excellentCurrencyCurrency != null){
            try {
                return (double) excellentCurrencyGetBalance.invoke(null, playerUUID, excellentCurrencyCurrency) >= amount;
            } catch (Exception e) {
                return false;
            }
        }
        else if(vaultAPI != null){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
            return offlinePlayer == null ? false : vaultAPI.getBalance(offlinePlayer) >= amount;
        }
        else if(playerPointsAPI != null){
            return playerPointsAPI.look(playerUUID) >= amount;
        }
        else if(votingPluginAPI != null){
            VotingPluginUser votingPluginUser = votingPluginAPI.getVotingPluginUser(playerUUID);
            return votingPluginUser == null ? false : votingPluginAPI.getVotingPluginUser(playerUUID).getPoints() >= amount;
        }
        return true;
    }


    public void reload(boolean useVault, @Nullable String excellentEconomyCurrencyName, boolean usePlayerPoints, boolean useVotingPlugin){
        initEconomy(useVault, excellentEconomyCurrencyName, usePlayerPoints, useVotingPlugin);
    }

}
