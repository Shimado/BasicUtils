package org.shimado.basicutils.nms;

import com.mojang.authlib.GameProfile;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.shimado.basicutils.instances.Pair;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.Map;

public interface IVersionControl {

    @Nonnull
    ItemStack createItemWithTag(@Nonnull ItemStack item, @Nonnull String tag, @Nonnull String value);

    @Nonnull
    ItemStack createItemWithTags(@Nonnull ItemStack item, @Nonnull Map<String, String> map);

    @Nonnull
    String getTag(@Nonnull ItemStack item, @Nonnull String tag);

    void moveHeadToBottom(@Nonnull Player player);

    void setPixelColor(@Nonnull MapCanvas mapCanvas, int x, int y, @Nonnull Color color);

    @Nonnull
    Color getBasePixelColor(@Nonnull MapCanvas mapCanvas, int x, int y);

    void createFirework(@Nonnull Player player, @Nonnull Location loc, @Nonnull ItemStack firework);

    @Nonnull
    GameProfile getGameProfile(@Nonnull Player player);

    @Nonnull
    BlockFace getFacing(@Nonnull Player player);

    void spawnParticleNote(@Nonnull World world, double x, double y, double z, float color);

    void spawnParticleDust(@Nonnull World world, double x, double y, double z, float r, float g, float b);

    @Nonnull
    Pair spawnArmorStandByLocation(@Nonnull Location loc, @Nonnull org.bukkit.inventory.ItemStack itemHead, boolean isSmall, float angleX, float angleY, float angleZ);

    void moveStand(@Nonnull Object entity, @Nonnull Location newLoc, @Nonnull Location oldLoc);

    void removeEntity(@Nonnull Object stand);

    void rotateStand(@Nonnull Object stand, int angle);

    void spawnArmorStandToPlayer(@Nonnull Player player, @Nonnull Object stand, @Nonnull ItemStack item, @Nonnull Object packet);

    @Nonnull
    Object createItem(@Nonnull List<Player> players, @Nonnull Location loc, @Nonnull ItemStack itemToDrop, double vectorX, double vectorY, double vectorZ);

}
