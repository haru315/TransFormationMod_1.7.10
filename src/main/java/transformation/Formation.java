package transformation;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Formation {
	public EntityLivingBase entity;

    public Formation(World world, NBTTagCompound tag)
    {
        if(tag != null)
        {
        	entity = (EntityLivingBase)EntityList.createEntityFromNBT(tag, world);
        }
    }
    public void readTag(World world, NBTTagCompound tag)
    {
    	entity = (EntityLivingBase)EntityList.createEntityFromNBT(tag, world);
    }
    public NBTTagCompound getTag()
    {
        NBTTagCompound tag1 = new NBTTagCompound();
        if(entity != null)
        {
        	entity.writeToNBTOptional(tag1);
        }
		return tag1;

    }
    public static NBTTagCompound getEntityLivingBaseTag(EntityLivingBase entity)
    {
        NBTTagCompound tag1 = new NBTTagCompound();
        if(entity != null)
        {
        	entity.writeToNBTOptional(tag1);
        }
		return tag1;

    }
}
