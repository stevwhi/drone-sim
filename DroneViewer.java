package droneGUI;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


//can we use the ball code given to us - it feels too good to be true

//future tasks:
//load save file
//scoreboard
//tostring sideboard
//add super defender

public class DroneViewer extends Application{
	private final int canvasSize = 512;
	private ArrayList<String> droneStrings = new ArrayList<>();
	
	private Stage stage;
	private AnimationTimer timer;								
	private VBox rtPane, ltBotPane, ltTopPane;
	private Button btnStart, btnStop;
	private Slider slider;
	
	private DroneArena da;
	private MyCanvas mc;
	
	private FileChooser chooser = new FileChooser();
	
	private Random ranGen = new Random();
		
	
	//adding things -------------------------------------------------------------------
	
	/**
	 * Function to set up the menu
	 */
	private MenuBar setMenu() {
		//create menuBar
		MenuBar menuBar = new MenuBar();		
					
		//file drop-down
		Menu mFile = new Menu("File");
		
		MenuItem mNew = new MenuItem("New Arena");
		mNew.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				timer.stop();
        		offColour();	
        		da = new DroneArena(canvasSize, canvasSize, 15, 0);
        		drawWorld();
        		drawStatus();
			}
		});
		
		
		MenuItem mSave = new MenuItem("Save Arena");
		mSave.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				timer.stop();
        		offColour();	
				saveArena(da);
			}
		});
		
		MenuItem mLoad = new MenuItem("Load Arena");
		mLoad.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				timer.stop();
        		offColour();	
        		loadArena(loadArenaInfo());
			}
		});
		
		
		
		
		MenuItem mExit = new MenuItem("Exit");
		mExit.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				System.exit(0);
			}
		});
		
		
		mFile.getItems().addAll(mSave, mLoad, mExit);
		
		//help drop-down
		Menu mHelp = new Menu("Help");
		MenuItem mAbout = new MenuItem("About");
		mAbout.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent a) {
				showAbout();
			}
		});
		mHelp.getItems().addAll(mAbout);
		
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
		btnStart = new Button("Start");
		btnStop = new Button("Pause");
		
		
		btnStart.setStyle("-fx-background-color: #ffffff; \n -fx-text-fill: #000000; \n -fx-border-color: #000000;");
	    btnStart.setOnAction(new EventHandler<ActionEvent>() {	
	        @Override
	        public void handle(ActionEvent event) {
	        	timer.start();
	        
	        	onColour();	
	        	
	       }
	    });

	    	
	    btnStop.setStyle("-fx-background-color: #000000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
	    btnStop.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	           	timer.stop();	
	           	
	           	offColour();
	           	
	          
	       }
	    });

	    
	    //Add buttons
	    Button btnAttack = new Button("Attack Drone");
	    btnAttack.setStyle("-fx-background-color: #ff0000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
	    btnAttack.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	//gui input of speed
	        	//double speed = 0;
	        	
	        	if(!da.checkDroneMax()) {
	        		da.addAttackDrone(ranGen.nextDouble(da.getXSize()), ranGen.nextDouble(da.getYSize()), ranGen.nextDouble(360));								
		           	drawWorld();
		           	drawStatus();
	        	} else {
	        		showMaxMessage();
	        	}
	        	
	        	
	       }
	    });
	    
	    Button btnDefend = new Button("Defender Drone");	
	    btnDefend.setStyle("-fx-background-color: #00ff00; \n -fx-text-fill: #000000; \n -fx-border-color: #000000;");
	    btnDefend.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	//gui input of speed
	        	//double speed = 0;
	      
	        	if(!da.checkDroneMax()) {
	        		da.addDefenderDrone(ranGen.nextDouble(da.getXSize()), ranGen.nextDouble(da.getYSize()), ranGen.nextDouble(360));								
		           	drawWorld();
		           	drawStatus();
	        	} else {
	        		showMaxMessage();
	        	}
	        	
	       }
	    });
	    
	    Button btnTarget = new Button("Target Drone");	
	    btnTarget.setStyle("-fx-background-color: #0000ff; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
	    btnTarget.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	           	
	        	//put -getradius size so it doesnt spawn outside arena
	        	
	        	
	        	if(!da.checkDroneMax()) {
	        		
	        		da.addTargetDrone(ranGen.nextDouble(da.getYSize()), ranGen.nextDouble(da.getYSize()));								
		           	drawWorld();
		           	drawStatus();
	        	} else {
	        		showMaxMessage();
	        	}
	        	
	        	
	       }
	    });
	    
	    
	    Button btnRemove = new Button("Remove Drone");
	    btnRemove.setStyle("-fx-background-color: #ffffff; \n-fx-text-fill: #000000; \n -fx-border-color: #000000;");
	    btnRemove.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	           	
	        	if(!da.checkDroneMin()) {
	        		da.removeLastDrone();							
		           	drawWorld();
		           	drawStatus();
	        	} else {
	        		showMinMessage();
	        	}
	   	
	        	
	       }
	    });
	    
	    
	    
	    	
	    												
	    return new HBox(new Label("Run: "), btnStart, btnStop, new Label("Add: "), btnAttack, btnDefend, btnTarget, btnRemove);
	}
	
	private VBox setSpeedSelector() {
		Label label = new Label("Speed Selector");
        Label displayLabel = new Label(" ");
 
        // set the color of the text
        displayLabel.setTextFill(Color.BLACK);
 
        // create slider
        slider = new Slider(0, 30, 15);
  
 
     // enable the marks
        slider.setShowTickMarks(true);
 
        // enable the Labels
        slider.setShowTickLabels(true);
 
        // set Major tick unit
        slider.setMajorTickUnit(5);
        // sets the value of the property
        // blockIncrement
        slider.setBlockIncrement(4);
        
        displayLabel.textProperty().setValue("15");
		
        slider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
               ObservableValue<? extends Number> observableValue, 
               Number oldValue, 
               Number newValue) { 
                  displayLabel.textProperty().setValue(String.valueOf(newValue.intValue()));
                  da.setSpeed(newValue.doubleValue());
              }
        
    });
		
		
		
		
		ltBotPane = new VBox();
		ltBotPane.getChildren().addAll(label, displayLabel, slider);
		
		return ltBotPane;
	}
	
	
	//functions -----------------------------------------------------------
	
	
	private void onColour() {
		//black start
		btnStart.setStyle("-fx-background-color: #000000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
		
		//white stop
		btnStop.setStyle("-fx-background-color: #ffffff; \n-fx-text-fill: #000000; \n -fx-border-color: #000000;");
	}
	
	private void offColour() {
		//black stop
		btnStop.setStyle("-fx-background-color: #000000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
		
		//white start
		btnStart.setStyle("-fx-background-color: #ffffff; \n -fx-text-fill: #000000; \n -fx-border-color: #000000;");
	}
	
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
	 * function to show in a box About the programme
	 */
	private void showAbout() {
		showMessage("About", "RJM's Solar System Demonstrator");
	}	
	
	private void showMaxMessage() {
		showMessage("Oh No", "Max drone capacity reached");
	}
	
	private void showMinMessage() {
		showMessage("Oh No", "No drones to remove");
	}
	/** 
	 * draw the world 
	 */
	public void drawWorld () {
	 	mc.clearCanvas();						// set beige colour
	 	da.drawArena(mc);
	}
	
	/**
	 * show where drones is, in pane on right
	 */
	public void drawStatus() {
		//clear
		rtPane.getChildren().clear();
		ltTopPane.getChildren().clear();
		
		//display
		droneStrings = da.describeAll();
		for (String s : droneStrings) {
			Label l = new Label(s); 		// turn description into a label
			rtPane.getChildren().add(l);	// add label	
		}
		
		Label lab = new Label("Score: " + Math.round(da.getScore()));
		ltTopPane.getChildren().add(lab);
		
	}
	
	private void saveArena(DroneArena da) {
	    		try {
	    			File saveFile = chooser.showSaveDialog(stage);
		    		System.out.println("here");
		    		
		    		if(saveFile != null) {
		    			try {
			    			FileOutputStream saveStream = new FileOutputStream(saveFile);
			    			byte[] strToBytes = da.toString().getBytes(StandardCharsets.UTF_8);
			    			saveStream.write(strToBytes);
			    			saveStream.close();
			    			System.out.println("Save successful");
			    		} catch (FileNotFoundException e) {
			    			e.printStackTrace();
			    		} catch (IOException e) {
							e.printStackTrace();
			    		} 
		    		}
	    		} catch(Exception e) {
	    			e.printStackTrace();
	    		}
		
					
		
	}


	private String loadArenaInfo() {
		String arenaInfo = "";
    	//select file
		
		
		File openFile = chooser.showOpenDialog(stage);
    	
    		
    		if(openFile != null && openFile.isFile()) {
    			try {
					FileInputStream openStream = new FileInputStream(openFile);
					byte[] byteArr = new byte[(int)openFile.length()];
					openStream.read(byteArr);
					arenaInfo = new String(byteArr, StandardCharsets.UTF_8);
					openStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}else {
    			System.out.println("not a file");
    		}
    	
    	
    	return arenaInfo;
	}
	
	private void loadArena(String ai) {
    	//read and store text from file into variables
    	
    	//System.out.print(ai); //does print, problem with scanning
		
		
		System.out.println(ai);
    	
    	double[] arenaParams = new double[4];
    	double[] droneParams = new double[3];
    	String droneType = "";
    	int arenaCounter = 0;
    	int droneCounter = 0;
    	
    	String[] lines = ai.split("\n");
    	
    	for(int i = 0; i < lines.length; i++) {
    		String[] words = lines[i].split(" ");
    		
    		if(containsDigit(lines[i])){
    			
    			for(int j = 0; j < words.length; j++) {
    				//number on first line
    				if(i == 0 && isNumeric(words[j])) {
    					
    					
    					
        				arenaParams[arenaCounter] = Double.parseDouble(words[j]);
        				arenaCounter++;
        			} else if(i != 0 && isNumeric(words[j])) {
        				
        				droneParams[droneCounter] = Double.parseDouble(words[j]);
        				droneCounter++;
        				
        			}
        			else if(words[j].equals("Attack") || words[j].equals("Defender") || words[j].equals("Target")){
        				droneType = words[j];
        			}
    				
        		}
    			
    			
    			if(arenaCounter == 4) {
    				System.out.println(arenaParams[0] + ", " + arenaParams[1]+ ", "+ arenaParams[2] +", " +arenaParams[3]);
    				da = new DroneArena(arenaParams[0], arenaParams[1], arenaParams[2], arenaParams[3]);
    				slider.setValue(arenaParams[2]);
    				
    				arenaCounter = 0;
    			}else if(droneCounter == 3 && droneType.equals("Attack")) {
    				System.out.println(droneParams[0] + ", " + droneParams[1] + ", " + droneParams[2]);
    				da.addAttackDrone(droneParams[0], droneParams[1], droneParams[2]);
    				droneType = "";
    				droneCounter = 0;
    			}
    			else if(droneCounter == 3 && droneType.equals("Defender")) {
    				System.out.println(droneParams[0] + ", " + droneParams[1]+ ", "+ droneParams[2]);
    				da.addDefenderDrone(droneParams[0], droneParams[1], droneParams[2]);
    				droneType = "";
    				droneCounter = 0;
    			}
    			else if(droneCounter == 2 && droneType.equals("Target")) {
    				System.out.println(droneParams[0] + ", " + droneParams[1]);
    				da.addTargetDrone(droneParams[0], droneParams[1]);
    				droneType = "";
    				droneCounter = 0;
    			}
    			
    		}
    		
    		drawWorld();
    		drawStatus();
    		
    	}
    	
    	
    }
    
	
	public static boolean containsDigit(String str) {
    	char[] chars = str.toCharArray();
    	
    	for(char c: chars) {
    		if(Character.isDigit(c)) return true;
    	}
    	
    	return false;
    }
	
	public static boolean isNumeric(String strNum) {
    	if(strNum == null) return false;
    	
   
    	
    	try {
    		double x = Double.parseDouble(strNum);
    	} catch (NumberFormatException e) {
    		return false;
    	}
    	
    	return true;
    }
	
	/*
	public static String roundString(String str) {
		
		int iend = str.indexOf("."); //this finds the first occurrence of "." 
		//in string thus giving you the index of where it is in the string

		// Now iend can be -1, if lets say the string had no "." at all in it i.e. no "." is found. 
		//So check and account for it.

		String subString = "";
		if (iend != -1) 
		{
		    subString= str.substring(0 , iend); //this will give abc
		}
		
		return subString;
	}
	*/
	
	
	//main --------------------------------------------------------------------------
	
	public void start(Stage stagePrimary) {
		this.stage = stagePrimary;
		//main
		stagePrimary.setTitle("Drone Simulator");
		BorderPane bp = new BorderPane();
		BorderPane bordp = new BorderPane();
		bp.setPadding(new Insets(10, 20, 10, 20));
		
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
		da = new DroneArena(canvasSize, canvasSize, 15, 0);
		drawWorld();
		
		 //GraphicsContext gc = canvas.getGraphicsContext2D();
		 //gc.strokeText("drone simulation", 1, 1);

		//left
		ltTopPane = new VBox();
		ltBotPane = new VBox();
		
		bp.setLeft(bordp);
		bordp.setBottom(setSpeedSelector());
		bordp.setTop(ltTopPane);
		
		
		//right
		rtPane = new VBox();
		bp.setRight(rtPane);
		
		//animation 
		timer = new AnimationTimer() {
			@Override
			public void handle(long currentNanoTime) {
				da.checkDrones();									// check the angle of all drones
	            da.adjustDrones();								// move all drones
				drawWorld();
				drawStatus();
			}
		};
		
		
		
		drawStatus();
		//finalise
		Scene scene = new Scene(bp, canvasSize*1.6, canvasSize*1.2);
		stagePrimary.setScene(scene);
		stagePrimary.show();	
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
