package model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectedGraphTest {

    private Graph graph;

    @Before
    public void init() {
        graph = new DirectedGraph();
        Vertex v1 = new ExampleVertex("A");
        Vertex v2 = new ExampleVertex("B");
        Vertex v3 = new ExampleVertex("C");
        Vertex v4 = new ExampleVertex("D");
        Vertex v5 = new ExampleVertex("E");
        Vertex v6 = new ExampleVertex("F");

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);

        graph.addEdge(v1, v2);
        graph.addEdge(v2, v1);
        graph.addEdge(v2, v3);
        graph.addEdge(v2, v4);
        graph.addEdge(v4, v5);
        graph.addEdge(v5, v4);
        graph.addEdge(v5, v6);
    }

    @Test
    public void testNoDuplicateVertex() {
        graph.addVertex(new ExampleVertex("A"));
        assertEquals(6, graph.getVertices().keySet().size());
    }

    @Test
    public void testNoDuplicateEdges() {
        graph.addEdge(new ExampleVertex("A"), new ExampleVertex("B"));
        assertEquals(7, graph.getEdges().size());
    }

    @Test
    public void testGetPath() {
        List<Edge> path = graph.getPath(new ExampleVertex("A"), new ExampleVertex("C"));
        List<Edge> expected = Arrays.asList(
                new Edge(new ExampleVertex("A"), new ExampleVertex("B"), 0),
                new Edge(new ExampleVertex("B"), new ExampleVertex("C"), 0));
        assertEquals(expected, path);

        path = graph.getPath(new ExampleVertex("A"), new ExampleVertex("F"));
        expected = Arrays.asList(
                new Edge(new ExampleVertex("A"), new ExampleVertex("B"), 0),
                new Edge(new ExampleVertex("B"), new ExampleVertex("D"), 0),
                new Edge(new ExampleVertex("D"), new ExampleVertex("E"), 0),
                new Edge(new ExampleVertex("E"), new ExampleVertex("F"), 0)
        );
        assertEquals(expected, path);

        path = graph.getPath(new ExampleVertex("D"), new ExampleVertex("F"));
        expected = Arrays.asList(
                new Edge(new ExampleVertex("D"), new ExampleVertex("E"), 0),
                new Edge(new ExampleVertex("E"), new ExampleVertex("F"), 0)
        );
        assertEquals(expected, path);

        path = graph.getPath(new ExampleVertex("C"), new ExampleVertex("F"));
        expected = Collections.emptyList();
        assertEquals(expected, path);
    }

    @Test
    public void testUserFunction() {
        Function<Vertex, Vertex> userFunc = v -> {((ExampleVertex)v).setLabel("123"); return v;};
        graph.applyUserFunction(userFunc);
        assertTrue(graph.getVertices().keySet().stream().allMatch(k -> ((ExampleVertex)k).getLabel().equals("123")));
    }
}