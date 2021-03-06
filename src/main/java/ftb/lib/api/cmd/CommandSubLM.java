package ftb.lib.api.cmd;

import latmod.lib.LMStringUtils;
import net.minecraft.command.*;
import net.minecraft.util.*;

import java.util.*;

public class CommandSubLM extends CommandLM implements ICustomCommandInfo
{
	public final Map<String, ICommand> subCommands;
	
	public CommandSubLM(String s, CommandLevel l)
	{
		super(s, l);
		subCommands = new HashMap<>();
	}
	
	public void add(ICommand c)
	{ subCommands.put(c.getCommandName(), c); }
	
	public String getCommandUsage(ICommandSender ics)
	{ return '/' + commandName + " [subcommand]"; }
	
	public List<String> addTabCompletionOptions(ICommandSender ics, String[] args, BlockPos pos)
	{
		if(args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, subCommands.keySet());
		}
		
		ICommand cmd = subCommands.get(args[0]);
		
		if(cmd != null)
		{
			return cmd.addTabCompletionOptions(ics, LMStringUtils.shiftArray(args), pos);
		}
		
		return null;
	}
	
	public boolean isUsernameIndex(String[] args, int i)
	{
		if(i > 0 && args.length > 1)
		{
			ICommand cmd = subCommands.get(args[0]);
			if(cmd != null) return cmd.isUsernameIndex(LMStringUtils.shiftArray(args), i - 1);
		}
		
		return false;
	}
	
	public void processCommand(ICommandSender ics, String[] args) throws CommandException
	{
		if(args.length < 1)
		{
			ics.addChatMessage(new ChatComponentText(LMStringUtils.strip(subCommands.keySet())));
		}
		else
		{
			ICommand cmd = subCommands.get(args[0]);
			if(cmd == null) throw new InvalidSubCommandException(args[0]);
			else
			{
				cmd.processCommand(ics, LMStringUtils.shiftArray(args));
			}
		}
	}
	
	public void addInfo(List<IChatComponent> list, ICommandSender sender)
	{
		list.add(new ChatComponentText('/' + commandName));
		list.add(null);
		addCommandUsage(sender, list, 0);
	}
	
	private static IChatComponent tree(IChatComponent sibling, int level)
	{
		if(level == 0) return sibling;
		char[] chars = new char[level * 2];
		Arrays.fill(chars, ' ');
		return new ChatComponentText(new String(chars)).appendSibling(sibling);
	}
	
	private void addCommandUsage(ICommandSender ics, List<IChatComponent> list, int level)
	{
		for(ICommand c : subCommands.values())
		{
			if(c instanceof CommandSubLM)
			{
				list.add(tree(new ChatComponentText('/' + c.getCommandName()), level));
				((CommandSubLM) c).addCommandUsage(ics, list, level + 1);
			}
			else
			{
				String usage = c.getCommandUsage(ics);
				if(usage.indexOf('/') != -1 || usage.indexOf('%') != -1)
				{
					list.add(tree(new ChatComponentText(usage), level));
				}
				else
				{
					list.add(tree(new ChatComponentTranslation(usage), level));
				}
			}
		}
	}
}