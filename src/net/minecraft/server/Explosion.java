// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.Server;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import net.minecraft.server.forge.ISpecialResistance;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Explosion
{
    public boolean a;
    private Random h;
    private World world;
    public double posX;
    public double posY;
    public double posZ;
    public Entity source;
    public float size;
    public Set blocks;
    public boolean wasCanceled;
    
    public Explosion(final World world, final Entity entity, final double d0, final double d1, final double d2, final float f) {
        this.a = false;
        this.h = new Random();
        this.blocks = new HashSet();
        this.wasCanceled = false;
        this.world = world;
        this.source = entity;
        this.size = f;
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
    }
    
    public void a() {
        final float f = this.size;
        final byte b0 = 16;
        for (int i = 0; i < b0; ++i) {
            for (int j = 0; j < b0; ++j) {
                for (int k = 0; k < b0; ++k) {
                    if (i == 0 || i == b0 - 1 || j == 0 || j == b0 - 1 || k == 0 || k == b0 - 1) {
                        double d3 = i / (b0 - 1.0f) * 2.0f - 1.0f;
                        double d4 = j / (b0 - 1.0f) * 2.0f - 1.0f;
                        double d5 = k / (b0 - 1.0f) * 2.0f - 1.0f;
                        final double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                        d3 /= d6;
                        d4 /= d6;
                        d5 /= d6;
                        float f2 = this.size * (0.7f + this.world.random.nextFloat() * 0.6f);
                        double d7 = this.posX;
                        double d8 = this.posY;
                        double d9 = this.posZ;
                        for (float f3 = 0.3f; f2 > 0.0f; f2 -= f3 * 0.75f) {
                            final int l = MathHelper.floor(d7);
                            final int i2 = MathHelper.floor(d8);
                            final int j2 = MathHelper.floor(d9);
                            final int k2 = this.world.getTypeId(l, i2, j2);
                            if (k2 > 0) {
                                if (Block.byId[k2] instanceof ISpecialResistance) {
                                    final ISpecialResistance isr = (ISpecialResistance)Block.byId[k2];
                                    f2 -= (isr.getSpecialExplosionResistance(this.world, l, i2, j2, this.posX, this.posY, this.posZ, this.source) + 0.3f) * f3;
                                }
                                else {
                                    f2 -= (Block.byId[k2].a(this.source) + 0.3f) * f3;
                                }
                            }
                            if (f2 > 0.0f) {
                                this.blocks.add(new ChunkPosition(l, i2, j2));
                            }
                            d7 += d3 * f3;
                            d8 += d4 * f3;
                            d9 += d5 * f3;
                        }
                    }
                }
            }
        }
        this.size *= 2.0f;
        int i = MathHelper.floor(this.posX - this.size - 1.0);
        int j = MathHelper.floor(this.posX + this.size + 1.0);
        int k = MathHelper.floor(this.posY - this.size - 1.0);
        final int l2 = MathHelper.floor(this.posY + this.size + 1.0);
        final int i3 = MathHelper.floor(this.posZ - this.size - 1.0);
        final int j3 = MathHelper.floor(this.posZ + this.size + 1.0);
        final List list = this.world.b(this.source, AxisAlignedBB.b((double)i, (double)k, (double)i3, (double)j, (double)l2, (double)j3));
        final Vec3D vec3d = Vec3D.create(this.posX, this.posY, this.posZ);
        for (int k3 = 0; k3 < list.size(); ++k3) {
            final Entity entity = list.get(k3);
            final double d10 = entity.f(this.posX, this.posY, this.posZ) / this.size;
            if (d10 <= 1.0) {
                double d7 = entity.locX - this.posX;
                double d8 = entity.locY - this.posY;
                double d9 = entity.locZ - this.posZ;
                final double d11 = MathHelper.a(d7 * d7 + d8 * d8 + d9 * d9);
                d7 /= d11;
                d8 /= d11;
                d9 /= d11;
                final double d12 = this.world.a(vec3d, entity.boundingBox);
                final double d13 = (1.0 - d10) * d12;
                final Server server = (Server)this.world.getServer();
                final org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                final int damageDone = (int)((d13 * d13 + d13) / 2.0 * 8.0 * this.size + 1.0);
                if (damagee != null) {
                    if (this.source == null) {
                        final EntityDamageByBlockEvent event = new EntityDamageByBlockEvent((org.bukkit.block.Block)null, damagee, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, damageDone);
                        server.getPluginManager().callEvent((Event)event);
                        if (!event.isCancelled()) {
                            entity.damageEntity(this.source, event.getDamage());
                            final Entity entity2 = entity;
                            entity2.motX += d7 * d13;
                            final Entity entity3 = entity;
                            entity3.motY += d8 * d13;
                            final Entity entity4 = entity;
                            entity4.motZ += d9 * d13;
                        }
                    }
                    else {
                        final EntityDamageByEntityEvent event2 = new EntityDamageByEntityEvent(this.source.getBukkitEntity(), damagee, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, damageDone);
                        server.getPluginManager().callEvent((Event)event2);
                        if (!event2.isCancelled()) {
                            entity.damageEntity(this.source, event2.getDamage());
                            final Entity entity5 = entity;
                            entity5.motX += d7 * d13;
                            final Entity entity6 = entity;
                            entity6.motY += d8 * d13;
                            final Entity entity7 = entity;
                            entity7.motZ += d9 * d13;
                        }
                    }
                }
            }
        }
        this.size = f;
        final ArrayList arraylist = new ArrayList();
        arraylist.addAll(this.blocks);
        if (this.a) {
            for (int l3 = arraylist.size() - 1; l3 >= 0; --l3) {
                final ChunkPosition chunkposition = arraylist.get(l3);
                final int i4 = chunkposition.x;
                final int j4 = chunkposition.y;
                final int k4 = chunkposition.z;
                final int l4 = this.world.getTypeId(i4, j4, k4);
                final int i5 = this.world.getTypeId(i4, j4 - 1, k4);
                if (l4 == 0 && Block.o[i5] && this.h.nextInt(3) == 0) {
                    this.world.setTypeId(i4, j4, k4, Block.FIRE.id);
                }
            }
        }
    }
    
    public void a(final boolean flag) {
        this.world.makeSound(this.posX, this.posY, this.posZ, "random.explode", 4.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f);
        final ArrayList arraylist = new ArrayList();
        arraylist.addAll(this.blocks);
        final org.bukkit.World bworld = (org.bukkit.World)this.world.getWorld();
        final org.bukkit.entity.Entity explode = (this.source == null) ? null : this.source.getBukkitEntity();
        final Location location = new Location(bworld, this.posX, this.posY, this.posZ);
        final List<org.bukkit.block.Block> blockList = new ArrayList<org.bukkit.block.Block>();
        for (int j = arraylist.size() - 1; j >= 0; --j) {
            final ChunkPosition cpos = arraylist.get(j);
            final org.bukkit.block.Block block = bworld.getBlockAt(cpos.x, cpos.y, cpos.z);
            if (block.getType() != Material.AIR) {
                blockList.add(block);
            }
        }
        final EntityExplodeEvent event = new EntityExplodeEvent(explode, location, (List)blockList);
        this.world.getServer().getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            this.wasCanceled = true;
            return;
        }
        for (int i = arraylist.size() - 1; i >= 0; --i) {
            final ChunkPosition chunkposition = arraylist.get(i);
            final int k = chunkposition.x;
            final int l = chunkposition.y;
            final int m = chunkposition.z;
            final int i2 = this.world.getTypeId(k, l, m);
            if (flag) {
                final double d0 = k + this.world.random.nextFloat();
                final double d2 = l + this.world.random.nextFloat();
                final double d3 = m + this.world.random.nextFloat();
                double d4 = d0 - this.posX;
                double d5 = d2 - this.posY;
                double d6 = d3 - this.posZ;
                final double d7 = MathHelper.a(d4 * d4 + d5 * d5 + d6 * d6);
                d4 /= d7;
                d5 /= d7;
                d6 /= d7;
                double d8 = 0.5 / (d7 / this.size + 0.1);
                d8 *= this.world.random.nextFloat() * this.world.random.nextFloat() + 0.3f;
                d4 *= d8;
                d5 *= d8;
                d6 *= d8;
                this.world.a("explode", (d0 + this.posX * 1.0) / 2.0, (d2 + this.posY * 1.0) / 2.0, (d3 + this.posZ * 1.0) / 2.0, d4, d5, d6);
                this.world.a("smoke", d0, d2, d3, d4, d5, d6);
            }
            if (i2 > 0 && i2 != Block.FIRE.id) {
                Block.byId[i2].dropNaturally(this.world, k, l, m, this.world.getData(k, l, m), event.getYield());
                this.world.setTypeId(k, l, m, 0);
                Block.byId[i2].d(this.world, k, l, m);
            }
        }
    }
}
