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

public class GlyphMaker implements Runnable
{
	private MinionUI minionUI;
	private Dimension dimension;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;

	private Robot robot;
	private MouseFunctions mouse;

	// Center Colors for potency (square), aspect (circle), essence (triangle)
	private Color potencyCenterColor;
	private Color essenceCenterColor;
	private Color aspectCenterColor;

	// Inventory Colors for potency (square), aspect (circle), essence
	// (triangle)
	private Color potencyInventoryColor;
	private Color essenceInventoryColor;
	private Color aspectInventoryColor;

	// search box for potency
	private Point potencySearchBoxTopLeftPoint;
	private Point potencySearchBoxBottomRightPoint;

	// search box for essence
	private Point essenceSearchBoxTopLeftPoint;
	private Point essenceSearchBoxBottomRightPoint;

	// search box for aspect
	private Point aspectSearchBoxTopLeftPoint;
	private Point aspectSearchBoxBottomRightPoint;

	// search positions for runes in inventory
	private Point invTopPoint;
	private Point invBottomPoint;

	// booleans for if rune is not found; triggers rune find
	private boolean potencyFound;
	private boolean essenceFound;
	private boolean aspectFound;

	// locations in inventory of each rune tab (for excluding rune type)
	private Point potencyTab;
	private Point essenceTab;
	private Point aspectTab;

	// integers for easy variance adjustment where Color checks are occuring
	private int inventoryColorVariance;
	private int centerColorVariance;

	// counter to let player know how many glyphs were completed
	private int glyphCount;
	
	public GlyphMaker(MinionUI minionUI) throws AWTException
	{
		robot = new Robot();

		this.minionUI = minionUI;
		mouse = new MouseFunctions();

		// center colors. These colors are avgs of search box values.
		// colorCenterVariance should allow a real color to be found.
		potencyCenterColor = new Color(90, 93, 123);
		essenceCenterColor = new Color(115, 118, 76);
		aspectCenterColor = new Color(142, 109, 70);

		// inventory colors
		potencyInventoryColor = new Color(90, 93, 123);
		essenceInventoryColor = new Color(131, 139, 82);
		aspectInventoryColor = new Color(115, 96, 71);

		// screen
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) dimension.getWidth();
		SCREEN_HEIGHT = (int) dimension.getHeight();

		// dimensions for center checks
		potencySearchBoxTopLeftPoint = new Point(810, 900);
		potencySearchBoxBottomRightPoint = new Point(835, 930);

		essenceSearchBoxTopLeftPoint = new Point(950, 900);
		essenceSearchBoxBottomRightPoint = new Point(970, 930);

		aspectSearchBoxTopLeftPoint = new Point(1085, 900);
		aspectSearchBoxBottomRightPoint = new Point(1105, 930);

		invTopPoint = new Point(1420, 340);
		invBottomPoint = new Point(1420, 875);

		potencyFound = false;
		essenceFound = false;
		aspectFound = false;

		potencyTab = new Point(1790, 260);
		essenceTab = new Point(1825, 260);
		aspectTab = new Point(1860, 260);

		inventoryColorVariance = 30; // bluVar ?= 39, grnVar =? 23, ojVar =?
		centerColorVariance = 20;

		glyphCount = 0;
	}

	/*
	 * This method is for scanning each center box and verifying the existence
	 * of a rune based on its color.
	 */
	private boolean checkCenterRuneColor(Point p1, Point p2, Color targetColor)
	{
		for(int y = (int) p1.getY(); y <= (int) p2.getY(); y++)
		{
			for(int x = (int) p1.getX(); x <= (int) p2.getX(); x++)
			{
				if(robot.getPixelColor(x, y).getRed() >= (targetColor.getRed() - centerColorVariance)
						&& robot.getPixelColor(x, y).getRed() <= (targetColor.getRed() + centerColorVariance)
						&& robot.getPixelColor(x, y).getGreen() >= (targetColor.getGreen() - centerColorVariance)
						&& robot.getPixelColor(x, y).getGreen() <= (targetColor.getGreen() + centerColorVariance)
						&& robot.getPixelColor(x, y).getBlue() >= (targetColor.getBlue() - centerColorVariance)
						&& robot.getPixelColor(x, y).getBlue() <= (targetColor.getBlue() + centerColorVariance))
				{
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * This is a generic method for finding a rune. The first argument is the
	 * location of the tab for which rune type you need. The second argument is
	 * a valid color for that rune type. This method will then tab to the
	 * correct rune page and search the inventory for a valid rune. Valid runes
	 * are runes the player has access to and can use to craft a glyph.
	 */
	private boolean getNextRune(Point runeTab, Color targetColor) throws InterruptedException
	{
		// click on the tab to show the correct inventory of runes to look for
		robot.mouseMove((int) runeTab.getX(), (int) runeTab.getY());
		mouse.mouseClick();
		Thread.sleep(250);
		// Create a temp Point and search for valid rune within inventory
		Point newRuneLocation = findRuneInInventory(targetColor);
		Thread.sleep(250);
		// if executes when location of valid rune is found
		if(newRuneLocation != null)
		{
			robot.mouseMove((int) newRuneLocation.getX(), (int) newRuneLocation.getY());
			Thread.sleep(500);
			mouse.doDoubleClick();
			robot.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
			return true;
		}
		else
		{
			return false;
		}
	}

	/*
	 * This method used the targetColor and compares it against the *11*
	 * inventory positions to see if a valid rune exists. The methods utilizes
	 * There is a Color variance on all RGB values to increase success.
	 */
	private Point findRuneInInventory(Color targetColor)
	{
		Point returnPoint = null;
		for(int x = (int) invTopPoint.getX(); x <= (int) invBottomPoint.getX(); x++)
		{
			for(int y = (int) invTopPoint.getY(); y <= (int) invBottomPoint.getY(); y++)
			{
				Color invPosColor = robot.getPixelColor(x, y);

				if(invPosColor.getRed() >= targetColor.getRed() - inventoryColorVariance
						&& invPosColor.getRed() <= targetColor.getRed() + inventoryColorVariance
						&& invPosColor.getGreen() >= targetColor.getGreen() - inventoryColorVariance
						&& invPosColor.getGreen() <= targetColor.getGreen() + inventoryColorVariance
						&& invPosColor.getBlue() >= targetColor.getBlue() - inventoryColorVariance
						&& invPosColor.getBlue() <= targetColor.getBlue() + inventoryColorVariance)
				{
					returnPoint = new Point(x, y);
					break;
				}
			}
		}
		return returnPoint;
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
			// clicks on Creation to make sure bot is on right window
			robot.mouseMove(1760, 205);
			mouse.mouseClick();
			Thread.sleep(250);

			do
			{
				potencyFound = false;
				essenceFound = false;
				aspectFound = false;

				minionUI.addText("Looking for potency rune..");
				if(checkCenterRuneColor(potencySearchBoxTopLeftPoint, potencySearchBoxBottomRightPoint, potencyCenterColor))
				{
					minionUI.addText("Potency rune Check!");
					potencyFound = true;
					Thread.sleep(250);
				}
				else
				{
					minionUI.addText("Did not find potency rune");
					potencyFound = false;
					Thread.sleep(250);
					// add potency
					minionUI.addText("Searching for new potency rune.");
					Thread.sleep(250);
					if(getNextRune(potencyTab, potencyInventoryColor))
					{
						minionUI.addText("Found new potency rune.");
						potencyFound = true;
						Thread.sleep(250);
					}
					else
					{
						minionUI.addText("Search for Potency rune failed.");
						Thread.sleep(250);
					}

				}
				if(potencyFound)
				{
					minionUI.addText("Looking for essence rune..");
					if(checkCenterRuneColor(essenceSearchBoxTopLeftPoint, essenceSearchBoxBottomRightPoint, essenceCenterColor))
					{
						minionUI.addText("Essence rune Check!");
						essenceFound = true;
						Thread.sleep(250);
					}
					else
					{
						minionUI.addText("Didnt find essence");
						essenceFound = false;
						Thread.sleep(250);
						// add essence
						minionUI.addText("Searching for new essence rune.");
						Thread.sleep(250);
						if(getNextRune(essenceTab, essenceInventoryColor))
						{
							minionUI.addText("Found new essence rune.");
							essenceFound = true;
							Thread.sleep(250);
						}
						else
						{
							minionUI.addText("Search for essence rune failed.");
							Thread.sleep(250);
						}

					}

					if(essenceFound)
					{
						minionUI.addText("Looking for aspect rune..");
						if(checkCenterRuneColor(aspectSearchBoxTopLeftPoint, aspectSearchBoxBottomRightPoint, aspectCenterColor) && essenceFound)
						{
							minionUI.addText("Aspect rune Check!");
							aspectFound = true;
							Thread.sleep(250);
						}
						else
						{
							minionUI.addText("Did not find aspect rune");
							aspectFound = false;
							Thread.sleep(250);
							// add aspect
							minionUI.addText("Searching for new aspect rune.");
							Thread.sleep(250);
							if(getNextRune(aspectTab, aspectInventoryColor))
							{
								minionUI.addText("Found new aspect rune.");
								aspectFound = true;
								Thread.sleep(250);
							}
							else
							{
								minionUI.addText("Search for aspect rune failed.");
								Thread.sleep(250);
							}
						}
					} // end of aspect
				} // end of essence

				if(potencyFound && essenceFound && aspectFound)
				{
					glyphCount += 1;
					minionUI.addText("\nCrafting...");
					robot.keyPress(KeyEvent.VK_R);
					robot.keyRelease(KeyEvent.VK_R);
					Thread.sleep(2000);
					minionUI.addText("\nSuccess!\n");
					Thread.sleep(2500);
					;
				}
				else
				{
					minionUI.addText("\nCrafting failed. You do not have one or more runes.");
					Thread.sleep(250);
				}

			} // end of do
			while(potencyFound && essenceFound && aspectFound);

			minionUI.addText("Glyph Maker completed " + glyphCount + " glyphs.");
		}
		catch(InterruptedException e)
		{
			minionUI.addText("Glyph Maker has been interrupted!");
			return;
		}

	} // end of run

}