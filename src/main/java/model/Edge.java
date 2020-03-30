package model;

import java.io.Serializable;
import java.util.Objects;


public class Edge implements Serializable {
    private static final long serialversionUID = 129348939L;

    private Vertex source;

    private Vertex destination;

    private double weight;

    public Edge(Vertex source, Vertex destination) {
        this(source, destination, Graph.DEFAULT_EDGE_WEIGHT);
    }

    public Edge(Vertex source, Vertex destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return Double.compare(edge.weight, weight) == 0 &&
                Objects.equals(source, edge.source) &&
                Objects.equals(destination, edge.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination, weight);
    }
}
