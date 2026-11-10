package metweaks.block;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gnu.trove.map.TObjectIntMap;
import lotr.common.content.LOTRBlocks;
import lotr.common.content.LOTRItems;
import lotr.common.item.LOTRItemMug;
import lotr.common.world.LOTRDimension;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeDecorator;
import metweaks.ASMConfig;
import metweaks.MeTweaks;
import metweaks.core.Hooks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class BlocksItemsInit {
	public static void disableTinCopperSilverGen() {
		try {
			Field field_biomeOres = ReflectionHelper.findField(LOTRBiomeDecorator.class, "biomeOres");
			Field field_oreGen = ReflectionHelper.findField(Class.forName("lotr.common.world.biome.LOTRBiomeDecorator$OreGenerant"), "oreGen");
			Field field_block = ReflectionHelper.findField(WorldGenMinable.class, "field_150519_a");
			
				for(LOTRBiome biome : LOTRDimension.MIDDLE_EARTH.biomeList) {
					if(biome != null) {
						@SuppressWarnings("rawtypes")
						Iterator ores = ((List) field_biomeOres.get(biome.decorator)).iterator();
						while(ores.hasNext()) {
							Block block = (Block) field_block.get(field_oreGen.get(ores.next()));
							if(block == LOTRBlocks.oreCopper || block == LOTRBlocks.oreTin || block == LOTRBlocks.oreSilver) {
								
								ores.remove();
							}
						}
					}
				}
			}
			catch(Exception ec) {
				ec.printStackTrace();
			}
	}
	
	public static void improveBlockBreakSpeeds() {
		String tool = "axe";
		Blocks.melon_block.setHarvestLevel(tool, 0);
		tool = "hoe";
		Blocks.hay_block.setHarvestLevel(tool, 0);
		if(MeTweaks.lotr) {
			LOTRBlocks.thatch.setHarvestLevel(tool, 0);
			LOTRBlocks.stairsThatch.setHarvestLevel(tool, 0);
			LOTRBlocks.slabDoubleThatch.setHarvestLevel(tool, 0);
			LOTRBlocks.slabSingleThatch.setHarvestLevel(tool, 0);
			LOTRBlocks.daub.setHarvestLevel(tool, 0);
			LOTRBlocks.stairsReed.setHarvestLevel(tool, 0);
			LOTRBlocks.reedBars.setHarvestLevel(tool, 0);
		}
	}
	
	public static void foodConsumeDurations() {
		TObjectIntMap<Item> durations = Hooks.consumeDurations;
		int half = 16;
		
		durations.put(Items.cookie, half);
		durations.put(Items.melon, half);
		
		if(MeTweaks.lotr) {
			durations.put(LOTRItems.blackberry, half);
			durations.put(LOTRItems.blueberry, half);
			durations.put(LOTRItems.cranberry, half);
			durations.put(LOTRItems.elderberry, half);
			durations.put(LOTRItems.raspberry, half);
			durations.put(LOTRItems.banana, half);
			durations.put(LOTRItems.cherry, half);
			durations.put(LOTRItems.date, half);
			durations.put(LOTRItems.chestnutRoast, half);
			durations.put(LOTRItems.lemon, half);
			durations.put(LOTRItems.lime, half);
			durations.put(LOTRItems.leek, half);
			durations.put(LOTRItems.almond, half);
			durations.put(LOTRItems.grapeRed, half);
			durations.put(LOTRItems.grapeWhite, half);
			durations.put(LOTRItems.raisins, half);
			durations.put(LOTRItems.olive, half);
		}
	}
	
	public static void draughtMaxStacksize() {
		int draughtMaxStacksize = ASMConfig.draughtMaxStacksize;
		
		Iterator<Item> it = GameData.getItemRegistry().iterator();
		
		while(it.hasNext()) {
			Item item = it.next();
			if(item instanceof LOTRItemMug && ((LOTRItemMug) item).isFullMug) {
				item.setMaxStackSize(draughtMaxStacksize);
			}
		}
		
		LOTRItems.entDraught.setMaxStackSize(draughtMaxStacksize);
		
	}
}
