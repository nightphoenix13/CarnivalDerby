// Written by Zack Rosales
// Images taken from http://www.mygrafico.com/build-your-own/build-your-race-car/prod_5483.html
// This program uses multithreading to make 5 cars race. The movement speed is random, so any car can
// win the race.

package CarnivalDerby;

import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame // MainWindow class start
{
	// class components & variables
	private JPanel headerPanel,
				   ioPanel,
				   wagerPanel,
				   totalPanel;
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
				   totalLabel;
	
	public MainWindow() // constructor start
	{
		// building panels
		setLayout(new BorderLayout());
		buildHeaderPanel();
		buildIOPanel();
		
		// adding panels to frame
		add(headerPanel, BorderLayout.NORTH);
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
} // MainWindow class end
