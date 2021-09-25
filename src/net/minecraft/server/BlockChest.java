// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Random;

public class BlockChest extends BlockContainer
{
    private Random a;
    
    protected BlockChest(final int i) {
        super(i, Material.WOOD);
        this.a = new Random();
        this.textureId = 26;
    }
    
    public int a(final int i) {
        if (i == 1) {
            return this.textureId - 1;
        }
        if (i == 0) {
            return this.textureId - 1;
        }
        if (i == 3) {
            return this.textureId + 1;
        }
        return this.textureId;
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        int l = 0;
        if (world.getTypeId(i - 1, j, k) == this.id) {
            ++l;
        }
        if (world.getTypeId(i + 1, j, k) == this.id) {
            ++l;
        }
        if (world.getTypeId(i, j, k - 1) == this.id) {
            ++l;
        }
        if (world.getTypeId(i, j, k + 1) == this.id) {
            ++l;
        }
        return l <= 1 && !this.g(world, i - 1, j, k) && !this.g(world, i + 1, j, k) && !this.g(world, i, j, k - 1) && !this.g(world, i, j, k + 1);
    }
    
    private boolean g(final World world, final int i, final int j, final int k) {
        return world.getTypeId(i, j, k) == this.id && (world.getTypeId(i - 1, j, k) == this.id || world.getTypeId(i + 1, j, k) == this.id || world.getTypeId(i, j, k - 1) == this.id || world.getTypeId(i, j, k + 1) == this.id);
    }
    
    public void remove(final World world, final int i, final int j, final int k) {
        final TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(i, j, k);
        for (int l = 0; l < ((IInventory)tileentitychest).getSize(); ++l) {
            final ItemStack itemstack = ((IInventory)tileentitychest).getItem(l);
            if (itemstack != null) {
                final float f = this.a.nextFloat() * 0.8f + 0.1f;
                final float f2 = this.a.nextFloat() * 0.8f + 0.1f;
                final float f3 = this.a.nextFloat() * 0.8f + 0.1f;
                while (itemstack.count > 0) {
                    int i2 = this.a.nextInt(21) + 10;
                    if (i2 > itemstack.count) {
                        i2 = itemstack.count;
                    }
                    final ItemStack itemStack = itemstack;
                    itemStack.count -= i2;
                    final EntityItem entityitem = new EntityItem(world, (double)(i + f), (double)(j + f2), (double)(k + f3), new ItemStack(itemstack.id, i2, itemstack.getData()));
                    final float f4 = 0.05f;
                    entityitem.motX = (float)this.a.nextGaussian() * f4;
                    entityitem.motY = (float)this.a.nextGaussian() * f4 + 0.2f;
                    entityitem.motZ = (float)this.a.nextGaussian() * f4;
                    world.addEntity((Entity)entityitem);
                }
            }
        }
        super.remove(world, i, j, k);
    }
    
    public boolean interact(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        Object obj = world.getTileEntity(i, j, k);
        if (world.isBlockSolidOnSide(i, j + 1, k, 0)) {
            return true;
        }
        if (world.getTypeId(i - 1, j, k) == this.id && world.isBlockSolidOnSide(i - 1, j + 1, k, 0)) {
            return true;
        }
        if (world.getTypeId(i + 1, j, k) == this.id && world.isBlockSolidOnSide(i + 1, j + 1, k, 0)) {
            return true;
        }
        if (world.getTypeId(i, j, k - 1) == this.id && world.isBlockSolidOnSide(i, j + 1, k - 1, 0)) {
            return true;
        }
        if (world.getTypeId(i, j, k + 1) == this.id && world.isBlockSolidOnSide(i, j + 1, k + 1, 0)) {
            return true;
        }
        if (world.getTypeId(i - 1, j, k) == this.id) {
            obj = new InventoryLargeChest("Large chest", (IInventory)world.getTileEntity(i - 1, j, k), (IInventory)obj);
        }
        if (world.getTypeId(i + 1, j, k) == this.id) {
            obj = new InventoryLargeChest("Large chest", (IInventory)obj, (IInventory)world.getTileEntity(i + 1, j, k));
        }
        if (world.getTypeId(i, j, k - 1) == this.id) {
            obj = new InventoryLargeChest("Large chest", (IInventory)world.getTileEntity(i, j, k - 1), (IInventory)obj);
        }
        if (world.getTypeId(i, j, k + 1) == this.id) {
            obj = new InventoryLargeChest("Large chest", (IInventory)obj, (IInventory)world.getTileEntity(i, j, k + 1));
        }
        if (world.isStatic) {
            return true;
        }
        entityhuman.a((IInventory)obj);
        return true;
    }
    
    protected TileEntity a_() {
        return (TileEntity)new TileEntityChest();
    }
}
