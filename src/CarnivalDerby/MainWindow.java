// Written by Zack Rosales
// Images taken from http://www.mygrafico.com/build-your-own/build-your-race-car/prod_5483.html
// This program uses multithreading to make 5 cars race. The movement speed is random, so any car can
// win the race.

package CarnivalDerby;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

import javax.imageio.*;
import javax.swing.*;

public class MainWindow extends JFrame // MainWindow class start
{
	// class components & variables
	private JPanel headerPanel,
				   ioPanel,
				   wagerPanel,
				   totalPanel,
				   gamePanel;
	private JMenu game,
				  help;
	private JMenuItem reset,
					  exit,
					  learnToPlay,
					  about;
	private JMenuBar bar;
	private JButton start;
	private JTextField wager,
					   total;
	private JLabel wagerLabel,
				   totalLabel,
				   car1Label,
				   car2Label,
				   car3Label,
				   car4Label,
				   car5Label,
				   finishLineLabel;
	private JComboBox carBox;
	private BufferedImage car1,
						  car2,
						  car3,
						  car4,
						  car5,
						  finishLine;
	private static final String[] comboBoxArray = {"Select a car", "Car 1", "Car 2", "Car 3", "Car 4", "Car 5"};
	private int car1x,
				car2x,
				car3x,
				car4x,
				car5x;
	private static final int WIN = 750,
							 DELAY = 3;
	private static Random ranNum = new Random();
	private boolean won;
	
	public MainWindow() // constructor start
	{
		// building panels
		setLayout(new BorderLayout());
		buildHeaderPanel();
		buildIOPanel();
		buildGamePanel();
		
		// adding panels to frame
		add(headerPanel, BorderLayout.NORTH);
		add(gamePanel, BorderLayout.CENTER);
		add(ioPanel, BorderLayout.SOUTH);
	} // constructor end
	
	public static void main(String[] args) // main method start
	{
		MainWindow mw = new MainWindow();
		mw.setTitle("Carnival Derby");
		mw.setSize(1000, 750);
		mw.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mw.setLocationRelativeTo(null);
		mw.setVisible(true);	
	} // main method end
	
	// buildHeaderPanel method builds the headerPanel
	private void buildHeaderPanel() // buildHeaderPanel method start
	{
		// creating components
		headerPanel = new JPanel();
		game = new JMenu("Game");
		help = new JMenu("Help");
		reset = new JMenuItem("Reset");
		exit = new JMenuItem("Exit");
		learnToPlay = new JMenuItem("Learn to Play");
		about = new JMenuItem("About");
		bar = new JMenuBar();
		
		// component properties
		game.setMnemonic('G');
		help.setMnemonic('H');
		reset.setMnemonic('R');
		exit.setMnemonic('x');
		learnToPlay.setMnemonic('L');
		about.setMnemonic('A');
		
		// adding components to panel
		game.add(reset);
		game.add(exit);
		help.add(learnToPlay);
		help.add(about);
		bar.add(game);
		bar.add(help);
		headerPanel.add(bar);
		setJMenuBar(bar);
	} // buildHeaderPanel method end
	
	// buildIOPanel method builds the ioPanel
	private void buildIOPanel() // buildIOPanel method start
	{
		// creating components
		ioPanel = new JPanel();
		wagerPanel = new JPanel();
		totalPanel = new JPanel();
		wager = new JTextField("0", 10);
		total = new JTextField("100", 10);
		wagerLabel = new JLabel("Wager amount: $");
		totalLabel = new JLabel("Your current total: $");
		start = new JButton("Start Race");
		carBox = new JComboBox(comboBoxArray);
		
		
		// component properties
		ioPanel.setLayout(new GridLayout(1, 3, 0, 0));
		wagerPanel.setLayout(new GridLayout(1, 3));
		totalPanel.setLayout(new GridLayout(1, 2));
		total.setEditable(false);
		start.setToolTipText("Enter a wager amount first.");
		carBox.setMaximumRowCount(6);
		
		// adding components to panel
		wagerPanel.add(wagerLabel);
		wagerPanel.add(wager);
		wagerPanel.add(carBox);
		totalPanel.add(totalLabel);
		totalPanel.add(total);
		ioPanel.add(totalPanel);
		ioPanel.add(start);
		ioPanel.add(wagerPanel);	
		
		// event handler
		start.addActionListener(new StartHandler());
	} // buildIOPanel method end
	
	// buildGamePanel method builds the gamePanel
	private void buildGamePanel() // buildGamePanel method start
	{
		// creating components
		gamePanel = new JPanel();
		try
		{
			car1 = ImageIO.read(new File("Car1.jpg"));
			car2 = ImageIO.read(new File("Car2.jpg"));
			car3 = ImageIO.read(new File("Car3.jpg"));
			car4 = ImageIO.read(new File("Car4.jpg"));
			car5 = ImageIO.read(new File("Car5.jpg"));
			finishLine = ImageIO.read(new File("FinishLine.jpg"));
		} // end try
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(gamePanel, "Error opening image file(s).", "File Error",
					JOptionPane.ERROR_MESSAGE);
		} // end catch
		car1Label = new JLabel(new ImageIcon(car1));
		car2Label = new JLabel(new ImageIcon(car2));
		car3Label = new JLabel(new ImageIcon(car3));
		car4Label = new JLabel(new ImageIcon(car4));
		car5Label = new JLabel(new ImageIcon(car5));
		finishLineLabel = new JLabel(new ImageIcon(finishLine));
		car1x = car2x = car3x = car4x = car5x = 20;
		
		// component properties
		gamePanel.setBackground(Color.WHITE);
		gamePanel.setLayout(null);
		car1Label.setBounds(car1x, 20, 150, 75);
		car2Label.setBounds(car2x, 140, 150, 75);
		car3Label.setBounds(car3x, 260, 150, 75);
		car4Label.setBounds(car4x, 380, 150, 75);
		car5Label.setBounds(car5x, 500, 150, 75);
		finishLineLabel.setBounds(900, 20, 40, 600);
		
		// adding components to panel
		gamePanel.add(car1Label);
		gamePanel.add(car2Label);
		gamePanel.add(car3Label);
		gamePanel.add(car4Label);
		gamePanel.add(car5Label);
		gamePanel.add(finishLineLabel);
	} // buildGamePanel method end
	
	// race method makes the cars move at random intervals
	private void race() throws InterruptedException // race method start
	{
		int increase = ranNum.nextInt(5);
		car1x += increase;
		car1Label.setBounds(car1x, 20, 150, 75);
		if (car1x > WIN)
		{
			won = true;
		} // end if
	} // race method end
	
	class StartHandler implements ActionListener // StartHandler class start
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				if (Integer.parseInt(wager.getText()) < 1)
				{
					JOptionPane.showMessageDialog(gamePanel, "You must enter a valid amount to wager.",
							"Invalid Wager Amount", JOptionPane.ERROR_MESSAGE);
				} // end if
				else if (Integer.parseInt(wager.getText()) > Integer.parseInt(total.getText()))
				{
					JOptionPane.showMessageDialog(gamePanel, "You cannot wager more money than you have.",
							"Invalid Wager Amount", JOptionPane.ERROR_MESSAGE);
				} // end else if
				else if (carBox.getSelectedIndex() == 0)
				{
					JOptionPane.showMessageDialog(gamePanel, "You must choose a car to wager on.",
							"No Car Chosen", JOptionPane.ERROR_MESSAGE);
				} // end else if
				else
				{
					start.setEnabled(false);
					won = false;
					while (!won)
					{
						try
						{
							race();
							Thread.sleep(DELAY);
						} // end try
						catch (InterruptedException e)
						{
							e.printStackTrace();
						} // end catch
					}
					start.setEnabled(true);
				} // end else if
			} // end try
			catch (NumberFormatException exception)
			{
				JOptionPane.showMessageDialog(gamePanel, "You can only enter an integer value.",
						"Invalid Wager Format", JOptionPane.ERROR_MESSAGE);
			} // end catch
		}
	} // StartHandler class end
} // MainWindow class end
