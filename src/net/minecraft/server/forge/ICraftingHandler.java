// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.EntityHuman;

public interface ICraftingHandler
{
    void onTakenFromCrafting(final EntityHuman p0, final ItemStack p1, final IInventory p2);
}
