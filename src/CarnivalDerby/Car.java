package CarnivalDerby;

import java.awt.image.*;
import javax.swing.*;

public class Car extends ImageIcon
{
	private int x,
				y;
	public final static int WIDTH = 150,
							 HEIGHT = 75;
	private String file;
	
	public Car(String i, int yStart)
	{
		super(i);
		setclass(yStart);
	} // constructor end
	
	private void setclass(int yStart) // class set method start
	{
		setY(yStart);
		setX(20);
	} // class set method end
	
	public void setY(int yStart) // y set method start
	{
		y = yStart;
	} // y set method end
	
	public void setX(int xStart) // x set method start
	{
		x = xStart;
	} // x set method end
	
	public int getY() // y get method start
	{
		return y;
	} // y get method end
	
	public int getX() // x get method start
	{
		return x;
	} // x get method end
	
	public void move(int num) // increaseY method start
	{
		x += num;
	} // increaseY method end
} // Car class end