package Controller;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.ArrayList;

import Utility.MouseFunctions;
import View.MinionUI;

public class GlyphDestroyerV2
{
	private MinionUI minionUI;
	private Dimension dimension;
	private final int SCREEN_WIDTH;
	private final int SCREEN_HEIGHT;

	private Robot robot;
	private MouseFunctions mouse;

	// colors of glyphs
	private Color bronzeColor;
	private Color silverColor;
	private Color goldColor;

	// center point
	private Point centerGylphPoint;

	// search positions for runes in inventory
	private ArrayList<Point> invPositions;

	// integers for easy variance adjustment where Color checks are occuring
	private int inventoryColorVariance;
	private int centerColorVariance;

	// counter to let player know how many glyphs were completed
	private int glyphCount;

	public GlyphDestroyerV2(MinionUI minionUI)
	{
		try
		{
			robot = new Robot();
		} catch (AWTException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			minionUI.addText("\nUnknown error at minion creation.");
		}

		this.minionUI = minionUI;
		mouse = new MouseFunctions();

		// screen
		dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SCREEN_WIDTH = (int) dimension.getWidth();
		SCREEN_HEIGHT = (int) dimension.getHeight();

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
		
		inventoryColorVariance = 30;
		centerColorVariance = 20;
		
		glyphCount = 0;
	}
}
