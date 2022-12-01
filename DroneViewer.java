package droneGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.JFileChooser;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
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
	private ArrayList<String> droneStrings = new ArrayList<>();
	
	private AnimationTimer timer;								
	private VBox rtPane;
	
	private DroneArena da;
	private MyCanvas mc;
	
	private JFileChooser chooser = new JFileChooser();
		
	
	//adding things -------------------------------------------------------------------
	
	/**
	 * Function to set up the menu
	 */
	private MenuBar setMenu() {
		//create menuBar
		MenuBar menuBar = new MenuBar();		
					
		//file drop-down
		Menu mFile = new Menu("File");
		
		
		MenuItem mSave = new MenuItem("Save Arena");
		mSave.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
				saveArena(da);
			}
		});
		
		MenuItem mLoad = new MenuItem("Load Arena");
		mLoad.setOnAction(new EventHandler<ActionEvent>() {
		public void handle(ActionEvent a) {
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
		Button btnStart = new Button("Start");
		Button btnStop = new Button("Pause");
		
		
		btnStart.setStyle("-fx-background-color: #ffffff; \n -fx-text-fill: #000000; \n -fx-border-color: #000000;");
	    btnStart.setOnAction(new EventHandler<ActionEvent>() {	
	        @Override
	        public void handle(ActionEvent event) {
	        	timer.start();
	        
	        		//black start
	        		btnStart.setStyle("-fx-background-color: #000000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
	        		
	        		//white stop
	        		btnStop.setStyle("-fx-background-color: #ffffff; \n-fx-text-fill: #000000; \n -fx-border-color: #000000;");
	        	
	       }
	    });

	    	
	    btnStop.setStyle("-fx-background-color: #000000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
	    btnStop.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	           	timer.stop();	
	           	
	          //black stop
        		btnStop.setStyle("-fx-background-color: #000000; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
        		
        		//white start
        		btnStart.setStyle("-fx-background-color: #ffffff; \n -fx-text-fill: #000000; \n -fx-border-color: #000000;");
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
	        	double speed = 15;
	        	da.addAttackDrone(speed);								
	           	drawWorld();
	       }
	    });
	    
	    Button btnDefend = new Button("Defender Drone");	
	    btnDefend.setStyle("-fx-background-color: #00ff00; \n -fx-text-fill: #000000; \n -fx-border-color: #000000;");
	    btnDefend.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	
	        	//gui input of speed
	        	//double speed = 0;
	        	double speed = 15;
	           	da.addDefenderDrone(speed);								
	           	drawWorld();
	       }
	    });
	    
	    Button btnTarget = new Button("Target Drone");	
	    btnTarget.setStyle("-fx-background-color: #0000ff; \n -fx-text-fill: #ffffff; \n -fx-border-color: #000000;");
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
		
		//display
		droneStrings = da.describeAll();
		for (String s : droneStrings) {
			Label l = new Label(s); 		// turn description into a label
			rtPane.getChildren().add(l);	// add label	
		}	
	}
	
	private void saveArena(DroneArena da) {
	
		System.out.println("here");
    	if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
    		
    		File saveFile = chooser.getSelectedFile();
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
	}
	
	private String loadArenaInfo() {
		String arenaInfo = "";
    	//select file
    	
    	if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    		File openFile = chooser.getSelectedFile();
    		if(openFile.isFile()) {
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
    	}
    	
    	return arenaInfo;
	}
	
	private void loadArena(String ai) {
    	//read and store text from file into variables
    	
    	//System.out.print(ai); //does print, problem with scanning
		
		
		
    	
    	double[] arenaParams = new double[2];
    	double[] droneParams = new double[4];
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
        				
        				arenaParams[arenaCounter] = Integer.parseInt(words[j]);
        				arenaCounter++;
        			} else if(i != 0 && isNumeric(words[j])) {
        				
        				droneParams[droneCounter] = Integer.parseInt(words[j]);
        				droneCounter++;
        			}
        			else if(words[j].equals("Attack") || words[j].equals("Defender") || words[j].equals("Target")){
        				droneType = words[j];
        			}
        		}
    			
    			if(arenaCounter == 2) {
    				da = new DroneArena(arenaParams[0], arenaParams[1]);
    				drawWorld();
    				arenaCounter = 0;
    			}else if(droneCounter == 4 && droneType.equals("Attack")) {
    				da.addAttackDrone(droneParams[3]);
    				droneType = "";
    				droneCounter = 0;
    			}
    			else if(droneCounter == 4 && droneType.equals("Defender")) {
    				da.addDefenderDrone(droneParams[3]);
    				droneType = "";
    				droneCounter = 0;
    			}
    			else if(droneCounter == 2 && droneType.equals("Target")) {
    				da.addTargetDrone();
    				droneType = "";
    				droneCounter = 0;
    			}
    			
    		}
    		
    		
    		
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
    		int x = Integer.parseInt(strNum);
    	} catch (NumberFormatException e) {
    		return false;
    	}
    	
    	return true;
    }
	
	
	//main --------------------------------------------------------------------------
	
	public void start(Stage stagePrimary) {
		//main
		stagePrimary.setTitle("Drone Simulator");
		BorderPane bp = new BorderPane();
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
		da = new DroneArena(canvasSize, canvasSize);
		drawWorld();
		
		 //GraphicsContext gc = canvas.getGraphicsContext2D();
		 //gc.strokeText("drone simulation", 1, 1);

		
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
		Scene scene = new Scene(bp, canvasSize*1.6, canvasSize*1.2);
		stagePrimary.setScene(scene);
		stagePrimary.show();	
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
