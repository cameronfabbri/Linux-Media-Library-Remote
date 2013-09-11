package org.oscar.cryptobionic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.net.*;
import javax.imageio.*;

public class RemoteGUI extends JPanel
                          implements ListSelectionListener {
    private JLabel picture;
    private JList list;
	boolean power = false;
	boolean playing = true;
    private JSplitPane splitPane;
	private JPanel movieInfoPane;
	private JButton playButton;
	private BufferedImage moviePoster;
	//Global swing elements that needs to be edited from multiple functions
	private JList fileList;
	private JPanel filelistpane;
	private JPanel infopane;
	//--------------------------
	
	String name;
	
    Play p_caller = new Play();
        
    String movielist = p_caller.getMovies();
	String filelist;//the list of files in a movie directory
    String[] movieNames = movielist.split("\\r?\\n");

	protected JPanel createPlayingPane()
	{
		JPanel playingPanel = new JPanel();
		playingPanel.setLayout(new BoxLayout(playingPanel, BoxLayout.Y_AXIS));
		JPanel movieButtonsPanel = new JPanel();
		movieButtonsPanel.setLayout(new BoxLayout(movieButtonsPanel, BoxLayout.X_AXIS));		
		
		String info_ = p_caller.listFiles(name);
		String[] infoArray = info_.split("\\r?\\n");
		String info = infoArray[0]; 
		
		
		JLabel title = new JLabel("Now Playing");
		//title.setFont(new Font("Comic Sans", Font.PLAIN, 30));
		playingPanel.add(title);		
		
		try {
		
			BufferedImage reverseIcon = ImageIO.read(new File("img\\rewind.png"));
			JButton reverseButton = new JButton(new ImageIcon(reverseIcon));
			movieButtonsPanel.add(reverseButton);
			
			BufferedImage playbuttonIcon = ImageIO.read(new File("img\\playbutton.png"));
			JButton playpauseButton = new JButton(new ImageIcon(playbuttonIcon));
			movieButtonsPanel.add(playpauseButton);
			
			BufferedImage stopbuttonIcon = ImageIO.read(new File("img\\stop.png"));
			JButton stopButton = new JButton(new ImageIcon(stopbuttonIcon));
			movieButtonsPanel.add(stopButton);

			BufferedImage volumeDownIcon = ImageIO.read(new File("img\\minus.png"));
			JButton volumeDownButton = new JButton(new ImageIcon(volumeDownIcon));
			movieButtonsPanel.add(volumeDownButton);
			
			BufferedImage volumeUpIcon = ImageIO.read(new File("img\\plus.png"));
			JButton volumeUpButton = new JButton(new ImageIcon(volumeUpIcon));
			movieButtonsPanel.add(volumeUpButton);
	
			BufferedImage forwardIcon = ImageIO.read(new File("img\\forward.png"));
			JButton forwardButton = new JButton(new ImageIcon(forwardIcon));
			movieButtonsPanel.add(forwardButton);
			

			
			forwardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				p_caller.forward();
			}
			});

			reverseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				p_caller.reverse();
			}
			});

			volumeDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				p_caller.volumeDown();
			}
			});
			
			volumeUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				p_caller.volumeUp();
			}
			});

			
			playpauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				p_caller.pause();
			}
			});
			
			stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				p_caller.killVLC();
				setRightSide(filelistpane);
			}
			});
			
		} catch (Exception e) { System.out.println(e); } 
		
		playingPanel.add(movieButtonsPanel);
		

		
		return playingPanel;
	}
	
	protected JPanel createInfoPane()
	{
		try {
		
			JPanel movieInfoPanel = new JPanel();
			movieInfoPanel.setLayout(new BoxLayout(movieInfoPanel, BoxLayout.X_AXIS));
			JPanel movieOverallPanel = new JPanel();
			movieOverallPanel.setLayout(new BoxLayout(movieOverallPanel, BoxLayout.Y_AXIS));
			JPanel actionButtonsPanel = new JPanel();
			actionButtonsPanel.setLayout(new BoxLayout(actionButtonsPanel, BoxLayout.X_AXIS));
			JPanel moviePicture = new JPanel();
			moviePicture.setLayout(new BoxLayout(moviePicture, BoxLayout.X_AXIS));
			
			BufferedImage playButtonIcon = ImageIO.read(new File("img\\play.png"));
			JButton playButton = new JButton(new ImageIcon(playButtonIcon));
			playButton.setPreferredSize(new Dimension(60, 60));
			
			playButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					String moviePlay = name + "/" + fileList.getSelectedValue().toString();
					setRightSide(createPlayingPane());
					p_caller.playMovie(moviePlay);
				}
			});
			
			BufferedImage backButtonIcon = ImageIO.read(new File("img\\left.png"));
			JButton backButton = new JButton(new ImageIcon(backButtonIcon));
			backButton.setPreferredSize(new Dimension(60, 60));
			
			backButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					setRightSide(filelistpane);
				}
			});

			actionButtonsPanel.add(backButton);
			actionButtonsPanel.add(playButton);
			
			String info_ = p_caller.listFiles(name);
			String[] infoArray = info_.split("\\r?\\n");
			String info = infoArray[0]; 
			
			String movieInfo = p_caller.movieInfo(name, info);

			JTextArea infoTextArea = new JTextArea(movieInfo);
			infoTextArea.setEditable(false);
			infoTextArea.setLineWrap(true);
			infoTextArea.setWrapStyleWord(true);
			JScrollPane infoScrollArea = new JScrollPane(infoTextArea);
			
			//movieInfoPanel.add(infoScrollArea);
			
			//JLabel moviePoster = new JLabel(new ImageIcon(p_caller.getPicture(name, info)));
			ImageIcon posterIcon=null;
			BufferedImage moviePoster = null;
			
			try{
				moviePoster = p_caller.getPicture(name, info);
				posterIcon = new ImageIcon(moviePoster);
			} catch (Exception e){
				System.out.println(e + " Does the image file exist?");
			}
			JLabel posterlabel = new JLabel("", posterIcon, JLabel.CENTER);
			JScrollPane posterScrollArea = new JScrollPane(posterlabel);
			
			JSplitPane infoSplitPane = null;
			if(posterIcon != null){
				//debugging output
				System.out.println("Also got this far");
				System.out.println(posterIcon.toString());
				
				infoSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, posterScrollArea, infoScrollArea);
				infoSplitPane.setAlignmentX(Component.CENTER_ALIGNMENT);
				infoSplitPane.setDividerLocation(posterIcon.getIconWidth());
				movieOverallPanel.add(infoSplitPane);
			}
			movieOverallPanel.add(actionButtonsPanel);
			
			return movieOverallPanel;
		} catch (Exception e) {
			JPanel dummy = new JPanel();
			System.err.println("Error: " + e);
			return dummy;
		}
	}
		
	
	protected void setRightSide(Component pane) {
		int splitlocation = splitPane.getDividerLocation();
		splitPane.setRightComponent(pane);
		splitPane.setDividerLocation(splitlocation);
	}
	
	//Creates the initial right side of the split pane used, the ones for selecting the file to be viewed.
	protected JPanel fileListPane() {
			
			JPanel powerButtons = new JPanel();
			
			try {
				BufferedImage powerButtonIcon = ImageIO.read(new File("img\\power.png"));
				JButton powerButton = new JButton(new ImageIcon(powerButtonIcon));
				powerButton.setPreferredSize(new Dimension(60, 60));
				
				BufferedImage selectButtonIcon = ImageIO.read(new File("img\\right.png"));
				JButton selectButton = new JButton(new ImageIcon(selectButtonIcon));
				selectButton.setPreferredSize(new Dimension(60, 60));

				
				//JButton button = new JButton("Select");

				powerButton.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e)
					{
						if (power == false)
						{
							p_caller.powerOn();
							power = true;
						}
						else 
						{
							p_caller.powerOff();
							power = false;
						}
					}
				});	
											
				selectButton.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e)
					{
						System.out.println(name + "/" + fileList.getSelectedValue().toString());
						setRightSide(createInfoPane());
					}
				});  
				
				JPanel buttons = new JPanel();
				buttons.setMaximumSize(new Dimension(1000, 100)); //sets the button panel size to allow the files list to take up the most room
				buttons.add(powerButton);
				buttons.add(selectButton);
				filelist = "";//p_caller.listFiles(name);    This is the globally defined file list, because it is updated with a separate function
				String[] temp = filelist.split("\\r?\\n");
				fileList = new JList(temp);
				JScrollPane fileScroll = new JScrollPane(fileList);
				
				// rightSide is the entire right side of the split pane.
				JPanel rightSide = new JPanel();
				rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
				rightSide.add(fileScroll);
				rightSide.add(buttons);
				rightSide.add(Box.createHorizontalGlue());
				
				filelistpane = rightSide;
				return rightSide;
				
				} catch (Exception e) { 
					System.err.println("Error: " + e); 
					JPanel dummy = new JPanel();
					return dummy;
				}
		}
		
	//creates the list of movies displayed on the left of the split pane	
	protected JScrollPane movieListPane() {
		// Create the list and put it in a scroll pane.
        list = new JList(movieNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
		
		//the listener for changed selections
        list.addListSelectionListener(this);
		
		// This declares the part on the left.
		JScrollPane listScrollPane = new JScrollPane(list);
		
		return listScrollPane;
	}
	
    public RemoteGUI() {

        /*picture = new JLabel();
        picture.setFont(picture.getFont().deriveFont(Font.ITALIC));
        picture.setHorizontalAlignment(JLabel.CENTER);*/
        

        //Create a split pane with the two scroll panes in it.
        //splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, rightSide);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, movieListPane(), fileListPane());
        splitPane.setOneTouchExpandable(true);
        //splitPane.setDividerLocation(150);
		splitPane.setDividerLocation(200);
		
        //Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        //listScrollPane.setMinimumSize(minimumSize);
        //fileScroll.setMinimumSize(minimumSize);

        //Provide a preferred size for the split pane.
        splitPane.setPreferredSize(new Dimension(400, 200));
        //updateLabel(movieNames[list.getSelectedIndex()]);
    }
    
    //Listens to the list
    public void valueChanged(ListSelectionEvent e) {
		//System.out.println("Listener e: " + e.getValueIsAdjusting());
		if (!e.getValueIsAdjusting())
		{
			JList list = (JList)e.getSource();
			updateLabel(movieNames[list.getSelectedIndex()]);
			// Actually get's the files in the dir
			filelist = p_caller.listFiles(name);
			updateList(filelist, fileList);
		}
    }
    
	protected void updateList(String moviename, JList pane)
	{
		String[] temp = moviename.split("\\r?\\n");
	    pane.setListData(temp);
		pane.setSelectedIndex(0);
	}
	
    /*//Renders the selected image
    protected void updateLabel (String name_) {
        String info = p_caller.listDir(name_); 
        System.out.println("Info: " + info);
        ImageIcon icon = createImageIcon(name_);
		name=name_;
        picture.setIcon(icon);
        if  (icon != null) {
            picture.setText("Nothing in this movie folder...");
        } else {
                picture.setText(info);
		  }
    }*/
	
	protected void updateLabel (String name_) {
		String info = p_caller.listDir(name_); 
        //System.out.println("Info: " + info);
		name=name_;
	}

	
    //Used by SplitPaneDemo2
    /*public JList getImageList() {
        return list;
    }*/

    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
       java.net.URL imgURL = RemoteGUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            return null;
        }
    }
	
	public JSplitPane getSplitPane() {
        return splitPane;
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {

		//set look and feel per system.
		try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch(Exception e){}


        //Create and set up the window.
        JFrame frame = new JFrame("Linux Media Library Remote");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RemoteGUI remoteGUI = new RemoteGUI();
        frame.getContentPane().add(remoteGUI.getSplitPane());
		
		ImageIcon img = new ImageIcon("img/CAMNEWICON.png");
		frame.setIconImage(img.getImage());
	
        //Display the window.
		frame.setSize(600,500);
        //frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
           }
        });
    }
      
}
