// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import org.bukkit.Bukkit;
import org.bukkit.TravelAgent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.craftbukkit.PortalTravelAgent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.Iterator;
import org.bukkit.craftbukkit.command.ColouredConsoleSender;
import java.util.HashSet;
import java.util.ArrayList;
import org.bukkit.craftbukkit.CraftServer;
import java.io.File;
import java.util.Set;
import java.util.List;
import java.util.logging.Logger;

public class ServerConfigurationManager
{
    public static Logger a;
    public List players;
    public MinecraftServer server;
    public int maxPlayers;
    private Set banByName;
    private Set banByIP;
    private Set h;
    private Set i;
    private File j;
    private File k;
    private File l;
    private File m;
    public PlayerFileData playerFileData;
    private boolean o;
    private CraftServer cserver;
    
    public ServerConfigurationManager(final MinecraftServer minecraftserver) {
        this.players = new ArrayList();
        this.banByName = new HashSet();
        this.banByIP = new HashSet();
        this.h = new HashSet();
        this.i = new HashSet();
        minecraftserver.server = new CraftServer(minecraftserver, this);
        minecraftserver.console = new ColouredConsoleSender(minecraftserver.server);
        this.cserver = minecraftserver.server;
        this.server = minecraftserver;
        this.j = minecraftserver.a("banned-players.txt");
        this.k = minecraftserver.a("banned-ips.txt");
        this.l = minecraftserver.a("ops.txt");
        this.m = minecraftserver.a("white-list.txt");
        final int i = minecraftserver.propertyManager.getInt("view-distance", 10);
        this.maxPlayers = minecraftserver.propertyManager.getInt("max-players", 20);
        this.o = minecraftserver.propertyManager.getBoolean("white-list", false);
        this.g();
        this.i();
        this.k();
        this.m();
        this.h();
        this.j();
        this.l();
        this.n();
    }
    
    public void setPlayerFileData(final WorldServer[] aworldserver) {
        if (this.playerFileData != null) {
            return;
        }
        this.playerFileData = aworldserver[0].p().d();
    }
    
    public void a(final EntityPlayer entityplayer) {
        for (final WorldServer world : this.server.worlds) {
            if (world.manager.managedPlayers.contains(entityplayer)) {
                world.manager.removePlayer(entityplayer);
                break;
            }
        }
        this.getPlayerManager(entityplayer.dimension).addPlayer(entityplayer);
        final WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);
        worldserver.chunkProviderServer.getChunkAt((int)entityplayer.locX >> 4, (int)entityplayer.locZ >> 4);
    }
    
    public int a() {
        if (this.server.worlds.size() == 0) {
            return this.server.propertyManager.getInt("view-distance", 10) * 16 - 16;
        }
        return this.server.worlds.get(0).manager.getFurthestViewableBlock();
    }
    
    private PlayerManager getPlayerManager(final int i) {
        return this.server.getWorldServer(i).manager;
    }
    
    public void b(final EntityPlayer entityplayer) {
        this.playerFileData.b((EntityHuman)entityplayer);
    }
    
    public void c(final EntityPlayer entityplayer) {
        this.players.add(entityplayer);
        final WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);
        worldserver.chunkProviderServer.getChunkAt((int)entityplayer.locX >> 4, (int)entityplayer.locZ >> 4);
        while (worldserver.getEntities((Entity)entityplayer, entityplayer.boundingBox).size() != 0) {
            entityplayer.setPosition(entityplayer.locX, entityplayer.locY + 1.0, entityplayer.locZ);
        }
        final PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(this.cserver.getPlayer(entityplayer), "§e" + entityplayer.name + " joined the game.");
        this.cserver.getPluginManager().callEvent((Event)playerJoinEvent);
        final String joinMessage = playerJoinEvent.getJoinMessage();
        if (joinMessage != null) {
            this.server.serverConfigurationManager.sendAll((Packet)new Packet3Chat(joinMessage));
        }
        worldserver.addEntity((Entity)entityplayer);
        this.getPlayerManager(entityplayer.dimension).addPlayer(entityplayer);
    }
    
    public void d(final EntityPlayer entityplayer) {
        this.getPlayerManager(entityplayer.dimension).movePlayer(entityplayer);
    }
    
    public String disconnect(final EntityPlayer entityplayer) {
        this.getPlayerManager(entityplayer.dimension).removePlayer(entityplayer);
        final PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(this.cserver.getPlayer(entityplayer), "§e" + entityplayer.name + " left the game.");
        this.cserver.getPluginManager().callEvent((Event)playerQuitEvent);
        this.playerFileData.a((EntityHuman)entityplayer);
        this.server.getWorldServer(entityplayer.dimension).kill((Entity)entityplayer);
        this.players.remove(entityplayer);
        this.getPlayerManager(entityplayer.dimension).removePlayer(entityplayer);
        return playerQuitEvent.getQuitMessage();
    }
    
    public EntityPlayer a(final NetLoginHandler netloginhandler, final String s) {
        final EntityPlayer entity = new EntityPlayer(this.server, (World)this.server.getWorldServer(0), s, new ItemInWorldManager(this.server.getWorldServer(0)));
        final Player player = (entity == null) ? null : ((Player)entity.getBukkitEntity());
        final PlayerLoginEvent event = new PlayerLoginEvent(player);
        String s2 = netloginhandler.networkManager.getSocketAddress().toString();
        s2 = s2.substring(s2.indexOf("/") + 1);
        s2 = s2.substring(0, s2.indexOf(":"));
        if (this.banByName.contains(s.trim().toLowerCase())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned from this server!");
        }
        else if (!this.isWhitelisted(s)) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "You are not white-listed on this server!");
        }
        else if (this.banByIP.contains(s2)) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Your IP address is banned from this server!");
        }
        else if (this.players.size() >= this.maxPlayers) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "The server is full!");
        }
        else {
            event.disallow(PlayerLoginEvent.Result.ALLOWED, s2);
        }
        this.cserver.getPluginManager().callEvent((Event)event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            netloginhandler.disconnect(event.getKickMessage());
            return null;
        }
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityPlayer entityplayer = this.players.get(i);
            if (entityplayer.name.equalsIgnoreCase(s)) {
                entityplayer.netServerHandler.disconnect("You logged in from another location");
            }
        }
        return entity;
    }
    
    public EntityPlayer moveToWorld(final EntityPlayer entityplayer, final int i) {
        return this.moveToWorld(entityplayer, i, null);
    }
    
    public EntityPlayer moveToWorld(final EntityPlayer entityplayer, final int i, Location location) {
        this.server.getTracker(entityplayer.dimension).untrackPlayer(entityplayer);
        this.getPlayerManager(entityplayer.dimension).removePlayer(entityplayer);
        this.players.remove(entityplayer);
        this.server.getWorldServer(entityplayer.dimension).removeEntity((Entity)entityplayer);
        ChunkCoordinates chunkcoordinates = entityplayer.getBed();
        final EntityPlayer entityplayer2 = entityplayer;
        if (location == null) {
            boolean isBedSpawn = false;
            CraftWorld cworld = (CraftWorld)this.server.server.getWorld(entityplayer.spawnWorld);
            if (cworld != null && chunkcoordinates != null) {
                final ChunkCoordinates chunkcoordinates2 = EntityHuman.getBed((World)cworld.getHandle(), chunkcoordinates);
                if (chunkcoordinates2 != null) {
                    isBedSpawn = true;
                    location = new Location((org.bukkit.World)cworld, chunkcoordinates2.x + 0.5, (double)chunkcoordinates2.y, chunkcoordinates2.z + 0.5);
                }
                else {
                    entityplayer2.netServerHandler.sendPacket((Packet)new Packet70Bed(0));
                }
            }
            if (location == null) {
                cworld = this.server.server.getWorlds().get(0);
                chunkcoordinates = cworld.getHandle().getSpawn();
                location = new Location((org.bukkit.World)cworld, chunkcoordinates.x + 0.5, (double)chunkcoordinates.y, chunkcoordinates.z + 0.5);
            }
            final Player respawnPlayer = this.cserver.getPlayer(entityplayer);
            final PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(respawnPlayer, location, isBedSpawn);
            this.cserver.getPluginManager().callEvent((Event)respawnEvent);
            location = respawnEvent.getRespawnLocation();
            entityplayer.health = 20;
            entityplayer.fireTicks = 0;
            entityplayer.fallDistance = 0.0f;
        }
        else {
            location.setWorld((org.bukkit.World)this.server.getWorldServer(i).getWorld());
        }
        final WorldServer worldserver = ((CraftWorld)location.getWorld()).getHandle();
        entityplayer2.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        worldserver.chunkProviderServer.getChunkAt((int)entityplayer2.locX >> 4, (int)entityplayer2.locZ >> 4);
        while (worldserver.getEntities((Entity)entityplayer2, entityplayer2.boundingBox).size() != 0) {
            entityplayer2.setPosition(entityplayer2.locX, entityplayer2.locY + 1.0, entityplayer2.locZ);
        }
        final byte actualDimension = (byte)worldserver.getWorld().getEnvironment().getId();
        entityplayer2.netServerHandler.sendPacket((Packet)new Packet9Respawn((byte)((actualDimension >= 0) ? -1 : 0)));
        entityplayer2.netServerHandler.sendPacket((Packet)new Packet9Respawn(actualDimension));
        entityplayer2.spawnIn((World)worldserver);
        entityplayer2.dead = false;
        entityplayer2.netServerHandler.teleport(new Location((org.bukkit.World)worldserver.getWorld(), entityplayer2.locX, entityplayer2.locY, entityplayer2.locZ, entityplayer2.yaw, entityplayer2.pitch));
        this.a(entityplayer2, worldserver);
        this.getPlayerManager(entityplayer2.dimension).addPlayer(entityplayer2);
        worldserver.addEntity((Entity)entityplayer2);
        this.players.add(entityplayer2);
        this.updateClient(entityplayer2);
        entityplayer2.x();
        return entityplayer2;
    }
    
    public void f(final EntityPlayer entityplayer) {
        final int dimension = entityplayer.dimension;
        final WorldServer fromWorld = this.server.getWorldServer(dimension);
        WorldServer toWorld = null;
        if (dimension < 10) {
            final int toDimension = (dimension == -1) ? 0 : -1;
            for (final WorldServer world : this.server.worlds) {
                if (world.dimension == toDimension) {
                    toWorld = world;
                }
            }
        }
        final double blockRatio = (dimension == -1) ? 8.0 : 0.125;
        final Location fromLocation = new Location((org.bukkit.World)fromWorld.getWorld(), entityplayer.locX, entityplayer.locY, entityplayer.locZ, entityplayer.yaw, entityplayer.pitch);
        final Location toLocation = (toWorld == null) ? null : new Location((org.bukkit.World)toWorld.getWorld(), entityplayer.locX * blockRatio, entityplayer.locY, entityplayer.locZ * blockRatio, entityplayer.yaw, entityplayer.pitch);
        final PortalTravelAgent pta = new PortalTravelAgent();
        final PlayerPortalEvent event = new PlayerPortalEvent((Player)entityplayer.getBukkitEntity(), fromLocation, toLocation, (TravelAgent)pta);
        Bukkit.getServer().getPluginManager().callEvent((Event)event);
        if (event.isCancelled() || event.getTo() == null) {
            return;
        }
        Location finalLocation = event.getTo();
        if (event.useTravelAgent()) {
            finalLocation = pta.findOrCreate(finalLocation);
        }
        toWorld = ((CraftWorld)finalLocation.getWorld()).getHandle();
        this.moveToWorld(entityplayer, toWorld.dimension, finalLocation);
    }
    
    public void b() {
        for (int i = 0; i < this.server.worlds.size(); ++i) {
            this.server.worlds.get(i).manager.flush();
        }
    }
    
    public void flagDirty(final int i, final int j, final int k, final int l) {
        this.getPlayerManager(l).flagDirty(i, j, k);
    }
    
    public void sendAll(final Packet packet) {
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityPlayer entityplayer = this.players.get(i);
            entityplayer.netServerHandler.sendPacket(packet);
        }
    }
    
    public void a(final Packet packet, final int i) {
        for (int j = 0; j < this.players.size(); ++j) {
            final EntityPlayer entityplayer = this.players.get(j);
            if (entityplayer.dimension == i) {
                entityplayer.netServerHandler.sendPacket(packet);
            }
        }
    }
    
    public String c() {
        String s = "";
        for (int i = 0; i < this.players.size(); ++i) {
            if (i > 0) {
                s += ", ";
            }
            s += this.players.get(i).name;
        }
        return s;
    }
    
    public void a(final String s) {
        this.banByName.add(s.toLowerCase());
        this.h();
    }
    
    public void b(final String s) {
        this.banByName.remove(s.toLowerCase());
        this.h();
    }
    
    private void g() {
        try {
            this.banByName.clear();
            final BufferedReader bufferedreader = new BufferedReader(new FileReader(this.j));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                this.banByName.add(s.trim().toLowerCase());
            }
            bufferedreader.close();
        }
        catch (Exception exception) {
            ServerConfigurationManager.a.warning("Failed to load ban list: " + exception);
        }
    }
    
    private void h() {
        try {
            final PrintWriter printwriter = new PrintWriter(new FileWriter(this.j, false));
            for (final String s : this.banByName) {
                printwriter.println(s);
            }
            printwriter.close();
        }
        catch (Exception exception) {
            ServerConfigurationManager.a.warning("Failed to save ban list: " + exception);
        }
    }
    
    public void c(final String s) {
        this.banByIP.add(s.toLowerCase());
        this.j();
    }
    
    public void d(final String s) {
        this.banByIP.remove(s.toLowerCase());
        this.j();
    }
    
    private void i() {
        try {
            this.banByIP.clear();
            final BufferedReader bufferedreader = new BufferedReader(new FileReader(this.k));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                this.banByIP.add(s.trim().toLowerCase());
            }
            bufferedreader.close();
        }
        catch (Exception exception) {
            ServerConfigurationManager.a.warning("Failed to load ip ban list: " + exception);
        }
    }
    
    private void j() {
        try {
            final PrintWriter printwriter = new PrintWriter(new FileWriter(this.k, false));
            for (final String s : this.banByIP) {
                printwriter.println(s);
            }
            printwriter.close();
        }
        catch (Exception exception) {
            ServerConfigurationManager.a.warning("Failed to save ip ban list: " + exception);
        }
    }
    
    public void e(final String s) {
        this.h.add(s.toLowerCase());
        this.l();
        final Player player = this.server.server.getPlayer(s);
        if (player != null) {
            player.recalculatePermissions();
        }
    }
    
    public void f(final String s) {
        this.h.remove(s.toLowerCase());
        this.l();
        final Player player = this.server.server.getPlayer(s);
        if (player != null) {
            player.recalculatePermissions();
        }
    }
    
    private void k() {
        try {
            this.h.clear();
            final BufferedReader bufferedreader = new BufferedReader(new FileReader(this.l));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                this.h.add(s.trim().toLowerCase());
            }
            bufferedreader.close();
        }
        catch (Exception exception) {
            ServerConfigurationManager.a.warning("Failed to load ops: " + exception);
        }
    }
    
    private void l() {
        try {
            final PrintWriter printwriter = new PrintWriter(new FileWriter(this.l, false));
            for (final String s : this.h) {
                printwriter.println(s);
            }
            printwriter.close();
        }
        catch (Exception exception) {
            ServerConfigurationManager.a.warning("Failed to save ops: " + exception);
        }
    }
    
    private void m() {
        try {
            this.i.clear();
            final BufferedReader bufferedreader = new BufferedReader(new FileReader(this.m));
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {
                this.i.add(s.trim().toLowerCase());
            }
            bufferedreader.close();
        }
        catch (Exception exception) {
            ServerConfigurationManager.a.warning("Failed to load white-list: " + exception);
        }
    }
    
    private void n() {
        try {
            final PrintWriter printwriter = new PrintWriter(new FileWriter(this.m, false));
            for (final String s : this.i) {
                printwriter.println(s);
            }
            printwriter.close();
        }
        catch (Exception exception) {
            ServerConfigurationManager.a.warning("Failed to save white-list: " + exception);
        }
    }
    
    public boolean isWhitelisted(String s) {
        s = s.trim().toLowerCase();
        return !this.o || this.h.contains(s) || this.i.contains(s);
    }
    
    public boolean isOp(final String s) {
        return this.h.contains(s.trim().toLowerCase());
    }
    
    public EntityPlayer i(final String s) {
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityPlayer entityplayer = this.players.get(i);
            if (entityplayer.name.equalsIgnoreCase(s)) {
                return entityplayer;
            }
        }
        return null;
    }
    
    public void a(final String s, final String s1) {
        final EntityPlayer entityplayer = this.i(s);
        if (entityplayer != null) {
            entityplayer.netServerHandler.sendPacket((Packet)new Packet3Chat(s1));
        }
    }
    
    public void sendPacketNearby(final double d0, final double d1, final double d2, final double d3, final int i, final Packet packet) {
        this.sendPacketNearby(null, d0, d1, d2, d3, i, packet);
    }
    
    public void sendPacketNearby(final EntityHuman entityhuman, final double d0, final double d1, final double d2, final double d3, final int i, final Packet packet) {
        for (int j = 0; j < this.players.size(); ++j) {
            final EntityPlayer entityplayer = this.players.get(j);
            if (entityplayer != entityhuman && entityplayer.dimension == i) {
                final double d4 = d0 - entityplayer.locX;
                final double d5 = d1 - entityplayer.locY;
                final double d6 = d2 - entityplayer.locZ;
                if (d4 * d4 + d5 * d5 + d6 * d6 < d3 * d3) {
                    entityplayer.netServerHandler.sendPacket(packet);
                }
            }
        }
    }
    
    public void j(final String s) {
        final Packet3Chat packet3chat = new Packet3Chat(s);
        for (int i = 0; i < this.players.size(); ++i) {
            final EntityPlayer entityplayer = this.players.get(i);
            if (this.isOp(entityplayer.name)) {
                entityplayer.netServerHandler.sendPacket((Packet)packet3chat);
            }
        }
    }
    
    public boolean a(final String s, final Packet packet) {
        final EntityPlayer entityplayer = this.i(s);
        if (entityplayer != null) {
            entityplayer.netServerHandler.sendPacket(packet);
            return true;
        }
        return false;
    }
    
    public void savePlayers() {
        for (int i = 0; i < this.players.size(); ++i) {
            this.playerFileData.a((EntityHuman)this.players.get(i));
        }
    }
    
    public void a(final int i, final int j, final int k, final TileEntity tileentity) {
    }
    
    public void k(final String s) {
        this.i.add(s);
        this.n();
    }
    
    public void l(final String s) {
        this.i.remove(s);
        this.n();
    }
    
    public Set e() {
        return this.i;
    }
    
    public void f() {
        this.m();
    }
    
    public void a(final EntityPlayer entityplayer, final WorldServer worldserver) {
        entityplayer.netServerHandler.sendPacket((Packet)new Packet4UpdateTime(worldserver.getTime()));
        if (worldserver.v()) {
            entityplayer.netServerHandler.sendPacket((Packet)new Packet70Bed(1));
        }
    }
    
    public void updateClient(final EntityPlayer entityplayer) {
        entityplayer.updateInventory(entityplayer.defaultContainer);
        entityplayer.C();
    }
    
    static {
        ServerConfigurationManager.a = Logger.getLogger("Minecraft");
    }
}
