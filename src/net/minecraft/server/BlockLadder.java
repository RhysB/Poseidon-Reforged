// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Random;

public class BlockLadder extends Block
{
    protected BlockLadder(final int i, final int j) {
        super(i, j, Material.ORIENTABLE);
    }
    
    public AxisAlignedBB e(final World world, final int i, final int j, final int k) {
        final int l = world.getData(i, j, k);
        final float f = 0.125f;
        if (l == 2) {
            this.a(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
        }
        if (l == 3) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
        }
        if (l == 4) {
            this.a(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        if (l == 5) {
            this.a(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
        }
        return super.e(world, i, j, k);
    }
    
    public boolean a() {
        return false;
    }
    
    public boolean b() {
        return false;
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i - 1, j, k, 5) || world.isBlockSolidOnSide(i + 1, j, k, 4) || world.isBlockSolidOnSide(i, j, k - 1, 3) || world.isBlockSolidOnSide(i, j, k + 1, 2);
    }
    
    public void postPlace(final World world, final int i, final int j, final int k, final int l) {
        int i2 = world.getData(i, j, k);
        if ((i2 == 0 || l == 2) && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
            i2 = 2;
        }
        if ((i2 == 0 || l == 3) && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            i2 = 3;
        }
        if ((i2 == 0 || l == 4) && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            i2 = 4;
        }
        if ((i2 == 0 || l == 5) && world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            i2 = 5;
        }
        world.setData(i, j, k, i2);
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        final int i2 = world.getData(i, j, k);
        boolean flag = false;
        if (i2 == 2 && world.isBlockSolidOnSide(i, j, k + 1, 2)) {
            flag = true;
        }
        if (i2 == 3 && world.isBlockSolidOnSide(i, j, k - 1, 3)) {
            flag = true;
        }
        if (i2 == 4 && world.isBlockSolidOnSide(i + 1, j, k, 4)) {
            flag = true;
        }
        if (i2 == 5 && world.isBlockSolidOnSide(i - 1, j, k, 5)) {
            flag = true;
        }
        if (!flag) {
            this.g(world, i, j, k, i2);
            world.setTypeId(i, j, k, 0);
        }
        super.doPhysics(world, i, j, k, l);
    }
    
    public int a(final Random random) {
        return 1;
    }
    
    public boolean isLadder() {
        return true;
    }
}
