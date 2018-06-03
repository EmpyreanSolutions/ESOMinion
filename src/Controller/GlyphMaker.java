package Controller;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Utility.MouseFunctions;
import View.MinionUI;

public class GlyphMaker
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
	private ArrayList<Point> invPositions;

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

	public GlyphMaker(MinionUI minionUI)
	{
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
			minionUI.addText("\nUnknown error at minion creation.");
		}

		this.minionUI = minionUI;
		mouse = new MouseFunctions();

		// center colors
		potencyCenterColor = new Color(79, 81, 122);
		essenceCenterColor = new Color(194, 200, 167);
		aspectCenterColor = new Color(214, 196, 168);

		// inventory colors
		potencyInventoryColor = new Color(120, 130, 168);
		essenceInventoryColor = new Color(131, 139, 82);
		aspectInventoryColor = new Color(115, 96, 71);

		// screen
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) dimension.getWidth();
		SCREEN_HEIGHT = (int) dimension.getHeight();

		// dimensions for center checks
		potencySearchBoxTopLeftPoint = new Point(795, 893);
		potencySearchBoxBottomRightPoint = new Point(850, 940);

		essenceSearchBoxTopLeftPoint = new Point(950, 892);
		essenceSearchBoxBottomRightPoint = new Point(970, 895);

		aspectSearchBoxTopLeftPoint = new Point(1075, 894);
		aspectSearchBoxBottomRightPoint = new Point(1090, 905);

		// the inventory without scrolls has *11* visible positions
		invPositions = new ArrayList<Point>();
		invPositions.add(new Point(1420, 350));
		invPositions.add(new Point(1420, 401));
		invPositions.add(new Point(1420, 452));
		invPositions.add(new Point(1420, 503));
		invPositions.add(new Point(1420, 554));
		invPositions.add(new Point(1420, 605));
		invPositions.add(new Point(1420, 656));
		invPositions.add(new Point(1420, 707));
		invPositions.add(new Point(1420, 763));
		invPositions.add(new Point(1420, 814));
		invPositions.add(new Point(1420, 865));

		potencyFound = false;
		essenceFound = false;
		aspectFound = false;

		potencyTab = new Point(1790, 260);
		essenceTab = new Point(1825, 260);
		aspectTab = new Point(1860, 260);

		inventoryColorVariance = 20;
		centerColorVariance = 10;

		makeGlyphs();
	}

	/*
	 * This method is for scanning each center box and verifying the existence
	 * of a rune based on its color.
	 */
	private boolean checkCenterRuneColor(Point p1, Point p2, Color targetColor)
	{
		for(int i = (int) p1.getY(); i <= (int) p2.getY(); i++)
		{
			for(int j = (int) p1.getX(); j <= (int) p2.getX(); j++)
			{
				if(robot.getPixelColor(i, j).getRed() >= targetColor.getRed() - centerColorVariance
						&& robot.getPixelColor(i, j).getRed() <= targetColor.getRed() + centerColorVariance
						&& robot.getPixelColor(i, j).getGreen() >= targetColor.getGreen() - centerColorVariance
						&& robot.getPixelColor(i, j).getGreen() <= targetColor.getGreen() + centerColorVariance
						&& robot.getPixelColor(i, j).getBlue() >= targetColor.getBlue() - centerColorVariance
						&& robot.getPixelColor(i, j).getBlue() <= targetColor.getBlue() + centerColorVariance)
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
	private boolean getNextRune(Point runeTab, Color targetColor)
	{
		// click on the tab to show the correct inventory of runes to look for
		robot.mouseMove((int) runeTab.getX(), (int) runeTab.getY());
		mouse.mouseClick();
		robot.delay(500);
		// Create a temp Point and search for valid rune within inventory
		Point newRuneLocation = findRuneInInventory(targetColor);
		robot.delay(500);
		// if executes when location of valid rune is found
		if(newRuneLocation != null)
		{
			robot.mouseMove((int) newRuneLocation.getX(), (int) newRuneLocation.getY());
			robot.delay(500);
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
		for(int i = 0; i < invPositions.size(); i++)
		{
			Color invPosColor = robot.getPixelColor((int) invPositions.get(i).getX(), (int) invPositions.get(i).getY());

			if(invPosColor.getRed() >= targetColor.getRed() - inventoryColorVariance
					&& invPosColor.getRed() <= targetColor.getRed() + inventoryColorVariance
					&& invPosColor.getGreen() >= targetColor.getGreen() - inventoryColorVariance
					&& invPosColor.getGreen() <= targetColor.getGreen() + inventoryColorVariance
					&& invPosColor.getBlue() >= targetColor.getBlue() - inventoryColorVariance
					&& invPosColor.getBlue() <= targetColor.getBlue() + inventoryColorVariance)
			{
				returnPoint = new Point((int) invPositions.get(i).getX(), (int) invPositions.get(i).getY());
				break;
			}
		}
		return returnPoint;
	}

	private void makeGlyphs()
	{

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// center of screen and make active window
				robot.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2); // 960,540
				mouse.mouseClick();
				robot.delay(500);

				do
				{
					potencyFound = false;
					essenceFound = false;
					aspectFound = false;

					if(checkCenterRuneColor(potencySearchBoxTopLeftPoint, potencySearchBoxBottomRightPoint, potencyCenterColor))
					{
						minionUI.addText("Potency rune Check!");
						potencyFound = true;
						robot.delay(250);
					}
					else
					{
						minionUI.addText("Did not find potency rune");
						potencyFound = false;
						robot.delay(250);
						// add potency
						minionUI.addText("Searching for new potency rune.");
						robot.delay(250);
						if(getNextRune(potencyTab, potencyInventoryColor))
						{
							minionUI.addText("Found new potency rune.");
							potencyFound = true;
							robot.delay(250);
						}
						else
						{
							minionUI.addText("Search for Potency rune failed.");
							robot.delay(250);
						}

					}
					
					if(checkCenterRuneColor(essenceSearchBoxTopLeftPoint, essenceSearchBoxBottomRightPoint, essenceCenterColor))
					{
						minionUI.addText("Essence rune Check!");
						essenceFound = true;
						robot.delay(250);
					}
					else
					{
						minionUI.addText("Didnt find essence");
						essenceFound = false;
						robot.delay(250);
						// add essence
						minionUI.addText("Searching for new essence rune.");
						robot.delay(250);
						if(getNextRune(essenceTab, essenceInventoryColor))
						{
							minionUI.addText("Found new essence rune.");
							essenceFound = true;
							robot.delay(250);
						}
						else
						{
							minionUI.addText("Search for essence rune failed.");
							robot.delay(250);
						}

					}

					if(checkCenterRuneColor(aspectSearchBoxTopLeftPoint, aspectSearchBoxBottomRightPoint, aspectCenterColor))
					{
						minionUI.addText("Aspect rune Check!");
						aspectFound = true;
						robot.delay(250);
					}
					else
					{
						minionUI.addText("Did not find aspect rune");
						aspectFound = false;
						robot.delay(250);
						// add aspect
						minionUI.addText("Searching for new aspect rune.");
						robot.delay(250);
						if(getNextRune(aspectTab, aspectInventoryColor))
						{
							minionUI.addText("Found new aspect rune.");
							aspectFound = true;
							robot.delay(250);
						}
						else
						{
							minionUI.addText("Search for aspect rune failed.");
							robot.delay(250);
						}

					}

					if(potencyFound && essenceFound && aspectFound)
					{
						minionUI.addText("\nCrafting...");
						robot.keyPress(KeyEvent.VK_R);
						robot.keyRelease(KeyEvent.VK_R);
						robot.delay(2000);
						minionUI.addText("\nSuccess!\n");
						robot.delay(2000);
					}
					else
					{
						minionUI.addText("\nCrafting failed");
						robot.delay(250);
					}

				} // end of do
				while(potencyFound && essenceFound && aspectFound);

				minionUI.addText("Glyph Maker is done.");
			} // end of run
		}).start(); // end of thread
	}
}