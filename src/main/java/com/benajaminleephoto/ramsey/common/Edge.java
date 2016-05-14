package com.benajaminleephoto.ramsey.common;

import com.google.common.base.Strings;

/**
 * This Class represents an edge within a CayleyGraph connecting two vertices.
 * 
 * @author Ben Ferenchak
 * @version 1.0
 */
public class Edge implements java.io.Serializable {

    private static final long serialVersionUID = -6733335734314818893L;
    private Vertex vertexA;
    private Vertex vertexB;
    private String color;


    /**
     * This is the main constructor for the Edge Class. It will set the vertices the edge is
     * connected to as well as well as initialize the color of the Edge.
     * 
     * @param A This is the first Vertex the Edge is connected too. Best practice is to make this
     *        the Vertex with the lesser ID.
     * @param B This is the second Vertex the Edge is connected too. Best practice is to make this
     *        the Vertex with the greater ID.
     * @param color This is the color which the Edge will be initialized to.
     */
    public Edge(Vertex A, Vertex B, String color) {
        this.vertexA = A;
        this.vertexB = B;
        setColor(color);

    }


    /**
     * This method will set the color of the edge.
     * 
     * @param color This is the color the edge will be set to.
     */
    public void setColor(String color) {
        this.color = color;
    }


    /**
     * This will return the Vertex object VertexA.
     * 
     * @return VertexA.
     */
    public Vertex getVertexA() {
        return vertexA;
    }


    /**
     * This will return the Vertex object VertexB.
     * 
     * @return VertexB.
     */
    public Vertex getVertexB() {
        return vertexB;
    }


    /**
     * This will return the String value of the edge's current color.
     * 
     * @return This is the String value of the color of the edge.
     */
    public String getColor() {
        return color;
    }


    /**
     * This will construct and return a human readable string representing the edge. This will be
     * used for debugging only.
     * 
     * @return A human readable string representing the Edge.
     */
    public String printEdge() {
        return "[" + getVertexA().getId() + ":" + getVertexB().getId() + ":" + getColor() + "]";
    }


    /**
     * This will return the vertex of an edge not equal to the provided vertex.
     * 
     * @param vertex Provided vertex of this edge for which the other vertex is requested.
     * @return The vertex attached to this edge which is not equal to the provided vertex.
     */
    public Vertex getOtherVertex(Vertex vertex) {
        if (vertexA.getId() == vertex.getId()) {
            return vertexB;
        } else {
            return vertexA;
        }
    }


    /**
     * This will flip the color of the edge.
     */
    public void flipColor() {
        if (getColor().equals("RED")) {
            setColor("BLUE");
        } else if (getColor().equals("BLUE")) {
            setColor("RED");
        }
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(Strings.padStart(Integer.toString(getVertexA().getId()), 3, '0'));
        builder.append(":");
        builder.append(Strings.padStart(Integer.toString(getVertexB().getId()), 3, '0'));
        builder.append(":");
        builder.append(Strings.padEnd(getColor(), 4, ' '));
        builder.append("]");
        return builder.toString();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((vertexA == null) ? 0 : vertexA.hashCode());
        result = prime * result + ((vertexB == null) ? 0 : vertexB.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Edge)) {
            return false;
        }
        Edge other = (Edge) obj;
        if (color == null) {
            if (other.color != null) {
                return false;
            }
        } else if (!color.equals(other.color)) {
            return false;
        }
        if (vertexA == null) {
            if (other.vertexA != null) {
                return false;
            }
        } else if (!vertexA.equals(other.vertexA)) {
            return false;
        }
        if (vertexB == null) {
            if (other.vertexB != null) {
                return false;
            }
        } else if (!vertexB.equals(other.vertexB)) {
            return false;
        }
        return true;
    }

}
