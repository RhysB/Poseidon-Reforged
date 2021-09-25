// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

import net.minecraft.server.EnumBedError;
import net.minecraft.server.EntityHuman;

public interface ISleepHandler
{
    EnumBedError sleepInBedAt(final EntityHuman p0, final int p1, final int p2, final int p3);
}
