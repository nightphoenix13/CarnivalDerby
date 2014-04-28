// Written by Zack Rosales
// Images taken from http://www.mygrafico.com/build-your-own/build-your-race-car/prod_5483.html
// This program uses multithreading to make 5 cars race. The movement speed is random, so any car can
// win the race.

package CarnivalDerby;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
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
	private JButton start,
					lineUp;
	private JTextField wager,
					   total;
	private JLabel wagerLabel,
				   totalLabel,
				   finishLineLabel;
	private JComboBox carBox;
	private Car[] cars = new Car[5];
	private BufferedImage finishLine;
	private static final String[] comboBoxArray = {"Select a car", "Car 1", "Car 2", "Car 3", "Car 4", "Car 5"};
	private static final int WIN = 750,
							 DELAY = 25;
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
			cars[0] = new Car("Car1.jpg", 120);
			cars[1] = new Car("Car2.jpg", 240);
			cars[2] = new Car("Car3.jpg", 360);
			cars[3] = new Car("Car4.jpg", 480);
			cars[4] = new Car("Car5.jpg", 600);
			finishLine = ImageIO.read(new File("FinishLine.jpg"));
		} // end try
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(gamePanel, "Error opening image file(s).", "File Error",
					JOptionPane.ERROR_MESSAGE);
		} // end catch
		finishLineLabel = new JLabel(new ImageIcon(finishLine));
		
		// component properties
		gamePanel.setBackground(Color.WHITE);
		gamePanel.setLayout(null);
		finishLineLabel.setBounds(900, 20, 40, 600);
		
		// adding components to panel
		gamePanel.add(finishLineLabel);
	} // buildGamePanel method end
	
	// race method makes the cars move at random intervals
	private void race(Car car) throws InterruptedException // race method start
	{
		while (!won)
		{
			int increase = ranNum.nextInt(5);
			car.move(increase);
			repaint();
			if (car.getX() > WIN)
			{
				won = true;
			} // end if
			Thread.sleep(DELAY);
		} // end while
	} // race method end
	
	public void paint(Graphics g) // paint method start
	{
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		for (int x = 0; x < cars.length; x++)
		{
			g.drawImage(cars[x].getImage(), cars[x].getX(), cars[x].getY(), gamePanel);
		} // end for
	} // paintComponent method end
	
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
					SwingWorker<Void, Object> car1Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[0]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					SwingWorker<Void, Object> car2Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[1]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					SwingWorker<Void, Object> car3Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[2]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					SwingWorker<Void, Object> car4Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[3]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					SwingWorker<Void, Object> car5Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[4]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					Executor executor = Executors.newCachedThreadPool();
					executor.execute(car1Worker);
					executor.execute(car2Worker);
					executor.execute(car3Worker);
					executor.execute(car4Worker);
					executor.execute(car5Worker);
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
