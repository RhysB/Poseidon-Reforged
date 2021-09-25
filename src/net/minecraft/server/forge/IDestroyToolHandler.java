// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

import net.minecraft.server.ItemStack;
import net.minecraft.server.EntityHuman;

public interface IDestroyToolHandler
{
    void onDestroyCurrentItem(final EntityHuman p0, final ItemStack p1);
}
