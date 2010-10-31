/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itexto.neo4jviewer;

/**
 * Neo4JViewerError
 * @author kicolobo
 */
public class Neo4JViewerError extends Exception {

    public Neo4JViewerError(String msg) {
        super(msg);
    }

    public Neo4JViewerError(String msg, Throwable t) {
        super(msg, t);
    }

}
