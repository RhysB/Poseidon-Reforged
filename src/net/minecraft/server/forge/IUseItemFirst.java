// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

import net.minecraft.server.World;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;

public interface IUseItemFirst
{
    boolean onItemUseFirst(final ItemStack p0, final EntityHuman p1, final World p2, final int p3, final int p4, final int p5, final int p6);
}
