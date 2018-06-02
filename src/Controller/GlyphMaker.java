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

	// potency (square), aspect (circle), essence (triangle)
	private Color potencyColor;
	private ArrayList<Color> aspectColor;
	private ArrayList<Color> essenceColor;

	// search box for potency
	private int potencyX1;
	private int potencyX2;
	private int potencyY1;
	private int potencyY2;
	// search box for aspect
	private int aspectX1;
	private int aspectX2;
	private int aspectY1;
	private int aspectY2;
	// search box for essence
	private int essenceX1;
	private int essenceX2;
	private int essenceY1;
	private int essenceY2;
	// search positions for runes in inventory
	private ArrayList<Point> invPositions;
	// booleans for if rune is not found; triggers rune find
	private boolean potencyFound;
	private boolean aspectFound;
	private boolean essenceFound;

	private Point potencyTab;

	private int aspectTabX;
	private int aspectTabY;

	private int essenceTabX;
	private int essenceTabY;

	public GlyphMaker(MinionUI minionUI)
	{
		this.minionUI = minionUI;
		mouse = new MouseFunctions();

		// Color *NEW*
		// square rune colors
		potencyColor = new Color(184, 200, 210);

		// arraylist
		aspectColor = new ArrayList<Color>();
		essenceColor = new ArrayList<Color>();

		// screen
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) dimension.getWidth();
		SCREEN_HEIGHT = (int) dimension.getHeight();

		// circle rune colors
		aspectColor.add(new Color(194, 200, 167));

		// triangle rune colors
		essenceColor.add(new Color(214, 196, 168));

		// dimensions for center checks
		potencyX1 = 835;
		potencyX2 = 850;
		potencyY1 = 893;
		potencyY2 = 905;

		aspectX1 = 950;
		aspectX2 = 970;
		aspectY1 = 892;
		aspectY2 = 895;

		essenceX1 = 1075;
		essenceX2 = 1090;
		essenceY1 = 894;
		essenceY2 = 905;

		// the inventory without scrolls has *11* visible positions
		invPositions = new ArrayList<Point>();
		invPositions.add(new Point(1420, 350));
		invPositions.add(new Point(1420, 400));
		invPositions.add(new Point(1420, 450));
		invPositions.add(new Point(1420, 500));
		invPositions.add(new Point(1420, 550));
		invPositions.add(new Point(1420, 600));
		invPositions.add(new Point(1420, 650));
		invPositions.add(new Point(1420, 700));
		invPositions.add(new Point(1420, 755));
		invPositions.add(new Point(1420, 805));
		invPositions.add(new Point(1420, 855));

		potencyFound = false;
		aspectFound = false;
		essenceFound = false;

		potencyTab = new Point(1790, 255);

		makeGlyphs();
	}

	public boolean potencyCheck()
	{
		for(int i = potencyX1; i <= potencyX2; i++)
		{

			for(int j = potencyY1; j <= potencyY2; j++)
			{
				if(robot.getPixelColor(i, j).getRed() >= potencyColor.getRed() - 10
						&& robot.getPixelColor(i, j).getRed() <= potencyColor.getRed() + 10
						&& robot.getPixelColor(i, j).getGreen() >= potencyColor.getGreen() - 10
						&& robot.getPixelColor(i, j).getGreen() <= potencyColor.getGreen() + 10
						&& robot.getPixelColor(i, j).getBlue() >= potencyColor.getBlue() - 10
						&& robot.getPixelColor(i, j).getBlue() <= potencyColor.getBlue() + 10)
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean aspectCheck()
	{
		for(int i = aspectX1; i < aspectX2; i++)
		{
			for(int j = aspectY1; j < aspectY2; j++)
			{
				for(int k = 0; k < aspectColor.size(); k++)
				{
					if(robot.getPixelColor(i, j).equals(aspectColor.get(k)))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean essenceCheck()
	{
		for(int i = essenceX1; i < essenceX2; i++)
		{
			for(int j = essenceY1; j < essenceY2; j++)
			{
				for(int k = 0; k < essenceColor.size(); k++)
				{
					if(robot.getPixelColor(i, j).equals(essenceColor.get(k)))
					{
						return true;
					}
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
	public boolean findRune(Point runeTab, Color targetColor)
	{
		// click on the tab to show the correct inventory of runes to look for
		robot.mouseMove((int) runeTab.getX(), (int) runeTab.getY());
		mouse.mouseClick();
		robot.delay(500);
		// Create a temp Point and search for valid rune within inventory
		Point newRuneLocation = inventoryCheck(targetColor);
		robot.delay(500);
		//if executes when location of valid rune is found
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
	 * 	inventory positions to see if a valid rune exists.
	 * 	The methods utilizes -10/+10 variance on all RGB values to increase reliability.
	 */
	public Point inventoryCheck(Color targetColor)
	{
		Point returnPoint = null;
		for(int i = 0; i < invPositions.size(); i++)
		{
			Color invPosColor = robot.getPixelColor((int) invPositions.get(i).getX(), (int) invPositions.get(i).getY());
			if(invPosColor.getRed() >= targetColor.getRed() - 10 && invPosColor.getRed() <= targetColor.getRed() + 10
					&& invPosColor.getGreen() >= targetColor.getGreen() - 10 && invPosColor.getGreen() <= targetColor.getGreen() + 10
					&& invPosColor.getBlue() >= targetColor.getBlue() - 10 && invPosColor.getBlue() <= targetColor.getBlue() + 10)
			{
				returnPoint = new Point((int) invPositions.get(i).getX(), (int) invPositions.get(i).getY());
				break;
			}
		}
		return returnPoint;
	}

	public void makeGlyphs()
	{

		new Thread(new Runnable()
		{
			@Override
			public void run()
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
					System.out.println("unknown error at minion creation.");
					minionUI.addText("\nunknown error at minion creation.");
				}

				// center of screen and make active window
				robot.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2); // 960,540
				mouse.mouseClick();
				robot.delay(500);

				do
				{
					potencyFound = false;
					aspectFound = false;
					essenceFound = false;

					if(potencyCheck())
					{
						minionUI.addText("Potency Check!");
						potencyFound = true;
					}
					else
					{
						minionUI.addText("Didnt find potency");
						potencyFound = false;
						// add potency
						minionUI.addText("Searching for new potency rune.");
						if(findRune(potencyTab, potencyColor))
						{
							minionUI.addText("Found new potency.");
							potencyFound = true;
						}
						else
						{
							minionUI.addText("Search for Potency rune failed.");
						}

					}

					if(aspectCheck())
					{
						minionUI.addText("Aspect Check!");
						aspectFound = true;
					}
					else
					{
						minionUI.addText("Didnt find aspect");
						// add aspect
						potencyFound = false;
					}

					if(essenceCheck())
					{
						minionUI.addText("Essence Check!");
						essenceFound = true;
					}
					else
					{
						minionUI.addText("Didnt find essence");
						// add essence
						potencyFound = false;
					}

					if(potencyFound && aspectFound && essenceFound)
					{
						minionUI.addText("\nCrafting...");
						robot.keyPress(KeyEvent.VK_R);
						robot.keyRelease(KeyEvent.VK_R);
						robot.delay(4000);
					}
					else
					{
						minionUI.addText("\nCrafting failed");
					}

				} // end of do
				while(potencyFound && aspectFound && essenceFound);

				minionUI.addText("Glyph Maker is done.");
			} // end of ren
		}).start();

	}

}