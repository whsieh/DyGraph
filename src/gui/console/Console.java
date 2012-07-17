package gui.console;

import gui.graph.GraphController;
import gui.graph.GraphViewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dygraph.FacebookGraphViewer;

import model.graph.Graph;
import model.graph.Vertex;

import stat.comm.CommunityTransformer;
import stat.comm.Dendrogram;

public class Console extends JFrame {

	private final static String LOG_STRING = "$>";
	private final static String ERR_STRING = "ERR>";
	
	private static Console CONSOLE;
	
	private JPanel contentPane;
	private JTextField input;
	private JPanel outputPanel;
	private JTextArea output;
	private JScrollPane outputScrollPane;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuEdit;
	private JMenuItem menuItemSaveOutput;
	private JMenuItem menuItemClearOutput;
	private JCheckBoxMenuItem menuItemLeetMode;
	private JMenu menuHelp;
	private JMenuItem menuItemShowCommands;
	
	private boolean isLeetMode;
	private List<String> commandHistory;
	private int historyIndex;
	private int posX=0,posY=0;
	
	private GraphController controller;	
	
	/**
	 * Create the frame.
	 */
	private Console() {
		historyIndex = 0;
		commandHistory = new ArrayList<String>(10);
		commandHistory.add("");
		commandHistory.add("");
		isLeetMode = false;
		controller = null;
		initializeComponents();
		initializeListeners();
		log("Console initialized");
	}
	
	public void setController(GraphController controller) {
		this.controller = controller;
	}

	public static Console getInstance() {
		if (CONSOLE == null) {
			CONSOLE = new Console();
		}
		return CONSOLE;		
	}
	
	public static boolean exists() {
		return CONSOLE != null;	
	}
	
	public void display() {
		setVisible(true);
	}
	
	public void log(String s) {
		String[] lines = s.split("\n");
		output.append(LOG_STRING + " " + lines[0] + "\n");
		for (int i = 1; i < lines.length; i++) {
			output.append("   " + lines[i] + "\n");
		}
	}
	
	public void err(String s) {
		String[] lines = s.split("\n");
		output.append(ERR_STRING + " " + lines[0] + "\n");
		for (int i = 1; i < lines.length; i++) {
			output.append("   " + lines[i] + "\n");
		}
	}
	
	private void parseStat(String[] in) {
		if (controller != null) {
			if (in.length == 1) {
				log("Usage for \"stat\":\n" + 
					"   stat comm: Run the modularity-based community detection algorithm.");
			} else if (in.length >= 2) {
				if (in[1].equals("comm")) {
					log("Running community detection algorithm...");
					long start = System.nanoTime();
					Dendrogram result = new CommunityTransformer().transform(controller.getModel());
					int end = (int)((System.nanoTime() - start)/1000000.0);
					log(result.toString());
					log("Time taken: " + end + " ms");
				}
			}
		} else {
			err("Missing reference to graph controller.");
		}
	}
	
	private void parseRemove(String[] in) {
		if (in.length >= 2) {
			if (in[1].equals("degree")) {
				GraphViewer view = controller.getView();
				Graph g = view.getGraph();
				for (String id : g.vertexSet()) {
					Vertex v = g.findVertex(id);
					if (v.degree() <= 0) {
						view.removeVertex(id);
					}
				}
			}
		}
	}
	
	private void parse(String command) {
		String[] in = command.split(" ");
		if (in.length > 0 && !in[0].equals("")) {
		switch(in[0]) {
			case "threshold":
				parseRemove(in);
				break;
			case "stat":
				parseStat(in);
				break;
			case "cls":
				output.setText("");
				break;
			case "close":
				dispose();
				break;
			case "exit":
				System.exit(0);
			case "whois":
				String id = in[1];
				FacebookGraphViewer fgv = (FacebookGraphViewer)controller.getView();
				log("Looking up id #" + id + ": " + fgv.whois(id));
			default:
				err("Unrecognized command: " + command);
				break;
		}
		} else {
			log("");
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintBorder((Graphics2D)g);
	}
	
	private void paintBorder(Graphics2D g) {
		
		Color c = g.getColor();
		
		g.setColor(Color.DARK_GRAY);
		
		Rectangle rect = getBounds();
		RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0,0,rect.width-1,rect.height-1,5,5);
		g.draw(roundedRectangle);
		
		g.setColor(c);
	}

	private void initializeListeners() {
		
		addMouseListener(new MouseAdapter() {
			@Override
		    public void mousePressed(MouseEvent e) {
				posX=e.getX();
				posY=e.getY();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				repaint();
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent evt) {		
				setLocation(evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
			}
		});		
		
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String cmd = input.getText();
				int size = commandHistory.size();
				parse(cmd);
				commandHistory.add(size-1,cmd);
				historyIndex = size;
				input.setText("");
			}
		});
		
		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (!commandHistory.isEmpty()) {
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (historyIndex > 0) {
							historyIndex--;
							String cmd = commandHistory.get(historyIndex);
							input.setText(cmd);
						} else {
							input.setText("");
						}

					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (historyIndex < commandHistory.size()-1) {
							historyIndex++;
							String cmd = commandHistory.get(historyIndex);
							input.setText(cmd);
						} else {
							input.setText("");
						}

					}
				}
			}
		});
		
		MouseAdapter exitBorderRepaint = new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				repaint(0,0,getWidth(),1);
			}
		};
		
		menuFile.addMouseListener(exitBorderRepaint);
		menuEdit.addMouseListener(exitBorderRepaint);
		menuHelp.addMouseListener(exitBorderRepaint);
		
		menuItemLeetMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isLeetMode) {
					output.setBackground(new Color(0, 0, 0));
					output.setForeground(new Color(0,255,0));
				} else {
					output.setBackground(new Color(255,255,255));
					output.setForeground(new Color(0,0,0));
				}
				isLeetMode = !isLeetMode;
				repaint(0,0,getWidth(),1);
			}
		});
		
		menuItemClearOutput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.setText("");
				repaint(0,0,getWidth(),1);
			}
		});
		
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	setState(Frame.ICONIFIED);
            }
        });
	}
	
	private void initializeComponents() {
		
		setTitle("Debug Console");
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		menuItemSaveOutput = new JMenuItem("Save Output");
		menuFile.add(menuItemSaveOutput);
		
		menuEdit = new JMenu("Edit");
		menuBar.add(menuEdit);
		
		menuItemClearOutput = new JMenuItem("Clear Output");
		menuEdit.add(menuItemClearOutput);
		
		menuItemLeetMode = new JCheckBoxMenuItem("Leet Mode");
		menuEdit.add(menuItemLeetMode);
		
		menuHelp = new JMenu("Help");
		menuBar.add(menuHelp);
		
		menuItemShowCommands = new JMenuItem("Show commands");
		menuHelp.add(menuItemShowCommands);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		outputPanel = new JPanel();
		outputPanel.setBackground(UIManager.getColor("Button.background"));
		outputPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Output", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_outputPanel = new GridBagConstraints();
		gbc_outputPanel.fill = GridBagConstraints.BOTH;
		gbc_outputPanel.gridwidth = 2;
		gbc_outputPanel.insets = new Insets(0, 0, 5, 0);
		gbc_outputPanel.gridx = 0;
		gbc_outputPanel.gridy = 0;
		contentPane.add(outputPanel, gbc_outputPanel);
		GridBagLayout gbl_outputPanel = new GridBagLayout();
		gbl_outputPanel.columnWidths = new int[]{0, 0};
		gbl_outputPanel.rowHeights = new int[]{0, 0};
		gbl_outputPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_outputPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		outputPanel.setLayout(gbl_outputPanel);
		
		outputScrollPane = new JScrollPane();
		GridBagConstraints gbc_outputScrollPane = new GridBagConstraints();
		gbc_outputScrollPane.fill = GridBagConstraints.BOTH;
		gbc_outputScrollPane.gridx = 0;
		gbc_outputScrollPane.gridy = 0;
		outputPanel.add(outputScrollPane, gbc_outputScrollPane);
		
		output = new JTextArea();
		outputScrollPane.setViewportView(output);
		output.setFont(new Font("Courier New", Font.PLAIN, 13));
		output.setForeground(Color.BLACK);
		output.setBackground(UIManager.getColor("Button.disabledShadow"));
		output.setEditable(false);
		
		JSeparator lineSeparator = new JSeparator();
		GridBagConstraints gbc_lineSeparator = new GridBagConstraints();
		gbc_lineSeparator.gridwidth = 2;
		gbc_lineSeparator.insets = new Insets(0, 0, 5, 0);
		gbc_lineSeparator.fill = GridBagConstraints.BOTH;
		gbc_lineSeparator.gridx = 0;
		gbc_lineSeparator.gridy = 1;
		contentPane.add(lineSeparator, gbc_lineSeparator);
		
		JLabel inputLabel = new JLabel(">>>");
		GridBagConstraints gbc_inputLabel = new GridBagConstraints();
		gbc_inputLabel.insets = new Insets(0, 0, 0, 5);
		gbc_inputLabel.anchor = GridBagConstraints.EAST;
		gbc_inputLabel.gridx = 0;
		gbc_inputLabel.gridy = 2;
		contentPane.add(inputLabel, gbc_inputLabel);
		
		input = new JTextField();
		GridBagConstraints gbc_input = new GridBagConstraints();
		gbc_input.fill = GridBagConstraints.HORIZONTAL;
		gbc_input.gridx = 1;
		gbc_input.gridy = 2;
		contentPane.add(input, gbc_input);
		input.setColumns(10);
		
		log("Please note: this build has a ton of issues.\n   Hopefully vertices flying everywhere is not one of them.");
		setState(Frame.ICONIFIED);
	}
	
}
