package View;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Controller.GlyphDestroyer;
import Controller.GlyphMaker;
import Controller.MinionController;
import javafx.application.Platform;

public class MinionUI
{
	private BorderPane bPane;
	private VBox buttonBox;

	private Button gMakerButton;
	private Button gDestroyerButton;
	private Button exit;
	
	private TextArea textArea;
	
	private MinionController minionController;

	public MinionUI(MinionController minionController)
	{
		this.minionController = minionController;
		bPane = new BorderPane();
		bPane.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		gMakerButton = new Button("Glyph Maker");
		gMakerButton.setPrefWidth(150);
		gDestroyerButton = new Button("Glyph Destroyer x10");
		gDestroyerButton.setPrefWidth(150);
		exit = new Button("Terminate");
		exit.setPrefWidth(150);
		
		textArea = new TextArea();
		textArea.setText("Welcome to the Empyrean Solution to ESO's crafting.");
		textArea.setEditable(false);
		textArea.setWrapText(true);
		buttonBox = new VBox();

		buttonBox.getChildren().add(gMakerButton);
		buttonBox.getChildren().add(gDestroyerButton);
		buttonBox.getChildren().add(exit);

		bPane.setLeft(buttonBox);
		bPane.setCenter(textArea);

		buttonActions();
		
		Stage primaryStage = new Stage();
		Scene scene = new Scene(bPane);
		primaryStage.setTitle("EmpyreanSolutionsESOMinion");
		primaryStage.setScene(scene);
		primaryStage.setHeight(200);
		primaryStage.setWidth(400);
		primaryStage.setResizable(false);
		primaryStage.setX(100);
		primaryStage.setY(100);
		primaryStage.show();
	}
	
	public void buttonActions()
	{
		gMakerButton.setOnAction(e ->
		{
			minionController.runGlyphMaker();			
		});

		gDestroyerButton.setOnAction(e ->
		{
			minionController.runGlyphDestroyerX10();
		});

		exit.setOnAction(e ->
		{	
				Platform.exit();	
		});
	}

	public TextArea getTextArea()
	{
		return textArea;
	}

	public void setTextArea(TextArea textArea)
	{
		this.textArea = textArea;
	}

	public void addText(String text)
	{
		textArea.appendText("\n" + text);
//		Platform.runLater(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				textArea.appendText("\n" + text);						
//			}
//		});
	}
}
