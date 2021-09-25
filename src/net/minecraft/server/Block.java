// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import net.minecraft.server.forge.ForgeHooks;
import java.util.Random;
import java.util.ArrayList;

public class Block
{
    public static final StepSound d;
    public static final StepSound e;
    public static final StepSound f;
    public static final StepSound g;
    public static final StepSound h;
    public static final StepSound i;
    public static final StepSound j;
    public static final StepSound k;
    public static final StepSound l;
    public static final Block[] byId;
    public static final boolean[] n;
    public static final boolean[] o;
    public static final boolean[] isTileEntity;
    public static final int[] q;
    public static final boolean[] r;
    public static final int[] s;
    public static final boolean[] t;
    public static final Block STONE;
    public static final BlockGrass GRASS;
    public static final Block DIRT;
    public static final Block COBBLESTONE;
    public static final Block WOOD;
    public static final Block SAPLING;
    public static final Block BEDROCK;
    public static final Block WATER;
    public static final Block STATIONARY_WATER;
    public static final Block LAVA;
    public static final Block STATIONARY_LAVA;
    public static final Block SAND;
    public static final Block GRAVEL;
    public static final Block GOLD_ORE;
    public static final Block IRON_ORE;
    public static final Block COAL_ORE;
    public static final Block LOG;
    public static final BlockLeaves LEAVES;
    public static final Block SPONGE;
    public static final Block GLASS;
    public static final Block LAPIS_ORE;
    public static final Block LAPIS_BLOCK;
    public static final Block DISPENSER;
    public static final Block SANDSTONE;
    public static final Block NOTE_BLOCK;
    public static final Block BED;
    public static final Block GOLDEN_RAIL;
    public static final Block DETECTOR_RAIL;
    public static final Block PISTON_STICKY;
    public static final Block WEB;
    public static final BlockLongGrass LONG_GRASS;
    public static final BlockDeadBush DEAD_BUSH;
    public static final Block PISTON;
    public static final BlockPistonExtension PISTON_EXTENSION;
    public static final Block WOOL;
    public static final BlockPistonMoving PISTON_MOVING;
    public static final BlockFlower YELLOW_FLOWER;
    public static final BlockFlower RED_ROSE;
    public static final BlockFlower BROWN_MUSHROOM;
    public static final BlockFlower RED_MUSHROOM;
    public static final Block GOLD_BLOCK;
    public static final Block IRON_BLOCK;
    public static final Block DOUBLE_STEP;
    public static final Block STEP;
    public static final Block BRICK;
    public static final Block TNT;
    public static final Block BOOKSHELF;
    public static final Block MOSSY_COBBLESTONE;
    public static final Block OBSIDIAN;
    public static final Block TORCH;
    public static final BlockFire FIRE;
    public static final Block MOB_SPAWNER;
    public static final Block WOOD_STAIRS;
    public static final Block CHEST;
    public static final Block REDSTONE_WIRE;
    public static final Block DIAMOND_ORE;
    public static final Block DIAMOND_BLOCK;
    public static final Block WORKBENCH;
    public static final Block CROPS;
    public static final Block SOIL;
    public static final Block FURNACE;
    public static final Block BURNING_FURNACE;
    public static final Block SIGN_POST;
    public static final Block WOODEN_DOOR;
    public static final Block LADDER;
    public static final Block RAILS;
    public static final Block COBBLESTONE_STAIRS;
    public static final Block WALL_SIGN;
    public static final Block LEVER;
    public static final Block STONE_PLATE;
    public static final Block IRON_DOOR_BLOCK;
    public static final Block WOOD_PLATE;
    public static final Block REDSTONE_ORE;
    public static final Block GLOWING_REDSTONE_ORE;
    public static final Block REDSTONE_TORCH_OFF;
    public static final Block REDSTONE_TORCH_ON;
    public static final Block STONE_BUTTON;
    public static final Block SNOW;
    public static final Block ICE;
    public static final Block SNOW_BLOCK;
    public static final Block CACTUS;
    public static final Block CLAY;
    public static final Block SUGAR_CANE_BLOCK;
    public static final Block JUKEBOX;
    public static final Block FENCE;
    public static final Block PUMPKIN;
    public static final Block NETHERRACK;
    public static final Block SOUL_SAND;
    public static final Block GLOWSTONE;
    public static final BlockPortal PORTAL;
    public static final Block JACK_O_LANTERN;
    public static final Block CAKE_BLOCK;
    public static final Block DIODE_OFF;
    public static final Block DIODE_ON;
    public static final Block LOCKED_CHEST;
    public static final Block TRAP_DOOR;
    public int textureId;
    public final int id;
    protected float strength;
    protected float durability;
    protected boolean bq;
    protected boolean br;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    public StepSound stepSound;
    public float bz;
    public final Material material;
    public float frictionFactor;
    private String name;
    
    protected Block(final int i, final Material material) {
        this.bq = true;
        this.br = true;
        this.stepSound = Block.d;
        this.bz = 1.0f;
        this.frictionFactor = 0.6f;
        if (Block.byId[i] != null) {
            throw new IllegalArgumentException("Slot " + i + " is already occupied by " + Block.byId[i] + " when adding " + this);
        }
        this.material = material;
        Block.byId[i] = this;
        this.id = i;
        this.a(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        Block.o[i] = this.a();
        Block.q[i] = (this.a() ? 255 : 0);
        Block.r[i] = !material.blocksLight();
        Block.isTileEntity[i] = false;
        org.bukkit.Material.addMaterial(i);
    }
    
    protected Block g() {
        Block.t[this.id] = true;
        return this;
    }
    
    protected void h() {
    }
    
    protected Block(final int i, final int j, final Material material) {
        this(i, material);
        this.textureId = j;
    }
    
    protected Block a(final StepSound stepsound) {
        this.stepSound = stepsound;
        return this;
    }
    
    protected Block f(final int i) {
        Block.q[this.id] = i;
        return this;
    }
    
    protected Block a(final float f) {
        Block.s[this.id] = (int)(15.0f * f);
        return this;
    }
    
    protected Block b(final float f) {
        this.durability = f * 3.0f;
        return this;
    }
    
    public boolean b() {
        return true;
    }
    
    protected Block c(final float f) {
        this.strength = f;
        if (this.durability < f * 5.0f) {
            this.durability = f * 5.0f;
        }
        return this;
    }
    
    protected Block i() {
        this.c(-1.0f);
        return this;
    }
    
    public float j() {
        return this.strength;
    }
    
    protected Block a(final boolean flag) {
        Block.n[this.id] = flag;
        return this;
    }
    
    public void a(final float f, final float f1, final float f2, final float f3, final float f4, final float f5) {
        this.minX = f;
        this.minY = f1;
        this.minZ = f2;
        this.maxX = f3;
        this.maxY = f4;
        this.maxZ = f5;
    }
    
    public boolean b(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int l) {
        return iblockaccess.getMaterial(i, j, k).isBuildable();
    }
    
    public int a(final int i, final int j) {
        return this.a(i);
    }
    
    public int a(final int i) {
        return this.textureId;
    }
    
    public void a(final World world, final int i, final int j, final int k, final AxisAlignedBB axisalignedbb, final ArrayList arraylist) {
        final AxisAlignedBB axisalignedbb2 = this.e(world, i, j, k);
        if (axisalignedbb2 != null && axisalignedbb.a(axisalignedbb2)) {
            arraylist.add(axisalignedbb2);
        }
    }
    
    public AxisAlignedBB e(final World world, final int i, final int j, final int k) {
        return AxisAlignedBB.b(i + this.minX, j + this.minY, k + this.minZ, i + this.maxX, j + this.maxY, k + this.maxZ);
    }
    
    public boolean a() {
        return true;
    }
    
    public boolean a(final int i, final boolean flag) {
        return this.k_();
    }
    
    public boolean k_() {
        return true;
    }
    
    public void a(final World world, final int i, final int j, final int k, final Random random) {
    }
    
    public void postBreak(final World world, final int i, final int j, final int k, final int l) {
    }
    
    public void doPhysics(final World world, final int i, final int j, final int k, final int l) {
    }
    
    public int c() {
        return 10;
    }
    
    public void c(final World world, final int i, final int j, final int k) {
    }
    
    public void remove(final World world, final int i, final int j, final int k) {
    }
    
    public int a(final Random random) {
        return 1;
    }
    
    public int a(final int i, final Random random) {
        return this.id;
    }
    
    public float getDamage(final EntityHuman entityhuman) {
        return this.blockStrength(entityhuman, 0);
    }
    
    public final void g(final World world, final int i, final int j, final int k, final int l) {
        this.dropNaturally(world, i, j, k, l, 1.0f);
    }
    
    public void dropNaturally(final World world, final int i, final int j, final int k, final int l, final float f) {
        if (!world.isStatic) {
            for (int i2 = this.a(world.random), j2 = 0; j2 < i2; ++j2) {
                if (world.random.nextFloat() < f) {
                    final int k2 = this.a(l, world.random);
                    if (k2 > 0) {
                        this.a(world, i, j, k, new ItemStack(k2, 1, this.a_(l)));
                    }
                }
            }
        }
    }
    
    protected void a(final World world, final int i, final int j, final int k, final ItemStack itemstack) {
        if (!world.isStatic) {
            final float f = 0.7f;
            final double d0 = world.random.nextFloat() * f + (1.0f - f) * 0.5;
            final double d2 = world.random.nextFloat() * f + (1.0f - f) * 0.5;
            final double d3 = world.random.nextFloat() * f + (1.0f - f) * 0.5;
            final EntityItem entityitem = new EntityItem(world, i + d0, j + d2, k + d3, itemstack);
            entityitem.pickupDelay = 10;
            world.addEntity((Entity)entityitem);
        }
    }
    
    protected int a_(final int i) {
        return 0;
    }
    
    public float a(final Entity entity) {
        return this.durability / 5.0f;
    }
    
    public MovingObjectPosition a(final World world, final int i, final int j, final int k, Vec3D vec3d, Vec3D vec3d1) {
        this.a((IBlockAccess)world, i, j, k);
        vec3d = vec3d.add((double)(-i), (double)(-j), (double)(-k));
        vec3d1 = vec3d1.add((double)(-i), (double)(-j), (double)(-k));
        Vec3D vec3d2 = vec3d.a(vec3d1, this.minX);
        Vec3D vec3d3 = vec3d.a(vec3d1, this.maxX);
        Vec3D vec3d4 = vec3d.b(vec3d1, this.minY);
        Vec3D vec3d5 = vec3d.b(vec3d1, this.maxY);
        Vec3D vec3d6 = vec3d.c(vec3d1, this.minZ);
        Vec3D vec3d7 = vec3d.c(vec3d1, this.maxZ);
        if (!this.a(vec3d2)) {
            vec3d2 = null;
        }
        if (!this.a(vec3d3)) {
            vec3d3 = null;
        }
        if (!this.b(vec3d4)) {
            vec3d4 = null;
        }
        if (!this.b(vec3d5)) {
            vec3d5 = null;
        }
        if (!this.c(vec3d6)) {
            vec3d6 = null;
        }
        if (!this.c(vec3d7)) {
            vec3d7 = null;
        }
        Vec3D vec3d8 = null;
        if (vec3d2 != null && (vec3d8 == null || vec3d.a(vec3d2) < vec3d.a(vec3d8))) {
            vec3d8 = vec3d2;
        }
        if (vec3d3 != null && (vec3d8 == null || vec3d.a(vec3d3) < vec3d.a(vec3d8))) {
            vec3d8 = vec3d3;
        }
        if (vec3d4 != null && (vec3d8 == null || vec3d.a(vec3d4) < vec3d.a(vec3d8))) {
            vec3d8 = vec3d4;
        }
        if (vec3d5 != null && (vec3d8 == null || vec3d.a(vec3d5) < vec3d.a(vec3d8))) {
            vec3d8 = vec3d5;
        }
        if (vec3d6 != null && (vec3d8 == null || vec3d.a(vec3d6) < vec3d.a(vec3d8))) {
            vec3d8 = vec3d6;
        }
        if (vec3d7 != null && (vec3d8 == null || vec3d.a(vec3d7) < vec3d.a(vec3d8))) {
            vec3d8 = vec3d7;
        }
        if (vec3d8 == null) {
            return null;
        }
        byte b0 = -1;
        if (vec3d8 == vec3d2) {
            b0 = 4;
        }
        if (vec3d8 == vec3d3) {
            b0 = 5;
        }
        if (vec3d8 == vec3d4) {
            b0 = 0;
        }
        if (vec3d8 == vec3d5) {
            b0 = 1;
        }
        if (vec3d8 == vec3d6) {
            b0 = 2;
        }
        if (vec3d8 == vec3d7) {
            b0 = 3;
        }
        return new MovingObjectPosition(i, j, k, (int)b0, vec3d8.add((double)i, (double)j, (double)k));
    }
    
    private boolean a(final Vec3D vec3d) {
        return vec3d != null && (vec3d.b >= this.minY && vec3d.b <= this.maxY && vec3d.c >= this.minZ && vec3d.c <= this.maxZ);
    }
    
    private boolean b(final Vec3D vec3d) {
        return vec3d != null && (vec3d.a >= this.minX && vec3d.a <= this.maxX && vec3d.c >= this.minZ && vec3d.c <= this.maxZ);
    }
    
    private boolean c(final Vec3D vec3d) {
        return vec3d != null && (vec3d.a >= this.minX && vec3d.a <= this.maxX && vec3d.b >= this.minY && vec3d.b <= this.maxY);
    }
    
    public void d(final World world, final int i, final int j, final int k) {
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k, final int l) {
        return this.canPlace(world, i, j, k);
    }
    
    public boolean canPlace(final World world, final int i, final int j, final int k) {
        final int l = world.getTypeId(i, j, k);
        return l == 0 || Block.byId[l].material.isReplacable();
    }
    
    public boolean interact(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
        return false;
    }
    
    public void b(final World world, final int i, final int j, final int k, final Entity entity) {
    }
    
    public void postPlace(final World world, final int i, final int j, final int k, final int l) {
    }
    
    public void b(final World world, final int i, final int j, final int k, final EntityHuman entityhuman) {
    }
    
    public void a(final World world, final int i, final int j, final int k, final Entity entity, final Vec3D vec3d) {
    }
    
    public void a(final IBlockAccess iblockaccess, final int i, final int j, final int k) {
    }
    
    public boolean a(final IBlockAccess iblockaccess, final int i, final int j, final int k, final int l) {
        return false;
    }
    
    public boolean isPowerSource() {
        return false;
    }
    
    public void a(final World world, final int i, final int j, final int k, final Entity entity) {
    }
    
    public boolean d(final World world, final int i, final int j, final int k, final int l) {
        return false;
    }
    
    public void a(final World world, final EntityHuman entityhuman, final int i, final int j, final int k, final int l) {
        entityhuman.a(StatisticList.C[this.id], 1);
        this.g(world, i, j, k, l);
    }
    
    public boolean f(final World world, final int i, final int j, final int k) {
        return true;
    }
    
    public void postPlace(final World world, final int i, final int j, final int k, final EntityLiving entityliving) {
    }
    
    public Block a(final String s) {
        this.name = "tile." + s;
        org.bukkit.Material.setMaterialName(this.id, s);
        return this;
    }
    
    public String k() {
        return StatisticCollector.a(this.l() + ".name");
    }
    
    public String l() {
        return this.name;
    }
    
    public void a(final World world, final int i, final int j, final int k, final int l, final int i1) {
    }
    
    public boolean m() {
        return this.br;
    }
    
    protected Block n() {
        this.br = false;
        return this;
    }
    
    public int e() {
        return this.material.j();
    }
    
    public boolean isLadder() {
        return false;
    }
    
    public boolean isBlockNormalCube(final World world, final int i, final int j, final int k) {
        return this.material.h() && this.b();
    }
    
    public boolean isBlockSolidOnSide(final World world, final int i, final int j, final int k, final int side) {
        return this.isBlockNormalCube(world, i, j, k);
    }
    
    public boolean isBlockReplaceable(final World world, final int i, final int j, final int k) {
        return false;
    }
    
    public boolean isBlockBurning(final World world, final int i, final int j, final int k) {
        return false;
    }
    
    public boolean isAirBlock(final World world, final int i, final int j, final int k) {
        return false;
    }
    
    public float getHardness(final int md) {
        return this.strength;
    }
    
    public float blockStrength(final World world, final EntityHuman player, final int i, final int j, final int k) {
        final int md = world.getData(i, j, k);
        return this.blockStrength(player, md);
    }
    
    public float blockStrength(final EntityHuman player, final int md) {
        return ForgeHooks.blockStrength(this, player, md);
    }
    
    public boolean canHarvestBlock(final EntityHuman player, final int md) {
        return ForgeHooks.canHarvestBlock(this, player, md);
    }
    
    static {
        Block.d = new StepSound("stone", 1.0f, 1.0f);
        Block.e = new StepSound("wood", 1.0f, 1.0f);
        Block.f = new StepSound("gravel", 1.0f, 1.0f);
        Block.g = new StepSound("grass", 1.0f, 1.0f);
        Block.h = new StepSound("stone", 1.0f, 1.0f);
        Block.i = new StepSound("stone", 1.0f, 1.5f);
        Block.j = (StepSound)new StepSoundStone("stone", 1.0f, 1.0f);
        Block.k = new StepSound("cloth", 1.0f, 1.0f);
        Block.l = (StepSound)new StepSoundSand("sand", 1.0f, 1.0f);
        Block.byId = new Block[256];
        Block.n = new boolean[256];
        Block.o = new boolean[256];
        Block.isTileEntity = new boolean[256];
        Block.q = new int[256];
        Block.r = new boolean[256];
        Block.s = new int[256];
        Block.t = new boolean[256];
        Block.STONE = new BlockStone(1, 1).c(1.5f).b(10.0f).a(Block.h).a("stone");
        Block.GRASS = (BlockGrass)new BlockGrass(2).c(0.6f).a(Block.g).a("grass");
        Block.DIRT = new BlockDirt(3, 2).c(0.5f).a(Block.f).a("dirt");
        Block.COBBLESTONE = new Block(4, 16, Material.STONE).c(2.0f).b(10.0f).a(Block.h).a("stonebrick");
        Block.WOOD = new Block(5, 4, Material.WOOD).c(2.0f).b(5.0f).a(Block.e).a("wood").g();
        Block.SAPLING = new BlockSapling(6, 15).c(0.0f).a(Block.g).a("sapling").g();
        Block.BEDROCK = new Block(7, 17, Material.STONE).i().b(6000000.0f).a(Block.h).a("bedrock").n();
        Block.WATER = new BlockFlowing(8, Material.WATER).c(100.0f).f(3).a("water").n().g();
        Block.STATIONARY_WATER = new BlockStationary(9, Material.WATER).c(100.0f).f(3).a("water").n().g();
        Block.LAVA = new BlockFlowing(10, Material.LAVA).c(0.0f).a(1.0f).f(255).a("lava").n().g();
        Block.STATIONARY_LAVA = new BlockStationary(11, Material.LAVA).c(100.0f).a(1.0f).f(255).a("lava").n().g();
        Block.SAND = new BlockSand(12, 18).c(0.5f).a(Block.l).a("sand");
        Block.GRAVEL = new BlockGravel(13, 19).c(0.6f).a(Block.f).a("gravel");
        Block.GOLD_ORE = new BlockOre(14, 32).c(3.0f).b(5.0f).a(Block.h).a("oreGold");
        Block.IRON_ORE = new BlockOre(15, 33).c(3.0f).b(5.0f).a(Block.h).a("oreIron");
        Block.COAL_ORE = new BlockOre(16, 34).c(3.0f).b(5.0f).a(Block.h).a("oreCoal");
        Block.LOG = new BlockLog(17).c(2.0f).a(Block.e).a("log").g();
        Block.LEAVES = (BlockLeaves)new BlockLeaves(18, 52).c(0.2f).f(1).a(Block.g).a("leaves").n().g();
        Block.SPONGE = new BlockSponge(19).c(0.6f).a(Block.g).a("sponge");
        Block.GLASS = new BlockGlass(20, 49, Material.SHATTERABLE, false).c(0.3f).a(Block.j).a("glass");
        Block.LAPIS_ORE = new BlockOre(21, 160).c(3.0f).b(5.0f).a(Block.h).a("oreLapis");
        Block.LAPIS_BLOCK = new Block(22, 144, Material.STONE).c(3.0f).b(5.0f).a(Block.h).a("blockLapis");
        Block.DISPENSER = new BlockDispenser(23).c(3.5f).a(Block.h).a("dispenser").g();
        Block.SANDSTONE = new BlockSandStone(24).a(Block.h).c(0.8f).a("sandStone");
        Block.NOTE_BLOCK = new BlockNote(25).c(0.8f).a("musicBlock").g();
        Block.BED = new BlockBed(26).c(0.2f).a("bed").n().g();
        Block.GOLDEN_RAIL = new BlockMinecartTrack(27, 179, true).c(0.7f).a(Block.i).a("goldenRail").g();
        Block.DETECTOR_RAIL = new BlockMinecartDetector(28, 195).c(0.7f).a(Block.i).a("detectorRail").g();
        Block.PISTON_STICKY = new BlockPiston(29, 106, true).a("pistonStickyBase").g();
        Block.WEB = new BlockWeb(30, 11).f(1).c(4.0f).a("web");
        Block.LONG_GRASS = (BlockLongGrass)new BlockLongGrass(31, 39).c(0.0f).a(Block.g).a("tallgrass");
        Block.DEAD_BUSH = (BlockDeadBush)new BlockDeadBush(32, 55).c(0.0f).a(Block.g).a("deadbush");
        Block.PISTON = new BlockPiston(33, 107, false).a("pistonBase").g();
        Block.PISTON_EXTENSION = (BlockPistonExtension)new BlockPistonExtension(34, 107).g();
        Block.WOOL = new BlockCloth().c(0.8f).a(Block.k).a("cloth").g();
        Block.PISTON_MOVING = new BlockPistonMoving(36);
        Block.YELLOW_FLOWER = (BlockFlower)new BlockFlower(37, 13).c(0.0f).a(Block.g).a("flower");
        Block.RED_ROSE = (BlockFlower)new BlockFlower(38, 12).c(0.0f).a(Block.g).a("rose");
        Block.BROWN_MUSHROOM = (BlockFlower)new BlockMushroom(39, 29).c(0.0f).a(Block.g).a(0.125f).a("mushroom");
        Block.RED_MUSHROOM = (BlockFlower)new BlockMushroom(40, 28).c(0.0f).a(Block.g).a("mushroom");
        Block.GOLD_BLOCK = new BlockOreBlock(41, 23).c(3.0f).b(10.0f).a(Block.i).a("blockGold");
        Block.IRON_BLOCK = new BlockOreBlock(42, 22).c(5.0f).b(10.0f).a(Block.i).a("blockIron");
        Block.DOUBLE_STEP = new BlockStep(43, true).c(2.0f).b(10.0f).a(Block.h).a("stoneSlab");
        Block.STEP = new BlockStep(44, false).c(2.0f).b(10.0f).a(Block.h).a("stoneSlab");
        Block.BRICK = new Block(45, 7, Material.STONE).c(2.0f).b(10.0f).a(Block.h).a("brick");
        Block.TNT = new BlockTNT(46, 8).c(0.0f).a(Block.g).a("tnt");
        Block.BOOKSHELF = new BlockBookshelf(47, 35).c(1.5f).a(Block.e).a("bookshelf");
        Block.MOSSY_COBBLESTONE = new Block(48, 36, Material.STONE).c(2.0f).b(10.0f).a(Block.h).a("stoneMoss");
        Block.OBSIDIAN = new BlockObsidian(49, 37).c(10.0f).b(2000.0f).a(Block.h).a("obsidian");
        Block.TORCH = new BlockTorch(50, 80).c(0.0f).a(0.9375f).a(Block.e).a("torch").g();
        Block.FIRE = (BlockFire)new BlockFire(51, 31).c(0.0f).a(1.0f).a(Block.e).a("fire").n().g();
        Block.MOB_SPAWNER = new BlockMobSpawner(52, 65).c(5.0f).a(Block.i).a("mobSpawner").n();
        Block.WOOD_STAIRS = new BlockStairs(53, Block.WOOD).a("stairsWood").g();
        Block.CHEST = new BlockChest(54).c(2.5f).a(Block.e).a("chest").g();
        Block.REDSTONE_WIRE = new BlockRedstoneWire(55, 164).c(0.0f).a(Block.d).a("redstoneDust").n().g();
        Block.DIAMOND_ORE = new BlockOre(56, 50).c(3.0f).b(5.0f).a(Block.h).a("oreDiamond");
        Block.DIAMOND_BLOCK = new BlockOreBlock(57, 24).c(5.0f).b(10.0f).a(Block.i).a("blockDiamond");
        Block.WORKBENCH = new BlockWorkbench(58).c(2.5f).a(Block.e).a("workbench");
        Block.CROPS = new BlockCrops(59, 88).c(0.0f).a(Block.g).a("crops").n().g();
        Block.SOIL = new BlockSoil(60).c(0.6f).a(Block.f).a("farmland");
        Block.FURNACE = new BlockFurnace(61, false).c(3.5f).a(Block.h).a("furnace").g();
        Block.BURNING_FURNACE = new BlockFurnace(62, true).c(3.5f).a(Block.h).a(0.875f).a("furnace").g();
        Block.SIGN_POST = new BlockSign(63, (Class)TileEntitySign.class, true).c(1.0f).a(Block.e).a("sign").n().g();
        Block.WOODEN_DOOR = new BlockDoor(64, Material.WOOD).c(3.0f).a(Block.e).a("doorWood").n().g();
        Block.LADDER = new BlockLadder(65, 83).c(0.4f).a(Block.e).a("ladder").g();
        Block.RAILS = new BlockMinecartTrack(66, 128, false).c(0.7f).a(Block.i).a("rail").g();
        Block.COBBLESTONE_STAIRS = new BlockStairs(67, Block.COBBLESTONE).a("stairsStone").g();
        Block.WALL_SIGN = new BlockSign(68, (Class)TileEntitySign.class, false).c(1.0f).a(Block.e).a("sign").n().g();
        Block.LEVER = new BlockLever(69, 96).c(0.5f).a(Block.e).a("lever").g();
        Block.STONE_PLATE = new BlockPressurePlate(70, Block.STONE.textureId, EnumMobType.MOBS, Material.STONE).c(0.5f).a(Block.h).a("pressurePlate").g();
        Block.IRON_DOOR_BLOCK = new BlockDoor(71, Material.ORE).c(5.0f).a(Block.i).a("doorIron").n().g();
        Block.WOOD_PLATE = new BlockPressurePlate(72, Block.WOOD.textureId, EnumMobType.EVERYTHING, Material.WOOD).c(0.5f).a(Block.e).a("pressurePlate").g();
        Block.REDSTONE_ORE = new BlockRedstoneOre(73, 51, false).c(3.0f).b(5.0f).a(Block.h).a("oreRedstone").g();
        Block.GLOWING_REDSTONE_ORE = new BlockRedstoneOre(74, 51, true).a(0.625f).c(3.0f).b(5.0f).a(Block.h).a("oreRedstone").g();
        Block.REDSTONE_TORCH_OFF = new BlockRedstoneTorch(75, 115, false).c(0.0f).a(Block.e).a("notGate").g();
        Block.REDSTONE_TORCH_ON = new BlockRedstoneTorch(76, 99, true).c(0.0f).a(0.5f).a(Block.e).a("notGate").g();
        Block.STONE_BUTTON = new BlockButton(77, Block.STONE.textureId).c(0.5f).a(Block.h).a("button").g();
        Block.SNOW = new BlockSnow(78, 66).c(0.1f).a(Block.k).a("snow");
        Block.ICE = new BlockIce(79, 67).c(0.5f).f(3).a(Block.j).a("ice");
        Block.SNOW_BLOCK = new BlockSnowBlock(80, 66).c(0.2f).a(Block.k).a("snow");
        Block.CACTUS = new BlockCactus(81, 70).c(0.4f).a(Block.k).a("cactus");
        Block.CLAY = new BlockClay(82, 72).c(0.6f).a(Block.f).a("clay");
        Block.SUGAR_CANE_BLOCK = new BlockReed(83, 73).c(0.0f).a(Block.g).a("reeds").n();
        Block.JUKEBOX = new BlockJukeBox(84, 74).c(2.0f).b(10.0f).a(Block.h).a("jukebox").g();
        Block.FENCE = new BlockFence(85, 4).c(2.0f).b(5.0f).a(Block.e).a("fence").g();
        Block.PUMPKIN = new BlockPumpkin(86, 102, false).c(1.0f).a(Block.e).a("pumpkin").g();
        Block.NETHERRACK = new BlockBloodStone(87, 103).c(0.4f).a(Block.h).a("hellrock");
        Block.SOUL_SAND = new BlockSlowSand(88, 104).c(0.5f).a(Block.l).a("hellsand");
        Block.GLOWSTONE = new BlockLightStone(89, 105, Material.STONE).c(0.3f).a(Block.j).a(1.0f).a("lightgem");
        Block.PORTAL = (BlockPortal)new BlockPortal(90, 14).c(-1.0f).a(Block.j).a(0.75f).a("portal");
        Block.JACK_O_LANTERN = new BlockPumpkin(91, 102, true).c(1.0f).a(Block.e).a(1.0f).a("litpumpkin").g();
        Block.CAKE_BLOCK = new BlockCake(92, 121).c(0.5f).a(Block.k).a("cake").n().g();
        Block.DIODE_OFF = new BlockDiode(93, false).c(0.0f).a(Block.e).a("diode").n().g();
        Block.DIODE_ON = new BlockDiode(94, true).c(0.0f).a(0.625f).a(Block.e).a("diode").n().g();
        Block.LOCKED_CHEST = new BlockLockedChest(95).c(0.0f).a(1.0f).a(Block.e).a("lockedchest").a(true).g();
        Block.TRAP_DOOR = new BlockTrapdoor(96, Material.WOOD).c(3.0f).a(Block.e).a("trapdoor").n().g();
        Item.byId[Block.WOOL.id] = new ItemCloth(Block.WOOL.id - 256).a("cloth");
        Item.byId[Block.LOG.id] = new ItemLog(Block.LOG.id - 256).a("log");
        Item.byId[Block.STEP.id] = new ItemStep(Block.STEP.id - 256).a("stoneSlab");
        Item.byId[Block.SAPLING.id] = new ItemSapling(Block.SAPLING.id - 256).a("sapling");
        Item.byId[Block.LEAVES.id] = new ItemLeaves(Block.LEAVES.id - 256).a("leaves");
        Item.byId[Block.PISTON.id] = (Item)new ItemPiston(Block.PISTON.id - 256);
        Item.byId[Block.PISTON_STICKY.id] = (Item)new ItemPiston(Block.PISTON_STICKY.id - 256);
        for (int m = 0; m < 256; ++m) {
            if (Block.byId[m] != null && Item.byId[m] == null) {
                Item.byId[m] = (Item)new ItemBlock(m - 256);
                Block.byId[m].h();
            }
        }
        Block.r[0] = true;
        StatisticList.b();
    }
}
