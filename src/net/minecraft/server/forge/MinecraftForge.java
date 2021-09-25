// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.forge;

import java.util.List;
import net.minecraft.server.Block;
import java.util.Arrays;
import java.io.Serializable;
import net.minecraft.server.Item;
import java.util.Iterator;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import java.util.LinkedList;

public class MinecraftForge
{
    private static LinkedList<IBucketHandler> bucketHandlers;
    
    @Deprecated
    public static void registerCustomBucketHander(final IBucketHandler handler) {
        MinecraftForge.bucketHandlers.add(handler);
    }
    
    public static void registerCustomBucketHandler(final IBucketHandler handler) {
        MinecraftForge.bucketHandlers.add(handler);
    }
    
    public static void registerSleepHandler(final ISleepHandler handler) {
        ForgeHooks.sleepHandlers.add(handler);
    }
    
    public static void registerDestroyToolHandler(final IDestroyToolHandler handler) {
        ForgeHooks.destroyToolHandlers.add(handler);
    }
    
    public static void registerCraftingHandler(final ICraftingHandler handler) {
        ForgeHooks.craftingHandlers.add(handler);
    }
    
    public static ItemStack fillCustomBucket(final World w, final int i, final int j, final int k) {
        for (final IBucketHandler handler : MinecraftForge.bucketHandlers) {
            final ItemStack stack = handler.fillCustomBucket(w, i, j, k);
            if (stack != null) {
                return stack;
            }
        }
        return null;
    }
    
    public static void setToolClass(final Item tool, final String tclass, final int hlevel) {
        ForgeHooks.initTools();
        ForgeHooks.toolClasses.put(tool.id, Arrays.asList(tclass, hlevel));
    }
    
    public static void setBlockHarvestLevel(final Block bl, final int md, final String tclass, final int hlevel) {
        ForgeHooks.initTools();
        final List key = Arrays.asList(bl.id, md, tclass);
        ForgeHooks.toolHarvestLevels.put(key, hlevel);
        ForgeHooks.toolEffectiveness.add(key);
    }
    
    public static void removeBlockEffectiveness(final Block bl, final int md, final String tclass) {
        ForgeHooks.initTools();
        final List key = Arrays.asList(bl.id, md, tclass);
        ForgeHooks.toolEffectiveness.remove(key);
    }
    
    public static void setBlockHarvestLevel(final Block bl, final String tclass, final int hlevel) {
        ForgeHooks.initTools();
        for (int md = 0; md < 16; ++md) {
            final List key = Arrays.asList(bl.id, md, tclass);
            ForgeHooks.toolHarvestLevels.put(key, hlevel);
            ForgeHooks.toolEffectiveness.add(key);
        }
    }
    
    public static void removeBlockEffectiveness(final Block bl, final String tclass) {
        ForgeHooks.initTools();
        for (int md = 0; md < 16; ++md) {
            final List key = Arrays.asList(bl.id, md, tclass);
            ForgeHooks.toolEffectiveness.remove(key);
        }
    }
    
    public static void addPickaxeBlockEffectiveAgainst(final Block block) {
        setBlockHarvestLevel(block, "pickaxe", 0);
    }
    
    public static void killMinecraft(final String modname, final String msg) {
        throw new RuntimeException(modname + ": " + msg);
    }
    
    public static void versionDetect(final String modname, final int major, final int minor, final int revision) {
        if (major != 1) {
            killMinecraft(modname, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
        }
        else if (minor != 0) {
            if (minor > 0) {
                killMinecraft(modname, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
            }
            else {
                System.out.println(modname + ": MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x, may lead to unexpected behavior");
            }
        }
        else if (revision > 6) {
            killMinecraft(modname, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
        }
    }
    
    public static void versionDetectStrict(final String modname, final int major, final int minor, final int revision) {
        if (major != 1) {
            killMinecraft(modname, "MinecraftForge Major Version Mismatch, expecting " + major + ".x.x");
        }
        else if (minor != 0) {
            if (minor > 0) {
                killMinecraft(modname, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
            }
            else {
                killMinecraft(modname, "MinecraftForge minor version mismatch, expecting " + major + "." + minor + ".x");
            }
        }
        else if (revision > 6) {
            killMinecraft(modname, "MinecraftForge Too Old, need at least " + major + "." + minor + "." + revision);
        }
    }
    
    static {
        MinecraftForge.bucketHandlers = new LinkedList();
    }
}
