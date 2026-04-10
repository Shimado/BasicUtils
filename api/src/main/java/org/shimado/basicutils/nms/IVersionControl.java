package org.shimado.basicutils.nms;

import com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.jetbrains.annotations.NotNull;
import org.shimado.basicutils.instances.Pair;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface IVersionControl {

    @NotNull
    ItemStack createItemWithTag(@NotNull ItemStack item, @NotNull String tag, @NotNull String value);

    @NotNull
    ItemStack createItemWithTags(@NotNull ItemStack item, @NotNull Map<String, String> map);

    @NotNull
    String getTag(@NotNull ItemStack item, @NotNull String tag);

    void moveHeadToBottom(@NotNull Player player);

    void setPixelColor(@NotNull MapCanvas mapCanvas, int x, int y, @NotNull Color color);

    @NotNull
    Color getBasePixelColor(@NotNull MapCanvas mapCanvas, int x, int y);

    void createFirework(@NotNull List<Player> players, @NotNull Location loc, @NotNull ItemStack firework);

    @NotNull
    GameProfile getGameProfile(@NotNull Player player);

    @NotNull
    BlockFace getFacing(@NotNull Player player);

    void spawnParticleNote(@NotNull World world, double x, double y, double z, float color);

    void spawnParticleDust(@NotNull World world, double x, double y, double z, float r, float g, float b);

    @NotNull
    Pair spawnArmorStandByLocation(@NotNull Location loc, @NotNull org.bukkit.inventory.ItemStack itemHead, boolean isSmall, float angleX, float angleY, float angleZ);

    void moveStand(@NotNull Object entity, @NotNull Location newLoc, @NotNull Location oldLoc);

    void removeEntity(@NotNull Object stand);

    void rotateStand(@NotNull Object stand, int angle);

    void spawnArmorStandToPlayer(@NotNull Player player, @NotNull Object stand, @NotNull ItemStack item, @NotNull Object packet);

    @NotNull
    Object createItem(@NotNull List<Player> players, @NotNull Location loc, @NotNull ItemStack itemToDrop, double vectorX, double vectorY, double vectorZ);

}
