// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

import net.minecraft.server.Item;
import java.util.Arrays;
import java.io.Serializable;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.EnumBedError;
import java.util.Iterator;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.EntityHuman;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;

public class ForgeHooks
{
    static LinkedList<ICraftingHandler> craftingHandlers;
    static LinkedList<IDestroyToolHandler> destroyToolHandlers;
    static LinkedList<ISleepHandler> sleepHandlers;
    public static final int majorVersion = 1;
    public static final int minorVersion = 0;
    public static final int revisionVersion = 6;
    static boolean toolInit;
    static HashMap toolClasses;
    static HashMap toolHarvestLevels;
    static HashSet toolEffectiveness;
    
    public static void onTakenFromCrafting(final EntityHuman player, final ItemStack ist, final IInventory craftMatrix) {
        for (final ICraftingHandler handler : ForgeHooks.craftingHandlers) {
            handler.onTakenFromCrafting(player, ist, craftMatrix);
        }
    }
    
    public static void onDestroyCurrentItem(final EntityHuman player, final ItemStack orig) {
        for (final IDestroyToolHandler handler : ForgeHooks.destroyToolHandlers) {
            handler.onDestroyCurrentItem(player, orig);
        }
    }
    
    public static EnumBedError sleepInBedAt(final EntityHuman player, final int i, final int j, final int k) {
        for (final ISleepHandler handler : ForgeHooks.sleepHandlers) {
            final EnumBedError status = handler.sleepInBedAt(player, i, j, k);
            if (status != null) {
                return status;
            }
        }
        return null;
    }
    
    public static boolean canHarvestBlock(final Block bl, final EntityHuman player, final int md) {
        if (bl.material.i()) {
            return true;
        }
        final ItemStack itemstack = player.inventory.getItemInHand();
        if (itemstack == null) {
            return false;
        }
        final List tc = ForgeHooks.toolClasses.get(itemstack.id);
        if (tc == null) {
            return itemstack.b(bl);
        }
        final Object[] ta = tc.toArray();
        final String cls = (String)ta[0];
        final int hvl = (int)ta[1];
        final Integer bhl = ForgeHooks.toolHarvestLevels.get(Arrays.asList(bl.id, md, cls));
        if (bhl == null) {
            return itemstack.b(bl);
        }
        return bhl <= hvl && itemstack.b(bl);
    }
    
    public static float blockStrength(final Block bl, final EntityHuman player, final int md) {
        final float bh = bl.getHardness(md);
        if (bh < 0.0f) {
            return 0.0f;
        }
        if (!canHarvestBlock(bl, player, md)) {
            return 1.0f / bh / 100.0f;
        }
        return player.getCurrentPlayerStrVsBlock(bl, md) / bh / 30.0f;
    }
    
    public static boolean isToolEffective(final ItemStack ist, final Block bl, final int md) {
        final List tc = ForgeHooks.toolClasses.get(ist.id);
        if (tc == null) {
            return false;
        }
        final Object[] ta = tc.toArray();
        final String cls = (String)ta[0];
        return ForgeHooks.toolEffectiveness.contains(Arrays.asList(bl.id, md, cls));
    }
    
    static void initTools() {
        if (ForgeHooks.toolInit) {
            return;
        }
        ForgeHooks.toolInit = true;
        MinecraftForge.setToolClass(Item.WOOD_PICKAXE, "pickaxe", 0);
        MinecraftForge.setToolClass(Item.STONE_PICKAXE, "pickaxe", 1);
        MinecraftForge.setToolClass(Item.IRON_PICKAXE, "pickaxe", 2);
        MinecraftForge.setToolClass(Item.GOLD_PICKAXE, "pickaxe", 0);
        MinecraftForge.setToolClass(Item.DIAMOND_PICKAXE, "pickaxe", 3);
        MinecraftForge.setToolClass(Item.WOOD_AXE, "axe", 0);
        MinecraftForge.setToolClass(Item.STONE_AXE, "axe", 1);
        MinecraftForge.setToolClass(Item.IRON_AXE, "axe", 2);
        MinecraftForge.setToolClass(Item.GOLD_AXE, "axe", 0);
        MinecraftForge.setToolClass(Item.DIAMOND_AXE, "axe", 3);
        MinecraftForge.setToolClass(Item.WOOD_SPADE, "shovel", 0);
        MinecraftForge.setToolClass(Item.STONE_SPADE, "shovel", 1);
        MinecraftForge.setToolClass(Item.IRON_SPADE, "shovel", 2);
        MinecraftForge.setToolClass(Item.GOLD_SPADE, "shovel", 0);
        MinecraftForge.setToolClass(Item.DIAMOND_SPADE, "shovel", 3);
        MinecraftForge.setBlockHarvestLevel(Block.OBSIDIAN, "pickaxe", 3);
        MinecraftForge.setBlockHarvestLevel(Block.DIAMOND_ORE, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(Block.DIAMOND_BLOCK, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(Block.GOLD_ORE, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(Block.GOLD_BLOCK, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(Block.IRON_ORE, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(Block.IRON_BLOCK, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(Block.LAPIS_ORE, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(Block.LAPIS_BLOCK, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(Block.REDSTONE_ORE, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(Block.GLOWING_REDSTONE_ORE, "pickaxe", 2);
        MinecraftForge.removeBlockEffectiveness(Block.REDSTONE_ORE, "pickaxe");
        MinecraftForge.removeBlockEffectiveness(Block.GLOWING_REDSTONE_ORE, "pickaxe");
        final Block[] arr$;
        final Block[] pickeff = arr$ = new Block[] { Block.COBBLESTONE, Block.DOUBLE_STEP, Block.STEP, Block.STONE, Block.SANDSTONE, Block.MOSSY_COBBLESTONE, Block.IRON_ORE, Block.IRON_BLOCK, Block.COAL_ORE, Block.GOLD_BLOCK, Block.GOLD_ORE, Block.DIAMOND_ORE, Block.DIAMOND_BLOCK, Block.ICE, Block.NETHERRACK, Block.LAPIS_ORE, Block.LAPIS_BLOCK };
        for (final Block bl : arr$) {
            MinecraftForge.setBlockHarvestLevel(bl, "pickaxe", 0);
        }
    }
    
    static {
        ForgeHooks.craftingHandlers = new LinkedList();
        ForgeHooks.destroyToolHandlers = new LinkedList();
        ForgeHooks.sleepHandlers = new LinkedList();
        System.out.printf("MinecraftForge V%d.%d.%d Initialized\n", 1, 0, 6);
        ForgeHooks.toolInit = false;
        ForgeHooks.toolClasses = new HashMap();
        ForgeHooks.toolHarvestLevels = new HashMap();
        ForgeHooks.toolEffectiveness = new HashSet();
    }
}
