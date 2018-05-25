package Controller;

import View.MinionUI;

public class MinionController
{
	private MinionUI minionUI;
	
	public MinionController()
	{
		minionUI = new MinionUI(this);
		System.out.println("mcv: " + minionUI);
	}
	
	public void runGlyphMaker()
	{
		System.out.println("Running Glyph Maker");
		minionUI.addText("\nRunning Glyph Maker");
		new GlyphMaker(minionUI);
		minionUI.addText("Glyph Maker is done.");
	}
	
	public void runGlyphDestroyer()
	{
		
	}
	
	public void runGlyphDestroyerX10()
	{
		System.out.println("Running Glyph Destroyer");
		minionUI.addText("Running Glyph Destroyer");
		new GlyphDestroyer();
		minionUI.addText("Glyph Destroyer is done.");
	}
}
