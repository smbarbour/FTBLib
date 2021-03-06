package ftb.lib.api.block;

import ftb.lib.api.item.IItemLM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

public interface IBlockLM extends IItemLM
{
	Class<? extends ItemBlock> getItemBlock();
	
	@SideOnly(Side.CLIENT)
	void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean adv);
}