import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;

public class DocumentFrame extends JInternalFrame implements InternalFrameListener {

    public static final String CLASS_NAME = DocumentFrame.class.getSimpleName();
    private static final Logger LOG = Logger.getLogger(CLASS_NAME);

    private SVGDiagram svg;

    public DocumentFrame(String title, Document document) {
       super( title, true, true, true, true);

        svg = new SVGDiagram(document);
        JScrollPane scrollPane = new JScrollPane(svg);
        getContentPane().add(scrollPane);

        this.addInternalFrameListener( this);
        pack();
    }

    public Document getDocument() {
        return svg.getDocument();
    }




    @Override
    public void internalFrameOpened(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {

    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        LOG.info("ACTIVE");
        try {
            this.setSelected(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        LOG.info("NO-ACTIVE");
        try {
            this.setSelected(false);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }

    }
}
