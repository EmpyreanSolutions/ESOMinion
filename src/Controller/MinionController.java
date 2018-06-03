package Controller;

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
		new GlyphMaker(minionUI);
		
	}
	
	public void runGlyphDestroyer()
	{
		minionUI.addText("Running Glyph Destroyer");
		new GlyphDestroyer();
	}
	
	public void runGlyphDestroyerX10()
	{
		minionUI.addText("Running Glyph DestroyerX10");
		new GlyphDestroyer();
	}
}
