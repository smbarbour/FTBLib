package ftb.lib;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

public enum EnumMCColor // ItemDye
{
	BLACK("Black", 0xFF3F3F3F),
	RED("Red", 0xFFFF0000),
	GREEN("Green", 0xFF009B0E),
	BROWN("Brown", 0xFFA35C2D),
	BLUE("Blue", 0xFF004CC4),
	PURPLE("Purple", 0xFF9A41E2),
	CYAN("Cyan", 0xFF00D8C6),
	LIGHT_GRAY("LightGray", 0xFFBCBCBC),
	GRAY("Gray", 0xFF636363),
	PINK("Pink", 0xFFFF95A3),
	LIME("Lime", 0xFF00FF2E),
	YELLOW("Yellow", 0xFFFFD500),
	LIGHT_BLUE("LightBlue", 0xFF63BEFF),
	MAGENTA("Magenta", 0xFFFF006E),
	ORANGE("Orange", 0xFFFF9500),
	WHITE("White", 0xFFFFFFFF);
	
	public static final EnumMCColor[] VALUES = values();
	
	public final int ID;
	public final String lang;
	public final String name;
	public final int color;
	public final int colorBright;
	
	public final String dyeName;
	public final String glassName;
	public final String paneName;
	
	EnumMCColor(String s, int c)
	{
		ID = ordinal();
		name = EnumDyeColor.byDyeDamage(ID).getUnlocalizedName();
		lang = "ftbl:color." + s.toLowerCase();
		color = ItemDye.dyeColors[ID];
		colorBright = c;
		
		dyeName = "dye" + s;
		glassName = "blockGlass" + s;
		paneName = "paneGlass" + s;
	}
	
	@SideOnly(Side.CLIENT)
	public String toString()
	{ return I18n.format(lang); }
	
	public ItemStack getDye(int s)
	{ return new ItemStack(Items.dye, s, ID); }
}