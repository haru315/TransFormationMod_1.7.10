package transformation;

import java.lang.reflect.Method;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.EntityRenderer;

public class Accessor {
    public static Method renderHandMethod;

    @SideOnly(Side.CLIENT)
    public static void client()
    {
        renderHandMethod = getMethod(EntityRenderer.class, "renderHand", "func_78476_b");
    }

    public static Method getMethod(Class clazz, String... names)
    {
    	for (Method method : clazz.getDeclaredMethods())
        {
    		for (String name : names)
    		{
    			if (method.getName().equals(name))
                {
                	method.setAccessible(true);
                	return method;
                }
    		}
        }

    	return null;
    }

    public static void common()
    {

    }

    public static void renderHand(EntityRenderer obj, float f, int i)
    {
        try
        {
            renderHandMethod.invoke(obj, f, i);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
