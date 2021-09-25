// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.UUID;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import java.util.Iterator;
import java.util.Collection;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.Location;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.CraftWorld;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;

public class World implements IBlockAccess
{
    public boolean a;
    private List C;
    public List entityList;
    private List D;
    private TreeSet E;
    private Set F;
    public List c;
    private List G;
    public List players;
    public List e;
    private long H;
    public int f;
    protected int g;
    protected final int h = 1013904223;
    protected float i;
    protected float j;
    protected float k;
    protected float l;
    protected int m;
    public int n;
    public boolean suppressPhysics;
    private long I;
    public int p;
    public int spawnMonsters;
    public Random random;
    public boolean s;
    public WorldProvider worldProvider;
    protected List u;
    public IChunkProvider chunkProvider;
    protected final IDataManager w;
    public WorldData worldData;
    public boolean isLoading;
    private boolean J;
    public WorldMapCollection worldMaps;
    private ArrayList K;
    private boolean L;
    private int M;
    public boolean allowMonsters;
    public boolean allowAnimals;
    static int A;
    private Set P;
    private int Q;
    private List R;
    public boolean isStatic;
    private final CraftWorld world;
    public boolean pvpMode;
    public boolean keepSpawnInMemory;
    public ChunkGenerator generator;
    Chunk lastChunkAccessed;
    int lastXAccessed;
    int lastZAccessed;
    final Object chunkLock;
    private List<TileEntity> tileEntitiesToUnload;
    
    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.b;
    }
    
    private boolean canSpawn(final int x, final int z) {
        if (this.generator != null) {
            return this.generator.canSpawn((org.bukkit.World)this.getWorld(), x, z);
        }
        return this.worldProvider.canSpawn(x, z);
    }
    
    public CraftWorld getWorld() {
        return this.world;
    }
    
    public CraftServer getServer() {
        return (CraftServer)Bukkit.getServer();
    }
    
    public void markForRemoval(final TileEntity tileentity) {
        this.tileEntitiesToUnload.add(tileentity);
    }
    
    public World(final IDataManager idatamanager, final String s, final long i, final WorldProvider worldprovider, final ChunkGenerator gen, final org.bukkit.World.Environment env) {
        this.a = false;
        this.C = new ArrayList();
        this.entityList = new ArrayList();
        this.D = new ArrayList();
        this.E = new TreeSet();
        this.F = new HashSet();
        this.c = new ArrayList();
        this.G = new ArrayList();
        this.players = new ArrayList();
        this.e = new ArrayList();
        this.H = 16777215L;
        this.f = 0;
        this.g = new Random().nextInt();
        this.h = 1013904223;
        this.m = 0;
        this.n = 0;
        this.suppressPhysics = false;
        this.I = System.currentTimeMillis();
        this.p = 40;
        this.random = new Random();
        this.s = false;
        this.u = new ArrayList();
        this.K = new ArrayList();
        this.M = 0;
        this.allowMonsters = true;
        this.allowAnimals = true;
        this.P = new HashSet();
        this.keepSpawnInMemory = true;
        this.lastXAccessed = Integer.MIN_VALUE;
        this.lastZAccessed = Integer.MIN_VALUE;
        this.chunkLock = new Object();
        this.generator = gen;
        this.world = new CraftWorld((WorldServer)this, gen, env);
        this.tileEntitiesToUnload = new ArrayList();
        this.Q = this.random.nextInt(12000);
        this.R = new ArrayList();
        this.isStatic = false;
        this.w = idatamanager;
        this.worldMaps = new WorldMapCollection(idatamanager);
        this.worldData = idatamanager.c();
        this.s = (this.worldData == null);
        if (worldprovider != null) {
            this.worldProvider = worldprovider;
        }
        else if (this.worldData != null && this.worldData.h() == -1) {
            this.worldProvider = WorldProvider.byDimension(-1);
        }
        else {
            this.worldProvider = WorldProvider.byDimension(0);
        }
        boolean flag = false;
        if (this.worldData == null) {
            this.worldData = new WorldData(i, s);
            flag = true;
        }
        else {
            this.worldData.a(s);
        }
        this.worldProvider.a(this);
        this.chunkProvider = this.b();
        if (flag) {
            this.c();
        }
        this.g();
        this.x();
        this.getServer().addWorld((org.bukkit.World)this.world);
    }
    
    protected IChunkProvider b() {
        final IChunkLoader ichunkloader = this.w.a(this.worldProvider);
        return (IChunkProvider)new ChunkProviderLoadOrGenerate(this, ichunkloader, this.worldProvider.getChunkProvider());
    }
    
    protected void c() {
        this.isLoading = true;
        int i = 0;
        final byte b0 = 64;
        if (this.generator != null) {
            final Random rand = new Random(this.getSeed());
            final Location spawn = this.generator.getFixedSpawnLocation((org.bukkit.World)((WorldServer)this).getWorld(), rand);
            if (spawn != null) {
                if (spawn.getWorld() != ((WorldServer)this).getWorld()) {
                    throw new IllegalStateException("Cannot set spawn point for " + this.worldData.name + " to be in another world (" + spawn.getWorld().getName() + ")");
                }
                this.worldData.setSpawn(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
                this.isLoading = false;
                return;
            }
        }
        int j;
        for (j = 0; !this.canSpawn(i, j); i += this.random.nextInt(64) - this.random.nextInt(64), j += this.random.nextInt(64) - this.random.nextInt(64)) {}
        this.worldData.setSpawn(i, (int)b0, j);
        this.isLoading = false;
    }
    
    public int a(final int i, final int j) {
        int k;
        for (k = 63; !this.isEmpty(i, k + 1, j); ++k) {}
        return this.getTypeId(i, k, j);
    }
    
    public void save(final boolean flag, final IProgressUpdate iprogressupdate) {
        if (this.chunkProvider.canSave()) {
            if (iprogressupdate != null) {
                iprogressupdate.a("Saving level");
            }
            this.w();
            if (iprogressupdate != null) {
                iprogressupdate.b("Saving chunks");
            }
            this.chunkProvider.saveChunks(flag, iprogressupdate);
        }
    }
    
    private void w() {
        this.k();
        this.w.a(this.worldData, this.players);
        this.worldMaps.a();
    }
    
    public int getTypeId(final int i, final int j, final int k) {
        return (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000) ? ((j < 0) ? 0 : ((j >= 128) ? 0 : this.getChunkAt(i >> 4, k >> 4).getTypeId(i & 0xF, j, k & 0xF))) : 0;
    }
    
    public boolean isEmpty(final int i, final int j, final int k) {
        final int iBlockID = this.getTypeId(i, j, k);
        return iBlockID == 0 || Block.byId[iBlockID].isAirBlock(this, i, j, k);
    }
    
    public boolean isLoaded(final int i, final int j, final int k) {
        return j >= 0 && j < 128 && this.isChunkLoaded(i >> 4, k >> 4);
    }
    
    public boolean areChunksLoaded(final int i, final int j, final int k, final int l) {
        return this.a(i - l, j - l, k - l, i + l, j + l, k + l);
    }
    
    public boolean a(int i, int j, int k, int l, int i1, int j1) {
        if (i1 >= 0 && j < 128) {
            i >>= 4;
            j >>= 4;
            k >>= 4;
            l >>= 4;
            i1 >>= 4;
            j1 >>= 4;
            for (int k2 = i; k2 <= l; ++k2) {
                for (int l2 = k; l2 <= j1; ++l2) {
                    if (!this.isChunkLoaded(k2, l2)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean isChunkLoaded(final int i, final int j) {
        return this.chunkProvider.isChunkLoaded(i, j);
    }
    
    public Chunk getChunkAtWorldCoords(final int i, final int j) {
        return this.getChunkAt(i >> 4, j >> 4);
    }
    
    public Chunk getChunkAt(final int i, final int j) {
        Chunk result = null;
        synchronized (this.chunkLock) {
            if (this.lastChunkAccessed == null || this.lastXAccessed != i || this.lastZAccessed != j) {
                this.lastXAccessed = i;
                this.lastZAccessed = j;
                this.lastChunkAccessed = this.chunkProvider.getOrCreateChunk(i, j);
            }
            result = this.lastChunkAccessed;
        }
        return result;
    }
    
    public boolean setRawTypeIdAndData(final int i, final int j, final int k, final int l, final int i1) {
        if (i < -32000000 || k < -32000000 || i >= 32000000 || k > 32000000) {
            return false;
        }
        if (j < 0) {
            return false;
        }
        if (j >= 128) {
            return false;
        }
        final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
        return chunk.a(i & 0xF, j, k & 0xF, l, i1);
    }
    
    public boolean setRawTypeId(final int i, final int j, final int k, final int l) {
        if (i < -32000000 || k < -32000000 || i >= 32000000 || k > 32000000) {
            return false;
        }
        if (j < 0) {
            return false;
        }
        if (j >= 128) {
            return false;
        }
        final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
        return chunk.a(i & 0xF, j, k & 0xF, l);
    }
    
    public Material getMaterial(final int i, final int j, final int k) {
        final int l = this.getTypeId(i, j, k);
        return (l == 0) ? Material.AIR : Block.byId[l].material;
    }
    
    public int getData(int i, final int j, int k) {
        if (i < -32000000 || k < -32000000 || i >= 32000000 || k > 32000000) {
            return 0;
        }
        if (j < 0) {
            return 0;
        }
        if (j >= 128) {
            return 0;
        }
        final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
        i &= 0xF;
        k &= 0xF;
        return chunk.getData(i, j, k);
    }
    
    public void setData(final int i, final int j, final int k, final int l) {
        if (this.setRawData(i, j, k, l)) {
            final int i2 = this.getTypeId(i, j, k);
            if (Block.t[i2 & 0xFF]) {
                this.update(i, j, k, i2);
            }
            else {
                this.applyPhysics(i, j, k, i2);
            }
        }
    }
    
    public boolean setRawData(int i, final int j, int k, final int l) {
        if (i < -32000000 || k < -32000000 || i >= 32000000 || k > 32000000) {
            return false;
        }
        if (j < 0) {
            return false;
        }
        if (j >= 128) {
            return false;
        }
        final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
        i &= 0xF;
        k &= 0xF;
        chunk.b(i, j, k, l);
        return true;
    }
    
    public boolean setTypeId(final int i, final int j, final int k, final int l) {
        final int old = this.getTypeId(i, j, k);
        if (this.setRawTypeId(i, j, k, l)) {
            this.update(i, j, k, (l == 0) ? old : l);
            return true;
        }
        return false;
    }
    
    public boolean setTypeIdAndData(final int i, final int j, final int k, final int l, final int i1) {
        final int old = this.getTypeId(i, j, k);
        if (this.setRawTypeIdAndData(i, j, k, l, i1)) {
            this.update(i, j, k, (l == 0) ? old : l);
            return true;
        }
        return false;
    }
    
    public void notify(final int i, final int j, final int k) {
        for (int l = 0; l < this.u.size(); ++l) {
            this.u.get(l).a(i, j, k);
        }
    }
    
    protected void update(final int i, final int j, final int k, final int l) {
        this.notify(i, j, k);
        this.applyPhysics(i, j, k, l);
    }
    
    public void g(final int i, final int j, int k, int l) {
        if (k > l) {
            final int i2 = l;
            l = k;
            k = i2;
        }
        this.b(i, k, j, i, l, j);
    }
    
    public void i(final int i, final int j, final int k) {
        for (int l = 0; l < this.u.size(); ++l) {
            this.u.get(l).a(i, j, k, i, j, k);
        }
    }
    
    public void b(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        for (int k2 = 0; k2 < this.u.size(); ++k2) {
            this.u.get(k2).a(i, j, k, l, i1, j1);
        }
    }
    
    public void applyPhysics(final int i, final int j, final int k, final int l) {
        this.k(i - 1, j, k, l);
        this.k(i + 1, j, k, l);
        this.k(i, j - 1, k, l);
        this.k(i, j + 1, k, l);
        this.k(i, j, k - 1, l);
        this.k(i, j, k + 1, l);
    }
    
    private void k(final int i, final int j, final int k, final int l) {
        if (!this.suppressPhysics && !this.isStatic) {
            final Block block = Block.byId[this.getTypeId(i, j, k)];
            if (block != null) {
                final CraftWorld world = ((WorldServer)this).getWorld();
                if (world != null) {
                    final BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(i, j, k), l);
                    this.getServer().getPluginManager().callEvent((Event)event);
                    if (event.isCancelled()) {
                        return;
                    }
                }
                block.doPhysics(this, i, j, k, l);
            }
        }
    }
    
    public boolean isChunkLoaded(final int i, final int j, final int k) {
        return this.getChunkAt(i >> 4, k >> 4).c(i & 0xF, j, k & 0xF);
    }
    
    public int k(final int i, int j, final int k) {
        if (j < 0) {
            return 0;
        }
        if (j >= 128) {
            j = 127;
        }
        return this.getChunkAt(i >> 4, k >> 4).c(i & 0xF, j, k & 0xF, 0);
    }
    
    public int getLightLevel(final int i, final int j, final int k) {
        return this.a(i, j, k, true);
    }
    
    public int a(int i, int j, int k, final boolean flag) {
        if (i < -32000000 || k < -32000000 || i >= 32000000 || k > 32000000) {
            return 15;
        }
        if (flag) {
            final int l = this.getTypeId(i, j, k);
            if (l == Block.STEP.id || l == Block.SOIL.id || l == Block.COBBLESTONE_STAIRS.id || l == Block.WOOD_STAIRS.id) {
                int i2 = this.a(i, j + 1, k, false);
                final int j2 = this.a(i + 1, j, k, false);
                final int k2 = this.a(i - 1, j, k, false);
                final int l2 = this.a(i, j, k + 1, false);
                final int i3 = this.a(i, j, k - 1, false);
                if (j2 > i2) {
                    i2 = j2;
                }
                if (k2 > i2) {
                    i2 = k2;
                }
                if (l2 > i2) {
                    i2 = l2;
                }
                if (i3 > i2) {
                    i2 = i3;
                }
                return i2;
            }
        }
        if (j < 0) {
            return 0;
        }
        if (j >= 128) {
            j = 127;
        }
        final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
        i &= 0xF;
        k &= 0xF;
        return chunk.c(i, j, k, this.f);
    }
    
    public boolean m(int i, final int j, int k) {
        if (i < -32000000 || k < -32000000 || i >= 32000000 || k > 32000000) {
            return false;
        }
        if (j < 0) {
            return false;
        }
        if (j >= 128) {
            return true;
        }
        if (!this.isChunkLoaded(i >> 4, k >> 4)) {
            return false;
        }
        final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
        i &= 0xF;
        k &= 0xF;
        return chunk.c(i, j, k);
    }
    
    public int getHighestBlockYAt(final int i, final int j) {
        if (i < -32000000 || j < -32000000 || i >= 32000000 || j > 32000000) {
            return 0;
        }
        if (!this.isChunkLoaded(i >> 4, j >> 4)) {
            return 0;
        }
        final Chunk chunk = this.getChunkAt(i >> 4, j >> 4);
        return chunk.b(i & 0xF, j & 0xF);
    }
    
    public void a(final EnumSkyBlock enumskyblock, final int i, final int j, final int k, int l) {
        if ((!this.worldProvider.e || enumskyblock != EnumSkyBlock.SKY) && this.isLoaded(i, j, k)) {
            if (enumskyblock == EnumSkyBlock.SKY) {
                if (this.m(i, j, k)) {
                    l = 15;
                }
            }
            else if (enumskyblock == EnumSkyBlock.BLOCK) {
                final int i2 = this.getTypeId(i, j, k);
                if (Block.s[i2] > l) {
                    l = Block.s[i2];
                }
            }
            if (this.a(enumskyblock, i, j, k) != l) {
                this.a(enumskyblock, i, j, k, i, j, k);
            }
        }
    }
    
    public int a(final EnumSkyBlock enumskyblock, final int i, int j, final int k) {
        if (j < 0) {
            j = 0;
        }
        if (j >= 128) {
            j = 127;
        }
        if (j < 0 || j >= 128 || i < -32000000 || k < -32000000 || i >= 32000000 || k > 32000000) {
            return enumskyblock.c;
        }
        final int l = i >> 4;
        final int i2 = k >> 4;
        if (!this.isChunkLoaded(l, i2)) {
            return 0;
        }
        final Chunk chunk = this.getChunkAt(l, i2);
        return chunk.a(enumskyblock, i & 0xF, j, k & 0xF);
    }
    
    public void b(final EnumSkyBlock enumskyblock, final int i, final int j, final int k, final int l) {
        if (i >= -32000000 && k >= -32000000 && i < 32000000 && k <= 32000000 && j >= 0 && j < 128 && this.isChunkLoaded(i >> 4, k >> 4)) {
            final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
            chunk.a(enumskyblock, i & 0xF, j, k & 0xF, l);
            for (int i2 = 0; i2 < this.u.size(); ++i2) {
                this.u.get(i2).a(i, j, k);
            }
        }
    }
    
    public float n(final int i, final int j, final int k) {
        return this.worldProvider.f[this.getLightLevel(i, j, k)];
    }
    
    public boolean d() {
        return this.f < 4;
    }
    
    public MovingObjectPosition a(final Vec3D vec3d, final Vec3D vec3d1) {
        return this.rayTrace(vec3d, vec3d1, false, false);
    }
    
    public MovingObjectPosition rayTrace(final Vec3D vec3d, final Vec3D vec3d1, final boolean flag) {
        return this.rayTrace(vec3d, vec3d1, flag, false);
    }
    
    public MovingObjectPosition rayTrace(final Vec3D vec3d, final Vec3D vec3d1, final boolean flag, final boolean flag1) {
        if (Double.isNaN(vec3d.a) || Double.isNaN(vec3d.b) || Double.isNaN(vec3d.c)) {
            return null;
        }
        if (!Double.isNaN(vec3d1.a) && !Double.isNaN(vec3d1.b) && !Double.isNaN(vec3d1.c)) {
            final int i = MathHelper.floor(vec3d1.a);
            final int j = MathHelper.floor(vec3d1.b);
            final int k = MathHelper.floor(vec3d1.c);
            int l = MathHelper.floor(vec3d.a);
            int i2 = MathHelper.floor(vec3d.b);
            int j2 = MathHelper.floor(vec3d.c);
            int k2 = this.getTypeId(l, i2, j2);
            final int l2 = this.getData(l, i2, j2);
            final Block block = Block.byId[k2];
            if ((!flag1 || block == null || block.e(this, l, i2, j2) != null) && k2 > 0 && block.a(l2, flag)) {
                final MovingObjectPosition movingobjectposition = block.a(this, l, i2, j2, vec3d, vec3d1);
                if (movingobjectposition != null) {
                    return movingobjectposition;
                }
            }
            k2 = 200;
            while (k2-- >= 0) {
                if (Double.isNaN(vec3d.a) || Double.isNaN(vec3d.b) || Double.isNaN(vec3d.c)) {
                    return null;
                }
                if (l == i && i2 == j && j2 == k) {
                    return null;
                }
                boolean flag2 = true;
                boolean flag3 = true;
                boolean flag4 = true;
                double d0 = 999.0;
                double d2 = 999.0;
                double d3 = 999.0;
                if (i > l) {
                    d0 = l + 1.0;
                }
                else if (i < l) {
                    d0 = l + 0.0;
                }
                else {
                    flag2 = false;
                }
                if (j > i2) {
                    d2 = i2 + 1.0;
                }
                else if (j < i2) {
                    d2 = i2 + 0.0;
                }
                else {
                    flag3 = false;
                }
                if (k > j2) {
                    d3 = j2 + 1.0;
                }
                else if (k < j2) {
                    d3 = j2 + 0.0;
                }
                else {
                    flag4 = false;
                }
                double d4 = 999.0;
                double d5 = 999.0;
                double d6 = 999.0;
                final double d7 = vec3d1.a - vec3d.a;
                final double d8 = vec3d1.b - vec3d.b;
                final double d9 = vec3d1.c - vec3d.c;
                if (flag2) {
                    d4 = (d0 - vec3d.a) / d7;
                }
                if (flag3) {
                    d5 = (d2 - vec3d.b) / d8;
                }
                if (flag4) {
                    d6 = (d3 - vec3d.c) / d9;
                }
                final boolean flag5 = false;
                byte b0;
                if (d4 < d5 && d4 < d6) {
                    if (i > l) {
                        b0 = 4;
                    }
                    else {
                        b0 = 5;
                    }
                    vec3d.a = d0;
                    vec3d.b += d8 * d4;
                    vec3d.c += d9 * d4;
                }
                else if (d5 < d6) {
                    if (j > i2) {
                        b0 = 0;
                    }
                    else {
                        b0 = 1;
                    }
                    vec3d.a += d7 * d5;
                    vec3d.b = d2;
                    vec3d.c += d9 * d5;
                }
                else {
                    if (k > j2) {
                        b0 = 2;
                    }
                    else {
                        b0 = 3;
                    }
                    vec3d.a += d7 * d6;
                    vec3d.b += d8 * d6;
                    vec3d.c = d3;
                }
                final Vec3D create;
                final Vec3D vec3d2 = create = Vec3D.create(vec3d.a, vec3d.b, vec3d.c);
                final double a = MathHelper.floor(vec3d.a);
                create.a = a;
                l = (int)a;
                if (b0 == 5) {
                    --l;
                    final Vec3D vec3D = vec3d2;
                    ++vec3D.a;
                }
                final Vec3D vec3D2 = vec3d2;
                final double b2 = MathHelper.floor(vec3d.b);
                vec3D2.b = b2;
                i2 = (int)b2;
                if (b0 == 1) {
                    --i2;
                    final Vec3D vec3D3 = vec3d2;
                    ++vec3D3.b;
                }
                final Vec3D vec3D4 = vec3d2;
                final double c = MathHelper.floor(vec3d.c);
                vec3D4.c = c;
                j2 = (int)c;
                if (b0 == 3) {
                    --j2;
                    final Vec3D vec3D5 = vec3d2;
                    ++vec3D5.c;
                }
                final int i3 = this.getTypeId(l, i2, j2);
                final int j3 = this.getData(l, i2, j2);
                final Block block2 = Block.byId[i3];
                if ((flag1 && block2 != null && block2.e(this, l, i2, j2) == null) || i3 <= 0 || !block2.a(j3, flag)) {
                    continue;
                }
                final MovingObjectPosition movingobjectposition2 = block2.a(this, l, i2, j2, vec3d, vec3d1);
                if (movingobjectposition2 != null) {
                    return movingobjectposition2;
                }
            }
            return null;
        }
        return null;
    }
    
    public void makeSound(final Entity entity, final String s, final float f, final float f1) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).a(s, entity.locX, entity.locY - entity.height, entity.locZ, f, f1);
        }
    }
    
    public void makeSound(final double d0, final double d1, final double d2, final String s, final float f, final float f1) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).a(s, d0, d1, d2, f, f1);
        }
    }
    
    public void a(final String s, final int i, final int j, final int k) {
        for (int l = 0; l < this.u.size(); ++l) {
            this.u.get(l).a(s, i, j, k);
        }
    }
    
    public void a(final String s, final double d0, final double d1, final double d2, final double d3, final double d4, final double d5) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).a(s, d0, d1, d2, d3, d4, d5);
        }
    }
    
    public boolean strikeLightning(final Entity entity) {
        this.e.add(entity);
        return true;
    }
    
    public boolean addEntity(final Entity entity) {
        return this.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }
    
    public boolean addEntity(final Entity entity, final CreatureSpawnEvent.SpawnReason spawnReason) {
        final int i = MathHelper.floor(entity.locX / 16.0);
        final int j = MathHelper.floor(entity.locZ / 16.0);
        boolean flag = false;
        if (entity instanceof EntityHuman) {
            flag = true;
        }
        if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
            final CreatureSpawnEvent event = CraftEventFactory.callCreatureSpawnEvent((EntityLiving)entity, spawnReason);
            if (event.isCancelled()) {
                return false;
            }
        }
        else if (entity instanceof EntityItem) {
            final ItemSpawnEvent event2 = CraftEventFactory.callItemSpawnEvent((EntityItem)entity);
            if (event2.isCancelled()) {
                return false;
            }
        }
        if (!flag && !this.isChunkLoaded(i, j)) {
            return false;
        }
        if (entity instanceof EntityHuman) {
            final EntityHuman entityhuman = (EntityHuman)entity;
            this.players.add(entityhuman);
            this.everyoneSleeping();
        }
        this.getChunkAt(i, j).a(entity);
        this.entityList.add(entity);
        this.c(entity);
        return true;
    }
    
    protected void c(final Entity entity) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).a(entity);
        }
    }
    
    protected void d(final Entity entity) {
        for (int i = 0; i < this.u.size(); ++i) {
            this.u.get(i).b(entity);
        }
    }
    
    public void kill(final Entity entity) {
        if (entity.passenger != null) {
            entity.passenger.mount((Entity)null);
        }
        if (entity.vehicle != null) {
            entity.mount((Entity)null);
        }
        entity.die();
        if (entity instanceof EntityHuman) {
            this.players.remove(entity);
            this.everyoneSleeping();
        }
    }
    
    public void removeEntity(final Entity entity) {
        entity.die();
        if (entity instanceof EntityHuman) {
            this.players.remove(entity);
            this.everyoneSleeping();
        }
        final int i = entity.bH;
        final int j = entity.bJ;
        if (entity.bG && this.isChunkLoaded(i, j)) {
            this.getChunkAt(i, j).b(entity);
        }
        this.entityList.remove(entity);
        this.d(entity);
    }
    
    public void addIWorldAccess(final IWorldAccess iworldaccess) {
        this.u.add(iworldaccess);
    }
    
    public List getEntities(final Entity entity, final AxisAlignedBB axisalignedbb) {
        this.K.clear();
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.floor(axisalignedbb.d + 1.0);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.floor(axisalignedbb.e + 1.0);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.floor(axisalignedbb.f + 1.0);
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = i2; l2 < j2; ++l2) {
                if (this.isLoaded(k2, 64, l2)) {
                    for (int i3 = k - 1; i3 < l; ++i3) {
                        final Block block = Block.byId[this.getTypeId(k2, i3, l2)];
                        if (block != null) {
                            block.a(this, k2, i3, l2, axisalignedbb, this.K);
                        }
                    }
                }
            }
        }
        final double d0 = 0.25;
        final List list = this.b(entity, axisalignedbb.b(d0, d0, d0));
        for (int j3 = 0; j3 < list.size(); ++j3) {
            AxisAlignedBB axisalignedbb2 = list.get(j3).e_();
            if (axisalignedbb2 != null && axisalignedbb2.a(axisalignedbb)) {
                this.K.add(axisalignedbb2);
            }
            axisalignedbb2 = entity.a_((Entity)list.get(j3));
            if (axisalignedbb2 != null && axisalignedbb2.a(axisalignedbb)) {
                this.K.add(axisalignedbb2);
            }
        }
        return this.K;
    }
    
    public int a(final float f) {
        final float f2 = this.b(f);
        float f3 = 1.0f - (MathHelper.cos(f2 * 3.1415927f * 2.0f) * 2.0f + 0.5f);
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        f3 = 1.0f - f3;
        f3 *= (float)(1.0 - this.d(f) * 5.0f / 16.0);
        f3 *= (float)(1.0 - this.c(f) * 5.0f / 16.0);
        f3 = 1.0f - f3;
        return (int)(f3 * 11.0f);
    }
    
    public float b(final float f) {
        return this.worldProvider.a(this.worldData.f(), f);
    }
    
    public int e(int i, int j) {
        final Chunk chunk = this.getChunkAtWorldCoords(i, j);
        int k = 127;
        i &= 0xF;
        j &= 0xF;
        while (k > 0) {
            final int l = chunk.getTypeId(i, k, j);
            final Material material = (l == 0) ? Material.AIR : Block.byId[l].material;
            if (material.isSolid() || material.isLiquid()) {
                return k + 1;
            }
            --k;
        }
        return -1;
    }
    
    public int f(int i, int j) {
        final Chunk chunk = this.getChunkAtWorldCoords(i, j);
        int k = 127;
        i &= 0xF;
        j &= 0xF;
        while (k > 0) {
            final int l = chunk.getTypeId(i, k, j);
            if (l != 0 && Block.byId[l].material.isSolid()) {
                return k + 1;
            }
            --k;
        }
        return -1;
    }
    
    public void c(final int i, final int j, final int k, final int l, final int i1) {
        final NextTickListEntry nextticklistentry = new NextTickListEntry(i, j, k, l);
        final byte b0 = 8;
        if (this.a) {
            if (this.a(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                final int j2 = this.getTypeId(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);
                if (j2 == nextticklistentry.d && j2 > 0) {
                    Block.byId[j2].a(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.random);
                }
            }
        }
        else if (this.a(i - b0, j - b0, k - b0, i + b0, j + b0, k + b0)) {
            if (l > 0) {
                nextticklistentry.a(i1 + this.worldData.f());
            }
            if (!this.F.contains(nextticklistentry)) {
                this.F.add(nextticklistentry);
                this.E.add(nextticklistentry);
            }
        }
    }
    
    public void cleanUp() {
        for (int i = 0; i < this.e.size(); ++i) {
            final Entity entity = this.e.get(i);
            if (entity != null) {
                entity.m_();
                if (entity.dead) {
                    this.e.remove(i--);
                }
            }
        }
        this.entityList.removeAll(this.D);
        for (int i = 0; i < this.D.size(); ++i) {
            final Entity entity = this.D.get(i);
            final int j = entity.bH;
            final int k = entity.bJ;
            if (entity.bG && this.isChunkLoaded(j, k)) {
                this.getChunkAt(j, k).b(entity);
            }
        }
        for (int i = 0; i < this.D.size(); ++i) {
            this.d(this.D.get(i));
        }
        this.D.clear();
        for (int i = 0; i < this.entityList.size(); ++i) {
            final Entity entity = this.entityList.get(i);
            if (entity.vehicle != null) {
                if (!entity.vehicle.dead && entity.vehicle.passenger == entity) {
                    continue;
                }
                entity.vehicle.passenger = null;
                entity.vehicle = null;
            }
            if (!entity.dead) {
                this.playerJoinedWorld(entity);
            }
            if (entity.dead) {
                final int j = entity.bH;
                final int k = entity.bJ;
                if (entity.bG && this.isChunkLoaded(j, k)) {
                    this.getChunkAt(j, k).b(entity);
                }
                this.entityList.remove(i--);
                this.d(entity);
            }
        }
        this.L = true;
        final Iterator iterator = this.c.iterator();
        while (iterator.hasNext()) {
            final TileEntity tileentity = iterator.next();
            if (!tileentity.g()) {
                tileentity.g_();
            }
            if (tileentity.g()) {
                iterator.remove();
                final Chunk chunk = this.getChunkAt(tileentity.x >> 4, tileentity.z >> 4);
                if (chunk == null) {
                    continue;
                }
                chunk.e(tileentity.x & 0xF, tileentity.y, tileentity.z & 0xF);
            }
        }
        this.L = false;
        if (!this.tileEntitiesToUnload.isEmpty()) {
            this.c.removeAll(this.tileEntitiesToUnload);
            this.tileEntitiesToUnload.clear();
        }
        if (!this.G.isEmpty()) {
            for (final TileEntity tileentity2 : this.G) {
                if (!tileentity2.g()) {
                    final Chunk chunk2 = this.getChunkAt(tileentity2.x >> 4, tileentity2.z >> 4);
                    if (chunk2 != null) {
                        chunk2.a(tileentity2.x & 0xF, tileentity2.y, tileentity2.z & 0xF, tileentity2);
                        if (!this.c.contains(tileentity2)) {
                            this.c.add(tileentity2);
                        }
                    }
                    this.notify(tileentity2.x, tileentity2.y, tileentity2.z);
                }
            }
            this.G.clear();
        }
    }
    
    public void a(final Collection collection) {
        if (this.L) {
            this.G.addAll(collection);
        }
        else {
            this.c.addAll(collection);
        }
    }
    
    public void playerJoinedWorld(final Entity entity) {
        this.entityJoinedWorld(entity, true);
    }
    
    public void entityJoinedWorld(final Entity entity, final boolean flag) {
        final int i = MathHelper.floor(entity.locX);
        final int j = MathHelper.floor(entity.locZ);
        final byte b0 = 32;
        if (!flag || this.a(i - b0, 0, j - b0, i + b0, 128, j + b0)) {
            entity.bo = entity.locX;
            entity.bp = entity.locY;
            entity.bq = entity.locZ;
            entity.lastYaw = entity.yaw;
            entity.lastPitch = entity.pitch;
            if (flag && entity.bG) {
                if (entity.vehicle != null) {
                    entity.E();
                }
                else {
                    entity.m_();
                }
            }
            if (Double.isNaN(entity.locX) || Double.isInfinite(entity.locX)) {
                entity.locX = entity.bo;
            }
            if (Double.isNaN(entity.locY) || Double.isInfinite(entity.locY)) {
                entity.locY = entity.bp;
            }
            if (Double.isNaN(entity.locZ) || Double.isInfinite(entity.locZ)) {
                entity.locZ = entity.bq;
            }
            if (Double.isNaN(entity.pitch) || Double.isInfinite(entity.pitch)) {
                entity.pitch = entity.lastPitch;
            }
            if (Double.isNaN(entity.yaw) || Double.isInfinite(entity.yaw)) {
                entity.yaw = entity.lastYaw;
            }
            final int k = MathHelper.floor(entity.locX / 16.0);
            final int l = MathHelper.floor(entity.locY / 16.0);
            final int i2 = MathHelper.floor(entity.locZ / 16.0);
            if (!entity.bG || entity.bH != k || entity.bI != l || entity.bJ != i2) {
                if (entity.bG && this.isChunkLoaded(entity.bH, entity.bJ)) {
                    this.getChunkAt(entity.bH, entity.bJ).a(entity, entity.bI);
                }
                if (this.isChunkLoaded(k, i2)) {
                    entity.bG = true;
                    this.getChunkAt(k, i2).a(entity);
                }
                else {
                    entity.bG = false;
                }
            }
            if (flag && entity.bG && entity.passenger != null) {
                if (!entity.passenger.dead && entity.passenger.vehicle == entity) {
                    this.playerJoinedWorld(entity.passenger);
                }
                else {
                    entity.passenger.vehicle = null;
                    entity.passenger = null;
                }
            }
        }
    }
    
    public boolean containsEntity(final AxisAlignedBB axisalignedbb) {
        final List list = this.b(null, axisalignedbb);
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity = list.get(i);
            if (!entity.dead && entity.aI) {
                return false;
            }
        }
        return true;
    }
    
    public boolean b(final AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.floor(axisalignedbb.d + 1.0);
        int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.floor(axisalignedbb.e + 1.0);
        int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.floor(axisalignedbb.f + 1.0);
        if (axisalignedbb.a < 0.0) {
            --i;
        }
        if (axisalignedbb.b < 0.0) {
            --k;
        }
        if (axisalignedbb.c < 0.0) {
            --i2;
        }
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final Block block = Block.byId[this.getTypeId(k2, l2, i3)];
                    if (block != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean c(final AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.floor(axisalignedbb.d + 1.0);
        int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.floor(axisalignedbb.e + 1.0);
        int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.floor(axisalignedbb.f + 1.0);
        if (axisalignedbb.a < 0.0) {
            --i;
        }
        if (axisalignedbb.b < 0.0) {
            --k;
        }
        if (axisalignedbb.c < 0.0) {
            --i2;
        }
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final Block block = Block.byId[this.getTypeId(k2, l2, i3)];
                    if (block != null && block.material.isLiquid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean d(final AxisAlignedBB axisalignedbb) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.floor(axisalignedbb.d + 1.0);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.floor(axisalignedbb.e + 1.0);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.floor(axisalignedbb.f + 1.0);
        if (this.a(i, k, i2, j, l, j2)) {
            for (int k2 = i; k2 < j; ++k2) {
                for (int l2 = k; l2 < l; ++l2) {
                    for (int i3 = i2; i3 < j2; ++i3) {
                        final int j3 = this.getTypeId(k2, l2, i3);
                        if (j3 == Block.FIRE.id || j3 == Block.LAVA.id || j3 == Block.STATIONARY_LAVA.id) {
                            return true;
                        }
                        if (j3 > 0 && Block.byId[j3].isBlockBurning(this, k2, l2, i3)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean a(final AxisAlignedBB axisalignedbb, final Material material, final Entity entity) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.floor(axisalignedbb.d + 1.0);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.floor(axisalignedbb.e + 1.0);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.floor(axisalignedbb.f + 1.0);
        if (!this.a(i, k, i2, j, l, j2)) {
            return false;
        }
        boolean flag = false;
        Vec3D vec3d = Vec3D.create(0.0, 0.0, 0.0);
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final Block block = Block.byId[this.getTypeId(k2, l2, i3)];
                    if (block != null && block.material == material) {
                        final double d0 = l2 + 1 - BlockFluids.c(this.getData(k2, l2, i3));
                        if (l >= d0) {
                            flag = true;
                            block.a(this, k2, l2, i3, entity, vec3d);
                        }
                    }
                }
            }
        }
        if (vec3d.c() > 0.0) {
            vec3d = vec3d.b();
            final double d2 = 0.014;
            entity.motX += vec3d.a * d2;
            entity.motY += vec3d.b * d2;
            entity.motZ += vec3d.c * d2;
        }
        return flag;
    }
    
    public boolean a(final AxisAlignedBB axisalignedbb, final Material material) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.floor(axisalignedbb.d + 1.0);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.floor(axisalignedbb.e + 1.0);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.floor(axisalignedbb.f + 1.0);
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final Block block = Block.byId[this.getTypeId(k2, l2, i3)];
                    if (block != null && block.material == material) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean b(final AxisAlignedBB axisalignedbb, final Material material) {
        final int i = MathHelper.floor(axisalignedbb.a);
        final int j = MathHelper.floor(axisalignedbb.d + 1.0);
        final int k = MathHelper.floor(axisalignedbb.b);
        final int l = MathHelper.floor(axisalignedbb.e + 1.0);
        final int i2 = MathHelper.floor(axisalignedbb.c);
        final int j2 = MathHelper.floor(axisalignedbb.f + 1.0);
        for (int k2 = i; k2 < j; ++k2) {
            for (int l2 = k; l2 < l; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final Block block = Block.byId[this.getTypeId(k2, l2, i3)];
                    if (block != null && block.material == material) {
                        final int j3 = this.getData(k2, l2, i3);
                        double d0 = l2 + 1;
                        if (j3 < 8) {
                            d0 = l2 + 1 - j3 / 8.0;
                        }
                        if (d0 >= axisalignedbb.b) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public Explosion a(final Entity entity, final double d0, final double d1, final double d2, final float f) {
        return this.createExplosion(entity, d0, d1, d2, f, false);
    }
    
    public Explosion createExplosion(final Entity entity, final double d0, final double d1, final double d2, final float f, final boolean flag) {
        final Explosion explosion = new Explosion(this, entity, d0, d1, d2, f);
        explosion.a = flag;
        explosion.a();
        explosion.a(true);
        return explosion;
    }
    
    public float a(final Vec3D vec3d, final AxisAlignedBB axisalignedbb) {
        final double d0 = 1.0 / ((axisalignedbb.d - axisalignedbb.a) * 2.0 + 1.0);
        final double d2 = 1.0 / ((axisalignedbb.e - axisalignedbb.b) * 2.0 + 1.0);
        final double d3 = 1.0 / ((axisalignedbb.f - axisalignedbb.c) * 2.0 + 1.0);
        int i = 0;
        int j = 0;
        for (float f = 0.0f; f <= 1.0f; f += (float)d0) {
            for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float)d2) {
                for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float)d3) {
                    final double d4 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * f;
                    final double d5 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * f2;
                    final double d6 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * f3;
                    if (this.a(Vec3D.create(d4, d5, d6), vec3d) == null) {
                        ++i;
                    }
                    ++j;
                }
            }
        }
        return i / (float)j;
    }
    
    public void douseFire(final EntityHuman entityhuman, int i, int j, int k, final int l) {
        if (l == 0) {
            --j;
        }
        if (l == 1) {
            ++j;
        }
        if (l == 2) {
            --k;
        }
        if (l == 3) {
            ++k;
        }
        if (l == 4) {
            --i;
        }
        if (l == 5) {
            ++i;
        }
        if (this.getTypeId(i, j, k) == Block.FIRE.id) {
            this.a(entityhuman, 1004, i, j, k, 0);
            this.setTypeId(i, j, k, 0);
        }
    }
    
    public TileEntity getTileEntity(final int i, final int j, final int k) {
        final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
        return (chunk != null) ? chunk.d(i & 0xF, j, k & 0xF) : null;
    }
    
    public void setTileEntity(final int i, final int j, final int k, final TileEntity tileentity) {
        if (!tileentity.g()) {
            if (this.L) {
                tileentity.x = i;
                tileentity.y = j;
                tileentity.z = k;
                this.G.add(tileentity);
            }
            else {
                final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
                if (chunk != null) {
                    chunk.a(i & 0xF, j, k & 0xF, tileentity);
                    this.c.add(tileentity);
                }
            }
        }
    }
    
    public void o(final int i, final int j, final int k) {
        final TileEntity tileentity = this.getTileEntity(i, j, k);
        if (tileentity != null && this.L) {
            tileentity.h();
        }
        else {
            if (tileentity != null) {
                this.c.remove(tileentity);
            }
            final Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
            if (chunk != null) {
                chunk.e(i & 0xF, j, k & 0xF);
            }
        }
    }
    
    public boolean p(final int i, final int j, final int k) {
        final Block block = Block.byId[this.getTypeId(i, j, k)];
        return block != null && block.a();
    }
    
    public boolean e(final int i, final int j, final int k) {
        final Block block = Block.byId[this.getTypeId(i, j, k)];
        return block != null && block.isBlockNormalCube(this, i, j, k);
    }
    
    public boolean isBlockSolidOnSide(final int i, final int j, final int k, final int side) {
        final Block block = Block.byId[this.getTypeId(i, j, k)];
        return block != null && block.isBlockSolidOnSide(this, i, j, k, side);
    }
    
    public boolean doLighting() {
        if (this.M >= 50) {
            return false;
        }
        ++this.M;
        boolean flag;
        try {
            int i = 500;
            while (this.C.size() > 0) {
                if (--i <= 0) {
                    flag = true;
                    return flag;
                }
                this.C.remove(this.C.size() - 1).a(this);
            }
            flag = false;
        }
        finally {
            --this.M;
        }
        return flag;
    }
    
    public void a(final EnumSkyBlock enumskyblock, final int i, final int j, final int k, final int l, final int i1, final int j1) {
        this.a(enumskyblock, i, j, k, l, i1, j1, true);
    }
    
    public void a(final EnumSkyBlock enumskyblock, final int i, final int j, final int k, final int l, final int i1, final int j1, final boolean flag) {
        if (!this.worldProvider.e || enumskyblock != EnumSkyBlock.SKY) {
            ++World.A;
            try {
                if (World.A == 50) {
                    return;
                }
                final int k2 = (l + i) / 2;
                final int l2 = (j1 + k) / 2;
                if (this.isLoaded(k2, 64, l2)) {
                    if (this.getChunkAtWorldCoords(k2, l2).isEmpty()) {
                        return;
                    }
                    final int i2 = this.C.size();
                    if (flag) {
                        int j2 = 5;
                        if (j2 > i2) {
                            j2 = i2;
                        }
                        for (int k3 = 0; k3 < j2; ++k3) {
                            final MetadataChunkBlock metadatachunkblock = this.C.get(this.C.size() - k3 - 1);
                            if (metadatachunkblock.a == enumskyblock && metadatachunkblock.a(i, j, k, l, i1, j1)) {
                                return;
                            }
                        }
                    }
                    this.C.add(new MetadataChunkBlock(enumskyblock, i, j, k, l, i1, j1));
                    int j2 = 1000000;
                    if (this.C.size() > 1000000) {
                        System.out.println("More than " + j2 + " updates, aborting lighting updates");
                        this.C.clear();
                    }
                }
            }
            finally {
                --World.A;
            }
        }
    }
    
    public void g() {
        final int i = this.a(1.0f);
        if (i != this.f) {
            this.f = i;
        }
    }
    
    public void setSpawnFlags(final boolean flag, final boolean flag1) {
        this.allowMonsters = flag;
        this.allowAnimals = flag1;
    }
    
    public void doTick() {
        this.i();
        if (this.everyoneDeeplySleeping()) {
            boolean flag = false;
            if (this.allowMonsters && this.spawnMonsters >= 1) {
                flag = SpawnerCreature.a(this, this.players);
            }
            if (!flag) {
                final long i = this.worldData.f() + 24000L;
                this.worldData.a(i - i % 24000L);
                this.s();
            }
        }
        if ((this.allowMonsters || this.allowAnimals) && this instanceof WorldServer && this.getServer().getHandle().players.size() > 0) {
            SpawnerCreature.spawnEntities(this, this.allowMonsters, this.allowAnimals);
        }
        this.chunkProvider.unloadChunks();
        final int j = this.a(1.0f);
        if (j != this.f) {
            this.f = j;
            for (int k = 0; k < this.u.size(); ++k) {
                this.u.get(k).a();
            }
        }
        final long i = this.worldData.f() + 1L;
        if (i % this.p == 0L) {
            this.save(false, null);
        }
        this.worldData.a(i);
        this.a(false);
        this.j();
    }
    
    private void x() {
        if (this.worldData.hasStorm()) {
            this.j = 1.0f;
            if (this.worldData.isThundering()) {
                this.l = 1.0f;
            }
        }
    }
    
    protected void i() {
        if (!this.worldProvider.e) {
            if (this.m > 0) {
                --this.m;
            }
            int i = this.worldData.getThunderDuration();
            if (i <= 0) {
                if (this.worldData.isThundering()) {
                    this.worldData.setThunderDuration(this.random.nextInt(12000) + 3600);
                }
                else {
                    this.worldData.setThunderDuration(this.random.nextInt(168000) + 12000);
                }
            }
            else {
                --i;
                this.worldData.setThunderDuration(i);
                if (i <= 0) {
                    final ThunderChangeEvent thunder = new ThunderChangeEvent((org.bukkit.World)this.getWorld(), !this.worldData.isThundering());
                    this.getServer().getPluginManager().callEvent((Event)thunder);
                    if (!thunder.isCancelled()) {
                        this.worldData.setThundering(!this.worldData.isThundering());
                    }
                }
            }
            int j = this.worldData.getWeatherDuration();
            if (j <= 0) {
                if (this.worldData.hasStorm()) {
                    this.worldData.setWeatherDuration(this.random.nextInt(12000) + 12000);
                }
                else {
                    this.worldData.setWeatherDuration(this.random.nextInt(168000) + 12000);
                }
            }
            else {
                --j;
                this.worldData.setWeatherDuration(j);
                if (j <= 0) {
                    final WeatherChangeEvent weather = new WeatherChangeEvent((org.bukkit.World)this.getWorld(), !this.worldData.hasStorm());
                    this.getServer().getPluginManager().callEvent((Event)weather);
                    if (!weather.isCancelled()) {
                        this.worldData.setStorm(!this.worldData.hasStorm());
                    }
                }
            }
            this.i = this.j;
            if (this.worldData.hasStorm()) {
                this.j += (float)0.01;
            }
            else {
                this.j -= (float)0.01;
            }
            if (this.j < 0.0f) {
                this.j = 0.0f;
            }
            if (this.j > 1.0f) {
                this.j = 1.0f;
            }
            this.k = this.l;
            if (this.worldData.isThundering()) {
                this.l += (float)0.01;
            }
            else {
                this.l -= (float)0.01;
            }
            if (this.l < 0.0f) {
                this.l = 0.0f;
            }
            if (this.l > 1.0f) {
                this.l = 1.0f;
            }
        }
    }
    
    private void y() {
        final WeatherChangeEvent weather = new WeatherChangeEvent((org.bukkit.World)this.getWorld(), false);
        this.getServer().getPluginManager().callEvent((Event)weather);
        final ThunderChangeEvent thunder = new ThunderChangeEvent((org.bukkit.World)this.getWorld(), false);
        this.getServer().getPluginManager().callEvent((Event)thunder);
        if (!weather.isCancelled()) {
            this.worldData.setWeatherDuration(0);
            this.worldData.setStorm(false);
        }
        if (!thunder.isCancelled()) {
            this.worldData.setThunderDuration(0);
            this.worldData.setThundering(false);
        }
    }
    
    protected void j() {
        this.P.clear();
        for (int i1 = 0; i1 < this.players.size(); ++i1) {
            final EntityHuman entityhuman = this.players.get(i1);
            final int j = MathHelper.floor(entityhuman.locX / 16.0);
            final int k = MathHelper.floor(entityhuman.locZ / 16.0);
            final byte b0 = 9;
            for (int l = -b0; l <= b0; ++l) {
                for (int m = -b0; m <= b0; ++m) {
                    this.P.add(new ChunkCoordIntPair(l + j, m + k));
                }
            }
        }
        if (this.Q > 0) {
            --this.Q;
        }
        for (final ChunkCoordIntPair chunkcoordintpair : this.P) {
            final int j = chunkcoordintpair.x * 16;
            final int k = chunkcoordintpair.z * 16;
            final Chunk chunk = this.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z);
            if (this.Q == 0) {
                this.g = this.g * 3 + 1013904223;
                final int l = this.g >> 2;
                int m = l & 0xF;
                int j2 = l >> 8 & 0xF;
                final int k2 = l >> 16 & 0x7F;
                final int l2 = chunk.getTypeId(m, k2, j2);
                m += j;
                j2 += k;
                if (l2 == 0 && this.k(m, k2, j2) <= this.random.nextInt(8) && this.a(EnumSkyBlock.SKY, m, k2, j2) <= 0) {
                    final EntityHuman entityhuman2 = this.a(m + 0.5, k2 + 0.5, j2 + 0.5, 8.0);
                    if (entityhuman2 != null && entityhuman2.e(m + 0.5, k2 + 0.5, j2 + 0.5) > 4.0) {
                        this.makeSound(m + 0.5, k2 + 0.5, j2 + 0.5, "ambient.cave.cave", 0.7f, 0.8f + this.random.nextFloat() * 0.2f);
                        this.Q = this.random.nextInt(12000) + 6000;
                    }
                }
            }
            if (this.random.nextInt(100000) == 0 && this.v() && this.u()) {
                this.g = this.g * 3 + 1013904223;
                final int l = this.g >> 2;
                final int m = j + (l & 0xF);
                final int j2 = k + (l >> 8 & 0xF);
                final int k2 = this.e(m, j2);
                if (this.s(m, k2, j2)) {
                    this.strikeLightning((Entity)new EntityWeatherStorm(this, (double)m, (double)k2, (double)j2));
                    this.m = 2;
                }
            }
            if (this.random.nextInt(16) == 0) {
                this.g = this.g * 3 + 1013904223;
                final int l = this.g >> 2;
                final int m = l & 0xF;
                final int j2 = l >> 8 & 0xF;
                final int k2 = this.e(m + j, j2 + k);
                if (this.getWorldChunkManager().getBiome(m + j, j2 + k).c() && k2 >= 0 && k2 < 128 && chunk.a(EnumSkyBlock.BLOCK, m, k2, j2) < 10) {
                    final int l2 = chunk.getTypeId(m, k2 - 1, j2);
                    final int i2 = chunk.getTypeId(m, k2, j2);
                    if (this.v() && i2 == 0 && Block.SNOW.canPlace(this, m + j, k2, j2 + k) && l2 != 0 && l2 != Block.ICE.id && Block.byId[l2].material.isSolid()) {
                        final BlockState blockState = this.getWorld().getBlockAt(m + j, k2, j2 + k).getState();
                        blockState.setTypeId(Block.SNOW.id);
                        final BlockFormEvent snow = new BlockFormEvent(blockState.getBlock(), blockState);
                        this.getServer().getPluginManager().callEvent((Event)snow);
                        if (!snow.isCancelled()) {
                            blockState.update(true);
                        }
                    }
                    if (l2 == Block.STATIONARY_WATER.id && chunk.getData(m, k2 - 1, j2) == 0) {
                        final BlockState blockState = this.getWorld().getBlockAt(m + j, k2 - 1, j2 + k).getState();
                        blockState.setTypeId(Block.ICE.id);
                        final BlockFormEvent iceBlockForm = new BlockFormEvent(blockState.getBlock(), blockState);
                        this.getServer().getPluginManager().callEvent((Event)iceBlockForm);
                        if (!iceBlockForm.isCancelled()) {
                            blockState.update(true);
                        }
                    }
                }
            }
            for (int l = 0; l < 80; ++l) {
                this.g = this.g * 3 + 1013904223;
                final int m = this.g >> 2;
                final int j2 = m & 0xF;
                final int k2 = m >> 8 & 0xF;
                final int l2 = m >> 16 & 0x7F;
                final int i2 = chunk.b[j2 << 11 | k2 << 7 | l2] & 0xFF;
                if (Block.n[i2]) {
                    Block.byId[i2].a(this, j2 + j, l2, k2 + k, this.random);
                }
            }
        }
    }
    
    public boolean a(final boolean flag) {
        int i = this.E.size();
        if (i != this.F.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        if (i > 1000) {
            i = 1000;
        }
        for (int j = 0; j < i; ++j) {
            final NextTickListEntry nextticklistentry = this.E.first();
            if (!flag && nextticklistentry.e > this.worldData.f()) {
                break;
            }
            this.E.remove(nextticklistentry);
            this.F.remove(nextticklistentry);
            final byte b0 = 8;
            if (this.a(nextticklistentry.a - b0, nextticklistentry.b - b0, nextticklistentry.c - b0, nextticklistentry.a + b0, nextticklistentry.b + b0, nextticklistentry.c + b0)) {
                final int k = this.getTypeId(nextticklistentry.a, nextticklistentry.b, nextticklistentry.c);
                if (k == nextticklistentry.d && k > 0) {
                    Block.byId[k].a(this, nextticklistentry.a, nextticklistentry.b, nextticklistentry.c, this.random);
                }
            }
        }
        return this.E.size() != 0;
    }
    
    public List b(final Entity entity, final AxisAlignedBB axisalignedbb) {
        this.R.clear();
        final int i = MathHelper.floor((axisalignedbb.a - 2.0) / 16.0);
        final int j = MathHelper.floor((axisalignedbb.d + 2.0) / 16.0);
        final int k = MathHelper.floor((axisalignedbb.c - 2.0) / 16.0);
        final int l = MathHelper.floor((axisalignedbb.f + 2.0) / 16.0);
        for (int i2 = i; i2 <= j; ++i2) {
            for (int j2 = k; j2 <= l; ++j2) {
                if (this.isChunkLoaded(i2, j2)) {
                    this.getChunkAt(i2, j2).a(entity, axisalignedbb, this.R);
                }
            }
        }
        return this.R;
    }
    
    public List a(final Class oclass, final AxisAlignedBB axisalignedbb) {
        final int i = MathHelper.floor((axisalignedbb.a - 2.0) / 16.0);
        final int j = MathHelper.floor((axisalignedbb.d + 2.0) / 16.0);
        final int k = MathHelper.floor((axisalignedbb.c - 2.0) / 16.0);
        final int l = MathHelper.floor((axisalignedbb.f + 2.0) / 16.0);
        final ArrayList arraylist = new ArrayList();
        for (int i2 = i; i2 <= j; ++i2) {
            for (int j2 = k; j2 <= l; ++j2) {
                if (this.isChunkLoaded(i2, j2)) {
                    this.getChunkAt(i2, j2).a(oclass, axisalignedbb, (List)arraylist);
                }
            }
        }
        return arraylist;
    }
    
    public void b(final int i, final int j, final int k, final TileEntity tileentity) {
        if (this.isLoaded(i, j, k)) {
            this.getChunkAtWorldCoords(i, k).f();
        }
        for (int l = 0; l < this.u.size(); ++l) {
            this.u.get(l).a(i, j, k, tileentity);
        }
    }
    
    public int a(final Class oclass) {
        int i = 0;
        for (int j = 0; j < this.entityList.size(); ++j) {
            final Entity entity = this.entityList.get(j);
            if (oclass.isAssignableFrom(entity.getClass())) {
                ++i;
            }
        }
        return i;
    }
    
    public void a(final List list) {
        Entity entity = null;
        for (int i = 0; i < list.size(); ++i) {
            entity = list.get(i);
            if (entity != null) {
                this.entityList.add(entity);
                this.c(list.get(i));
            }
        }
    }
    
    public void b(final List list) {
        this.D.addAll(list);
    }
    
    public boolean a(final int i, final int j, final int k, final int l, final boolean flag, final int i1) {
        final int j2 = this.getTypeId(j, k, l);
        Block block = Block.byId[j2];
        final Block block2 = Block.byId[i];
        AxisAlignedBB axisalignedbb = block2.e(this, j, k, l);
        if (flag) {
            axisalignedbb = null;
        }
        boolean defaultReturn;
        if (axisalignedbb != null && !this.containsEntity(axisalignedbb)) {
            defaultReturn = false;
        }
        else {
            if (block == Block.WATER || block == Block.STATIONARY_WATER || block == Block.LAVA || block == Block.STATIONARY_LAVA || block == Block.FIRE || block == Block.SNOW) {
                block = null;
            }
            if (block != null && block.isBlockReplaceable(this, j, k, l)) {
                block = null;
            }
            defaultReturn = (i > 0 && block == null && block2.canPlace(this, j, k, l, i1));
        }
        final BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(j, k, l), i, defaultReturn);
        this.getServer().getPluginManager().callEvent((Event)event);
        return event.isBuildable();
    }
    
    public PathEntity findPath(final Entity entity, final Entity entity1, final float f) {
        final int i = MathHelper.floor(entity.locX);
        final int j = MathHelper.floor(entity.locY);
        final int k = MathHelper.floor(entity.locZ);
        final int l = (int)(f + 16.0f);
        final int i2 = i - l;
        final int j2 = j - l;
        final int k2 = k - l;
        final int l2 = i + l;
        final int i3 = j + l;
        final int j3 = k + l;
        final ChunkCache chunkcache = new ChunkCache(this, i2, j2, k2, l2, i3, j3);
        return new Pathfinder((IBlockAccess)chunkcache).a(entity, entity1, f);
    }
    
    public PathEntity a(final Entity entity, final int i, final int j, final int k, final float f) {
        final int l = MathHelper.floor(entity.locX);
        final int i2 = MathHelper.floor(entity.locY);
        final int j2 = MathHelper.floor(entity.locZ);
        final int k2 = (int)(f + 8.0f);
        final int l2 = l - k2;
        final int i3 = i2 - k2;
        final int j3 = j2 - k2;
        final int k3 = l + k2;
        final int l3 = i2 + k2;
        final int i4 = j2 + k2;
        final ChunkCache chunkcache = new ChunkCache(this, l2, i3, j3, k3, l3, i4);
        return new Pathfinder((IBlockAccess)chunkcache).a(entity, i, j, k, f);
    }
    
    public boolean isBlockFacePowered(final int i, final int j, final int k, final int l) {
        final int i2 = this.getTypeId(i, j, k);
        return i2 != 0 && Block.byId[i2].d(this, i, j, k, l);
    }
    
    public boolean isBlockPowered(final int i, final int j, final int k) {
        return this.isBlockFacePowered(i, j - 1, k, 0) || this.isBlockFacePowered(i, j + 1, k, 1) || this.isBlockFacePowered(i, j, k - 1, 2) || this.isBlockFacePowered(i, j, k + 1, 3) || this.isBlockFacePowered(i - 1, j, k, 4) || this.isBlockFacePowered(i + 1, j, k, 5);
    }
    
    public boolean isBlockFaceIndirectlyPowered(final int i, final int j, final int k, final int l) {
        if (this.e(i, j, k)) {
            return this.isBlockPowered(i, j, k);
        }
        final int i2 = this.getTypeId(i, j, k);
        return i2 != 0 && Block.byId[i2].a((IBlockAccess)this, i, j, k, l);
    }
    
    public boolean isBlockIndirectlyPowered(final int i, final int j, final int k) {
        return this.isBlockFaceIndirectlyPowered(i, j - 1, k, 0) || this.isBlockFaceIndirectlyPowered(i, j + 1, k, 1) || this.isBlockFaceIndirectlyPowered(i, j, k - 1, 2) || this.isBlockFaceIndirectlyPowered(i, j, k + 1, 3) || this.isBlockFaceIndirectlyPowered(i - 1, j, k, 4) || this.isBlockFaceIndirectlyPowered(i + 1, j, k, 5);
    }
    
    public EntityHuman findNearbyPlayer(final Entity entity, final double d0) {
        return this.a(entity.locX, entity.locY, entity.locZ, d0);
    }
    
    public EntityHuman a(final double d0, final double d1, final double d2, final double d3) {
        double d4 = -1.0;
        EntityHuman entityhuman = null;
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityHuman entityhuman2 = this.players.get(i);
            if (entityhuman2 != null) {
                if (!entityhuman2.dead) {
                    final double d5 = entityhuman2.e(d0, d1, d2);
                    if ((d3 < 0.0 || d5 < d3 * d3) && (d4 == -1.0 || d5 < d4)) {
                        d4 = d5;
                        entityhuman = entityhuman2;
                    }
                }
            }
        }
        return entityhuman;
    }
    
    public EntityHuman a(final String s) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (s.equals(this.players.get(i).name)) {
                return this.players.get(i);
            }
        }
        return null;
    }
    
    public byte[] getMultiChunkData(final int i, final int j, final int k, final int l, final int i1, final int j1) {
        final byte[] abyte = new byte[l * i1 * j1 * 5 / 2];
        final int k2 = i >> 4;
        final int l2 = k >> 4;
        final int i2 = i + l - 1 >> 4;
        final int j2 = k + j1 - 1 >> 4;
        int k3 = 0;
        int l3 = j;
        int i3 = j + i1;
        if (j < 0) {
            l3 = 0;
        }
        if (i3 > 128) {
            i3 = 128;
        }
        for (int j3 = k2; j3 <= i2; ++j3) {
            int k4 = i - j3 * 16;
            int l4 = i + l - j3 * 16;
            if (k4 < 0) {
                k4 = 0;
            }
            if (l4 > 16) {
                l4 = 16;
            }
            for (int i4 = l2; i4 <= j2; ++i4) {
                int j4 = k - i4 * 16;
                int k5 = k + j1 - i4 * 16;
                if (j4 < 0) {
                    j4 = 0;
                }
                if (k5 > 16) {
                    k5 = 16;
                }
                k3 = this.getChunkAt(j3, i4).getData(abyte, k4, l3, j4, l4, i3, k5, k3);
            }
        }
        return abyte;
    }
    
    public void k() {
        this.w.b();
    }
    
    public void setTime(final long i) {
        this.worldData.a(i);
    }
    
    public void setTimeAndFixTicklists(final long i) {
        final long j = i - this.worldData.f();
        for (final NextTickListEntry nextTickListEntry : this.F) {
            final NextTickListEntry nextticklistentry = nextTickListEntry;
            nextTickListEntry.e += j;
        }
        this.setTime(i);
    }
    
    public long getSeed() {
        return this.worldData.getSeed();
    }
    
    public long getTime() {
        return this.worldData.f();
    }
    
    public ChunkCoordinates getSpawn() {
        return new ChunkCoordinates(this.worldData.c(), this.worldData.d(), this.worldData.e());
    }
    
    public boolean a(final EntityHuman entityhuman, final int i, final int j, final int k) {
        return true;
    }
    
    public void a(final Entity entity, final byte b0) {
    }
    
    public IChunkProvider o() {
        return this.chunkProvider;
    }
    
    public void playNote(final int i, final int j, final int k, final int l, final int i1) {
        final int j2 = this.getTypeId(i, j, k);
        if (j2 > 0) {
            Block.byId[j2].a(this, i, j, k, l, i1);
        }
    }
    
    public IDataManager p() {
        return this.w;
    }
    
    public WorldData q() {
        return this.worldData;
    }
    
    public void everyoneSleeping() {
        this.J = !this.players.isEmpty();
        for (final EntityHuman entityhuman : this.players) {
            if (!entityhuman.isSleeping() && !entityhuman.fauxSleeping) {
                this.J = false;
                break;
            }
        }
    }
    
    public void checkSleepStatus() {
        if (!this.isStatic) {
            this.everyoneSleeping();
        }
    }
    
    protected void s() {
        this.J = false;
        for (final EntityHuman entityhuman : this.players) {
            if (entityhuman.isSleeping()) {
                entityhuman.a(false, false, true);
            }
        }
        this.y();
    }
    
    public boolean everyoneDeeplySleeping() {
        if (this.J && !this.isStatic) {
            final Iterator iterator = this.players.iterator();
            boolean foundActualSleepers = false;
            while (iterator.hasNext()) {
                final EntityHuman entityhuman = iterator.next();
                if (entityhuman.isDeeplySleeping()) {
                    foundActualSleepers = true;
                }
                if (!entityhuman.isDeeplySleeping() && !entityhuman.fauxSleeping) {
                    return false;
                }
            }
            return foundActualSleepers;
        }
        return false;
    }
    
    public float c(final float f) {
        return (this.k + (this.l - this.k) * f) * this.d(f);
    }
    
    public float d(final float f) {
        return this.i + (this.j - this.i) * f;
    }
    
    public boolean u() {
        return this.c(1.0f) > 0.9;
    }
    
    public boolean v() {
        return this.d(1.0f) > 0.2;
    }
    
    public boolean s(final int i, final int j, final int k) {
        if (!this.v()) {
            return false;
        }
        if (!this.isChunkLoaded(i, j, k)) {
            return false;
        }
        if (this.e(i, k) > j) {
            return false;
        }
        final BiomeBase biomebase = this.getWorldChunkManager().getBiome(i, k);
        return !biomebase.c() && biomebase.d();
    }
    
    public void a(final String s, final WorldMapBase worldmapbase) {
        this.worldMaps.a(s, worldmapbase);
    }
    
    public WorldMapBase a(final Class oclass, final String s) {
        return this.worldMaps.a(oclass, s);
    }
    
    public int b(final String s) {
        return this.worldMaps.a(s);
    }
    
    public void e(final int i, final int j, final int k, final int l, final int i1) {
        this.a(null, i, j, k, l, i1);
    }
    
    public void a(final EntityHuman entityhuman, final int i, final int j, final int k, final int l, final int i1) {
        for (int j2 = 0; j2 < this.u.size(); ++j2) {
            this.u.get(j2).a(entityhuman, i, j, k, l, i1);
        }
    }
    
    public UUID getUUID() {
        return this.w.getUUID();
    }
    
    static {
        World.A = 0;
    }
}
