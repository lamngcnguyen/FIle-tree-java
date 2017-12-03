package com.lamnguyen.uml;

import com.manhnguyen.classdiagram.ClassDiagram;
import com.manhnguyen.classdiagram.ClassTree;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicMenuBarUI;

import javax.imageio.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Create the menu bar, its menus, and every action on the menu bar
 *
 * @author Nguyen Ngoc Lam
 */

public class MenuBar extends JMenuBar {
    private static MenuBar menu = new MenuBar();
    private ToolBar toolBar = new ToolBar();

    private JMenu menuFile;
    private JMenu menuTool;
    private JMenu menuHelp;

    private JMenuItem menuFile_Open;
    private JMenuItem menuFile_SaveImage;
    private JMenuItem menuFile_SaveText;
    private JMenuItem menuFile_Exit;

    private JMenuItem menuTool_FindAttribute;
    private JMenuItem menuTool_FindFunction;

    private JMenu subMenuWindow_Themes;

    private JMenuItem menuHelp_About;
    private JMenuItem menuHelp_Description;

    /**
     * MenuBar constructor
     */
    private MenuBar() {
        super();

        menuFile = new JMenu("File");
        menuTool = new JMenu("Tool");
        menuHelp = new JMenu("Help");

        menuFile_Open = new JMenuItem("Open Project");
        menuFile_SaveImage = new JMenuItem("Save as Image");
        menuFile_SaveText = new JMenuItem("Save as Text");
        menuFile_Exit = new JMenuItem("Exit");

        menuTool_FindAttribute = new JMenuItem("Find Attribute");
        menuTool_FindFunction = new JMenuItem("Find Function");

        menuHelp_About = new JMenuItem("About");
        menuHelp_Description = new JMenuItem("Bullet point description");

        this.setUI(new BasicMenuBarUI());

        initContents();
        addListeners();
    }

    /**
     * Menu bar getter
     *
     * @return menu bar to be added to the GUI
     */
    protected static MenuBar getMenuBar() {
        return menu;
    }

    /**
     * Initialize all contents on the menu bar
     */
    private void initContents() {
        this.add(menuFile);
        this.add(menuTool);
        this.add(menuHelp);

        menuFile.add(menuFile_Open);
        menuFile.add(menuFile_SaveImage);
        menuFile.add(menuFile_SaveText);
        menuFile.add(menuFile_Exit);

        menuTool.add(menuTool_FindAttribute);
        menuTool.add(menuTool_FindFunction);

        menuHelp.add(menuHelp_Description);
        menuHelp.add(menuHelp_About);
    }

    /**
     * Add listeners and actions on the menu bar
     */
    private void addListeners() {
        ToolBar toolBarFunctions = new ToolBar();
        //File > Open listener
        menuFile_Open.addActionListener(e -> toolBar.openFolder());
        //File > Exit listener
        menuFile_Exit.addActionListener(e -> System.exit(0));
        //File > Save Text listener
        menuFile_SaveText.addActionListener(e -> exportText());
        //File > Save Image listener
        menuFile_SaveImage.addActionListener(e -> exportImage());
        //Tool > Find Attribute listener
        menuTool_FindAttribute.addActionListener(e -> toolBarFunctions.findAttribute());
        //Tool > Find Function listener
        menuTool_FindFunction.addActionListener(e -> toolBarFunctions.findFunction());
        //Help > Bullet point description listener
        menuHelp_Description.addActionListener(e -> displayDescriptionPanel());
        //Help > About listener
        menuHelp_About.addActionListener(e -> displayAboutPanel());
    }

    /**
     * Export project diagram to text file
     */
    private void exportText() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Save as");
        chooser.setSelectedFile(new File(".\\" + "export"));
        //Add filter to choose .txt files only
        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return false;
                String name = f.getName();
                return name.endsWith(".txt") || name.endsWith(".TXT");
            }

            @Override
            public String getDescription() {
                return "Text files (*.txt)";
            }
        });
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showDialog(UML_GUI.getUMLWindow(), "Save Text") == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();
            if (file == null) return;
            //Add .txt extension if the save file don't have the .txt extension
            if (!file.getName().toLowerCase().endsWith(".txt"))
                file = new File(file.getParentFile(), file.getName() + ".txt");
            try {
                FileWriter writer = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(writer);

                ArrayList<ClassTree> classes = ToolBar.setClasses(ToolBar.selectedPath);
                bw.write("-----------Classes Relationship-----------\n");
                bw.write(ClassDiagram.linkClass(classes));
                bw.write("\n");
                bw.write("-----------Classes Information-----------\n");
                for (ClassTree c : classes) {
                    bw.write(c.showInformation());
                }

                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Export project diagram to image file
     */
    private void exportImage() {

        JFileChooser chooser = new JFileChooser(new java.io.File("."));

        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Save as");
        chooser.setSelectedFile(new File(".\\" + "export"));

        //<editor-fold defaultstate="collapsed" desc="Image file filter code">
        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return false;
                String name = f.getName();
                return name.endsWith(".bmp") || name.endsWith(".BMP");
            }

            @Override
            public String getDescription() {
                return "BMP Files (*.bmp)";
            }
        });

        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return false;
                String name = f.getName();
                return name.endsWith(".jpg") || name.endsWith(".JPG");
            }

            @Override
            public String getDescription() {
                return "JPG Files (*.jpg)";
            }
        });

        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return false;
                String name = f.getName();
                return name.endsWith(".jpeg") || name.endsWith(".JPEG");
            }

            @Override
            public String getDescription() {
                return "JPEG Files (*.jpeg)";
            }
        });

        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return false;
                String name = f.getName();
                return name.endsWith(".png") || name.endsWith(".PNG");
            }

            @Override
            public String getDescription() {
                return "PNG Files (*.png)";
            }
        });

        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) return false;
                String name = f.getName();
                return name.endsWith(".gif") || name.endsWith(".GIF");
            }

            @Override
            public String getDescription() {
                return "GIF Files (*.gif)";
            }
        });
        //</editor-fold>
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showDialog(UML_GUI.getUMLWindow(), "Save Picture") == JFileChooser.APPROVE_OPTION) {
            String ext = "";

            String fileExtension = chooser.getFileFilter().getDescription();
            if (fileExtension.equals("BMP Files (*.bmp)")) {
                ext = "bmp";

            } else if (fileExtension.equals("JPG Files (*.jpg)")) {
                ext = "jpg";

            } else if (fileExtension.equals("JPEG Files (*.jpeg)")) {
                ext = "jpeg";

            } else if (fileExtension.equals("PNG Files (*.png)")) {
                ext = "png";

            } else if (fileExtension.equals("GIF Files (*.gif)")) {
                ext = "gif";

            }
            //Add extension if file to save doesn't have extension
            File file = chooser.getSelectedFile();
            if (file == null) return;
            if (!file.getName().toLowerCase().endsWith("." + ext))
                file = new File(file.getParentFile(), file.getName() + "." + ext);
            try {
                BufferedImage img = UML_GUI.getDiagramPanel().getDiagram().createImage(BufferedImage.TYPE_INT_RGB);
                try {
                    ImageIO.write(img, ext, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch(Exception e){
                e.printStackTrace();
                ToolBar.noProjectError();
            }
        }
    }

    /**
     * Display the about panel
     */
    private void displayAboutPanel(){
        String info = "Authors: \nNguyen Ngoc Lam & Nguyen Duc Manh" +
                "\nClass: K61 - CA - CLC2\nUniversity of Engineering and Technology - VNU\n " +
                "\nThis program uses MindFusion's JDiagram library\nhttps://www.mindfusion.eu/java-pack.html";
        JOptionPane.showMessageDialog(null, info, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * Display the description panel
     */
    private void displayDescriptionPanel(){
        String description = "At the start of each cell, there are a few bullet points\n" + "Here's what they mean\n\n" + "' + ' Public attribute or function\n"
                + "' - ' Private attribute or function\n" + "' # ' Protected attribute or function\n";
        JOptionPane.showMessageDialog(null, description, "Description", JOptionPane.INFORMATION_MESSAGE);
    }
}
