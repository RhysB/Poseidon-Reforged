// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Random;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockButton extends Block
{
    protected BlockButton(final int i, final int j) {
        super(i, j, Material.ORIENTABLE);
        this.a(true);
    }
    
    public AxisAlignedBB e(final World world, final int i, final int j, final int k) {
        return null;
    }
    
    public int c() {
        return 20;
    }
    
    public boolean a() {
        return false;
    }
    
    public boolean b() {
        return false;
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k, final int l) {
        return (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) || (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) || (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) || (l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5));
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i - 1, j, k, 5) || world.isBlockSolidOnSide(i + 1, j, k, 4) || world.isBlockSolidOnSide(i, j, k - 1, 3) || world.isBlockSolidOnSide(i, j, k + 1, 2);
    }
    
    public void postPlace(final World world, final int i, final int j, final int k, final int l) {
        int i2 = world.getData(i, j, k);
        final int j2 = i2 & 0x8;
        i2 &= 0x7;
        if (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
            i2 = 4;
        }
        else if (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            i2 = 3;
        }
        else if (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            i2 = 2;
        }
        else if (l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            i2 = 1;
        }
        else {
            i2 = this.g(world, i, j, k);
        }
        world.setData(i, j, k, i2 + j2);
    }
    
    private int g(final World world, final int i, final int j, final int k) {
        if (world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            return 1;
        }
        if (world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            return 2;
        }
        if (world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            return 3;
        }
        if (world.isBlockSolidOnSide(i, j, k + 1, 2)) {
            return 4;
        }
        return 1;
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        if (this.h(world, i, j, k)) {
            final int i2 = world.getData(i, j, k) & 0x7;
            boolean flag = false;
            if (!world.isBlockSolidOnSide(i - 1, j, k, 5) && i2 == 1) {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i + 1, j, k, 4) && i2 == 2) {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i, j, k - 1, 3) && i2 == 3) {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i, j, k + 1, 2) && i2 == 4) {
                flag = true;
            }
            if (flag) {
                this.g(world, i, j, k, world.getData(i, j, k));
                world.setTypeId(i, j, k, 0);
            }
        }
    }
    
    private boolean h(final World world, final int i, final int j, final int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
            return false;
        }
        return true;
    }
    
    public void a(final IBlockAccess iblockaccess, final int i, final int j, final int k) {
        final int l = iblockaccess.getData(i, j, k);
        final int i2 = l & 0x7;
        final boolean flag = (l & 0x8) > 0;
        final float f = 0.375f;
        final float f2 = 0.625f;
        final float f3 = 0.1875f;
        float f4 = 0.125f;
        if (flag) {
            f4 = 0.0625f;
        }
        if (i2 == 1) {
            this.a(0.0f, f, 0.5f - f3, f4, f2, 0.5f + f3);
        }
        else if (i2 == 2) {
            this.a(1.0f - f4, f, 0.5f - f3, 1.0f, f2, 0.5f + f3);
        }
        else if (i2 == 3) {
            this.a(0.5f - f3, f, 0.0f, 0.5f + f3, f2, f4);
        }
        else if (i2 == 4) {
            this.a(0.5f - f3, f, 1.0f - f4, 0.5f + f3, f2, 1.0f);
        }
    }
    
    public void b(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        this.interact(world, i, j, k, entityhuman);
    }
    
    public boolean interact(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        final int l = world.getData(i, j, k);
        final int i2 = l & 0x7;
        final int j2 = 8 - (l & 0x8);
        if (j2 == 0) {
            return true;
        }
        final org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
        final int old = (j2 != 8) ? 1 : 0;
        final int current = (j2 == 8) ? 1 : 0;
        final BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
        world.getServer().getPluginManager().callEvent((Event)eventRedstone);
        if (eventRedstone.getNewCurrent() > 0 != (j2 == 8)) {
            return true;
        }
        world.setData(i, j, k, i2 + j2);
        world.b(i, j, k, i, j, k);
        world.makeSound(i + 0.5, j + 0.5, k + 0.5, "random.click", 0.3f, 0.6f);
        world.applyPhysics(i, j, k, this.id);
        if (i2 == 1) {
            world.applyPhysics(i - 1, j, k, this.id);
        }
        else if (i2 == 2) {
            world.applyPhysics(i + 1, j, k, this.id);
        }
        else if (i2 == 3) {
            world.applyPhysics(i, j, k - 1, this.id);
        }
        else if (i2 == 4) {
            world.applyPhysics(i, j, k + 1, this.id);
        }
        else {
            world.applyPhysics(i, j - 1, k, this.id);
        }
        world.c(i, j, k, this.id, this.c());
        return true;
    }
    
    public void remove(final World world, final int i, final int j, final int k) {
        final int l = world.getData(i, j, k);
        if ((l & 0x8) > 0) {
            world.applyPhysics(i, j, k, this.id);
            final int i2 = l & 0x7;
            if (i2 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            }
            else if (i2 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            }
            else if (i2 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            }
            else if (i2 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            }
            else {
                world.applyPhysics(i, j - 1, k, this.id);
            }
        }
        super.remove(world, i, j, k);
    }
    
    public boolean a(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int l) {
        return (iblockaccess.getData(i, j, k) & 0x8) > 0;
    }
    
    public boolean d(final World world, final int i, final int j, final int k, final int l) {
        final int i2 = world.getData(i, j, k);
        if ((i2 & 0x8) == 0x0) {
            return false;
        }
        final int j2 = i2 & 0x7;
        return (j2 == 5 && l == 1) || (j2 == 4 && l == 2) || (j2 == 3 && l == 3) || (j2 == 2 && l == 4) || (j2 == 1 && l == 5);
    }
    
    public boolean isPowerSource() {
        return true;
    }
    
    public void a(final World world, final int i, final int j, final int k, final Random random) {
        if (!world.isStatic) {
            final int l = world.getData(i, j, k);
            if ((l & 0x8) != 0x0) {
                final org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
                final BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 1, 0);
                world.getServer().getPluginManager().callEvent((Event)eventRedstone);
                if (eventRedstone.getNewCurrent() > 0) {
                    return;
                }
                world.setData(i, j, k, l & 0x7);
                world.applyPhysics(i, j, k, this.id);
                final int i2 = l & 0x7;
                if (i2 == 1) {
                    world.applyPhysics(i - 1, j, k, this.id);
                }
                else if (i2 == 2) {
                    world.applyPhysics(i + 1, j, k, this.id);
                }
                else if (i2 == 3) {
                    world.applyPhysics(i, j, k - 1, this.id);
                }
                else if (i2 == 4) {
                    world.applyPhysics(i, j, k + 1, this.id);
                }
                else {
                    world.applyPhysics(i, j - 1, k, this.id);
                }
                world.makeSound(i + 0.5, j + 0.5, k + 0.5, "random.click", 0.3f, 0.5f);
                world.b(i, j, k, i, j, k);
            }
        }
    }
}
