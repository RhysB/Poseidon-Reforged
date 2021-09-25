// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Random;

public class BlockDiode extends Block
{
    public static final double[] a;
    private static final int[] b;
    private final boolean c;
    
    protected BlockDiode(final int i, final boolean flag) {
        super(i, 6, Material.ORIENTABLE);
        this.c = flag;
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }
    
    public boolean b() {
        return false;
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i, j - 1, k, 1) && super.canPlace(world, i, j, k);
    }
    
    public boolean f(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i, j - 1, k, 1) && super.f(world, i, j, k);
    }
    
    public void a(final World world, final int i, final int j, final int k, final Random random) {
        final int l = world.getData(i, j, k);
        final boolean flag = this.f(world, i, j, k, l);
        if (this.c && !flag) {
            world.setTypeIdAndData(i, j, k, Block.DIODE_OFF.id, l);
        }
        else if (!this.c) {
            world.setTypeIdAndData(i, j, k, Block.DIODE_ON.id, l);
            if (!flag) {
                final int i2 = (l & 0xC) >> 2;
                world.c(i, j, k, Block.DIODE_ON.id, BlockDiode.b[i2] * 2);
            }
        }
    }
    
    public int a(final int i, final int j) {
        if (i == 0) {
            return this.c ? 99 : 115;
        }
        if (i == 1) {
            return this.c ? 147 : 131;
        }
        return 5;
    }
    
    public int a(final int i) {
        return this.a(i, 0);
    }
    
    public boolean d(final World world, final int i, final int j, final int k, final int l) {
        return this.a((IBlockAccess)world, i, j, k, l);
    }
    
    public boolean a(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int l) {
        if (!this.c) {
            return false;
        }
        final int i2 = iblockaccess.getData(i, j, k) & 0x3;
        return (i2 == 0 && l == 3) || (i2 == 1 && l == 4) || (i2 == 2 && l == 2) || (i2 == 3 && l == 5);
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        if (!this.f(world, i, j, k)) {
            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
            return;
        }
        final int i2 = world.getData(i, j, k);
        final boolean flag = this.f(world, i, j, k, i2);
        final int j2 = (i2 & 0xC) >> 2;
        if (this.c && !flag) {
            world.c(i, j, k, this.id, BlockDiode.b[j2] * 2);
        }
        else if (!this.c && flag) {
            world.c(i, j, k, this.id, BlockDiode.b[j2] * 2);
        }
    }
    
    private boolean f(final World world, final int i, final int j, final int k, final int l) {
        final int i2 = l & 0x3;
        switch (i2) {
            case 0: {
                return world.isBlockFaceIndirectlyPowered(i, j, k + 1, 3) || (world.getTypeId(i, j, k + 1) == Block.REDSTONE_WIRE.id && world.getData(i, j, k + 1) > 0);
            }
            case 2: {
                return world.isBlockFaceIndirectlyPowered(i, j, k - 1, 2) || (world.getTypeId(i, j, k - 1) == Block.REDSTONE_WIRE.id && world.getData(i, j, k - 1) > 0);
            }
            case 3: {
                return world.isBlockFaceIndirectlyPowered(i + 1, j, k, 5) || (world.getTypeId(i + 1, j, k) == Block.REDSTONE_WIRE.id && world.getData(i + 1, j, k) > 0);
            }
            case 1: {
                return world.isBlockFaceIndirectlyPowered(i - 1, j, k, 4) || (world.getTypeId(i - 1, j, k) == Block.REDSTONE_WIRE.id && world.getData(i - 1, j, k) > 0);
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean interact(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        final int l = world.getData(i, j, k);
        int i2 = (l & 0xC) >> 2;
        i2 = (i2 + 1 << 2 & 0xC);
        world.setData(i, j, k, i2 | (l & 0x3));
        return true;
    }
    
    public boolean isPowerSource() {
        return false;
    }
    
    public void postPlace(final World world, final int i, final int j, final int k, final EntityLiving entityliving) {
        final int l = ((MathHelper.floor(entityliving.yaw * 4.0f / 360.0f + 0.5) & 0x3) + 2) % 4;
        world.setData(i, j, k, l);
        final boolean flag = this.f(world, i, j, k, l);
        if (flag) {
            world.c(i, j, k, this.id, 1);
        }
    }
    
    public void c(final World world, final int i, final int j, final int k) {
        world.applyPhysics(i + 1, j, k, this.id);
        world.applyPhysics(i - 1, j, k, this.id);
        world.applyPhysics(i, j, k + 1, this.id);
        world.applyPhysics(i, j, k - 1, this.id);
        world.applyPhysics(i, j - 1, k, this.id);
        world.applyPhysics(i, j + 1, k, this.id);
    }
    
    public boolean a() {
        return false;
    }
    
    public int a(final int i, final Random random) {
        return Item.DIODE.id;
    }
    
    static {
        BlockDiode.a = new double[] { -0.0625, 0.0625, 0.1875, 0.3125 };
        BlockDiode.b = new int[] { 1, 2, 3, 4 };
    }
}
