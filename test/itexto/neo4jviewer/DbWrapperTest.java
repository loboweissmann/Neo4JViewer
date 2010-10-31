/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package itexto.neo4jviewer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JDialog;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import static org.junit.Assert.*;

/**
 *
 * @author kicolobo
 */
public class DbWrapperTest {

    public DbWrapperTest() {
    }

    private String path = System.getProperty("user.home") + "/neo4j.twitjava";

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of closeDatabase method, of class DbWrapper.
     */
    @Test
    public void testCloseDatabase() throws Exception {
        System.out.println("closeDatabase");
        DbWrapper instance = new DbWrapper();
        instance.openDatabase(new File(path));
        assertNotNull(instance.getDatabase());
        instance.closeDatabase();
        assertNull(instance.getDatabase());
    }

    /**
     * Test of getDatabase method, of class DbWrapper.
     */
    @Test
    public void testGetDatabase() {
        System.out.println("getDatabase");
        DbWrapper instance = new DbWrapper();
        assertNull(instance.getDatabase());
    }

    /**
     * Test of openDatabase method, of class DbWrapper.
     */
    @Test
    public void testOpenDatabase() throws Exception {
        System.out.println("openDatabase");
        
        DbWrapper instance = new DbWrapper();
        instance.openDatabase(new File(path));
        assertNotNull(instance.getDatabase());
        assertEquals(instance.getDatabase().getStoreDir(), path);
        instance.closeDatabase();
        assertNull(instance.getDatabase());
    }

    /**
     * Test of getGraph method, of class DbWrapper.
     */
    @Test
    public void testGetGraph() throws Exception {
        System.out.println("getGraph");
        DbWrapper instance = new DbWrapper();
        instance.openDatabase(new File(path));
        try {
            Graph<Node, Relationship> result = instance.getGraph();
            assertNotNull(result);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
        finally {
            instance.closeDatabase();
        }
    }

    @Test
    public void testGetViewer() throws Exception {
        System.out.println("getViewer");
        DbWrapper instance = new DbWrapper();
        instance.openDatabase(new File(path));

        BasicVisualizationServer server = instance.getViewer();
        assertNotNull(server);
        JDialog form = new JDialog();
        form.setModal(true);
        form.setLayout(new java.awt.BorderLayout());
        form.add(server, BorderLayout.CENTER);

        form.setSize(500, 500);
        while (form.isVisible()) {

        }
        form.setVisible(true);
        instance.closeDatabase();
    }

}