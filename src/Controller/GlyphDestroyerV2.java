package Controller;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import Utility.MouseFunctions;
import View.MinionUI;

public class GlyphDestroyerV2 implements Runnable
{
	private MinionUI minionUI;
	private Dimension dimension;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;

	private Robot robot;
	private MouseFunctions mouse;

	// glyph colors
	private Color bronzeColor;
	private Color silverColor;
	private Color goldColor;
	private Color[] glyphColors;

	// color for inv check. Different logic than G.M. Using "E" Add color in
	// bottom right for check
	private Point invPosCheck;
	private Point addPointBR;
	private Color addColorBR;

	// center point
	private Point topLeftCenterGylphPoint;
	private Point bottomRightCenterGylphPoint;

	// integers for easy variance adjustment where Color checks are occuring
	private int addColorVariance;
	private int centerColorVariance;

	// counter to let player know how many glyphs were completed
	private int gdCount;

	private boolean glyphFound;

	public GlyphDestroyerV2(MinionUI minionUI) throws AWTException
	{
		robot = new Robot();

		this.minionUI = minionUI;
		mouse = new MouseFunctions();

		// screen
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) dimension.getWidth();
		SCREEN_HEIGHT = (int) dimension.getHeight();

		// glyph colors
		bronzeColor = new Color(129, 106, 97);
		silverColor = new Color(174, 171, 178);
		goldColor = new Color(177, 136, 72);
		glyphColors = new Color[] { bronzeColor, silverColor, goldColor };

		// center glyph area
		topLeftCenterGylphPoint = new Point(955, 905);
		bottomRightCenterGylphPoint = new Point(965, 908);

		// invCheck
		invPosCheck = new Point(1450, 350);
		addPointBR = new Point(1674, 1050);
		addColorBR = new Color(255, 255, 255);

		addColorVariance = 10;
		centerColorVariance = 20;

		glyphFound = false;

		gdCount = 0;

	}

	/*
	 * This method is for scanning each center box and verifying the existence
	 * of a glyph based on their colors i.e. bronze, silver, gold.
	 */
	private boolean checkCenterforGlyph(Point p1, Point p2, Color[] targetColors)
	{
		for(int y = (int) p1.getY(); y <= (int) p2.getY(); y++)
		{
			for(int x = (int) p1.getX(); x <= (int) p2.getX(); x++)
			{
				for(int i = 0; i < targetColors.length; i++)
				{
					if(robot.getPixelColor(x, y).getRed() >= (targetColors[i].getRed() - centerColorVariance)
							&& robot.getPixelColor(x, y).getRed() <= (targetColors[i].getRed() + centerColorVariance)
							&& robot.getPixelColor(x, y).getGreen() >= (targetColors[i].getGreen() - centerColorVariance)
							&& robot.getPixelColor(x, y).getGreen() <= (targetColors[i].getGreen() + centerColorVariance)
							&& robot.getPixelColor(x, y).getBlue() >= (targetColors[i].getBlue() - centerColorVariance)
							&& robot.getPixelColor(x, y).getBlue() <= (targetColors[i].getBlue() + centerColorVariance))
					{
						return true;
					}
				}

			}
		}
		return false;
	}

	private boolean findGlyph() throws InterruptedException
	{
		robot.mouseMove((int) invPosCheck.getX(), (int) invPosCheck.getY());
		Thread.sleep(250);
		if(robot.getPixelColor((int) addPointBR.getX(), (int) addPointBR.getY()).getRed() >= (addColorBR.getRed() - addColorVariance)
				&& robot.getPixelColor((int) addPointBR.getX(), (int) addPointBR.getY()).getRed() <= (addColorBR.getRed() + addColorVariance)
				&& robot.getPixelColor((int) addPointBR.getX(), (int) addPointBR.getY()).getGreen() >= (addColorBR.getGreen() - addColorVariance)
				&& robot.getPixelColor((int) addPointBR.getX(), (int) addPointBR.getY()).getGreen() <= (addColorBR.getGreen() + addColorVariance)
				&& robot.getPixelColor((int) addPointBR.getX(), (int) addPointBR.getY()).getBlue() >= (addColorBR.getBlue() - addColorVariance)
				&& robot.getPixelColor((int) addPointBR.getX(), (int) addPointBR.getY()).getBlue() <= (addColorBR.getBlue() + addColorVariance))
		{
			robot.keyPress(KeyEvent.VK_E);
			robot.keyRelease(KeyEvent.VK_E);
			return true;
		}
		return false;
	}

	@Override
	public void run()
	{
		try
		{
			// center of screen and make active window
			robot.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2); // 960,540
			mouse.mouseClick();
			Thread.sleep(250);
			// clicks on Destruction to make sure bot is on right window
			robot.mouseMove(1800, 205);
			mouse.mouseClick();
			Thread.sleep(250);

			do
			{
				glyphFound = false;
				minionUI.addText("Checking for glyph");
				Thread.sleep(250);
				if(checkCenterforGlyph(topLeftCenterGylphPoint, bottomRightCenterGylphPoint, glyphColors))
				{
					minionUI.addText("Glyph ready!");
					glyphFound = true;
					Thread.sleep(250);

				}
				else
				{
					minionUI.addText("Gylph Check failed.");
					Thread.sleep(250);
					minionUI.addText("Looking for a new Glyph.");
					Thread.sleep(250);
					if(findGlyph())
					{
						minionUI.addText("New Glyph found!");
						glyphFound = true;
						Thread.sleep(250);
					}
					else
					{
						minionUI.addText("Couldn't find another Glyph to destroy.");
						glyphFound = false;
						Thread.sleep(250);
					}
				}

				if(glyphFound)
				{
					minionUI.addText("Destroying Glyph!");
					robot.keyPress(KeyEvent.VK_R);
					robot.keyRelease(KeyEvent.VK_R);
					Thread.sleep(1000);
					minionUI.addText("Successful Destruction!!!");
					gdCount++;
					Thread.sleep(1000);
				}
				else
				{
					minionUI.addText("Destruction failed.");
					Thread.sleep(250);
				}

			}
			while(glyphFound);
			minionUI.addText("Glyph Destroyer eradicated " + gdCount + " glyphs!");
		}
		catch(InterruptedException e)
		{
			minionUI.addText("Glyph Destroyer has been interrupted!");
			return;
		}
	} // end of run
}
