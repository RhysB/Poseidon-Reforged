// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

class MinecartTrackLogic
{
    private World b;
    private int c;
    private int d;
    private int e;
    private final boolean f;
    private List g;
    final BlockMinecartTrack a;
    
    public MinecartTrackLogic(final BlockMinecartTrack blockminecarttrack, final World world, final int i, final int j, final int k) {
        this.a = blockminecarttrack;
        this.g = new ArrayList();
        this.b = world;
        this.c = i;
        this.d = j;
        this.e = k;
        final int l = world.getTypeId(i, j, k);
        int i2 = world.getData(i, j, k);
        if (BlockMinecartTrack.a((BlockMinecartTrack)Block.byId[l])) {
            this.f = true;
            i2 &= 0xFFFFFFF7;
        }
        else {
            this.f = false;
        }
        this.a(i2);
    }
    
    private void a(final int i) {
        this.g.clear();
        if (i == 0) {
            this.g.add(new ChunkPosition(this.c, this.d, this.e - 1));
            this.g.add(new ChunkPosition(this.c, this.d, this.e + 1));
        }
        else if (i == 1) {
            this.g.add(new ChunkPosition(this.c - 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c + 1, this.d, this.e));
        }
        else if (i == 2) {
            this.g.add(new ChunkPosition(this.c - 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c + 1, this.d + 1, this.e));
        }
        else if (i == 3) {
            this.g.add(new ChunkPosition(this.c - 1, this.d + 1, this.e));
            this.g.add(new ChunkPosition(this.c + 1, this.d, this.e));
        }
        else if (i == 4) {
            this.g.add(new ChunkPosition(this.c, this.d + 1, this.e - 1));
            this.g.add(new ChunkPosition(this.c, this.d, this.e + 1));
        }
        else if (i == 5) {
            this.g.add(new ChunkPosition(this.c, this.d, this.e - 1));
            this.g.add(new ChunkPosition(this.c, this.d + 1, this.e + 1));
        }
        else if (i == 6) {
            this.g.add(new ChunkPosition(this.c + 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c, this.d, this.e + 1));
        }
        else if (i == 7) {
            this.g.add(new ChunkPosition(this.c - 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c, this.d, this.e + 1));
        }
        else if (i == 8) {
            this.g.add(new ChunkPosition(this.c - 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c, this.d, this.e - 1));
        }
        else if (i == 9) {
            this.g.add(new ChunkPosition(this.c + 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c, this.d, this.e - 1));
        }
    }
    
    private void a() {
        for (int i = 0; i < this.g.size(); ++i) {
            final MinecartTrackLogic minecarttracklogic = this.a(this.g.get(i));
            if (minecarttracklogic == null || !minecarttracklogic.b(this)) {
                this.g.remove(i--);
            }
            else {
                this.g.set(i, new ChunkPosition(minecarttracklogic.c, minecarttracklogic.d, minecarttracklogic.e));
            }
        }
    }
    
    private boolean a(final int i, final int j, final int k) {
        return BlockMinecartTrack.g(this.b, i, j, k) || BlockMinecartTrack.g(this.b, i, j + 1, k) || BlockMinecartTrack.g(this.b, i, j - 1, k);
    }
    
    private MinecartTrackLogic a(final ChunkPosition chunkposition) {
        if (BlockMinecartTrack.g(this.b, chunkposition.x, chunkposition.y, chunkposition.z)) {
            return new MinecartTrackLogic(this.a, this.b, chunkposition.x, chunkposition.y, chunkposition.z);
        }
        if (BlockMinecartTrack.g(this.b, chunkposition.x, chunkposition.y + 1, chunkposition.z)) {
            return new MinecartTrackLogic(this.a, this.b, chunkposition.x, chunkposition.y + 1, chunkposition.z);
        }
        if (BlockMinecartTrack.g(this.b, chunkposition.x, chunkposition.y - 1, chunkposition.z)) {
            return new MinecartTrackLogic(this.a, this.b, chunkposition.x, chunkposition.y - 1, chunkposition.z);
        }
        return null;
    }
    
    private boolean b(final MinecartTrackLogic minecarttracklogic) {
        for (int i = 0; i < this.g.size(); ++i) {
            final ChunkPosition chunkposition = this.g.get(i);
            if (chunkposition.x == minecarttracklogic.c && chunkposition.z == minecarttracklogic.e) {
                return true;
            }
        }
        return false;
    }
    
    private boolean b(final int i, final int j, final int k) {
        for (int l = 0; l < this.g.size(); ++l) {
            final ChunkPosition chunkposition = this.g.get(l);
            if (chunkposition.x == i && chunkposition.z == k) {
                return true;
            }
        }
        return false;
    }
    
    private int b() {
        int i = 0;
        if (this.a(this.c, this.d, this.e - 1)) {
            ++i;
        }
        if (this.a(this.c, this.d, this.e + 1)) {
            ++i;
        }
        if (this.a(this.c - 1, this.d, this.e)) {
            ++i;
        }
        if (this.a(this.c + 1, this.d, this.e)) {
            ++i;
        }
        return i;
    }
    
    private boolean c(final MinecartTrackLogic minecarttracklogic) {
        if (this.b(minecarttracklogic)) {
            return true;
        }
        if (this.g.size() == 2) {
            return false;
        }
        if (this.g.size() == 0) {
            return true;
        }
        final ChunkPosition chunkposition = this.g.get(0);
        return (minecarttracklogic.d == this.d && chunkposition.y == this.d) || true;
    }
    
    private void d(final MinecartTrackLogic minecarttracklogic) {
        this.g.add(new ChunkPosition(minecarttracklogic.c, minecarttracklogic.d, minecarttracklogic.e));
        final boolean flag = this.b(this.c, this.d, this.e - 1);
        final boolean flag2 = this.b(this.c, this.d, this.e + 1);
        final boolean flag3 = this.b(this.c - 1, this.d, this.e);
        final boolean flag4 = this.b(this.c + 1, this.d, this.e);
        byte byte0 = -1;
        if (flag || flag2) {
            byte0 = 0;
        }
        if (flag3 || flag4) {
            byte0 = 1;
        }
        if (!this.f) {
            if (flag2 && flag4 && !flag && !flag3) {
                byte0 = 6;
            }
            if (flag2 && flag3 && !flag && !flag4) {
                byte0 = 7;
            }
            if (flag && flag3 && !flag2 && !flag4) {
                byte0 = 8;
            }
            if (flag && flag4 && !flag2 && !flag3) {
                byte0 = 9;
            }
        }
        if (byte0 == 0) {
            if (BlockMinecartTrack.g(this.b, this.c, this.d + 1, this.e - 1)) {
                byte0 = 4;
            }
            if (BlockMinecartTrack.g(this.b, this.c, this.d + 1, this.e + 1)) {
                byte0 = 5;
            }
        }
        if (byte0 == 1) {
            if (BlockMinecartTrack.g(this.b, this.c + 1, this.d + 1, this.e)) {
                byte0 = 2;
            }
            if (BlockMinecartTrack.g(this.b, this.c - 1, this.d + 1, this.e)) {
                byte0 = 3;
            }
        }
        if (byte0 < 0) {
            byte0 = 0;
        }
        int i = byte0;
        if (this.f) {
            i = ((this.b.getData(this.c, this.d, this.e) & 0x8) | byte0);
        }
        this.b.setData(this.c, this.d, this.e, i);
    }
    
    private boolean c(final int i, final int j, final int k) {
        final MinecartTrackLogic minecarttracklogic = this.a(new ChunkPosition(i, j, k));
        if (minecarttracklogic == null) {
            return false;
        }
        minecarttracklogic.a();
        return minecarttracklogic.c(this);
    }
    
    public void a(final boolean flag, final boolean flag1) {
        final boolean flag2 = this.c(this.c, this.d, this.e - 1);
        final boolean flag3 = this.c(this.c, this.d, this.e + 1);
        final boolean flag4 = this.c(this.c - 1, this.d, this.e);
        final boolean flag5 = this.c(this.c + 1, this.d, this.e);
        byte byte0 = -1;
        if ((flag2 || flag3) && !flag4 && !flag5) {
            byte0 = 0;
        }
        if ((flag4 || flag5) && !flag2 && !flag3) {
            byte0 = 1;
        }
        if (!this.f) {
            if (flag3 && flag5 && !flag2 && !flag4) {
                byte0 = 6;
            }
            if (flag3 && flag4 && !flag2 && !flag5) {
                byte0 = 7;
            }
            if (flag2 && flag4 && !flag3 && !flag5) {
                byte0 = 8;
            }
            if (flag2 && flag5 && !flag3 && !flag4) {
                byte0 = 9;
            }
        }
        if (byte0 == -1) {
            if (flag2 || flag3) {
                byte0 = 0;
            }
            if (flag4 || flag5) {
                byte0 = 1;
            }
            if (!this.f) {
                if (flag) {
                    if (flag3 && flag5) {
                        byte0 = 6;
                    }
                    if (flag4 && flag3) {
                        byte0 = 7;
                    }
                    if (flag5 && flag2) {
                        byte0 = 9;
                    }
                    if (flag2 && flag4) {
                        byte0 = 8;
                    }
                }
                else {
                    if (flag2 && flag4) {
                        byte0 = 8;
                    }
                    if (flag5 && flag2) {
                        byte0 = 9;
                    }
                    if (flag4 && flag3) {
                        byte0 = 7;
                    }
                    if (flag3 && flag5) {
                        byte0 = 6;
                    }
                }
            }
        }
        if (byte0 == 0) {
            if (BlockMinecartTrack.g(this.b, this.c, this.d + 1, this.e - 1)) {
                byte0 = 4;
            }
            if (BlockMinecartTrack.g(this.b, this.c, this.d + 1, this.e + 1)) {
                byte0 = 5;
            }
        }
        if (byte0 == 1) {
            if (BlockMinecartTrack.g(this.b, this.c + 1, this.d + 1, this.e)) {
                byte0 = 2;
            }
            if (BlockMinecartTrack.g(this.b, this.c - 1, this.d + 1, this.e)) {
                byte0 = 3;
            }
        }
        if (byte0 < 0) {
            byte0 = 0;
        }
        this.a(byte0);
        int i = byte0;
        if (this.f) {
            i = ((this.b.getData(this.c, this.d, this.e) & 0x8) | byte0);
        }
        if (flag1 || this.b.getData(this.c, this.d, this.e) != i) {
            this.b.setData(this.c, this.d, this.e, i);
            for (int j = 0; j < this.g.size(); ++j) {
                final MinecartTrackLogic minecarttracklogic = this.a(this.g.get(j));
                if (minecarttracklogic != null) {
                    minecarttracklogic.a();
                    if (minecarttracklogic.c(this)) {
                        minecarttracklogic.d(this);
                    }
                }
            }
        }
    }
    
    static int a(final MinecartTrackLogic minecarttracklogic) {
        return minecarttracklogic.b();
    }
}
