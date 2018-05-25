package Controller;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Utility.MouseFunctions;
import View.MinionUI;

public class GlyphDestroyerV2
{
	private Dimension dimension;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private Robot minion;
	private ArrayList<Color> noGlyph;
	private boolean glyphExists;
	private boolean glyphFound;
	private MouseFunctions mouse;
	private MinionUI view;

	public GlyphDestroyerV2()
	{
		// screen
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) dimension.getWidth();
		SCREEN_HEIGHT = (int) dimension.getHeight();

		try
		{
			minion = new Robot();
		}
		catch(AWTException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("unkown error at minion creation");

		}

		noGlyph = new ArrayList<Color>();
		noGlyph.add(new Color(52, 51, 46));
		noGlyph.add(new Color(53, 51, 46));
		glyphExists = false;
		glyphFound = false;
		mouse = new MouseFunctions();

		destroyGlyphs();
	}

	public boolean glyphFound()
	{
		for(int i = 0; i < noGlyph.size(); i++)
		{
			if(minion.getPixelColor(960, 905).equals(noGlyph.get(i)))
			{
				return false;
			}
		}
		return true;
	}

	public void getNextGlyph()
	{
		minion.mouseMove(1410, 350);
		mouse.doDoubleClick();
		minion.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
	}

	public void destroyGlyphs()
	{
		minion.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2); // 960,540
		mouse.mouseClick();
		minion.delay(250);

		int count = 0;
		do
		{

			glyphExists = glyphFound();
			if(!glyphFound())
			{
				System.out.println("no glyph found");
				getNextGlyph();
				if(glyphFound())
				{
					glyphExists = true;
					System.out.println("NEXT GLYPH");
				}
			}

			if(glyphExists)
			{
				minion.delay(250);
				minion.keyPress(KeyEvent.VK_R);
				minion.keyRelease(KeyEvent.VK_R);
				System.out.println("SUCCESSFUL DESTRUCTION");
				minion.delay(1500);
				count++;
			}
		}
		while(glyphExists && count < 10);
		System.out.println("FINISHED");
	}

}