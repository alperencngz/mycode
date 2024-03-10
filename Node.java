/**
 * The Node class represents a node in a linked list for polynomial terms.
 * Each node contains an integer coefficient, a string component representing a variable,
 * and a reference to the next node in the linked list.
 */

public class Node {

    /**
     * The integer coefficient of the polynomial term.
     */
    protected int data;

    /**
     * The reference to the next node in the linked list.
     */
    protected Node next;

    /**
     * The string component representing a variable in the polynomial term.
     */
    protected String component;

    /**
     * Constructs a new Node with the given coefficient and component.
     *
     * @param data      The coefficient of the polynomial term.
     * @param component The string component representing a variable.
     */
    public Node(int data, String component){
        this.data = data;
        this.next = null;
        this.component = component;
    }

    /**
     * Compares the current node with another node based on precedence rules.
     * The comparison is made based on the components (x, y, z) and exponents of the components.
     *
     * @param otherNode The node to compare with.
     * @return A negative integer if the other node has higher precedence,
     *         a positive integer if the current node has higher precedence,
     *         or zero if they have the same precedence.
     */
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

    /**
     * Gets the exponent of the specified variable in the current component.
     *
     * @param variable The variable (x, y, z) for which the exponent needs to be obtained.
     * @return The exponent of the variable in the current component.
     */
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

    /**
     * Sets the next node in the linked list.
     *
     * @param next The next node in the linked list.
     */
    public void setNext(Node next){
        this.next = next;
    }

    /**
     * Gets the next node in the linked list.
     *
     * @return The next node in the linked list.
     */
    public Node getNext(){
        return next;
    }

    /**
     * Gets the coefficient of the polynomial term.
     *
     * @return The coefficient of the polynomial term.
     */
    public int getData(){
        return data;
    }

    /**
     * Gets the string component representing a variable in the polynomial term.
     *
     * @return The string component representing a variable.
     */
    public String getComponent(){
        return this.component;
    }

    /**
     * Gets the coefficient of the polynomial term.
     *
     * @return The coefficient of the polynomial term.
     */
    public int getCoefficient(){
        return this.data;
    }

    /**
     * Sets a new coefficient for the polynomial term.
     *
     * @param newCoefficient The new coefficient to set.
     */
    public void setCoefficient(int newCoefficient) {
        this.data = newCoefficient;
    }

    /**
     * Converts the node to a string representation.
     *
     * @return A string representation of the node.
     */
    public String toString(){
        return "" + data;
    }

}