// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

import net.minecraft.server.World;

public interface IBlockSecondaryProperties
{
    boolean isBlockNormalCube(final World p0, final int p1, final int p2, final int p3);
    
    boolean isBlockReplaceable(final World p0, final int p1, final int p2, final int p3);
    
    boolean isBlockBurning(final World p0, final int p1, final int p2, final int p3);
    
    boolean isAirBlock(final World p0, final int p1, final int p2, final int p3);
}
