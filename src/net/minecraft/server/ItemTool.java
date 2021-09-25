// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Arrays;
import net.minecraft.server.forge.ForgeHooks;

public class ItemTool extends Item
{
    private Block[] bk;
    public float bl;
    public int bm;
    protected EnumToolMaterial a;
    
    protected ItemTool(final int i, final int j, final EnumToolMaterial enumtoolmaterial, final Block[] ablock) {
        super(i);
        this.bl = 4.0f;
        this.a = enumtoolmaterial;
        this.bk = ablock;
        this.maxStackSize = 1;
        this.d(enumtoolmaterial.a());
        this.bl = enumtoolmaterial.b();
        this.bm = j + enumtoolmaterial.c();
    }
    
    public float a(final ItemStack itemstack, final Block block) {
        for (int i = 0; i < this.bk.length; ++i) {
            if (this.bk[i] == block) {
                return this.bl;
            }
        }
        return 1.0f;
    }
    
    public float getStrVsBlock(final ItemStack itemstack, final Block block, final int md) {
        if (ForgeHooks.isToolEffective(itemstack, block, md)) {
            return this.bl;
        }
        return this.a(itemstack, block);
    }
    
    public boolean a(final ItemStack itemstack, final EntityLiving entityliving, final EntityLiving entityliving1) {
        itemstack.damage(2, (Entity)entityliving1);
        return true;
    }
    
    public boolean a(final ItemStack itemstack, final int i, final int j, final int k, final int l, final EntityLiving entityliving) {
        itemstack.damage(1, (Entity)entityliving);
        return true;
    }
    
    public int a(final Entity entity) {
        return this.bm;
    }
    
    public void addBlockEffectiveAgainst(final Block block) {
        (this.bk = Arrays.copyOf(this.bk, this.bk.length + 1))[this.bk.length - 1] = block;
    }
}
