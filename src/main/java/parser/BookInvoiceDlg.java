package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistent.BookData;
import tech.sunnykit.guikit.util.IdentityKey;
import tech.sunnykit.guikit.widget.GUIWidgetAdaptor;
import tech.sunnykit.guikit.widget.XYLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

/**
 * Inputs: total Fee and paid, purchasers number, note(address) may need character limit
 * Display: Event Title with Client Name, first date
 */

public class BookInvoiceDlg extends JDialog implements ActionListener
{
    @Serial
    static private final long  serialVersionUID = GUIWidgetAdaptor.serialVersionUID;
    private static final Logger logger = LogManager.getLogger(ParserFrame.class);

    int wid, hgt;
    ParserFrame frame;
    BookData bookData;
    MediaData pdfData;

    //input;
    JTextField purchaseFd, numberFd, feeFd, pdfNameFd;
    JTextArea  noteTxt;
    JCheckBox paidBox;
    JButton cancelBtn, goBtn;

    //display:
    String evtTitle;
    String btnCommand1, btnCommand2;

    public BookInvoiceDlg(ParserFrame frm, BookData data)
    {
        super(frm);
        frame = frm;
        setModal(true);
        setTitle("Create Invoice From Book");
        bookData = data;
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        evtTitle = String.format("%s by %s", bookData.getEventTitle(), bookData.getClientName());
        btnCommand1 = "Create Invoice";
        btnCommand2 = "Close";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.equals(btnCommand2))
        {
            dispose();
            setVisible(false);
        }else if(cmd.equals(btnCommand1))
        {
            //for storage data
            bookData.setPurchaseOrder(purchaseFd.getText());
            bookData.setInvoiceNbr(numberFd.getText());
            bookData.setPayStatus(paidBox.isSelected()? "paid" : "unpaid");
            bookData.setNewData(false);
            bookData.setBookNote(noteTxt.getText());

            pdfData.setPurchaseOrderNo(purchaseFd.getText());
            pdfData.setInvoiceNo(numberFd.getText());
            String hireFee = feeFd.getText();
            if(paidBox.isSelected())
                hireFee += "  -*(paid with thanks)";
            pdfData.setHireFee(hireFee);
            if(makeInvoicePdf()){
                dispose();
                setVisible(false);
            }

        }
    }
    //w=500, h=500
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
        if (bookData == null) dispose();
        else {
            initDlg();
            setVisible(true);
        }
    }
    //----------- support methods --------------------
    private void initDlg(){
        pdfData = bookData.makeInvoiceMediaData();
        JPanel invPane = new JPanel();
        invPane.setLayout(new XYLayout());
        int x=10, y=20, w=120, w1=200, h=25;
        JLabel titleLb = new JLabel("Event Title: "+evtTitle, JLabel.CENTER);
        titleLb.setBounds(x, y, w+w1, h);
        invPane.add(titleLb);
        y+=h;
        JLabel timeLb = new JLabel("Booking Start: "+pdfData.getFirstDate(), JLabel.CENTER);
        timeLb.setBounds(x, y, w+w1, h);
        invPane.add(timeLb);
        y+=h;
        JLabel nbrLb = new JLabel("Invoice Number:", JLabel.RIGHT);
        nbrLb.setBounds(x, y, w, h);
        invPane.add(nbrLb);
        x+=w;
        numberFd = new JTextField(pdfData.getInvoiceNo());
        numberFd.setBounds(x, y, w1, h);
        invPane.add(numberFd);
        y+=h; x=10;
        JLabel orderLb = new JLabel("Purchase Order:", JLabel.RIGHT);
        orderLb.setBounds(x, y, w, h);
        invPane.add(orderLb);
        x+=w;
        purchaseFd = new JTextField();
        purchaseFd.setBounds(x, y, w, h);
        invPane.add(purchaseFd);
        y+=h; x=10;
        JLabel feeLb = new JLabel("Total Fee:", JLabel.RIGHT);
        feeLb.setBounds(x, y, w, h);
        invPane.add(feeLb); x+=w;
        feeFd = new JTextField(pdfData.getHireFee());
        feeFd.setBounds(x, y, w, h);
        invPane.add(feeFd); x+=w;
        paidBox = new JCheckBox("Paid");
        paidBox.setBounds(x, y, w, h);
        invPane.add(paidBox);
        y+=h; x=10;
        JLabel pdfNameLb = new JLabel("File Name:", JLabel.RIGHT);
        pdfNameLb.setBounds(x, y, w, h); x+=w;
        invPane.add(pdfNameLb);
        String ikey = IdentityKey.getDateKey("Inv", 1);
        pdfNameFd = new JTextField(ikey+".pdf");
        pdfNameFd.setBounds(x, y, w1, h);
        invPane.add(pdfNameFd);
        y+=h; x=10;
        JLabel noteLb = new JLabel("Additional Note (Max 200 Chars)", JLabel.CENTER);
        noteLb.setBounds(x, y, w+w1, h);
        invPane.add(noteLb);
        y+=h;
        noteTxt = new JTextArea(bookData.getBookNote());
        noteTxt.setBounds(x, y, w+w1, 3*h);
        invPane.add(noteTxt);
        y+=(3*h+10); x=50;
        goBtn = new JButton(btnCommand1);
        goBtn.setBounds(x, y, w, h);
        goBtn.addActionListener(this);
        invPane.add(goBtn);
        x+=w;
        cancelBtn = new JButton(btnCommand2);
        cancelBtn.setBounds(x, y, w, h);
        cancelBtn.addActionListener(this);
        invPane.add(cancelBtn);
        this.getContentPane().add(invPane);
    }

    private boolean makeInvoicePdf()
    {
        boolean fg = false;
        try
        {
            String file_name = pdfNameFd.getText();
            bookData.setFileNames(file_name);
            if(file_name==null || file_name.trim().length()<2)
            {
                String tit = "Warning to Produce Invoice";
                NoticeBox box = new NoticeBox("Please add the pdf file name for processing", NoticeBox.WARNING, 300, 200);
                box.showMsg(tit);
                return fg;
            }

            String invNbr = numberFd.getText();
            if(invNbr==null || invNbr.trim().length()<2)
            {
                String tit = "Warning to Produce Invoice";
                NoticeBox box = new NoticeBox("Please add the invoice number for processing", NoticeBox.ERROR, 300, 200);
                box.showMsg(tit);
                return fg;
            }
            String file_path = frame.pdfMaker.checkFile(file_name);
            if(file_path == null)
            {
                String tit = "Warning to Produce Invoice";
                NoticeBox box = new NoticeBox("The file exists already, please change another name", NoticeBox.WARNING, 300, 200);
                box.showMsg(tit);
                return fg;
            }
            frame.pdfMaker.makeInvoice(pdfData, file_path);
            NoticeBox box1 = new NoticeBox("Successful create PDF in: "+file_path, NoticeBox.SUCCESS, 300, 200);
            String msg = null;
            if(frame.bookStore.storeInvoice(bookData)) {
                msg = "Success to create invoice PDF file and store in DB ";
                frame.bookStore.updateBookStatus(AppConstants.INVOICE_DONE_STATUS, bookData.getActionId());
            }else msg = "Failed to store invoice PDF file in DB, but create it";
            box1.showMsg(msg);
            fg = true;
        }catch (Exception ex)
        {
            NoticeBox box2 = new NoticeBox("Something wrong to create PDF. Please contact Admin", NoticeBox.ERROR, 300, 200);
            box2.showMsg("Fail to create invoice pdf file");
            ex.printStackTrace();
            logger.debug("{}", ex);
        }
        return fg;
    }

}
