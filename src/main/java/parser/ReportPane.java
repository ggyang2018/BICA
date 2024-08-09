package parser;
/**
 * the sample code site: https://zetcode.com/java/jfreechart/
 * JFreeChart API: https://www.jfree.org/jfreechart/javadoc/index.html
 * @author Guang Yang: Sunny
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import tech.sunnykit.guikit.gui.MainGuiFrame;
import tech.sunnykit.guikit.widget.XYLayout;

public class ReportPane extends JPanel
{
	private static final long serialVersionUID = MainGuiFrame.serialVersionUID;
	//data
	int totalTime = 0;
	float totalPay = 0.0f;
	String period;
	Color navyBlue;
	
	Color pvtColor, comColor, uniColor, chuColor, chumColor, youColor, othColor;
	//components
	boolean isLoad;
	JLabel periodLb;
	
	
	public ReportPane() {
		super();
		isLoad = false;
		navyBlue = new Color(0, 0, 52);
		setLayout(new XYLayout());
		pvtColor = new Color(170, 255, 195); //Mint: 170, 255, 195, #aaffc3
		comColor = new Color(170, 110, 40); //	Brown:  170, 110, 40;  #9A6324
		uniColor = new Color(128, 128, 0); //Olive: 128, 128, 0; #808000
		chuColor = new Color(0, 128, 128); //Teal: 0, 128, 128; #469990 
		chumColor = new Color(210, 245, 60);//Lime: 210, 245, 60; #bfef45
		youColor =  new Color(240, 50, 230); //Magenta: 240, 50, 230; #f032e6 --> light pink
		othColor = new Color(0, 0, 128); //Navy: 0, 0, 128; #000075
				
	}
	
	public void setTotalTime(int total_time) { totalTime = total_time; }
	public void setTotalPay(float total_pay) { totalPay = total_pay; }
	public void setPeriod(String periodStr) { period = periodStr; }
	public boolean hasLoad() { return isLoad; }
	
	public void composePieChart(Map<String, Integer> timeMap, Map<String, Float> payMap) {
		//remove all components
		Component[] components = getComponents();
		if (components !=null && components.length >0) {
			removeAll();
			revalidate();
			repaint();
		}
				
		DefaultPieDataset<String> dataset1 = createDataset1(timeMap);
		if (dataset1 !=null) {
			JFreeChart chart1 = createChart(dataset1, "Proportion of Durations for Each Group");
			ChartPanel chartPanel = new ChartPanel(chart1);
			chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			chartPanel.setBackground(Color.white);
			chartPanel.setBounds(10, 20, 600, 480);
			add(chartPanel);
		}
		 
		DefaultPieDataset<String> dataset2 = createDataset2(payMap);
		if(dataset2 !=null) {
			JFreeChart chart2 = createChart(dataset2, "Proportion of Income for Each Group");
			ChartPanel chartPanel2 = new ChartPanel(chart2);
			chartPanel2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			chartPanel2.setBackground(Color.white);
			chartPanel2.setBounds(615, 20, 600, 480);
			add(chartPanel2);
		}
		int total_hours = totalTime/60;  //round up 
		int residual = totalTime % 60;
		
		if(residual >= 45) total_hours++;
		else if(residual <=15) residual = 0;
		else residual = 30;
		
		String hstr = null;
		if(residual >0)
			hstr = String.format("Total Times: %d hours %d minutes; ", total_hours, residual);
		else
			hstr = String.format("Total Times: %d hours; ", total_hours);
		
		
		int totPay = Math.round(totalPay);
		String pstr = String.format("Total income: £%d", totPay);
		
		periodLb = new JLabel(period+hstr+pstr, JLabel.CENTER);
		periodLb.setFont(new Font("Dialog", Font.BOLD+Font.ITALIC, 14));
		periodLb.setBounds(20, 510, 1000, 35);
		add(periodLb);
		isLoad = true;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//BufferedImage bimg = createImage(this);
		//if (bimg != null) 
           // g.drawImage(bimg, 5, 5, getWidth()-5, getHeight()-25, 
           	//	 	 0, 0, bimg.getWidth(), bimg.getHeight(), navyBlue, this); 
	}
	
	//create buffered image
	public BufferedImage createImage(JPanel panel) {

	    int w = 1200;
	    int h = 500;
	    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = bi.createGraphics();
	    panel.print(g); //using print instead of paint(g)
	    g.dispose();
	    return bi;
	}
	
	
	//  ----- support mehtods ----------------
	private DefaultPieDataset<String> createDataset1(Map<String, Integer>mp) {
		if(mp==null || mp.size()<5) return null;

		DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
        mp.forEach((key, value) ->dataset.setValue(key, value));  
        return dataset;
    }

	
	private DefaultPieDataset<String> createDataset2(Map<String, Float>mp) {
		if(mp==null || mp.size()<5) return null;

		DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
        mp.forEach((key, value) ->dataset.setValue(key, value));  
        return dataset;
    }
	
	
    private JFreeChart createChart(DefaultPieDataset<String> dataset, String title) {

    	JFreeChart pieChart = ChartFactory.createPieChart(
    			title,
                dataset,
                true, false, false);
    	@SuppressWarnings("unchecked")
		PiePlot<String> plot = (PiePlot<String>)pieChart.getPlot();
    	
    	//change tooltip or label
    	StandardPieSectionLabelGenerator labelGen = new StandardPieSectionLabelGenerator(
                "{0} ({2})", new DecimalFormat("0"), new DecimalFormat("0%")) {
    		private static final long serialVersionUID = 1L;

			@SuppressWarnings("rawtypes")
			@Override    //return super.generateSectionLabel(dataset, key); without tooltip
            public String generateSectionLabel(PieDataset dataset, Comparable key) { return null;}
        }; 	 	
    	plot.setLabelGenerator(labelGen);      
        
    	//change legend colors
    	List<String> keyList = dataset.getKeys();
    	String key1 = keyList.get(0);
    	plot.setSectionPaint(key1, pvtColor);
    	String key2 = keyList.get(1);
    	plot.setSectionPaint(key2, comColor);
    	String key3 = keyList.get(2);
    	plot.setSectionPaint(key3, uniColor);
    	String key4 = keyList.get(3);
    	plot.setSectionPaint(key4, chuColor);
    	String key6 = keyList.get(4);
    	plot.setSectionPaint(key6, youColor);
    	String key7 = keyList.get(5);
    	plot.setSectionPaint(key7, othColor);
    	    	
    	return pieChart;
    }

}
