package Utility;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class MouseFunctions 
{
	public Robot minion;
	
	public MouseFunctions()
	{
		try 
		{
			minion = new Robot();
		} 
		catch (AWTException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void mouseClick()
	{
		minion.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		minion.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	//Double clicks the mouse
	public void doDoubleClick()
	{
		minion.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		minion.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		minion.delay(200);
		minion.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		minion.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void rollUpOne()
	{
		minion.mouseMove(1420, 365);
		minion.delay(1000);
		minion.mouseWheel(-1);
	}

	public void rollDownOne()
	{
		minion.mouseMove(1420, 365);
		minion.delay(1000);
		minion.mouseWheel(1);
	}

}
