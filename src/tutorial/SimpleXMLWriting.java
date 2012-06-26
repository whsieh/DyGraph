package tutorial;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SimpleXMLWriting {
    
    public static void main(String[] args) {

        try {
            /* This is necessary to initialize a document */
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            /* This will create a node, 'root', by invoking createElement, then use
             * the setAttribute methods to add attributes to the node  */
            Element root = doc.createElement("root");
            root.setAttribute("someRootData", "1337");
            root.setAttribute("moreRootData", "no cow level");
            doc.appendChild(root);

            /*  */
            Element firstChild = doc.createElement("child0");
            firstChild.appendChild(doc.createTextNode("this is some text"));

        } catch (Exception e) { }
    }
}

