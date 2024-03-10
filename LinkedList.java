/**
 * The LinkedList class represents a linked list of nodes, where each node
 * contains an integer coefficient, a string component representing a variable,
 * and a reference to the next node in the list.
 */
public class LinkedList {

    /**
     * The head node of the linked list.
     */
    protected Node head;

    /**
     * The tail node of the linked list.
     */
    protected Node tail;

    /**
     * Constructs an empty LinkedList with a null head and tail.
     */
    public LinkedList() {
        head = null;
        tail = null;
    }

    /**
     * Checks if the linked list is empty.
     *
     * @return True if the linked list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Gets the head node of the linked list.
     *
     * @return The head node of the linked list.
     */
    public Node getHead() {
        return head;
    }

    /**
     * Inserts a new node at the beginning of the linked list.
     *
     * @param newNode The node to be inserted.
     */
    public void insertFirst(Node newNode) {
        if (isEmpty()) {
            tail = newNode;
        }
        newNode.setNext(head);
        head = newNode;
    }

    /**
     * Inserts a new node at the end of the linked list.
     *
     * @param newNode The node to be inserted.
     */
    public void insertLast(Node newNode) {
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }
        tail = newNode;
    }

    /**
     * Inserts a new node after a specified node in the linked list.
     *
     * @param newNode  The node to be inserted.
     * @param previous The node after which the new node will be inserted.
     */
    public void insertMiddle(Node newNode, Node previous) {
        newNode.setNext(previous.getNext());
        previous.setNext(newNode);
    }

    /**
     * @param value The value to be searched.
     * @return The node that has the data value. If no node exists, returns null.
     */
    public Node search(int value) {
        Node tmp = head;
        while (tmp != null) {
            if (value == tmp.getData()) {
                return tmp;
            }
            tmp = tmp.getNext();
        }
        return null;
    }

    /**
     * Gets the node at the specified index in the linked list.
     *
     * @param i The index of the node to be obtained.
     * @return The node at the specified index. If no node exists, returns null.
     */
    public Node getNodeI(int i) {
        Node tmp = head;
        int index = 0;
        while (tmp != null) {
            if (index == i){
                return tmp;
            }
            index++;
            tmp = tmp.getNext();
        }
        return null;
    }

    /**
     * Gets the number of elements in the linked list.
     *
     * @return The number of elements in the linked list.
     */
    public int numberOfElements(){
        Node tmp = head;
        int count = 0;
        while (tmp != null) {
            count++;
            tmp = tmp.getNext();
        }
        return count;
    }

    /**
     * Deletes the first node in the linked list.
     */
    public void deleteFirst(){
        head = head.getNext();
        if (isEmpty()){
            tail = null;
        }
    }

    /**
     * Gets the previous node of a specified node in the linked list.
     *
     * @param node The node for which the previous node is to be obtained.
     * @return The previous node of the specified node.
     */
    public Node getPrevious(Node node){
        Node tmp = head;
        Node previous = null;
        while (tmp != node) {
            previous = tmp;
            tmp = tmp.getNext();
        }
        return previous;
    }

    /**
     * Deletes a node with a specific data value from the linked list.
     *
     * @param value The value of the node to be deleted.
     */
    public void deleteValue(int value){
        Node tmp = head;
        Node previous = null;
        while (tmp != null) {
            if (tmp.getData() == value){
                if (previous != null){
                    previous.setNext(tmp.next);
                    if (tmp.next == null){
                        tail = previous;
                    }
                } else {
                    head = tmp.next;
                    if (head == null){
                        tail = null;
                    }
                }
                break;
            }
            previous = tmp;
            tmp = tmp.getNext();
        }
    }

    /**
     * Deletes the last node in the linked list.
     */
    public void deleteLast(){
        tail = getPrevious(tail);
        if (tail != null){
            tail.setNext(null);
        } else {
            head = null;
        }
    }

    /**
     * Deletes a specified node from the linked list.
     *
     * @param node The node to be deleted.
     */
    public void deleteMiddle(Node node){
        Node previous;
        previous = getPrevious(node);
        previous.setNext(node.getNext());
    }

    /**
     * Converts the linked list to a string representation.
     *
     * @return A string representation of the linked list.
     */
    public String toString(){
        StringBuilder result = new StringBuilder();
        Node tmp = head;
        while (tmp != null) {
            result.append(tmp).append(" ");
            tmp = tmp.getNext();
        }
        return result.toString();
    }
}