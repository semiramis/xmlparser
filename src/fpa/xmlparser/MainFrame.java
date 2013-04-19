package fpa.xmlparser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;

public class MainFrame extends JFrame {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 300;

	JPanel panel_ta;
	JPanel panel_menu;
	JTextArea ta_xmlOut;
	JTextField tf;
	String[] operations = new String[] { "xml", "txt" };
	JRadioButton rb_xml;
	JRadioButton rb_txt;

	JButton btn_save;
	JButton btn_read;
	JFileChooser chooser;
	FileTypeFilter filter;
	File file;

	EventHandler listener;

	public MainFrame() {
		init();
	}

	private void init() {
		// *** MainFrame ***//
		setTitle("FPA - XML Parser");
		setSize(WIDTH, HEIGHT);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setVisible(true);

		// *** EventHandler ***//
		listener = new EventHandler();

		// *** JPanel TextArea***//
		panel_ta = new JPanel();
		add(panel_ta, BorderLayout.CENTER);
		panel_ta.setVisible(true);
		panel_ta.setLayout(new BorderLayout(5, 5));
		panel_ta.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// *** JPanel Menu ***//
		panel_menu = new JPanel();
		panel_menu.setLayout(new FlowLayout());
		panel_ta.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));

		add(panel_menu, BorderLayout.NORTH);
		panel_ta.setVisible(true);
		panel_ta.setLayout(new BorderLayout(5, 5));
		panel_ta.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// *** JTextArea ***//
		ta_xmlOut = new JTextArea();
		ta_xmlOut.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
				Color.DARK_GRAY, Color.LIGHT_GRAY));
		JScrollPane sp = new JScrollPane(ta_xmlOut);
		panel_ta.add(sp, BorderLayout.CENTER);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		setVisible(true);
		sp.add(ta_xmlOut);
		

		// *** JTextField ***//
		tf = new JTextField("test");
		tf.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setVisible(true);

		// *** JButton ***//
		btn_read = new JButton("read");
		btn_save = new JButton("save");
		panel_menu.add(btn_read);
		panel_menu.add(btn_save);

		btn_read.addActionListener(listener);
		btn_save.addActionListener(listener);

		// *** JRadioButton ***//
		rb_xml = new JRadioButton("xml");
		rb_xml.setSelected(true);
		rb_txt = new JRadioButton("txt");
		rb_txt.setSelected(false);
		panel_menu.add(rb_xml);
		panel_menu.add(rb_txt);

		rb_xml.addActionListener(listener);
		rb_txt.addActionListener(listener);

		// *** JFileChooser ***//
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);

		// *** FileTypeFilter ***//
		for (String s : operations) {
			filter = new FileTypeFilter(s);
			chooser.addChoosableFileFilter(filter);
		}

		setVisible(true);

	}

	// *** EventHandler ***//

	public class EventHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == rb_txt) {
				System.out.println("txt");
				rb_xml.setSelected(!rb_xml.isSelected());
			}

			if (e.getSource() == rb_xml) {
				System.out.println("xml");
				rb_txt.setSelected(!rb_txt.isSelected());
			}

			if (e.getSource() == btn_read) {
				System.out.println("read File");
				chooser.showOpenDialog(null);
				file = chooser.getSelectedFile();
				if (file != null) {
					setTitle(getTitle() + "  |  " + file.getPath());

					try {
						String string;
						BufferedReader reader = new BufferedReader(
								new FileReader(file));
						System.out.println("file ausgelesen aus: "
								+ file.getAbsolutePath());
						while ((string = reader.readLine()) != null) {
							System.out.println(string);
							ta_xmlOut.append("\n" + string);
							// ta_xmlOut.setText(ta_xmlOut + "\n" + string);
						}
					} catch (Exception ex) {

					}
				}
			}

			if (e.getSource() == btn_save) {
				System.out.println("save File");
				chooser.showSaveDialog(null);
			}
		}

	}

	// *** FileTypeFilter ***//

	public class FileTypeFilter extends FileFilter {
		private String extension;

		public FileTypeFilter(String extension) {
			this.extension = extension;
		}

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}
			return f.getName().endsWith(extension);
		}

		@Override
		public String getDescription() {
			return ("*." + extension);
		}
	}
    //
    //public static void main(String[] args) {
	//	new MainFrame();
	//}

}
