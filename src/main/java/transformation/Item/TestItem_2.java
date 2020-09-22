package transformation.Item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import transformation.TransFormation;

public class TestItem_2  extends Item
{
	/**アイテムでブロックを右クリックしたのメソッド。ItemMonsterPlacer参照。*/
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float posX, float posY, float posZ)
    {
            return false;
    }

    /**アイテムを使ったときのメソッド。ItemMonsterPlacer参照。*/
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
    	//サーバー側の場合は処理をスキップする
        //if (world.isRemote){return itemStack;}
		//player.addChatMessage( new ChatComponentText(""));

		//シフトクリック検知
    	if(!player.isSneaking()){
    		String id = "null";
    		NBTTagCompound tag = (NBTTagCompound)player.getEntityData().getTag(TransFormation.MOD_ID+"_transID");
    		if(tag != null) {
    			id = tag.getString("id");
    		}
    		player.addChatMessage( new ChatComponentText("world.isRemote="+world.isRemote+"  player.transID="+id));
    	}
		return itemStack;
    }
    /**
     * EntityLivingBaseを右クリックしたのメソッド。
     */
    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity)
    {
    	if(player.isSneaking()){//シフトクリック検知
    		String id = "null";
    		NBTTagCompound tag = (NBTTagCompound)entity.getEntityData().getTag(TransFormation.MOD_ID+"_transID");
    		if(tag != null) {
    			id = tag.getString("id");
    		}
    		player.addChatMessage( new ChatComponentText("world.isRemote="+entity.worldObj.isRemote+"  entity.transID="+id));
    	}
        return true;
    }
}