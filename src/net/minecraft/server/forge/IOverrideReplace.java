// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

import net.minecraft.server.World;

public interface IOverrideReplace
{
    boolean canReplaceBlock(final World p0, final int p1, final int p2, final int p3, final int p4);
    
    boolean getReplacedSuccess();
}
