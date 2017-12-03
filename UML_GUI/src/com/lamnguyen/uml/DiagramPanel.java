package com.lamnguyen.uml;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JScrollPane;

import com.mindfusion.drawing.*;
import com.mindfusion.diagramming.*;

import com.manhnguyen.classdiagram.*;

/**
 * Draw Java Project diagram
 * @author Nguyen Ngoc Lam
 * This class uses MindFusion's JDiagram library
 */

public class DiagramPanel extends JScrollPane {
	private Diagram diagram;
	private DiagramView diagramView;
	private Font containerFont;
	private HashMap<String, ContainerNode> diagramNodes = new HashMap<>();

	/**
	 * DiagramPanel Constructor
	 */
	public DiagramPanel() {
		super();
		containerFont = new Font("", Font.BOLD, 8);
	}

	/**
	 * Diagram getter
	 * @return diagram to be used in other classes
	 */

	public Diagram getDiagram() {
		return diagram;
	}

	/**
	 * Draw the diagram
	 * @param classes
	 */

	public void draw(ArrayList<ClassTree> classes) {

		diagram = new Diagram();
		diagramView = new DiagramView(diagram);

		setViewportView(diagramView);

		createDiagram(classes);

		diagram.getNodeEffects().add(new GlassEffect()); //Using the glass effect (pre-defined in the API) for diagram
		TableNodeStyle tableNodeStyle = diagram.getTableNodeStyle();
		tableNodeStyle.setBrush(new SolidBrush(Color.BLACK)); //Using black for the diagram outline colour

		ContainerNodeStyle containerNodeStyle = diagram.getContainerNodeStyle();
		containerNodeStyle.setShadowBrush(new SolidBrush(Color.white));

		diagramView.setBehavior(Behavior.PanAndModify);
		diagramView.setModificationStart(ModificationStart.AutoHandles);

		diagram.setSelectAfterCreate(false);
		diagram.setFont(containerFont);
		diagram.setEnableLanes(true);
		diagram.setLinkRouter(new GridRouter());
		diagram.setLinkCrossings(LinkCrossings.Cut);
		diagramView.setZoomFactor(70);
		//Scroll wheel listener, to be used for zooming
		diagramView.addMouseWheelListener(e -> zoom(e));
	}

	/**
	 * Create the diagram
	 * @param classes ArrayList of classes in project
	 */

	private void createDiagram(ArrayList<ClassTree> classes) {
		for (ClassTree c : classes) {
			Cell cell;
			ContainerNode container = diagram.getFactory().createContainerNode(0, 15, 0, 0, false); //Main container containing class information
			TableNode node = diagram.getFactory().createTableNode(0, 15, 0, 0); //Nodes to store attributes and methods

			//Set the class name as title
			container.setId(c.getName());
			container.setAllowAddChildren(false);
			container.setCaption(c.getName());
			container.setCaptionHeight(10);
			container.setFont(containerFont);

			//Set node properties
			node.redimTable(1, 0);
			node.setLocked(true);
			node.setCellFrameStyle(CellFrameStyle.Simple);
			node.setCaptionHeight(0);
			node.setCaption(null);

			//Attribute List
			for (String a : c.getAttributeList()) {
				cell = node.getCell(0, node.addRow());
				cell.setTextPadding(new Thickness(8, 2, 5, 2));
				cell.setBrush(new SolidBrush(Color.white));
				cell.setText(a);
				cell.setTextFormat(new TextFormat(Align.Near, Align.Center));
			}
			cell = node.getCell(0, node.addRow());
			//Method List
			for (String m : c.getMethodList()) {
				cell = node.getCell(0, node.addRow());
				cell.setTextPadding( new Thickness(8, 2, 5, 2));
				cell.setBrush(new SolidBrush(Color.white));
				cell.setText(m);
				cell.setTextFormat(new TextFormat(Align.Near, Align.Center));
			}

			node.addRow();

			node.setObstacle(true);
			node.resizeToFitText(true);

            container.setMargin(0);
			container.add(node);
			container.updateBounds(true);
			container.setObstacle(false);

			diagramNodes.put(container.getCaption(), container);
			diagram.add(container);
		}

		createRelationship(classes);

		TreeLayout layout = new TreeLayout();
		layout.setLinkStyle(TreeLayoutLinkType.Straight);
		layout.setLevelDistance(30);
		layout.setNodeDistance(30);
		layout.arrange(diagram);

		diagram.resizeToFitItems(10);
		diagram.setAutoResize(AutoResize.AllDirections);
	}

	/**
	 * Create relationships between classes
	 * @param classes ArrayList of classes in project
	 */
	private void createRelationship(ArrayList<ClassTree> classes) {
		Class currentClass;
		DiagramNode parent;
		DiagramNode child;
        ArrayList<String> className = new ArrayList<>();
        for(ClassTree c : classes){
            className.add(c.getName());
        }

		for (ClassTree c : classes) {
			child = diagramNodes.get(c.getName());

			if(c.getParentName() != null) {
				parent = diagramNodes.get(c.getParentName());
				if(child == parent) continue;
				else if(!className.contains(c.getParentName())) continue;
				DiagramLink link = diagram.getFactory().createDiagramLink(parent, child);
				link.setBaseShape(ArrowHeads.PointerArrow);
				link.setHeadShape(ArrowHeads.None);
				link.setBaseBrush(new SolidBrush(new Color(238, 19, 32)));
				link.setDynamic(true);
				link.setShadowBrush(new SolidBrush(Color.white));
				link.setLocked(true);
			}
			ClassDiagram cd = new ClassDiagram();
			cd.checkHasARelationship(classes);
			for(String s : c.objectAttributeList){
				if(s.equals(" ")) continue;
				else{
					parent = diagramNodes.get(s);
					DiagramLink link = diagram.getFactory().createDiagramLink(child, parent); //Reversing the order due to how the logic works, need to find a better way
					link.setBaseShape(ArrowHeads.PointerArrow);
					link.setHeadShape(ArrowHeads.None);
					link.setBaseBrush(new SolidBrush(new Color(238, 19, 32)));
					link.setDynamic(true);
					link.setShadowBrush(new SolidBrush(Color.white));
					link.setLocked(true);
				}
			}
		}
	}

	/**
	 * Zoom method
	 */
	private void zoom(MouseWheelEvent e){
		int notches = e.getWheelRotation();
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			float zoomFactor = diagramView.getZoomFactor();
			if (zoomFactor <= 20 && notches > 0) return;
			diagramView.setZoomFactor(zoomFactor - notches);
		}
	}

}