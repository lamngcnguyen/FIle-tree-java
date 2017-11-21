import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
/**
 * @todo Menu Bar: Select folder dir and custom setting (font, font size) // done
 * @todo 4 buttons: select dir, zoom, exit
 * @todo Interactive panel for classes and methods
 * @todo Folder directory selector //done - side-panel
 * @todo (Optional) drop down menu to choose font and size
 */

/**
 * Display a file system in a JTree view
 * @author Nguyen Ngoc Lam
 * This code is based on Ian Darwin's solution for displaying folder tree in Java Swing
 * 
 */
public class FileTree extends JPanel {
    /**
     * FileTree Constructor
     *
     * @param dir file directory
     */
    
    public FileTree(File dir) {
        setLayout(new BorderLayout());

        // Make a tree list with all the nodes, and make it a JTree
        JTree tree = new JTree(addNodes(null, dir));

        // Add a listener
        tree.addTreeSelectionListener((TreeSelectionEvent e) -> {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) e
                    .getPath().getLastPathComponent();
            System.out.println("You selected " + node);
        });

        // Lastly, put the JTree into a JScrollPane.
        JScrollPane scrollpane = new JScrollPane();
        scrollpane.getViewport().add(tree);
        add(BorderLayout.CENTER, scrollpane);
    }

    /**
     *  Add nodes from under "dir" into curTop. Highly recursive.
     *  @param curTop current top position
     *  @param dir directory
     */
    DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
        String curPath = dir.getPath();
        
        DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
        if (curTop != null) { // should only be null at root
            curTop.add(curDir);
        }
        
        ArrayList<String> ol = new ArrayList();
        String[] tmp = dir.list();
        ol.addAll(Arrays.asList(tmp));
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        File f;
        ArrayList<String> files = new ArrayList();
        
        // Make two passes, one for Dirs and one for Files. This is #1.
        for (int i = 0; i < ol.size(); i++) {
            String thisObject = (String) ol.get(i);
            String newPath;
            if (curPath.equals("."))
                newPath = thisObject;
            else
                newPath = curPath + File.separator + thisObject;
            if ((f = new File(newPath)).isDirectory())
                addNodes(curDir, f);
            else
                files.add(thisObject);
        }
        
        // Pass two: for files.
        for (int fnum = 0; fnum < files.size(); fnum++)
            curDir.add(new DefaultMutableTreeNode(files.get(fnum)));
        return curDir;
    }
    
    protected static void displayFileTree(String path, JPanel panel){
        if (path.length() == 0) {
            panel.add(new FileTree(new File(".")));
        } else {
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));          
                panel.add(new FileTree(new File(path)));
        }
    }
}