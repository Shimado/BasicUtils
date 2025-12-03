package org.shimado.basicutils.inventory;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class InvSessionManager<T extends InvSessionInstance> {

    private final Class<T> clazz;

    public InvSessionManager(@Nonnull Class<T> clazz){
        this.clazz = clazz;
        reload();
    }

    private Map<Player, T> sessions = new HashMap<>();


    @Nonnull
    public Map<Player, T> getSessions(){
        return sessions;
    }


    @Nonnull
    public T getOrCreateNewSession(@Nonnull Player player){
        T session = getSession(player);
        if(session == null){
            try {
                session = clazz.getDeclaredConstructor().newInstance();
                sessions.put(player, session);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return session;
    }


    @Nullable
    public T getSessionOrCloseInventory(@Nonnull Player player){
        T session = sessions.get(player);
        if(session == null) {
            player.closeInventory();
            return null;
        }
        return session;
    }


    @Nullable
    public T getSession(@Nonnull Player player){
        return sessions.get(player);
    }


    public void removeSession(@Nonnull Player player){
        sessions.remove(player);
    }


    public void returnToPreviousPage(@Nonnull Player player){

    }


    public void reload(){
        for(Map.Entry<Player, T> a : sessions.entrySet()){
            if(a.getKey() != null && a.getKey().isOnline()){
                a.getValue().closeInventory(a.getKey());
            }
        }
        sessions.clear();
    }


}
