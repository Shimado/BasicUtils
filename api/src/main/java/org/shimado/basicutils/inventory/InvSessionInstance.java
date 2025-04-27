package org.shimado.basicutils.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InvSessionInstance {

    private Inventory inv;
    private boolean changingPage = false;

    public void setInv(Inventory inv){
        this.inv = inv;
    }

    public Inventory getInv(){
        return this.inv;
    }


    public void setChangingPage(boolean changingPage){
        this.changingPage = changingPage;
    }

    public boolean isChangingPage(){
        return this.changingPage;
    }


    public void closeInventory(Player player){
        this.setChangingPage(true);
        player.closeInventory();
        this.setChangingPage(false);
    }

}
