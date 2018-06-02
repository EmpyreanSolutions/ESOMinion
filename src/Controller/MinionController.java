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
		minionUI.addText("\nRunning Glyph Maker");
		new GlyphMaker(minionUI);
		
	}
	
	public void runGlyphDestroyer()
	{
		
	}
	
	public void runGlyphDestroyerX10()
	{
		minionUI.addText("Running Glyph Destroyer");
		new GlyphDestroyer();
		minionUI.addText("Glyph Destroyer is done.");
	}
}
