// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.event.entity.EntityDamageByBlockEvent;
import java.util.Iterator;
import java.util.List;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class EntityLiving extends Entity
{
    public int maxNoDamageTicks;
    public float I;
    public float J;
    public float K;
    public float L;
    protected float M;
    protected float N;
    protected float O;
    protected float P;
    protected boolean Q;
    protected String texture;
    protected boolean S;
    protected float T;
    protected String U;
    protected float V;
    protected int W;
    protected float X;
    public boolean Y;
    public float Z;
    public float aa;
    public int health;
    public int ac;
    private int a;
    public int hurtTicks;
    public int ae;
    public float af;
    public int deathTicks;
    public int attackTicks;
    public float ai;
    public float aj;
    protected boolean ak;
    public int al;
    public float am;
    public float an;
    public float ao;
    public float ap;
    protected int aq;
    protected double ar;
    protected double as;
    protected double at;
    protected double au;
    protected double av;
    float aw;
    public int lastDamage;
    protected int ay;
    protected float az;
    protected float aA;
    protected float aB;
    protected boolean aC;
    protected float aD;
    protected float aE;
    private Entity b;
    protected int aF;
    
    public EntityLiving(final World world) {
        super(world);
        this.maxNoDamageTicks = 20;
        this.K = 0.0f;
        this.L = 0.0f;
        this.Q = true;
        this.texture = "/mob/char.png";
        this.S = true;
        this.T = 0.0f;
        this.U = null;
        this.V = 1.0f;
        this.W = 0;
        this.X = 0.0f;
        this.Y = false;
        this.health = 10;
        this.af = 0.0f;
        this.deathTicks = 0;
        this.attackTicks = 0;
        this.ak = false;
        this.al = -1;
        this.am = (float)(Math.random() * 0.8999999761581421 + 0.10000000149011612);
        this.aw = 0.0f;
        this.lastDamage = 0;
        this.ay = 0;
        this.aC = false;
        this.aD = 0.0f;
        this.aE = 0.7f;
        this.aF = 0;
        this.aI = true;
        this.J = (float)(Math.random() + 1.0) * 0.01f;
        this.setPosition(this.locX, this.locY, this.locZ);
        this.I = (float)Math.random() * 12398.0f;
        this.yaw = (float)(Math.random() * 3.1415927410125732 * 2.0);
        this.bs = 0.5f;
    }
    
    protected void b() {
    }
    
    public boolean e(final Entity entity) {
        return this.world.a(Vec3D.create(this.locX, this.locY + this.t(), this.locZ), Vec3D.create(entity.locX, entity.locY + entity.t(), entity.locZ)) == null;
    }
    
    public boolean l_() {
        return !this.dead;
    }
    
    public boolean d_() {
        return !this.dead;
    }
    
    public float t() {
        return this.width * 0.85f;
    }
    
    public int e() {
        return 80;
    }
    
    public void Q() {
        final String s = this.g();
        if (s != null) {
            this.world.makeSound((Entity)this, s, this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
    }
    
    public void R() {
        this.Z = this.aa;
        super.R();
        if (this.random.nextInt(1000) < this.a++) {
            this.a = -this.e();
            this.Q();
        }
        if (this.T() && this.K()) {
            final EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.SUFFOCATION, 1);
            this.world.getServer().getPluginManager().callEvent((Event)event);
            if (!event.isCancelled()) {
                this.damageEntity(null, event.getDamage());
            }
        }
        if (this.fireProof || this.world.isStatic) {
            this.fireTicks = 0;
        }
        if (this.T() && this.a(Material.WATER) && !this.b_()) {
            --this.airTicks;
            if (this.airTicks == -20) {
                this.airTicks = 0;
                for (int i = 0; i < 8; ++i) {
                    final float f = this.random.nextFloat() - this.random.nextFloat();
                    final float f2 = this.random.nextFloat() - this.random.nextFloat();
                    final float f3 = this.random.nextFloat() - this.random.nextFloat();
                    this.world.a("bubble", this.locX + f, this.locY + f2, this.locZ + f3, this.motX, this.motY, this.motZ);
                }
                final EntityDamageEvent event2 = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.DROWNING, 2);
                this.world.getServer().getPluginManager().callEvent((Event)event2);
                if (!event2.isCancelled() && event2.getDamage() != 0) {
                    this.damageEntity(null, event2.getDamage());
                }
            }
            this.fireTicks = 0;
        }
        else {
            this.airTicks = this.maxAirTicks;
        }
        this.ai = this.aj;
        if (this.attackTicks > 0) {
            --this.attackTicks;
        }
        if (this.hurtTicks > 0) {
            --this.hurtTicks;
        }
        if (this.noDamageTicks > 0) {
            --this.noDamageTicks;
        }
        if (this.health <= 0) {
            ++this.deathTicks;
            if (this.deathTicks > 20) {
                this.X();
                this.die();
                for (int i = 0; i < 20; ++i) {
                    final double d0 = this.random.nextGaussian() * 0.02;
                    final double d2 = this.random.nextGaussian() * 0.02;
                    final double d3 = this.random.nextGaussian() * 0.02;
                    this.world.a("explode", this.locX + this.random.nextFloat() * this.length * 2.0f - this.length, this.locY + this.random.nextFloat() * this.width, this.locZ + this.random.nextFloat() * this.length * 2.0f - this.length, d0, d2, d3);
                }
            }
        }
        this.P = this.O;
        this.L = this.K;
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
    }
    
    public void S() {
        for (int i = 0; i < 20; ++i) {
            final double d0 = this.random.nextGaussian() * 0.02;
            final double d2 = this.random.nextGaussian() * 0.02;
            final double d3 = this.random.nextGaussian() * 0.02;
            final double d4 = 10.0;
            this.world.a("explode", this.locX + this.random.nextFloat() * this.length * 2.0f - this.length - d0 * d4, this.locY + this.random.nextFloat() * this.width - d2 * d4, this.locZ + this.random.nextFloat() * this.length * 2.0f - this.length - d3 * d4, d0, d2, d3);
        }
    }
    
    public void E() {
        super.E();
        this.M = this.N;
        this.N = 0.0f;
    }
    
    public void m_() {
        super.m_();
        this.v();
        final double d0 = this.locX - this.lastX;
        final double d2 = this.locZ - this.lastZ;
        final float f = MathHelper.a(d0 * d0 + d2 * d2);
        float f2 = this.K;
        float f3 = 0.0f;
        this.M = this.N;
        float f4 = 0.0f;
        if (f > 0.05f) {
            f4 = 1.0f;
            f3 = f * 3.0f;
            f2 = (float)TrigMath.atan2(d2, d0) * 180.0f / 3.1415927f - 90.0f;
        }
        if (this.aa > 0.0f) {
            f2 = this.yaw;
        }
        if (!this.onGround) {
            f4 = 0.0f;
        }
        this.N += (f4 - this.N) * 0.3f;
        float f5;
        for (f5 = f2 - this.K; f5 < -180.0f; f5 += 360.0f) {}
        while (f5 >= 180.0f) {
            f5 -= 360.0f;
        }
        this.K += f5 * 0.3f;
        float f6;
        for (f6 = this.yaw - this.K; f6 < -180.0f; f6 += 360.0f) {}
        while (f6 >= 180.0f) {
            f6 -= 360.0f;
        }
        final boolean flag = f6 < -90.0f || f6 >= 90.0f;
        if (f6 < -75.0f) {
            f6 = -75.0f;
        }
        if (f6 >= 75.0f) {
            f6 = 75.0f;
        }
        this.K = this.yaw - f6;
        if (f6 * f6 > 2500.0f) {
            this.K += f6 * 0.2f;
        }
        if (flag) {
            f3 *= -1.0f;
        }
        while (this.yaw - this.lastYaw < -180.0f) {
            this.lastYaw -= 360.0f;
        }
        while (this.yaw - this.lastYaw >= 180.0f) {
            this.lastYaw += 360.0f;
        }
        while (this.K - this.L < -180.0f) {
            this.L -= 360.0f;
        }
        while (this.K - this.L >= 180.0f) {
            this.L += 360.0f;
        }
        while (this.pitch - this.lastPitch < -180.0f) {
            this.lastPitch -= 360.0f;
        }
        while (this.pitch - this.lastPitch >= 180.0f) {
            this.lastPitch += 360.0f;
        }
        this.O += f3;
    }
    
    protected void b(final float f, final float f1) {
        super.b(f, f1);
    }
    
    public void b(final int i) {
        this.b(i, EntityRegainHealthEvent.RegainReason.CUSTOM);
    }
    
    public void b(final int i, final EntityRegainHealthEvent.RegainReason regainReason) {
        if (this.health > 0) {
            final EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), i, regainReason);
            this.world.getServer().getPluginManager().callEvent((Event)event);
            if (!event.isCancelled()) {
                this.health += event.getAmount();
            }
            if (this.health > 20) {
                this.health = 20;
            }
            this.noDamageTicks = this.maxNoDamageTicks / 2;
        }
    }
    
    public boolean damageEntity(final Entity entity, final int i) {
        if (this.world.isStatic) {
            return false;
        }
        this.ay = 0;
        if (this.health <= 0) {
            return false;
        }
        this.ao = 1.5f;
        boolean flag = true;
        if (this.noDamageTicks > this.maxNoDamageTicks / 2.0f) {
            if (i <= this.lastDamage) {
                return false;
            }
            this.c(i - this.lastDamage);
            this.lastDamage = i;
            flag = false;
        }
        else {
            this.lastDamage = i;
            this.ac = this.health;
            this.noDamageTicks = this.maxNoDamageTicks;
            this.c(i);
            final int n = 10;
            this.ae = n;
            this.hurtTicks = n;
        }
        this.af = 0.0f;
        if (flag) {
            this.world.a((Entity)this, (byte)2);
            this.af();
            if (entity != null) {
                double d0;
                double d2;
                for (d0 = entity.locX - this.locX, d2 = entity.locZ - this.locZ; d0 * d0 + d2 * d2 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01, d2 = (Math.random() - Math.random()) * 0.01) {}
                this.af = (float)(Math.atan2(d2, d0) * 180.0 / 3.1415927410125732) - this.yaw;
                this.a(entity, i, d0, d2);
            }
            else {
                this.af = (float)((int)(Math.random() * 2.0) * 180);
            }
        }
        if (this.health <= 0) {
            if (flag) {
                this.world.makeSound((Entity)this, this.i(), this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }
            this.die(entity);
        }
        else if (flag) {
            this.world.makeSound((Entity)this, this.h(), this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
        }
        return true;
    }
    
    protected void c(final int i) {
        this.health -= i;
    }
    
    protected float k() {
        return 1.0f;
    }
    
    protected String g() {
        return null;
    }
    
    protected String h() {
        return "random.hurt";
    }
    
    protected String i() {
        return "random.hurt";
    }
    
    public void a(final Entity entity, final int i, final double d0, final double d1) {
        final float f = MathHelper.a(d0 * d0 + d1 * d1);
        final float f2 = 0.4f;
        this.motX /= 2.0;
        this.motY /= 2.0;
        this.motZ /= 2.0;
        this.motX -= d0 / f * f2;
        this.motY += 0.4000000059604645;
        this.motZ -= d1 / f * f2;
        if (this.motY > 0.4000000059604645) {
            this.motY = 0.4000000059604645;
        }
    }
    
    public void die(final Entity entity) {
        if (this.W >= 0 && entity != null) {
            entity.c((Entity)this, this.W);
        }
        if (entity != null) {
            entity.a(this);
        }
        this.ak = true;
        if (!this.world.isStatic) {
            this.q();
        }
        this.world.a((Entity)this, (byte)3);
    }
    
    protected void q() {
        final int i = this.j();
        final List<ItemStack> loot = new ArrayList<ItemStack>();
        final int count = this.random.nextInt(3);
        if (i > 0 && count > 0) {
            loot.add(new ItemStack(i, count));
        }
        final CraftEntity entity = (CraftEntity)this.getBukkitEntity();
        final EntityDeathEvent event = new EntityDeathEvent((org.bukkit.entity.Entity)entity, (List)loot);
        final org.bukkit.World bworld = (org.bukkit.World)this.world.getWorld();
        this.world.getServer().getPluginManager().callEvent((Event)event);
        for (final ItemStack stack : event.getDrops()) {
            bworld.dropItemNaturally(entity.getLocation(), stack);
        }
    }
    
    protected int j() {
        return 0;
    }
    
    protected void a(final float f) {
        super.a(f);
        final int i = (int)Math.ceil(f - 3.0f);
        if (i > 0) {
            final EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FALL, i);
            this.world.getServer().getPluginManager().callEvent((Event)event);
            if (!event.isCancelled() && event.getDamage() != 0) {
                this.damageEntity(null, event.getDamage());
            }
            final int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.locY - 0.20000000298023224 - this.height), MathHelper.floor(this.locZ));
            if (j > 0) {
                final StepSound stepsound = Block.byId[j].stepSound;
                this.world.makeSound((Entity)this, stepsound.getName(), stepsound.getVolume1() * 0.5f, stepsound.getVolume2() * 0.75f);
            }
        }
    }
    
    public void a(final float f, final float f1) {
        if (this.ad()) {
            final double d0 = this.locY;
            this.a(f, f1, 0.02f);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.800000011920929;
            this.motY *= 0.800000011920929;
            this.motZ *= 0.800000011920929;
            this.motY -= 0.02;
            if (this.positionChanged && this.d(this.motX, this.motY + 0.6000000238418579 - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896;
            }
        }
        else if (this.ae()) {
            final double d0 = this.locY;
            this.a(f, f1, 0.02f);
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.5;
            this.motY *= 0.5;
            this.motZ *= 0.5;
            this.motY -= 0.02;
            if (this.positionChanged && this.d(this.motX, this.motY + 0.6000000238418579 - this.locY + d0, this.motZ)) {
                this.motY = 0.30000001192092896;
            }
        }
        else {
            float f2 = 0.91f;
            if (this.onGround) {
                f2 = 0.54600006f;
                final int i = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));
                if (i > 0) {
                    f2 = Block.byId[i].frictionFactor * 0.91f;
                }
            }
            final float f3 = 0.16277136f / (f2 * f2 * f2);
            this.a(f, f1, this.onGround ? (0.1f * f3) : 0.02f);
            f2 = 0.91f;
            if (this.onGround) {
                f2 = 0.54600006f;
                final int j = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));
                if (j > 0) {
                    f2 = Block.byId[j].frictionFactor * 0.91f;
                }
            }
            if (this.p()) {
                final float f4 = 0.15f;
                if (this.motX < -f4) {
                    this.motX = -f4;
                }
                if (this.motX > f4) {
                    this.motX = f4;
                }
                if (this.motZ < -f4) {
                    this.motZ = -f4;
                }
                if (this.motZ > f4) {
                    this.motZ = f4;
                }
                this.fallDistance = 0.0f;
                if (this.motY < -0.15) {
                    this.motY = -0.15;
                }
                if (this.isSneaking() && this.motY < 0.0) {
                    this.motY = 0.0;
                }
            }
            this.move(this.motX, this.motY, this.motZ);
            if (this.positionChanged && this.p()) {
                this.motY = 0.2;
            }
            this.motY -= 0.08;
            this.motY *= 0.9800000190734863;
            this.motX *= f2;
            this.motZ *= f2;
        }
        this.an = this.ao;
        final double d0 = this.locX - this.lastX;
        final double d2 = this.locZ - this.lastZ;
        float f5 = MathHelper.a(d0 * d0 + d2 * d2) * 4.0f;
        if (f5 > 1.0f) {
            f5 = 1.0f;
        }
        this.ao += (f5 - this.ao) * 0.4f;
        this.ap += this.ao;
    }
    
    public boolean p() {
        final int i = MathHelper.floor(this.locX);
        final int j = MathHelper.floor(this.boundingBox.b);
        final int k = MathHelper.floor(this.locZ);
        final Block block = Block.byId[this.world.getTypeId(i, j, k)];
        return block != null && block.isLadder();
    }
    
    public void b(final NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Health", (short)this.health);
        nbttagcompound.a("HurtTime", (short)this.hurtTicks);
        nbttagcompound.a("DeathTime", (short)this.deathTicks);
        nbttagcompound.a("AttackTime", (short)this.attackTicks);
    }
    
    public void a(final NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.d("Health");
        if (!nbttagcompound.hasKey("Health")) {
            this.health = 10;
        }
        this.hurtTicks = nbttagcompound.d("HurtTime");
        this.deathTicks = nbttagcompound.d("DeathTime");
        this.attackTicks = nbttagcompound.d("AttackTime");
    }
    
    public boolean T() {
        return !this.dead && this.health > 0;
    }
    
    public boolean b_() {
        return false;
    }
    
    public void v() {
        if (this.aq > 0) {
            final double d0 = this.locX + (this.ar - this.locX) / this.aq;
            double d2 = this.locY + (this.as - this.locY) / this.aq;
            final double d3 = this.locZ + (this.at - this.locZ) / this.aq;
            double d4;
            for (d4 = this.au - this.yaw; d4 < -180.0; d4 += 360.0) {}
            while (d4 >= 180.0) {
                d4 -= 360.0;
            }
            this.yaw += (float)(d4 / this.aq);
            this.pitch += (float)((this.av - this.pitch) / this.aq);
            --this.aq;
            this.setPosition(d0, d2, d3);
            this.c(this.yaw, this.pitch);
            final List list = this.world.getEntities((Entity)this, this.boundingBox.shrink(0.03125, 0.0, 0.03125));
            if (list.size() > 0) {
                double d5 = 0.0;
                for (int i = 0; i < list.size(); ++i) {
                    final AxisAlignedBB axisalignedbb = list.get(i);
                    if (axisalignedbb.e > d5) {
                        d5 = axisalignedbb.e;
                    }
                }
                d2 += d5 - this.boundingBox.b;
                this.setPosition(d0, d2, d3);
            }
        }
        if (this.D()) {
            this.aC = false;
            this.az = 0.0f;
            this.aA = 0.0f;
            this.aB = 0.0f;
        }
        else if (!this.Y) {
            this.c_();
        }
        final boolean flag = this.ad();
        final boolean flag2 = this.ae();
        if (this.aC) {
            if (flag) {
                this.motY += 0.03999999910593033;
            }
            else if (flag2) {
                this.motY += 0.03999999910593033;
            }
            else if (this.onGround) {
                this.O();
            }
        }
        this.az *= 0.98f;
        this.aA *= 0.98f;
        this.aB *= 0.9f;
        this.a(this.az, this.aA);
        final List list2 = this.world.b((Entity)this, this.boundingBox.b(0.20000000298023224, 0.0, 0.20000000298023224));
        if (list2 != null && list2.size() > 0) {
            for (int j = 0; j < list2.size(); ++j) {
                final Entity entity = list2.get(j);
                if (entity.d_()) {
                    entity.collide((Entity)this);
                }
            }
        }
    }
    
    protected boolean D() {
        return this.health <= 0;
    }
    
    protected void O() {
        this.motY = 0.41999998688697815;
    }
    
    protected boolean h_() {
        return true;
    }
    
    protected void U() {
        final EntityHuman entityhuman = this.world.findNearbyPlayer((Entity)this, -1.0);
        if (this.h_() && entityhuman != null) {
            final double d0 = entityhuman.locX - this.locX;
            final double d2 = entityhuman.locY - this.locY;
            final double d3 = entityhuman.locZ - this.locZ;
            final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
            if (d4 > 16384.0) {
                this.die();
            }
            if (this.ay > 600 && this.random.nextInt(800) == 0) {
                if (d4 < 1024.0) {
                    this.ay = 0;
                }
                else {
                    this.die();
                }
            }
        }
    }
    
    protected void c_() {
        ++this.ay;
        EntityHuman entityhuman = this.world.findNearbyPlayer((Entity)this, -1.0);
        this.U();
        this.az = 0.0f;
        this.aA = 0.0f;
        final float f = 8.0f;
        if (this.random.nextFloat() < 0.02f) {
            entityhuman = this.world.findNearbyPlayer((Entity)this, (double)f);
            if (entityhuman != null) {
                this.b = (Entity)entityhuman;
                this.aF = 10 + this.random.nextInt(20);
            }
            else {
                this.aB = (this.random.nextFloat() - 0.5f) * 20.0f;
            }
        }
        if (this.b != null) {
            this.a(this.b, 10.0f, (float)this.u());
            if (this.aF-- <= 0 || this.b.dead || this.b.g((Entity)this) > f * f) {
                this.b = null;
            }
        }
        else {
            if (this.random.nextFloat() < 0.05f) {
                this.aB = (this.random.nextFloat() - 0.5f) * 20.0f;
            }
            this.yaw += this.aB;
            this.pitch = this.aD;
        }
        final boolean flag = this.ad();
        final boolean flag2 = this.ae();
        if (flag || flag2) {
            this.aC = (this.random.nextFloat() < 0.8f);
        }
    }
    
    protected int u() {
        return 40;
    }
    
    public void a(final Entity entity, final float f, final float f1) {
        final double d0 = entity.locX - this.locX;
        final double d2 = entity.locZ - this.locZ;
        double d3;
        if (entity instanceof EntityLiving) {
            final EntityLiving entityliving = (EntityLiving)entity;
            d3 = this.locY + this.t() - (entityliving.locY + entityliving.t());
        }
        else {
            d3 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0 - (this.locY + this.t());
        }
        final double d4 = MathHelper.a(d0 * d0 + d2 * d2);
        final float f2 = (float)(Math.atan2(d2, d0) * 180.0 / 3.1415927410125732) - 90.0f;
        final float f3 = (float)(-(Math.atan2(d3, d4) * 180.0 / 3.1415927410125732));
        this.pitch = -this.b(this.pitch, f3, f1);
        this.yaw = this.b(this.yaw, f2, f);
    }
    
    public boolean V() {
        return this.b != null;
    }
    
    public Entity W() {
        return this.b;
    }
    
    private float b(final float f, final float f1, final float f2) {
        float f3;
        for (f3 = f1 - f; f3 < -180.0f; f3 += 360.0f) {}
        while (f3 >= 180.0f) {
            f3 -= 360.0f;
        }
        if (f3 > f2) {
            f3 = f2;
        }
        if (f3 < -f2) {
            f3 = -f2;
        }
        return f + f3;
    }
    
    public void X() {
    }
    
    public boolean d() {
        return this.world.containsEntity(this.boundingBox) && this.world.getEntities((Entity)this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }
    
    protected void Y() {
        final EntityDamageByBlockEvent event = new EntityDamageByBlockEvent((org.bukkit.block.Block)null, this.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, 4);
        this.world.getServer().getPluginManager().callEvent((Event)event);
        if (event.isCancelled() || event.getDamage() == 0) {
            return;
        }
        this.damageEntity(null, event.getDamage());
    }
    
    public Vec3D Z() {
        return this.b(1.0f);
    }
    
    public Vec3D b(final float f) {
        if (f == 1.0f) {
            final float f2 = MathHelper.cos(-this.yaw * 0.017453292f - 3.1415927f);
            final float f3 = MathHelper.sin(-this.yaw * 0.017453292f - 3.1415927f);
            final float f4 = -MathHelper.cos(-this.pitch * 0.017453292f);
            final float f5 = MathHelper.sin(-this.pitch * 0.017453292f);
            return Vec3D.create((double)(f3 * f4), (double)f5, (double)(f2 * f4));
        }
        final float f2 = this.lastPitch + (this.pitch - this.lastPitch) * f;
        final float f3 = this.lastYaw + (this.yaw - this.lastYaw) * f;
        final float f4 = MathHelper.cos(-f3 * 0.017453292f - 3.1415927f);
        final float f5 = MathHelper.sin(-f3 * 0.017453292f - 3.1415927f);
        final float f6 = -MathHelper.cos(-f2 * 0.017453292f);
        final float f7 = MathHelper.sin(-f2 * 0.017453292f);
        return Vec3D.create((double)(f5 * f6), (double)f7, (double)(f4 * f6));
    }
    
    public int l() {
        return 4;
    }
    
    public boolean isSleeping() {
        return false;
    }
}
