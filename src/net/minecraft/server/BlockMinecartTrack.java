// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Random;

public class BlockMinecartTrack extends Block
{
    private final boolean a;
    
    public static final boolean g(final World world, final int i, final int j, final int k) {
        final int l = world.getTypeId(i, j, k);
        return Block.byId[l] instanceof BlockMinecartTrack;
    }
    
    public static final boolean c(final int i) {
        return Block.byId[i] instanceof BlockMinecartTrack;
    }
    
    protected BlockMinecartTrack(final int i, final int j, final boolean flag) {
        super(i, j, Material.ORIENTABLE);
        this.a = flag;
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }
    
    public boolean f() {
        return this.a;
    }
    
    public AxisAlignedBB e(final World world, final int i, final int j, final int k) {
        return null;
    }
    
    public boolean a() {
        return false;
    }
    
    public MovingObjectPosition a(final World world, final int i, final int j, final int k, final Vec3D vec3d, final Vec3D vec3d1) {
        this.a((IBlockAccess)world, i, j, k);
        return super.a(world, i, j, k, vec3d, vec3d1);
    }
    
    public void a(final IBlockAccess iblockaccess, final int i, final int j, final int k) {
        final int l = iblockaccess.getData(i, j, k);
        if (l >= 2 && l <= 5) {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.625f, 1.0f);
        }
        else {
            this.a(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
        }
    }
    
    public int a(final int i, final int j) {
        if (this.a) {
            if (this.id == Block.GOLDEN_RAIL.id && (j & 0x8) == 0x0) {
                return this.textureId - 16;
            }
        }
        else if (j >= 6) {
            return this.textureId - 16;
        }
        return this.textureId;
    }
    
    public boolean b() {
        return false;
    }
    
    public int a(final Random random) {
        return 1;
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        return world.isBlockSolidOnSide(i, j - 1, k, 1);
    }
    
    public void c(final World world, final int i, final int j, final int k) {
        if (!world.isStatic) {
            this.a(world, i, j, k, true);
        }
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
        if (world.isStatic) {
            return;
        }
        int j2;
        final int i2 = j2 = world.getData(i, j, k);
        if (this.a) {
            j2 &= 0x7;
        }
        boolean flag = false;
        if (!world.isBlockSolidOnSide(i, j - 1, k, 1)) {
            flag = true;
        }
        if (j2 == 2 && !world.isBlockSolidOnSide(i + 1, j, k, 1)) {
            flag = true;
        }
        if (j2 == 3 && !world.isBlockSolidOnSide(i - 1, j, k, 1)) {
            flag = true;
        }
        if (j2 == 4 && !world.isBlockSolidOnSide(i, j, k - 1, 1)) {
            flag = true;
        }
        if (j2 == 5 && !world.isBlockSolidOnSide(i, j, k + 1, 1)) {
            flag = true;
        }
        if (flag) {
            this.g(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
        }
        else if (this.id == Block.GOLDEN_RAIL.id) {
            boolean flag2 = world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k);
            flag2 = (flag2 || this.a(world, i, j, k, i2, true, 0) || this.a(world, i, j, k, i2, false, 0));
            boolean flag3 = false;
            if (flag2 && (i2 & 0x8) == 0x0) {
                world.setData(i, j, k, j2 | 0x8);
                flag3 = true;
            }
            else if (!flag2 && (i2 & 0x8) != 0x0) {
                world.setData(i, j, k, j2);
                flag3 = true;
            }
            if (flag3) {
                world.applyPhysics(i, j - 1, k, this.id);
                if (j2 == 2 || j2 == 3 || j2 == 4 || j2 == 5) {
                    world.applyPhysics(i, j + 1, k, this.id);
                }
            }
        }
        else if (l > 0 && Block.byId[l].isPowerSource() && !this.a && MinecartTrackLogic.a(new MinecartTrackLogic(this, world, i, j, k)) == 3) {
            this.a(world, i, j, k, false);
        }
    }
    
    private void a(final World world, final int i, final int j, final int k, final boolean flag) {
        if (world.isStatic) {
            return;
        }
        new MinecartTrackLogic(this, world, i, j, k).a(world.isBlockIndirectlyPowered(i, j, k), flag);
    }
    
    private boolean a(final World world, int i, int j, int k, final int l, final boolean flag, final int i1) {
        if (i1 >= 8) {
            return false;
        }
        int j2 = l & 0x7;
        boolean flag2 = true;
        switch (j2) {
            case 0: {
                if (flag) {
                    ++k;
                    break;
                }
                --k;
                break;
            }
            case 1: {
                if (flag) {
                    --i;
                    break;
                }
                ++i;
                break;
            }
            case 2: {
                if (flag) {
                    --i;
                }
                else {
                    ++i;
                    ++j;
                    flag2 = false;
                }
                j2 = 1;
                break;
            }
            case 3: {
                if (flag) {
                    --i;
                    ++j;
                    flag2 = false;
                }
                else {
                    ++i;
                }
                j2 = 1;
                break;
            }
            case 4: {
                if (flag) {
                    ++k;
                }
                else {
                    --k;
                    ++j;
                    flag2 = false;
                }
                j2 = 0;
                break;
            }
            case 5: {
                if (flag) {
                    ++k;
                    ++j;
                    flag2 = false;
                }
                else {
                    --k;
                }
                j2 = 0;
                break;
            }
        }
        return this.a(world, i, j, k, flag, i1, j2) || (flag2 && this.a(world, i, j - 1, k, flag, i1, j2));
    }
    
    private boolean a(final World world, final int i, final int j, final int k, final boolean flag, final int l, final int i1) {
        final int j2 = world.getTypeId(i, j, k);
        if (j2 == Block.GOLDEN_RAIL.id) {
            final int k2 = world.getData(i, j, k);
            final int l2 = k2 & 0x7;
            if (i1 == 1 && (l2 == 0 || l2 == 4 || l2 == 5)) {
                return false;
            }
            if (i1 == 0 && (l2 == 1 || l2 == 2 || l2 == 3)) {
                return false;
            }
            if ((k2 & 0x8) != 0x0) {
                return world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k) || this.a(world, i, j, k, k2, flag, l + 1);
            }
        }
        return false;
    }
    
    public int e() {
        return 0;
    }
    
    static boolean a(final BlockMinecartTrack blockminecarttrack) {
        return blockminecarttrack.a;
    }
}
