
package gui.graph;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class InfoDisplay extends JInternalFrame {
    
    GViewer view;

    public InfoDisplay(GViewer parent) {
        this.view = parent;
        initComponents();
        setFocusable(true);
    }

    private void initComponents() {
        
        mainScrollPane = new JScrollPane();
        mainTabbedPane = new JTabbedPane();
        dataManagerPanel = new JPanel();
        componentLabel = new JLabel();
        topDivider = new JSeparator();
        tagInfoSplitPane = new JSplitPane();
        listScrollPane = new JScrollPane();
        tagList = new JList();
        textAreaScrollPane = new JScrollPane();
        infoTextArea = new JTextArea();
        newTagField = new JTextField();
        physicsPane = new JPanel();
        massSlider = new JSlider();
        equilibriumSlider = new JSlider();
        equilibriumLabel = new JLabel();
        repulsionLabel = new JLabel();
        repulsionSlider = new JSlider();
        massLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Control Window");

        setResizable(true);
        setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE ));  
        setMinimumSize(new Dimension(315,35));  
        setMaximizable(false);
        setClosable(false);
        setIconifiable(true);
        
        setFocusable(true);
        setVisible(true);
        
        mainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        mainTabbedPane.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mainTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        componentLabel.setText("(no component selected)");
        componentLabel.setToolTipText("This is the currently selected component");

        tagInfoSplitPane.setDividerLocation(85);
        tagInfoSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        tagList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tagList.setToolTipText("Select a tag to view or edit.");
        listScrollPane.setViewportView(tagList);

        tagInfoSplitPane.setTopComponent(listScrollPane);

        infoTextArea.setColumns(20);
        infoTextArea.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        infoTextArea.setRows(5);
        infoTextArea.setToolTipText("View and edit information regarding the selected component and tag.");
        textAreaScrollPane.setViewportView(infoTextArea);

        tagInfoSplitPane.setRightComponent(textAreaScrollPane);

        newTagField.setToolTipText("Enter a new tag for this object.");

        GroupLayout dataManagerPanelLayout = new GroupLayout(dataManagerPanel);
        dataManagerPanel.setLayout(dataManagerPanelLayout);
        dataManagerPanelLayout.setHorizontalGroup(
            dataManagerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, dataManagerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataManagerPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(tagInfoSplitPane, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(newTagField, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(componentLabel, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(topDivider, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );
        dataManagerPanelLayout.setVerticalGroup(
            dataManagerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(dataManagerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(componentLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topDivider, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tagInfoSplitPane, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newTagField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainTabbedPane.addTab("Data Manager", dataManagerPanel);

        massSlider.setMajorTickSpacing(100);
        massSlider.setMaximum(1000);
        massSlider.setMinorTickSpacing(50);
        massSlider.setPaintTicks(true);
        massSlider.setToolTipText("Drag to change the mass of each vertex.");
        massSlider.setValue(400);

        equilibriumSlider.setMajorTickSpacing(100);
        equilibriumSlider.setMaximum(500);
        equilibriumSlider.setMinorTickSpacing(50);
        equilibriumSlider.setPaintTicks(true);
        equilibriumSlider.setToolTipText("Drag to change the equilibrium distance.");
        equilibriumSlider.setValue(150);

        equilibriumLabel.setText("Equlibrium Distance: 150");

        repulsionLabel.setText("Repulsion Strength (log scale): 16");

        repulsionSlider.setMajorTickSpacing(8);
        repulsionSlider.setMaximum(48);
        repulsionSlider.setMinorTickSpacing(4);
        repulsionSlider.setPaintTicks(true);
        repulsionSlider.setToolTipText("Drag to change repulsion between vertices.");
        repulsionSlider.setValue(16);

        massLabel.setText("Mass: 400");

        GroupLayout physicsPaneLayout = new GroupLayout(physicsPane);
        physicsPane.setLayout(physicsPaneLayout);
        physicsPaneLayout.setHorizontalGroup(
            physicsPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(physicsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(physicsPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(equilibriumLabel)
                    .addComponent(repulsionLabel)
                    .addGroup(physicsPaneLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(equilibriumSlider, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                    .addGroup(physicsPaneLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(repulsionSlider, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                    .addGroup(physicsPaneLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(massSlider, GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                    .addComponent(massLabel))
                .addContainerGap())
        );
        physicsPaneLayout.setVerticalGroup(
            physicsPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(physicsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(equilibriumLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(equilibriumSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repulsionLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repulsionSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(massLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(massSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Physics Options", physicsPane);

        mainScrollPane.setViewportView(mainTabbedPane);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(mainScrollPane, GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );

        pack();
        setLayout(new GridLayout());
    }
    
    void addKeyListener() {
        addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (view.currentlySelected != null) {
                        if (view.currentlySelected instanceof EdgePainter) {
                            view.removeEdge((EdgePainter)view.currentlySelected);
                        } else if (view.currentlySelected instanceof VertexPainter) {
                            view.removeVertex((VertexPainter)view.currentlySelected);
                        }
                        view.currentlySelected = null;
                    }
                }
            }
        });
    }
    
    void addIconListener() {
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                setBounds(getX(),getY(),getWidth(),35);
            }
            
            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                setBounds(getX(),getY(),getWidth(),500);
            }
        });
    }
    
    void addSliderListener() {
        massSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                view.controller.unitMass = massSlider.getValue();
                massLabel.setText("Mass: " + view.controller.unitMass);
            }
        });
        equilibriumSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                view.controller.equilibriumLength = equilibriumSlider.getValue();
                equilibriumLabel.setText("Equilibrium Length: " + view.controller.equilibriumLength);
            } 
        });
        repulsionSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                view.controller.repulsiveConstant = (float)(Math.pow(2,repulsionSlider.getValue()/4.0));
                repulsionLabel.setText("Repulsion strength: " + (int)(view.controller.repulsiveConstant));
            }
        });
    }

    private JLabel componentLabel;
    private JPanel dataManagerPanel;
    private JLabel equilibriumLabel;
    private JSlider equilibriumSlider;
    private JTextArea infoTextArea;
    private JScrollPane listScrollPane;
    private JScrollPane mainScrollPane;
    private JTabbedPane mainTabbedPane;
    private JLabel massLabel;
    private JSlider massSlider;
    private JTextField newTagField;
    private JPanel physicsPane;
    private JLabel repulsionLabel;
    private JSlider repulsionSlider;
    private JSplitPane tagInfoSplitPane;
    private JList tagList;
    private JScrollPane textAreaScrollPane;
    private JSeparator topDivider;
}