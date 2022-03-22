import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.w3c.dom.Document;

import javax.swing.JComponent;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SVGDiagram extends JComponent {

    public static final String CLASS_NAME = SVGDiagram.class.getSimpleName();

    public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private Document svgDocument; // Documento DOM con los componentes del dibujo
    private Element svgRoot; // Elemento raiz (SVG)
    private int svgW; // ancho del dibujo
    private int svgH;  // alto del dibujo
    private Properties webColors;

    public SVGDiagram(Document svgDoc) {
        super();

        svgDocument = svgDoc;

        svgRoot = svgDocument.getDocumentElement();

        // establecer dimensiones del dibujo
        svgW = Integer.parseInt( svgRoot.getAttribute("width") );
        svgH = Integer.parseInt( svgRoot.getAttribute("height") );

        // establecer colorCode de fondo
        this.setBackground( Color.white );

        loadColors();
    }

    public Document getDocument() {
        return svgDocument;
    }

    private void loadColors() {
        try {

            String userDir = System.getProperty("user.dir");
            FileReader reader = new FileReader(userDir + "/colors.properties");

            webColors = new Properties();
            webColors.load(reader);

        } catch (FileNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(svgW, svgH);
    }

    @Override
    public void paintComponent(Graphics g) {
        // Pinta el dibujo cada vez que se requiera
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Pintar el fondo del dibujo
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());

        NodeList list = null;

        // Obtener cada una de las figuras del dibujo
        list = svgRoot.getChildNodes();

        int n = list.getLength();
        Element element = null;
        for (int i = 0; i < n; i++) {
            Node nodo = list.item(i);

            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                element = (Element) nodo;

                // Qué tipo de figura es?
                String name = element.getTagName();

                if (name.equals("line")) {
                    // dibujar una linea
                    drawLine(element, g);
                }

                if (name.equals("rect")) {
                    // dibujar una linea
                    drawRect(element, g);
                }

                if (name.equals("circle")) {
                    // dibujar una linea
                    drawCircle(element, g);
                }
            }
        }
    }

    private void drawCircle(Element circle, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // coordenadas
        String value = circle.getAttribute("cx");
        double x = 0.0;
        if (!value.isEmpty()) {
            x = Double.parseDouble(value);
        }

        value = circle.getAttribute("cy");
        double y = 0.0;
        if (!value.isEmpty()) {
            y = Double.parseDouble(value);
        }

        value = circle.getAttribute("r");
        double r = 0.0;
        if (!value.isEmpty()) {
            r = Double.parseDouble(value);
        }
        boolean fill = false;
        if (circle.hasAttribute("fill")) {
            // Especificar colorCode de linea
            String colorCode = circle.getAttribute("fill");

            if (!colorCode.equals("none")) {
                Color fillColor = webColor(colorCode, 1);
                g2d.setColor(fillColor);
                fill = true;
            }

        } else {
            // colorCode por default
            g2d.setColor(Color.BLACK);
            fill = true;
        }

        if (fill) {
            g2d.fill(new Ellipse2D.Double(x - r, y - r, r * 2, r * 2));
        }

        if (circle.hasAttribute("stroke")) {
            // Especificar colorCode de linea
            String colorCode = circle.getAttribute("stroke");
            //System.out.println(colorCode);
            g2d.setColor(webColor(colorCode, 1));

            if (circle.hasAttribute("stroke-width")) {
                // Especificar grosor de linea
                float sw = Float.parseFloat(circle.getAttribute("stroke-width"));
                g2d.setStroke(new BasicStroke(sw));
            } else {
                // Grosor por default: 1 pixel
                g2d.setStroke(new BasicStroke(1));
            }
            g2d.draw(new Ellipse2D.Double(x - r, y - r, r * 2, r * 2));
        }
    }

    private void drawRect(Element rect, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //System.out.println(rect.getAttribute("id"));
        // coordenadas
        String value = rect.getAttribute("x");

        double x = 0.0;
        if (!value.isEmpty()) {
            x = Double.parseDouble(value);
        }

        value = rect.getAttribute("y");
        double y = 0.0;
        if (!value.isEmpty()) {
            y = Double.parseDouble(value);
        }

        value = rect.getAttribute("width");
        double width = 10.0;
        if (!value.isEmpty()) {
            width = Double.parseDouble(value);
        }

        value = rect.getAttribute("height");
        double height = 10.0;
        if (!value.isEmpty()) {
            height = Double.parseDouble(value);
        }

        double rx = 0.0;
        value = rect.getAttribute("rx");
        if (!value.isEmpty()) {
            rx = Double.parseDouble(value);
            // System.out.println(rx);
        }

        value = rect.getAttribute("ry");
        double ry = 0.0;
        if (!value.isEmpty()) {
            ry = Double.parseDouble(value);
        }

        //g2d.draw(new Rectangle2D.Double(x,y,width,height) );
        boolean fill = false;
        if (rect.hasAttribute("fill")) {
            // Especificar colorCode de linea
            String colorCode = rect.getAttribute("fill");
            //System.out.println(colorCode);
            if (!colorCode.equals("none")) {
                g2d.setColor(webColor(colorCode, 1));
                fill = true;
            }

        } else {
            // colorCode por default
            g2d.setColor(Color.BLACK);
            fill = false;
        }

        if (fill) {
            g2d.fill(new RoundRectangle2D.Double(x, y, width, height, rx, ry));
        }

        if (rect.hasAttribute("stroke")) {
            // Especificar colorCode de linea
            String colorCode = rect.getAttribute("stroke");
            //System.out.println(colorCode);
            g2d.setColor(webColor(colorCode, 1));

            if (rect.hasAttribute("stroke-width")) {
                // Especificar grosor de linea
                float sw = Float.parseFloat(rect.getAttribute("stroke-width"));
                g2d.setStroke(new BasicStroke(sw));
            } else {
                // Grosor por default: 1 pixel
                g2d.setStroke(new BasicStroke(1));
            }
            g2d.draw(new RoundRectangle2D.Double(x, y, width, height, rx, ry));
        }
    }

    private void drawLine(Element line, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        //System.out.println(line.getAttribute("id"));

        if ( line.hasAttribute("stroke-width") ) {
            // Especificar grosor de linea
            float sw = Float.parseFloat(line.getAttribute("stroke-width"));
            g2d.setStroke( new BasicStroke(sw) );
        } else {
            // Grosor por default: 1 pixel
            g2d.setStroke(new BasicStroke(1));
        }

        if (line.hasAttribute("stroke")) {
            // Especificar colorCode de linea
            String colorCode = line.getAttribute("stroke");
            //System.out.println(colorCode);
            g2d.setColor( webColor(colorCode, 1) );

        } else {
            // colorCode por default
            g2d.setColor(Color.BLACK);
        }

        // coordenadas de la linea
        int x1 = (int) Double.parseDouble(line.getAttribute("x1"));
        int y1 = (int) Double.parseDouble(line.getAttribute("y1"));

        int x2 = (int) Double.parseDouble(line.getAttribute("x2"));
        int y2 = (int) Double.parseDouble(line.getAttribute("y2"));

        // g2d.drawLine(x1, y1, x2, y2);
        g2d.draw(new Line2D.Double(x1, y1, x2, y2));

    }

    /*
    Returns drawing/fill color built from WWW/HTML color specification
     */
    private Color webColor(String colorString, float opacity) {
        String colorCode = colorString.toLowerCase();
        Color newColor = Color.BLACK;

        if (colorCode.startsWith("#")) {
            // Codigo de color "#00ffff"
            colorCode = colorCode.substring(1);
        } else if (colorCode.startsWith("0x")) {
            colorCode = colorCode.substring(2);
        } else if (colorCode.startsWith("rgb")) {
            // Codigo de color "rgb(00,255,0)"
            if (colorCode.startsWith("(", 3)) {
                return Color.BLACK;
            } else if (colorCode.startsWith("a(", 3)) {
                return Color.BLACK;
            }
        } else {
            // nombre de color e.g. "red
            //Color namedColor = Color.getColor(colorString);
            // System.out.println("<" + colorCode);
            LOGGER.info(colorCode);
            colorCode = webColors.getProperty(colorCode).substring(1).trim();
        }

        try {
            int r;
            int g;
            int b;
            int a;
            int len = colorCode.length();

            // Codigo de color "#00ffff"
            if (len == 6) {
                //System.out.println(colorCode);
                r = Integer.parseInt(colorCode.substring(0, 2), 16);
                g = Integer.parseInt(colorCode.substring(2, 4), 16);
                b = Integer.parseInt(colorCode.substring(4, 6), 16);
                newColor = new Color(r, g, b);
            }
        } catch (NumberFormatException nfe) {
            LOGGER.severe(nfe.getMessage());
        }
        return newColor;
    }
}



