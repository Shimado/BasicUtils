package org.shimado.basicutils.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAnvilHandler {

    @NotNull
    Inventory openAnvil(@NotNull Player player, @NotNull String anvilTitle);

    @Nullable
    String getAnvilText(@NotNull Player player);

}
