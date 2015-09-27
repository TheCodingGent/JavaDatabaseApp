import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
/**
 * This class is the main class of the application where everything is initialized 
 * and launched.
 * 
 * @author Yasser Ghamlouch
 *
 */
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GUI components
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private JPanel contentPane;
	private JPanel leftPanel;
	private JButton btnOnline;
	private JButton btnOffline;
	private JPanel rightPanel;
	private JList<String> leftList;
	private JList<String> rightList;
	private JLabel lblSessions;
	private JButton btnSourceConfiguration;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Member fields
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private String[] mOnlineUsers = null;
	private String[] mOfflineUsers = null;
	private String[] sessions;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Custom listeners
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ActionListener btnOnlineListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			updateList(true);
		}
	};

	private ActionListener btnOfflineListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			updateList(false);
		}
	};

	private MouseListener listMouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && e.getComponent() == leftList) {
				updateSessionList();
			} else if (e.getClickCount() == 2 && e.getComponent() == rightList) {
				openGraphWindow();
			}
		}
	};

	private MouseListener configMouseListener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			if (!leftList.isSelectionEmpty()) {
				openConfigWindow();
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								"Please select the user for who you would like to da a configuration",
								"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	};

	// /
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Launch the application.
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create the frame.
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public MainWindow() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1018, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		leftPanel = new JPanel();
		leftPanel.setBounds(10, 11, 331, 415);
		getContentPane().add(leftPanel);
		leftPanel.setLayout(null);

		btnOnline = new JButton("Online");
		btnOnline.setBounds(10, 11, 89, 23);
		leftPanel.add(btnOnline);

		btnOffline = new JButton("Offline");
		btnOffline.setBounds(109, 11, 89, 23);
		leftPanel.add(btnOffline);

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Populating lists
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		mOnlineUsers = updateOnlineUserList();
		mOfflineUsers = updateOfflineUserList();
		sessions = updateUserSessions();

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Creating lists
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		leftList = new JList<String>();
		leftList.setListData(mOnlineUsers);
		leftList.setBounds(10, 36, 311, 368);
		leftList.setVisible(true);
		leftPanel.add(leftList);

		rightPanel = new JPanel();
		rightPanel.setBounds(351, 11, 643, 415);
		getContentPane().add(rightPanel);
		rightPanel.setLayout(null);

		lblSessions = new JLabel("Sessions");
		lblSessions.setBounds(279, 11, 65, 14);
		rightPanel.add(lblSessions);

		btnSourceConfiguration = new JButton("Source Configuration");
		btnSourceConfiguration.setBounds(10, 386, 185, 23);
		rightPanel.add(btnSourceConfiguration);

		rightList = new JList<String>();
		rightList.setBounds(10, 36, 623, 339);
		rightList.setVisible(true);
		rightPanel.add(rightList);

		// Listeners

		btnOnline.addActionListener(btnOnlineListener);
		btnOffline.addActionListener(btnOfflineListener);
		leftList.addMouseListener(listMouseListener);
		rightList.addMouseListener(listMouseListener);
		btnSourceConfiguration.addMouseListener(configMouseListener);

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Utils methods
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void updateList(Boolean Online) {

		if (Online) {
			mOnlineUsers = updateOnlineUserList();
			leftList.setListData(mOnlineUsers);
			leftList.updateUI();
		} else {
			mOfflineUsers = updateOfflineUserList();
			leftList.setListData(mOfflineUsers);
			leftList.updateUI();
		}

	}

	private void updateSessionList() {

		sessions = updateUserSessions();
		rightList.setListData(sessions);
		rightList.updateUI();
	}

	private void openGraphWindow() {
		GraphWindow graphWin = new GraphWindow();
		graphWin.setVisible(true);

	}

	private void openConfigWindow() {
		ConfigurationWindow configWin = new ConfigurationWindow();
		configWin.setVisible(true);

	}

	private String[] updateOnlineUserList() {

		DatabaseInterface db = new DatabaseInterface();
		db.connect();
		ResultSet res = null;
		try {
			res = db.runQuery("SELECT * FROM subject WHERE Online = TRUE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> ar = new ArrayList<String>();

		try {
			while (res.next()) {
				String name = res.getString("Name");
				ar.add(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar.toArray(new String[ar.size()]);
	}

	private String[] updateOfflineUserList() {

		DatabaseInterface db = new DatabaseInterface();
		db.connect();
		ResultSet res = null;
		try {
			res = db.runQuery("SELECT * FROM subject WHERE Online = FALSE");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> ar = new ArrayList<String>();

		try {
			while (res.next()) {
				String name = res.getString("Name");
				ar.add(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar.toArray(new String[ar.size()]);
	}

	private String[] updateUserSessions() {

		DatabaseInterface db = new DatabaseInterface();
		db.connect();
		ResultSet res = null;
		try {
			res = db.runQuery("SELECT * FROM session");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> ar = new ArrayList<String>();

		try {
			while (res.next()) {
				String sessionID = "Session " + res.getString("SessionID");
				ar.add(sessionID);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar.toArray(new String[ar.size()]);
	}
}
