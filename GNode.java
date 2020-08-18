package com.leerdev.graphs.graphVis;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** 
 * 'Graphic' Node class
 * JavaFX Circle with graph node properties
 *  @author Artem Leer
 */
public final class GNode extends Circle {
	private int name;
	protected boolean isDragged;
	private Coord force = new Coord();
	private Coord velocity = new Coord();
	protected enum nodeState {ACTIVE, 	//normal node
							MARKED, 	//marked node
							DISABLED};	//'switched off' node
	protected int weight;
	protected nodeState state;
	
	//Constructor
	public GNode (int name, double x, double y, double r, Color color) {
		super (x, y, r, color);
		this.setStroke(Color.BLACK);
		this.name=name;
		this.weight=0;
		this.state=nodeState.ACTIVE;
	}
	
	//Calculating repulsion based on Pythagorean theorem and repulsion ratio
	public void calcRepulsion (GNode otherNode, Double repulsion) {
		double squareDist = Math.pow(getX()-otherNode.getX(),2) + Math.pow(getY()-otherNode.getY(),2);
        force.x += repulsion * (getX()-otherNode.getX()) / (squareDist+0.001);
        force.y += repulsion * (getY()-otherNode.getY()) / (squareDist+0.001);
	}
	//calculating attraction based on gravity constant
	public void calcAttraction (GNode otherNode, Double gravity) {
        force.x += gravity*(otherNode.getX() - getX());
        force.y += gravity*(otherNode.getY() - getY());
	}
	//calculating velocity using damper
	public void calcVelocity (double damper) {
	    velocity.x = (velocity.x + force.x)*(1-damper); 
	    velocity.y = (velocity.y + force.y)*(1-damper); 	
	}
	//setting up new positions
	public void updatePos () {
		setX(getX()+velocity.x); setY(getY()+velocity.y);
	}
	
	//getters and setters
	public int getName() { return name;}
	public double getX() { return getCenterX();}
	public double getY() { return getCenterY();}
	public void setX(double x) { setCenterX(x);}
	public void setY(double y) { setCenterY(y);}
	
	public void resetForces() {
		force.x=force.y=0;
	}
}

//helper class for x_y coordinates
class Coord {
	double x;
	double y;
	Coord() {
		x=0.0;
		y=0.0;
	}
}