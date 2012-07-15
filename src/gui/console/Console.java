package gui.console;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	
	
	/**
	 * Create the frame.
	 */
	private Console() {
		historyIndex = 0;
		commandHistory = new ArrayList<String>(10);
		commandHistory.add("");
		commandHistory.add("");
		isLeetMode = false;
		initializeComponents();
		initializeListeners();
		log("Console initialized");
	}

	public static Console getInstance() {
		if (CONSOLE == null) {
			CONSOLE = new Console();
		}
		return CONSOLE;		
	}
	
	public void display() {
		setVisible(true);
	}
	
	public void log(String s) {
		output.append(LOG_STRING + " " + s + "\n");
	}
	
	public void err(String s) {
		output.append(ERR_STRING + " " + s + "\n");
	}
	
	private void parse(String command) {
		
		switch(command) {
			default:
				err("Unrecognized command: " + command);
				break;
		}
	}

	private void initializeListeners() {
		
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
			}
		});
		
		menuItemClearOutput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output.setText("");
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
		setBounds(100, 100, 500, 600);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
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
	}
	
}
