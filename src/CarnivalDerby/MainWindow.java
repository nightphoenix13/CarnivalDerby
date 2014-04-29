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
				   gamePanel,
				   buttonPanel;
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
							 DELAY = 50;
	private static Random ranNum = new Random();
	private boolean won,
					done;
	private int[][] placing = new int[5][2];
	private String resultOutput;
	
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
		
		// Event handlers
		reset.addActionListener(new ResetHandler());
		exit.addActionListener(new ExitHandler());
		learnToPlay.addActionListener(new LearnToPlayHandler());
		about.addActionListener(new AboutHandler());
	} // buildHeaderPanel method end
	
	// buildIOPanel method builds the ioPanel
	private void buildIOPanel() // buildIOPanel method start
	{
		// creating components
		ioPanel = new JPanel();
		wagerPanel = new JPanel();
		totalPanel = new JPanel();
		buttonPanel = new JPanel();
		wager = new JTextField("0", 10);
		total = new JTextField("100", 10);
		wagerLabel = new JLabel("Wager amount: $");
		totalLabel = new JLabel("Your current total: $");
		start = new JButton("Start Race");
		lineUp = new JButton("Line Up");
		carBox = new JComboBox(comboBoxArray);
		
		
		// component properties
		ioPanel.setLayout(new GridLayout(1, 3, 0, 0));
		wagerPanel.setLayout(new GridLayout(1, 3));
		totalPanel.setLayout(new GridLayout(1, 2));
		buttonPanel.setLayout(new GridLayout(1, 2));
		total.setEditable(false);
		lineUp.setEnabled(false);
		start.setToolTipText("Enter a wager amount first.");
		carBox.setMaximumRowCount(6);
		
		// adding components to panel
		wagerPanel.add(wagerLabel);
		wagerPanel.add(wager);
		wagerPanel.add(carBox);
		buttonPanel.add(start);
		buttonPanel.add(lineUp);
		totalPanel.add(totalLabel);
		totalPanel.add(total);
		ioPanel.add(totalPanel);
		ioPanel.add(buttonPanel);
		ioPanel.add(wagerPanel);	
		
		// event handler
		start.addActionListener(new StartHandler());
		lineUp.addActionListener(new LineUpHandler());
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
			int increase = ranNum.nextInt(10);
			car.move(increase);
			repaint();
			if (car.getX() > WIN)
			{
				won = true;
				lineUp.setEnabled(true);
			} // end if
			Thread.sleep(DELAY);
		} // end while
		getPlacing();
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
	
	// resetCars method moves cars back to the start line and turns won value to false
	private void resetCars() // resetCars method start
	{
		for (int x = 0; x < cars.length; x++)
		{
			cars[x].setX(20);
		} // end for
		repaint();
		won = false;
		done = false;
	} // resetCars method end
	
	// getPlacing method gets the results of the race
	private void getPlacing() // getPlacing method start
	{
		for (int x = 0; x < cars.length; x++)
		{
			placing[x][0] = 0;
		} // end for
		for (int x = 0; x < cars.length; x++)
		{
			if (cars[x].getX() > placing[0][0])
			{
				placing[4][0] = placing[3][0];
				placing[4][1] = placing[3][1];
				placing[3][0] = placing[2][0];
				placing[3][1] = placing[2][1];
				placing[2][0] = placing[1][0];
				placing[2][1] = placing[1][1];
				placing[1][0] = placing[0][0];
				placing[1][1] = placing[0][1];
				placing[0][0] = cars[x].getX();
				placing[0][1] = x + 1;
			} // end if
			else if (cars[x].getX() > placing[1][0])
			{
				placing[4][0] = placing[3][0];
				placing[4][1] = placing[3][1];
				placing[3][0] = placing[2][0];
				placing[3][1] = placing[2][1];
				placing[2][0] = placing[1][0];
				placing[2][1] = placing[1][1];
				placing[1][0] = cars[x].getX();
				placing[1][1] = x + 1;
			} // end else if
			else if (cars[x].getX() > placing[2][0])
			{
				placing[4][0] = placing[3][0];
				placing[4][1] = placing[3][1];
				placing[3][0] = placing[2][0];
				placing[3][1] = placing[2][1];
				placing[2][0] = cars[x].getX();
				placing[2][1] = x + 1;
			} // end else if
			else if (cars[x].getX() > placing[3][0])
			{
				placing[4][0] = placing[3][0];
				placing[4][1] = placing[3][1];
				placing[3][0] = cars[x].getX();
				placing[3][1] = x + 1;
			} // end else if
			else
			{
				placing[4][0] = cars[x].getX();
				placing[4][1] = x + 1;
			} // end else
		} // end for
	} // getPlacing method end
	
	class StartHandler implements ActionListener // StartHandler class start
	{
		@Override
		public void actionPerformed(ActionEvent event) // actionPerformed method start
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
					total.setText(String.format("%d", Integer.parseInt(total.getText()) - Integer.parseInt(wager.getText())));
					start.setEnabled(false);
					won = false;
					final SwingWorker<Void, Object> car1Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[0]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					final SwingWorker<Void, Object> car2Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[1]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					final SwingWorker<Void, Object> car3Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[2]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					final SwingWorker<Void, Object> car4Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[3]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					final SwingWorker<Void, Object> car5Worker = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() throws Exception // doInBackground method start
						{
							race(cars[4]);
							return null;
						} // doInBackground method end
					}; // end anonymous inner class
					SwingWorker<Void, Object> waitUntilDone = new SwingWorker<Void, Object>()
					{ // start anonymous inner class
						@Override
						protected Void doInBackground() // doInBackground method start
						{
							while (!done)
							{
								if (car1Worker.isDone() && car2Worker.isDone() && car3Worker.isDone() &&
										car4Worker.isDone() && car5Worker.isDone())
								{
									done = true;
								} // end if
								try
								{
									Thread.sleep(DELAY);
								} // end try
								catch (InterruptedException e)
								{
									e.printStackTrace();
								} // end catch
							} // end while
							return null;
						} // doInBackground method end
						protected void done() // done method start
						{
							resultOutput = String.format("Placings:\n1st: Car %d\n2nd: Car %d\n3rd: Car %d\n\n",
									placing[0][1], placing[1][1], placing[2][1]);
							if (placing[0][1] == carBox.getSelectedIndex())
							{
								resultOutput += "You correctly wagered on the 1st place car!\nYou won 5 times your wager amount!";
								total.setText(String.format("%d", Integer.parseInt(total.getText()) + (Integer.parseInt(wager.getText()) * 5)));
							} // end if
							else if (placing[1][1] == carBox.getSelectedIndex())
							{
								resultOutput += "You correctly wagered on the 2nd place car!\nYou won 2 times your wager amount!";
								total.setText(String.format("%d", Integer.parseInt(total.getText()) + (Integer.parseInt(wager.getText()) * 2)));
							} // end else if
							else if (placing[2][1] == carBox.getSelectedIndex())
							{
								resultOutput += "You correctly wagered on the 3rd place car!\nYou won 1 times your wager amount!";
								total.setText(String.format("%d", Integer.parseInt(total.getText()) + Integer.parseInt(wager.getText())));
							} // end else if
							else
							{
								resultOutput += "Unfortunately, your car did not finish in the top 3.\nBetter luck next time.";
							} // end else
							JOptionPane.showMessageDialog(gamePanel, resultOutput, "Final Car Placing", JOptionPane.INFORMATION_MESSAGE);
						} // done method end
					}; // end anonymous inner class
					Executor executor = Executors.newCachedThreadPool();
					executor.execute(car1Worker);
					executor.execute(car2Worker);
					executor.execute(car3Worker);
					executor.execute(car4Worker);
					executor.execute(car5Worker);
					executor.execute(waitUntilDone);
				} // end else if
			} // end try
			catch (NumberFormatException exception)
			{
				JOptionPane.showMessageDialog(gamePanel, "You can only enter an integer value.",
						"Invalid Wager Format", JOptionPane.ERROR_MESSAGE);
			} // end catch
		} // actionPerformed method end
	} // StartHandler class end
	
	class LineUpHandler implements ActionListener // LineUpHandler class start
	{
		@Override
		public void actionPerformed(ActionEvent event) // actionPerformed method start
		{
			if (Integer.parseInt(total.getText()) == 0)
			{
				JOptionPane.showMessageDialog(gamePanel, "You have no more money to wager. Please start a new game through the menu.",
						"No More Funds", JOptionPane.ERROR_MESSAGE);
			} // end if
			else
			{
				resetCars();
				lineUp.setEnabled(false);
				start.setEnabled(true);
			} // end else
		} // actionPerformed method end
	} // LineUpHandler class end
	
	class ResetHandler implements ActionListener // ResetHandler class start
	{
		@Override
		public void actionPerformed(ActionEvent event) // actionPerformed method start
		{
			resetCars();
			lineUp.setEnabled(false);
			start.setEnabled(true);
			total.setText("100");
		} // actionPerformed method end
	} // ResetHandler class end
	
	class ExitHandler implements ActionListener // ExitHandler class start
	{
		@Override
		public void actionPerformed(ActionEvent event) // actionPerformed method start
		{
			System.exit(0);
		} // actionPerformed method end
	} // ExitHandler class end
	
	class LearnToPlayHandler implements ActionListener // LearnToPlayHandler class start
	{
		@Override
		public void actionPerformed(ActionEvent event) // actionPerformed method start
		{
			JOptionPane.showMessageDialog(gamePanel, "You play by placing a wager on one of the cars.\nPayouts are as follows:\n\n1st Place: 1:5\n2nd Place: 1:2\n3rd Place: 1:1\n\nIf you run out of money, reset the game from the menu.",
					"Learn To Play", JOptionPane.INFORMATION_MESSAGE);
		} // actionPerformed method end
	} // LearnToPlayHandler class end
	
	class AboutHandler implements ActionListener // AboutHandler class start
	{
		@Override
		public void actionPerformed(ActionEvent event) // actionPerformed method start
		{
			JOptionPane.showMessageDialog(gamePanel, "This program was written by Zack Rosales.\nThe car images were taken from http://www.mygrafico.com/build-your-own/build-your-race-car/prod_5483.html.",
					"About This Program", JOptionPane.INFORMATION_MESSAGE);
		} // actionPerformed method end
	} // AboutHandler class end
} // MainWindow class end
