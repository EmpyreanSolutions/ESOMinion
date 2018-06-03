package Utility;
import java.awt.AWTException;
import java.awt.Robot;

import java.awt.Color;


/**
 * @author Daniel Sales
 * 
 * This class will tell you the RGB value at a certain pixel. 
 * 	Coords of the pixel must be manually entered. 
 * 	Results display to the console.
 */
public class ColorPicker
{
	Robot robot;

	public ColorPicker()
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
			System.out.println("unkown error at minion creation.");
		}
	}

	public static void main(String[] args)
	{
		ColorPicker colorPicker = new ColorPicker();
		int tempX = 850;
		int tempY = 940;
		colorPicker.robot.mouseMove(tempX, tempY);
		Color c1 = colorPicker.robot.getPixelColor(tempX, tempY);
		System.out.println(c1);
		colorPicker.robot.delay(250);
		colorPicker.robot.mouseMove(tempX, tempY);
		Color c2 = colorPicker.robot.getPixelColor(tempX, tempY);
		System.out.println(c2);
	}

}
