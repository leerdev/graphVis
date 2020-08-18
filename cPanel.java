package com.leerdev.graphs.graphVis;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class cPanel {
		protected static Label loadedNodesLabel, 
							   itrLabel, itrTLabel, avgTLabel, 
							   repulsionLabel, gravityLabel, damperLabel, 
							   radiusLabel;
		
		// control panel nodes and handlers
		protected static Group controls () {
			loadedNodesLabel = new Label(); itrLabel = new Label(); itrTLabel = new Label(); avgTLabel = new Label();
			repulsionLabel = new Label(String.format("%.2f", GraphsApp.repulsion));
			gravityLabel = new Label(String.format("%.2f", GraphsApp.gravity));
			damperLabel = new Label(String.format("%.2f", GraphsApp.damper));
			radiusLabel = new Label(String.format("%d", GraphsApp.rad));
			
			Group ret = new Group();
			int baseX=10; int baseY=10;
			
			//background transparent rectangle
			Rectangle r = new Rectangle(baseX, baseY, baseX+170, baseY+500);
			r.setStroke(Color.DARKCYAN);
			r.setFill(Color.rgb(220, 220, 220, 0.85)); r.setArcHeight(10); r.setArcWidth(10);
			ret.getChildren().add(r);
			
			//GridPane
			GridPane cPanel = new GridPane();
			cPanel.setHgap(0); cPanel.setVgap(5); 
			cPanel.setPadding(new Insets(baseY+10,0,0,baseX+10));
			;
			//Creating columns
	        ColumnConstraints col1 = new ColumnConstraints();
	        col1.setPrefWidth(120);
	        cPanel.getColumnConstraints().add(col1);
	        ColumnConstraints col2 = new ColumnConstraints();
	        col2.setPrefWidth(40);
	        col2.setHalignment(HPos.RIGHT);
	        cPanel.getColumnConstraints().add(col2);
			cPanel.setPrefWidth(230);
			
			// summary information 
			cPanel.add(new Text("****** CONTROL PANEL ******"),0,0,2,1);
			cPanel.add(new Text("\u25CF Nodes loaded: "),0,1); cPanel.add(loadedNodesLabel,1,1);
			cPanel.add(new Text("\u25CF Iteration: "),0,2); cPanel.add(itrLabel, 1, 2);
			cPanel.add(new Text("\u25CF T/iteration [ms]: "), 0, 3); cPanel.add(itrTLabel, 1, 3);
			cPanel.add(new Text("\u25CF Tavg (100 itr.) [ms]: "),0,4); 
				cPanel.add(avgTLabel, 1, 4);
			
			// Repulsion slider
			cPanel.add(new Text("Repulsion: "),0,6); cPanel.add(repulsionLabel, 1, 6);
			Slider repulsionSlider = new Slider (0, 50, GraphsApp.repulsion);
			repulsionSlider.setShowTickMarks(true); repulsionSlider.setShowTickLabels(true);
			repulsionSlider.setMajorTickUnit(10);
	        repulsionSlider.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    GraphsApp.repulsion = (double) new_val;
	                    if (GraphsApp.repulsion<0.1) GraphsApp.repulsion = 0.1;
	            		repulsionLabel.setText(String.format("%.2f",GraphsApp.repulsion));
	            }
	        });
	        cPanel.add(repulsionSlider,0,7,2,1);	
	       
	        //Gravity slider
			cPanel.add(new Text("Gravity: "),0,8); cPanel.add(gravityLabel, 1, 8);		
			Slider gravitySlider = new Slider (0.00, 0.1, GraphsApp.gravity);
			gravitySlider.setShowTickMarks(true); gravitySlider.setShowTickLabels(true);
			gravitySlider.setMajorTickUnit(0.02);
	        gravitySlider.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    GraphsApp.gravity = (double) new_val;
	                    if (GraphsApp.gravity<0.001) GraphsApp.gravity = 0.001;
	            		gravityLabel.setText(String.format("%.2f",GraphsApp.gravity));
	            }
	        });
	        cPanel.add(gravitySlider,0,9,2,1);
	        
	        // Damper slider
			cPanel.add(new Text("Damper: "),0,10); cPanel.add(damperLabel, 1, 10);
			Slider damperSlider = new Slider (0, 1, GraphsApp.damper);
			damperSlider.setShowTickMarks(true); damperSlider.setShowTickLabels(true);
			damperSlider.setMajorTickUnit(0.2);
	        damperSlider.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	                    GraphsApp.damper= (double) new_val;
	                    //if (damper<0.1) damper = 0.1;
	            		damperLabel.setText(String.format("%.2f",GraphsApp.damper));
	            }
	        });      
	        cPanel.add(damperSlider,0,11,2,1);
	        // Radius slider
	 		cPanel.add(new Text("Radius: "),0,12); cPanel.add(radiusLabel, 1, 12);
	 		Slider radiusSlider = new Slider (1, 9, GraphsApp.rad);
	 		radiusSlider.setShowTickMarks(true); radiusSlider.setShowTickLabels(true);
	 		radiusSlider.setMajorTickUnit(2);
	 		radiusSlider.setMinorTickCount(1);
	 		radiusSlider.setSnapToTicks(true);
	        radiusSlider.valueProperty().addListener(new ChangeListener<Number>() {
	             public void changed(ObservableValue<? extends Number> ov,
	                 Number old_val, Number new_val) {
	                    GraphsApp.rad = new_val.intValue();
	             		radiusLabel.setText(String.format("%d",GraphsApp.rad));
	             		for (GNode node: GraphsApp.vertices.values()) node.setRadius(GraphsApp.rad==1?GraphsApp.rad:GraphsApp.rad+node.weight/6);
	             }
	         });      
	         cPanel.add(radiusSlider,0,13,2,1);
	        
	         // ****** checkbox to show/hide edges drawing
	        CheckBox edgesCB = new CheckBox ("Show Edges");
	        edgesCB.setSelected(true);
	        edgesCB.setOnAction(new EventHandler<ActionEvent>()
	        {  @Override public void handle(ActionEvent event) {
				GraphsApp.showEdgesFlag = edgesCB.isSelected();
	        	GraphsApp.updateScreenEdges();
	            }
	        });
	        cPanel.add(edgesCB, 0, 14,2,1);
	        
	        // ****** checkbox to show/hide greedy nodes
	        CheckBox greedyCB = new CheckBox ("Show Greedy");
	        greedyCB.setSelected(false);
	        greedyCB.setOnAction(new EventHandler<ActionEvent>()
	        {  @Override public void handle(ActionEvent event) {
				if (greedyCB.isSelected()) GraphsApp.showGreedy();
					else 
		        		for(GNode currNode: GraphsApp.vertices.values()) {
		        			currNode.setFill(GraphsApp.NODE_COL);
		        			currNode.state=GNode.nodeState.ACTIVE;
		        		}
				};
	     
	        });
	        cPanel.add(greedyCB, 0, 15,2,1);
	        
	        // ****** button to open a graph
			Button openButton = new Button("Open graph...");
			openButton.setMaxHeight(Double.MAX_VALUE);
			openButton.setMaxWidth(Double.MAX_VALUE);
			openButton.setOnAction(new EventHandler<ActionEvent>()
	        {  @Override public void handle(ActionEvent event) {
	        	GraphsApp.openFile((Node) event.getSource());
	            }
	        });
			cPanel.add(openButton, 0, 17,2,1);
			// ****** button to start/stop calculations
			Button forcesButton = new Button("START forces calc");
			forcesButton.setMaxHeight(Double.MAX_VALUE);
			forcesButton.setMaxWidth(Double.MAX_VALUE);
			forcesButton.setOnAction(new EventHandler<ActionEvent>()
	        {  @Override public void handle(ActionEvent event) {
	        		if (!GraphsApp.fileOpened) return;
	        		if (!GraphsApp.calcFlag) {
	        			GraphsApp.timer.start();
	        			forcesButton.setText("STOP forces calc");
	        			openButton.setDisable(true);
	        			GraphsApp.calcFlag = true;
	        		} else
	        		{ 	GraphsApp.timer.stop();
	    				forcesButton.setText("START forces calc");
	           			openButton.setDisable(false);
	    				GraphsApp.calcFlag = false;
	        		}
	            }
	        });
			cPanel.add(forcesButton, 0, 18,2,1);
			
			//adding control panel pane to the screen root node
			ret.getChildren().add(cPanel);
			return ret;
		}
		
}
