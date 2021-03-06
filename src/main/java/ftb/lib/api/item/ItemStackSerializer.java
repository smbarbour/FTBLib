package ftb.lib.api.item;

import com.google.gson.*;
import latmod.lib.LMStringUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemStackSerializer
{
	private static String getParseRegex(String s)
	{
		if(s.indexOf(' ') != -1) return " ";
		else if(s.indexOf(';') != -1) return ";";
		else if(s.indexOf('@') != -1) return "@";
		else return " x ";
	}
	
	public static ItemStack parseItem(String s)
	{
		if(s == null) return null;
		s = s.trim();
		if(s.isEmpty()) return null;
		
		String[] s1 = s.split(getParseRegex(s));
		if(s1.length <= 0) return null;
		
		String itemID = s1[0];
		int dmg = 0;
		int size = 1;
		String nbt = null;
		
		if(s1.length > 1)
		{
			size = Integer.parseInt(s1[1]);
		}
		
		if(s1.length > 2)
		{
			dmg = (s1[2].charAt(0) == '*') ? ODItems.ANY : Integer.parseInt(s1[2]);
		}
		
		if(s1.length > 3)
		{
			nbt = LMStringUtils.unsplitSpaceUntilEnd(3, s1);
		}
		
		return GameRegistry.makeItemStack(itemID, size, dmg, nbt);
	}
	
	public static String toString(ItemStack is)
	{
		if(is == null) return null;
		return LMInvUtils.getRegName(is).toString() + is + ' ' + is.stackSize + ' ' + is.getItemDamage();
	}
	
	public static JsonElement serialize(ItemStack is)
	{
		if(is == null || is.getItem() == null) return null;
		return new JsonPrimitive(toString(is));
	}
	
	public static ItemStack deserialize(JsonElement e)
	{
		if(e == null) return null;
		else if(e.isJsonPrimitive()) return parseItem(e.getAsString());
		return null;
	}
}