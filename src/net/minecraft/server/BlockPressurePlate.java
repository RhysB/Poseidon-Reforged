// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.event.Cancellable;
import java.util.Iterator;
import org.bukkit.plugin.PluginManager;
import java.util.List;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.Action;
import java.util.Random;

public class BlockPressurePlate extends Block
{
    private EnumMobType a;
    
    protected BlockPressurePlate(final int i, final int j, final EnumMobType enummobtype, final Material material) {
        super(i, j, material);
        this.a = enummobtype;
        this.a(true);
        final float f = 0.0625f;
        this.a(f, 0.0f, f, 1.0f - f, 0.03125f, 1.0f - f);
    }
    
    public int c() {
        return 20;
    }
    
    public AxisAlignedBB e(final World world, final int i, final int j, final int k) {
        return null;
    }
    
    public boolean a() {
        return false;
    }
    
    public boolean b() {
        return false;
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i, j - 1, k, 1);
    }
    
    public void c(final World world, final int i, final int j, final int k) {
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        boolean flag = false;
        if (!world.isBlockSolidOnSide(i, j - 1, k, 1)) {
            flag = true;
        }
        if (flag) {
            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
        }
    }
    
    public void a(final World world, final int i, final int j, final int k, final Random random) {
        if (!world.isStatic && world.getData(i, j, k) != 0) {
            this.g(world, i, j, k);
        }
    }
    
    public void a(final World world, final int i, final int j, final int k, final Entity entity) {
        if (!world.isStatic && world.getData(i, j, k) != 1) {
            this.g(world, i, j, k);
        }
    }
    
    private void g(final World world, final int i, final int j, final int k) {
        final boolean flag = world.getData(i, j, k) == 1;
        boolean flag2 = false;
        final float f = 0.125f;
        List list = null;
        if (this.a == EnumMobType.EVERYTHING) {
            list = world.b((Entity)null, AxisAlignedBB.b((double)(i + f), (double)j, (double)(k + f), (double)(i + 1 - f), j + 0.25, (double)(k + 1 - f)));
        }
        if (this.a == EnumMobType.MOBS) {
            list = world.a((Class)EntityLiving.class, AxisAlignedBB.b((double)(i + f), (double)j, (double)(k + f), (double)(i + 1 - f), j + 0.25, (double)(k + 1 - f)));
        }
        if (this.a == EnumMobType.PLAYERS) {
            list = world.a((Class)EntityHuman.class, AxisAlignedBB.b((double)(i + f), (double)j, (double)(k + f), (double)(i + 1 - f), j + 0.25, (double)(k + 1 - f)));
        }
        if (list.size() > 0) {
            flag2 = true;
        }
        final org.bukkit.World bworld = (org.bukkit.World)world.getWorld();
        final PluginManager manager = world.getServer().getPluginManager();
        if (flag != flag2) {
            if (flag2) {
                for (final Object object : list) {
                    if (object != null) {
                        Cancellable cancellable;
                        if (object instanceof EntityHuman) {
                            cancellable = (Cancellable)CraftEventFactory.callPlayerInteractEvent((EntityHuman)object, Action.PHYSICAL, i, j, k, -1, (ItemStack)null);
                        }
                        else {
                            if (!(object instanceof Entity)) {
                                continue;
                            }
                            cancellable = (Cancellable)new EntityInteractEvent(((Entity)object).getBukkitEntity(), bworld.getBlockAt(i, j, k));
                            manager.callEvent((Event)cancellable);
                        }
                        if (cancellable.isCancelled()) {
                            return;
                        }
                        continue;
                    }
                }
            }
            final BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bworld.getBlockAt(i, j, k), (int)(flag ? 1 : 0), (int)(flag2 ? 1 : 0));
            manager.callEvent((Event)eventRedstone);
            flag2 = (eventRedstone.getNewCurrent() > 0);
        }
        if (flag2 && !flag) {
            world.setData(i, j, k, 1);
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.b(i, j, k, i, j, k);
            world.makeSound(i + 0.5, j + 0.1, k + 0.5, "random.click", 0.3f, 0.6f);
        }
        if (!flag2 && flag) {
            world.setData(i, j, k, 0);
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
            world.b(i, j, k, i, j, k);
            world.makeSound(i + 0.5, j + 0.1, k + 0.5, "random.click", 0.3f, 0.5f);
        }
        if (flag2) {
            world.c(i, j, k, this.id, this.c());
        }
    }
    
    public void remove(final World world, final int i, final int j, final int k) {
        final int l = world.getData(i, j, k);
        if (l > 0) {
            world.applyPhysics(i, j, k, this.id);
            world.applyPhysics(i, j - 1, k, this.id);
        }
        super.remove(world, i, j, k);
    }
    
    public void a(final IBlockAccess iblockaccess, final int i, final int j, final int k) {
        final boolean flag = iblockaccess.getData(i, j, k) == 1;
        final float f = 0.0625f;
        if (flag) {
            this.a(f, 0.0f, f, 1.0f - f, 0.03125f, 1.0f - f);
        }
        else {
            this.a(f, 0.0f, f, 1.0f - f, 0.0625f, 1.0f - f);
        }
    }
    
    public boolean a(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int l) {
        return iblockaccess.getData(i, j, k) > 0;
    }
    
    public boolean d(final World world, final int i, final int j, final int k, final int l) {
        return world.getData(i, j, k) != 0 && l == 1;
    }
    
    public boolean isPowerSource() {
        return true;
    }
    
    public int e() {
        return 1;
    }
}
