package parser;

import model.sceneObjects.EagleBoard;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * The interface Xml parser.
 */
public interface IXmlParser {

    /**
     * Read file.
     *
     * @param file the file
     * @throws ParserConfigurationException the parser configuration exception
     * @throws IOException                  the io exception
     * @throws SAXException                 the sax exception
     */
    void readFile(File file) throws ParserConfigurationException, IOException, SAXException;

    /**
     * Write file boolean.
     *
     * @param board the board
     * @return the boolean
     */
    boolean writeFile(EagleBoard board);
}
