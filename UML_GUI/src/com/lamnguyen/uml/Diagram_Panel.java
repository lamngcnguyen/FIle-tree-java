package com.lamnguyen.uml;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import com.manhnguyen.classdiagram.*;
import java.io.File;

/**
 * Handle diagram drawing operation
 *
 * @author Nguyen Ngoc Lam
 */
public class Diagram_Panel extends JPanel {

    private Graphics2D g2d;
    private final double scale;
    private final JScrollPane scrollpane;
    private File selectedFolder;
    private ArrayList<File> files;
    private ArrayList<Rectangle> nodeList;
    private ArrayList<ClassTree> classes;
    private int nodeHeight;
    private int nodeWidth;
    private int nodeX;
    private int nodeY;
    private int space;
    FileUtils fu = new FileUtils();

    private final Font font;
    private final Color color;
    private String fontName;

    public Diagram_Panel(String path) {
        scale = 1.0;
        font = new Font("", Font.PLAIN, 20);
        color = Color.BLACK;
        classes = new ArrayList<>();
        if (!path.equals("")) {
            selectedFolder = new File(path);
            files = fu.getAllJavaFiles(selectedFolder);
        }

        nodeHeight = 25;
        nodeWidth = 350;
        nodeX = 30;
        nodeY = 30;
        space = 20;

        scrollpane = new JScrollPane(this);
        int unitIncrement = 15;
        scrollpane.getVerticalScrollBar().setUnitIncrement(unitIncrement);
        scrollpane.getHorizontalScrollBar().setUnitIncrement(unitIncrement);
        // mouse wheel is for zooming
        scrollpane.setWheelScrollingEnabled(false);
        // disable keyboard scrolling
        InputMap im = scrollpane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke("UP"), "none");
        im.put(KeyStroke.getKeyStroke("DOWN"), "none");
        im.put(KeyStroke.getKeyStroke("LEFT"), "none");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "none");
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public Component getScrollPane() {
        return scrollpane;
    }

    public int getNodeHeight() {
        return nodeHeight;
    }

    public void setNodeHeight(int nodeHeight) {
        this.nodeHeight = nodeHeight;
    }

    public int getNodeWidth() {
        return nodeWidth;
    }

    public void setNodeWidth(int nodeWidth) {
        this.nodeWidth = nodeWidth;
    }

    public int getNodeX() {
        return nodeX;
    }

    public void setNodeX(int nodeX) {
        this.nodeX = nodeX;
    }

    public int getNodeY() {
        return nodeY;
    }

    public void setNodeY(int nodeY) {
        this.nodeY = nodeY;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public ArrayList<ClassTree> getClasses() {
        return classes;
    }

    public void setClasses() {
        for (File f : files) {
            ClassTree c = new ClassTree(f);
            classes.add(c);
        }
    }

    public ArrayList<Rectangle> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList<Rectangle> nodeList) {
        this.nodeList = nodeList;
    }

    public ArrayList<Rectangle> assignNodeToClass() {
        int X = getNodeX();
        int Y = getNodeY();
        for (int i = 0; i < classes.size(); i++) {
            if (classes.size() > 0) {
                Rectangle node = new Rectangle();
                node.setBounds(X, Y, getNodeWidth(), getNodeHeight() * classes.size());
                nodeList.add(node);
                X += getNodeWidth() + getSpace();
            }
        }
        return nodeList;
    }

    public void drawPanel(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.scale(scale, scale);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int tableHeight = fm.getHeight();

        ArrayList<Rectangle> nodes = assignNodeToClass();
        if (nodes != null) {
            for (int i = 0; i < nodes.size(); i++) {
                g2d.draw(nodes.get(i));
                for (int j = 0; j < classes.size(); j++) {
                    Rectangle table = new Table();
                    table.setBounds((int) nodes.get(i).getX(), (int) nodes.get(i).getY() + nodeHeight * j, nodeWidth, nodeHeight);
                    g2d.draw(table);
                    g2d.setColor(color);
                    g2d.drawString(classes.get(j).getName(), (float) table.getX() + 5, (float) table.getY() + 15);
                    g2d.setColor(Color.BLACK);
                }
            }
        }

        //g2d.dispose();
        updateUI();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPanel(g);
    }
//    @Override
//    public Dimension getPreferredSize(){
//        return new Dimension(10000,5000);
//    }

}
