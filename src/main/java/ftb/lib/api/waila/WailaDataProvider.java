package ftb.lib.api.waila;

import mcp.mobius.waila.api.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class WailaDataProvider implements IWailaDataProvider
{
	public final BasicWailaHandler handler;
	private static WailaDataAccessor dataAccessor = null;
	
	public WailaDataProvider(BasicWailaHandler h)
	{ handler = h; }
	
	private static WailaDataAccessor getData(IWailaDataAccessor i)
	{
		if(dataAccessor == null) dataAccessor = new WailaDataAccessor();
		dataAccessor.player = i.getPlayer();
		dataAccessor.world = i.getWorld();
		dataAccessor.position = i.getMOP();
		dataAccessor.tile = i.getTileEntity();
		dataAccessor.block = i.getBlock();
		dataAccessor.meta = i.getMetadata();
		dataAccessor.side = dataAccessor.position.sideHit;
		return dataAccessor;
	}
	
	public ItemStack getWailaStack(IWailaDataAccessor data, IWailaConfigHandler config)
	{ return handler.getWailaStack(getData(data)); }
	
	public List<String> getWailaHead(ItemStack is, List<String> l, IWailaDataAccessor data, IWailaConfigHandler config)
	{ return handler.getWailaHead(is, l, getData(data)); }
	
	public List<String> getWailaBody(ItemStack is, List<String> l, IWailaDataAccessor data, IWailaConfigHandler config)
	{ return handler.getWailaBody(is, l, getData(data)); }
	
	public List<String> getWailaTail(ItemStack is, List<String> l, IWailaDataAccessor data, IWailaConfigHandler config)
	{ return handler.getWailaTail(is, l, getData(data)); }
	
	public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, BlockPos blockPos)
	{ return null; }
}