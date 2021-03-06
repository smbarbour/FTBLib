package ftb.lib.mod.client;

import ftb.lib.FTBLib;
import ftb.lib.api.*;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.PlayerActionRegistry;
import ftb.lib.api.item.*;
import ftb.lib.mod.client.gui.GuiPlayerActions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Keyboard;

import java.util.List;

@SideOnly(Side.CLIENT)
public class FTBLibClientEventHandler
{
	public static final FTBLibClientEventHandler instance = new FTBLibClientEventHandler();
	
	/*@SubscribeEvent
	public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
	}*/
	
	@SubscribeEvent
	public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
	{
		if(ForgeWorldSP.inst != null)
		{
			ForgeWorldSP.inst.onClosed();
			ForgeWorldSP.inst = null;
		}
	}
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent e)
	{
		if(e.itemStack == null || e.itemStack.getItem() == null) return;
		
		if(FTBLibModClient.item_reg_names.get())
		{
			e.toolTip.add(LMInvUtils.getRegName(e.itemStack).toString());
		}
		
		if(FTBLibModClient.item_ore_names.get())
		{
			List<String> ores = ODItems.getOreNames(e.itemStack);
			
			if(ores != null && !ores.isEmpty())
			{
				e.toolTip.add("Ore Dictionary names:");
				
				for(String or : ores)
				{
					e.toolTip.add("> " + or);
				}
			}
		}
		
		if(FTBLib.ftbu != null) FTBLib.ftbu.onTooltip(e);
	}
	
	@SubscribeEvent
	public void onDrawDebugText(RenderGameOverlayEvent.Text e)
	{
		if(!FTBLibClient.mc.gameSettings.showDebugInfo)
		{
			if(FTBLib.DEV_ENV)
			{
				e.left.add("[MC " + EnumChatFormatting.GOLD + Loader.MC_VERSION + EnumChatFormatting.WHITE + " DevEnv]");
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityRightClick(EntityInteractEvent e)
	{
		if(e.entity.worldObj.isRemote && FTBLibClient.isIngame() && FTBLibModClient.player_options_shortcut.get() && e.entityPlayer.getGameProfile().getId().equals(FTBLibClient.mc.thePlayer.getGameProfile().getId()))
		{
			ForgePlayer self = ForgeWorldSP.inst.clientPlayer;
			ForgePlayer other = (FTBLib.ftbu == null) ? new ForgePlayerTemp(((EntityPlayer) e.target)) : ForgeWorldSP.inst.getPlayer(e.target);
			if(other != null)
			{
				List<PlayerAction> a = PlayerActionRegistry.getPlayerActions(PlayerAction.Type.OTHER, self, other, true);
				if(!a.isEmpty()) FTBLibClient.openGui(new GuiPlayerActions(self, other, a));
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyEvent(InputEvent.KeyInputEvent e)
	{
		if(Keyboard.getEventKeyState())
		{
			Shortcuts.onKeyPressed(Keyboard.getEventKey());
		}
	}
}