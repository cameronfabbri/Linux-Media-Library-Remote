package org.oscar.cryptobionic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class RemoteGUI extends JPanel
                          implements ListSelectionListener {
    private JLabel picture;
    private JList list;
    private JSplitPane splitPane;
	private JPanel movieInfoPane;
	private JButton playButton;
	
	//Global swing elements that needs to be edited from multiple functions
	private JList fileList;
	//--------------------------
	
	String name;
	
    Play p_caller = new Play();
        
    String movielist = p_caller.getMovies();
	String filelist;//the list of files in a movie directory
    String[] movieNames = movielist.split("\\r?\\n");

	protected JPanel createInfoPane()
	{
		JPanel movieInfoPanel = new JPanel();
		//String title = "<html><B><center>Movie Info</center></B><br><br></html>";
		String title = "Title";
		
		// Check if there is actually info for the movie
		//p_caller.checkMovieInfo(name);
		
		String info_ = p_caller.listFiles(name);
		String[] infoArray = info_.split("\\r?\\n");
		String info = infoArray[0]; 
		
		String movieInfo = p_caller.movieInfo(info);
		String[] movieInfoArray = movieInfo.split("\\r?\\n");
		int len = movieInfoArray.length;
		
		//p_caller.checkMovieInfo(info);
		
		JLabel infoTitle = new JLabel(title);
		
		movieInfoPanel.add(infoTitle);
		
		for (int i = 0; i < len; i++)
		{
			movieInfo = movieInfoArray[i];
			JLabel infoLabel = new JLabel(movieInfo);	
			movieInfoPanel.add(infoLabel);
			movieInfoPanel.setPreferredSize(new Dimension(350, 190));
		}
		
		
		return movieInfoPanel;
	}
		
	//Creates the initial right side of the split pane used, the ones for selecting the file to be viewed.
	protected JPanel fileListPane() {
			JButton button = new JButton("Select");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					//Execute when button is pressed
					//System.out.println("Movie " + name + " is being played");
					//p_caller.playMovie(name + "/" + fileList.getSelectedValue().toString());
					//System.out.println(name + "/" + fileList.getSelectedValue().toString());'
					splitPane.remove(1);
					splitPane.add(createInfoPane(), 1);
				}
			});  
			
			JPanel buttons = new JPanel();
			buttons.setMaximumSize(new Dimension(150, 50)); //sets the button panel size to allow the files list to take up the most room
			buttons.add(button);
			filelist = "";//p_caller.listFiles(name);    This is the globally defined file list, because it is updated with a separate function
			String[] temp = filelist.split("\\r?\\n");
			fileList = new JList(temp);
			JScrollPane fileScroll = new JScrollPane(fileList);
			
			// rightSide is the entire right side of the split pane.
			JPanel rightSide = new JPanel();
			rightSide.add(fileScroll);
			rightSide.add(buttons);
			rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
			rightSide.add(Box.createHorizontalGlue());
			
			return rightSide;
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
		System.out.println("Listener e: " + e.getValueIsAdjusting());
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
        System.out.println("Info: " + info);
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
            //System.err.println("Couldn't find file: " + path);
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
