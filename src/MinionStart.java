import Controller.MinionController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MinionStart extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		new MinionController();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
