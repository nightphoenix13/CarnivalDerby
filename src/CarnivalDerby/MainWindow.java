// Written by Zack Rosales
// Images taken from http://www.mygrafico.com/build-your-own/build-your-race-car/prod_5483.html
// This program uses multithreading to make 5 cars race. The movement speed is random, so any car can
// win the race.

package CarnivalDerby;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

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
	private BufferedImage car1,
						  car2,
						  car3,
						  car4,
						  car5,
						  finishLine;
	
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
		wagerLabel = new JLabel("Amount to wager: $");
		totalLabel = new JLabel("Your current total: $");
		start = new JButton("Start Race");
		
		// component properties
		ioPanel.setLayout(new GridLayout(1, 3, 50, 0));
		wagerPanel.setLayout(new GridLayout(1, 2));
		totalPanel.setLayout(new GridLayout(1, 2));
		start.setEnabled(false);
		total.setEditable(false);
		start.setToolTipText("Enter a wager amount first.");
		
		// adding components to panel
		wagerPanel.add(wagerLabel);
		wagerPanel.add(wager);
		totalPanel.add(totalLabel);
		totalPanel.add(total);
		ioPanel.add(totalPanel);
		ioPanel.add(start);
		ioPanel.add(wagerPanel);		
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
		
		// component properties
		gamePanel.setBackground(Color.WHITE);
		gamePanel.setLayout(null);
		car1Label.setBounds(20, 20, 150, 75);
		car2Label.setBounds(20, 140, 150, 75);
		car3Label.setBounds(20, 260, 150, 75);
		car4Label.setBounds(20, 380, 150, 75);
		car5Label.setBounds(20, 500, 150, 75);
		finishLineLabel.setBounds(900, 20, 40, 600);
		
		// adding components to panel
		gamePanel.add(car1Label);
		gamePanel.add(car2Label);
		gamePanel.add(car3Label);
		gamePanel.add(car4Label);
		gamePanel.add(car5Label);
		gamePanel.add(finishLineLabel);
	} // buildGamePanel method end
} // MainWindow class end
