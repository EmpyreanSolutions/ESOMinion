package Controller;

import java.awt.AWTException;

import View.MinionUI;

public class MinionController
{
	private MinionUI minionUI;
	private GlyphMaker gMaker;
	private GlyphDestroyerV2 gDV2;
	private Thread taskThread;

	public MinionController()
	{
		minionUI = new MinionUI(this);

		try
		{
			gMaker = new GlyphMaker(minionUI);
		}
		catch(AWTException e)
		{
			minionUI.addText("Robot failed to instantiate. Try redownloading file. Please contact support.");
			e.printStackTrace();
		}

		try
		{
			gDV2 = new GlyphDestroyerV2(minionUI);
		}
		catch(AWTException e)
		{
			minionUI.addText("Robot failed to instantiate. Try redownloading file. Please contact support.");
			e.printStackTrace();
		}

	}

	public void newTask(String task)
	{
		threadStop();

		switch(task)
		{
		case "gm":
		{
			minionUI.addText("Running Glyph Maker");
			taskThread = new Thread(gMaker, "gm");
			taskThread.start();
			break;
		}
		case "gd":
		{
			minionUI.addText("Running Glyph Destroyer");
			taskThread = new Thread(gDV2, "gm");
			taskThread.start();
			break;
		}
		default:
		{
			break;
		}
		}
	}

	public void threadStop()
	{
		if(taskThread != null && taskThread.isAlive())
		{

			taskThread.interrupt();
			try
			{
				taskThread.join();
				taskThread = null;
			}
			catch(InterruptedException e)
			{
				minionUI.addText("new task interrupted exception");
				e.printStackTrace();
			}
		}
	}
}
