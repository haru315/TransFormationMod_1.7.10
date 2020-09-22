package transformation.event;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.RendererLivingEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import transformation.Accessor;
import transformation.TransFormation;
import transformation.packet.MessagePlayerJoinInAnnouncement;
import transformation.packet.MessageServerSample;
import transformation.packet.PacketHandler;
import transformation.render.RenderPlayerHand;

public class EventHandler {

    /**
     * エンティティがWorldに接続した
     *  サーバーへの接続時はServer/Clientで発生。
     *  Entityがプレイヤーから見える範囲に入ってきた時にはClientのみで発生する。
     */
	// クライアント側で実行されることになる
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientEntityJoinWorld(EntityJoinWorldEvent event)  {
    	if(event.entity instanceof EntityLivingBase) {
            if(event.entity == Minecraft.getMinecraft().thePlayer) {
            	//プレイヤーの同期メッセージ
                EntityPlayer player = (EntityPlayer)event.entity;
                PacketHandler.INSTANCE.sendToServer(new MessagePlayerJoinInAnnouncement(player));
            }else {
            	//プレイヤー以外のエンティティの同期メッセージ
                PacketHandler.INSTANCE.sendToServer(new MessageServerSample(event.entity));
            }
    	}
    }

    /**手をレンダリング*/
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderHand(RenderHandEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
    	EntityClientPlayerMP player = mc.thePlayer;

    	GL11.glPushMatrix();
    	if (player.getEntityData().getBoolean(TransFormation.MOD_ID+"_trans") && player.getEntityData().getTag(TransFormation.MOD_ID+"_transID") != null) {

    		event.setCanceled(true);

    		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

    		NBTTagCompound tag =(NBTTagCompound)player.getEntityData().getTag(TransFormation.MOD_ID+"_transID");
    		EntityLivingBase entity = (EntityLivingBase)EntityList.createEntityFromNBT(tag, player.worldObj);

    		RendererLivingEntity rend = (RendererLivingEntity)RenderManager.instance.getEntityClassRenderObject(entity.getClass());
    		ModelRenderer assumedArm = RenderPlayerHand.getArm(RendererLivingEntityAccessor.getMainModel(rend));
			ResourceLocation resourceLoc = RenderPlayerHand.invokeGetEntityTexture(rend, rend.getClass(), entity);
			RenderPlayerHand renderHandInstance = new RenderPlayerHand();
			RenderPlayer rend1 = (RenderPlayer)RenderManager.instance.getEntityRenderObject(player);
			renderHandInstance.progress = 1.0F;
            renderHandInstance.setParent(rend1);
            renderHandInstance.resourceLoc = resourceLoc;
            renderHandInstance.replacement = assumedArm;

            RenderManager.instance.entityRenderMap.put(player.getClass(), renderHandInstance);


    	    Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class,Minecraft.getMinecraft(),"field_71428_T","timer");
    	    float partialTicks = timer.renderPartialTicks;
            Accessor.renderHand( mc.entityRenderer, partialTicks, 0);

            RenderManager.instance.entityRenderMap.put(player.getClass(), rend1);

    	}
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    /**エンティティをレンダーイベント*/
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderLiving(RenderLivingEvent.Pre event)
    {
    	if (event.entity.getEntityData().getBoolean(TransFormation.MOD_ID+"_trans") && event.entity.getEntityData().getTag(TransFormation.MOD_ID+"_transID") != null) {
    		//EntityPlayer  EntityPig  EntityCow  EntityOcelot EntityWolf
    		event.setCanceled(true);

    		// new FakePlayer((WorldServer)event.entity.worldObj,EntityHelperBase.getSimpleGameProfileFromName("Player544"));
    		// (EntityLivingBase)EntityList.createEntityFromNBT(tag, event.entity.worldObj);

    	    Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class,Minecraft.getMinecraft(),"field_71428_T","timer");
    	    float partialTicks = timer.renderPartialTicks;

    		NBTTagCompound tag =(NBTTagCompound)event.entity.getEntityData().getTag(TransFormation.MOD_ID+"_transID");
    		EntityLivingBase entity = (EntityLivingBase)EntityList.createEntityFromNBT(tag, event.entity.worldObj);
    		float ySize = 0;
    		if(event.entity == Minecraft.getMinecraft().thePlayer) {
    			ySize = event.entity.yOffset - entity.getEyeHeight() + ((EntityPlayer)event.entity).getDefaultEyeHeight();
    		}

    		entity.prevRotationYawHead = event.entity.prevRotationYawHead;
    		entity.prevRotationYaw = event.entity.prevRotationYaw;
    		entity.prevRotationPitch = event.entity.prevRotationPitch;
    		entity.prevRenderYawOffset = event.entity.prevRenderYawOffset;
    		entity.prevLimbSwingAmount = event.entity.prevLimbSwingAmount;
    		entity.prevSwingProgress = event.entity.prevSwingProgress;
    		entity.prevPosX = event.entity.prevPosX;
    		entity.prevPosY = event.entity.prevPosY;
    		entity.prevPosZ = event.entity.prevPosZ;

    		entity.rotationYawHead = event.entity.rotationYawHead;
    		entity.rotationYaw = event.entity.rotationYaw;
    		entity.rotationPitch = event.entity.rotationPitch;
    		entity.renderYawOffset = event.entity.renderYawOffset;
    		entity.limbSwingAmount = event.entity.limbSwingAmount;
    		entity.swingProgress = event.entity.swingProgress;
    		entity.limbSwing = event.entity.limbSwing;
    		entity.posX = event.entity.posX;
    		entity.posY = event.entity.posY;
    		entity.posZ = event.entity.posZ;
    		entity.motionX = event.entity.motionX;
    		entity.motionY = event.entity.motionY;
    		entity.motionZ = event.entity.motionZ;
    		entity.ticksExisted = event.entity.ticksExisted;
    		entity.isAirBorne = event.entity.isAirBorne;
    		entity.moveStrafing  = event.entity.moveStrafing;
    		entity.moveForward = event.entity.moveForward;
    		entity.dimension = event.entity.dimension;
    		entity.worldObj = event.entity.worldObj;
    		entity.ridingEntity  = event.entity.ridingEntity;
    		entity.hurtTime = event.entity.hurtTime;
    		entity.deathTime = event.entity.deathTime;
    		entity.isSwingInProgress = event.entity.isSwingInProgress;

    		entity.noClip = false;
    		entity.boundingBox.setBB( event.entity.boundingBox);
    		entity.moveEntity(0.0D, -0.01D, 0.0D);
    		entity.prevPosY =  event.entity.prevPosY;

    		entity.setSneaking(event.entity.isSneaking());
            entity.setSprinting(event.entity.isSprinting());
            entity.setInvisible(event.entity.isInvisible());
    		entity.setHealth(entity.getMaxHealth() * ( event.entity.getHealth() /  event.entity.getMaxHealth()));

    		//装備を同期
            for(int i = 0; i < 5; i++){
                if(entity.getEquipmentInSlot(i) == null && event.entity.getEquipmentInSlot(i) != null ||
                		entity.getEquipmentInSlot(i) != null && event.entity.getEquipmentInSlot(i) == null ||
                		entity.getEquipmentInSlot(i) != null && event.entity.getEquipmentInSlot(i) != null &&
                       !entity.getEquipmentInSlot(i).isItemEqual(event.entity.getEquipmentInSlot(i)))
                {
                	entity.setCurrentItemOrArmor(i, event.entity.getEquipmentInSlot(i) != null ? event.entity.getEquipmentInSlot(i).copy() : null);
                }
            }

    		int i = entity.getBrightnessForRender((float)partialTicks);

            if (event.entity.isBurning())
            {
                i = 15728880;
            }
            int j = i % 65536;
            int k = i / 65536;
            //　これでエンティティの明るさをいじる
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);

            if(Minecraft.getMinecraft().currentScreen instanceof GuiContainer && RenderManager.instance.playerViewY == 180.0F)
            {/*
                EntityLivingBase renderView = Minecraft.getMinecraft().renderViewEntity;

                entity.renderYawOffset = renderView.renderYawOffset;
                entity.rotationYaw = renderView.rotationYaw;
                entity.rotationPitch = renderView.rotationPitch;
                entity.prevRotationYawHead = renderView.prevRotationYawHead;
                entity.rotationYawHead = renderView.rotationYawHead;*/
                partialTicks = 1.0F;
                ySize = 0;
            }

    		Render rend = RenderManager.instance.getEntityClassRenderObject(entity.getClass());

    		GL11.glPushMatrix();
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    		rend.doRender(entity, event.x, event.y + ySize, event.z, 1.0F, partialTicks);
            GL11.glPopMatrix();
    	}
    }

    /*
	//プレイヤーレンダーイベント
    @SideOnly(Side.CLIENT)
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {LivingEvent.LivingUpdateEvent
    }
    //エンティティ更新
	@SubscribeEvent
	public void onLivingUpdate(EntityEvent event){
	}
    */
    /**エンティティ更新*/
	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
	{
		if(event.entity.getEntityData().getBoolean(TransFormation.MOD_ID+"_trans") && event.entity.getEntityData().getTag(TransFormation.MOD_ID+"_transID") != null) {

			// EntityDataから取得したNBTTagCompoundをEntityLivingBaseに変換
			NBTTagCompound tag =(NBTTagCompound)event.entity.getEntityData().getTag(TransFormation.MOD_ID+"_transID");
    		EntityLivingBase entity = (EntityLivingBase)EntityList.createEntityFromNBT(tag, event.entity.worldObj);

    		// プレイヤーの処理
    		if(event.entity instanceof EntityPlayer) {
    			EntityPlayer player = (EntityPlayer)event.entity;
    			player.eyeHeight = entity.getEyeHeight() - player.yOffset;
    		}else {
    			//event.setCanceled(true);
        		event.entity.height = entity.getEyeHeight() - event.entity.yOffset;
    		}
    		// サイズ変更
    		setEntitySize(event.entity, entity.width, entity.height);
		}
	}


		   private static java.lang.reflect.Method  methodEntitySetSize;
		   private static java.lang.reflect.Method methodZombieSetSize;
		   private static java.lang.reflect.Method  methodZombieSetSize2;
		   private static java.lang.reflect.Method  methodAgeableSetSize;
		   private static java.lang.reflect.Method  methodAgeableSetSize2;
		   public static void setEntitySize(Entity entity, float width, float height)
		   {
			      try {
			         if(entity instanceof EntityZombie) {
						if(methodZombieSetSize == null) {
			               methodZombieSetSize = ReflectionHelper.findMethod(EntityZombie.class, (EntityZombie)entity, new String[]{"setSize", "func_70105_a", "a"}, new Class[]{Float.TYPE, Float.TYPE});
			            }

						if(methodZombieSetSize2 == null) {
			               methodZombieSetSize2 = ReflectionHelper.findMethod(EntityZombie.class, (EntityZombie)entity, new String[]{"func_146069_a", "a"}, new Class[]{Float.TYPE});
			            }

			            methodZombieSetSize.invoke(entity, new Object[]{Float.valueOf(width), Float.valueOf(height)});
			            methodZombieSetSize2.invoke(entity, new Object[]{Float.valueOf(1.0F)});
			         } else if(entity instanceof EntityAgeable) {
						if(methodAgeableSetSize == null) {
			               methodAgeableSetSize = ReflectionHelper.findMethod(EntityAgeable.class, (EntityAgeable)entity, new String[]{"setSize", "func_70105_a", "a"}, new Class[]{Float.TYPE, Float.TYPE});
			            }

						if(methodAgeableSetSize2 == null) {
			               methodAgeableSetSize2 = ReflectionHelper.findMethod(EntityAgeable.class, (EntityAgeable)entity, new String[]{"setScale", "func_98055_j", "a"}, new Class[]{Float.TYPE});
			            }

			            methodAgeableSetSize.invoke(entity, new Object[]{Float.valueOf(width), Float.valueOf(height)});
			            methodAgeableSetSize2.invoke(entity, new Object[]{Float.valueOf(1.0F)});
			         } else {
						if(methodEntitySetSize == null) {
			               methodEntitySetSize = ReflectionHelper.findMethod(Entity.class, entity, new String[]{"setSize", "func_70105_a", "a"}, new Class[]{Float.TYPE, Float.TYPE});
			            }

			            methodEntitySetSize.invoke(entity, new Object[]{Float.valueOf(width), Float.valueOf(height)});
			         }
			      } catch (IllegalAccessException var4) {
			         ;
			      } catch (IllegalArgumentException var5) {
			         ;
			      } catch (InvocationTargetException var6) {
			         ;
			      }
		   }
}



