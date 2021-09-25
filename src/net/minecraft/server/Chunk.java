// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.Random;
import java.util.Iterator;
import org.bukkit.Location;
import java.util.Collection;
import net.minecraft.server.forge.IOverrideReplace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.CraftChunk;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chunk
{
    public static boolean a;
    public byte[] b;
    public boolean c;
    public World world;
    public NibbleArray e;
    public NibbleArray f;
    public NibbleArray g;
    public byte[] heightMap;
    public int i;
    public final int x;
    public final int z;
    public Map tileEntities;
    public List[] entitySlices;
    public boolean done;
    public boolean o;
    public boolean p;
    public boolean q;
    public long r;
    public org.bukkit.Chunk bukkitChunk;
    
    public Chunk(final World world, final int i, final int j) {
        this.tileEntities = new HashMap();
        this.entitySlices = new List[8];
        this.done = false;
        this.o = false;
        this.q = false;
        this.r = 0L;
        this.world = world;
        this.x = i;
        this.z = j;
        this.heightMap = new byte[256];
        for (int k = 0; k < this.entitySlices.length; ++k) {
            this.entitySlices[k] = new ArrayList();
        }
        final CraftWorld cworld = this.world.getWorld();
        this.bukkitChunk = ((cworld == null) ? null : cworld.popPreservedChunk(i, j));
        if (this.bukkitChunk == null) {
            this.bukkitChunk = (org.bukkit.Chunk)new CraftChunk(this);
        }
    }
    
    public Chunk(final World world, final byte[] abyte, final int i, final int j) {
        this(world, i, j);
        this.b = abyte;
        this.e = new NibbleArray(abyte.length);
        this.f = new NibbleArray(abyte.length);
        this.g = new NibbleArray(abyte.length);
    }
    
    public boolean a(final int i, final int j) {
        return i == this.x && j == this.z;
    }
    
    public int b(final int i, final int j) {
        return this.heightMap[j << 4 | i] & 0xFF;
    }
    
    public void a() {
    }
    
    public void initLighting() {
        int i = 127;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                int l;
                int i2;
                for (l = 127, i2 = (j << 11 | k << 7); l > 0 && Block.q[this.b[i2 + l - 1] & 0xFF] == 0; --l) {}
                this.heightMap[k << 4 | j] = (byte)l;
                if (l < i) {
                    i = l;
                }
                if (!this.world.worldProvider.e) {
                    int j2 = 15;
                    int k2 = 127;
                    do {
                        j2 -= Block.q[this.b[i2 + k2] & 0xFF];
                        if (j2 > 0) {
                            this.f.a(j, k2, k, j2);
                        }
                    } while (--k2 > 0 && j2 > 0);
                }
            }
        }
        this.i = i;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                this.c(j, k);
            }
        }
        this.o = true;
    }
    
    public void loadNOP() {
    }
    
    private void c(final int i, final int j) {
        final int k = this.b(i, j);
        final int l = this.x * 16 + i;
        final int i2 = this.z * 16 + j;
        this.f(l - 1, i2, k);
        this.f(l + 1, i2, k);
        this.f(l, i2 - 1, k);
        this.f(l, i2 + 1, k);
    }
    
    private void f(final int i, final int j, final int k) {
        final int l = this.world.getHighestBlockYAt(i, j);
        if (l > k) {
            this.world.a(EnumSkyBlock.SKY, i, k, j, i, l, j);
            this.o = true;
        }
        else if (l < k) {
            this.world.a(EnumSkyBlock.SKY, i, l, j, i, k, j);
            this.o = true;
        }
    }
    
    private void g(final int i, final int j, final int k) {
        int i2;
        final int l = i2 = (this.heightMap[k << 4 | i] & 0xFF);
        if (j > l) {
            i2 = j;
        }
        for (int j2 = i << 11 | k << 7; i2 > 0 && Block.q[this.b[j2 + i2 - 1] & 0xFF] == 0; --i2) {}
        if (i2 != l) {
            this.world.g(i, k, i2, l);
            this.heightMap[k << 4 | i] = (byte)i2;
            if (i2 < this.i) {
                this.i = i2;
            }
            else {
                int k2 = 127;
                for (int l2 = 0; l2 < 16; ++l2) {
                    for (int i3 = 0; i3 < 16; ++i3) {
                        if ((this.heightMap[i3 << 4 | l2] & 0xFF) < k2) {
                            k2 = (this.heightMap[i3 << 4 | l2] & 0xFF);
                        }
                    }
                }
                this.i = k2;
            }
            int k2 = this.x * 16 + i;
            int l2 = this.z * 16 + k;
            if (i2 < l) {
                for (int i3 = i2; i3 < l; ++i3) {
                    this.f.a(i, i3, k, 15);
                }
            }
            else {
                this.world.a(EnumSkyBlock.SKY, k2, l, l2, k2, i2, l2);
                for (int i3 = l; i3 < i2; ++i3) {
                    this.f.a(i, i3, k, 0);
                }
            }
            int i3 = 15;
            final int j3 = i2;
            while (i2 > 0 && i3 > 0) {
                --i2;
                int k3 = Block.q[this.getTypeId(i, i2, k)];
                if (k3 == 0) {
                    k3 = 1;
                }
                i3 -= k3;
                if (i3 < 0) {
                    i3 = 0;
                }
                this.f.a(i, i2, k, i3);
            }
            while (i2 > 0 && Block.q[this.getTypeId(i, i2 - 1, k)] == 0) {
                --i2;
            }
            if (i2 != j3) {
                this.world.a(EnumSkyBlock.SKY, k2 - 1, i2, l2 - 1, k2 + 1, j3, l2 + 1);
            }
            this.o = true;
        }
    }
    
    public int getTypeId(final int i, final int j, final int k) {
        return this.b[i << 11 | k << 7 | j] & 0xFF;
    }
    
    public boolean a(final int i, final int j, final int k, final int l, final int i1) {
        final byte b0 = (byte)l;
        final int j2 = this.heightMap[k << 4 | i] & 0xFF;
        final int k2 = this.b[i << 11 | k << 7 | j] & 0xFF;
        if (k2 == l && this.e.a(i, j, k) == i1) {
            return false;
        }
        final int l2 = this.x * 16 + i;
        final int i2 = this.z * 16 + k;
        if (Block.byId[k2] instanceof IOverrideReplace) {
            final IOverrideReplace iovr = (IOverrideReplace)Block.byId[k2];
            if (!iovr.canReplaceBlock(this.world, l2, j, i2, l)) {
                return iovr.getReplacedSuccess();
            }
        }
        this.b[i << 11 | k << 7 | j] = (byte)(b0 & 0xFF);
        if (k2 != 0 && !this.world.isStatic) {
            Block.byId[k2].remove(this.world, l2, j, i2);
        }
        this.e.a(i, j, k, i1);
        if (!this.world.worldProvider.e) {
            if (Block.q[b0 & 0xFF] != 0) {
                if (j >= j2) {
                    this.g(i, j + 1, k);
                }
            }
            else if (j == j2 - 1) {
                this.g(i, j, k);
            }
            this.world.a(EnumSkyBlock.SKY, l2, j, i2, l2, j, i2);
        }
        this.world.a(EnumSkyBlock.BLOCK, l2, j, i2, l2, j, i2);
        this.c(i, k);
        this.e.a(i, j, k, i1);
        if (l != 0) {
            Block.byId[l].c(this.world, l2, j, i2);
        }
        return this.o = true;
    }
    
    public boolean a(final int i, final int j, final int k, final int l) {
        final byte b0 = (byte)l;
        final int i2 = this.heightMap[k << 4 | i] & 0xFF;
        final int j2 = this.b[i << 11 | k << 7 | j] & 0xFF;
        if (j2 == l) {
            return false;
        }
        final int k2 = this.x * 16 + i;
        final int l2 = this.z * 16 + k;
        if (Block.byId[j2] instanceof IOverrideReplace) {
            final IOverrideReplace iovr = (IOverrideReplace)Block.byId[j2];
            if (!iovr.canReplaceBlock(this.world, k2, j, l2, l)) {
                return iovr.getReplacedSuccess();
            }
        }
        this.b[i << 11 | k << 7 | j] = (byte)(b0 & 0xFF);
        if (j2 != 0) {
            Block.byId[j2].remove(this.world, k2, j, l2);
        }
        this.e.a(i, j, k, 0);
        if (Block.q[b0 & 0xFF] != 0) {
            if (j >= i2) {
                this.g(i, j + 1, k);
            }
        }
        else if (j == i2 - 1) {
            this.g(i, j, k);
        }
        this.world.a(EnumSkyBlock.SKY, k2, j, l2, k2, j, l2);
        this.world.a(EnumSkyBlock.BLOCK, k2, j, l2, k2, j, l2);
        this.c(i, k);
        if (l != 0 && !this.world.isStatic) {
            Block.byId[l].c(this.world, k2, j, l2);
        }
        return this.o = true;
    }
    
    public int getData(final int i, final int j, final int k) {
        return this.e.a(i, j, k);
    }
    
    public void b(final int i, final int j, final int k, final int l) {
        this.o = true;
        this.e.a(i, j, k, l);
    }
    
    public int a(final EnumSkyBlock enumskyblock, final int i, final int j, final int k) {
        return (enumskyblock == EnumSkyBlock.SKY) ? this.f.a(i, j, k) : ((enumskyblock == EnumSkyBlock.BLOCK) ? this.g.a(i, j, k) : 0);
    }
    
    public void a(final EnumSkyBlock enumskyblock, final int i, final int j, final int k, final int l) {
        this.o = true;
        if (enumskyblock == EnumSkyBlock.SKY) {
            this.f.a(i, j, k, l);
        }
        else {
            if (enumskyblock != EnumSkyBlock.BLOCK) {
                return;
            }
            this.g.a(i, j, k, l);
        }
    }
    
    public int c(final int i, final int j, final int k, final int l) {
        int i2 = this.f.a(i, j, k);
        if (i2 > 0) {
            Chunk.a = true;
        }
        i2 -= l;
        final int j2 = this.g.a(i, j, k);
        if (j2 > i2) {
            i2 = j2;
        }
        return i2;
    }
    
    public void a(final Entity entity) {
        this.q = true;
        final int i = MathHelper.floor(entity.locX / 16.0);
        final int j = MathHelper.floor(entity.locZ / 16.0);
        if (i != this.x || j != this.z) {
            System.out.println("Wrong location! " + entity);
            System.out.println("" + entity.locX + "," + entity.locZ + "(" + i + "," + j + ") vs " + this.x + "," + this.z);
        }
        int k = MathHelper.floor(entity.locY / 16.0);
        if (k < 0) {
            k = 0;
        }
        if (k >= this.entitySlices.length) {
            k = this.entitySlices.length - 1;
        }
        entity.bG = true;
        entity.bH = this.x;
        entity.bI = k;
        entity.bJ = this.z;
        this.entitySlices[k].add(entity);
    }
    
    public void b(final Entity entity) {
        this.a(entity, entity.bI);
    }
    
    public void a(final Entity entity, int i) {
        if (i < 0) {
            i = 0;
        }
        if (i >= this.entitySlices.length) {
            i = this.entitySlices.length - 1;
        }
        this.entitySlices[i].remove(entity);
    }
    
    public boolean c(final int i, final int j, final int k) {
        return j >= (this.heightMap[k << 4 | i] & 0xFF);
    }
    
    public TileEntity d(final int i, final int j, final int k) {
        final ChunkPosition chunkposition = new ChunkPosition(i, j, k);
        TileEntity tileentity = this.tileEntities.get(chunkposition);
        if (tileentity == null) {
            final int l = this.getTypeId(i, j, k);
            if (!Block.isTileEntity[l]) {
                return null;
            }
            final BlockContainer blockcontainer = (BlockContainer)Block.byId[l];
            blockcontainer.c(this.world, this.x * 16 + i, j, this.z * 16 + k);
            tileentity = this.tileEntities.get(chunkposition);
        }
        if (tileentity != null && tileentity.g()) {
            this.tileEntities.remove(chunkposition);
            return null;
        }
        return tileentity;
    }
    
    public void a(final TileEntity tileentity) {
        final int i = tileentity.x - this.x * 16;
        final int j = tileentity.y;
        final int k = tileentity.z - this.z * 16;
        this.a(i, j, k, tileentity);
        if (this.c) {
            this.world.c.add(tileentity);
        }
    }
    
    public void a(final int i, final int j, final int k, final TileEntity tileentity) {
        final ChunkPosition chunkposition = new ChunkPosition(i, j, k);
        tileentity.world = this.world;
        tileentity.x = this.x * 16 + i;
        tileentity.y = j;
        tileentity.z = this.z * 16 + k;
        if (this.getTypeId(i, j, k) != 0 && Block.byId[this.getTypeId(i, j, k)] instanceof BlockContainer) {
            tileentity.j();
            this.tileEntities.put(chunkposition, tileentity);
        }
        else {
            System.out.println("Attempted to place a tile entity where there was no entity tile!");
        }
    }
    
    public void e(final int i, final int j, final int k) {
        final ChunkPosition chunkposition = new ChunkPosition(i, j, k);
        if (this.c) {
            final TileEntity tileentity = this.tileEntities.remove(chunkposition);
            if (tileentity != null) {
                tileentity.h();
            }
        }
    }
    
    public void addEntities() {
        this.c = true;
        this.world.a((Collection)this.tileEntities.values());
        for (int i = 0; i < this.entitySlices.length; ++i) {
            this.world.a(this.entitySlices[i]);
        }
    }
    
    public void removeEntities() {
        this.c = false;
        for (final TileEntity tileentity : this.tileEntities.values()) {
            this.world.markForRemoval(tileentity);
        }
        for (int i = 0; i < this.entitySlices.length; ++i) {
            final Iterator<Object> iter = this.entitySlices[i].iterator();
            while (iter.hasNext()) {
                final Entity entity = iter.next();
                final int cx = Location.locToBlock(entity.locX) >> 4;
                final int cz = Location.locToBlock(entity.locZ) >> 4;
                if (entity instanceof EntityPlayer && (cx != this.x || cz != this.z)) {
                    iter.remove();
                }
            }
            this.world.b(this.entitySlices[i]);
        }
    }
    
    public void f() {
        this.o = true;
    }
    
    public void a(final Entity entity, final AxisAlignedBB axisalignedbb, final List list) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0) / 16.0);
        int j = MathHelper.floor((axisalignedbb.e + 2.0) / 16.0);
        if (i < 0) {
            i = 0;
        }
        if (j >= this.entitySlices.length) {
            j = this.entitySlices.length - 1;
        }
        for (int k = i; k <= j; ++k) {
            final List list2 = this.entitySlices[k];
            for (int l = 0; l < list2.size(); ++l) {
                final Entity entity2 = list2.get(l);
                if (entity2 != entity && entity2.boundingBox.a(axisalignedbb)) {
                    list.add(entity2);
                }
            }
        }
    }
    
    public void a(final Class oclass, final AxisAlignedBB axisalignedbb, final List list) {
        int i = MathHelper.floor((axisalignedbb.b - 2.0) / 16.0);
        int j = MathHelper.floor((axisalignedbb.e + 2.0) / 16.0);
        if (i < 0) {
            i = 0;
        }
        if (j >= this.entitySlices.length) {
            j = this.entitySlices.length - 1;
        }
        for (int k = i; k <= j; ++k) {
            final List list2 = this.entitySlices[k];
            for (int l = 0; l < list2.size(); ++l) {
                final Entity entity = list2.get(l);
                if (oclass.isAssignableFrom(entity.getClass()) && entity.boundingBox.a(axisalignedbb)) {
                    list.add(entity);
                }
            }
        }
    }
    
    public boolean a(final boolean flag) {
        if (this.p) {
            return false;
        }
        if (flag) {
            if (this.q && this.world.getTime() != this.r) {
                return true;
            }
        }
        else if (this.q && this.world.getTime() >= this.r + 600L) {
            return true;
        }
        return this.o;
    }
    
    public int getData(final byte[] abyte, final int i, final int j, final int k, final int l, final int i1, final int j1, int k1) {
        final int l2 = l - i;
        final int i2 = i1 - j;
        final int j2 = j1 - k;
        if (l2 * i2 * j2 == this.b.length) {
            System.arraycopy(this.b, 0, abyte, k1, this.b.length);
            k1 += this.b.length;
            System.arraycopy(this.e.a, 0, abyte, k1, this.e.a.length);
            k1 += this.e.a.length;
            System.arraycopy(this.g.a, 0, abyte, k1, this.g.a.length);
            k1 += this.g.a.length;
            System.arraycopy(this.f.a, 0, abyte, k1, this.f.a.length);
            k1 += this.f.a.length;
            return k1;
        }
        for (int k2 = i; k2 < l; ++k2) {
            for (int l3 = k; l3 < j1; ++l3) {
                final int i3 = k2 << 11 | l3 << 7 | j;
                final int j3 = i1 - j;
                System.arraycopy(this.b, i3, abyte, k1, j3);
                k1 += j3;
            }
        }
        for (int k2 = i; k2 < l; ++k2) {
            for (int l3 = k; l3 < j1; ++l3) {
                final int i3 = (k2 << 11 | l3 << 7 | j) >> 1;
                final int j3 = (i1 - j) / 2;
                System.arraycopy(this.e.a, i3, abyte, k1, j3);
                k1 += j3;
            }
        }
        for (int k2 = i; k2 < l; ++k2) {
            for (int l3 = k; l3 < j1; ++l3) {
                final int i3 = (k2 << 11 | l3 << 7 | j) >> 1;
                final int j3 = (i1 - j) / 2;
                System.arraycopy(this.g.a, i3, abyte, k1, j3);
                k1 += j3;
            }
        }
        for (int k2 = i; k2 < l; ++k2) {
            for (int l3 = k; l3 < j1; ++l3) {
                final int i3 = (k2 << 11 | l3 << 7 | j) >> 1;
                final int j3 = (i1 - j) / 2;
                System.arraycopy(this.f.a, i3, abyte, k1, j3);
                k1 += j3;
            }
        }
        return k1;
    }
    
    public Random a(final long i) {
        return new Random(this.world.getSeed() + this.x * this.x * 4987142 + this.x * 5947611 + this.z * this.z * 4392871L + this.z * 389711 ^ i);
    }
    
    public boolean isEmpty() {
        return false;
    }
    
    public void h() {
        BlockRegister.a(this.b);
    }
}
