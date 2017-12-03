package com.lamnguyen.uml;

import com.manhnguyen.classdiagram.ClassDiagram;
import com.manhnguyen.classdiagram.ClassTree;
import com.manhnguyen.classdiagram.FileUtils;
import com.mindfusion.diagramming.Diagram;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Create the tool bar, its buttons, and every action on the tool bar
 *
 * @author Nguyen Ngoc Lam
 */

public class ToolBar extends JToolBar{
    private static ToolBar toolBar = new ToolBar();

    private JButton openProject;
    private JButton findFunction;
    private JButton findAttribute;
    private JButton redrawDiagram;

    protected static String selectedPath;
    private static String folderName;
    private ArrayList<ClassTree> classes;

    /**
     * ToolBar constructor
     */
    public ToolBar(){
        openProject = new JButton("Open Project");
        redrawDiagram = new JButton("Re-draw Diagram");
        findFunction = new JButton("Find Function");
        findAttribute = new JButton("Find Attribute");
        initContents();
    }

    /**
     * Initialize all contents on the tool bar
     */
    private void initContents(){
        openProject.setPreferredSize(new Dimension(120,40));
        redrawDiagram.setPreferredSize(new Dimension(150,40));
        findFunction.setPreferredSize(new Dimension(120,40));
        findAttribute.setPreferredSize(new Dimension(120,40));

        openProject.setBorder(BorderFactory.createBevelBorder(0));
        redrawDiagram.setBorder(BorderFactory.createBevelBorder(0));
        findFunction.setBorder(BorderFactory.createBevelBorder(0));
        findAttribute.setBorder(BorderFactory.createBevelBorder(0));

        openProject.setFocusPainted(false);
        redrawDiagram.setFocusPainted(false);
        findFunction.setFocusPainted(false);
        findAttribute.setFocusPainted(false);

        addListeners();

        this.add(openProject);
        this.add(Box.createHorizontalStrut(10));
        this.add(redrawDiagram);
        this.add(Box.createHorizontalStrut(10));
        this.add(findFunction);
        this.add(Box.createHorizontalStrut(10));
        this.add(findAttribute);
    }

    /**
     * Tool bar getter
     * @return tool bar to be used on other class and added to the GUI
     */
    public static ToolBar getToolBar() {
        return toolBar;
    }

    /**
     * Set the array list contains all classes from the project
     * @param selectedPath path to the project
     * @return the array list
     */
    protected static ArrayList<ClassTree> setClasses(String selectedPath){
        FileUtils fu = new FileUtils();
        ArrayList<File> files = new ArrayList<>();
        try {
            files = fu.getAllJavaFiles(new File(selectedPath));
        } catch(Exception e){
            e.printStackTrace();
            noProjectError();
        }
        //System.out.println(new File("..").getAbsolutePath());
        ArrayList<ClassTree> classes = new ArrayList<>();

        for (File f : files) {
            ClassTree c = new ClassTree(f);
            classes.add(c);
        }
        ClassDiagram.linkClass(classes);
        for (ClassTree c : classes) {
            System.out.print(c.showInformation());
        }
        return classes;
    }

    /**
     * Get the selected path to project folder
     * @return path to project folder
     */
    public String getSelectedPath() {
        return selectedPath;
    }

    /**
     * Set the selected path to project folder
     * @param selectedPath path to project folder
     */
    public void setSelectedPath(String selectedPath) {
        ToolBar.selectedPath = selectedPath;
    }

    /**
     * Get the project folder's name
     * @return project folder's name
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * Set the project folder's name
     * @param folderName project folder's name
     */
    public void setFolderName(String folderName) {
        ToolBar.folderName = folderName;
    }

    /**
     * Add listeners to the buttons
     */
    private void addListeners() {
        openProject.addActionListener(e -> openFolder());
        redrawDiagram.addActionListener(e -> re_draw());
        findFunction.addActionListener(e -> findFunction());
        findAttribute.addActionListener(e -> findAttribute());
    }

    /**
     * Re-draw the diagram
     */
    private void re_draw(){
        try {
            DiagramPanel dp = UML_GUI.getDiagramPanel();
            Diagram diagram = dp.getDiagram();
            diagram.clearAll();
            drawGUI();
        } catch(Exception e){
            noProjectError();
        }
    }

    /**
     * Open the project folder
     */
    protected void openFolder(){
        JFileChooser chooser = new JFileChooser(new java.io.File("."));

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.changeToParentDirectory();
        chooser.setDialogTitle("Load Project");
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);

        if (chooser.showDialog(UML_GUI.getUMLWindow(), "Load") == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();
            setSelectedPath(folder.getAbsolutePath());
            setFolderName(folder.getName());
            drawGUI();
        }
    }

    /**
     * Set the parameters to create everything on the GUI
     */
    private static void drawGUI(){
        ArrayList<ClassTree> classes = setClasses(selectedPath);

        UML_GUI.getProjectTree().draw(classes, folderName);
        UML_GUI.getDiagramPanel().draw(classes);

        UML_GUI.getUMLWindow().revalidate();
        UML_GUI.getUMLWindow().repaint();
    }

    /**
     * Find function by name in project
     */
    protected void findFunction(){
        String name = JOptionPane.showInputDialog(UML_GUI.getUMLWindow(), "Input function you want to find",
                null);
        if(name != null) {
            if(!name.contains("(") && !name.contains(")")){
                StringBuilder fixName = new StringBuilder();
                fixName.append(name + "()");
                name = fixName.toString();
            }
            try {
                classes = setClasses(selectedPath);
                ClassDiagram cd = new ClassDiagram();
                String result = cd.searchForFunction(classes, name);
                if (!result.equals("")) {
                    JOptionPane.showMessageDialog(null, result, "Result", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No Function Found", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch(Exception e){
                noProjectError();
            }
        }
    }

    /**
     * Find Attribute in project
     */
    protected void findAttribute(){
        String name = JOptionPane.showInputDialog(UML_GUI.getUMLWindow(), "Input attribute you want to find",
                null);
        if(name != null) {
            try {
                classes = setClasses(selectedPath);
                ClassDiagram cd = new ClassDiagram();
                String result = cd.searchForAttribute(classes, name);
                if (!result.equals("")) {
                    JOptionPane.showMessageDialog(null, result, "Result", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No Attribute Found", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch(Exception e){
                noProjectError();
            }
        }
    }
    protected static void noProjectError(){
        JOptionPane.showMessageDialog(null, "No Project Loaded", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
