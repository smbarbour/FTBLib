package ftb.lib;

import ftb.lib.api.ForgePlayer;
import ftb.lib.api.gui.GuiIcons;
import ftb.lib.api.permissions.ForgePermissionRegistry;
import ftb.lib.mod.*;

public enum PrivacyLevel
{
	PUBLIC("public"),
	PRIVATE("private"),
	FRIENDS("friends");
	
	public static final PrivacyLevel[] VALUES_3 = new PrivacyLevel[] {PUBLIC, PRIVATE, FRIENDS};
	public static final PrivacyLevel[] VALUES_2 = new PrivacyLevel[] {PUBLIC, PRIVATE};
	
	public final String uname;
	
	PrivacyLevel(String s)
	{
		uname = s;
	}
	
	public boolean isPublic()
	{ return this == PUBLIC; }
	
	public boolean isRestricted()
	{ return this == FRIENDS; }
	
	public PrivacyLevel next(PrivacyLevel[] l)
	{ return l[(ordinal() + 1) % l.length]; }
	
	public PrivacyLevel prev(PrivacyLevel[] l)
	{
		int id = ordinal() - 1;
		if(id < 0) id = l.length - 1;
		return l[id];
	}
	
	public String getText()
	{ return FTBLibMod.proxy.translate("ftbl.security." + uname); }
	
	public String getTitle()
	{ return FTBLibMod.proxy.translate("ftbl.security"); }
	
	public TextureCoords getIcon()
	{ return GuiIcons.security[ordinal()]; }
	
	public static String[] getNames()
	{
		String[] s = new String[VALUES_3.length];
		for(int i = 0; i < VALUES_3.length; i++)
			s[i] = VALUES_3[i].uname;
		return s;
	}
	
	public static PrivacyLevel get(String s)
	{
		for(PrivacyLevel l : VALUES_3)
		{
			if(l.uname.equalsIgnoreCase(s)) return l;
		}
		return null;
	}
	
	public boolean canInteract(ForgePlayer owner, ForgePlayer player)
	{
		if(FTBLib.ftbu == null) return true;
		if(this == PrivacyLevel.PUBLIC || owner == null) return true;
		if(player == null) return false;
		if(owner.equalsPlayer(player)) return true;
		if(player.isOnline() && ForgePermissionRegistry.hasPermission(FTBLibPermissions.interact_secure, player.getProfile()))
			return true;
		if(this == PrivacyLevel.PRIVATE) return false;
		return this == PrivacyLevel.FRIENDS && owner.isFriend(player);
	}
}