package itexto.neo4jviewer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import java.awt.Color;
import java.awt.Paint;
import java.io.File;
import java.util.Iterator;
import org.apache.commons.collections15.Transformer;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * DbWrapper
 * @author kicolobo
 */
public class DbWrapper {

    private EmbeddedGraphDatabase database;

    public void closeDatabase() {
        if (getDatabase() != null) {
            getDatabase().shutdown();
            database = null;
        }
    }

    public EmbeddedGraphDatabase getDatabase() {
        return database;
    }

    public void openDatabase(File path) throws Neo4JViewerError {
        database = new EmbeddedGraphDatabase(path.getAbsolutePath());
    }

    private void processNode(Node start) throws Neo4JViewerError {
        if (start != null) {
            Iterable<Relationship> relationships = start.getRelationships();
            if (relationships != null) {
                Iterator<Relationship> rels = relationships.iterator();
                while (rels.hasNext()) {

                    Relationship rel = rels.next();
                    if (! graph.containsEdge(rel)) {
                        graph.addEdge(rel, start, rel.getEndNode());
                    }

                    if (! rel.getEndNode().equals(start)) {
                        if (! graph.containsVertex(rel.getEndNode())) {
                            graph.addVertex(rel.getEndNode());
                        }
                        processNode(rel.getEndNode());
                    }
                    
                    
                }
            }
        }
    }

    private void generateGraph() throws Neo4JViewerError {
        if (database != null) {
            graph = new SparseMultigraph<Node, Relationship>();
            Transaction tx = database.beginTx();
            try {
                Node root = database.getReferenceNode();
                graph.addVertex(root);
                processNode(root);
                tx.success();
            } catch (Throwable t) {
                graph = null;
                tx.failure();
                throw new Neo4JViewerError("Error reading graph", t);
            } finally {
                tx.finish();
            }
        }
    }

    private Graph<Node, Relationship> graph;

    public Graph<Node, Relationship> getGraph() throws Neo4JViewerError {
        if (graph == null && database != null) {
            generateGraph();
        }
        return graph;
    }

    public VisualizationViewer getViewer() throws Neo4JViewerError {
        if (getGraph() != null) {
            Layout<Node, Relationship> layout = new edu.uci.ics.jung.algorithms.layout.FRLayout<Node, Relationship>(getGraph());
            
            VisualizationViewer<Node, Relationship> viewer = new VisualizationViewer<Node, Relationship>(layout);


           


           
            

            

            DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
            gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
            viewer.setGraphMouse(gm);
            viewer.addKeyListener(gm.getModeKeyListener());
            

            viewer.getRenderContext().setVertexLabelTransformer(new Transformer<Node, String>() {

                public String transform(Node i) {
                    return Long.toString(i.getId());

                }
            });

            viewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Relationship, String>() {

                public String transform(Relationship i) {
                    return i.getType().name();
                }
            });

            
            viewer.getRenderContext().setVertexFillPaintTransformer(new Transformer<Node, Paint>(){

                public Paint transform(Node i) {
                    if (i.getId() == getDatabase().getReferenceNode().getId()) {
                        return Color.RED;
                    } else {
                        return Color.GREEN;
                    }
                }
            });

            
            

            viewer.setAutoscrolls(true);
            

            return viewer;
        }
        return null;
    }

}
