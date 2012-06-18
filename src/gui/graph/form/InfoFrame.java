
package gui.graph.form;

public class InfoFrame extends javax.swing.JFrame {

    public InfoFrame() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainScrollPane = new javax.swing.JScrollPane();
        mainTabbedPane = new javax.swing.JTabbedPane();
        dataManagerPanel = new javax.swing.JPanel();
        componentLabel = new javax.swing.JLabel();
        topDivider = new javax.swing.JSeparator();
        tagInfoSplitPane = new javax.swing.JSplitPane();
        listScrollPane = new javax.swing.JScrollPane();
        tagList = new javax.swing.JList();
        textAreaScrollPane = new javax.swing.JScrollPane();
        infoTextArea = new javax.swing.JTextArea();
        newTagField = new javax.swing.JTextField();
        physicsPane = new javax.swing.JPanel();
        massSlider = new javax.swing.JSlider();
        equilibriumSlider = new javax.swing.JSlider();
        equilibriumLabel = new javax.swing.JLabel();
        repulsionLabel = new javax.swing.JLabel();
        repulsionSlider = new javax.swing.JSlider();
        massLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Control Pane");
        setName("controlFrame"); // NOI18N

        mainScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        mainTabbedPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mainTabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        componentLabel.setText("(no component selected)");
        componentLabel.setToolTipText("This is the currently selected component");

        tagInfoSplitPane.setDividerLocation(85);
        tagInfoSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tagList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

        javax.swing.GroupLayout dataManagerPanelLayout = new javax.swing.GroupLayout(dataManagerPanel);
        dataManagerPanel.setLayout(dataManagerPanelLayout);
        dataManagerPanelLayout.setHorizontalGroup(
            dataManagerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataManagerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataManagerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tagInfoSplitPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(newTagField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(componentLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(topDivider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );
        dataManagerPanelLayout.setVerticalGroup(
            dataManagerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataManagerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(componentLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(topDivider, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tagInfoSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newTagField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        repulsionLabel.setText("Repulsion Strength (log scale): 8");

        repulsionSlider.setMajorTickSpacing(4);
        repulsionSlider.setMaximum(24);
        repulsionSlider.setMinorTickSpacing(2);
        repulsionSlider.setPaintTicks(true);
        repulsionSlider.setToolTipText("Drag to change repulsion between vertices.");
        repulsionSlider.setValue(8);

        massLabel.setText("Mass: 400 ");

        javax.swing.GroupLayout physicsPaneLayout = new javax.swing.GroupLayout(physicsPane);
        physicsPane.setLayout(physicsPaneLayout);
        physicsPaneLayout.setHorizontalGroup(
            physicsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(physicsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(physicsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(equilibriumLabel)
                    .addComponent(repulsionLabel)
                    .addGroup(physicsPaneLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(equilibriumSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                    .addGroup(physicsPaneLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(repulsionSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                    .addGroup(physicsPaneLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(massSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                    .addComponent(massLabel))
                .addContainerGap())
        );
        physicsPaneLayout.setVerticalGroup(
            physicsPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(physicsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(equilibriumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(equilibriumSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repulsionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repulsionSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(massLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(massSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Physics Options", physicsPane);

        mainScrollPane.setViewportView(mainTabbedPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new InfoFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel componentLabel;
    private javax.swing.JPanel dataManagerPanel;
    private javax.swing.JLabel equilibriumLabel;
    private javax.swing.JSlider equilibriumSlider;
    private javax.swing.JTextArea infoTextArea;
    private javax.swing.JScrollPane listScrollPane;
    private javax.swing.JScrollPane mainScrollPane;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JLabel massLabel;
    private javax.swing.JSlider massSlider;
    private javax.swing.JTextField newTagField;
    private javax.swing.JPanel physicsPane;
    private javax.swing.JLabel repulsionLabel;
    private javax.swing.JSlider repulsionSlider;
    private javax.swing.JSplitPane tagInfoSplitPane;
    private javax.swing.JList tagList;
    private javax.swing.JScrollPane textAreaScrollPane;
    private javax.swing.JSeparator topDivider;
    // End of variables declaration//GEN-END:variables
}
