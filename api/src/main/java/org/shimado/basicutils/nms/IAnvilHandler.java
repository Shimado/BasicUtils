package org.shimado.basicutils.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IAnvilHandler {

    @Nonnull
    Inventory openAnvil(@Nonnull Player player, @Nonnull String anvilTitle);

    @Nullable
    String getAnvilText(@Nonnull Player player);

}
