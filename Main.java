import java.io.*;

/**
 * The Main class is responsible for reading input from a file, performing polynomial
 * operations based on the given operations, and writing the results to an output file.
 * It utilizes the LinkedList and Node classes for representing and manipulating
 * polynomial expressions.
 *
 * The program supports addition, subtraction, and multiplication of polynomials.
 *
 * @author [Alperen Cengiz Ozturk]
 * @version 1.0
 */
public class Main {

    /**
     * The main method reads input from a file, performs polynomial operations based on
     * the given operations, and writes the results to an output file. It uses the
     * LinkedList and Node classes for representing and manipulating polynomial expressions.
     * The program supports addition, subtraction, and multiplication of polynomials.
     *
     * @param args The command-line arguments (not used in this program).
     */
    public static void main(String[] args) {

        /**
         * The file path to the input file containing polynomial operations.
         */
        String filePath = "input.txt";

        /**
         * The file path to the output file where the results will be written.
         */
        String newFilePath = "output.txt";

        /**
         * The number of test cases to be processed.
         */
        int numberOfTestLines = 0;

        /**
         * The linked list representing the result of polynomial operations.
         */
        LinkedList result = new LinkedList();

        /**
         * The array that stores the string representations of the results for each test case.
         */
        String[] resultsAsStrings = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){

            // Read the number of test cases from the first line of the input file
            numberOfTestLines = Integer.parseInt(br.readLine());

            // Initialize the array to store string representations of results
            resultsAsStrings = new String[numberOfTestLines];

            int testCaseIndex = 0; // Index to store result string in the array
            int readedLines = 1; // Counter for lines read from the input file
            String line;

            // Process each test case
            while((line = br.readLine()) != null && readedLines <= numberOfTestLines){
                readedLines++;

                // Split the line into three parts: operation, first polynomial, and second polynomial
                String[] parts = line.split("\\s+");

                String operation = parts[0];
                String firstPolynom = parts[1];
                String secondPolynom = parts[2];

                // Convert polynomial strings to LinkedLists
                LinkedList firstPolynomLinkedList = stringToLinkedList(firstPolynom);
                LinkedList secondPoynomLinkedList = stringToLinkedList(secondPolynom);


                // Perform the specified polynomial operation
                if (operation.equals("+")) {
                    result = polynomialAddition(firstPolynomLinkedList, secondPoynomLinkedList);
                } else if (operation.equals("-")) {
                    result = polynomialSubtraction(firstPolynomLinkedList, secondPoynomLinkedList);
                } else if (operation.equals("*")) {
                    result = polynomialMultiplication(firstPolynomLinkedList, secondPoynomLinkedList);
                }

                // Sort the result polynomial before converting to string
                LinkedList sortedResult = sortPolynomialLinkedList(result);
                String resultString = linkedListToString(sortedResult);

                // Store result string in the array
                resultsAsStrings[testCaseIndex] = resultString;
                testCaseIndex++; // Move to the next test case index
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write results to the output file
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

    /**
     * Takes a string in polynomial form and returns a LinkedList that contains that polynomial's information.
     * @param polynom String polynomial object to convert it to a LinkedList.
     * @return
     */
    public static LinkedList stringToLinkedList(String polynom){
        LinkedList polynomLinkedList = new LinkedList();

        // Split the polynomial string into parts based on the presence of '+' or '-' signs
        String[] parts = polynom.split("(?=[+-])");
        for (int i = 0; i < parts.length; i++){
            int sign = 1;

            // Handle negative terms by adjusting the sign
            if(parts[i].startsWith("-")){
                sign = -1;
                parts[i] = parts[i].substring(1);
            }

            // Convert the term to a Node and insert it into the LinkedList
            polynomLinkedList.insertLast(termToNode(sign, parts[i]));
        }
        return polynomLinkedList;
    }

    /**
     * Converts a term string into a Node with coefficient and component values.
     *
     * @param sign The sign of the term (+1 or -1).
     * @param term The input term string.
     * @return A Node representing the term.
     */
    public static Node termToNode(int sign, String term) {
        int coefficient = 1; // default coefficient if no explicit coefficient is found
        String component = term; // default component if no letter is found

        for (int i = 0; i < term.length(); i++) {
            if (Character.isLetter(term.charAt(i))) {
                // If a letter is found, split the term into coefficient and component
                String coefficientStr = term.substring(0, i);
                coefficient = (coefficientStr.isEmpty() || coefficientStr.equals("+")) ? 1 : Integer.parseInt(coefficientStr);

                // Adjust the coefficient based on the sign
                if (sign == -1){
                    coefficient *= -1;
                }

                // Set the component based on the letter found in the term
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

    /**
     * Converts a polynomial linked list into a string representation.
     *
     * @param polynom The input polynomial linked list.
     * @return A string representation of the polynomial.
     */
    public static String linkedListToString(LinkedList polynom){
        StringBuilder polynomString = new StringBuilder();
        Node currentNode = polynom.getHead();

        // Iterate through the linked list
        while (currentNode != null){
            if (currentNode.getCoefficient() > 0){
                // Append positive coefficients with '+' sign
                polynomString.append("+" + ((currentNode.getCoefficient() == 1) ? "" : currentNode.getCoefficient()) + currentNode.getComponent());
            } else if (currentNode.getCoefficient() < 0) {
                // TODO -1'i düzgün yapıyor mu kontrol et
                // Append negative coefficients with proper sign
                polynomString.append(((currentNode.getCoefficient() == 1) ? "-" : currentNode.getCoefficient()) + currentNode.getComponent());
            }

            // Move to the next node
            currentNode = currentNode.getNext();
        }

        // Remove leading '+' if present and handle the case when the polynomial is zero
        if (polynomString.length() > 0 && polynomString.charAt(0) == '+') {
            polynomString.deleteCharAt(0);
        }
        if (polynomString.length() == 0) {
            polynomString.append("0");
        }
        return polynomString.toString();
    }

    /**
     * Adds the coefficients of a given node to the corresponding node in the linked list,
     * updating the existing coefficient if the component matches.
     *
     * @param polynomial The polynomial linked list.
     * @param additionalNode The node to be added.
     */
    public static void addTerm(LinkedList polynomial, Node additionalNode){
        Node currentNode = polynomial.getHead();

        // Iterate through the linked list
        while (currentNode != null) {
            if (currentNode.getComponent().equals(additionalNode.getComponent())) {
                // Update the coefficient and return since there can be only one node with the same component
                currentNode.setCoefficient(currentNode.getCoefficient() + additionalNode.getCoefficient());
                return;  // Return after updating the coefficient since there can be only 1 with same component
            }

            // Move to the next node
            currentNode = currentNode.getNext();
        }
    }

    /**
     * Checks if a given term (node) exists in the polynomial linked list.
     *
     * @param polynomial The polynomial linked list.
     * @param term The term (node) to be searched for.
     * @return True if the term exists, false otherwise.
     */
    public static boolean findTerm(LinkedList polynomial, Node term){
        Node currentNode = polynomial.getHead();

        // Iterate through the linked list
        while(currentNode != null) {
            if (currentNode.getComponent().equals(term.getComponent())) {
                return true; // Return true if the term is found
            }

            // Move to the next node
            currentNode = currentNode.getNext();
        }

        return false; // Return false if the term is not found
    }

    /**
     * Sorts a polynomial linked list in correct precedence based on components and exponents.
     *
     * @param polynomialLinkedList The original unsorted polynomial linked list.
     * @return A new sorted polynomial linked list.
     */
    public static LinkedList sortPolynomialLinkedList(LinkedList polynomialLinkedList) {
        LinkedList sortedList = new LinkedList();
        Node currentNode = polynomialLinkedList.getHead();

        // Iterate through the original linked list and insert nodes into the sorted list
        while (currentNode != null) {
            insertIntoSorted(sortedList, new Node(currentNode.getCoefficient(), currentNode.getComponent()));
            currentNode = currentNode.getNext();
        }

        return sortedList;
    }

    /**
     * Inserts a new node into a sorted linked list based on precedence rules.
     *
     * @param sortedList The sorted linked list.
     * @param newNode The new node to be inserted.
     */
    private static void insertIntoSorted(LinkedList sortedList, Node newNode) {
        Node current = sortedList.getHead();
        Node previous = null;

        // Find the correct position to insert the new node based on precedence rules

        // If compareNodes returns a negative number, that means newNode has lower precedence.
        while (current != null && compareNodes(newNode, current) < 0) {
            previous = current;
            current = current.getNext();
        }

        // Insert at the beginning if the new node has the highest precedence
        if (previous == null) {
            sortedList.insertFirst(newNode);
        } else {
            // Insert in the middle or at the end
            sortedList.insertMiddle(newNode, previous);
        }
    }

    /**
     * Compares two nodes based on precedence rules, considering components and exponents.
     * The precedence rules are as follows:
     * x > y > z in variables.
     * Higher exponent of same variable has higher precedence than lower exponent. (x3 > x2)
     *
     * @param newNode The node to be compared.
     * @param current The node to be compared against.
     * @return A negative value if newNode has lower precedence, a positive value if newNode has higher precedence, or 0 if equal.
     */
    private static int compareNodes(Node newNode, Node current) {
        // Compare based on components (x, y, z) and exponents of components
        int componentComparison = newNode.compareToPrecedence(current);

        // Return a value indicating the relative precedence of the nodes
        return componentComparison;
    }


    /**
     * Performs addition of two polynomials represented as linked lists.
     *
     * @param firstPolynom The linked list representation of the first polynomial.
     * @param secondPolynom The linked list representation of the second polynomial.
     * @return A new linked list representing the result of polynomial addition.
     */
    public static LinkedList polynomialAddition(LinkedList firstPolynom, LinkedList secondPolynom) {
        LinkedList result = firstPolynom;
        Node currentNodeToAdd = secondPolynom.getHead();

        // Iterate through the second polynomial and add terms to the result
        while (currentNodeToAdd != null) {
            boolean containsSameComponent = findTerm(result, currentNodeToAdd);
            if (containsSameComponent) {
                // If the result already contains a term with the same component, add coefficients
                addTerm(result, currentNodeToAdd);
            } else {
                // If the result doesn't contain the same component, insert a new term
                result.insertLast(new Node(currentNodeToAdd.getCoefficient(), currentNodeToAdd.getComponent()));
            }

            currentNodeToAdd = currentNodeToAdd.getNext();
        }

        return result;
    }

    /**
     * Performs subtraction of two polynomials represented as linked lists.
     *
     * @param firstPolynom The linked list representation of the first polynomial.
     * @param secondPolynom The linked list representation of the second polynomial.
     * @return A new linked list representing the result of polynomial subtraction.
     */
    public static LinkedList polynomialSubtraction(LinkedList firstPolynom, LinkedList secondPolynom) {
        LinkedList result = firstPolynom;
        Node currentNodeToAdd = secondPolynom.getHead();

        // Iterate through the second polynomial and subtract terms from the result
        while (currentNodeToAdd != null) {
            // Create a negated node for subtraction
            Node negatedNode = new Node(-currentNodeToAdd.getCoefficient(), currentNodeToAdd.getComponent());
            boolean containsSameComponent = findTerm(result, currentNodeToAdd);
            if (containsSameComponent) {
                // If the result already contains a term with the same component, subtract coefficients
                addTerm(result, negatedNode);
            } else {
                // If the result doesn't contain the same component, insert the negated term
                result.insertLast(negatedNode);
            }

            currentNodeToAdd = currentNodeToAdd.getNext();
        }

        return result;
    }

    /**
     * Performs multiplication of two polynomials represented as linked lists.
     *
     * @param firstPolynom The linked list representation of the first polynomial.
     * @param secondPolynom The linked list representation of the second polynomial.
     * @return A new linked list representing the result of polynomial multiplication.
     */
    public static LinkedList polynomialMultiplication(LinkedList firstPolynom, LinkedList secondPolynom) {
        LinkedList result = new LinkedList();
        Node currentNodeFirst = firstPolynom.getHead();

        // Iterate through the first polynomial and multiply terms with the second polynomial
        while (currentNodeFirst != null) {
            Node currentNodeSecond = secondPolynom.getHead();
            LinkedList multipliedNodes = new LinkedList();

            // Iterate through the second polynomial and calculate the product of terms
            while (currentNodeSecond != null) {
                Node multipliedNode = nodeMultiplication(currentNodeFirst, currentNodeSecond);
                multipliedNodes.insertLast(multipliedNode);
                currentNodeSecond = currentNodeSecond.getNext();
            }

            // Add the product terms to the result using polynomial addition
            polynomialAddition(result, multipliedNodes);
            currentNodeFirst = currentNodeFirst.getNext();
        }

        return result;
    }

    /**
     * Performs multiplication of two polynomial nodes.
     *
     * @param node1 The first polynomial node.
     * @param node2 The second polynomial node.
     * @return A new node representing the result of multiplying the input nodes.
     */
    private static Node nodeMultiplication(Node node1, Node node2) {
        // Multiply coefficients
        int newCoefficient = node1.getCoefficient() * node2.getCoefficient();

        // Multiply components
        String newComponent = multiplyComponents(node1.getComponent(), node2.getComponent(), newCoefficient);

        return new Node(newCoefficient, newComponent);
    }

    /**
     * Multiply the components of two polynomials and create a new component.
     *
     * @param component1 The first component.
     * @param component2 The second component.
     * @param newCoefficient The coefficient obtained from multiplying the coefficients.
     * @return A new component resulting from the multiplication of input components.
     */
    private static String multiplyComponents(String component1, String component2, int newCoefficient) {
        StringBuilder resultComponent = new StringBuilder();
        int[] exponents1 = new int[3];
        int[] exponents2 = new int[3];
        int[] newExponents = setExponents(exponents1, exponents2, component1, component2);

        int newXExponent = newExponents[0];
        int newYExponent = newExponents[1];
        int newZExponent = newExponents[2];
        if (newXExponent != 0) {
            resultComponent.append("x" + ((newXExponent == 1) ? "" : newXExponent));
        }
        if (newYExponent != 0) {
            resultComponent.append("y" + ((newYExponent == 1) ? "" : newYExponent));
        }
        if (newZExponent != 0) {
            resultComponent.append("z" + ((newZExponent == 1) ? "" : newZExponent));
        }
        return resultComponent.toString();
    }

    /**
     * Sets the exponents for each variable in two polynomial components and calculates the sum of exponents.
     *
     * @param exponents1 An array to store the exponents of the first component (x, y, z).
     * @param exponents2 An array to store the exponents of the second component (x, y, z).
     * @param component1 The first polynomial component.
     * @param component2 The second polynomial component.
     * @return An array containing the sum of exponents for each variable (x, y, z).
     */
    private static int[] setExponents(int[] exponents1, int[] exponents2, String component1, String component2) {
        exponents1[0] = getExponentFromComponent(component1, "x");
        exponents1[1] = getExponentFromComponent(component1, "y");
        exponents1[2] = getExponentFromComponent(component1, "z");
        exponents2[0] = getExponentFromComponent(component2, "x");
        exponents2[1] = getExponentFromComponent(component2, "y");
        exponents2[2] = getExponentFromComponent(component2, "z");
        int newXExponent = exponents1[0] + exponents2[0];
        int newYExponent = exponents1[1] + exponents2[1];
        int newZExponent = exponents1[2] + exponents2[2];
        int[] exponents = new int[3];
        exponents[0] = newXExponent;
        exponents[1] = newYExponent;
        exponents[2] = newZExponent;
        return exponents;
    }

    /**
     * Extracts the exponent from a polynomial component for the given variable.
     * Example: if given x241y352z41 and variable x, the return should be 241
     * Example: if given x241y352z41 and variable y, the return should be 352
     * Example: if given x241y352z41 and variable z, the return should be 41
     *
     * @param component The polynomial component.
     * @param variable The variable (x, y, z) for which the exponent needs to be extracted.
     * @return The exponent of the variable in the given component.
     *         Returns 0 if the variable is not found in the component.
     */
    private static int getExponentFromComponent(String component, String variable) {
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