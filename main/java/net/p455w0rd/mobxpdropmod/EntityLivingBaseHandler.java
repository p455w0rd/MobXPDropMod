package net.p455w0rd.mobxpdropmod;

import java.lang.reflect.Method;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityLivingBaseHandler {
	@SubscribeEvent
	public void dropEvent(LivingDropsEvent event){
		EntityLivingBase entity = event.entityLiving;
		World world = entity.worldObj;
		if(entity != null){
			EntityLivingBase elb = entity;
			if(!world.isRemote && !event.recentlyHit && world.getGameRules().getGameRuleBooleanValue("doMobLoot")){
				int i = 0;
				try{
					Method m = ReflectionHelper.findMethod(EntityLivingBase.class, elb, new String[] {"func_70693_a", "getExperiencePoints"}, EntityPlayer.class);
					m.setAccessible(true);
					i = (Integer)m.invoke(elb, FakePlayerFactory.getMinecraft((WorldServer)world));
				}catch(Exception e){ }
				while(i > 0){
					int cap = EntityXPOrb.getXPSplit(i);
					i -= cap;
					elb.worldObj.spawnEntityInWorld(new EntityXPOrb(elb.worldObj, elb.posX, elb.posY, elb.posZ, cap));
				}
			}
		}
	}
}