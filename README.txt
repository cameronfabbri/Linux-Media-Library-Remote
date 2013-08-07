                         Linux Media Library Remote

What is it?
-----------

The Java Media Server Remote allows you to connect to a server Running any form
of Linux.
...
...



Documentation
-------------



Installation
------------
Server-Side:
        Bash script to install vlc, get the jsch library, create a user, vlc-bin,
        input directory for their Movie Library, etc
        Or just instructions on how to do all of that.

Client-Side:
        Just the JAR
	    			      


Scripts
-------


+--------+    +------------+
|list.sh | -> | movies.txt |
+--------+----+------------+------------------------------------------------+
| Creates a list of all the movies in your Movies root folder and puts them |
| into list.txt.  This is used by Play.java to populate the scroll pane on  |
| the left sido of the GUI                                                  |
+---------------------------------------------------------------------------+


+--------------+
| listFiles.sh |
+--------------+-------------------------------------------------------------------+
| Takes in a movie name as a parameter (the folder name containing that movie)     |
| It's a good idea to name all of your movies correctly (the folder names at least)|
| This script is used when clicking on a movie on the left scroll bar to display   |
| the contents of the folder.                                                      |
+----------------------------------------------------------------------------------+  


+--------------+
| movieInfo.sh |
+--------------+----------------------------------------------------------------------+
| Displays the info file about the movie which is grabbed by imdb-lookup/findMovie.sh |
| If there is no movie information available, you have the option to download it.     |
| This display is also captured by the GUI to display it there.                       |
+-------------------------------------------------------------------------------------+  


+---------+
| play.sh |
+---------+-----------------------------------------------------------------------------+
| Takes in the movie folder and the movie name as a single parameter. The GUI grabs the |
| folder name from the scroll list on the left, and the file name from the scrool list  |
| on the right.  These are both passed to the script.  If a movie is playing and the    |
| script is called again, vlc is killed and the second movie is then played.            |
+---------------------------------------------------------------------------------------+

+----------------+
| randomMovie.sh |
+----------------+-----------------------------------------------------------------------+
| Picks a random movie folder in your Movies root directory, then in case that movie has |
| more than one movie file in it (for collections) or another directory, picks randomly  |
| from that.                                                                             | 
+----------------------------------------------------------------------------------------+


+-------------+
| randomTV.sh |
+-------------+-------------------------------------------------------------------------+
| Script that plays a random TV show by picking a random TV show, then a random season, |
| and then finally a random episode.  This script is based highly off of your directory |
| structure, so edit it and make sure it works for you.  It works correctly for:        |
|                                                                                       |
| /path/to/TVROOT/Show-name/Season-Number/Episode-Name.ext     and also:                |
|                                                                                       |
| /path/to/TVROOT/Show-name/Volume-Number/Disc-Number/Episode-Name.ext                  |
+---------------------------------------------------------------------------------------+


+-----------+
| select.sh |
+-----------+----------------------------------------------------+
| Grabs all of the files in the directory of the selected movie. |
+----------------------------------------------------------------+


