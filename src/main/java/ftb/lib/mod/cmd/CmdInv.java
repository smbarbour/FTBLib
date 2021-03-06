package ftb.lib.mod.cmd;

import ftb.lib.*;
import ftb.lib.api.cmd.*;
import ftb.lib.api.item.LMInvUtils;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class CmdInv extends CommandSubLM
{
	public CmdInv()
	{
		super("ftb_inv", CommandLevel.OP);
	}
	
	public static class CmdSave extends CommandLM
	{
		public CmdSave(String s)
		{ super(s, CommandLevel.OP); }
		
		public String getCommandUsage(ICommandSender ics)
		{ return '/' + commandName + " <player> <file_id>"; }
		
		public boolean isUsernameIndex(String[] args, int i)
		{ return i == 0; }
		
		public void processCommand(ICommandSender ics, String[] args) throws CommandException
		{
			checkArgs(args, 2);
			EntityPlayerMP ep = getPlayer(ics, args[0]);
			File file = new File(FTBLib.folderLocal, "ftbu/playerinvs/" + UUIDTypeAdapterLM.getString(ep.getGameProfile().getId()) + "_" + args[1].toLowerCase() + ".dat");
			
			try
			{
				onInvCmd(file, ep);
			}
			catch(Exception e)
			{
				if(FTBLib.DEV_ENV) e.printStackTrace();
				throw new RawCommandException("Failed to load inventory!");
			}
		}
		
		protected void onInvCmd(File file, EntityPlayerMP ep) throws Exception
		{
			NBTTagCompound tag = new NBTTagCompound();
			LMInvUtils.writeItemsToNBT(ep.inventory, tag, "Inventory");
			
			if(FTBLib.isModInstalled(OtherMods.BAUBLES))
				LMInvUtils.writeItemsToNBT(BaublesHelper.getBaubles(ep), tag, "Baubles");
			
			LMNBTUtils.writeTag(file, tag);
		}
	}
	
	public static class CmdLoad extends CmdSave
	{
		public CmdLoad(String s)
		{ super(s); }
		
		protected void onInvCmd(File file, EntityPlayerMP ep) throws Exception
		{
			NBTTagCompound tag = LMNBTUtils.readTag(file);
			
			LMInvUtils.readItemsFromNBT(ep.inventory, tag, "Inventory");
			
			if(FTBLib.isModInstalled(OtherMods.BAUBLES))
				LMInvUtils.readItemsFromNBT(BaublesHelper.getBaubles(ep), tag, "Baubles");
		}
	}
	
	public static class CmdList extends CommandLM
	{
		public CmdList(String s)
		{ super(s, CommandLevel.OP); }
		
		public void processCommand(ICommandSender ics, String[] args) throws CommandException
		{
		}
	}
}