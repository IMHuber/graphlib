package model;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import static java.lang.String.format;


public interface Graph {

    double DEFAULT_EDGE_WEIGHT = 0;

    default void addVertex(Vertex vertex) {
        GraphStructure.getVertices().putIfAbsent(vertex, new CopyOnWriteArrayList<>());
    }

    default void removeVertex(Vertex vertex) {
        GraphStructure.getVertices().values().forEach(e -> e.remove(vertex));
        GraphStructure.getVertices().remove(vertex);
    }

    default void addEdge(Vertex source, Vertex destination) {
        addEdge(source, destination, DEFAULT_EDGE_WEIGHT);
    }

    default void addEdge(Vertex source, Vertex destination, double weight) {
        Utils.checkVerticesPresent(source, source);

        if (GraphStructure.getEdges().stream()
                .anyMatch(e -> e.getSource().equals(source) && e.getDestination().equals(destination) && e.getWeight() == weight)) {
            return;
        }

        GraphStructure.getEdges().add(new Edge(source, destination, weight));
        GraphStructure.getVertices().get(source).add(destination);
    }

    default void removeEdge(Edge e) {
        Utils.checkVerticesPresent(e.getSource(), e.getDestination());
        Utils.checkEdgePresent(e.getSource(), e.getDestination());

        GraphStructure.getEdges().remove(e);
    }

    default List<Edge> getPath(Vertex source, Vertex destination) {
        return Utils.findEdgePath(source, destination, new ArrayList<>(), new HashSet<>());
    }

    default <T> void applyUserFunction(Function<Vertex, T> predicate) {
        Set<Vertex> visited = new LinkedHashSet<>();
        GraphStructure.getVertices().keySet().stream().filter(k -> !visited.contains(k)).forEach(k -> {
            visited.add(k);
            predicate.apply(k);
        });
    }

    default Map<Vertex, List<Vertex>> getVertices() {
        return GraphStructure.getVertices();
    }

    default void setVertices(Map<Vertex, List<Vertex>> vertices) {
        GraphStructure.setVertices(vertices);
    }

    default List<Edge> getEdges() {
        return GraphStructure.getEdges();
    }

    default void setEdges(List<Edge> edges) {
        GraphStructure.setEdges(edges);
    }


    class GraphStructure {
        //graph is represented by adjacency list representation
        private static Map<Vertex, List<Vertex>> vertices;

        private static List<Edge> edges;

        private static Map<Vertex, List<Vertex>> getVertices() {
            return vertices;
        }

        private static void setVertices(Map<Vertex, List<Vertex>> newVertices) {
            vertices = newVertices;
        }

        private static List<Edge> getEdges() {
            return edges;
        }

        private static void setEdges(List<Edge> newEdges) {
            edges = newEdges;
        }
    }


    class Utils {

        private static List<Vertex> getAdjacentVertices(Map<Vertex, List<Vertex>> vertices, Vertex vertex) {
            return vertices.get(vertex);
        }

        private static void addPathEdge(Vertex source, Vertex destination, List<Edge> path) {
            path.add(GraphStructure.getEdges().stream()
                    .filter(e -> e.getSource().equals(source) && e.getDestination().equals(destination))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(format("Edge not found from '%s' to %s", source, destination)))
            );
        }

        private static void checkVerticesPresent(Vertex source, Vertex destination) {
            if (MapUtils.isEmpty(GraphStructure.getVertices())
                    || GraphStructure.getVertices().keySet().stream().noneMatch(v -> v.equals(source))
                    || GraphStructure.getVertices().keySet().stream().noneMatch(v -> v.equals(destination))) {
                throw new RuntimeException(String.format("Can't find vertex for [%s] or [%s]", source, destination));
            }
        }

        private static void checkEdgePresent(Vertex source, Vertex destination) {
            if (CollectionUtils.isEmpty(GraphStructure.getEdges())
                    || GraphStructure.getEdges().stream().noneMatch(e -> e.getSource().equals(source) && e.getDestination().equals(destination))) {
                throw new RuntimeException(String.format("Can't find edge for [%s] and [%s]", source, destination));
            }
        }

        private static List<Edge> findEdgePath(Vertex source, Vertex destination, List<Edge> edges, Set<Vertex> visited) {
            if (visited.contains(source)) {
                return edges;
            }
            visited.add(source);

            for (Vertex v : GraphStructure.getVertices().get(source)) {
                if (v.equals(destination)) {
                    addPathEdge(source, destination, edges);
                    break;
                }

                List<Edge> tempPath = findEdgePath(v, destination, edges, visited);

                if (CollectionUtils.isNotEmpty(tempPath)) {
                    List<Edge> res = new CopyOnWriteArrayList<>();
                    addPathEdge(source, v, res);
                    res.addAll(tempPath);
                    edges = res;
                    break;
                }
            }
            return edges;
        }
    }
}
