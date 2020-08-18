package com.leerdev.graphs.graphVis;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import com.leerdev.graphs.graphVis.GNode.nodeState;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Graph visualization study project
 * @author Artem Leer, e-mail: artem.leer(a)gmail.com
 * @version 1.0.0
 * https://github.com/leerdev/graphs_visualization
 */

public class GraphsApp extends Application{
	protected static MyGraph g; 	//graph
	protected static HashMap<Integer,GNode> vertices;	//vertices set with neighbors
	//defaults
	protected static double repulsion =50.0, gravity = 0.05, damper = 0.2; //calculation defaults
	protected static int rad = 3; 		//radius
	//Constants
	private static final int RSEED = 5;   // random seed
	private static final int SCR_MAX_X=1024, SCR_MAX_Y=768; //screen resolution
	protected static final Color BGR_COL = Color.ALICEBLUE,   //background color
							   NODE_COL = Color.DARKBLUE,	  //node color
							   EDGE_COL = Color.DIMGRAY,		//edge color
							   GREEDY_NODE_COL=Color.RED;   //greedy node color
	private static Group edgesGroup, nodesGroup; 		//screen UI nodes groups
	//helpers
	protected static boolean calcFlag, fileOpened, showEdgesFlag; //flags
	private static int iteration; //counter
	private static double timeSum; //total time for 100 iterations

	//main method - entry point
	public static void main (String[] args) {
		vertices = new HashMap<>();
		calcFlag=false; fileOpened = false; showEdgesFlag = true;
		launch(args);
	}
	
	//JavaFX start method to show the primary stage
	@Override
	public void start(Stage primaryStage) throws Exception {
		edgesGroup = new Group();			// layer of edges
	    nodesGroup = new Group();			// layer of nodes
	    Group mainGroup = new Group();		// creating main  group
		mainGroup.getChildren().add(edgesGroup);	//adding graph edges layer
	    mainGroup.getChildren().add(nodesGroup);	//adding graph nodes layer
	    mainGroup.getChildren().add(cPanel.controls());	//adding control panel				
        Scene scene = new Scene(mainGroup, SCR_MAX_X, SCR_MAX_Y, true);
        scene.setFill(BGR_COL);						// setting up the stage
		primaryStage.setTitle("Graph Visualization");	
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();						//starting the stage
	}
	
	static void init (File f) {
		vertices.clear(); iteration = 0; timeSum = 0;
		nodesGroup.getChildren().clear();
		edgesGroup.getChildren().clear();
		
		g = new MyGraph (f);	
		if (g.getSize()<1) return;
		// creating Screen nodes with randomized position
		Random r = new Random(RSEED);
		for (Integer name: g.getVertices()) {
			GNode node = new GNode (name, r.nextInt(SCR_MAX_X), 
											r.nextInt(SCR_MAX_Y), rad,
											NODE_COL);
			node.setOnMouseDragged (startDrag);
			node.setOnMouseReleased (stopDrag);
			node.weight = g.getNeighbors(node.getName()).size();
			node.setRadius(rad==1?rad:rad+node.weight/6);
			vertices.put(node.getName(),node);
		}
		//adding the nodes to the stage
		for (GNode node: vertices.values()) {
				nodesGroup.getChildren().add(node);  	//adding nodes (circles)
		}
		updateScreenEdges();
		//updating graph information
		cPanel.loadedNodesLabel.setText(Integer.toString(vertices.size()));
		cPanel.itrLabel.setText("");
		cPanel.itrTLabel.setText("");
		cPanel.avgTLabel.setText("");
	}
	
	// opening file method + graph initialization
	static void openFile (Node parentnode) {
		FileChooser chooser = new FileChooser();
		File f = chooser.showOpenDialog((Stage) parentnode.getScene().getWindow());
		if (f!=null) {
			init(f);
			fileOpened=true;
		} 
	}
	
	private static void calculateForces () {
		for (GNode currNode: vertices.values()) {			        //--- iterating through all vertices ---
			currNode.resetForces();
			for (GNode otherNode: vertices.values()) {				//--- iteration through all other vertices
				if (currNode==otherNode) continue;
				currNode.calcRepulsion (otherNode, repulsion);		//calculating repulsions.
				if (g.isNeighbor(currNode, otherNode)) 				//if there's an edge between...
					currNode.calcAttraction (otherNode, gravity); 	//...calculating attractions
				}
			currNode.calcVelocity(damper);	// calculating the velocity with damping
		}
		//setting up new positions 
		for (GNode currNode: vertices.values())
			if (!currNode.isDragged) currNode.updatePos();		
	}
	
	//method to do multi threaded forces calculation
	@SuppressWarnings("unused")
	private static void calculateForcesMT() {
		//to be implemented later
	}
	
	//updating screen per Animation_timer
	static AnimationTimer timer = new AnimationTimer() {
		@Override 
		public void handle (long now) {
	        long startTime = System.nanoTime(); iteration++;
			
	        //standard forces calculation function call
	        calculateForces();
	        
	        //multithreaded forces calculation
	        //calculateForcesMT();

			// calculating runtime of one iteration 
			double elapsedTime = (System.nanoTime() - startTime)/1000000.0;
			//calculating average time of 100 iterations
			if (iteration<100) timeSum+=elapsedTime;
			if (iteration==100) cPanel.avgTLabel.setText(String.format("%.3f", timeSum/100));
			//updating labels
			cPanel.itrLabel.setText(Integer.toString(iteration));
			cPanel.itrTLabel.setText(String.format("%.3f", elapsedTime));
			//updatingEdges
			updateScreenEdges();
		}
	};
	
	//static Task
	
	//drawing edges if checkBox "Show edges" selected
	static void updateScreenEdges() {
		edgesGroup.getChildren().clear();
		if (!showEdgesFlag) return;
		for (Integer nodeName: vertices.keySet()) { //iterating through nodes
			double x1 = vertices.get(nodeName).getX();
			double y1 = vertices.get(nodeName).getY();
			for (Integer otherNodeName: g.getNeighbors(nodeName)) { //iterating through node neighbor
				double x2 = vertices.get(otherNodeName).getX();
				double y2 = vertices.get(otherNodeName).getY();
				Line l = new Line (x1, y1, x2, y2);
				l.setStrokeWidth(0.1); l.setStroke(EDGE_COL);
				edgesGroup.getChildren().add(l);
			}
		}
	}
	

	
	//Returns set of vertices using greedy algorithm
	static void showGreedy() {
		if (vertices.size()<2) return;
		GNode currNode;
		while ((currNode=getNextMinWeightNode()) != null) {
				currNode.state=nodeState.MARKED;
				currNode.setFill(GREEDY_NODE_COL);
				for (int otherNodeName: g.getNeighbors(currNode.getName())) {
					GNode otherNode = vertices.get(otherNodeName);
					otherNode.state = nodeState.DISABLED;
					otherNode.setFill(EDGE_COL);
				}
		}
	}
	
	//helper function to get next node with minimum weight
	private static GNode getNextMinWeightNode() {
		int min=Integer.MAX_VALUE; GNode ret=null;
		for (GNode node: vertices.values()) {
			if (node.state==nodeState.MARKED||
					node.state==nodeState.DISABLED) continue; //skipping marked nodes
			if (node.weight<min) {
				min=node.weight;
				ret=node;
			}
		}
		return ret;
	}
	
	//handlers for dragging nodes
	private static EventHandler<MouseEvent> startDrag = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent t) {
        	GNode node = ((GNode)(t.getSource()));
        	node.setX(t.getSceneX()); node.setY(t.getSceneY());
        	node.isDragged = true;
           	GraphsApp.updateScreenEdges();
        }
    };
    private static EventHandler<MouseEvent> stopDrag = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent t) {
        	GNode node = ((GNode)(t.getSource()));
        	node.isDragged = false;
        }
    }; 
}