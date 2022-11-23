package droneGUI;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

//can we use the ball code given to us - it feels too good to be true

//future tasks:
//load save file
//scoreboard
//tostring sideboard
//add super defender

public class DroneViewer extends Application{
	private final int canvasSize = 512;
	
	private AnimationTimer timer;								
	private VBox rtPane;
	
	private DroneArena da;
	private MyCanvas mc;
		
	
	//adding things -------------------------------------------------------------------
	
	/**
	 * Function to set up the menu
	 */
	private MenuBar setMenu() {
		//create menuBar
		MenuBar menuBar = new MenuBar();		
					
		//help drop-down
		Menu mHelp = new Menu("Help");
		MenuItem mAbout = new MenuItem("About");
		mAbout.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent a1) {
				showAbout();
			}
		});
		mHelp.getItems().addAll(mAbout);
		
		//file drop-down
		Menu mFile = new Menu("File");
		MenuItem mExit = new MenuItem("Exit");
		mExit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent a2) {
				System.exit(0);
			}
		});
		mFile.getItems().addAll(mExit);
		
		//add menus to menuBar
		menuBar.getMenus().addAll(mFile, mHelp);
		return menuBar;
	}
	
	/**
	 * set up the horizontal box for the bottom with relevant buttons
	 * @return
	 */
	private HBox setButtons() {
	    
		//Run buttons
		Button btnStart = new Button("Start");					
	    btnStart.setOnAction(new EventHandler<ActionEvent>() {	
	        @Override
	        public void handle(ActionEvent event) {
	        	timer.start();									
	       }
	    });

	    Button btnStop = new Button("Pause");					
	    btnStop.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	           	timer.stop();								
	       }
	    });

	    
	    //Add buttons
	    Button btnAttack = new Button("Attack Drone");				
	    btnAttack.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	//gui input of speed
	        	double speed = 0;
	        	da.addAttackDrone(speed);								
	           	drawWorld();
	       }
	    });
	    
	    Button btnDefend = new Button("Defender Drone");				
	    btnDefend.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	//gui input of speed
	        	double speed = 0;
	           	da.addDefenderDrone(speed);								
	           	drawWorld();
	       }
	    });
	    
	    Button btnTarget = new Button("Target Drone");				
	    btnTarget.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	           	
	        	da.addTargetDrone();								
	           	drawWorld();
	       }
	    });
	    	
	    														
	    
	    return new HBox(new Label("Run: "), btnStart, btnStop, new Label("Add: "), btnAttack, btnDefend, btnTarget);
	}
	
	
	//functions -----------------------------------------------------------
	
	/**
	  * Function to show a message, 
	  * @param TStr		title of message block
	  * @param CStr		content of message
	  */
	private void showMessage(String TStr, String CStr) {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle(TStr);
	    alert.setHeaderText(null);
	    alert.setContentText(CStr);

	    alert.showAndWait();
	}
	
	/**
	 * function to show in a box ABout the programme
	 */
	private void showAbout() {
		showMessage("About", "RJM's Solar System Demonstrator");
	}	
	
	public void drawWorld () {
	 	mc.clearCanvas();						// set beige colour
	 	da.drawArena(mc);
	}
	
	
	//main --------------------------------------------------------------------------
	
	public void start(Stage stagePrimary) {
		//main
		stagePrimary.setTitle("Drone Simulator");
		BorderPane bp = new BorderPane();
		
		
		//top
		bp.setTop(setMenu());
		
		//bottom
		bp.setBottom(setButtons());	
		
		//middle
		Group grp = new Group();
		Canvas canvas = new Canvas(canvasSize, canvasSize);
		grp.getChildren().add(canvas);
		bp.setCenter(grp);
		mc = new MyCanvas(canvas.getGraphicsContext2D(), canvasSize, canvasSize);
		da = new DroneArena(canvasSize, canvasSize);
		
		//right
		rtPane = new VBox();
		bp.setRight(rtPane);
		
		//animation 
		timer = new AnimationTimer() {
			@Override
			public void handle(long currentNanoTime) {
				//da.checkDrones();									// check the angle of all balls
	            //da.adjustDrones();								// move all balls
				drawWorld();
			}
		};
		
		//finalise
		Scene scene = new Scene(bp, canvasSize*1.6, canvasSize*1.2);
		stagePrimary.setScene(scene);
		stagePrimary.show();	
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
