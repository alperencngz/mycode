import java.io.*;
public class Main {
    public static void main(String[] args) {

        String filePath = "/Users/alperencngzz/Desktop/input.txt";
        String newFilePath = "/Users/alperencngzz/Desktop/output.txt";
        int numberOfTestLines = 0;
        LinkedList result = new LinkedList();

        // the array that will keep our results
        String[] resultsAsStrings = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            System.out.println("In try block of reader");
            numberOfTestLines = Integer.parseInt(br.readLine());

            resultsAsStrings = new String[numberOfTestLines];
            int testCaseIndex = 0; // Index to store result string in the array

            int readedLines = 1;
            String line;

            while((line = br.readLine()) != null && readedLines <= numberOfTestLines){
                readedLines++;
                System.out.println(line);

                String[] parts = line.split("\\s+");

                String operation = parts[0];
                String firstPolynom = parts[1];
                String secondPolynom = parts[2];

                System.out.println(operation);

                // now our linkedLists are ready
                LinkedList firstPolynomLinkedList = stringToLinkedList(firstPolynom);
                LinkedList secondPoynomLinkedList = stringToLinkedList(secondPolynom);

                System.out.println(secondPoynomLinkedList.getHead().getNext().getCoefficient());

                if (operation.equals("+")) {
                    System.out.println("in addition");
                    result = polynomialAddition(firstPolynomLinkedList, secondPoynomLinkedList);
                    System.out.println("Addition finished");
                }

                if (operation.equals("-")) {
                    result = polynomialSubtraction(firstPolynomLinkedList, secondPoynomLinkedList);
                }

                if (operation.equals("*")) {
                    result = polynomialMultiplication(firstPolynomLinkedList, secondPoynomLinkedList);
                }

                // sort the result polynomial before converting to string
                LinkedList sortedResult = sortPolynomialLinkedList(result);

                String resultString = linkedListToString(sortedResult);

                resultsAsStrings[testCaseIndex] = resultString; // Store result string in the array
                testCaseIndex++; // Move to the next test case index
                System.out.println(resultString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFilePath))) {
            for (String resultString : resultsAsStrings) {
                // Writing content to the file
                writer.write(resultString);
                writer.newLine(); // Add a new line after each result
            }
            System.out.println("File has been written successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // METHODS
    public static LinkedList stringToLinkedList(String polynom){
        LinkedList polynomLinkedList = new LinkedList();

        String[] parts = polynom.split("(?=[+-])");
        for (int i = 0; i < parts.length; i++){
            int sign = 1;
            if(parts[i].startsWith("-")){
                sign = -1;
                parts[i] = parts[i].substring(1);
            }
            polynomLinkedList.insertLast(termToNode(sign, parts[i]));
        }
        return polynomLinkedList;
    }

    public static Node termToNode(int sign, String term) {
        int coefficient = 1; // default coefficient if no explicit coefficient is found
        String component = term; // default component if no letter is found

        for (int i = 0; i < term.length(); i++) {
            if (Character.isLetter(term.charAt(i))) {
                // If a letter is found, split the term into coefficient and component
                String coefficientStr = term.substring(0, i);
                coefficient = coefficientStr.isEmpty() ? 1 : Integer.parseInt(coefficientStr);
                if (sign == -1){
                    coefficient *= -1;
                }
                component = term.substring(i);
                break;
            }
        }
        // If the component is "0", set the coefficient to 0
        if (component.equals("0")) {
            coefficient = 0;
        }
        return new Node(coefficient, component);
    }

    public static String linkedListToString(LinkedList polynom){
        StringBuilder polynomString = new StringBuilder();
        Node currentNode = polynom.getHead();
        while (currentNode != null){
            if (currentNode.getCoefficient() > 0){
                polynomString.append("+" + ((currentNode.getCoefficient() == 1) ? "" : currentNode.getCoefficient()) + currentNode.getComponent());
            } else if (currentNode.getCoefficient() < 0) {
                polynomString.append(((currentNode.getCoefficient() == 1) ? "" : currentNode.getCoefficient()) + currentNode.getComponent());
            }
            currentNode = currentNode.getNext();
        }
        if (polynomString.length() > 0 && polynomString.charAt(0) == '+') {
            polynomString.deleteCharAt(0);
        }
        if (polynomString.length() == 0) {
            polynomString.append("0");
        }
        return polynomString.toString();
    }

    public static void addTerm(LinkedList polynomial, Node additionalNode){
        Node currentNode = polynomial.getHead();

        while (currentNode != null) {
            if (currentNode.getComponent().equals(additionalNode.getComponent())) {
                currentNode.setCoefficient(currentNode.getCoefficient() + additionalNode.getCoefficient());
                return;  // Return after updating the coefficient since there can be only 1 with same component
            }

            currentNode = currentNode.getNext();
        }
    }

    public static boolean findTerm(LinkedList polynomial, Node term){
        Node currentNode = polynomial.getHead();

        while(currentNode != null) {
            if (currentNode.getComponent().equals(term.getComponent())) {
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    // This method will be used while outputting the polynomials in correct precedence
    public static LinkedList sortPolynomialLinkedList(LinkedList polynomialLinkedList) {
        LinkedList sortedList = new LinkedList();
        Node currentNode = polynomialLinkedList.getHead();

        // Iterate through the original linked list
        while (currentNode != null) {
            insertIntoSorted(sortedList, new Node(currentNode.getCoefficient(), currentNode.getComponent()));
            System.out.println("CURRENT NODE: " + currentNode.getComponent());
            currentNode = currentNode.getNext();
        }

        return sortedList;
    }

    private static void insertIntoSorted(LinkedList sortedList, Node newNode) {
        Node current = sortedList.getHead();
        Node previous = null;

        // Find the correct position to insert the new node based on precedence rules

        while (current != null && compareNodes(newNode, current) > 0) {
            System.out.println(newNode.getComponent() + "second term: " + current.getComponent() + " result: " + compareNodes(newNode, current));
            previous = current;
            current = current.getNext();
        }

        if (previous == null) {
            // Insert at the beginning
            sortedList.insertFirst(newNode);
        } else {
            // Insert in the middle or at the end
            sortedList.insertMiddle(newNode, previous);
        }
    }

    // Compare two nodes based on precedence rules
    private static int compareNodes(Node node1, Node node2) {
        // Your implementation for comparing nodes goes here
        // Compare based on precedence rules (x > y > z, higher exponent has higher precedence)
        // Return a positive number if node1 > node2, negative if node1 < node2, and 0 if they are equal.

        // For example:
        // Compare based on components (x, y, z)
        int componentComparison = node2.compareToPrecedence(node1);
        if (componentComparison != 0) {
            return componentComparison;
        }

        // Compare based on exponent (higher exponent has higher precedence)
        return Integer.compare(node2.getExponent(), node1.getExponent());
    }

    public static LinkedList polynomialAddition(LinkedList firstPolynom, LinkedList secondPolynom){
        LinkedList result = firstPolynom;
        Node currentNodeToAdd = secondPolynom.getHead();

        while (currentNodeToAdd != null) {
            boolean containsSameComponent = findTerm(result, currentNodeToAdd);
            if (containsSameComponent) {
                addTerm(result, currentNodeToAdd);
            } else {
                result.insertLast(new Node(currentNodeToAdd.getCoefficient(), currentNodeToAdd.getComponent()));
            }

            currentNodeToAdd = currentNodeToAdd.getNext();
        }

        return result;
    }

    public static LinkedList polynomialSubtraction(LinkedList firstPolynom, LinkedList secondPolynom){
        LinkedList result = firstPolynom;
        Node currentNodeToAdd = secondPolynom.getHead();
        while (currentNodeToAdd != null) {
            Node negatedNode = new Node(-currentNodeToAdd.getCoefficient(), currentNodeToAdd.getComponent());
            boolean containsSameComponent = findTerm(result, currentNodeToAdd);
            if (containsSameComponent) {
                addTerm(result, negatedNode);
            } else {
                result.insertLast(negatedNode);
            }
            currentNodeToAdd = currentNodeToAdd.getNext();
        }
        return result;
    }

    public  static LinkedList polynomialMultiplication(LinkedList firstPolynom, LinkedList secondPolynom) {
        return new LinkedList();
    }
}