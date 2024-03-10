public class Node {
    protected int data;
    protected Node next;
    protected String component;

    public Node(int data, String component){
        this.data = data;
        this.next = null;
        this.component = component;
    }

    // Compare two nodes based on precedence rules
    public int compareToPrecedence(Node otherNode) {
        // Compare based on components (x, y, z) and exponents of components

        // Check if the current component has 'x'
        boolean hasX = this.component.contains("x");
        // Check if the other component has 'x'
        boolean otherHasX = otherNode.getComponent().contains("x");

        // Check if the current component has 'y'
        boolean hasY = this.component.contains("y");
        // Check if the other component has 'y'
        boolean otherHasY = otherNode.getComponent().contains("y");

        // Check if the current component has 'z'
        boolean hasZ = this.component.contains("z");
        // Check if the other component has 'z'
        boolean otherHasZ = otherNode.getComponent().contains("z");

        // Compare based on the precedence of variables
        if (hasX && !otherHasX) {
            return 1; // Current component has higher precedence
        } else if (!hasX && otherHasX) {
            return -1; // Other component has higher precedence
        } else if (hasX && otherHasX && this.getExponent("x") > otherNode.getExponent("x")) {
            return 1;
        } else if (hasX && otherHasX && this.getExponent("x") < otherNode.getExponent("x")) {
            return -1;
        } if (hasY && !otherHasY) {
            return 1; // Current component has higher precedence
        } else if (!hasY && otherHasY) {
            return -1; // Other component has higher precedence
        } else if (hasY && otherHasY && this.getExponent("y") > otherNode.getExponent("y")) {
            return 1;
        } else if (hasY && otherHasY && this.getExponent("y") < otherNode.getExponent("y")) {
            return -1;
        } else if (hasZ && !otherHasZ) {
            return 1; // Current component has higher precedence
        } else if (!hasZ && otherHasZ) {
            return -1; // Other component has higher precedence
        } else if (hasZ && otherHasZ && this.getExponent("z") > otherNode.getExponent("z")) {
            return 1;
        } else if (hasZ && otherHasZ && this.getExponent("z") < otherNode.getExponent("z")) {
            return -1;
        } else {
            return 0;
        }
    }

    // TODO GET EXPONENTI BITIR VE SONRA DA YUKARIDAKI YERI TEK TEK IF STATEMENTLERLE BITIRIP GPT'YE AT 20 SATIRA INDIRSIN
    private int getExponent(String variable) {
        // Extract the exponent from the component for the given variable
        String[] parts = this.component.split(variable);
        if (parts.length > 1) {
            // If the variable is found, extract the substring containing the exponent
            String exponentString = parts[1];
            String exponent = "";
            for (int i = 0; i < exponentString.length(); i++) {
                if (Character.isDigit(exponentString.charAt(i))){
                    exponent += exponentString.charAt(i);
                } else {
                    break;
                }
            }
            return exponent.equals("") ? 1 : Integer.parseInt(exponent);
        }
        return 1; // Default exponent if not found
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