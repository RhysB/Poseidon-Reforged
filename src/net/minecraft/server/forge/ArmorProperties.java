// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

public class ArmorProperties
{
    public int damageRemove;
    public boolean allowRegularComputation;
    
    public ArmorProperties() {
        this.damageRemove = 0;
        this.allowRegularComputation = false;
    }
    
    public ArmorProperties(final int damageRemove, final boolean allowRegularCompuation) {
        this.damageRemove = 0;
        this.allowRegularComputation = false;
        this.damageRemove = damageRemove;
        this.allowRegularComputation = allowRegularCompuation;
    }
}
