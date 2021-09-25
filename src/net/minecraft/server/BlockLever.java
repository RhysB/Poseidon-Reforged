// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockLever extends Block
{
    protected BlockLever(final int i, final int j) {
        super(i, j, Material.ORIENTABLE);
    }
    
    public AxisAlignedBB e(final World world, final int i, final int j, final int k) {
        return null;
    }
    
    public boolean a() {
        return false;
    }
    
    public boolean b() {
        return false;
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k, final int l) {
        return (l == 1 && world.isBlockSolidOnSide(i, j - 1, k, 1)) || (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) || (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) || (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) || (l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5));
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i - 1, j, k, 5) || world.isBlockSolidOnSide(i + 1, j, k, 4) || world.isBlockSolidOnSide(i, j, k - 1, 3) || world.isBlockSolidOnSide(i, j, k + 1, 2) || world.isBlockSolidOnSide(i, j - 1, k, 1);
    }
    
    public void postPlace(final World world, final int i, final int j, final int k, final int l) {
        int i2 = world.getData(i, j, k);
        final int j2 = i2 & 0x8;
        i2 &= 0x7;
        i2 = -1;
        if (l == 1 && world.isBlockSolidOnSide(i, j - 1, k, 1)) {
            i2 = 5 + world.random.nextInt(2);
        }
        if (l == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
            i2 = 4;
        }
        if (l == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            i2 = 3;
        }
        if (l == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            i2 = 2;
        }
        if (l == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            i2 = 1;
        }
        if (i2 == -1) {
            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
        }
        else {
            world.setData(i, j, k, i2 + j2);
        }
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        if (this.g(world, i, j, k)) {
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
            if (!world.isBlockSolidOnSide(i, j - 1, k, 1) && i2 == 5) {
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i, j - 1, k, 1) && i2 == 6) {
                flag = true;
            }
            if (flag) {
                this.g(world, i, j, k, world.getData(i, j, k));
                world.setTypeId(i, j, k, 0);
            }
        }
    }
    
    private boolean g(final World world, final int i, final int j, final int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
            return false;
        }
        return true;
    }
    
    public void a(final IBlockAccess iblockaccess, final int i, final int j, final int k) {
        final int l = iblockaccess.getData(i, j, k) & 0x7;
        float f = 0.1875f;
        if (l == 1) {
            this.a(0.0f, 0.2f, 0.5f - f, f * 2.0f, 0.8f, 0.5f + f);
        }
        else if (l == 2) {
            this.a(1.0f - f * 2.0f, 0.2f, 0.5f - f, 1.0f, 0.8f, 0.5f + f);
        }
        else if (l == 3) {
            this.a(0.5f - f, 0.2f, 0.0f, 0.5f + f, 0.8f, f * 2.0f);
        }
        else if (l == 4) {
            this.a(0.5f - f, 0.2f, 1.0f - f * 2.0f, 0.5f + f, 0.8f, 1.0f);
        }
        else {
            f = 0.25f;
            this.a(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, 0.6f, 0.5f + f);
        }
    }
    
    public void b(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        this.interact(world, i, j, k, entityhuman);
    }
    
    public boolean interact(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        }
        final int l = world.getData(i, j, k);
        final int i2 = l & 0x7;
        final int j2 = 8 - (l & 0x8);
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
        world.makeSound(i + 0.5, j + 0.5, k + 0.5, "random.click", 0.3f, (j2 > 0) ? 0.6f : 0.5f);
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
        return (j2 == 6 && l == 1) || (j2 == 5 && l == 1) || (j2 == 4 && l == 2) || (j2 == 3 && l == 3) || (j2 == 2 && l == 4) || (j2 == 1 && l == 5);
    }
    
    public boolean isPowerSource() {
        return true;
    }
}
