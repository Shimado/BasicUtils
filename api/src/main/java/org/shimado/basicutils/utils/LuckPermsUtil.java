package org.shimado.basicutils.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.shimado.basicutils.BasicUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LuckPermsUtil {

    private LuckPerms luckPerms;

    public LuckPermsUtil(){
        this.luckPerms = BasicUtils.getPlugin().getServer().getServicesManager().load(LuckPerms.class);
    }

    public List<String> getPrimaryPlayerGroup(UUID playerUUID, boolean hasExpiry){
        User user = this.luckPerms.getUserManager().getUser(playerUUID);
        if(user == null){
            this.luckPerms.getUserManager().loadUser(playerUUID).join();
            user = this.luckPerms.getUserManager().getUser(playerUUID);
        }

        List<String> groups = new ArrayList<>();
        user.getNodes().stream()
                .filter(node -> node instanceof InheritanceNode)
                .forEach(node -> {
                    InheritanceNode inheritanceNode = (InheritanceNode) node;
                    if (inheritanceNode.hasExpiry()) {
                        if(hasExpiry) groups.add(inheritanceNode.getGroupName());
                    } else {
                        if(!hasExpiry) groups.add(inheritanceNode.getGroupName());
                    }
                });

        return groups;
    }


    public void addGroupToPlayer(UUID playerUUID, String groupName, long expiry, String defaultContextKeys, String contextValue) {
        this.luckPerms.getUserManager().modifyUser(playerUUID, user -> {

            if(expiry != -1){
                List<String> toDeleteList = getPrimaryPlayerGroup(playerUUID, false);
                for(String toDelete : toDeleteList){
                    if(!toDelete.equalsIgnoreCase("default")){
                        Node oldGroupNode = Node.builder("group." + toDelete).build();
                        user.data().remove(oldGroupNode);
                    }
                }
            }

            Node newGroupNode = null;
            if(expiry != -1){
                newGroupNode = Node.builder("group." + groupName)
                        .expiry(Duration.ofSeconds(expiry))
//                        .withContext(defaultContextKeys, contextValue)
                        .build();
            }
            else{
                newGroupNode = Node.builder("group." + groupName)
//                        .withContext(defaultContextKeys, contextValue)
                        .build();
            }
            user.data().add(newGroupNode);

            user.setPrimaryGroup(groupName);
            this.luckPerms.getUserManager().saveUser(user);
        });
    }


    public void removeGroupFromPlayer(UUID playerUUID, String groupName, boolean hasExpiry) {
        User user = this.luckPerms.getUserManager().getUser(playerUUID);
        if(user == null) return;
        user.data().clear(node ->
            node instanceof InheritanceNode && ((InheritanceNode) node).getGroupName().equalsIgnoreCase(groupName) && ((node.hasExpiry() && hasExpiry) || (!node.hasExpiry() && !hasExpiry))
        );
        this.luckPerms.getUserManager().saveUser(user);
    }


    public boolean hasGroup(UUID playerUUID, String groupName) {
        User user = this.luckPerms.getUserManager().getUser(playerUUID);
        if (user == null) return false;
        return user.getNodes().stream().anyMatch(node -> node.getKey().equals("group." + groupName));
    }

}
