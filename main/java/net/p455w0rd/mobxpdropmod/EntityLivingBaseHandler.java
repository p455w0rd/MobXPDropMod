package net.p455w0rd.mobxpdropmod;

import java.lang.reflect.Method;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityLivingBaseHandler {
	public static final Method xpPoints = getExperiencePoints();

	@SubscribeEvent
	public void dropEvent(LivingDropsEvent event){
		EntityLivingBase entity = event.entityLiving;
		World world = entity.worldObj;
		if(entity != null){
			EntityLivingBase living = entity;
			if(!world.isRemote && !event.recentlyHit){
				int xp = 0;
				try{
					xp = (Integer)xpPoints.invoke(living, FakePlayerFactory.getMinecraft((WorldServer)world));
				}catch(Exception ex){ throw new RuntimeException(ex); }
				while(xp > 0){
					int cap = EntityXPOrb.getXPSplit(xp);
					xp -= cap;
					living.worldObj.spawnEntityInWorld(new EntityXPOrb(living.worldObj, living.posX, living.posY, living.posZ, cap));
				}
			}
		}
	}

	public static Method getExperiencePoints(){
		Method e = null;
		try {
			e = EntityLivingBase.class.getDeclaredMethod("getExperiencePoints", EntityPlayer.class);
			e.setAccessible(true);
		} catch(Exception ex) { throw new RuntimeException(ex); }
		return e;
	}
}
