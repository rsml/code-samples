/**
 * @author: Ross Miller
 * @version: 1.0
 * @since: March 11, 2010
 */


import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * An undirected graph data structure, that does not allow multiple edges.
 * Note: This is highly inefficient for large graphs with few edges,
 *    because they result in a sparse 2D array whose size grows exponentially with
 *    the number of edges. This was just a homework assignment, and should not be
 *    used for production systems without caution. To improve the space efficiency,
 *    the 2D array should be changed to a trie. Each node in the trie would be a 2D
 *    array of smaller size than the entire array.
 *
 *    Ex) If the entire 2D array is (20 x 20) elements, and the trie node
 *        size is (4 x 4) elements, then the original 2D matrix can be compressed into a 
 *        2D array of size (5 x 5) elements, where each element is a reference to a trie node.
 *        Additionally, you would need to store an index of all of the different unique trie nodes.
 * 
 * VARIABLES
 * ---------
 * private boolean[] vertices;
 *      A 1d array of booleans, where each index corresponds to that elements id (as given by getTerritoryId for example)
 *      If an element is true, that vertice is active, if it is false, it is inactive.
 *      
 * private boolean[][] edges;
 *      A 2d array of booleans. The 1st column and the 1st row both correspond to element id 1, etc.
 *      So if I add an edge from id 4 to id 6, then boolean[4][6] == boolean[6][4] == true
 *      Note, this is not a diagonal matrix
 * 
 * 
 * METHODS
 * -------
 * public Graph(int numVertices)
 *      Constructor. Takes in an integer, and makes vertices have size 'numVertices', and edges has 'numVertices' number of rows and columns
 * 
 * public List<Integer> getUnusedVertices()
 *      Loops through all of the vertices and returns a list of vertices that are active
 *      
 * public boolean isEdge(int source, int destination)
 *      Returns whether or not a given edge exists.
 * 
 * public void addEdge(int source, int destination)
 *      Adds an edge given a source and destination.
 *      
 * public void removeEdge(int source, int destination)
 *      Removes a given edge
 * 
 * public boolean isInGraph(int vertex)
 *      Returns a boolean determining whether or not a vertex is in the graph
 *      
 * public void removeVertex(int vertex)
 *      Removes a given vertex from the graph
 * 
 * public List<Integer> getAdjacent(int vertex)
 *      Returns a list of all of the vertices that are connected to the given vertex through an edge
 *      
 * public int degree(int vertex)
 *      Returns the number of vertices that are directly connected to the given vertex
 * 
 * public boolean isConnected()
 *      Returns true if it is possible to get from any vertex to any other vertex through a finite number of edges. False otherwise
 *
 */

public class Graph {
  private boolean[] vertices;
  private boolean[][] edges;
  
  /**
   * Constructs a graph object.
   * Postconditions: A graph is created with the given number of vertices, which are all empty, and with no edges
   * @param numVertices The number of vertices that wil be in this graph
   * @return Initialized Graph object
   */
  public Graph(int numVertices) {
    vertices = new boolean[numVertices];
    edges = new boolean[numVertices][numVertices];
  }
  
  /**
   * Loops through all of the vertices and returns a list of vertices that are active
   * @return List of all unused vertices that are active
   */
  public List<Integer> getUnusedVertices() {
    List<Integer> unusedVertices = new ArrayList<Integer>(); // keeps track of the unused vertices in the graph
    int i=0;
    for (boolean isVertex : vertices) {
      if (!isVertex)
        unusedVertices.add(i);
      i++;
    }
    return unusedVertices;
  }
  
  /**
   * Returns whether or not a given edge exists.
   * @param source The index of the source vertex
   * @param destination The index of the destination vertex
   * @return True if the edge exists, otherwise false.
   */
  public boolean isEdge(int source, int destination) {
    return (edges[source][destination] || edges[destination][source]);
  }
  
  /**
   * Adds an edge given a source and destination.
   * Postconditions: Adds an edge to the graph. Also sets the source and destination vertices to be active
   * @param source The index of the source vertex
   * @param destination The index of the destination vertex
   */
  public void addEdge(int source, int destination) {
    vertices[source] = true;
    vertices[destination] = true;

    if (source!=destination) {
      edges[source][destination] = true;
      edges[destination][source] = true;
    }
  }
  
  /**
   * Removes a given edge
   * Postconditions: Removes the given edge from the graph
   * @param source The index of the source vertex
   * @param destination The index of the destination vertex
   */
  public void removeEdge(int source, int destination) {
     edges[source][destination] = false;
     edges[destination][source] = false;
  }
  
  /**
   * Returns a boolean determining whether or not a vertex is in the graph
   * @param vertex The index of the vertex to be tested
   * @return True if the given vertex is in the graph, false otherwise
   */
  public boolean isInGraph(int vertex) {
    return vertices[vertex];
  }
  
  /**
   * Removes a given vertex from the graph
   * Postconditions: Deactivates the vertex and removes all edges to this vertex
   * @param vertex The index of the vertex to be removed
   */
  public void removeVertex(int vertex) {
    vertices[vertex] = false;
    for (int destination=0; destination<vertices.length; destination++)
      removeEdge(vertex, destination);
  }
  
  /**
   * Returns a list of all of the vertices that are connected to the given vertex through an edge
   * @return: List<Integer>, a list containing all of the vertices' indexes that are directly connected to the given vertex
   * @param vertex The vertex who's adjacent vertices are to be found
   */
  public List<Integer> getAdjacent(int vertex) {
    List<Integer> list = new ArrayList<Integer>(); // Keeps track of all adjacent vertices
    if (isInGraph(vertex)) {
      for (int destination=0; destination<vertices.length; destination++){
        if (isEdge(vertex, destination))
          list.add(destination);
      }
    }
    return list;
  }
  
  /**
   * Returns the number of vertices that are directly connected to the given vertex
   * @param vertex The index of the vertex who's degree is to be found
   * @return The number of vertices adjacent to the given vertex
   */
  public int degree(int vertex) {
    return getAdjacent(vertex).size();
  }
  
  /**
   * Returns true if it is possible to get from any vertex to any other vertex through a finite number of edges, false otherwise
   * @return True if the graph is connected, false if it is not
   */
  public boolean isConnected() {
    List<Integer> nodes = new ArrayList<Integer>(); // Keeps track of all of the vertices connected to the first node
    
    int firstNode=0;
    while (firstNode<vertices.length && vertices[firstNode]==false)
      firstNode++;

    if (firstNode >= vertices.length)
      return true;
    
    nodes.add(firstNode);
    
    // While territories are still being added
    boolean keepGoing = true;
    while (keepGoing) {
      keepGoing = false;
      int currentSize = nodes.size();
      for (Integer node : nodes) {
        List<Integer> adjacents = getAdjacent(node);
        for (Integer adjacent : adjacents) {
          if (!nodes.contains(adjacent)) {
            nodes.add(adjacent);
            keepGoing = true;
          }
        }
      }
    }

    return nodes.size() == (vertices.length - getUnusedVertices().size());
  }
}
