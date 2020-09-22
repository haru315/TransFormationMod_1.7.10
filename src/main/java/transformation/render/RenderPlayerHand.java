package transformation.render;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class RenderPlayerHand extends RenderPlayer
{
	public float progress;

	public RenderPlayer parent;

	public ModelBiped biped;

	public ModelRenderer replacement;

	public ResourceLocation resourceLoc;

	@Override
    public void renderFirstPersonArm(EntityPlayer par1EntityPlayer)
    {
    	if(replacement != null)
    	{
	        float f = 1.0F;

	        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLoc); //try func_110776_a

	        GL11.glColor4f(f, f, f, progress);
	    	GL11.glEnable(GL11.GL_BLEND);
	    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

	    	ModelRenderer arm = biped.bipedRightArm;
    		biped.bipedRightArm = replacement;

    		//player arms are 12 blocks long
    		int heightDiff = 12 - modelHeight(replacement);
    		float rotX = replacement.rotationPointX;
    		float rotY = replacement.rotationPointY;
    		float rotZ = replacement.rotationPointZ;

    		float angX = replacement.rotateAngleX;
    		float angY = replacement.rotateAngleY;
    		float angZ = replacement.rotateAngleZ;

	        replacement.rotationPointX = arm.rotationPointX;
	        replacement.rotationPointY = arm.rotationPointY + heightDiff;
	        replacement.rotationPointZ = arm.rotationPointZ;

	        biped.onGround = 0.0F;
	        biped.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, par1EntityPlayer);
	        biped.bipedRightArm.render(0.0625F);

	        biped.bipedRightArm = arm;

	        replacement.rotationPointX = rotX;
	        replacement.rotationPointY = rotY;
	        replacement.rotationPointZ = rotZ;

	        replacement.rotateAngleX = angX;
	        replacement.rotateAngleY = angY;
	        replacement.rotateAngleZ = angZ;

	    	GL11.glDisable(GL11.GL_BLEND);

	        GL11.glColor4f(f, f, f, 1.0F);
       	}
    }

	public void setParent(RenderPlayer render)
	{
		if(parent != render)
		{
            biped = render.modelBipedMain;
		}
		parent = render;
	}
	// ここからstaticメソッド
	public static int modelHeight(ModelRenderer model)
	{
		int height = 0;//Y1 lower than Y2
		for(int i = 0; i < model.cubeList.size(); i++)
		{
			ModelBox box = (ModelBox)model.cubeList.get(i);
			if((int)Math.abs(box.posY2 - box.posY1) > height)
			{
				height = (int)Math.abs(box.posY2 - box.posY1);
			}
		}
		return height;
	}
    public static ModelRenderer getArm(ModelBase parent)
	{
		if(parent != null)
		{
			Class clz = parent.getClass();
			if(clz == ModelHorse.class)
			{
				ModelHorse dummy = new ModelHorse();

				ModelRenderer leg = new ModelRenderer(dummy, 60, 29);
		        leg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4);
		        leg.setRotationPoint(-4.0F, 9.0F, -8.0F);

		        ModelRenderer shin = new ModelRenderer(dummy, 60, 41);
		        shin.addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3);
		        shin.setRotationPoint(0.0F, 7.0F, 0.0F);
		        leg.addChild(shin);

		        ModelRenderer hoof = new ModelRenderer(dummy, 60, 51);
		        hoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4);
		        hoof.setRotationPoint(0.0F, 0.0F, 0.0F);

		        shin.addChild(hoof);

		        return leg;
			}
			else
			{
				while(clz != ModelBase.class && ModelBase.class.isAssignableFrom(clz))
				{
					try
					{
						Field[] fields = clz.getDeclaredFields();
						for(Field f : fields)
						{
							f.setAccessible(true);
							if(f.getType() == ModelRenderer.class)
							{
								if(clz == ModelBiped.class && (f.getName().equalsIgnoreCase("bipedRightArm") || f.getName().equalsIgnoreCase("f") || f.getName().equalsIgnoreCase("field_78112_f")) ||
									clz == ModelQuadruped.class && (f.getName().equalsIgnoreCase("leg3") || f.getName().equalsIgnoreCase("e") || f.getName().equalsIgnoreCase("field_78147_e")) ||
									clz == ModelCreeper.class && (f.getName().equalsIgnoreCase("leg3") || f.getName().equalsIgnoreCase("f") || f.getName().equalsIgnoreCase("field_78129_f")) ||
									clz == ModelIronGolem.class && (f.getName().equalsIgnoreCase("ironGolemRightArm") || f.getName().equalsIgnoreCase("c") || f.getName().equalsIgnoreCase("field_78177_c")) ||
									clz == ModelSpider.class && (f.getName().equalsIgnoreCase("spiderLeg7") || f.getName().equalsIgnoreCase("j") || f.getName().equalsIgnoreCase("field_78210_j")) ||
									clz == ModelWolf.class && (f.getName().equalsIgnoreCase("wolfLeg3") || f.getName().equalsIgnoreCase("e") || f.getName().equalsIgnoreCase("field_78182_e")) ||
									clz == ModelOcelot.class && (f.getName().equalsIgnoreCase("ocelotFrontRightLeg") || f.getName().equalsIgnoreCase("d") || f.getName().equalsIgnoreCase("field_78157_d")) ||
									clz != ModelBiped.class && clz != ModelQuadruped.class && clz != ModelCreeper.class && clz != ModelIronGolem.class && clz != ModelSpider.class && clz != ModelWolf.class && clz != ModelOcelot.class &&
									(f.getName().contains("Right") || f.getName().contains("right")) && (f.getName().contains("arm") || f.getName().contains("hand") || f.getName().contains("Arm") || f.getName().contains("Hand")))
								{
									ModelRenderer arm = (ModelRenderer)f.get(parent);
									if(arm != null)
									{
										return arm; // Add normal parent fields
									}
								}
							}
							else if(f.getType() == ModelRenderer[].class && clz == ModelSquid.class && (f.getName().equalsIgnoreCase("squidTentacles") || f.getName().equalsIgnoreCase("b") || f.getName().equalsIgnoreCase("field_78201_b")))
							{
								return ((ModelRenderer[])f.get(parent))[0];
							}
						}
						clz = clz.getSuperclass();
					}
					catch(Exception e)
					{
						throw new UnableToAccessFieldException(new String[0], e);
					}
				}
			}
		}

		return null;
	}
	private static boolean obfuscation;

    public static void detectObfuscation()
    {
        obfuscation = true;
        try
        {
            Field[] fields = Class.forName("net.minecraft.world.World").getDeclaredFields();
            for(Field f : fields)
            {
            	f.setAccessible(true);
            	if(f.getName().equalsIgnoreCase("loadedEntityList"))
            	{
            		obfuscation = false;
            		return;
            	}
            }
        }
        catch (Exception e)
        {
        }
    }
    public static ResourceLocation invokeGetEntityTexture(Render rend, Class clz, EntityLivingBase ent)
    {
        ResourceLocation loc = getEntTexture(rend, clz, ent);
        if(loc != null)
        {
            return loc;
        }
        return AbstractClientPlayer.locationStevePng;
    }

    private static ResourceLocation getEntTexture(Render rend, Class clz, EntityLivingBase ent)
    {
        try
        {
            Method m = clz.getDeclaredMethod(obfuscation ? "func_110775_a" : "getEntityTexture", Entity.class);
            m.setAccessible(true);
            return (ResourceLocation)m.invoke(rend, ent);
        }
        catch(NoSuchMethodException e)
        {
            if(clz != RendererLivingEntity.class)
            {
                return invokeGetEntityTexture(rend, clz.getSuperclass(), ent);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return AbstractClientPlayer.locationStevePng;
    }
}