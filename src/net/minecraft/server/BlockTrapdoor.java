// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockTrapdoor extends Block
{
    public static boolean disableValidation;
    
    protected BlockTrapdoor(final int i, final Material material) {
        super(i, material);
        this.textureId = 84;
        if (material == Material.ORE) {
            ++this.textureId;
        }
        final float f = 0.5f;
        final float f2 = 1.0f;
        this.a(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f2, 0.5f + f);
    }
    
    public boolean a() {
        return false;
    }
    
    public boolean b() {
        return false;
    }
    
    public AxisAlignedBB e(final World world, final int i, final int j, final int k) {
        this.a((IBlockAccess)world, i, j, k);
        return super.e(world, i, j, k);
    }
    
    public void a(final IBlockAccess iblockaccess, final int i, final int j, final int k) {
        this.c(iblockaccess.getData(i, j, k));
    }
    
    public void c(final int i) {
        final float f = 0.1875f;
        this.a(0.0f, 0.0f, 0.0f, 1.0f, f, 1.0f);
        if (d(i)) {
            if ((i & 0x3) == 0x0) {
                this.a(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
            }
            if ((i & 0x3) == 0x1) {
                this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
            }
            if ((i & 0x3) == 0x2) {
                this.a(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            }
            if ((i & 0x3) == 0x3) {
                this.a(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
            }
        }
    }
    
    public void b(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        this.interact(world, i, j, k, entityhuman);
    }
    
    public boolean interact(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        if (this.material == Material.ORE) {
            return true;
        }
        final int l = world.getData(i, j, k);
        world.setData(i, j, k, l ^ 0x4);
        world.a(entityhuman, 1003, i, j, k, 0);
        return true;
    }
    
    public void a(final World world, final int i, final int j, final int k, final boolean flag) {
        final int l = world.getData(i, j, k);
        final boolean flag2 = (l & 0x4) > 0;
        if (flag2 != flag) {
            world.setData(i, j, k, l ^ 0x4);
            world.a((EntityHuman)null, 1003, i, j, k, 0);
        }
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        if (!world.isStatic) {
            final int i2 = world.getData(i, j, k);
            int j2 = i;
            int k2 = k;
            if ((i2 & 0x3) == 0x0) {
                k2 = k + 1;
            }
            if ((i2 & 0x3) == 0x1) {
                --k2;
            }
            if ((i2 & 0x3) == 0x2) {
                j2 = i + 1;
            }
            if ((i2 & 0x3) == 0x3) {
                --j2;
            }
            if (!BlockTrapdoor.disableValidation && !world.isBlockSolidOnSide(j2, j, k2, (i2 & 0x3) + 2)) {
                world.setTypeId(i, j, k, 0);
                this.g(world, i, j, k, i2);
            }
            if (l > 0 && Block.byId[l] != null && Block.byId[l].isPowerSource()) {
                final org.bukkit.World bworld = (org.bukkit.World)world.getWorld();
                final org.bukkit.block.Block block = bworld.getBlockAt(i, j, k);
                final int power = block.getBlockPower();
                final int oldPower = ((world.getData(i, j, k) & 0x4) > 0) ? 15 : 0;
                if (oldPower == 0 ^ power == 0) {
                    final BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, oldPower, power);
                    world.getServer().getPluginManager().callEvent((Event)eventRedstone);
                    this.a(world, i, j, k, eventRedstone.getNewCurrent() > 0);
                }
            }
        }
    }
    
    public MovingObjectPosition a(final World world, final int i, final int j, final int k, final Vec3D vec3d, final Vec3D vec3d1) {
        this.a((IBlockAccess)world, i, j, k);
        return super.a(world, i, j, k, vec3d, vec3d1);
    }
    
    public void postPlace(final World world, final int i, final int j, final int k, final int l) {
        byte b0 = 0;
        if (l == 2) {
            b0 = 0;
        }
        if (l == 3) {
            b0 = 1;
        }
        if (l == 4) {
            b0 = 2;
        }
        if (l == 5) {
            b0 = 3;
        }
        world.setData(i, j, k, (int)b0);
        this.doPhysics(world, i, j, k, Block.REDSTONE_WIRE.id);
    }
    
    public boolean canPlace(final World world, int i, final int j, int k, final int l) {
        if (l == 0) {
            return false;
        }
        if (l == 1) {
            return false;
        }
        if (l == 2) {
            ++k;
        }
        if (l == 3) {
            --k;
        }
        if (l == 4) {
            ++i;
        }
        if (l == 5) {
            --i;
        }
        return world.isBlockSolidOnSide(i, j, k, l);
    }
    
    public static boolean d(final int i) {
        return (i & 0x4) != 0x0;
    }
    
    static {
        BlockTrapdoor.disableValidation = false;
    }
}
