import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sourceforge.chart2d.Chart2DProperties;
import net.sourceforge.chart2d.Dataset;
import net.sourceforge.chart2d.GraphChart2DProperties;
import net.sourceforge.chart2d.GraphProperties;
import net.sourceforge.chart2d.LBChart2D;
import net.sourceforge.chart2d.LegendProperties;
import net.sourceforge.chart2d.MultiColorsProperties;
import net.sourceforge.chart2d.Object2DProperties;

public class GraphWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GUI components
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private JPanel graphPanel;
	private JCheckBox boxECG;
	private JCheckBox boxAccl;
	private JCheckBox boxTemp;
	private JCheckBox boxHum;
	private JCheckBox boxBlood;
	private JButton refresh;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// //GRAPH PANEL PROPERTIES
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final int GRAPH_HEIGHT = 270;
	private static final int GRAPH_WIDTH = 900;
	private String mGraphTitle = "Sensor signal";
	private float mGraphRatio = 0.75f;
	// - Vertical axis
	private String mVertAxisTitle = "f(x)";
	// - Horizontal axis
	private String mHoriAxisTitle = "x";

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Other varibales
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private LBChart2D chart2D = setupGraph2D();

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Custom Listeners
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ItemListener ecgCheckBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == boxECG) {
				if (boxECG.isSelected()) {

					Dataset dataset = setupDataset(ecgDataArray);
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(dataset);

				} else {
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(new Dataset());
				}
			}
		}
	};

	private final ItemListener aclCheckBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == boxAccl) {
				if (boxAccl.isSelected()) {
					Dataset dataset = setupDataset(acclDataArray);
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(dataset);
				} else {
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(new Dataset());
				}
			}
		}
	};

	private final ItemListener tempCheckBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == boxTemp) {
				if (boxTemp.isSelected()) {
					Dataset dataset = setupDataset(tempDataArray);
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(dataset);
				} else {
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(new Dataset());
				}
			}
		}
	};

	private final ItemListener humCheckBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == boxHum) {
				if (boxHum.isSelected()) {
					Dataset dataset = setupDataset(humDataArray);
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(dataset);
				} else {
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(new Dataset());
				}
			}
		}
	};

	private ItemListener bloodCheckBoxListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == boxBlood) {
				if (boxBlood.isSelected()) {
					Dataset dataset = setupDataset(bloodDataArray);
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(dataset);
				} else {
					chart2D.removeDataset(chart2D.getDataset(0));
					chart2D.addDataset(new Dataset());
				}
			}
		}
	};

	private ActionListener btnRefreshListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			addChart(graphPanel, chart2D);

		}
	};

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Launch the application.
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphWindow frame = new GraphWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Create the frame.
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public GraphWindow() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1008, 460);
		getContentPane().setLayout(null);

		graphPanel = new JPanel();
		graphPanel.setBounds(10, 11, 957, 270);
		getContentPane().add(graphPanel);

		boxECG = new JCheckBox("ECG");
		boxECG.setBounds(10, 300, 97, 23);
		getContentPane().add(boxECG);

		boxAccl = new JCheckBox("Accelerometer");
		boxAccl.setBounds(10, 326, 97, 23);
		getContentPane().add(boxAccl);

		refresh = new JButton("Refresh");
		refresh.setBounds(10, 388, 89, 23);
		getContentPane().add(refresh);

		boxTemp = new JCheckBox("Temperature");
		boxTemp.setBounds(10, 352, 97, 23);
		getContentPane().add(boxTemp);

		boxHum = new JCheckBox("Humidity");
		boxHum.setBounds(109, 300, 97, 23);
		getContentPane().add(boxHum);

		boxBlood = new JCheckBox("Blood Pressure");
		boxBlood.setBounds(109, 326, 97, 23);
		getContentPane().add(boxBlood);

		chart2D = setupGraph2D();
		addChart(graphPanel, chart2D);

		// Action Listeners

		boxECG.addItemListener(ecgCheckBoxListener);
		boxAccl.addItemListener(aclCheckBoxListener);
		boxTemp.addItemListener(tempCheckBoxListener);
		boxHum.addItemListener(humCheckBoxListener);
		boxBlood.addItemListener(bloodCheckBoxListener);

		refresh.addActionListener(btnRefreshListener);

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// / CHART CREATION
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// <-- Begin Chart2D configuration -->

	private LBChart2D setupGraph2D() {

		LBChart2D chart = new LBChart2D();

		chart.setObject2DProperties(setupObject2D());
		chart.setChart2DProperties(setupChart2D());
		chart.setLegendProperties(setupLegend());
		chart.setGraphChart2DProperties(setupGraphChart());
		chart.addGraphProperties(setupGraph());
		chart.addDataset(new Dataset());
		chart.addMultiColorsProperties(setupMultiColors());

		chart.setPreferredSize(new Dimension(GRAPH_WIDTH, GRAPH_HEIGHT));

		chart.validate(true);

		chart.setVisible(true);

		return chart;
	}

	private Object2DProperties setupObject2D() {
		Object2DProperties objectProp = new Object2DProperties();

		// Setup the graph title
		if (mGraphTitle != null) {
			if (!mGraphTitle.equals("")) {
				objectProp.setObjectTitleText(mGraphTitle);
				objectProp.setObjectTitleExistence(true);
			} else {
				objectProp.setObjectTitleExistence(false);
			}
		} else {
			objectProp.setObjectTitleExistence(false);
		}

		// Thats all for now

		return objectProp;
	}

	private Chart2DProperties setupChart2D() {
		Chart2DProperties chartProp = new Chart2DProperties();

		// Thats all for now

		return chartProp;
	}

	private LegendProperties setupLegend() {
		LegendProperties legendProp = new LegendProperties();

		legendProp.setLegendBorderExistence(false);

		// Thats all for now

		return legendProp;
	}

	private GraphChart2DProperties setupGraphChart() {
		GraphChart2DProperties graphChartProp = new GraphChart2DProperties();

		// ratio dedicated to the graph
		graphChartProp.setChartGraphableToAvailableRatio(mGraphRatio);

		// x axis
		String[] labelText = new String[timeArray.length];

		for (int i = 0; i < labelText.length; i++) {
			labelText[i] = timeArray[i].toString();
		}

		graphChartProp.setLabelsAxisLabelsTexts(labelText);
		graphChartProp.setLabelsAxisTitleText(mHoriAxisTitle);

		// y axis
		graphChartProp.setNumbersAxisTitleText(mVertAxisTitle);

		graphChartProp
				.setLabelsAxisTicksAlignment(GraphChart2DProperties.CENTERED);

		return graphChartProp;
	}

	private GraphProperties setupGraph() {
		GraphProperties graphProp = new GraphProperties();

		graphProp.setGraphLinesExistence(true);
		graphProp.setGraphBarsExistence(false);
		graphProp.setGraphDotsExistence(false);
		graphProp.setGraphAllowComponentAlignment(true);

		return graphProp;
	}

	private MultiColorsProperties setupMultiColors() {
		MultiColorsProperties multiColorsProp = new MultiColorsProperties();

		return multiColorsProp;
	}

	// <-- End Chart2D configuration -->

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// UTILS
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void addChart(JPanel panel, LBChart2D chart) {

		panel.removeAll();
		panel.add(chart);
		panel.setVisible(true);
		panel.updateUI();
	}

	private Dataset setupDataset(Float[] anArray) {
		Dataset dataset = new Dataset();

		dataset.setSize(1, 1, anArray.length);

		for (int aSample = 0; aSample < anArray.length; aSample++) {
			dataset.set(0, 0, aSample, anArray[aSample]);
		}

		return dataset;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Sample data Queries will be added later
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Timestamp[] timeArray = { new Timestamp(10000),
			new Timestamp(20000), new Timestamp(30000), new Timestamp(40000),
			new Timestamp(50000), new Timestamp(60000), new Timestamp(70000),
			new Timestamp(80000) };

	private static Float[] ecgDataArray = { 0f, 0f, 64f, -15f, 0f, 0f, 64f,
			-15f };

	private static Float[] acclDataArray = { 30f, 30f, 30f, 40f, 45f, 40f, 20f,
			10f };
	private static Float[] tempDataArray = { 37f, 37f, 37f, 37f, 37f, 37f, 37f,
			37f };
	private static Float[] humDataArray = { 128f, 160f, 162f, 150f, 140f, 130f,
			170f, 100f };
	private static Float[] bloodDataArray = { 67f, 45f, 30f, 40f, 20f, 70f,
			20f, 10f };
}
