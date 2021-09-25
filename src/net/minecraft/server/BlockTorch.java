// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Random;

public class BlockTorch extends Block
{
    protected BlockTorch(final int i, final int j) {
        super(i, j, Material.ORIENTABLE);
        this.a(true);
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
    
    private boolean g(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i, j, k, 1) || world.getTypeId(i, j, k) == Block.FENCE.id;
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i - 1, j, k, 5) || world.isBlockSolidOnSide(i + 1, j, k, 4) || world.isBlockSolidOnSide(i, j, k - 1, 3) || world.isBlockSolidOnSide(i, j, k + 1, 2) || this.g(world, i, j - 1, k);
    }
    
    public void postPlace(final World world, final int i, final int j, final int k, final int l) {
        int i2 = world.getData(i, j, k);
        if (l == 1 && this.g(world, i, j - 1, k)) {
            i2 = 5;
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
        world.setData(i, j, k, i2);
    }
    
    public void a(final World world, final int i, final int j, final int k, final Random random) {
        super.a(world, i, j, k, random);
        if (world.getData(i, j, k) == 0) {
            this.c(world, i, j, k);
        }
    }
    
    public void c(final World world, final int i, final int j, final int k) {
        if (world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            world.setData(i, j, k, 1);
        }
        else if (world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            world.setData(i, j, k, 2);
        }
        else if (world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            world.setData(i, j, k, 3);
        }
        else if (world.isBlockSolidOnSide(i, j, k + 1, 2)) {
            world.setData(i, j, k, 4);
        }
        else if (this.g(world, i, j - 1, k)) {
            world.setData(i, j, k, 5);
        }
        this.h(world, i, j, k);
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        if (this.h(world, i, j, k)) {
            final int i2 = world.getData(i, j, k);
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
            if (!this.g(world, i, j - 1, k) && i2 == 5) {
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
    
    public MovingObjectPosition a(final World world, final int i, final int j, final int k, final Vec3D vec3d, final Vec3D vec3d1) {
        final int l = world.getData(i, j, k) & 0x7;
        final float f = 0.15f;
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
            final float f2 = 0.1f;
            this.a(0.5f - f2, 0.0f, 0.5f - f2, 0.5f + f2, 0.6f, 0.5f + f2);
        }
        return super.a(world, i, j, k, vec3d, vec3d1);
    }
}
