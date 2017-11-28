package com.lamnguyen.uml;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import com.manhnguyen.classdiagram.*;
import java.io.File;
/**
 * TODO: use Graphics library to draw rectangles containing file informations
 * TODO: draw lines between rectangles
 */

/**
 *
 * @author dhungc3
 */

public class Diagram_Panel extends JPanel{
    private Graphics2D g2d;
    private final double scale;
    private ArrayList<File> methodList;
    private String selectedFile;
    
    private final Font font;
    private final Color color;
    private String fontName;
    private int size;
    
    public Diagram_Panel(String path){
        scale = 1.0;
        font = new Font("", Font.PLAIN, 50);
        color = Color.BLACK;
        methodList = new ArrayList<>();
        selectedFile = path;
    }
    
    public Graphics2D getG2d() {
        return g2d;
    }

    public void setG2d(Graphics2D g2d) {
        this.g2d = g2d;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<File> getMethodList() {
        return methodList;
    }

    public void setMethodList(ArrayList<File> methodList) {
        this.methodList = methodList;
    }
      
    public String getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawPanel(g);
    }

    public void drawPanel(Graphics g){
//        super.paint(g);
        File folder = new File(getSelectedFile());
        g2d = (Graphics2D) g;
        ArrayList<File> methods = getMethodList();
        FontMetrics fm = g2d.getFontMetrics();
        String string = "Hello World";
        
        g2d.scale(scale, scale);
        g2d.setFont(font);
        g2d.setColor(color);

        int x = 50;
        int y = 50;
        int h = fm.getHeight();
        int w = fm.stringWidth(string);

        g2d.drawRect(x, y, w, h);
        g2d.drawString(string, x, y + h);
        g2d.dispose();
        
        updateUI();
    }
    
//    @Override
//    public Dimension getPreferredSize(){
//        return new Dimension(10000,5000);
//    }

}
