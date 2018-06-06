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

public class GlyphDestroyer
{
	private Dimension dimension;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;
	private Robot robot;
	private ArrayList<Color> noGlyph;
	private boolean glyphExists;
	private boolean glyphFound;
	private MouseFunctions mouse;
	private MinionUI minionUI;

	public GlyphDestroyer(MinionUI minionUI)
	{
		// screen
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) dimension.getWidth();
		SCREEN_HEIGHT = (int) dimension.getHeight();
		
		this.minionUI = minionUI;

		try
		{
			robot = new Robot();
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
		noGlyph.add(new Color(53, 52, 46));
		glyphExists = false;
		glyphFound = false;
		mouse = new MouseFunctions();

		destroyGlyphs();
	}

	public boolean glyphFound()
	{
		for(int i = 0; i < noGlyph.size(); i++)
		{
			if(robot.getPixelColor(960, 905).equals(noGlyph.get(i)))
			{
				return false;
			}
		}
		return true;
	}

	public void getNextGlyph()
	{
		robot.mouseMove(1410, 350);
		mouse.doDoubleClick();
		robot.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
	}

	public void destroyGlyphs()
	{
		robot.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2); // 960,540
		mouse.mouseClick();
		robot.delay(250);

		int count = 0;
		do
		{

			glyphExists = glyphFound();
			if(!glyphExists)
			{
				minionUI.addText("No glyph found");
				getNextGlyph();
				if(glyphExists)
				{
					glyphExists = true;
					minionUI.addText("Found another glyph!");
				}
			}
			else
			{
				robot.delay(250);
				robot.keyPress(KeyEvent.VK_R);
				robot.keyRelease(KeyEvent.VK_R);
				minionUI.addText("Succesful Destruction");
				robot.delay(1500);
				count++;
			}
		}
		while(glyphExists && count < 10);
		minionUI.addText("Glyph Destroyer is done.");
	}

}
