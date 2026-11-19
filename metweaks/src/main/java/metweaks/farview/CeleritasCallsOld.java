package metweaks.farview;

import org.embeddedt.embeddium.impl.render.chunk.map.ChunkTrackerHolder;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;

public class CeleritasCallsOld {
	public static boolean preventUnload;
	
	public static void unloadChunk(World world, int x, int z) {
		preventUnload = false;
		ChunkTrackerHolder.get((WorldClient) world).onChunkStatusRemoved(x, z, 3);
	}
	
	public static void loadChunk(World world, int x, int z) {
		ChunkTrackerHolder.get((WorldClient) world).onChunkStatusAdded(x, z, 3);
	}
}
