// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import net.minecraft.server.forge.ForgeHooks;
import net.minecraft.server.forge.ArmorProperties;
import net.minecraft.server.forge.ISpecialArmor;
import java.util.Iterator;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public abstract class EntityHuman extends EntityLiving
{
    public InventoryPlayer inventory;
    public Container defaultContainer;
    public Container activeContainer;
    public byte l;
    public int m;
    public float n;
    public float o;
    public boolean p;
    public int q;
    public String name;
    public int dimension;
    public double t;
    public double u;
    public double v;
    public double w;
    public double x;
    public double y;
    public boolean sleeping;
    public ChunkCoordinates A;
    public int sleepTicks;
    public float B;
    public float C;
    private ChunkCoordinates b;
    private ChunkCoordinates c;
    public int D;
    protected boolean E;
    public float F;
    private int d;
    public EntityFish hookedFish;
    public boolean fauxSleeping;
    public String spawnWorld;
    
    public EntityHuman(final World world) {
        super(world);
        this.inventory = new InventoryPlayer(this);
        this.l = 0;
        this.m = 0;
        this.p = false;
        this.q = 0;
        this.D = 20;
        this.E = false;
        this.d = 0;
        this.hookedFish = null;
        this.spawnWorld = "";
        this.defaultContainer = (Container)new ContainerPlayer(this.inventory, !world.isStatic);
        this.activeContainer = this.defaultContainer;
        this.height = 1.62f;
        final ChunkCoordinates chunkcoordinates = world.getSpawn();
        this.setPositionRotation(chunkcoordinates.x + 0.5, (double)(chunkcoordinates.y + 1), chunkcoordinates.z + 0.5, 0.0f, 0.0f);
        this.health = 20;
        this.U = "humanoid";
        this.T = 180.0f;
        this.maxFireTicks = 20;
        this.texture = "/mob/char.png";
    }
    
    protected void b() {
        super.b();
        this.datawatcher.a(16, (Object)0);
    }
    
    public void m_() {
        if (this.isSleeping()) {
            ++this.sleepTicks;
            if (this.sleepTicks > 100) {
                this.sleepTicks = 100;
            }
            if (!this.world.isStatic) {
                if (!this.o()) {
                    this.a(true, true, false);
                }
                else if (this.world.d()) {
                    this.a(false, true, true);
                }
            }
        }
        else if (this.sleepTicks > 0) {
            ++this.sleepTicks;
            if (this.sleepTicks >= 110) {
                this.sleepTicks = 0;
            }
        }
        super.m_();
        if (!this.world.isStatic && this.activeContainer != null && !this.activeContainer.b(this)) {
            this.y();
            this.activeContainer = this.defaultContainer;
        }
        this.t = this.w;
        this.u = this.x;
        this.v = this.y;
        final double d0 = this.locX - this.w;
        final double d2 = this.locY - this.x;
        final double d3 = this.locZ - this.y;
        final double d4 = 10.0;
        if (d0 > d4) {
            final double locX = this.locX;
            this.w = locX;
            this.t = locX;
        }
        if (d3 > d4) {
            final double locZ = this.locZ;
            this.y = locZ;
            this.v = locZ;
        }
        if (d2 > d4) {
            final double locY = this.locY;
            this.x = locY;
            this.u = locY;
        }
        if (d0 < -d4) {
            final double locX2 = this.locX;
            this.w = locX2;
            this.t = locX2;
        }
        if (d3 < -d4) {
            final double locZ2 = this.locZ;
            this.y = locZ2;
            this.v = locZ2;
        }
        if (d2 < -d4) {
            final double locY2 = this.locY;
            this.x = locY2;
            this.u = locY2;
        }
        this.w += d0 * 0.25;
        this.y += d3 * 0.25;
        this.x += d2 * 0.25;
        this.a(StatisticList.k, 1);
        if (this.vehicle == null) {
            this.c = null;
        }
    }
    
    protected boolean D() {
        return this.health <= 0 || this.isSleeping();
    }
    
    protected void y() {
        this.activeContainer = this.defaultContainer;
    }
    
    public void E() {
        final double d0 = this.locX;
        final double d2 = this.locY;
        final double d3 = this.locZ;
        super.E();
        this.n = this.o;
        this.o = 0.0f;
        this.i(this.locX - d0, this.locY - d2, this.locZ - d3);
    }
    
    protected void c_() {
        if (this.p) {
            ++this.q;
            if (this.q >= 8) {
                this.q = 0;
                this.p = false;
            }
        }
        else {
            this.q = 0;
        }
        this.aa = this.q / 8.0f;
    }
    
    public void v() {
        if (!this.world.allowMonsters && this.health < 20 && this.ticksLived % 20 * 12 == 0) {
            this.b(1, EntityRegainHealthEvent.RegainReason.REGEN);
        }
        this.inventory.f();
        this.n = this.o;
        super.v();
        float f = MathHelper.a(this.motX * this.motX + this.motZ * this.motZ);
        float f2 = (float)TrigMath.atan(-this.motY * 0.20000000298023224) * 15.0f;
        if (f > 0.1f) {
            f = 0.1f;
        }
        if (!this.onGround || this.health <= 0) {
            f = 0.0f;
        }
        if (this.onGround || this.health <= 0) {
            f2 = 0.0f;
        }
        this.o += (f - this.o) * 0.4f;
        this.aj += (f2 - this.aj) * 0.8f;
        if (this.health > 0) {
            final List list = this.world.b((Entity)this, this.boundingBox.b(1.0, 0.0, 1.0));
            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    final Entity entity = list.get(i);
                    if (!entity.dead) {
                        this.i(entity);
                    }
                }
            }
        }
    }
    
    private void i(final Entity entity) {
        entity.b(this);
    }
    
    public void die(final Entity entity) {
        super.die(entity);
        this.b(0.2f, 0.2f);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.motY = 0.10000000149011612;
        if (this.name.equals("Notch")) {
            this.a(new ItemStack(Item.APPLE, 1), true);
        }
        this.inventory.h();
        if (entity != null) {
            this.motX = -MathHelper.cos((this.af + this.yaw) * 3.1415927f / 180.0f) * 0.1f;
            this.motZ = -MathHelper.sin((this.af + this.yaw) * 3.1415927f / 180.0f) * 0.1f;
        }
        else {
            final double n = 0.0;
            this.motZ = n;
            this.motX = n;
        }
        this.height = 0.1f;
        this.a(StatisticList.y, 1);
    }
    
    public void c(final Entity entity, final int i) {
        this.m += i;
        if (entity instanceof EntityHuman) {
            this.a(StatisticList.A, 1);
        }
        else {
            this.a(StatisticList.z, 1);
        }
    }
    
    public void F() {
        this.a(this.inventory.splitStack(this.inventory.itemInHandIndex, 1), false);
    }
    
    public void b(final ItemStack itemstack) {
        this.a(itemstack, false);
    }
    
    public void a(final ItemStack itemstack, final boolean flag) {
        if (itemstack != null) {
            final EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY - 0.30000001192092896 + this.t(), this.locZ, itemstack);
            entityitem.pickupDelay = 40;
            float f = 0.1f;
            if (flag) {
                final float f2 = this.random.nextFloat() * 0.5f;
                final float f3 = this.random.nextFloat() * 3.1415927f * 2.0f;
                entityitem.motX = -MathHelper.sin(f3) * f2;
                entityitem.motZ = MathHelper.cos(f3) * f2;
                entityitem.motY = 0.20000000298023224;
            }
            else {
                f = 0.3f;
                entityitem.motX = -MathHelper.sin(this.yaw / 180.0f * 3.1415927f) * MathHelper.cos(this.pitch / 180.0f * 3.1415927f) * f;
                entityitem.motZ = MathHelper.cos(this.yaw / 180.0f * 3.1415927f) * MathHelper.cos(this.pitch / 180.0f * 3.1415927f) * f;
                entityitem.motY = -MathHelper.sin(this.pitch / 180.0f * 3.1415927f) * f + 0.1f;
                f = 0.02f;
                final float f2 = this.random.nextFloat() * 3.1415927f * 2.0f;
                f *= this.random.nextFloat();
                final EntityItem entityItem = entityitem;
                entityItem.motX += Math.cos(f2) * f;
                final EntityItem entityItem2 = entityitem;
                entityItem2.motY += (this.random.nextFloat() - this.random.nextFloat()) * 0.1f;
                final EntityItem entityItem3 = entityitem;
                entityItem3.motZ += Math.sin(f2) * f;
            }
            final Player player = (Player)this.getBukkitEntity();
            final CraftItem drop = new CraftItem(this.world.getServer(), entityitem);
            final PlayerDropItemEvent event = new PlayerDropItemEvent(player, (org.bukkit.entity.Item)drop);
            this.world.getServer().getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                final org.bukkit.inventory.ItemStack stack = drop.getItemStack();
                stack.setAmount(1);
                player.getInventory().addItem(new org.bukkit.inventory.ItemStack[] { stack });
                return;
            }
            this.a(entityitem);
            this.a(StatisticList.v, 1);
        }
    }
    
    protected void a(final EntityItem entityitem) {
        this.world.addEntity((Entity)entityitem);
    }
    
    public float a(final Block block) {
        float f = this.inventory.a(block);
        if (this.a(Material.WATER)) {
            f /= 5.0f;
        }
        if (!this.onGround) {
            f /= 5.0f;
        }
        return f;
    }
    
    public float getCurrentPlayerStrVsBlock(final Block block, final int md) {
        float f = 1.0f;
        final ItemStack ist = this.inventory.getItemInHand();
        if (ist != null) {
            f = ist.getItem().getStrVsBlock(ist, block, md);
        }
        if (this.a(Material.WATER)) {
            f /= 5.0f;
        }
        if (!this.onGround) {
            f /= 5.0f;
        }
        return f;
    }
    
    public boolean b(final Block block) {
        return this.inventory.b(block);
    }
    
    public void a(final NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        final NBTTagList nbttaglist = nbttagcompound.l("Inventory");
        this.inventory.b(nbttaglist);
        this.dimension = nbttagcompound.e("Dimension");
        this.sleeping = nbttagcompound.m("Sleeping");
        this.sleepTicks = nbttagcompound.d("SleepTimer");
        if (this.sleeping) {
            this.A = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
            this.a(true, true, false);
        }
        this.spawnWorld = nbttagcompound.getString("SpawnWorld");
        if (this.spawnWorld == "") {
            this.spawnWorld = this.world.getServer().getWorlds().get(0).getName();
        }
        if (nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ")) {
            this.b = new ChunkCoordinates(nbttagcompound.e("SpawnX"), nbttagcompound.e("SpawnY"), nbttagcompound.e("SpawnZ"));
        }
    }
    
    public void b(final NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("Inventory", (NBTBase)this.inventory.a(new NBTTagList()));
        nbttagcompound.a("Dimension", this.dimension);
        nbttagcompound.a("Sleeping", this.sleeping);
        nbttagcompound.a("SleepTimer", (short)this.sleepTicks);
        if (this.b != null) {
            nbttagcompound.a("SpawnX", this.b.x);
            nbttagcompound.a("SpawnY", this.b.y);
            nbttagcompound.a("SpawnZ", this.b.z);
            nbttagcompound.setString("SpawnWorld", this.spawnWorld);
        }
    }
    
    public void a(final IInventory iinventory) {
    }
    
    public void b(final int i, final int j, final int k) {
    }
    
    public void receive(final Entity entity, final int i) {
    }
    
    public float t() {
        return 0.12f;
    }
    
    protected void s() {
        this.height = 1.62f;
    }
    
    public boolean damageEntity(final Entity entity, int i) {
        this.ay = 0;
        if (this.health <= 0) {
            return false;
        }
        if (this.isSleeping() && !this.world.isStatic) {
            this.a(true, true, false);
        }
        if (entity instanceof EntityMonster || entity instanceof EntityArrow) {
            if (this.world.spawnMonsters == 0) {
                i = 0;
            }
            if (this.world.spawnMonsters == 1) {
                i = i / 3 + 1;
            }
            if (this.world.spawnMonsters == 3) {
                i = i * 3 / 2;
            }
        }
        if (i == 0) {
            return false;
        }
        Object object = entity;
        if (entity instanceof EntityArrow && ((EntityArrow)entity).shooter != null) {
            object = ((EntityArrow)entity).shooter;
        }
        if (object instanceof EntityLiving) {
            if (!(entity.getBukkitEntity() instanceof Projectile)) {
                final org.bukkit.entity.Entity damager = ((Entity)object).getBukkitEntity();
                final org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                final EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, EntityDamageEvent.DamageCause.ENTITY_ATTACK, i);
                this.world.getServer().getPluginManager().callEvent((Event)event);
                if (event.isCancelled() || event.getDamage() == 0) {
                    return false;
                }
                i = event.getDamage();
            }
            this.a((EntityLiving)object, false);
        }
        this.a(StatisticList.x, i);
        return super.damageEntity(entity, i);
    }
    
    protected boolean j_() {
        return false;
    }
    
    protected void a(final EntityLiving entityliving, final boolean flag) {
        if (!(entityliving instanceof EntityCreeper) && !(entityliving instanceof EntityGhast)) {
            if (entityliving instanceof EntityWolf) {
                final EntityWolf entitywolf = (EntityWolf)entityliving;
                if (entitywolf.isTamed() && this.name.equals(entitywolf.getOwnerName())) {
                    return;
                }
            }
            if (!(entityliving instanceof EntityHuman) || this.j_()) {
                final List list = this.world.a((Class)EntityWolf.class, AxisAlignedBB.b(this.locX, this.locY, this.locZ, this.locX + 1.0, this.locY + 1.0, this.locZ + 1.0).b(16.0, 4.0, 16.0));
                for (final Entity entity : list) {
                    final EntityWolf entitywolf2 = (EntityWolf)entity;
                    if (entitywolf2.isTamed() && entitywolf2.F() == null && this.name.equals(entitywolf2.getOwnerName()) && (!flag || !entitywolf2.isSitting())) {
                        final org.bukkit.entity.Entity bukkitTarget = (entity == null) ? null : entityliving.getBukkitEntity();
                        EntityTargetEvent event;
                        if (flag) {
                            event = new EntityTargetEvent(entitywolf2.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET);
                        }
                        else {
                            event = new EntityTargetEvent(entitywolf2.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER);
                        }
                        this.world.getServer().getPluginManager().callEvent((Event)event);
                        if (event.isCancelled()) {
                            continue;
                        }
                        entitywolf2.setSitting(false);
                        entitywolf2.setTarget((Entity)entityliving);
                    }
                }
            }
        }
    }
    
    protected void c(int i) {
        boolean doRegularComputation = true;
        final int initialDamage = i;
        for (final ItemStack stack : this.inventory.armor) {
            if (stack != null && stack.getItem() instanceof ISpecialArmor) {
                final ISpecialArmor armor = (ISpecialArmor)stack.getItem();
                final ArmorProperties props = armor.getProperties(this, initialDamage, i);
                i -= props.damageRemove;
                doRegularComputation = (doRegularComputation && props.allowRegularComputation);
            }
        }
        if (!doRegularComputation) {
            super.c(i);
            return;
        }
        final int j = 25 - this.inventory.g();
        final int k = i * j + this.d;
        this.inventory.c(i);
        i = k / 25;
        this.d = k % 25;
        super.c(i);
    }
    
    public void a(final TileEntityFurnace tileentityfurnace) {
    }
    
    public void a(final TileEntityDispenser tileentitydispenser) {
    }
    
    public void a(final TileEntitySign tileentitysign) {
    }
    
    public void c(final Entity entity) {
        if (!entity.a(this)) {
            final ItemStack itemstack = this.G();
            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving)entity);
                if (itemstack.count == 0) {
                    itemstack.a(this);
                    this.H();
                }
            }
        }
    }
    
    public ItemStack G() {
        return this.inventory.getItemInHand();
    }
    
    public void H() {
        final ItemStack orig = this.inventory.getItemInHand();
        this.inventory.setItem(this.inventory.itemInHandIndex, (ItemStack)null);
        ForgeHooks.onDestroyCurrentItem(this, orig);
    }
    
    public double I() {
        return this.height - 0.5f;
    }
    
    public void w() {
        this.q = -1;
        this.p = true;
    }
    
    public void d(final Entity entity) {
        int i = this.inventory.a(entity);
        if (i > 0) {
            if (this.motY < 0.0) {
                ++i;
            }
            if (entity instanceof EntityLiving && !(entity instanceof EntityHuman)) {
                final org.bukkit.entity.Entity damager = this.getBukkitEntity();
                final org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                final EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, EntityDamageEvent.DamageCause.ENTITY_ATTACK, i);
                this.world.getServer().getPluginManager().callEvent((Event)event);
                if (event.isCancelled() || event.getDamage() == 0) {
                    return;
                }
                i = event.getDamage();
            }
            if (!entity.damageEntity((Entity)this, i)) {
                return;
            }
            final ItemStack itemstack = this.G();
            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving)entity, this);
                if (itemstack.count == 0) {
                    itemstack.a(this);
                    this.H();
                }
            }
            if (entity instanceof EntityLiving) {
                if (entity.T()) {
                    this.a((EntityLiving)entity, true);
                }
                this.a(StatisticList.w, i);
            }
        }
    }
    
    public void a(final ItemStack itemstack) {
    }
    
    public void die() {
        super.die();
        this.defaultContainer.a(this);
        if (this.activeContainer != null) {
            this.activeContainer.a(this);
        }
    }
    
    public boolean K() {
        return !this.sleeping && super.K();
    }
    
    public EnumBedError a(final int i, final int j, final int k) {
        final EnumBedError customSleep = ForgeHooks.sleepInBedAt(this, i, j, k);
        if (customSleep != null) {
            return customSleep;
        }
        if (!this.world.isStatic) {
            if (this.isSleeping() || !this.T()) {
                return EnumBedError.OTHER_PROBLEM;
            }
            if (this.world.worldProvider.c) {
                return EnumBedError.NOT_POSSIBLE_HERE;
            }
            if (this.world.d()) {
                return EnumBedError.NOT_POSSIBLE_NOW;
            }
            if (Math.abs(this.locX - i) > 3.0 || Math.abs(this.locY - j) > 2.0 || Math.abs(this.locZ - k) > 3.0) {
                return EnumBedError.TOO_FAR_AWAY;
            }
        }
        if (this.getBukkitEntity() instanceof Player) {
            final Player player = (Player)this.getBukkitEntity();
            final org.bukkit.block.Block bed = this.world.getWorld().getBlockAt(i, j, k);
            final PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, bed);
            this.world.getServer().getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                return EnumBedError.OTHER_PROBLEM;
            }
        }
        this.b(0.2f, 0.2f);
        this.height = 0.2f;
        if (this.world.isLoaded(i, j, k)) {
            final int l = this.world.getData(i, j, k);
            final int i2 = BlockBed.c(l);
            float f = 0.5f;
            float f2 = 0.5f;
            switch (i2) {
                case 0: {
                    f2 = 0.9f;
                    break;
                }
                case 1: {
                    f = 0.1f;
                    break;
                }
                case 2: {
                    f2 = 0.1f;
                    break;
                }
                case 3: {
                    f = 0.9f;
                    break;
                }
            }
            this.e(i2);
            this.setPosition((double)(i + f), (double)(j + 0.9375f), (double)(k + f2));
        }
        else {
            this.setPosition((double)(i + 0.5f), (double)(j + 0.9375f), (double)(k + 0.5f));
        }
        this.sleeping = true;
        this.sleepTicks = 0;
        this.A = new ChunkCoordinates(i, j, k);
        final double motX = 0.0;
        this.motY = motX;
        this.motZ = motX;
        this.motX = motX;
        if (!this.world.isStatic) {
            this.world.everyoneSleeping();
        }
        return EnumBedError.OK;
    }
    
    private void e(final int i) {
        this.B = 0.0f;
        this.C = 0.0f;
        switch (i) {
            case 0: {
                this.C = -1.8f;
                break;
            }
            case 1: {
                this.B = 1.8f;
                break;
            }
            case 2: {
                this.C = 1.8f;
                break;
            }
            case 3: {
                this.B = -1.8f;
                break;
            }
        }
    }
    
    public void a(final boolean flag, final boolean flag1, final boolean flag2) {
        this.b(0.6f, 1.8f);
        this.s();
        final ChunkCoordinates chunkcoordinates = this.A;
        ChunkCoordinates chunkcoordinates2 = this.A;
        if (chunkcoordinates != null && this.world.getTypeId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) == Block.BED.id) {
            BlockBed.a(this.world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, false);
            chunkcoordinates2 = BlockBed.f(this.world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);
            if (chunkcoordinates2 == null) {
                chunkcoordinates2 = new ChunkCoordinates(chunkcoordinates.x, chunkcoordinates.y + 1, chunkcoordinates.z);
            }
            this.setPosition((double)(chunkcoordinates2.x + 0.5f), (double)(chunkcoordinates2.y + this.height + 0.1f), (double)(chunkcoordinates2.z + 0.5f));
        }
        this.sleeping = false;
        if (!this.world.isStatic && flag1) {
            this.world.everyoneSleeping();
        }
        if (this.getBukkitEntity() instanceof Player) {
            final Player player = (Player)this.getBukkitEntity();
            org.bukkit.block.Block bed;
            if (chunkcoordinates != null) {
                bed = this.world.getWorld().getBlockAt(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z);
            }
            else {
                bed = this.world.getWorld().getBlockAt(player.getLocation());
            }
            final PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, bed);
            this.world.getServer().getPluginManager().callEvent((Event)event);
        }
        if (flag) {
            this.sleepTicks = 0;
        }
        else {
            this.sleepTicks = 100;
        }
        if (flag2) {
            this.a(this.A);
        }
    }
    
    private boolean o() {
        return this.world.getTypeId(this.A.x, this.A.y, this.A.z) == Block.BED.id;
    }
    
    public static ChunkCoordinates getBed(final World world, final ChunkCoordinates chunkcoordinates) {
        final IChunkProvider ichunkprovider = world.o();
        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z + 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z + 3 >> 4);
        if (world.getTypeId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) != Block.BED.id) {
            return null;
        }
        final ChunkCoordinates chunkcoordinates2 = BlockBed.f(world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);
        return chunkcoordinates2;
    }
    
    public boolean isSleeping() {
        return this.sleeping;
    }
    
    public boolean isDeeplySleeping() {
        return this.sleeping && this.sleepTicks >= 100;
    }
    
    public void a(final String s) {
    }
    
    public ChunkCoordinates getBed() {
        return this.b;
    }
    
    public void a(final ChunkCoordinates chunkcoordinates) {
        if (chunkcoordinates != null) {
            this.b = new ChunkCoordinates(chunkcoordinates);
            this.spawnWorld = this.world.worldData.name;
        }
        else {
            this.b = null;
        }
    }
    
    public void a(final Statistic statistic) {
        this.a(statistic, 1);
    }
    
    public void a(final Statistic statistic, final int i) {
    }
    
    protected void O() {
        super.O();
        this.a(StatisticList.u, 1);
    }
    
    public void a(final float f, final float f1) {
        final double d0 = this.locX;
        final double d2 = this.locY;
        final double d3 = this.locZ;
        super.a(f, f1);
        this.h(this.locX - d0, this.locY - d2, this.locZ - d3);
    }
    
    private void h(final double d0, final double d1, final double d2) {
        if (this.vehicle == null) {
            if (this.a(Material.WATER)) {
                final int i = Math.round(MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0f);
                if (i > 0) {
                    this.a(StatisticList.q, i);
                }
            }
            else if (this.ad()) {
                final int i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0f);
                if (i > 0) {
                    this.a(StatisticList.m, i);
                }
            }
            else if (this.p()) {
                if (d1 > 0.0) {
                    this.a(StatisticList.o, (int)Math.round(d1 * 100.0));
                }
            }
            else if (this.onGround) {
                final int i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0f);
                if (i > 0) {
                    this.a(StatisticList.l, i);
                }
            }
            else {
                final int i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0f);
                if (i > 25) {
                    this.a(StatisticList.p, i);
                }
            }
        }
    }
    
    private void i(final double d0, final double d1, final double d2) {
        if (this.vehicle != null) {
            final int i = Math.round(MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0f);
            if (i > 0) {
                if (this.vehicle instanceof EntityMinecart) {
                    this.a(StatisticList.r, i);
                    if (this.c == null) {
                        this.c = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
                    }
                    else if (this.c.a(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) >= 1000.0) {
                        this.a((Statistic)AchievementList.q, 1);
                    }
                }
                else if (this.vehicle instanceof EntityBoat) {
                    this.a(StatisticList.s, i);
                }
                else if (this.vehicle instanceof EntityPig) {
                    this.a(StatisticList.t, i);
                }
            }
        }
    }
    
    protected void a(final float f) {
        if (f >= 2.0f) {
            this.a(StatisticList.n, (int)Math.round(f * 100.0));
        }
        super.a(f);
    }
    
    public void a(final EntityLiving entityliving) {
        if (entityliving instanceof EntityMonster) {
            this.a((Statistic)AchievementList.s);
        }
    }
    
    public void P() {
        if (this.D > 0) {
            this.D = 10;
        }
        else {
            this.E = true;
        }
    }
}
