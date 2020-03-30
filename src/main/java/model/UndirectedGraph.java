package model;

import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class UndirectedGraph implements Graph {

    public UndirectedGraph() {
        setVertices(new ConcurrentHashMap<>());
        setEdges(new CopyOnWriteArrayList<>());
    }

    @Override
    public void addEdge(Vertex source, Vertex destination) {
        addEdge(source, destination, Graph.DEFAULT_EDGE_WEIGHT);
    }

    @Override
    public void addEdge(Vertex source, Vertex destination, double weight) {
        Graph.super.addEdge(source, destination, weight);
        Graph.super.addEdge(destination, source, weight);
    }
}
