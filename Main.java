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
            numberOfTestLines = Integer.parseInt(br.readLine());

            resultsAsStrings = new String[numberOfTestLines];
            int testCaseIndex = 0; // Index to store result string in the array

            int readedLines = 1;
            String line;

            while((line = br.readLine()) != null && readedLines <= numberOfTestLines){
                readedLines++;

                String[] parts = line.split("\\s+");

                String operation = parts[0];
                String firstPolynom = parts[1];
                String secondPolynom = parts[2];

                // now our linkedLists are ready
                LinkedList firstPolynomLinkedList = stringToLinkedList(firstPolynom);
                LinkedList secondPoynomLinkedList = stringToLinkedList(secondPolynom);

                if (operation.equals("+")) {
                    result = polynomialAddition(firstPolynomLinkedList, secondPoynomLinkedList);
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
                coefficient = (coefficientStr.isEmpty() || coefficientStr.equals("+")) ? 1 : Integer.parseInt(coefficientStr);
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
                // TODO -1'i düzgün yapıyor mu kontrol et
                polynomString.append(((currentNode.getCoefficient() == 1) ? "-" : currentNode.getCoefficient()) + currentNode.getComponent());
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
            currentNode = currentNode.getNext();
        }

        return sortedList;
    }

    private static void insertIntoSorted(LinkedList sortedList, Node newNode) {
        Node current = sortedList.getHead();
        Node previous = null;

        // Find the correct position to insert the new node based on precedence rules

        // if compareNodes returns a negative number, that means newNode has lower precedence.
        while (current != null && compareNodes(newNode, current) < 0) {
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
    private static int compareNodes(Node newNode, Node current) {
        // Compare based on components (x, y, z) and exponents of components
        int componentComparison = newNode.compareToPrecedence(current);

        // this will be negative if newNode has lower precedence, positive if newNode has higher precedence
        return componentComparison;
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
        LinkedList result = new LinkedList();
        Node currentNodeFirst = firstPolynom.getHead();

        while(currentNodeFirst != null){
            Node currentNodeSecond = secondPolynom.getHead();
            LinkedList multipliedNodes = new LinkedList();

            while (currentNodeSecond != null){
                Node multipliedNode = nodeMultiplication(currentNodeFirst, currentNodeSecond);
                multipliedNodes.insertLast(multipliedNode);
                currentNodeSecond = currentNodeSecond.getNext();
            }
            polynomialAddition(result, multipliedNodes);
            currentNodeFirst = currentNodeFirst.getNext();
        }


        return result;
    }

    private static Node nodeMultiplication(Node node1, Node node2){
        // Multiply coefficients
        int newCoefficient = node1.getCoefficient() * node2.getCoefficient();

        // Multiply components
        String newComponent = multiplyComponents(node1.getComponent(), node2.getComponent(), newCoefficient);

        return new Node(newCoefficient, newComponent);
    }

    private static String multiplyComponents(String component1, String component2, int newCoefficient) {
        StringBuilder resultComponent = new StringBuilder();
        int[] exponents1 = new int[3];
        int[] exponents2 = new int[3];
        int[] newExponents = setExponents(exponents1, exponents2, component1, component2);

        int newXExponent = newExponents[0];
        int newYExponent = newExponents[1];
        int newZExponent = newExponents[2];
        if (newXExponent != 0){
            resultComponent.append("x" + ((newXExponent == 1) ? "" : newXExponent));
        }
        if (newYExponent != 0){
            resultComponent.append("y" + ((newYExponent == 1) ? "" : newYExponent));
        }
        if (newZExponent != 0){
            resultComponent.append("z" + ((newZExponent == 1) ? "" : newZExponent));
        }
        return resultComponent.toString();
    }

    private static int getSign(String component) {
        return component.startsWith("-") ? -1 : 1;
    }

    private static int[] setExponents(int[] exponents1, int[] exponents2, String component1, String component2){
        exponents1[0] = getExponentFromComponent(component1,"x");
        exponents1[1] = getExponentFromComponent(component1,"y");
        exponents1[2] = getExponentFromComponent(component1,"z");
        exponents2[0] = getExponentFromComponent(component2,"x");
        exponents2[1] = getExponentFromComponent(component2,"y");
        exponents2[2] = getExponentFromComponent(component2,"z");
        int newXExponent = exponents1[0] + exponents2[0];
        int newYExponent = exponents1[1] + exponents2[1];
        int newZExponent = exponents1[2] + exponents2[2];
        int[] exponents = new int[3];
        exponents[0] = newXExponent;
        exponents[1] = newYExponent;
        exponents[2] = newZExponent;
        return exponents;
    }

    private static int getExponentFromComponent(String component, String variable) {
        // Extract the exponent from the component for the given variable
        // Example: if given x241y352z41 and variable x, the return should be 241
        // Example: if given x241y352z41 and variable y, the return should be 352
        // Example: if given x241y352z41 and variable z, the return should be 41
        int startIndex = component.indexOf(variable);
        if (startIndex == -1) {
            // Variable not found in the component
            return 0;
        }
        int endIndex = startIndex + variable.length();
        StringBuilder exponentString = new StringBuilder();

        // Extract the substring containing the exponent
        for (int i = endIndex; i < component.length(); i++) {
            char currentChar = component.charAt(i);
            if (Character.isDigit(currentChar)) {
                exponentString.append(currentChar);
            } else {
                // Break if a non-digit character is encountered
                break;
            }
        }
        // Convert the exponent string to an integer
        return exponentString.length() > 0 ? Integer.parseInt(exponentString.toString()) : 1;
    }
}