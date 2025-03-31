package org.shimado.basicutils.utils;

import com.bencodez.advancedcore.api.user.UserManager;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.shimado.basicutils.BasicUtils;

import java.util.UUID;

public class EconomyUtil {

    public EconomyUtil() {
        reload();
    }

    private Economy eco;
    private PlayerPointsAPI playerPointsAPI;
    private UserManager votingPluginAPI;


    private void initEconomy(){
        if(!initVault()){
//            if(!initPlayerPoints()){
//                initVotingPlugin();
//            }
        }
    }


    private boolean initVault() {
//        if(!ConfigData.VAULT_ENABLED()){
//            return false;
//        }
        if (BasicUtils.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            BasicUtils.getPlugin().getLogger().severe("VAULT IS NOT EXISTS!");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = BasicUtils.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            BasicUtils.getPlugin().getLogger().severe("ECONOMY PLUGIN FOR VAULT IS NOT EXISTS!");
            return false;
        }

        this.eco = rsp.getProvider();

        if (this.eco == null) {
            BasicUtils.getPlugin().getLogger().severe("ECONOMY PLUGIN FOR VAULT IS NOT EXISTS!");
            return false;
        }

        return true;
    }


//    private boolean initPlayerPoints(){
//        if(!ConfigData.PLAYER_POINTS_ENABLED()){
//            return false;
//        }
//        if (DrawOnMap.getInstance().getServer().getPluginManager().getPlugin("PlayerPoints") == null) {
//            DrawOnMap.getInstance().getLogger().severe("PLAYER POINTS IS NOT EXISTS!");
//            return false;
//        }
//
//        this.playerPointsAPI = PlayerPoints.getInstance().getAPI();
//        return true;
//    }
//
//
//    private boolean initVotingPlugin(){
//        if(!ConfigData.VOTING_PLUGIN_ENABLED()){
//            return false;
//        }
//        if (DrawOnMap.getInstance().getServer().getPluginManager().getPlugin("VotingPlugin") == null) {
//            DrawOnMap.getInstance().getLogger().severe("VOTING PLUGIN IS NOT EXISTS!");
//            return false;
//        }
//
//        this.votingPluginAPI = VotingPluginMain.getPlugin().getVotingPluginUserManager();
//        return true;
//    }
//
//
//    public void addBalance(UUID playerUUID, double amount) {
//        if(amount == 0.0) return;
//        if(this.eco != null){
//            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
//            if(offlinePlayer != null){
//                this.eco.depositPlayer(offlinePlayer, amount);
//            }
//        }
//        else if(this.playerPointsAPI != null){
//            this.playerPointsAPI.giveAsync(playerUUID, (int) Math.floor(amount));
//        }
//        else if(this.votingPluginAPI != null){
//            VotingPluginUser votingPluginUser = this.votingPluginAPI.getVotingPluginUser(playerUUID);
//            if(votingPluginUser != null){
//                votingPluginUser.addPoints((int) Math.floor(amount));
//            }
//        }
//    }
//
//
//    public void removeBalanceByKey(UUID playerUUID, String key) {
//        removeBalance(playerUUID, ConfigData.PRICES_FOR_COMMANDS_AND_FUNCTIONS(key));
//    }
//
//
//    public void removeBalance(UUID playerUUID, double amount) {
//        if(amount == 0.0) return;
//        if(this.eco != null){
//            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
//            if(offlinePlayer != null){
//                this.eco.withdrawPlayer(offlinePlayer, amount);
//            }
//        }
//        else if(this.playerPointsAPI != null){
//            this.playerPointsAPI.takeAsync(playerUUID, (int) Math.floor(amount));
//        }
//        else if(this.votingPluginAPI != null){
//            VotingPluginUser votingPluginUser = this.votingPluginAPI.getVotingPluginUser(playerUUID);
//            if(votingPluginUser != null){
//                votingPluginUser.removePoints((int) Math.floor(amount));
//            }
//        }
//    }
//
//
//    public boolean isHaveMoney(UUID playerUUID, double amount){
//        if(amount == 0.0) return true;
//        if(this.eco != null){
//            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerUUID);
//            return offlinePlayer == null ? false : this.eco.getBalance(offlinePlayer) >= amount;
//        }
//        else if(this.playerPointsAPI != null){
//            return this.playerPointsAPI.lookAsync(playerUUID).getNumberOfDependents() >= amount;
//        }
//        else if(this.votingPluginAPI != null){
//            VotingPluginUser votingPluginUser = this.votingPluginAPI.getVotingPluginUser(playerUUID);
//            return votingPluginUser == null ? false : this.votingPluginAPI.getVotingPluginUser(playerUUID).getPoints() >= amount;
//        }
//        return true;
//    }
//
//
//    public boolean isHaveMoneyToExecute(Player player, String key){
//        if(!isHaveMoney(player.getUniqueId(), ConfigData.PRICES_FOR_COMMANDS_AND_FUNCTIONS(key))){
//            player.sendMessage(ColorUtil.getColor(ConfigData.HAVENT_ENOUGH_MONEY()));
//            SoundUtil.reject(player);
//            return false;
//        }
//        return true;
//    }


    public void reload(){
        initEconomy();
    }

}
