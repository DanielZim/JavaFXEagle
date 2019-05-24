package parser;

import model.sceneObjects.EagleBoard;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * The type Eagle xml parser.
 */
public class EagleXmlParser implements IXmlParser {

    private EagleXmlReader reader;
    private EagleXmlWriter writer;

    /**
     * Instantiates a new Eagle xml parser.
     */
    public EagleXmlParser() {
        this.reader = new EagleXmlReader();
        this.writer = new EagleXmlWriter();
    }

    @Override
    public void readFile(File file) throws ParserConfigurationException, IOException, SAXException {
        this.reader.readFile(file);
    }

    @Override
    public boolean writeFile(EagleBoard board) {
        return this.writer.writeFile(board);
    }

    /**
     * Gets board.
     *
     * @return the board
     */
    public EagleBoard getBoard() {
        return reader.getBoard();
    }
}
