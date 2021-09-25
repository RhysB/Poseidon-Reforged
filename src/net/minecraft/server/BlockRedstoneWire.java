// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import net.minecraft.server.forge.IConnectRedstone;
import java.util.Random;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockRedstoneEvent;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BlockRedstoneWire extends Block
{
    private boolean a;
    private Set b;
    
    public BlockRedstoneWire(final int i, final int j) {
        super(i, j, Material.ORIENTABLE);
        this.a = true;
        this.b = new HashSet();
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f);
    }
    
    public int a(final int i, final int j) {
        return this.textureId;
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
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i, j - 1, k, 1);
    }
    
    private void g(final World world, final int i, final int j, final int k) {
        this.a(world, i, j, k, i, j, k);
        final ArrayList arraylist = new ArrayList(this.b);
        this.b.clear();
        for (int l = 0; l < arraylist.size(); ++l) {
            final ChunkPosition chunkposition = arraylist.get(l);
            world.applyPhysics(chunkposition.x, chunkposition.y, chunkposition.z, this.id);
        }
    }
    
    private void a(final World world, final int i, final int j, final int k, final int l, final int i1, final int j1) {
        final int k2 = world.getData(i, j, k);
        int l2 = 0;
        this.a = false;
        final boolean flag = world.isBlockIndirectlyPowered(i, j, k);
        this.a = true;
        if (flag) {
            l2 = 15;
        }
        else {
            for (int i2 = 0; i2 < 4; ++i2) {
                int j2 = i;
                int k3 = k;
                if (i2 == 0) {
                    j2 = i - 1;
                }
                if (i2 == 1) {
                    ++j2;
                }
                if (i2 == 2) {
                    k3 = k - 1;
                }
                if (i2 == 3) {
                    ++k3;
                }
                if (j2 != l || j != i1 || k3 != j1) {
                    l2 = this.getPower(world, j2, j, k3, l2);
                }
                if (world.e(j2, j, k3) && !world.e(i, j + 1, k)) {
                    if (j2 != l || j + 1 != i1 || k3 != j1) {
                        l2 = this.getPower(world, j2, j + 1, k3, l2);
                    }
                }
                else if (!world.e(j2, j, k3) && (j2 != l || j - 1 != i1 || k3 != j1)) {
                    l2 = this.getPower(world, j2, j - 1, k3, l2);
                }
            }
            if (l2 > 0) {
                --l2;
            }
            else {
                l2 = 0;
            }
        }
        if (k2 != l2) {
            final BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(i, j, k), k2, l2);
            world.getServer().getPluginManager().callEvent((Event)event);
            l2 = event.getNewCurrent();
        }
        if (k2 != l2) {
            world.suppressPhysics = true;
            world.setData(i, j, k, l2);
            world.b(i, j, k, i, j, k);
            world.suppressPhysics = false;
            for (int i2 = 0; i2 < 4; ++i2) {
                int j2 = i;
                int k3 = k;
                int l3 = j - 1;
                if (i2 == 0) {
                    j2 = i - 1;
                }
                if (i2 == 1) {
                    ++j2;
                }
                if (i2 == 2) {
                    k3 = k - 1;
                }
                if (i2 == 3) {
                    ++k3;
                }
                if (world.e(j2, j, k3)) {
                    l3 += 2;
                }
                final boolean flag2 = false;
                int i3 = this.getPower(world, j2, j, k3, -1);
                l2 = world.getData(i, j, k);
                if (l2 > 0) {
                    --l2;
                }
                if (i3 >= 0 && i3 != l2) {
                    this.a(world, j2, j, k3, i, j, k);
                }
                i3 = this.getPower(world, j2, l3, k3, -1);
                l2 = world.getData(i, j, k);
                if (l2 > 0) {
                    --l2;
                }
                if (i3 >= 0 && i3 != l2) {
                    this.a(world, j2, l3, k3, i, j, k);
                }
            }
            if (k2 == 0 || l2 == 0) {
                this.b.add(new ChunkPosition(i, j, k));
                this.b.add(new ChunkPosition(i - 1, j, k));
                this.b.add(new ChunkPosition(i + 1, j, k));
                this.b.add(new ChunkPosition(i, j - 1, k));
                this.b.add(new ChunkPosition(i, j + 1, k));
                this.b.add(new ChunkPosition(i, j, k - 1));
                this.b.add(new ChunkPosition(i, j, k + 1));
            }
        }
    }
    
    private void h(final World world, final int i, final int j, final int k) {
        if (world.getTypeId(i, j, k) == this.id) {
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i - 1, j, k, this.id);
            world.applyPhysics(i + 1, j, k, this.id);
            world.applyPhysics(i, j, k - 1, this.id);
            world.applyPhysics(i, j, k + 1, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.applyPhysics(i, j + 1, k, this.id);
        }
    }
    
    public void c(final World world, final int i, final int j, final int k) {
        super.c(world, i, j, k);
        if (!world.isStatic) {
            this.g(world, i, j, k);
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            this.h(world, i - 1, j, k);
            this.h(world, i + 1, j, k);
            this.h(world, i, j, k - 1);
            this.h(world, i, j, k + 1);
            if (world.e(i - 1, j, k)) {
                this.h(world, i - 1, j + 1, k);
            }
            else {
                this.h(world, i - 1, j - 1, k);
            }
            if (world.e(i + 1, j, k)) {
                this.h(world, i + 1, j + 1, k);
            }
            else {
                this.h(world, i + 1, j - 1, k);
            }
            if (world.e(i, j, k - 1)) {
                this.h(world, i, j + 1, k - 1);
            }
            else {
                this.h(world, i, j - 1, k - 1);
            }
            if (world.e(i, j, k + 1)) {
                this.h(world, i, j + 1, k + 1);
            }
            else {
                this.h(world, i, j - 1, k + 1);
            }
        }
    }
    
    public void remove(final World world, final int i, final int j, final int k) {
        super.remove(world, i, j, k);
        if (!world.isStatic) {
            world.applyPhysics(i, j + 1, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            this.g(world, i, j, k);
            this.h(world, i - 1, j, k);
            this.h(world, i + 1, j, k);
            this.h(world, i, j, k - 1);
            this.h(world, i, j, k + 1);
            if (world.e(i - 1, j, k)) {
                this.h(world, i - 1, j + 1, k);
            }
            else {
                this.h(world, i - 1, j - 1, k);
            }
            if (world.e(i + 1, j, k)) {
                this.h(world, i + 1, j + 1, k);
            }
            else {
                this.h(world, i + 1, j - 1, k);
            }
            if (world.e(i, j, k - 1)) {
                this.h(world, i, j + 1, k - 1);
            }
            else {
                this.h(world, i, j - 1, k - 1);
            }
            if (world.e(i, j, k + 1)) {
                this.h(world, i, j + 1, k + 1);
            }
            else {
                this.h(world, i, j - 1, k + 1);
            }
        }
    }
    
    public int getPower(final World world, final int i, final int j, final int k, final int l) {
        if (world.getTypeId(i, j, k) != this.id) {
            return l;
        }
        final int i2 = world.getData(i, j, k);
        return (i2 > l) ? i2 : l;
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        if (!world.isStatic) {
            final int i2 = world.getData(i, j, k);
            final boolean flag = this.canPlace(world, i, j, k);
            if (!flag) {
                this.g(world, i, j, k, i2);
                world.setTypeId(i, j, k, 0);
            }
            else {
                this.g(world, i, j, k);
            }
            super.doPhysics(world, i, j, k, l);
        }
    }
    
    public int a(final int i, final Random random) {
        return Item.REDSTONE.id;
    }
    
    public boolean d(final World world, final int i, final int j, final int k, final int l) {
        return this.a && this.a((IBlockAccess)world, i, j, k, l);
    }
    
    public boolean a(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int l) {
        if (!this.a) {
            return false;
        }
        if (iblockaccess.getData(i, j, k) == 0) {
            return false;
        }
        if (l == 1) {
            return true;
        }
        boolean flag = c(iblockaccess, i - 1, j, k, 1) || (!iblockaccess.e(i - 1, j, k) && c(iblockaccess, i - 1, j - 1, k, -1));
        boolean flag2 = c(iblockaccess, i + 1, j, k, 3) || (!iblockaccess.e(i + 1, j, k) && c(iblockaccess, i + 1, j - 1, k, -1));
        boolean flag3 = c(iblockaccess, i, j, k - 1, 2) || (!iblockaccess.e(i, j, k - 1) && c(iblockaccess, i, j - 1, k - 1, -1));
        boolean flag4 = c(iblockaccess, i, j, k + 1, 0) || (!iblockaccess.e(i, j, k + 1) && c(iblockaccess, i, j - 1, k + 1, -1));
        if (!iblockaccess.e(i, j + 1, k)) {
            if (iblockaccess.e(i - 1, j, k) && c(iblockaccess, i - 1, j + 1, k, -1)) {
                flag = true;
            }
            if (iblockaccess.e(i + 1, j, k) && c(iblockaccess, i + 1, j + 1, k, -1)) {
                flag2 = true;
            }
            if (iblockaccess.e(i, j, k - 1) && c(iblockaccess, i, j + 1, k - 1, -1)) {
                flag3 = true;
            }
            if (iblockaccess.e(i, j, k + 1) && c(iblockaccess, i, j + 1, k + 1, -1)) {
                flag4 = true;
            }
        }
        return (!flag3 && !flag2 && !flag && !flag4 && l >= 2 && l <= 5) || (l == 2 && flag3 && !flag && !flag2) || (l == 3 && flag4 && !flag && !flag2) || (l == 4 && flag && !flag3 && !flag4) || (l == 5 && flag2 && !flag3 && !flag4);
    }
    
    public boolean isPowerSource() {
        return this.a;
    }
    
    public static boolean c(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int l) {
        final int i2 = iblockaccess.getTypeId(i, j, k);
        if (i2 == Block.REDSTONE_WIRE.id) {
            return true;
        }
        if (i2 == 0) {
            return false;
        }
        if (Block.byId[i2] instanceof IConnectRedstone) {
            final IConnectRedstone icr = (IConnectRedstone)Block.byId[i2];
            return icr.canConnectRedstone(iblockaccess, i, j, k, l);
        }
        if (Block.byId[i2].isPowerSource()) {
            return true;
        }
        if (i2 != Block.DIODE_OFF.id && i2 != Block.DIODE_ON.id) {
            return false;
        }
        final int j2 = iblockaccess.getData(i, j, k);
        return l == BedBlockTextures.b[j2 & 0x3];
    }
}
