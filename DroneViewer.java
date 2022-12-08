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
import javafx.scene.text.Font;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * @author Steven Whitby
 * GUI class for Drone Simulator
 */
public class DroneViewer extends Application{
	private final int canvasSize = 512;
	private ArrayList<String> droneStrings = new ArrayList<>();
	private Random ranGen = new Random();
	
	
	//javafx components
	private Scene scene;
	private Stage stage;
	private AnimationTimer timer;								
	private VBox rtPane, ltBotPane, ltTopPane;
	private Button btnStart, btnStop;
	private Slider slider;
	private FileChooser chooser = new FileChooser();
	
	
	//class declarations
	private DroneArena da;
	private MyCanvas mc;
	
	
	//adding things -------------------------------------------------------------------
	
	/**
	 * Function to set up the menu
	 * @return the menu bar
	 */
	private MenuBar setMenu() {
		//create menuBar
		MenuBar menuBar = new MenuBar();		
					
		//file drop-down
		Menu mFile = new Menu("File");
		
		//new arena button
		MenuItem mNew = new MenuItem("New Arena");
		mNew.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				timer.stop();
        		offColour();	
        		da = new DroneArena(canvasSize, canvasSize, 15, 0);
        		slider.setValue(15);
        		drawWorld();
        		drawStatus();
			}
		});
		
		//save arena button
		MenuItem mSave = new MenuItem("Save Arena");
		mSave.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				timer.stop();
        		offColour();	
				saveArena(da);
			}
		});
		
		//load arena button
		MenuItem mLoad = new MenuItem("Load Arena");
		mLoad.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				timer.stop();
        		offColour();	
        		loadArena(loadArenaInfo());
			}
		});
		
		//Exit button
		MenuItem mExit = new MenuItem("Exit");
		mExit.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				System.exit(0);
			}
		});
		
		
		mFile.getItems().addAll(mNew, mSave, mLoad, mExit);
		
		//help drop-down
		Menu mHelp = new Menu("Help");
		
		//about button
		MenuItem mAbout = new MenuItem("About");
		mAbout.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent a) {
				showAbout();
			}
		});
		mHelp.getItems().addAll(mAbout);
		
		//add drop-downs to menuBar
		menuBar.getMenus().addAll(mFile, mHelp);
		return menuBar;
	}
	
	/**
	 * set up the horizontal box for the bottom with relevant buttons
	 * @return the HBox
	 */
	private HBox setButtons() {
	    
		//Run buttons
		btnStart = new Button("Start");
		btnStop = new Button("Pause");
		
		//start button
		btnStart.setStyle("-fx-background-color: #ffffff; \n -fx-text-fill: #000000; \n -fx-border-color: #000000;");
	    btnStart.setOnAction(new EventHandler<ActionEvent>() {	
	        @Override
	        public void handle(ActionEvent event) {
	        	timer.start();
	        	onColour();		
	       }
	    });

	    //pause button
	    btnStop.setStyle("-fx-background-color: #000000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
	    btnStop.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	           	timer.stop();	
	           	offColour();  
	       }
	    });

	    
	    //Add Drone buttons
	    
	    //Add attack drone
	    Button btnAttack = new Button("Attack Drone");
	    btnAttack.setStyle("-fx-background-color: #ff0000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
	    btnAttack.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	if(!da.checkDroneMax()) {
	        		da.addAttackDrone(ranGen.nextDouble(da.getXSize()), ranGen.nextDouble(da.getYSize()), ranGen.nextDouble(360));								
		           	drawWorld();
		           	drawStatus();
	        	} else {
	        		showMaxMessage();
	        	}
	        	
	        	
	       }
	    });
	    
	    //Add defender drone
	    Button btnDefend = new Button("Defender Drone");	
	    btnDefend.setStyle("-fx-background-color: #00ff00; \n -fx-text-fill: #000000; \n -fx-border-color: #000000;");
	    btnDefend.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	if(!da.checkDroneMax()) {
	        		da.addDefenderDrone(ranGen.nextDouble(da.getXSize()), ranGen.nextDouble(da.getYSize()), ranGen.nextDouble(360));								
		           	drawWorld();
		           	drawStatus();
	        	} else {
	        		showMaxMessage();
	        	}
	        	
	       }
	    });
	   
	    //add target drone
	    Button btnTarget = new Button("Target Drone");	
	    btnTarget.setStyle("-fx-background-color: #0000ff; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
	    btnTarget.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	if(!da.checkDroneMax()) {
	        		da.addTargetDrone(ranGen.nextDouble(da.getYSize()), ranGen.nextDouble(da.getYSize()));								
		           	drawWorld();
		           	drawStatus();
	        	} else {
	        		showMaxMessage();
	        	}
	       }
	    });
	    
	    //remove drone button
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
	   
	    //return row of buttons										
	    return new HBox(new Label("Run: "), btnStart, btnStop, new Label("Add: "), btnAttack, btnDefend, btnTarget, btnRemove);
		 
	}
	
	/**
	 * set up the speed selection slider panel
	 * @return VBox containing the speed selector
	 */
	private VBox setSpeedSelector() {
		Label label = new Label("Speed Selector");
        Label displayLabel = new Label(" ");
 
      
        displayLabel.setTextFill(Color.BLACK);
 
        // create slider
        slider = new Slider(0, 30, 15);
  
        //slider aesthetics
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(5);
        slider.setBlockIncrement(4);
        
        displayLabel.textProperty().setValue("15");
		
        //change speed of drones according to slider
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
		
        //add components to VBox and return
		ltBotPane = new VBox();
		ltBotPane.getChildren().addAll(label, displayLabel, slider);
		
		return ltBotPane;
	}
	
	
	//functions -----------------------------------------------------------
	
	/**
	  * set up the mouse event - when mouse pressed, put drone there
	  * @param canvas
	  */
	void setMouseEvents (Canvas canvas) {
	       canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 		// for MOUSE PRESSED event
	    	       new EventHandler<MouseEvent>() {
	    	           @Override
	    	           public void handle(MouseEvent e) {
	    	        	   	if(!da.checkDroneMax()) {
	    	        	   		da.addUserDrone(e.getX(), e.getY());
		  		            	drawWorld();							// redraw world
		  		            	drawStatus();
	    	        	   	}else {
	    	        	   		showMaxMessage();
	    	        	   	}
	    	           }
	    	       });
	}
	
	/**
	 * flip the switch aesthetics to "on"
	 */
	private void onColour() {
		//black start
		btnStart.setStyle("-fx-background-color: #000000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
		//white stop
		btnStop.setStyle("-fx-background-color: #ffffff; \n-fx-text-fill: #000000; \n -fx-border-color: #000000;");
	}
	
	/**
	 * flip the switch aesthetics to "off"
	 */
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
	 * function to show, in a box, About the programme
	 */
	private void showAbout() {
		showMessage("About", "Welcome to my drone simulator, here are the key features" + "\n" +
								"- start/pause buttons" + "\n" +
								"- adding Attack/Defender/Target drones via their respectively coloured buttons" + "\n" +
								"- adding User drones via a mouse click within the arena" + "\n" +
								"- adjustable drone speed via slider" + "\n" +
								"- scoreboard to keep track of number of times an Attack drone collides with a Target Drone" + "\n" +
								"- save/load drone arena states" + "\n" +
								"- start new drone arena state" + "\n" +
								"- a max drone limit");
	}	
	
	/**
	 * function to show, in a box, max drone error message
	 */
	private void showMaxMessage() {
		showMessage("Oh No", "Max drone capacity reached");
	}
	
	/**
	 * function to show, in a box, min drone error message
	 */
	private void showMinMessage() {
		showMessage("Oh No", "No drones to remove");
	}
	
	/**
	 * function to show, in a box, min drone error message
	 */
	private void showSaveMessage() {
		showMessage("Hooray!", "File Saved Successfully");
	}
	
	/** 
	 * draw the world 
	 */
	public void drawWorld () {
	 	mc.clearCanvas();						// set beige colour
	 	da.drawArena(mc);
	}
	
	/**
	 * show where drones is, on right pane, and the score, on left pane
	 */
	public void drawStatus() {
		//clear
		rtPane.getChildren().clear();
		ltTopPane.getChildren().clear();
		
		//drone status display
		droneStrings = da.describeAll();
		for (String s : droneStrings) {
			Label l = new Label(s); 		// turn description into a label
			l.setFont(new Font(12.5));
			rtPane.getChildren().add(l);	// add label	
		}
		
		//score display
		Label lab = new Label("Score: " + Math.round(da.getScore()));
		ltTopPane.getChildren().add(lab);
		
	}
	
	/**
	 * saves DroneArena to string into chosen file
	 * @param da 		drone arena which current state will be extracted from
	 */
	private void saveArena(DroneArena da) {
	    		try {
	    			//open file explorer
	    			File saveFile = chooser.showSaveDialog(stage);
		    		
		    		if(saveFile != null) {
		    			try {
			    			FileOutputStream saveStream = new FileOutputStream(saveFile);		//open an output stream
			    			byte[] strToBytes = da.toString().getBytes(StandardCharsets.UTF_8);	//convert string to bytes
			    			saveStream.write(strToBytes);										//save byte string via stream
			    			saveStream.close(); 
			    			showSaveMessage();
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

	
	/**
	 * returns a string containing the contents of the file selected
	 * @return String containing parameters for saved drone arena state
	 */
	private String loadArenaInfo() {
		String arenaInfo = "";	//string to be written into
    	//open file explorer
		File openFile = chooser.showOpenDialog(stage);
    		if(openFile != null && openFile.isFile()) {
    			try {
					FileInputStream openStream = new FileInputStream(openFile);	//open an input stream
					byte[] byteArr = new byte[(int)openFile.length()];			//create byte array
					openStream.read(byteArr);									//read file contents into byte array
					arenaInfo = new String(byteArr, StandardCharsets.UTF_8);	//convert byte array into string
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
	
	
	/**
	 * instantiates a new DroneArena with the parameters from string
	 * @param ai 		String containing parameters for saved drone arena state
	 */
	private void loadArena(String ai) {
	
		//variables to store parameters 
    	double[] arenaParams = new double[4];
    	double[] droneParams = new double[3];
    	String droneType = "";
    	int arenaCounter = 0;
    	int droneCounter = 0;
    	
    	//split string into lines
    	String[] lines = ai.split("\n");
    	
    	for(int i = 0; i < lines.length; i++) {
    		//split lines into words
    		String[] words = lines[i].split(" ");
    		
    		if(containsDigit(lines[i])){
    			for(int j = 0; j < words.length; j++) {
    				
    				if(i == 0 && isNumeric(words[j])) {	//arena parameters
        				arenaParams[arenaCounter] = Double.parseDouble(words[j]);
        				arenaCounter++;
        			} else if(i != 0 && isNumeric(words[j])) { //drone parameters
        				droneParams[droneCounter] = Double.parseDouble(words[j]);
        				droneCounter++;
        				
        			}
        			else if(words[j].equals("Attack") || words[j].equals("Defender") || words[j].equals("Target") ||words[j].equals("User")){ //drone type
        				droneType = words[j];
        			}
    				
        		}
    
    			//when there are enough parameters collected for the arena or drone
    			
    			if(arenaCounter == 4) { //create new arena
    				da = new DroneArena(arenaParams[0], arenaParams[1], arenaParams[2], arenaParams[3]);
    				da.removeAllDrones();
    				slider.setValue(arenaParams[2]);
    				arenaCounter = 0;				
    			}else if(droneCounter == 3 && droneType.equals("Attack")) { //add attacker
    				da.addAttackDrone(droneParams[0], droneParams[1], droneParams[2]);
    				droneType = "";
    				droneCounter = 0;
    			}
    			else if(droneCounter == 3 && droneType.equals("Defender")) { //add defender
    				da.addDefenderDrone(droneParams[0], droneParams[1], droneParams[2]);
    				droneType = "";
    				droneCounter = 0;
    			}
    			else if(droneCounter == 2 && droneType.equals("Target")) { //add target
    				da.addTargetDrone(droneParams[0], droneParams[1]);
    				droneType = "";
    				droneCounter = 0;
    			}
    			else if(droneCounter == 2 && droneType.equals("User")) { //add user
    				da.addUserDrone(droneParams[0], droneParams[1]);
    				droneType = "";
    				droneCounter = 0;
    			}
    		}
    		
    		
    		//display new arena
    		drawWorld();
    		drawStatus();
    		
    	}
    	
    	
    }
    
	/**
	 * Determines if a string contains a digit
	 * @param str 	string to be determined for containing a digit
	 * @return 	boolean value whether the string does contain a digit
	 */
	public static boolean containsDigit(String str) {
    	char[] chars = str.toCharArray();
    	
    	for(char c: chars) {
    		if(Character.isDigit(c)) return true;
    	}
    	
    	return false;
    }
	
	
	/**
	 * Determines if a string is a number
	 * @param str 	string to be determined for being a number
	 * @return 	boolean value whether the string is a number
	 */
	public static boolean isNumeric(String strNum) {
    	if(strNum == null) return false;
    	
    	try {
    		double x = Double.parseDouble(strNum);
    	} catch (NumberFormatException e) {
    		return false;
    	}
    	
    	return true;
    }
	
	//main --------------------------------------------------------------------------
	
	@Override
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
		
		
		//finalise
		drawStatus();
		scene = new Scene(bp, canvasSize*1.6, canvasSize*1.2);
		setMouseEvents(canvas);
		stagePrimary.setScene(scene);
		stagePrimary.show();	
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
}
