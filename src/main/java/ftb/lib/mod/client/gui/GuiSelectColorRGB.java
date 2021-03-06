package ftb.lib.mod.client.gui;

import ftb.lib.*;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.config.ClientConfigRegistry;
import ftb.lib.api.gui.*;
import ftb.lib.api.gui.callback.*;
import ftb.lib.api.gui.widgets.*;
import ftb.lib.mod.client.FTBLibModClient;
import latmod.lib.LMColor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSelectColorRGB extends GuiLM
{
	public static final ResourceLocation tex = new ResourceLocation("ftbl", "textures/gui/colselector_rgb.png");
	public static final TextureCoords col_tex = new TextureCoords(tex, 98, 13, 32, 16);
	
	public static final int SLIDER_W = 6, SLIDER_H = 13, SLIDER_BAR_W = 86;
	public static final TextureCoords slider_tex = new TextureCoords(tex, 98, 29, SLIDER_W, SLIDER_H);
	public static final TextureCoords slider_col_tex = new TextureCoords(tex, 98, 0, SLIDER_BAR_W, SLIDER_H);
	
	public final IColorCallback callback;
	public final LMColor initCol;
	public final Object colorID;
	public final boolean isInstant;
	public final LMColor.RGB currentColor;
	
	public final ButtonLM colorInit, colorCurrent, switchHSB;
	public final SliderLM currentColR, currentColG, currentColB;
	
	public GuiSelectColorRGB(IColorCallback cb, LMColor col, Object id, boolean instant)
	{
		super(null, tex);
		callback = cb;
		initCol = col.copy();
		currentColor = new LMColor.RGB();
		currentColor.set(initCol.copy());
		colorID = id;
		isInstant = instant;
		
		mainPanel.width = 98;
		mainPanel.height = 76;
		
		colorInit = new ButtonLM(this, 6, 6, col_tex.widthI(), col_tex.heightI())
		{
			public void onButtonPressed(int b)
			{ closeGui(false); }
			
			public void addMouseOverText(List<String> s)
			{
				s.add(FTBLibLang.button_cancel());
				s.add(colorInit.toString());
			}
		};
		
		colorCurrent = new ButtonLM(this, 60, 6, col_tex.widthI(), col_tex.heightI())
		{
			public void onButtonPressed(int b)
			{ closeGui(true); }
			
			public void addMouseOverText(List<String> s)
			{
				s.add(FTBLibLang.button_accept());
				s.add(currentColor.toString());
			}
		};
		
		switchHSB = new ButtonLM(this, 41, 6, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				FTBLibClient.playClickSound();
				FTBLibModClient.open_hsb_cg.set(true);
				ClientConfigRegistry.provider().save();
				FTBLibClient.openGui(new GuiSelectColorHSB(callback, initCol, colorID, isInstant));
			}
		};
		
		switchHSB.title = "HSB";
		
		currentColR = new SliderLM(this, 6, 25, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		currentColR.value = col.red() / 255F;
		currentColR.displayMax = 255;
		currentColR.title = EnumMCColor.RED.toString();
		currentColR.scrollStep = 1F / 255F;
		
		currentColG = new SliderLM(this, 6, 41, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		currentColG.value = col.green() / 255F;
		currentColG.displayMax = 255;
		currentColG.title = EnumMCColor.GREEN.toString();
		currentColG.scrollStep = 1F / 255F;
		
		currentColB = new SliderLM(this, 6, 57, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
		currentColB.value = col.blue() / 255F;
		currentColB.displayMax = 255;
		currentColB.title = EnumMCColor.BLUE.toString();
		currentColB.scrollStep = 1F / 255F;
	}
	
	public void addWidgets()
	{
		mainPanel.add(colorInit);
		mainPanel.add(colorCurrent);
		mainPanel.add(switchHSB);
		mainPanel.add(currentColR);
		mainPanel.add(currentColG);
		mainPanel.add(currentColB);
	}
	
	public void drawBackground()
	{
		super.drawBackground();
		
		int prevCol = currentColor.color();
		update();
		
		if(isInstant && prevCol != currentColor.color())
			callback.onColorSelected(new ColorSelected(colorID, true, currentColor, false));
		
		FTBLibClient.setGLColor(initCol.color(), 255);
		colorInit.render(col_tex);
		GlStateManager.color(currentColR.value, currentColG.value, currentColB.value, 1F);
		colorCurrent.render(col_tex);
		GlStateManager.color(1F, 1F, 1F, 1F);
		switchHSB.render(GuiIcons.color_hsb);
		
		FTBLibClient.setTexture(tex);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		double z = zLevel;
		double w = slider_col_tex.width;
		double h = slider_col_tex.height;
		double u0 = slider_col_tex.minU;
		double v0 = slider_col_tex.minV;
		double u1 = slider_col_tex.maxU;
		double v1 = slider_col_tex.maxV;
		
		int x = mainPanel.posX + currentColR.posX;
		int y = mainPanel.posY + currentColR.posY;
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glBegin(GL11.GL_QUADS);
		GlStateManager.color(0F, currentColG.value, currentColB.value, 1F);
		GL11.glTexCoord2d(u0, v0);
		GL11.glVertex3d(x, y, z);
		GL11.glTexCoord2d(u0, v1);
		GL11.glVertex3d(x, y + h, z);
		GlStateManager.color(1F, currentColG.value, currentColB.value, 1F);
		GL11.glTexCoord2d(u1, v1);
		GL11.glVertex3d(x + w, y + h, z);
		GL11.glTexCoord2d(u1, v0);
		GL11.glVertex3d(x + w, y, z);
		GL11.glEnd();
		
		x = mainPanel.posX + currentColG.posX;
		y = mainPanel.posY + currentColG.posY;
		GL11.glBegin(GL11.GL_QUADS);
		GlStateManager.color(currentColR.value, 0F, currentColB.value, 1F);
		GL11.glTexCoord2d(u0, v0);
		GL11.glVertex3d(x, y, z);
		GL11.glTexCoord2d(u0, v1);
		GL11.glVertex3d(x, y + h, z);
		GlStateManager.color(currentColR.value, 1F, currentColB.value, 1F);
		GL11.glTexCoord2d(u1, v1);
		GL11.glVertex3d(x + w, y + h, z);
		GL11.glTexCoord2d(u1, v0);
		GL11.glVertex3d(x + w, y, z);
		GL11.glEnd();
		
		x = mainPanel.posX + currentColB.posX;
		y = mainPanel.posY + currentColB.posY;
		GL11.glBegin(GL11.GL_QUADS);
		GlStateManager.color(currentColR.value, currentColG.value, 0F, 1F);
		GL11.glTexCoord2d(u0, v0);
		GL11.glVertex3d(x, y, z);
		GL11.glTexCoord2d(u0, v1);
		GL11.glVertex3d(x, y + h, z);
		GlStateManager.color(currentColR.value, currentColG.value, 1F, 1F);
		GL11.glTexCoord2d(u1, v1);
		GL11.glVertex3d(x + w, y + h, z);
		GL11.glTexCoord2d(u1, v0);
		GL11.glVertex3d(x + w, y, z);
		GL11.glEnd();
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableTexture2D();
		GL11.glShadeModel(GL11.GL_FLAT);
		
		currentColR.renderSlider(slider_tex);
		currentColG.renderSlider(slider_tex);
		currentColB.renderSlider(slider_tex);
	}
	
	public void update()
	{
		boolean u = false;
		u |= currentColR.update();
		u |= currentColG.update();
		u |= currentColB.update();
		
		if(u)
		{
			int r = (int) (currentColR.value * 255F);
			int g = (int) (currentColG.value * 255F);
			int b = (int) (currentColB.value * 255F);
			currentColor.setRGBA(r, g, b, 255);
		}
	}
	
	public void closeGui(boolean set)
	{
		FTBLibClient.playClickSound();
		callback.onColorSelected(new ColorSelected(colorID, set, set ? currentColor : initCol, true));
	}
}