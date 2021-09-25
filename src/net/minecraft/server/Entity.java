// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.Server;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import java.util.UUID;
import java.util.Random;

public abstract class Entity
{
    private static int entityCount;
    public int id;
    public double aH;
    public boolean aI;
    public Entity passenger;
    public Entity vehicle;
    public World world;
    public double lastX;
    public double lastY;
    public double lastZ;
    public double locX;
    public double locY;
    public double locZ;
    public double motX;
    public double motY;
    public double motZ;
    public float yaw;
    public float pitch;
    public float lastYaw;
    public float lastPitch;
    public final AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean positionChanged;
    public boolean bc;
    public boolean bd;
    public boolean velocityChanged;
    public boolean bf;
    public boolean bg;
    public boolean dead;
    public float height;
    public float length;
    public float width;
    public float bl;
    public float bm;
    public float fallDistance;
    private int b;
    public double bo;
    public double bp;
    public double bq;
    public float br;
    public float bs;
    public boolean bt;
    public float bu;
    protected Random random;
    public int ticksLived;
    public int maxFireTicks;
    public int fireTicks;
    public int maxAirTicks;
    protected boolean bA;
    public int noDamageTicks;
    public int airTicks;
    private boolean justCreated;
    protected boolean fireProof;
    protected DataWatcher datawatcher;
    public float bF;
    private double d;
    private double e;
    public boolean bG;
    public int bH;
    public int bI;
    public int bJ;
    public boolean bK;
    public UUID uniqueId;
    protected org.bukkit.entity.Entity bukkitEntity;
    
    public Entity(final World world) {
        this.uniqueId = UUID.randomUUID();
        this.id = Entity.entityCount++;
        this.aH = 1.0;
        this.aI = false;
        this.boundingBox = AxisAlignedBB.a(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        this.onGround = false;
        this.bd = false;
        this.velocityChanged = false;
        this.bg = true;
        this.dead = false;
        this.height = 0.0f;
        this.length = 0.6f;
        this.width = 1.8f;
        this.bl = 0.0f;
        this.bm = 0.0f;
        this.fallDistance = 0.0f;
        this.b = 1;
        this.br = 0.0f;
        this.bs = 0.0f;
        this.bt = false;
        this.bu = 0.0f;
        this.random = new Random();
        this.ticksLived = 0;
        this.maxFireTicks = 1;
        this.fireTicks = 0;
        this.maxAirTicks = 300;
        this.bA = false;
        this.noDamageTicks = 0;
        this.airTicks = 300;
        this.justCreated = true;
        this.fireProof = false;
        this.datawatcher = new DataWatcher();
        this.bF = 0.0f;
        this.bG = false;
        this.world = world;
        this.setPosition(0.0, 0.0, 0.0);
        this.datawatcher.a(0, (Object)0);
        this.b();
    }
    
    protected abstract void b();
    
    public DataWatcher aa() {
        return this.datawatcher;
    }
    
    @Override
    public boolean equals(final Object object) {
        return object instanceof Entity && ((Entity)object).id == this.id;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
    
    public void die() {
        this.dead = true;
    }
    
    protected void b(final float f, final float f1) {
        this.length = f;
        this.width = f1;
    }
    
    protected void c(float f, float f1) {
        if (Float.isNaN(f)) {
            f = 0.0f;
        }
        if (f == Float.POSITIVE_INFINITY || f == Float.NEGATIVE_INFINITY) {
            if (this instanceof EntityPlayer) {
                System.err.println(((CraftPlayer)this.getBukkitEntity()).getName() + " was caught trying to crash the server with an invalid yaw");
                ((CraftPlayer)this.getBukkitEntity()).kickPlayer("Nope");
            }
            f = 0.0f;
        }
        if (Float.isNaN(f1)) {
            f1 = 0.0f;
        }
        if (f1 == Float.POSITIVE_INFINITY || f1 == Float.NEGATIVE_INFINITY) {
            if (this instanceof EntityPlayer) {
                System.err.println(((CraftPlayer)this.getBukkitEntity()).getName() + " was caught trying to crash the server with an invalid pitch");
                ((CraftPlayer)this.getBukkitEntity()).kickPlayer("Nope");
            }
            f1 = 0.0f;
        }
        this.yaw = f % 360.0f;
        this.pitch = f1 % 360.0f;
    }
    
    public void setPosition(final double d0, final double d1, final double d2) {
        this.locX = d0;
        this.locY = d1;
        this.locZ = d2;
        final float f = this.length / 2.0f;
        final float f2 = this.width;
        this.boundingBox.c(d0 - f, d1 - this.height + this.br, d2 - f, d0 + f, d1 - this.height + this.br + f2, d2 + f);
    }
    
    public void m_() {
        this.R();
    }
    
    public void R() {
        if (this.vehicle != null && this.vehicle.dead) {
            this.vehicle = null;
        }
        ++this.ticksLived;
        this.bl = this.bm;
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.lastPitch = this.pitch;
        this.lastYaw = this.yaw;
        if (this.f_()) {
            if (!this.bA && !this.justCreated) {
                float f = MathHelper.a(this.motX * this.motX * 0.20000000298023224 + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224) * 0.2f;
                if (f > 1.0f) {
                    f = 1.0f;
                }
                this.world.makeSound(this, "random.splash", f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
                final float f2 = (float)MathHelper.floor(this.boundingBox.b);
                for (int i = 0; i < 1.0f + this.length * 20.0f; ++i) {
                    final float f3 = (this.random.nextFloat() * 2.0f - 1.0f) * this.length;
                    final float f4 = (this.random.nextFloat() * 2.0f - 1.0f) * this.length;
                    this.world.a("bubble", this.locX + f3, (double)(f2 + 1.0f), this.locZ + f4, this.motX, this.motY - this.random.nextFloat() * 0.2f, this.motZ);
                }
                for (int i = 0; i < 1.0f + this.length * 20.0f; ++i) {
                    final float f3 = (this.random.nextFloat() * 2.0f - 1.0f) * this.length;
                    final float f4 = (this.random.nextFloat() * 2.0f - 1.0f) * this.length;
                    this.world.a("splash", this.locX + f3, (double)(f2 + 1.0f), this.locZ + f4, this.motX, this.motY, this.motZ);
                }
            }
            this.fallDistance = 0.0f;
            this.bA = true;
            this.fireTicks = 0;
        }
        else {
            this.bA = false;
        }
        if (this.world.isStatic) {
            this.fireTicks = 0;
        }
        else if (this.fireTicks > 0) {
            if (this.fireProof) {
                this.fireTicks -= 4;
                if (this.fireTicks < 0) {
                    this.fireTicks = 0;
                }
            }
            else {
                if (this.fireTicks % 20 == 0) {
                    if (this instanceof EntityLiving) {
                        final EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FIRE_TICK, 1);
                        this.world.getServer().getPluginManager().callEvent((Event)event);
                        if (!event.isCancelled()) {
                            this.damageEntity(null, event.getDamage());
                        }
                    }
                    else {
                        this.damageEntity(null, 1);
                    }
                }
                --this.fireTicks;
            }
        }
        if (this.ae()) {
            this.ab();
        }
        if (this.locY < -64.0) {
            this.Y();
        }
        if (!this.world.isStatic) {
            this.a(0, this.fireTicks > 0);
            this.a(2, this.vehicle != null);
        }
        this.justCreated = false;
    }
    
    protected void ab() {
        if (!this.fireProof) {
            if (this instanceof EntityLiving) {
                final Server server = (Server)this.world.getServer();
                final Block damager = null;
                final org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                final EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagee, EntityDamageEvent.DamageCause.LAVA, 4);
                server.getPluginManager().callEvent((Event)event);
                if (!event.isCancelled()) {
                    this.damageEntity(null, event.getDamage());
                }
                if (this.fireTicks <= 0) {
                    final EntityCombustEvent combustEvent = new EntityCombustEvent(damagee);
                    server.getPluginManager().callEvent((Event)combustEvent);
                    if (!combustEvent.isCancelled()) {
                        this.fireTicks = 600;
                    }
                }
                else {
                    this.fireTicks = 600;
                }
                return;
            }
            this.damageEntity(null, 4);
            this.fireTicks = 600;
        }
    }
    
    protected void Y() {
        this.die();
    }
    
    public boolean d(final double d0, final double d1, final double d2) {
        final AxisAlignedBB axisalignedbb = this.boundingBox.c(d0, d1, d2);
        final List list = this.world.getEntities(this, axisalignedbb);
        return list.size() <= 0 && !this.world.c(axisalignedbb);
    }
    
    public void move(double d0, double d1, double d2) {
        if (this.bt) {
            this.boundingBox.d(d0, d1, d2);
            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0;
            this.locY = this.boundingBox.b + this.height - this.br;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0;
        }
        else {
            this.br *= 0.4f;
            final double d3 = this.locX;
            final double d4 = this.locZ;
            if (this.bf) {
                this.bf = false;
                d0 *= 0.25;
                d1 *= 0.05000000074505806;
                d2 *= 0.25;
                this.motX = 0.0;
                this.motY = 0.0;
                this.motZ = 0.0;
            }
            double d5 = d0;
            final double d6 = d1;
            double d7 = d2;
            final AxisAlignedBB axisalignedbb = this.boundingBox.clone();
            final boolean flag = this.onGround && this.isSneaking();
            if (flag) {
                final double d8 = 0.05;
                while (d0 != 0.0 && this.world.getEntities(this, this.boundingBox.c(d0, -1.0, 0.0)).size() == 0) {
                    if (d0 < d8 && d0 >= -d8) {
                        d0 = 0.0;
                    }
                    else if (d0 > 0.0) {
                        d0 -= d8;
                    }
                    else {
                        d0 += d8;
                    }
                    d5 = d0;
                }
                while (d2 != 0.0 && this.world.getEntities(this, this.boundingBox.c(0.0, -1.0, d2)).size() == 0) {
                    if (d2 < d8 && d2 >= -d8) {
                        d2 = 0.0;
                    }
                    else if (d2 > 0.0) {
                        d2 -= d8;
                    }
                    else {
                        d2 += d8;
                    }
                    d7 = d2;
                }
            }
            List list = this.world.getEntities(this, this.boundingBox.a(d0, d1, d2));
            for (int i = 0; i < list.size(); ++i) {
                d1 = list.get(i).b(this.boundingBox, d1);
            }
            this.boundingBox.d(0.0, d1, 0.0);
            if (!this.bg && d6 != d1) {
                d2 = 0.0;
                d1 = 0.0;
                d0 = 0.0;
            }
            final boolean flag2 = this.onGround || (d6 != d1 && d6 < 0.0);
            for (int j = 0; j < list.size(); ++j) {
                d0 = list.get(j).a(this.boundingBox, d0);
            }
            this.boundingBox.d(d0, 0.0, 0.0);
            if (!this.bg && d5 != d0) {
                d2 = 0.0;
                d1 = 0.0;
                d0 = 0.0;
            }
            for (int j = 0; j < list.size(); ++j) {
                d2 = list.get(j).c(this.boundingBox, d2);
            }
            this.boundingBox.d(0.0, 0.0, d2);
            if (!this.bg && d7 != d2) {
                d2 = 0.0;
                d1 = 0.0;
                d0 = 0.0;
            }
            if (this.bs > 0.0f && flag2 && (flag || this.br < 0.05f) && (d5 != d0 || d7 != d2)) {
                final double d9 = d0;
                final double d10 = d1;
                final double d11 = d2;
                d0 = d5;
                d1 = this.bs;
                d2 = d7;
                final AxisAlignedBB axisalignedbb2 = this.boundingBox.clone();
                this.boundingBox.b(axisalignedbb);
                list = this.world.getEntities(this, this.boundingBox.a(d5, d1, d7));
                for (int k = 0; k < list.size(); ++k) {
                    d1 = list.get(k).b(this.boundingBox, d1);
                }
                this.boundingBox.d(0.0, d1, 0.0);
                if (!this.bg && d6 != d1) {
                    d2 = 0.0;
                    d1 = 0.0;
                    d0 = 0.0;
                }
                for (int k = 0; k < list.size(); ++k) {
                    d0 = list.get(k).a(this.boundingBox, d0);
                }
                this.boundingBox.d(d0, 0.0, 0.0);
                if (!this.bg && d5 != d0) {
                    d2 = 0.0;
                    d1 = 0.0;
                    d0 = 0.0;
                }
                for (int k = 0; k < list.size(); ++k) {
                    d2 = list.get(k).c(this.boundingBox, d2);
                }
                this.boundingBox.d(0.0, 0.0, d2);
                if (!this.bg && d7 != d2) {
                    d2 = 0.0;
                    d1 = 0.0;
                    d0 = 0.0;
                }
                if (!this.bg && d6 != d1) {
                    d2 = 0.0;
                    d1 = 0.0;
                    d0 = 0.0;
                }
                else {
                    d1 = -this.bs;
                    for (int k = 0; k < list.size(); ++k) {
                        d1 = list.get(k).b(this.boundingBox, d1);
                    }
                    this.boundingBox.d(0.0, d1, 0.0);
                }
                if (d9 * d9 + d11 * d11 >= d0 * d0 + d2 * d2) {
                    d0 = d9;
                    d1 = d10;
                    d2 = d11;
                    this.boundingBox.b(axisalignedbb2);
                }
                else {
                    final double d12 = this.boundingBox.b - (int)this.boundingBox.b;
                    if (d12 > 0.0) {
                        this.br = (float)(this.br + d12 + 0.01);
                    }
                }
            }
            this.locX = (this.boundingBox.a + this.boundingBox.d) / 2.0;
            this.locY = this.boundingBox.b + this.height - this.br;
            this.locZ = (this.boundingBox.c + this.boundingBox.f) / 2.0;
            this.positionChanged = (d5 != d0 || d7 != d2);
            this.bc = (d6 != d1);
            this.onGround = (d6 != d1 && d6 < 0.0);
            this.bd = (this.positionChanged || this.bc);
            this.a(d1, this.onGround);
            if (d5 != d0) {
                this.motX = 0.0;
            }
            if (d6 != d1) {
                this.motY = 0.0;
            }
            if (d7 != d2) {
                this.motZ = 0.0;
            }
            final double d9 = this.locX - d3;
            final double d10 = this.locZ - d4;
            if (this.positionChanged && this.getBukkitEntity() instanceof Vehicle) {
                final Vehicle vehicle = (Vehicle)this.getBukkitEntity();
                Block block = this.world.getWorld().getBlockAt(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.20000000298023224 - this.height), MathHelper.floor(this.locZ));
                if (d5 > d0) {
                    block = block.getRelative(BlockFace.SOUTH);
                }
                else if (d5 < d0) {
                    block = block.getRelative(BlockFace.NORTH);
                }
                else if (d7 > d2) {
                    block = block.getRelative(BlockFace.WEST);
                }
                else if (d7 < d2) {
                    block = block.getRelative(BlockFace.EAST);
                }
                final VehicleBlockCollisionEvent event = new VehicleBlockCollisionEvent(vehicle, block);
                this.world.getServer().getPluginManager().callEvent((Event)event);
            }
            if (this.n() && !flag && this.vehicle == null) {
                this.bm += (float)(MathHelper.a(d9 * d9 + d10 * d10) * 0.6);
                final int l = MathHelper.floor(this.locX);
                final int i2 = MathHelper.floor(this.locY - 0.20000000298023224 - this.height);
                final int j2 = MathHelper.floor(this.locZ);
                int k = this.world.getTypeId(l, i2, j2);
                if (this.world.getTypeId(l, i2 - 1, j2) == net.minecraft.server.Block.FENCE.id) {
                    k = this.world.getTypeId(l, i2 - 1, j2);
                }
                if (this.bm > this.b && k > 0) {
                    ++this.b;
                    StepSound stepsound = net.minecraft.server.Block.byId[k].stepSound;
                    if (this.world.getTypeId(l, i2 + 1, j2) == net.minecraft.server.Block.SNOW.id) {
                        stepsound = net.minecraft.server.Block.SNOW.stepSound;
                        this.world.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.15f, stepsound.getVolume2());
                    }
                    else if (!net.minecraft.server.Block.byId[k].material.isLiquid()) {
                        this.world.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.15f, stepsound.getVolume2());
                    }
                    net.minecraft.server.Block.byId[k].b(this.world, l, i2, j2, this);
                }
            }
            final int l = MathHelper.floor(this.boundingBox.a + 0.001);
            final int i2 = MathHelper.floor(this.boundingBox.b + 0.001);
            final int j2 = MathHelper.floor(this.boundingBox.c + 0.001);
            int k = MathHelper.floor(this.boundingBox.d - 0.001);
            final int k2 = MathHelper.floor(this.boundingBox.e - 0.001);
            final int l2 = MathHelper.floor(this.boundingBox.f - 0.001);
            if (this.world.a(l, i2, j2, k, k2, l2)) {
                for (int i3 = l; i3 <= k; ++i3) {
                    for (int j3 = i2; j3 <= k2; ++j3) {
                        for (int k3 = j2; k3 <= l2; ++k3) {
                            final int l3 = this.world.getTypeId(i3, j3, k3);
                            if (l3 > 0) {
                                net.minecraft.server.Block.byId[l3].a(this.world, i3, j3, k3, this);
                            }
                        }
                    }
                }
            }
            final boolean flag3 = this.ac();
            if (this.world.d(this.boundingBox.shrink(0.001, 0.001, 0.001))) {
                this.burn(1);
                if (!flag3) {
                    ++this.fireTicks;
                    if (this.fireTicks <= 0) {
                        final EntityCombustEvent event2 = new EntityCombustEvent(this.getBukkitEntity());
                        this.world.getServer().getPluginManager().callEvent((Event)event2);
                        if (!event2.isCancelled()) {
                            this.fireTicks = 300;
                        }
                    }
                    else {
                        this.fireTicks = 300;
                    }
                }
            }
            else if (this.fireTicks <= 0) {
                this.fireTicks = -this.maxFireTicks;
            }
            if (flag3 && this.fireTicks > 0) {
                this.world.makeSound(this, "random.fizz", 0.7f, 1.6f + (this.random.nextFloat() - this.random.nextFloat()) * 0.4f);
                this.fireTicks = -this.maxFireTicks;
            }
        }
    }
    
    protected boolean n() {
        return true;
    }
    
    protected void a(final double d0, final boolean flag) {
        if (flag) {
            if (this.fallDistance > 0.0f) {
                this.a(this.fallDistance);
                this.fallDistance = 0.0f;
            }
        }
        else if (d0 < 0.0) {
            this.fallDistance -= (float)d0;
        }
    }
    
    public AxisAlignedBB e_() {
        return null;
    }
    
    protected void burn(int i) {
        if (!this.fireProof) {
            if (this instanceof EntityLiving) {
                final EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FIRE, i);
                this.world.getServer().getPluginManager().callEvent((Event)event);
                if (event.isCancelled()) {
                    return;
                }
                i = event.getDamage();
            }
            this.damageEntity(null, i);
        }
    }
    
    protected void a(final float f) {
        if (this.passenger != null) {
            this.passenger.a(f);
        }
    }
    
    public boolean ac() {
        return this.bA || this.world.s(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
    }
    
    public boolean ad() {
        return this.bA;
    }
    
    public boolean f_() {
        return this.world.a(this.boundingBox.b(0.0, -0.4000000059604645, 0.0).shrink(0.001, 0.001, 0.001), Material.WATER, this);
    }
    
    public boolean a(final Material material) {
        final double d0 = this.locY + this.t();
        final int i = MathHelper.floor(this.locX);
        final int j = MathHelper.d((float)MathHelper.floor(d0));
        final int k = MathHelper.floor(this.locZ);
        final int l = this.world.getTypeId(i, j, k);
        if (l != 0 && net.minecraft.server.Block.byId[l].material == material) {
            final float f = BlockFluids.c(this.world.getData(i, j, k)) - 0.11111111f;
            final float f2 = j + 1 - f;
            return d0 < f2;
        }
        return false;
    }
    
    public float t() {
        return 0.0f;
    }
    
    public boolean ae() {
        return this.world.a(this.boundingBox.b(-0.10000000149011612, -0.4000000059604645, -0.10000000149011612), Material.LAVA);
    }
    
    public void a(float f, float f1, final float f2) {
        float f3 = MathHelper.c(f * f + f1 * f1);
        if (f3 >= 0.01f) {
            if (f3 < 1.0f) {
                f3 = 1.0f;
            }
            f3 = f2 / f3;
            f *= f3;
            f1 *= f3;
            final float f4 = MathHelper.sin(this.yaw * 3.1415927f / 180.0f);
            final float f5 = MathHelper.cos(this.yaw * 3.1415927f / 180.0f);
            this.motX += f * f5 - f1 * f4;
            this.motZ += f1 * f5 + f * f4;
        }
    }
    
    public float c(final float f) {
        final int i = MathHelper.floor(this.locX);
        final double d0 = (this.boundingBox.e - this.boundingBox.b) * 0.66;
        final int j = MathHelper.floor(this.locY - this.height + d0);
        final int k = MathHelper.floor(this.locZ);
        if (this.world.a(MathHelper.floor(this.boundingBox.a), MathHelper.floor(this.boundingBox.b), MathHelper.floor(this.boundingBox.c), MathHelper.floor(this.boundingBox.d), MathHelper.floor(this.boundingBox.e), MathHelper.floor(this.boundingBox.f))) {
            float f2 = this.world.n(i, j, k);
            if (f2 < this.bF) {
                f2 = this.bF;
            }
            return f2;
        }
        return this.bF;
    }
    
    public void spawnIn(final World world) {
        if (world == null) {
            this.die();
            this.world = (World)Bukkit.getServer().getWorlds().get(0).getHandle();
            return;
        }
        this.world = world;
    }
    
    public void setLocation(final double d0, final double d1, final double d2, final float f, final float f1) {
        this.locX = d0;
        this.lastX = d0;
        this.locY = d1;
        this.lastY = d1;
        this.locZ = d2;
        this.lastZ = d2;
        this.yaw = f;
        this.lastYaw = f;
        this.pitch = f1;
        this.lastPitch = f1;
        this.br = 0.0f;
        final double d3 = this.lastYaw - f;
        if (d3 < -180.0) {
            this.lastYaw += 360.0f;
        }
        if (d3 >= 180.0) {
            this.lastYaw -= 360.0f;
        }
        this.setPosition(this.locX, this.locY, this.locZ);
        this.c(f, f1);
    }
    
    public void setPositionRotation(final double d0, final double d1, final double d2, final float f, final float f1) {
        this.locX = d0;
        this.lastX = d0;
        this.bo = d0;
        final double bp = d1 + this.height;
        this.locY = bp;
        this.lastY = bp;
        this.bp = bp;
        this.locZ = d2;
        this.lastZ = d2;
        this.bq = d2;
        this.yaw = f;
        this.pitch = f1;
        this.setPosition(this.locX, this.locY, this.locZ);
    }
    
    public float f(final Entity entity) {
        final float f = (float)(this.locX - entity.locX);
        final float f2 = (float)(this.locY - entity.locY);
        final float f3 = (float)(this.locZ - entity.locZ);
        return MathHelper.c(f * f + f2 * f2 + f3 * f3);
    }
    
    public double e(final double d0, final double d1, final double d2) {
        final double d3 = this.locX - d0;
        final double d4 = this.locY - d1;
        final double d5 = this.locZ - d2;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }
    
    public double f(final double d0, final double d1, final double d2) {
        final double d3 = this.locX - d0;
        final double d4 = this.locY - d1;
        final double d5 = this.locZ - d2;
        return MathHelper.a(d3 * d3 + d4 * d4 + d5 * d5);
    }
    
    public double g(final Entity entity) {
        final double d0 = this.locX - entity.locX;
        final double d2 = this.locY - entity.locY;
        final double d3 = this.locZ - entity.locZ;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public void b(final EntityHuman entityhuman) {
    }
    
    public void collide(final Entity entity) {
        if (entity.passenger != this && entity.vehicle != this) {
            double d0 = entity.locX - this.locX;
            double d2 = entity.locZ - this.locZ;
            double d3 = MathHelper.a(d0, d2);
            if (d3 >= 0.009999999776482582) {
                d3 = MathHelper.a(d3);
                d0 /= d3;
                d2 /= d3;
                double d4 = 1.0 / d3;
                if (d4 > 1.0) {
                    d4 = 1.0;
                }
                d0 *= d4;
                d2 *= d4;
                d0 *= 0.05000000074505806;
                d2 *= 0.05000000074505806;
                d0 *= 1.0f - this.bu;
                d2 *= 1.0f - this.bu;
                this.b(-d0, 0.0, -d2);
                entity.b(d0, 0.0, d2);
            }
        }
    }
    
    public void b(final double d0, final double d1, final double d2) {
        this.motX += d0;
        this.motY += d1;
        this.motZ += d2;
    }
    
    protected void af() {
        this.velocityChanged = true;
    }
    
    public boolean damageEntity(final Entity entity, final int i) {
        this.af();
        return false;
    }
    
    public boolean l_() {
        return false;
    }
    
    public boolean d_() {
        return false;
    }
    
    public void c(final Entity entity, final int i) {
    }
    
    public boolean c(final NBTTagCompound nbttagcompound) {
        final String s = this.ag();
        if (!this.dead && s != null) {
            nbttagcompound.setString("id", s);
            this.d(nbttagcompound);
            return true;
        }
        return false;
    }
    
    public void d(final NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Pos", (NBTBase)this.a(new double[] { this.locX, this.locY + this.br, this.locZ }));
        nbttagcompound.a("Motion", (NBTBase)this.a(new double[] { this.motX, this.motY, this.motZ }));
        if (Float.isNaN(this.yaw)) {
            this.yaw = 0.0f;
        }
        if (Float.isNaN(this.pitch)) {
            this.pitch = 0.0f;
        }
        nbttagcompound.a("Rotation", (NBTBase)this.a(new float[] { this.yaw, this.pitch }));
        nbttagcompound.a("FallDistance", this.fallDistance);
        nbttagcompound.a("Fire", (short)this.fireTicks);
        nbttagcompound.a("Air", (short)this.airTicks);
        nbttagcompound.a("OnGround", this.onGround);
        nbttagcompound.setLong("WorldUUIDLeast", this.world.getUUID().getLeastSignificantBits());
        nbttagcompound.setLong("WorldUUIDMost", this.world.getUUID().getMostSignificantBits());
        nbttagcompound.setLong("UUIDLeast", this.uniqueId.getLeastSignificantBits());
        nbttagcompound.setLong("UUIDMost", this.uniqueId.getMostSignificantBits());
        this.b(nbttagcompound);
    }
    
    public void e(final NBTTagCompound nbttagcompound) {
        final NBTTagList nbttaglist = nbttagcompound.l("Pos");
        final NBTTagList nbttaglist2 = nbttagcompound.l("Motion");
        final NBTTagList nbttaglist3 = nbttagcompound.l("Rotation");
        this.motX = ((NBTTagDouble)nbttaglist2.a(0)).a;
        this.motY = ((NBTTagDouble)nbttaglist2.a(1)).a;
        this.motZ = ((NBTTagDouble)nbttaglist2.a(2)).a;
        final double a = ((NBTTagDouble)nbttaglist.a(0)).a;
        this.locX = a;
        this.bo = a;
        this.lastX = a;
        final double a2 = ((NBTTagDouble)nbttaglist.a(1)).a;
        this.locY = a2;
        this.bp = a2;
        this.lastY = a2;
        final double a3 = ((NBTTagDouble)nbttaglist.a(2)).a;
        this.locZ = a3;
        this.bq = a3;
        this.lastZ = a3;
        final float a4 = ((NBTTagFloat)nbttaglist3.a(0)).a;
        this.yaw = a4;
        this.lastYaw = a4;
        final float a5 = ((NBTTagFloat)nbttaglist3.a(1)).a;
        this.pitch = a5;
        this.lastPitch = a5;
        this.fallDistance = nbttagcompound.g("FallDistance");
        this.fireTicks = nbttagcompound.d("Fire");
        this.airTicks = nbttagcompound.d("Air");
        this.onGround = nbttagcompound.m("OnGround");
        this.setPosition(this.locX, this.locY, this.locZ);
        final long least = nbttagcompound.getLong("UUIDLeast");
        final long most = nbttagcompound.getLong("UUIDMost");
        if (least != 0L && most != 0L) {
            this.uniqueId = new UUID(most, least);
        }
        this.c(this.yaw, this.pitch);
        this.a(nbttagcompound);
        if (!(this.getBukkitEntity() instanceof Vehicle)) {
            if (Math.abs(this.motX) > 10.0) {
                this.motX = 0.0;
            }
            if (Math.abs(this.motY) > 10.0) {
                this.motY = 0.0;
            }
            if (Math.abs(this.motZ) > 10.0) {
                this.motZ = 0.0;
            }
        }
        if (this instanceof EntityPlayer) {
            final Server server = Bukkit.getServer();
            org.bukkit.World bworld = null;
            final String worldName = nbttagcompound.getString("World");
            if (nbttagcompound.hasKey("WorldUUIDMost") && nbttagcompound.hasKey("WorldUUIDLeast")) {
                final UUID uid = new UUID(nbttagcompound.getLong("WorldUUIDMost"), nbttagcompound.getLong("WorldUUIDLeast"));
                bworld = server.getWorld(uid);
            }
            else {
                bworld = server.getWorld(worldName);
            }
            if (bworld == null) {
                final EntityPlayer entityPlayer = (EntityPlayer)this;
                bworld = (org.bukkit.World)((CraftServer)server).getServer().getWorldServer(entityPlayer.dimension).getWorld();
            }
            this.spawnIn((World)((bworld == null) ? null : ((CraftWorld)bworld).getHandle()));
        }
    }
    
    protected final String ag() {
        return EntityTypes.b(this);
    }
    
    protected abstract void a(final NBTTagCompound p0);
    
    protected abstract void b(final NBTTagCompound p0);
    
    protected NBTTagList a(final double... adouble) {
        final NBTTagList nbttaglist = new NBTTagList();
        final double[] adouble2 = adouble;
        for (int i = adouble.length, j = 0; j < i; ++j) {
            final double d0 = adouble2[j];
            nbttaglist.a((NBTBase)new NBTTagDouble(d0));
        }
        return nbttaglist;
    }
    
    protected NBTTagList a(final float... afloat) {
        final NBTTagList nbttaglist = new NBTTagList();
        final float[] afloat2 = afloat;
        for (int i = afloat.length, j = 0; j < i; ++j) {
            final float f = afloat2[j];
            nbttaglist.a((NBTBase)new NBTTagFloat(f));
        }
        return nbttaglist;
    }
    
    public EntityItem b(final int i, final int j) {
        return this.a(i, j, 0.0f);
    }
    
    public EntityItem a(final int i, final int j, final float f) {
        return this.a(new ItemStack(i, j, 0), f);
    }
    
    public EntityItem a(final ItemStack itemstack, final float f) {
        final EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY + f, this.locZ, itemstack);
        entityitem.pickupDelay = 10;
        this.world.addEntity((Entity)entityitem);
        return entityitem;
    }
    
    public boolean T() {
        return !this.dead;
    }
    
    public boolean K() {
        for (int i = 0; i < 8; ++i) {
            final float f = ((i >> 0) % 2 - 0.5f) * this.length * 0.9f;
            final float f2 = ((i >> 1) % 2 - 0.5f) * 0.1f;
            final float f3 = ((i >> 2) % 2 - 0.5f) * this.length * 0.9f;
            final int j = MathHelper.floor(this.locX + f);
            final int k = MathHelper.floor(this.locY + this.t() + f2);
            final int l = MathHelper.floor(this.locZ + f3);
            if (this.world.e(j, k, l)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean a(final EntityHuman entityhuman) {
        return false;
    }
    
    public AxisAlignedBB a_(final Entity entity) {
        return null;
    }
    
    public void E() {
        if (this.vehicle.dead) {
            this.vehicle = null;
        }
        else {
            this.motX = 0.0;
            this.motY = 0.0;
            this.motZ = 0.0;
            this.m_();
            if (this.vehicle != null) {
                this.vehicle.f();
                this.e += this.vehicle.yaw - this.vehicle.lastYaw;
                this.d += this.vehicle.pitch - this.vehicle.lastPitch;
                while (this.e >= 180.0) {
                    this.e -= 360.0;
                }
                while (this.e < -180.0) {
                    this.e += 360.0;
                }
                while (this.d >= 180.0) {
                    this.d -= 360.0;
                }
                while (this.d < -180.0) {
                    this.d += 360.0;
                }
                double d0 = this.e * 0.5;
                double d2 = this.d * 0.5;
                final float f = 10.0f;
                if (d0 > f) {
                    d0 = f;
                }
                if (d0 < -f) {
                    d0 = -f;
                }
                if (d2 > f) {
                    d2 = f;
                }
                if (d2 < -f) {
                    d2 = -f;
                }
                this.e -= d0;
                this.d -= d2;
                this.yaw += (float)d0;
                this.pitch += (float)d2;
            }
        }
    }
    
    public void f() {
        this.passenger.setPosition(this.locX, this.locY + this.m() + this.passenger.I(), this.locZ);
    }
    
    public double I() {
        return this.height;
    }
    
    public double m() {
        return this.width * 0.75;
    }
    
    public void mount(final Entity entity) {
        this.setPassengerOf(entity);
    }
    
    public org.bukkit.entity.Entity getBukkitEntity() {
        if (this.bukkitEntity == null) {
            this.bukkitEntity = (org.bukkit.entity.Entity)CraftEntity.getEntity(this.world.getServer(), this);
        }
        return this.bukkitEntity;
    }
    
    public void setPassengerOf(final Entity entity) {
        this.d = 0.0;
        this.e = 0.0;
        if (entity == null) {
            if (this.vehicle != null) {
                if (this.getBukkitEntity() instanceof LivingEntity && this.vehicle.getBukkitEntity() instanceof Vehicle) {
                    final VehicleExitEvent event = new VehicleExitEvent((Vehicle)this.vehicle.getBukkitEntity(), (LivingEntity)this.getBukkitEntity());
                    this.world.getServer().getPluginManager().callEvent((Event)event);
                }
                this.setPositionRotation(this.vehicle.locX, this.vehicle.boundingBox.b + this.vehicle.width, this.vehicle.locZ, this.yaw, this.pitch);
                this.vehicle.passenger = null;
            }
            this.vehicle = null;
        }
        else if (this.vehicle == entity) {
            if (this.getBukkitEntity() instanceof LivingEntity && this.vehicle.getBukkitEntity() instanceof Vehicle) {
                final VehicleExitEvent event = new VehicleExitEvent((Vehicle)this.vehicle.getBukkitEntity(), (LivingEntity)this.getBukkitEntity());
                this.world.getServer().getPluginManager().callEvent((Event)event);
            }
            this.vehicle.passenger = null;
            this.vehicle = null;
            this.setPositionRotation(entity.locX, entity.boundingBox.b + entity.width, entity.locZ, this.yaw, this.pitch);
        }
        else {
            if (this.vehicle != null) {
                this.vehicle.passenger = null;
            }
            if (entity.passenger != null) {
                entity.passenger.vehicle = null;
            }
            this.vehicle = entity;
            entity.passenger = this;
        }
    }
    
    public Vec3D Z() {
        return null;
    }
    
    public void P() {
    }
    
    public ItemStack[] getEquipment() {
        return null;
    }
    
    public boolean isSneaking() {
        return this.d(1);
    }
    
    public void setSneak(final boolean flag) {
        this.a(1, flag);
    }
    
    protected boolean d(final int i) {
        return (this.datawatcher.a(0) & 1 << i) != 0x0;
    }
    
    protected void a(final int i, final boolean flag) {
        final byte b0 = this.datawatcher.a(0);
        if (flag) {
            this.datawatcher.watch(0, (Object)(byte)(b0 | 1 << i));
        }
        else {
            this.datawatcher.watch(0, (Object)(byte)(b0 & ~(1 << i)));
        }
    }
    
    public void a(final EntityWeatherStorm entityweatherstorm) {
        final EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(entityweatherstorm.getBukkitEntity(), this.getBukkitEntity(), EntityDamageEvent.DamageCause.LIGHTNING, 5);
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return;
        }
        this.burn(event.getDamage());
        ++this.fireTicks;
        if (this.fireTicks == 0) {
            this.fireTicks = 300;
        }
    }
    
    public void a(final EntityLiving entityliving) {
    }
    
    protected boolean g(final double d0, final double d1, final double d2) {
        final int i = MathHelper.floor(d0);
        final int j = MathHelper.floor(d1);
        final int k = MathHelper.floor(d2);
        final double d3 = d0 - i;
        final double d4 = d1 - j;
        final double d5 = d2 - k;
        if (this.world.e(i, j, k)) {
            final boolean flag = !this.world.e(i - 1, j, k);
            final boolean flag2 = !this.world.e(i + 1, j, k);
            final boolean flag3 = !this.world.e(i, j - 1, k);
            final boolean flag4 = !this.world.e(i, j + 1, k);
            final boolean flag5 = !this.world.e(i, j, k - 1);
            final boolean flag6 = !this.world.e(i, j, k + 1);
            byte b0 = -1;
            double d6 = 9999.0;
            if (flag && d3 < d6) {
                d6 = d3;
                b0 = 0;
            }
            if (flag2 && 1.0 - d3 < d6) {
                d6 = 1.0 - d3;
                b0 = 1;
            }
            if (flag3 && d4 < d6) {
                d6 = d4;
                b0 = 2;
            }
            if (flag4 && 1.0 - d4 < d6) {
                d6 = 1.0 - d4;
                b0 = 3;
            }
            if (flag5 && d5 < d6) {
                d6 = d5;
                b0 = 4;
            }
            if (flag6 && 1.0 - d5 < d6) {
                d6 = 1.0 - d5;
                b0 = 5;
            }
            final float f = this.random.nextFloat() * 0.2f + 0.1f;
            if (b0 == 0) {
                this.motX = -f;
            }
            if (b0 == 1) {
                this.motX = f;
            }
            if (b0 == 2) {
                this.motY = -f;
            }
            if (b0 == 3) {
                this.motY = f;
            }
            if (b0 == 4) {
                this.motZ = -f;
            }
            if (b0 == 5) {
                this.motZ = f;
            }
        }
        return false;
    }
    
    static {
        Entity.entityCount = 0;
    }
}
