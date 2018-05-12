package network;

public class RouterEdge {
    private String src;
    private String dest;
    private int weight;

    public RouterEdge(Router src, Router dest, int weight) {
        this.src = src.getAddress();
        this.dest = dest.getAddress();
        this.weight = weight;
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}