// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import net.minecraft.server.forge.MinecraftForge;

public class ItemBucket extends Item
{
    private int a;
    
    public ItemBucket(final int i, final int j) {
        super(i);
        this.maxStackSize = 1;
        this.a = j;
    }
    
    public ItemStack a(final ItemStack itemstack, final World world, final EntityHuman entityhuman) {
        final float f = 1.0f;
        final float f2 = entityhuman.lastPitch + (entityhuman.pitch - entityhuman.lastPitch) * f;
        final float f3 = entityhuman.lastYaw + (entityhuman.yaw - entityhuman.lastYaw) * f;
        final double d0 = entityhuman.lastX + (entityhuman.locX - entityhuman.lastX) * f;
        final double d2 = entityhuman.lastY + (entityhuman.locY - entityhuman.lastY) * f + 1.62 - entityhuman.height;
        final double d3 = entityhuman.lastZ + (entityhuman.locZ - entityhuman.lastZ) * f;
        final Vec3D vec3d = Vec3D.create(d0, d2, d3);
        final float f4 = MathHelper.cos(-f3 * 0.017453292f - 3.1415927f);
        final float f5 = MathHelper.sin(-f3 * 0.017453292f - 3.1415927f);
        final float f6 = -MathHelper.cos(-f2 * 0.017453292f);
        final float f7 = MathHelper.sin(-f2 * 0.017453292f);
        final float f8 = f5 * f6;
        final float f9 = f4 * f6;
        final double d4 = 5.0;
        final Vec3D vec3d2 = vec3d.add(f8 * d4, f7 * d4, f9 * d4);
        final MovingObjectPosition movingobjectposition = world.rayTrace(vec3d, vec3d2, this.a == 0);
        if (movingobjectposition == null) {
            return itemstack;
        }
        if (movingobjectposition.type == EnumMovingObjectType.TILE) {
            int i = movingobjectposition.b;
            int j = movingobjectposition.c;
            int k = movingobjectposition.d;
            if (!world.a(entityhuman, i, j, k)) {
                return itemstack;
            }
            if (this.a == 0) {
                final ItemStack customBucket = MinecraftForge.fillCustomBucket(world, i, j, k);
                if (customBucket != null) {
                    return customBucket;
                }
                if (world.getMaterial(i, j, k) == Material.WATER && world.getData(i, j, k) == 0) {
                    final PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, i, j, k, -1, itemstack, Item.WATER_BUCKET);
                    if (event.isCancelled()) {
                        return itemstack;
                    }
                    final CraftItemStack itemInHand = (CraftItemStack)event.getItemStack();
                    final byte data = (byte)((itemInHand.getData() == null) ? 0 : itemInHand.getData().getData());
                    world.setTypeId(i, j, k, 0);
                    return new ItemStack(itemInHand.getTypeId(), itemInHand.getAmount(), (int)data);
                }
                else if (world.getMaterial(i, j, k) == Material.LAVA && world.getData(i, j, k) == 0) {
                    final PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, i, j, k, -1, itemstack, Item.LAVA_BUCKET);
                    if (event.isCancelled()) {
                        return itemstack;
                    }
                    final CraftItemStack itemInHand = (CraftItemStack)event.getItemStack();
                    final byte data = (byte)((itemInHand.getData() == null) ? 0 : itemInHand.getData().getData());
                    world.setTypeId(i, j, k, 0);
                    return new ItemStack(itemInHand.getTypeId(), itemInHand.getAmount(), (int)data);
                }
            }
            else if (this.a < 0) {
                final PlayerBucketEmptyEvent event2 = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, i, j, k, movingobjectposition.face, itemstack);
                if (event2.isCancelled()) {
                    return itemstack;
                }
                final CraftItemStack itemInHand2 = (CraftItemStack)event2.getItemStack();
                final byte data2 = (byte)((itemInHand2.getData() == null) ? 0 : itemInHand2.getData().getData());
                return new ItemStack(itemInHand2.getTypeId(), itemInHand2.getAmount(), (int)data2);
            }
            else {
                final int clickedX = i;
                final int clickedY = j;
                final int clickedZ = k;
                if (movingobjectposition.face == 0) {
                    --j;
                }
                if (movingobjectposition.face == 1) {
                    ++j;
                }
                if (movingobjectposition.face == 2) {
                    --k;
                }
                if (movingobjectposition.face == 3) {
                    ++k;
                }
                if (movingobjectposition.face == 4) {
                    --i;
                }
                if (movingobjectposition.face == 5) {
                    ++i;
                }
                if (world.isEmpty(i, j, k) || !world.getMaterial(i, j, k).isBuildable()) {
                    final PlayerBucketEmptyEvent event3 = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, clickedX, clickedY, clickedZ, movingobjectposition.face, itemstack);
                    if (event3.isCancelled()) {
                        return itemstack;
                    }
                    if (world.worldProvider.d && this.a == Block.WATER.id) {
                        world.makeSound(d0 + 0.5, d2 + 0.5, d3 + 0.5, "random.fizz", 0.5f, 2.6f + (world.random.nextFloat() - world.random.nextFloat()) * 0.8f);
                        for (int l = 0; l < 8; ++l) {
                            world.a("largesmoke", i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0);
                        }
                    }
                    else {
                        world.setTypeIdAndData(i, j, k, this.a, 0);
                    }
                    final CraftItemStack itemInHand3 = (CraftItemStack)event3.getItemStack();
                    final byte data3 = (byte)((itemInHand3.getData() == null) ? 0 : itemInHand3.getData().getData());
                    return new ItemStack(itemInHand3.getTypeId(), itemInHand3.getAmount(), (int)data3);
                }
            }
        }
        else if (this.a == 0 && movingobjectposition.entity instanceof EntityCow) {
            final Location loc = movingobjectposition.entity.getBukkitEntity().getLocation();
            final PlayerBucketFillEvent event4 = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), -1, itemstack, Item.MILK_BUCKET);
            if (event4.isCancelled()) {
                return itemstack;
            }
            final CraftItemStack itemInHand4 = (CraftItemStack)event4.getItemStack();
            final byte data4 = (byte)((itemInHand4.getData() == null) ? 0 : itemInHand4.getData().getData());
            return new ItemStack(itemInHand4.getTypeId(), itemInHand4.getAmount(), (int)data4);
        }
        return itemstack;
    }
}
