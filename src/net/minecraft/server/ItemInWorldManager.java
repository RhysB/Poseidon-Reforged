// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import net.minecraft.server.forge.IUseItemFirst;
import net.minecraft.server.forge.ForgeHooks;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.Event;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.Action;

public class ItemInWorldManager
{
    private WorldServer world;
    public EntityHuman player;
    private float c;
    private int lastDigTick;
    private int e;
    private int f;
    private int g;
    private int currentTick;
    private boolean i;
    private int j;
    private int k;
    private int l;
    private int m;
    
    public ItemInWorldManager(final WorldServer worldserver) {
        this.c = 0.0f;
        this.world = worldserver;
    }
    
    public void a() {
        this.currentTick = (int)(System.currentTimeMillis() / 50L);
        if (this.i) {
            final int i = this.currentTick - this.m;
            final int j = this.world.getTypeId(this.j, this.k, this.l);
            if (j != 0) {
                final Block block = Block.byId[j];
                final float f = block.blockStrength((World)this.world, this.player, this.j, this.k, this.l) * (i + 1);
                if (f >= 1.0f) {
                    this.i = false;
                    this.c(this.j, this.k, this.l);
                }
            }
            else {
                this.i = false;
            }
        }
    }
    
    public void dig(final int i, final int j, final int k, final int l) {
        this.lastDigTick = (int)(System.currentTimeMillis() / 50L);
        final int i2 = this.world.getTypeId(i, j, k);
        if (i2 <= 0) {
            return;
        }
        final PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, i, j, k, l, this.player.inventory.getItemInHand());
        if (event.useInteractedBlock() == Event.Result.DENY) {
            if (i2 == Block.WOODEN_DOOR.id) {
                final boolean bottom = (this.world.getData(i, j, k) & 0x8) == 0x0;
                ((EntityPlayer)this.player).netServerHandler.sendPacket((Packet)new Packet53BlockChange(i, j, k, (World)this.world));
                ((EntityPlayer)this.player).netServerHandler.sendPacket((Packet)new Packet53BlockChange(i, j + (bottom ? 1 : -1), k, (World)this.world));
            }
            else if (i2 == Block.TRAP_DOOR.id) {
                ((EntityPlayer)this.player).netServerHandler.sendPacket((Packet)new Packet53BlockChange(i, j, k, (World)this.world));
            }
        }
        else {
            Block.byId[i2].b((World)this.world, i, j, k, this.player);
            this.world.douseFire((EntityHuman)null, i, j, k, l);
        }
        float toolDamage = Block.byId[i2].blockStrength((World)this.world, this.player, i, j, k);
        if (event.useItemInHand() == Event.Result.DENY) {
            if (toolDamage > 1.0f) {
                ((EntityPlayer)this.player).netServerHandler.sendPacket((Packet)new Packet53BlockChange(i, j, k, (World)this.world));
            }
            return;
        }
        final BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.player, i, j, k, this.player.inventory.getItemInHand(), toolDamage >= 1.0f);
        if (blockEvent.isCancelled()) {
            return;
        }
        if (blockEvent.getInstaBreak()) {
            toolDamage = 2.0f;
        }
        if (toolDamage >= 1.0f) {
            this.c(i, j, k);
        }
        else {
            this.e = i;
            this.f = j;
            this.g = k;
        }
    }
    
    public void a(final int i, final int j, final int k) {
        if (i == this.e && j == this.f && k == this.g) {
            this.currentTick = (int)(System.currentTimeMillis() / 50L);
            final int l = this.currentTick - this.lastDigTick;
            final int i2 = this.world.getTypeId(i, j, k);
            if (i2 != 0) {
                final Block block = Block.byId[i2];
                final float f = block.blockStrength((World)this.world, this.player, i, j, k) * (l + 1);
                if (f >= 0.7f) {
                    this.c(i, j, k);
                }
                else if (!this.i) {
                    this.i = true;
                    this.j = i;
                    this.k = j;
                    this.l = k;
                    this.m = this.lastDigTick;
                }
            }
        }
        else {
            ((EntityPlayer)this.player).netServerHandler.sendPacket((Packet)new Packet53BlockChange(i, j, k, (World)this.world));
        }
        this.c = 0.0f;
    }
    
    public boolean b(final int i, final int j, final int k) {
        final Block block = Block.byId[this.world.getTypeId(i, j, k)];
        final int l = this.world.getData(i, j, k);
        final boolean flag = this.world.setTypeId(i, j, k, 0);
        if (block != null && flag) {
            block.postBreak((World)this.world, i, j, k, l);
        }
        return flag;
    }
    
    public boolean c(final int i, final int j, final int k) {
        if (this.player instanceof EntityPlayer) {
            final org.bukkit.block.Block block = this.world.getWorld().getBlockAt(i, j, k);
            final BlockBreakEvent event = new BlockBreakEvent(block, (Player)this.player.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent((Event)event);
            if (event.isCancelled()) {
                return false;
            }
        }
        final int l = this.world.getTypeId(i, j, k);
        final int i2 = this.world.getData(i, j, k);
        this.world.a(this.player, 2001, i, j, k, l + this.world.getData(i, j, k) * 256);
        final boolean flag = this.b(i, j, k);
        final ItemStack itemstack = this.player.G();
        if (itemstack != null) {
            itemstack.a(l, i, j, k, this.player);
            if (itemstack.count == 0) {
                itemstack.a(this.player);
                this.player.H();
            }
        }
        if (flag && Block.byId[l].canHarvestBlock(this.player, i2)) {
            Block.byId[l].a((World)this.world, this.player, i, j, k, i2);
            ((EntityPlayer)this.player).netServerHandler.sendPacket((Packet)new Packet53BlockChange(i, j, k, (World)this.world));
        }
        return flag;
    }
    
    public boolean useItem(final EntityHuman entityhuman, final World world, final ItemStack itemstack) {
        final int i = itemstack.count;
        final ItemStack itemstack2 = itemstack.a(world, entityhuman);
        if (itemstack2 == itemstack && (itemstack2 == null || itemstack2.count == i)) {
            return false;
        }
        entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = itemstack2;
        if (itemstack2.count == 0) {
            entityhuman.inventory.items[entityhuman.inventory.itemInHandIndex] = null;
            ForgeHooks.onDestroyCurrentItem(entityhuman, itemstack2);
        }
        return true;
    }
    
    public boolean interact(final EntityHuman entityhuman, final World world, final ItemStack itemstack, final int i, final int j, final int k, final int l) {
        if (itemstack != null && itemstack.getItem() instanceof IUseItemFirst) {
            final IUseItemFirst iuif = (IUseItemFirst)itemstack.getItem();
            if (iuif.onItemUseFirst(itemstack, entityhuman, world, i, j, k, l)) {
                return true;
            }
        }
        final int i2 = world.getTypeId(i, j, k);
        boolean result = false;
        if (i2 > 0) {
            final PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityhuman, Action.RIGHT_CLICK_BLOCK, i, j, k, l, itemstack);
            if (event.useInteractedBlock() == Event.Result.DENY) {
                if (i2 == Block.WOODEN_DOOR.id) {
                    final boolean bottom = (world.getData(i, j, k) & 0x8) == 0x0;
                    ((EntityPlayer)entityhuman).netServerHandler.sendPacket((Packet)new Packet53BlockChange(i, j + (bottom ? 1 : -1), k, world));
                }
                result = (event.useItemInHand() != Event.Result.ALLOW);
            }
            else {
                result = Block.byId[i2].interact(world, i, j, k, entityhuman);
            }
            if (itemstack != null && !result) {
                result = itemstack.placeItem(entityhuman, world, i, j, k, l);
                if (result && itemstack.count == 0) {
                    ForgeHooks.onDestroyCurrentItem(entityhuman, itemstack);
                }
            }
            if (itemstack != null && ((!result && event.useItemInHand() != Event.Result.DENY) || event.useItemInHand() == Event.Result.ALLOW)) {
                this.useItem(entityhuman, world, itemstack);
            }
        }
        return result;
    }
}
