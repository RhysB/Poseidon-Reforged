// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import org.bukkit.Material;
import java.util.Random;

public class Item
{
    protected static Random b;
    public static Item[] byId;
    public static Item IRON_SPADE;
    public static Item IRON_PICKAXE;
    public static Item IRON_AXE;
    public static Item FLINT_AND_STEEL;
    public static Item APPLE;
    public static Item BOW;
    public static Item ARROW;
    public static Item COAL;
    public static Item DIAMOND;
    public static Item IRON_INGOT;
    public static Item GOLD_INGOT;
    public static Item IRON_SWORD;
    public static Item WOOD_SWORD;
    public static Item WOOD_SPADE;
    public static Item WOOD_PICKAXE;
    public static Item WOOD_AXE;
    public static Item STONE_SWORD;
    public static Item STONE_SPADE;
    public static Item STONE_PICKAXE;
    public static Item STONE_AXE;
    public static Item DIAMOND_SWORD;
    public static Item DIAMOND_SPADE;
    public static Item DIAMOND_PICKAXE;
    public static Item DIAMOND_AXE;
    public static Item STICK;
    public static Item BOWL;
    public static Item MUSHROOM_SOUP;
    public static Item GOLD_SWORD;
    public static Item GOLD_SPADE;
    public static Item GOLD_PICKAXE;
    public static Item GOLD_AXE;
    public static Item STRING;
    public static Item FEATHER;
    public static Item SULPHUR;
    public static Item WOOD_HOE;
    public static Item STONE_HOE;
    public static Item IRON_HOE;
    public static Item DIAMOND_HOE;
    public static Item GOLD_HOE;
    public static Item SEEDS;
    public static Item WHEAT;
    public static Item BREAD;
    public static Item LEATHER_HELMET;
    public static Item LEATHER_CHESTPLATE;
    public static Item LEATHER_LEGGINGS;
    public static Item LEATHER_BOOTS;
    public static Item CHAINMAIL_HELMET;
    public static Item CHAINMAIL_CHESTPLATE;
    public static Item CHAINMAIL_LEGGINGS;
    public static Item CHAINMAIL_BOOTS;
    public static Item IRON_HELMET;
    public static Item IRON_CHESTPLATE;
    public static Item IRON_LEGGINGS;
    public static Item IRON_BOOTS;
    public static Item DIAMOND_HELMET;
    public static Item DIAMOND_CHESTPLATE;
    public static Item DIAMOND_LEGGINGS;
    public static Item DIAMOND_BOOTS;
    public static Item GOLD_HELMET;
    public static Item GOLD_CHESTPLATE;
    public static Item GOLD_LEGGINGS;
    public static Item GOLD_BOOTS;
    public static Item FLINT;
    public static Item PORK;
    public static Item GRILLED_PORK;
    public static Item PAINTING;
    public static Item GOLDEN_APPLE;
    public static Item SIGN;
    public static Item WOOD_DOOR;
    public static Item BUCKET;
    public static Item WATER_BUCKET;
    public static Item LAVA_BUCKET;
    public static Item MINECART;
    public static Item SADDLE;
    public static Item IRON_DOOR;
    public static Item REDSTONE;
    public static Item SNOW_BALL;
    public static Item BOAT;
    public static Item LEATHER;
    public static Item MILK_BUCKET;
    public static Item CLAY_BRICK;
    public static Item CLAY_BALL;
    public static Item SUGAR_CANE;
    public static Item PAPER;
    public static Item BOOK;
    public static Item SLIME_BALL;
    public static Item STORAGE_MINECART;
    public static Item POWERED_MINECART;
    public static Item EGG;
    public static Item COMPASS;
    public static Item FISHING_ROD;
    public static Item WATCH;
    public static Item GLOWSTONE_DUST;
    public static Item RAW_FISH;
    public static Item COOKED_FISH;
    public static Item INK_SACK;
    public static Item BONE;
    public static Item SUGAR;
    public static Item CAKE;
    public static Item BED;
    public static Item DIODE;
    public static Item COOKIE;
    public static ItemWorldMap MAP;
    public static ItemShears SHEARS;
    public static Item GOLD_RECORD;
    public static Item GREEN_RECORD;
    public final int id;
    protected int maxStackSize;
    private int durability;
    protected int textureId;
    protected boolean bh;
    protected boolean bi;
    private Item craftingResult;
    private String name;
    
    protected Item(final int k) {
        this.maxStackSize = 64;
        this.durability = 0;
        this.bh = false;
        this.bi = false;
        this.craftingResult = null;
        this.id = 256 + k;
        if (Item.byId[256 + k] != null) {
            System.out.println("CONFLICT @ " + k + ":" + Item.byId[256 + k]);
        }
        Item.byId[256 + k] = this;
        Material.addMaterial(256 + k);
    }
    
    public Item b(final int k) {
        this.textureId = k;
        return this;
    }
    
    public Item c(final int k) {
        this.maxStackSize = k;
        return this;
    }
    
    public Item a(final int k, final int l) {
        this.textureId = k + l * 16;
        return this;
    }
    
    public boolean a(final ItemStack itemstack, final EntityHuman entityhuman, final World world, final int k, final int l, final int i1, final int j1) {
        return false;
    }
    
    public float a(final ItemStack itemstack, final Block block) {
        return 1.0f;
    }
    
    public float getStrVsBlock(final ItemStack itemstack, final Block block, final int md) {
        return this.a(itemstack, block);
    }
    
    public ItemStack a(final ItemStack itemstack, final World world, final EntityHuman entityhuman) {
        return itemstack;
    }
    
    public int getMaxStackSize() {
        return this.maxStackSize;
    }
    
    public int filterData(final int k) {
        return 0;
    }
    
    public boolean d() {
        return this.bi;
    }
    
    protected Item a(final boolean flag) {
        this.bi = flag;
        return this;
    }
    
    public int e() {
        return this.durability;
    }
    
    protected Item d(final int k) {
        this.durability = k;
        return this;
    }
    
    public boolean f() {
        return this.durability > 0 && !this.bi;
    }
    
    public boolean a(final ItemStack itemstack, final EntityLiving entityliving, final EntityLiving entityliving1) {
        return false;
    }
    
    public boolean a(final ItemStack itemstack, final int k, final int l, final int i1, final int j1, final EntityLiving entityliving) {
        return false;
    }
    
    public int a(final Entity entity) {
        return 1;
    }
    
    public boolean a(final Block block) {
        return false;
    }
    
    public void a(final ItemStack itemstack, final EntityLiving entityliving) {
    }
    
    public Item g() {
        this.bh = true;
        return this;
    }
    
    public Item a(final String s) {
        this.name = "item." + s;
        Material.setMaterialName(this.id, s);
        return this;
    }
    
    public String a() {
        return this.name;
    }
    
    public Item a(final Item item) {
        if (this.maxStackSize > 1) {
            throw new IllegalArgumentException("Max stack size must be 1 for items with crafting results");
        }
        this.craftingResult = item;
        return this;
    }
    
    public Item h() {
        return this.craftingResult;
    }
    
    public boolean i() {
        return this.craftingResult != null;
    }
    
    public String j() {
        return StatisticCollector.a(this.a() + ".name");
    }
    
    public void a(final ItemStack itemstack, final World world, final Entity entity, final int k, final boolean flag) {
    }
    
    public void c(final ItemStack itemstack, final World world, final EntityHuman entityhuman) {
    }
    
    public boolean b() {
        return false;
    }
    
    static {
        Item.b = new Random();
        Item.byId = new Item[32000];
        Item.FLINT_AND_STEEL = new ItemFlintAndSteel(3).a(5, 0).a("flintAndSteel");
        Item.APPLE = new ItemFood(4, 4, false).a(10, 0).a("apple");
        Item.BOW = new ItemBow(5).a(5, 1).a("bow");
        Item.ARROW = new Item(6).a(5, 2).a("arrow");
        Item.COAL = new ItemCoal(7).a(7, 0).a("coal");
        Item.DIAMOND = new Item(8).a(7, 3).a("emerald");
        Item.IRON_INGOT = new Item(9).a(7, 1).a("ingotIron");
        Item.GOLD_INGOT = new Item(10).a(7, 2).a("ingotGold");
        Item.STICK = new Item(24).a(5, 3).g().a("stick");
        Item.BOWL = new Item(25).a(7, 4).a("bowl");
        Item.MUSHROOM_SOUP = new ItemSoup(26, 10).a(8, 4).a("mushroomStew");
        Item.STRING = new Item(31).a(8, 0).a("string");
        Item.FEATHER = new Item(32).a(8, 1).a("feather");
        Item.SULPHUR = new Item(33).a(8, 2).a("sulphur");
        Item.WHEAT = new Item(40).a(9, 1).a("wheat");
        Item.BREAD = new ItemFood(41, 5, false).a(9, 2).a("bread");
        Item.LEATHER_HELMET = new ItemArmor(42, 0, 0, 0).a(0, 0).a("helmetCloth");
        Item.LEATHER_CHESTPLATE = new ItemArmor(43, 0, 0, 1).a(0, 1).a("chestplateCloth");
        Item.LEATHER_LEGGINGS = new ItemArmor(44, 0, 0, 2).a(0, 2).a("leggingsCloth");
        Item.LEATHER_BOOTS = new ItemArmor(45, 0, 0, 3).a(0, 3).a("bootsCloth");
        Item.CHAINMAIL_HELMET = new ItemArmor(46, 1, 1, 0).a(1, 0).a("helmetChain");
        Item.CHAINMAIL_CHESTPLATE = new ItemArmor(47, 1, 1, 1).a(1, 1).a("chestplateChain");
        Item.CHAINMAIL_LEGGINGS = new ItemArmor(48, 1, 1, 2).a(1, 2).a("leggingsChain");
        Item.CHAINMAIL_BOOTS = new ItemArmor(49, 1, 1, 3).a(1, 3).a("bootsChain");
        Item.IRON_HELMET = new ItemArmor(50, 2, 2, 0).a(2, 0).a("helmetIron");
        Item.IRON_CHESTPLATE = new ItemArmor(51, 2, 2, 1).a(2, 1).a("chestplateIron");
        Item.IRON_LEGGINGS = new ItemArmor(52, 2, 2, 2).a(2, 2).a("leggingsIron");
        Item.IRON_BOOTS = new ItemArmor(53, 2, 2, 3).a(2, 3).a("bootsIron");
        Item.DIAMOND_HELMET = new ItemArmor(54, 3, 3, 0).a(3, 0).a("helmetDiamond");
        Item.DIAMOND_CHESTPLATE = new ItemArmor(55, 3, 3, 1).a(3, 1).a("chestplateDiamond");
        Item.DIAMOND_LEGGINGS = new ItemArmor(56, 3, 3, 2).a(3, 2).a("leggingsDiamond");
        Item.DIAMOND_BOOTS = new ItemArmor(57, 3, 3, 3).a(3, 3).a("bootsDiamond");
        Item.GOLD_HELMET = new ItemArmor(58, 1, 4, 0).a(4, 0).a("helmetGold");
        Item.GOLD_CHESTPLATE = new ItemArmor(59, 1, 4, 1).a(4, 1).a("chestplateGold");
        Item.GOLD_LEGGINGS = new ItemArmor(60, 1, 4, 2).a(4, 2).a("leggingsGold");
        Item.GOLD_BOOTS = new ItemArmor(61, 1, 4, 3).a(4, 3).a("bootsGold");
        Item.FLINT = new Item(62).a(6, 0).a("flint");
        Item.PORK = new ItemFood(63, 3, true).a(7, 5).a("porkchopRaw");
        Item.GRILLED_PORK = new ItemFood(64, 8, true).a(8, 5).a("porkchopCooked");
        Item.PAINTING = new ItemPainting(65).a(10, 1).a("painting");
        Item.GOLDEN_APPLE = new ItemFood(66, 42, false).a(11, 0).a("appleGold");
        Item.SIGN = new ItemSign(67).a(10, 2).a("sign");
        Item.MINECART = new ItemMinecart(72, 0).a(7, 8).a("minecart");
        Item.SADDLE = new ItemSaddle(73).a(8, 6).a("saddle");
        Item.REDSTONE = new ItemRedstone(75).a(8, 3).a("redstone");
        Item.SNOW_BALL = new ItemSnowball(76).a(14, 0).a("snowball");
        Item.BOAT = new ItemBoat(77).a(8, 8).a("boat");
        Item.LEATHER = new Item(78).a(7, 6).a("leather");
        Item.CLAY_BRICK = new Item(80).a(6, 1).a("brick");
        Item.CLAY_BALL = new Item(81).a(9, 3).a("clay");
        Item.PAPER = new Item(83).a(10, 3).a("paper");
        Item.BOOK = new Item(84).a(11, 3).a("book");
        Item.SLIME_BALL = new Item(85).a(14, 1).a("slimeball");
        Item.STORAGE_MINECART = new ItemMinecart(86, 1).a(7, 9).a("minecartChest");
        Item.POWERED_MINECART = new ItemMinecart(87, 2).a(7, 10).a("minecartFurnace");
        Item.EGG = new ItemEgg(88).a(12, 0).a("egg");
        Item.COMPASS = new Item(89).a(6, 3).a("compass");
        Item.FISHING_ROD = new ItemFishingRod(90).a(5, 4).a("fishingRod");
        Item.WATCH = new Item(91).a(6, 4).a("clock");
        Item.GLOWSTONE_DUST = new Item(92).a(9, 4).a("yellowDust");
        Item.RAW_FISH = new ItemFood(93, 2, false).a(9, 5).a("fishRaw");
        Item.COOKED_FISH = new ItemFood(94, 5, false).a(10, 5).a("fishCooked");
        Item.INK_SACK = new ItemDye(95).a(14, 4).a("dyePowder");
        Item.BONE = new Item(96).a(12, 1).a("bone").g();
        Item.SUGAR = new Item(97).a(13, 0).a("sugar").g();
        Item.BED = new ItemBed(99).c(1).a(13, 2).a("bed");
        Item.COOKIE = new ItemCookie(101, 1, false, 8).a(12, 5).a("cookie");
        Item.MAP = (ItemWorldMap)new ItemWorldMap(102).a(12, 3).a("map");
        Item.SHEARS = (ItemShears)new ItemShears(103).a(13, 5).a("shears");
        Item.GOLD_RECORD = new ItemRecord(2000, "13").a(0, 15).a("record");
        Item.GREEN_RECORD = new ItemRecord(2001, "cat").a(1, 15).a("record");
        Item.IRON_SPADE = new ItemSpade(0, EnumToolMaterial.IRON).a(2, 5).a("shovelIron");
        Item.IRON_PICKAXE = new ItemPickaxe(1, EnumToolMaterial.IRON).a(2, 6).a("pickaxeIron");
        Item.IRON_AXE = new ItemAxe(2, EnumToolMaterial.IRON).a(2, 7).a("hatchetIron");
        Item.IRON_SWORD = new ItemSword(11, EnumToolMaterial.IRON).a(2, 4).a("swordIron");
        Item.WOOD_SWORD = new ItemSword(12, EnumToolMaterial.WOOD).a(0, 4).a("swordWood");
        Item.WOOD_SPADE = new ItemSpade(13, EnumToolMaterial.WOOD).a(0, 5).a("shovelWood");
        Item.WOOD_PICKAXE = new ItemPickaxe(14, EnumToolMaterial.WOOD).a(0, 6).a("pickaxeWood");
        Item.WOOD_AXE = new ItemAxe(15, EnumToolMaterial.WOOD).a(0, 7).a("hatchetWood");
        Item.STONE_SWORD = new ItemSword(16, EnumToolMaterial.STONE).a(1, 4).a("swordStone");
        Item.STONE_SPADE = new ItemSpade(17, EnumToolMaterial.STONE).a(1, 5).a("shovelStone");
        Item.STONE_PICKAXE = new ItemPickaxe(18, EnumToolMaterial.STONE).a(1, 6).a("pickaxeStone");
        Item.STONE_AXE = new ItemAxe(19, EnumToolMaterial.STONE).a(1, 7).a("hatchetStone");
        Item.DIAMOND_SWORD = new ItemSword(20, EnumToolMaterial.DIAMOND).a(3, 4).a("swordDiamond");
        Item.DIAMOND_SPADE = new ItemSpade(21, EnumToolMaterial.DIAMOND).a(3, 5).a("shovelDiamond");
        Item.DIAMOND_PICKAXE = new ItemPickaxe(22, EnumToolMaterial.DIAMOND).a(3, 6).a("pickaxeDiamond");
        Item.DIAMOND_AXE = new ItemAxe(23, EnumToolMaterial.DIAMOND).a(3, 7).a("hatchetDiamond");
        Item.GOLD_SWORD = new ItemSword(27, EnumToolMaterial.GOLD).a(4, 4).a("swordGold");
        Item.GOLD_SPADE = new ItemSpade(28, EnumToolMaterial.GOLD).a(4, 5).a("shovelGold");
        Item.GOLD_PICKAXE = new ItemPickaxe(29, EnumToolMaterial.GOLD).a(4, 6).a("pickaxeGold");
        Item.GOLD_AXE = new ItemAxe(30, EnumToolMaterial.GOLD).a(4, 7).a("hatchetGold");
        Item.WOOD_HOE = new ItemHoe(34, EnumToolMaterial.WOOD).a(0, 8).a("hoeWood");
        Item.STONE_HOE = new ItemHoe(35, EnumToolMaterial.STONE).a(1, 8).a("hoeStone");
        Item.IRON_HOE = new ItemHoe(36, EnumToolMaterial.IRON).a(2, 8).a("hoeIron");
        Item.DIAMOND_HOE = new ItemHoe(37, EnumToolMaterial.DIAMOND).a(3, 8).a("hoeDiamond");
        Item.GOLD_HOE = new ItemHoe(38, EnumToolMaterial.GOLD).a(4, 8).a("hoeGold");
        Item.SEEDS = new ItemSeeds(39, Block.CROPS.id).a(9, 0).a("seeds");
        Item.WOOD_DOOR = new ItemDoor(68, net.minecraft.server.Material.WOOD).a(11, 2).a("doorWood");
        Item.BUCKET = new ItemBucket(69, 0).a(10, 4).a("bucket");
        Item.WATER_BUCKET = new ItemBucket(70, Block.WATER.id).a(11, 4).a("bucketWater").a(Item.BUCKET);
        Item.LAVA_BUCKET = new ItemBucket(71, Block.LAVA.id).a(12, 4).a("bucketLava").a(Item.BUCKET);
        Item.IRON_DOOR = new ItemDoor(74, net.minecraft.server.Material.ORE).a(12, 2).a("doorIron");
        Item.MILK_BUCKET = new ItemBucket(79, -1).a(13, 4).a("milk").a(Item.BUCKET);
        Item.SUGAR_CANE = new ItemReed(82, Block.SUGAR_CANE_BLOCK).a(11, 1).a("reeds");
        Item.CAKE = new ItemReed(98, Block.CAKE_BLOCK).c(1).a(13, 1).a("cake");
        Item.DIODE = new ItemReed(100, Block.DIODE_OFF).a(6, 5).a("diode");
        StatisticList.c();
    }
}
