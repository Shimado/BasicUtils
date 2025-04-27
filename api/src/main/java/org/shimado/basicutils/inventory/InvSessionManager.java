package org.shimado.basicutils.inventory;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InvSessionManager<T extends InvSessionInstance> {

    private final Class<T> clazz;

    public InvSessionManager(Class<T> clazz){
        this.clazz = clazz;
        reload();
    }

    private Map<Player, T> sessions = new HashMap<>();


    public T getOrCreateNewSession(Player player){
        T session = getSession(player);
        if(session == null){
            try {
                session = clazz.getDeclaredConstructor().newInstance();
                this.sessions.put(player, session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return session;
    }

    public T getSessionOrCloseInventory(Player player){
        T session = this.sessions.get(player);
        if(session == null) {
            player.closeInventory();
            return null;
        }
        return session;
    }

    public T getSession(Player player){
        return this.sessions.get(player);
    }

    public void removeSession(Player player){
        this.sessions.remove(player);
    }


    public void returnToPreviousPage(Player player){

    }


    public void reload(){
        for(Map.Entry<Player, T> a : this.sessions.entrySet()){
            if(a.getKey() != null && a.getKey().isOnline()){
                a.getValue().closeInventory(a.getKey());
            }
        }
        this.sessions.clear();
    }


}
