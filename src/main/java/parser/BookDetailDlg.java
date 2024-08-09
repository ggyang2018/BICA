package parser;

import persistent.BookData;
import tech.sunnykit.guikit.widget.GTableModel;
import tech.sunnykit.guikit.widget.GTablePane;
import tech.sunnykit.guikit.widget.GUIWidgetAdaptor;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.*;
import java.util.List;

public class BookDetailDlg  extends JDialog implements ActionListener
{
    @Serial
    static private final long  serialVersionUID = GUIWidgetAdaptor.serialVersionUID;
    int wid, hgt;
    ParserFrame mainFrame;
    //component
    JPanel pane;
    JButton closeBtn;
    GTablePane  tablePane;
    BookData bookData;
    String[] header ={"Book Time", "Book Space", "Price", "Duration"};
    Integer[] columnSizes = {250, 180, 100, 100};

    public BookDetailDlg(ParserFrame frm, BookData bookData)
    {
        super(frm);
        setModal(true);
        setTitle("Booking Detail List");
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.bookData = bookData;
        tablePane = new GTablePane(3);
        closeBtn = new JButton("Close");
        closeBtn.addActionListener(this);
        pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(tablePane, BorderLayout.CENTER);
        pane.add(closeBtn, BorderLayout.SOUTH);
        getContentPane().add(pane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        this.setVisible(false);
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

    public void displayDlg()
    {
        if(bookData == null) dispose();
        List<String> hd = Arrays.asList(header);
        List<List<String>> table_data = new ArrayList<>();
        List<String> timeList = bookData.getTimeList();
        List<String> spaceList = bookData.getSpaceList();
        List<String> priceList = bookData.getPriceList();
        List<String> durationList = bookData.getDurationList();
        for(int i=0; i<timeList.size(); i++)
        {
            List<String> row = new ArrayList<>();
            row.add(timeList.get(i));
            row.add(spaceList.get(i));
            row.add(priceList.get(i));
            row.add(durationList.get(i));
            table_data.add(row);
        }
        GTableModel md = new GTableModel(table_data, hd);
        List<Integer> sizes = Arrays.asList(columnSizes);
        md.setColumnSizes(sizes);
        tablePane.setMainTable(md, false);
        tablePane.updateUI();
        this.setVisible(true);
    }
}
