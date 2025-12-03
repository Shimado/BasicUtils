package org.shimado.basicutils.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

public class InvSessionInstance {

    private Inventory inv;
    private boolean changingPage = false;


    public Inventory getInv(){
        return inv;
    }

    public void setInv(Inventory inv){
        this.inv = inv;
    }


    public boolean isChangingPage(){
        return changingPage;
    }

    public void setChangingPage(boolean changingPage){
        this.changingPage = changingPage;
    }


    public void closeInventory(@Nonnull Player player){
        this.setChangingPage(true);
        player.closeInventory();
        this.setChangingPage(false);
    }

}
