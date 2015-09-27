import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ConfigurationWindow extends JFrame {

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Variables
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final long serialVersionUID = 1L;

	// GUI variables

	private JPanel contentPane;
	private JLabel lblEnterCommand;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JButton btnSend;
	private JSplitPane splitPane;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JButton btnClear;

	// Other

	private static boolean connected;
	private String command;
	private DatabaseInterface db = new DatabaseInterface();

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Action Listeners
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ActionListener btnConnectListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			connect();
		}
	};

	private ActionListener btnDisconnectListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			disconnect();
		}
	};

	private ActionListener btnSendListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				sendCommand();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};

	private ActionListener btnClearListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			clearTextArea();
		}
	};

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Launch the application.
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConfigurationWindow frame = new ConfigurationWindow();
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

	public ConfigurationWindow() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textArea = new JTextArea(12, 40);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(10, 36, 414, 93);
		contentPane.add(scrollPane);

		lblEnterCommand = new JLabel("Enter Command");
		lblEnterCommand.setBounds(157, 11, 110, 14);
		contentPane.add(lblEnterCommand);

		btnSend = new JButton("Send");
		btnSend.setBounds(10, 140, 89, 23);
		contentPane.add(btnSend);

		splitPane = new JSplitPane();
		splitPane.setBounds(130, 226, 200, 25);
		contentPane.add(splitPane);

		btnConnect = new JButton("Connect");
		splitPane.setLeftComponent(btnConnect);

		btnDisconnect = new JButton("Disconnect");
		splitPane.setRightComponent(btnDisconnect);

		btnClear = new JButton("Clear");
		btnClear.setBounds(335, 140, 89, 23);
		contentPane.add(btnClear);

		// Listeners

		btnConnect.addActionListener(btnConnectListener);
		btnDisconnect.addActionListener(btnDisconnectListener);
		btnSend.addActionListener(btnSendListener);
		btnClear.addActionListener(btnClearListener);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Utils Methods
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void connect() {

		// DatabaseInterface db = new DatabaseInterface();
		connected = db.connect();

		// Verify connection
		System.out.println(connected);
	}

	private void disconnect() {

		// DatabaseInterface db = new DatabaseInterface();
		connected = db.disconnect();

		// Verify connection
		System.out.println(connected);
	}

	private void sendCommand() throws SQLException {

		command = new String(textArea.getText());

		System.out.println(command);

		if (!connected) {
			connected = db.connect();
		}

		// Create a query and send the data to the database
		// Example set a user offline

		ResultSet res = null;

		try {
			res = db.runQuery(command);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Sending Failed");
		}

		// Try for example the command: SELECT * FROM subject WHERE Name =
		// 'Yasser'

		while (res.next()) {

			String name = res.getString("Name");
			String weight = res.getString("Weight");
			String height = res.getString("Height");
			Date date = res.getDate("Birthday");
			String gender = res.getString("Gender");
			String online = res.getString("Online");
			System.out.println("Name: " + name);
			System.out.println("Weight: " + weight);
			System.out.println("Height: " + height);
			System.out.println("Birthday: " + date);
			System.out.println("Gender: " + gender);
			System.out.println("Online: " + online);
		}

	}

	private void clearTextArea() {

		textArea.setText(null);
	}
}
