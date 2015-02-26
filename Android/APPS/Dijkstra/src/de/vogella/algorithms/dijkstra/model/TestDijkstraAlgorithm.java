package de.vogella.algorithms.dijkstra.model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.vogella.algorithms.dijkstra.model.Edge;
import de.vogella.algorithms.dijkstra.model.Graph;
import de.vogella.algorithms.dijkstra.model.Vertex;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestDijkstraAlgorithm {

  private List<Vertex> nodes;
  private List<Edge> edges;
  
	
	static Point path0= new Point(161, 715);
	static Point path1= new Point(527, 674);
	static Point path2= new Point(604, 696);
	static Point path3= new Point(637, 670);
	static Point path4= new Point(818, 645);
	static Point path5= new Point(1124, 390);
	static Point path6= new Point(1618, 553);
	static Point path7= new Point(1331, 842);
	static Point path8= new Point(818, 645);

	static List<Point> path = new ArrayList<Point>();
	
	static{
		path.add(path0);
		path.add(path1);
		path.add(path2);
		path.add(path3);
		path.add(path4);
		path.add(path5);
		path.add(path6);
		path.add(path7);
		path.add(path8);

	}

  @Test
  public void testExcute() {
    nodes = new ArrayList<Vertex>();
    edges = new ArrayList<Edge>();
    for (int i = 0; i < path.size(); i++) {
      Vertex location = new Vertex("" + i, "Node_" + i, path.get(i));
      nodes.add(location);
    }

    addLane("Edge_0", 0,1,1);
    addLane("Edge_1", 1,2,1);
    addLane("Edge_2", 2,3,1);
    addLane("Edge_3", 3,4,1);
    addLane("Edge_4", 4,5,2);
    addLane("Edge_5", 4,7,4);
    addLane("Edge_6", 5,6,4);
    addLane("Edge_7", 6,7,2);
    addLane("Edge_8", 7,4,4);

    // Lets check from location Loc_1 to Loc_10
    Graph graph = new Graph(nodes, edges);
    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
    dijkstra.execute(nodes.get(0));
    LinkedList<Vertex> path = dijkstra.getPath(nodes.get(6));
    
    assertNotNull(path);
    assertTrue(path.size() > 0);
    
    for (Vertex vertex : path) {
      System.out.println(vertex.getPoint());
    }
    System.out.println(dijkstra.getShortestDistance(nodes.get(6)));

  }

  private void addLane(String laneId, int sourceLocNo, int destLocNo,
      int duration) {
    Edge lane = new Edge(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
    edges.add(lane);
  }
} 