package metweaks.features;

import lotr.common.content.LOTRItems;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.npc.LOTRUnitTradeEntries;
import lotr.common.entity.npc.LOTRUnitTradeEntry;
import lotr.common.entity.npc.rohan.LOTREntityRohanBannerBearer;
import lotr.common.entity.npc.rohan.LOTREntityRohirrim;
import lotr.common.entity.npc.rohan.LOTREntityRohirrimArcher;

public class MountedBannerBearers {
	public static void setup() {
		// Trader Interaction	
		LOTRUnitTradeEntries.ROHIRRIM_MARSHAL = new LOTRUnitTradeEntries(150.0F, new LOTRUnitTradeEntry[] {
				new LOTRUnitTradeEntry(LOTREntityRohirrim.class, 30, 0.0F), 
				new LOTRUnitTradeEntry(LOTREntityRohirrimArcher.class, 50, 50.0F), 
				new LOTRUnitTradeEntry(LOTREntityRohirrim.class, LOTREntityHorse.class, "Rohirrim_Horse", 50, 100.0F)
				        .setMountArmor(LOTRItems.horseArmorRohan), 
				new LOTRUnitTradeEntry(LOTREntityRohirrimArcher.class, LOTREntityHorse.class, "RohirrimArcher_Horse", 70, 150.0F)
				        .setMountArmor(LOTRItems.horseArmorRohan), 
				new LOTRUnitTradeEntry(LOTREntityRohanBannerBearer.class, 50, 150.0F),
				new LOTRUnitTradeEntry(LOTREntityRohanBannerBearer.class, LOTREntityHorse.class, "RohirrimBanner_Horse", 90, 200.0F)
		        	.setMountArmor(LOTRItems.horseArmorRohan).setPledgeExclusive()
		});	  
	}
}
