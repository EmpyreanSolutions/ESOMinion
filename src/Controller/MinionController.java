package Controller;

import java.awt.AWTException;

import View.MinionUI;

public class MinionController
{
	private MinionUI minionUI;
	
	public MinionController()
	{
		minionUI = new MinionUI(this);
	}
	
	public void runGlyphMaker()
	{
		minionUI.addText("Running Glyph Maker");
		try
		{
			new GlyphMaker(minionUI);
		}
		catch(AWTException e)
		{
			e.printStackTrace();
			minionUI.addText("Robot failed to instantiate. Try redownloading file. Please contact support.");
		}
		
	}
	
	public void runGlyphDestroyer()
	{
		minionUI.addText("Running Glyph Destroyer");
		try
		{
			new GlyphDestroyerV2(minionUI);
		}
		catch(AWTException e)
		{
			e.printStackTrace();
			minionUI.addText("Robot failed to instantiate. Try redownloading file. Please contact support.");
		}
	}
}
