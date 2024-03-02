public class Node {
    protected int data;
    protected Node next;
    protected String component;

    public Node(int data, String component){
        this.data = data;
        this.next = null;
        this.component = component;
    }

    public void setNext(Node next){
        this.next = next;
    }

    public Node getNext(){
        return next;
    }

    public int getData(){
        return data;
    }

    public String getComponent(){
        return this.component;
    }

    public int getCoefficient(){
        return this.data;
    }

    public void setCoefficient(int newCoefficient) {
        this.data = newCoefficient;
    }

    public String toString(){
        return "" + data;
    }

}