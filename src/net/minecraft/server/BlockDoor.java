// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Random;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockDoor extends Block
{
    protected BlockDoor(final int i, final Material material) {
        super(i, material);
        this.textureId = 97;
        if (material == Material.ORE) {
            ++this.textureId;
        }
        final float f = 0.5f;
        final float f2 = 1.0f;
        this.a(0.5f - f, 0.0f, 0.5f - f, 0.5f + f, f2, 0.5f + f);
    }
    
    public int a(final int i, final int j) {
        if (i == 0 || i == 1) {
            return this.textureId;
        }
        final int k = this.d(j);
        if ((k == 0 || k == 2) ^ i <= 3) {
            return this.textureId;
        }
        int l = k / 2 + ((i & 0x1) ^ k);
        l += (j & 0x4) / 4;
        int i2 = this.textureId - (j & 0x8) * 2;
        if ((l & 0x1) != 0x0) {
            i2 = -i2;
        }
        return i2;
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
        this.c(this.d(iblockaccess.getData(i, j, k)));
    }
    
    public void c(final int i) {
        final float f = 0.1875f;
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 2.0f, 1.0f);
        if (i == 0) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
        }
        if (i == 1) {
            this.a(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        if (i == 2) {
            this.a(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
        }
        if (i == 3) {
            this.a(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
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
        if ((l & 0x8) != 0x0) {
            if (world.getTypeId(i, j - 1, k) == this.id) {
                this.interact(world, i, j - 1, k, entityhuman);
            }
            return true;
        }
        if (world.getTypeId(i, j + 1, k) == this.id) {
            world.setData(i, j + 1, k, (l ^ 0x4) + 8);
        }
        world.setData(i, j, k, l ^ 0x4);
        world.b(i, j - 1, k, i, j, k);
        world.a(entityhuman, 1003, i, j, k, 0);
        return true;
    }
    
    public void setDoor(final World world, final int i, final int j, final int k, final boolean flag) {
        final int l = world.getData(i, j, k);
        if ((l & 0x8) != 0x0) {
            if (world.getTypeId(i, j - 1, k) == this.id) {
                this.setDoor(world, i, j - 1, k, flag);
            }
        }
        else {
            final boolean flag2 = (world.getData(i, j, k) & 0x4) > 0;
            if (flag2 != flag) {
                if (world.getTypeId(i, j + 1, k) == this.id) {
                    world.setData(i, j + 1, k, (l ^ 0x4) + 8);
                }
                world.setData(i, j, k, l ^ 0x4);
                world.b(i, j - 1, k, i, j, k);
                world.a((EntityHuman)null, 1003, i, j, k, 0);
            }
        }
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        final int i2 = world.getData(i, j, k);
        if ((i2 & 0x8) != 0x0) {
            if (world.getTypeId(i, j - 1, k) != this.id) {
                world.setTypeId(i, j, k, 0);
            }
            if (l > 0 && Block.byId[l].isPowerSource()) {
                this.doPhysics(world, i, j - 1, k, l);
            }
        }
        else {
            boolean flag = false;
            if (world.getTypeId(i, j + 1, k) != this.id) {
                world.setTypeId(i, j, k, 0);
                flag = true;
            }
            if (!world.isBlockSolidOnSide(i, j - 1, k, 1)) {
                world.setTypeId(i, j, k, 0);
                flag = true;
                if (world.getTypeId(i, j + 1, k) == this.id) {
                    world.setTypeId(i, j + 1, k, 0);
                }
            }
            if (flag) {
                if (!world.isStatic) {
                    this.g(world, i, j, k, i2);
                }
            }
            else if (l > 0 && Block.byId[l].isPowerSource()) {
                final org.bukkit.World bworld = (org.bukkit.World)world.getWorld();
                final org.bukkit.block.Block block = bworld.getBlockAt(i, j, k);
                final org.bukkit.block.Block blockTop = bworld.getBlockAt(i, j + 1, k);
                int power = block.getBlockPower();
                final int powerTop = blockTop.getBlockPower();
                if (powerTop > power) {
                    power = powerTop;
                }
                final int oldPower = ((world.getData(i, j, k) & 0x4) > 0) ? 15 : 0;
                if (oldPower == 0 ^ power == 0) {
                    final BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, oldPower, power);
                    world.getServer().getPluginManager().callEvent((Event)eventRedstone);
                    this.setDoor(world, i, j, k, eventRedstone.getNewCurrent() > 0);
                }
            }
        }
    }
    
    public int a(final int i, final Random random) {
        return ((i & 0x8) != 0x0) ? 0 : ((this.material == Material.ORE) ? Item.IRON_DOOR.id : Item.WOOD_DOOR.id);
    }
    
    public MovingObjectPosition a(final World world, final int i, final int j, final int k, final Vec3D vec3d, final Vec3D vec3d1) {
        this.a((IBlockAccess)world, i, j, k);
        return super.a(world, i, j, k, vec3d, vec3d1);
    }
    
    public int d(final int i) {
        return ((i & 0x4) == 0x0) ? (i - 1 & 0x3) : (i & 0x3);
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return j < 127 && (world.isBlockSolidOnSide(i, j - 1, k, 1) && super.canPlace(world, i, j, k) && super.canPlace(world, i, j + 1, k));
    }
    
    public static boolean e(final int i) {
        return (i & 0x4) != 0x0;
    }
    
    public int e() {
        return 1;
    }
}
