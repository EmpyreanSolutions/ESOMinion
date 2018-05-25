package View;

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
	Robot minion;

	public ColorPicker()
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
			System.out.println("unkown error at minion creation.");
		}
	}

	public static void main(String[] args)
	{
		ColorPicker colorPicker = new ColorPicker();
		int tempX = 960;
		int tempY = 905;
		colorPicker.minion.mouseMove(tempX, tempY);
		Color c1 = colorPicker.minion.getPixelColor(tempX, tempY);
		System.out.println(c1);
		colorPicker.minion.delay(250);
		colorPicker.minion.mouseMove(tempX, tempY);
		Color c2 = colorPicker.minion.getPixelColor(tempX, tempY);
		System.out.println(c2);
	}

}
