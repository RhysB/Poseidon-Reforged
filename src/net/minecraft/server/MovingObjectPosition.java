// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

public class MovingObjectPosition
{
    public EnumMovingObjectType type;
    public int b;
    public int c;
    public int d;
    public int face;
    public Vec3D f;
    public Entity entity;
    public int subHit;
    
    public MovingObjectPosition(final int i, final int j, final int k, final int l, final Vec3D vec3d) {
        this.subHit = -1;
        this.type = EnumMovingObjectType.TILE;
        this.b = i;
        this.c = j;
        this.d = k;
        this.face = l;
        this.f = Vec3D.create(vec3d.a, vec3d.b, vec3d.c);
    }
    
    public MovingObjectPosition(final Entity entity1) {
        this.subHit = -1;
        this.type = EnumMovingObjectType.ENTITY;
        this.entity = entity1;
        this.f = Vec3D.create(entity1.locX, entity1.locY, entity1.locZ);
    }
}
