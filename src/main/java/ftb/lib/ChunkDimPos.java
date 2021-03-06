package ftb.lib;

import latmod.lib.*;
import net.minecraft.world.ChunkCoordIntPair;

/**
 * Created by LatvianModder on 14.03.2016.
 */
public class ChunkDimPos extends ChunkCoordIntPair
{
	public final int dim;
	
	public ChunkDimPos(int d, int x, int z)
	{
		super(x, z);
		dim = d;
	}
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else return equalsChunk((ChunkDimPos) o);
	}
	
	public boolean equalsChunk(ChunkDimPos p)
	{ return p == this || (p != null && p.dim == dim && p.chunkXPos == chunkXPos && p.chunkZPos == chunkZPos); }
	
	public String toString()
	{ return "[" + dim + ',' + chunkXPos + ',' + chunkZPos + ']'; }
	
	public int hashCode()
	{ return LMUtils.hashCode(dim, chunkXPos, chunkZPos); }
	
	public double getDistSq(double x, double z)
	{
		double x0 = MathHelperLM.unchunk(chunkXPos) + 8.5D;
		double z0 = MathHelperLM.unchunk(chunkZPos) + 8.5D;
		return MathHelperLM.distSq(x0, 0D, z0, x, 0D, z);
	}
	
	public double getDistSq(ChunkDimPos c)
	{ return getDistSq(MathHelperLM.unchunk(c.chunkXPos) + 8.5D, MathHelperLM.unchunk(c.chunkZPos) + 8.5D); }
}
