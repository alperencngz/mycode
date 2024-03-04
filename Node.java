public class Node {
    protected int data;
    protected Node next;
    protected String component;

    public Node(int data, String component){
        this.data = data;
        this.next = null;
        this.component = component;
    }

    public int getExponent() {
        // Extract the exponent from the component
        String[] parts = this.component.split("[xyz]");
        if (parts.length > 1) {
            return Integer.parseInt(parts[1]);
        }
        return 0; // Default exponent if not found
    }

    // Compare two nodes based on precedence rules
    public int compareToPrecedence(Node otherNode) {
        // Compare based on components (x, y, z)
        int componentComparison = compareComponents(this.component, otherNode.component);
        if (componentComparison != 0) {
            return componentComparison;
        }

        // Compare based on exponent (higher exponent has higher precedence)
        return Integer.compare(this.getExponent(), otherNode.getExponent());
    }

    // Compare components based on precedence rules (x > y > z)
    private int compareComponents(String component1, String component2) {
        if (component1.equals("x")) {
            return component2.equals("x") ? 0 : 1;
        } else if (component1.equals("y")) {
            return component2.equals("x") ? -1 : (component2.equals("y") ? 0 : 1);
        } else if (component1.equals("z")) {
            return component2.equals("z") ? 0 : -1;
        }
        return 0; // Default case, should not happen in valid input
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