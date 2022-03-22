import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Util {

    public static void saveDocument(Document doc, String fileName) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);

            FileWriter writer = new FileWriter( new File(fileName) );
            StreamResult result = new StreamResult(writer);
            transformer.transform( source, result );
        } catch (TransformerConfigurationException | IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void scaleSVG( Document document, double scaleFactor) {

        Element root = document.getDocumentElement();

        double w = Double.parseDouble( root.getAttribute("width") ) * scaleFactor ;
        root.setAttribute("width",String.valueOf(w));

        double h = Double.parseDouble( root.getAttribute("height")  ) * scaleFactor  ;
        root.setAttribute("height",String.valueOf(h));

        NodeList shapes = root.getChildNodes();

        int len = shapes.getLength();

        for (int index =0; index < len; index++) {
            Node current = shapes.item(index);
            if( current.getNodeType() != Node.ELEMENT_NODE) {
               continue;
            }
            Element shape = (Element) current;

            String shapeName = shape.getNodeName();
            if( shapeName.equals("line") ) {
                double x1 = Double.parseDouble(  shape.getAttribute("x1")  ) * scaleFactor;
                shape.setAttribute("x1", String.valueOf(x1) );

                double y1 = Double.parseDouble(  shape.getAttribute("y1")  ) * scaleFactor;
                shape.setAttribute("y1",String.valueOf(y1));

                double x2 = Double.parseDouble(  shape.getAttribute("x2")  ) * scaleFactor;
                shape.setAttribute("x2",String.valueOf(x2));

                double y2 = Double.parseDouble(  shape.getAttribute("y2")  ) * scaleFactor;
                shape.setAttribute("y2",String.valueOf(y2));
            }

            if( shapeName.equals("circle") ) {
                double x1 = Double.parseDouble(  shape.getAttribute("cx")  ) * scaleFactor;
                shape.setAttribute("cx",String.valueOf(x1));

                double y1 = Double.parseDouble(  shape.getAttribute("cy")  ) * scaleFactor;
                shape.setAttribute("cy",String.valueOf(y1));

                double r = Double.parseDouble(  shape.getAttribute("r")  ) * scaleFactor;
                shape.setAttribute("r",String.valueOf(r));
            }

            if( shapeName.equals("rect") ) {
                double x = Double.parseDouble(  shape.getAttribute("x")  ) * scaleFactor;
                shape.setAttribute("x",String.valueOf(x));

                double y = Double.parseDouble(  shape.getAttribute("y")  ) * scaleFactor;
                shape.setAttribute("y",String.valueOf(y));

                double width = Double.parseDouble( shape.getAttribute("width") ) * scaleFactor ;
                shape.setAttribute("width",String.valueOf(width));

                double height = Double.parseDouble( shape.getAttribute("height")  ) * scaleFactor  ;
                shape.setAttribute("height",String.valueOf(height));
            }
        }

    }

    public static void rotateSVG(Document document, double dg) {
        Element root = document.getDocumentElement();

        int w = Integer.parseInt( root.getAttribute("width") );

        int h = Integer.parseInt( root.getAttribute("height") );


        // Obtener los circulos
        NodeList lines = root.getElementsByTagName("line");

        int len = lines.getLength();

        Point center = new Point(w / 2,h / 2 );

        for (int i = 0; i < len; i++) {
            Element line = (Element) lines.item(i);

            double x1 = Double.parseDouble(line.getAttribute("x1"));
            double y1 = Double.parseDouble(line.getAttribute("y1"));

            Point p1 = new Point();
            p1.setLocation(x1, y1);
            rotate(p1, center, dg);

            line.setAttribute("x1", String.valueOf(p1.getX()));
            line.setAttribute("y1", String.valueOf(p1.getY()));

            double x2 = Double.parseDouble(line.getAttribute("x2"));
            double y2 = Double.parseDouble(line.getAttribute("y2"));

            Point p2 = new Point();
            p2.setLocation(x2, y2);
            rotate(p2, center, dg);

            line.setAttribute("x1", String.valueOf(p1.getX()));
            line.setAttribute("y1", String.valueOf(p1.getY()));

            line.setAttribute("x2", String.valueOf(p2.getX()));
            line.setAttribute("y2", String.valueOf(p2.getY()));

        }

    }

    public static void rotate(Point point, double degrees) {
        double newX = point.getX() * Math.cos(Math.toRadians(degrees)) -
                point.getY() * Math.sin(Math.toRadians(degrees));

        double newY = point.getX() * Math.sin(Math.toRadians(degrees)) +
                point.getY() * Math.cos(Math.toRadians(degrees));

        point.setLocation(newX, newY);
    }

    public static void rotate(Point p,Point c, double degrees) {
        p.x -= c.x;
        p.y -= c.y;

        double newX = p.x * Math.cos( Math.toRadians(degrees) ) -
                p.y * Math.sin( Math.toRadians(degrees) );

        double newY = p.x * Math.sin( Math.toRadians(degrees) ) +
                p.y * Math.cos( Math.toRadians(degrees) );

        p.setLocation(newX+c.x, newY+c.y);
    }


    public double[] matXvect(double m[][], double v[]) {
        int cols = m[0].length;
        double a[] = null;


        if( cols == v.length) {
            a = new double[v.length];
            for(int i=0; i < cols; i++ ) {
                double sum = 0.0;
                for( int j=0; j < cols; j++) {
                    sum = sum + m[i][j] * v[j];
                }
                a[i] = sum;
            }

        }
        return  a;
    }


}
