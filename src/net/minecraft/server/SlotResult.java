// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import net.minecraft.server.forge.ForgeHooks;

public class SlotResult extends Slot
{
    private final IInventory d;
    private EntityHuman field_25004_e;
    
    public SlotResult(final EntityHuman entityhuman, final IInventory iinventory, final IInventory iinventory1, final int i, final int j, final int k) {
        super(iinventory1, i, j, k);
        this.field_25004_e = entityhuman;
        this.d = iinventory;
    }
    
    public boolean isAllowed(final ItemStack itemstack) {
        return false;
    }
    
    public void a(final ItemStack itemstack) {
        itemstack.b(this.field_25004_e.world, this.field_25004_e);
        if (itemstack.id == Block.WORKBENCH.id) {
            this.field_25004_e.a((Statistic)AchievementList.h, 1);
        }
        else if (itemstack.id == Item.WOOD_PICKAXE.id) {
            this.field_25004_e.a((Statistic)AchievementList.i, 1);
        }
        else if (itemstack.id == Block.FURNACE.id) {
            this.field_25004_e.a((Statistic)AchievementList.j, 1);
        }
        else if (itemstack.id == Item.WOOD_HOE.id) {
            this.field_25004_e.a((Statistic)AchievementList.l, 1);
        }
        else if (itemstack.id == Item.BREAD.id) {
            this.field_25004_e.a((Statistic)AchievementList.m, 1);
        }
        else if (itemstack.id == Item.CAKE.id) {
            this.field_25004_e.a((Statistic)AchievementList.n, 1);
        }
        else if (itemstack.id == Item.STONE_PICKAXE.id) {
            this.field_25004_e.a((Statistic)AchievementList.o, 1);
        }
        else if (itemstack.id == Item.WOOD_SWORD.id) {
            this.field_25004_e.a((Statistic)AchievementList.r, 1);
        }
        ModLoader.TakenFromCrafting(this.field_25004_e, itemstack);
        ForgeHooks.onTakenFromCrafting(this.field_25004_e, itemstack, this.d);
        for (int i = 0; i < this.d.getSize(); ++i) {
            final ItemStack itemstack2 = this.d.getItem(i);
            if (itemstack2 != null) {
                this.d.splitStack(i, 1);
                if (itemstack2.getItem().i()) {
                    this.d.setItem(i, new ItemStack(itemstack2.getItem().h()));
                }
            }
        }
    }
}
