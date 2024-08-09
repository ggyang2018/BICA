package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistent.BookData;
import tech.sunnykit.guikit.widget.GUIWidgetAdaptor;
import tech.sunnykit.guikit.widget.XYLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

public class SortRecordDlg extends JDialog implements ActionListener
{
    @Serial
    static private final long  serialVersionUID = GUIWidgetAdaptor.serialVersionUID;
    private static final Logger logger = LogManager.getLogger(ParserFrame.class);

    int wid, hgt;
    ParserFrame frame;
    JButton sortBtn, cancelBtn;
    JRadioButton bookRdoBtn, invRdoBtn, ascentRdobtn, descentRdoBtn;
    JComboBox<String> sortFieldBox;
    final String[] fields = {"Invoice Number", "Event Title"};


    boolean isAscent; //descent
    boolean isBook; //invoice
    String sortStr;

    public SortRecordDlg(ParserFrame frm) {
        super(frm);
        frame = frm;
        setModal(true);
        setTitle("Sorting Records");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void setFavourBounds(int width, int height)
    {
        wid = width; hgt = height;
        Dimension dim = Toolkit.getDefaultToolkit( ).getScreenSize();
        float fx = (float)dim.getWidth( )/2;
        float fy = (float)dim.getHeight( )/2;
        int x = Math.round(fx - (float)(width/2));
        int y = Math.round(fy - (float)(height/2))-15;
        setBounds(x, y, width, height);
    }

    public void displayDlg() {
            initDlg();
            setVisible(true);
    }

    @Override //ActionListener implementation
    public void actionPerformed(ActionEvent evt) {
        String cmd = evt.getActionCommand();
        if(cmd.equals("Close")){
            dispose();
            setVisible(false);
        }else if(cmd.equals("Sorting")){
            boolean isBook = bookRdoBtn.isSelected();
            boolean isAscend = ascentRdobtn.isSelected();
            String fld = sortFieldBox.getSelectedItem().toString();
            if(isBook){
                if(fld.equals(fields[0]))
                    frame.ctxPane.sortBookInvNumber(isAscend);
                else if(fld.equals(fields[1]))
                    frame.ctxPane.sortBookTitle(isAscend);
            }else{
                if(fld.equals(fields[0]))
                    frame.ctxPane.sortInvoiceInvNumber(isAscend);
                else if(fld.equals(fields[1]))
                    frame.ctxPane.sortInvoiceTitle(isAscend);
            }
            dispose();
            setVisible(false);
        }
    }

    //------- support methods ----------
    private void initDlg(){
        JPanel pane = new JPanel();
        pane.setLayout(new XYLayout());
        int x0=20;
        int x=x0, y=20, w=110, w1=130, h=25;
        JSeparator sp0 = new JSeparator();
        sp0.setBounds(0,y, w+w1+x0, 5);
        pane.add(sp0);
        y+=10;
        JLabel targetLb = new JLabel("Sorting Target ", JLabel.CENTER);
        targetLb.setBounds(0, y, w+w1+x0, h);
        pane.add(targetLb);
        y+=h;
        bookRdoBtn = new JRadioButton("Booking");
        bookRdoBtn.setSelected(true); //as default
        bookRdoBtn.setBounds(x, y, w, h);
        pane.add(bookRdoBtn);
        x+=w;
        invRdoBtn = new JRadioButton("Invoice");
        invRdoBtn.setBounds(x, y, w, h);
        pane.add(invRdoBtn);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(bookRdoBtn);
        group1.add(invRdoBtn);

        x=x0; y+=h;
        JSeparator sp1 = new JSeparator();
        sp1.setBounds(0,y, w+w1+x0, 5);
        pane.add(sp1);
        y+=10;
        JLabel orderLb = new JLabel("Sorting Order",JLabel.CENTER);
        orderLb.setBounds(0, y, w+w1+x0, h);
        pane.add(orderLb);
        y+=h;
        ascentRdobtn = new JRadioButton("Ascending");
        ascentRdobtn.setSelected(true);
        ascentRdobtn.setBounds(x, y, w, h);
        pane.add(ascentRdobtn);
        x+=w;
        descentRdoBtn = new JRadioButton("Descending");
        descentRdoBtn.setBounds(x, y, w, h);
        pane.add(descentRdoBtn);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(ascentRdobtn);
        group2.add(descentRdoBtn);
        x=x0; y+=h;
        JSeparator sp2 = new JSeparator();
        sp2.setBounds(0,y, w+w1+x0, 5);
        pane.add(sp2);
        y+=10; x=10;
        JLabel searchLb = new JLabel("Search Field:", JLabel.RIGHT);
        searchLb.setBounds(x, y, w, h);
        pane.add(searchLb);
        x+=w;
        sortFieldBox = new JComboBox<>(fields);
        sortFieldBox.setBounds(x, y, w1, h);
        sortFieldBox.setSelectedIndex(0);
        pane.add(sortFieldBox);
        x=x0; y+=(h+10);
        JSeparator sp3 = new JSeparator();
        sp3.setBounds(0,y, w+w1+x0, 5);
        pane.add(sp3);
        y+=15; w=80; x=60;
        sortBtn = new JButton("Sorting");
        sortBtn.addActionListener(this);
        sortBtn.setBounds(x, y, w, h);
        pane.add(sortBtn);
        x+=w;
        cancelBtn = new JButton("Close");
        cancelBtn.addActionListener(this);
        cancelBtn.setBounds(x, y, w, h);
        pane.add(cancelBtn);
        getContentPane().add(pane);
    }
}
