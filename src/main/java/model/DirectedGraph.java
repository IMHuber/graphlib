package model;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DirectedGraph implements Graph {

    public DirectedGraph() {
        setVertices(new ConcurrentHashMap<>());
        setEdges(new CopyOnWriteArrayList<>());
    }
}
