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

public class GlyphMaker
{
	private MinionUI view;
	private Dimension dimension;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;

	private Robot minion;
	private MouseFunctions mouse;

	// potency (square), aspect (circle), essence (triangle)
	private ArrayList<Color> potencyColor;
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
	// search box for runes in inventory
	private int inventoryX1;
	private int inventoryX2;
	private int inventoryY1;
	private int inventoryY2;
	// booleans for if rune is not found; triggers rune find
	private boolean potencyFound;
	private boolean aspectFound;
	private boolean essenceFound;

	public GlyphMaker(MinionUI v)
	{
		view = v;
		System.out.println("gmv: " + view);
		mouse = new MouseFunctions();
		// arraylist
		potencyColor = new ArrayList<Color>();
		aspectColor = new ArrayList<Color>();
		essenceColor = new ArrayList<Color>();
		// screen
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) dimension.getWidth();
		SCREEN_HEIGHT = (int) dimension.getHeight();
		// square rune colors
		potencyColor.add(new Color(184, 200, 210));
		potencyColor.add(new Color(200, 210, 220));

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

		inventoryX1 = 1380;
		inventoryX2 = 1440;
		inventoryY1 = 320;
		inventoryY2 = 900;

		potencyFound = false;
		aspectFound = false;
		essenceFound = false;
		
		makeGlyphs();
	}

	public boolean potencyCheck()
	{
		for(int i = potencyX1; i <= potencyX2; i++)
		{

			for(int j = potencyY1; j <= potencyY2; j++)
			{
				for(int k = 0; k < potencyColor.size(); k++)
				{
					if(minion.getPixelColor(i, j).equals(potencyColor.get(k)))
					{
						return true;
					}
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
					if(minion.getPixelColor(i, j).equals(aspectColor.get(k)))
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
					if(minion.getPixelColor(i, j).equals(essenceColor.get(k)))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	// method not currently implemented
	public boolean findPotency()
	{
		minion.mouseMove(1790, 255);
		mouse.mouseClick();
		for(int y = inventoryY1; y <= inventoryY2; y++)
		{
			for(int x = inventoryX1; x <= inventoryX2; x++)
			{
				for(int k = 0; k < potencyColor.size(); k++)
				{
					if(minion.getPixelColor(x, y).equals(potencyColor.get(k)))
					{
						minion.mouseMove(x, y);
						minion.delay(2500);
						mouse.doDoubleClick();
						minion.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
						return true;
					}
				}
			}
		}

		return false;
	}

	public void makeGlyphs()
	{
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
			System.out.println("unknown error at minion creation.");
			view.addText("\nunknown error at minion creation.");
		}

		// center of screen and make active window
		minion.mouseMove(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2); // 960,540
		mouse.mouseClick();
		minion.delay(500);

		do
		{
			potencyFound = false;
			aspectFound = false;
			essenceFound = false;

			if(potencyCheck())
			{
				System.out.println("POTENCY CHECK!");
				view.addText("Potency Check!");
				potencyFound = true;
			}
			else
			{
				System.out.println("Didnt find potency");
				view.addText("Didnt find potency");
				potencyFound = false;
				// add potency
			}

			if(aspectCheck())
			{
				System.out.println("ASPECT CHECK!");
				view.addText("Aspect Check!");
				aspectFound = true;
			}
			else
			{
				System.out.println("Didnt find aspect");
				view.addText("Didnt find aspect");
				// add aspect
				potencyFound = false;
			}

			if(essenceCheck())
			{
				System.out.println("ESSENCE CHECK!");
				view.addText("Essence Check!");
				essenceFound = true;
			}
			else
			{
				System.out.println("Didnt find essence");
				view.addText("Didnt find essence");
				// add essence
				potencyFound = false;
			}

			if(potencyFound && aspectFound && essenceFound)
			{
				System.out.println("Crafting now..");
				view.addText("\nCrafting...");
				minion.keyPress(KeyEvent.VK_R);
				minion.keyRelease(KeyEvent.VK_R);
				minion.delay(4000);
			}
			else
			{
				System.out.println("CRAFTING FAILED");
				view.addText("\nCrafting failed");
			}

		}
		while(potencyFound && aspectFound && essenceFound);
		System.out.println("FINISHED");
	}
}