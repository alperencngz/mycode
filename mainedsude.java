
import java.io.*;
public class Main {
    public static void main(String[] args) {

        String inputFile = "\\Users\\SUDE\\Desktop\\input.txt";
        String outputFile = "\\Users\\SUDE\\Desktop\\output.txt";
        int numberOfLines = 0;
        LinkedList result = new LinkedList();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

            numberOfLines = Integer.parseInt(br.readLine());

            int readedLines = 1;
            String line;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                while ((line = br.readLine()) != null && readedLines <= numberOfLines) {
                    readedLines++;
                    System.out.println(line);

                    String[] parts = line.split("\\s+");

                    String operation = parts[0];
                    String firstPolynom = parts[1];
                    String secondPolynom = parts[2];
                    System.out.println(operation);

                    LinkedList firstPolynomLinkedList = stringToLinkedList(firstPolynom);
                    LinkedList secondPoynomLinkedList = stringToLinkedList(secondPolynom);

                    System.out.println(secondPoynomLinkedList.getHead().getNext().getCoefficient());

                    if (operation.equals("+")) {
                        result = addPolynomials(firstPolynomLinkedList, secondPoynomLinkedList);
                        System.out.println("in addition");
                        System.out.println(result);
                    }

                    if (operation.equals("-")) {
                        result = substractPolynomials(firstPolynomLinkedList, secondPoynomLinkedList);
                        System.out.println("substraction");
                        System.out.println(result);
                    }

                    if (operation.equals("*")) {
                        result = multiplyPolynomials(firstPolynomLinkedList, secondPoynomLinkedList);
                        System.out.println(result);
                    }

                    writer.write(linkedListToString(result));
                    writer.newLine();
                    result = new LinkedList();
                }

                System.out.println("File has been written successfully.");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a polynomial string to a linked list representation.
     *
     * @param polynom The polynomial string to convert
     * @return The linked list representation of the polynomial
     */
    public static LinkedList stringToLinkedList(String polynom) {
        LinkedList polynomLinkedList = new LinkedList();

        // Split the polynomial string into parts based on the occurrence of '+' or '-'
        String[] parts = polynom.split("(?=[+-])");

        // Iterate through each part and convert it to a node, then add it to the linked list
        for (int i = 0; i < parts.length; i++) {
            int sign = 1;
            if (parts[i].startsWith("-")) {
                sign = -1;
                parts[i] = parts[i].substring(1);
            }
            // Convert the part to a node and insert it at the end of the linked list
            polynomLinkedList.insertLast(termToNode(sign, parts[i]));
        }
        return polynomLinkedList;
    }

    /**
     * Converts a term string to a node.
     *
     * @param sign The sign of the coefficient (+1 or -1)
     * @param term The term string to convert
     * @return The node representing the term
     */
    public static Node termToNode(int sign, String term) {
        int coefficient = 0;
        String component = "";

        // Remove any leading '+' or '-'
        if (term.startsWith("+") || term.startsWith("-")) {
            term = term.substring(1);
        }

        // Find the index of the first letter in the term
        int indexOfFirstLetter = -1;
        for (int i = 0; i < term.length(); i++) {
            if (Character.isLetter(term.charAt(i))) {
                indexOfFirstLetter = i;
                break;
            }
        }

        // Extract the coefficient and component from the term
        if (indexOfFirstLetter == -1) {
            coefficient = sign * Integer.parseInt(term);
        } else {
            if (indexOfFirstLetter > 0) {
                coefficient = sign * Integer.parseInt(term.substring(0, indexOfFirstLetter));
            } else {
                coefficient = sign;
            }
            component = term.substring(indexOfFirstLetter);
        }

        // Create a new node with the extracted coefficient and component
        return new Node(coefficient, component);
    }

    /**
     * Converts a linked list representation of a polynomial to a string.
     *
     * @param polynom The linked list representing the polynomial
     * @return The string representation of the polynomial
     */
    public static String linkedListToString(LinkedList polynom) {
        StringBuilder polynomString = new StringBuilder();
        Node currentNode = polynom.getHead();
        // Traverse the linked list and append each node to the string builder
        while (currentNode != null) {
            if (currentNode.getCoefficient() > 0) {
                polynomString.append("+" + ((currentNode.getCoefficient() == 1) ? "" : currentNode.getCoefficient()) + currentNode.getTerm());
            } else if (currentNode.getCoefficient() < 0) {
                polynomString.append(((currentNode.getCoefficient() == 1) ? "-" : currentNode.getCoefficient()) + currentNode.getTerm());
            }
            currentNode = currentNode.getNext();
        }
        // Remove leading '+' if present, and replace empty string with '0'
        if (polynomString.length() > 0 && polynomString.charAt(0) == '+') {
            polynomString.deleteCharAt(0);
        }
        if (polynomString.length() == 0) {
            polynomString.append("0");
        }
        return polynomString.toString();
    }

    /**
     * Adds a term to a polynomial.
     *
     * @param polynomial The polynomial to which the term will be added
     * @param additionalNode The node representing the term to add
     */
    public static void addTerm(LinkedList polynomial, Node additionalNode) {
        Node currentNode = polynomial.getHead();
        // Traverse the polynomial to find the term
        while (currentNode != null) {
            // If the term already exists, update its coefficient by adding the additional node's coefficient
            if (currentNode.getTerm().equals(additionalNode.getTerm())) {
                currentNode.setCoefficient(currentNode.getCoefficient() + additionalNode.getCoefficient());
                return;
            }
            currentNode = currentNode.getNext();
        }
    }

    /**
     * Subtracts a term from a polynomial.
     *
     * @param polynomial The polynomial from which the term will be subtracted
     * @param additionalNode The node representing the term to subtract
     */
    public static void subtractTerm(LinkedList polynomial, Node additionalNode) {
        Node currentNode = polynomial.getHead();
        // Traverse the polynomial to find the term
        while (currentNode != null) {
            // If the term already exists, update its coefficient by subtracting the additional node's coefficient
            if (currentNode.getTerm().equals(additionalNode.getTerm())) {
                currentNode.setCoefficient(currentNode.getCoefficient() - additionalNode.getCoefficient());
                return;  // Return after updating the coefficient
            }
            currentNode = currentNode.getNext();
        }
    }



    /**
     * Checks if a given term exists in the polynomial.
     *
     * @param polynomial The polynomial in which to search for the term
     * @param term The term node to search for
     * @return true if the term exists in the polynomial, false otherwise
     */
    public static boolean findTerm(LinkedList polynomial, Node term) {
        Node currentNode = polynomial.getHead();

        // Traverse the polynomial and compare each term with the given term
        while (currentNode != null) {
            if (currentNode.getTerm().equals(term.getTerm())) {
                return true; // Term found, return true
            }
            currentNode = currentNode.getNext();
        }
        return false; // Term not found, return false
    }

    /**
     * Adds two polynomials together.
     *
     * @param firstPolynom The first polynomial
     * @param secondPolynom The second polynomial
     * @return The sum of the two polynomials
     */
    public static LinkedList addPolynomials(LinkedList firstPolynom, LinkedList secondPolynom) {
        LinkedList result = firstPolynom;
        Node currentNodeToAdd = secondPolynom.getHead();

        // Traverse the second polynomial and add each term to the result polynomial
        while (currentNodeToAdd != null) {
            boolean containsSameComponent = findTerm(result, currentNodeToAdd);
            if (containsSameComponent) {
                addTerm(result, currentNodeToAdd);
            } else {
                result.insertLast(new Node(currentNodeToAdd.getCoefficient(), currentNodeToAdd.getTerm()));
            }

            currentNodeToAdd = currentNodeToAdd.getNext();
        }

        return result; // Return the result polynomial
    }

    /**
     * Subtracts one polynomial from another.
     *
     * @param firstPolynom The polynomial from which to subtract
     * @param secondPolynom The polynomial to subtract
     * @return The result of subtracting the second polynomial from the first
     */
    public static LinkedList substractPolynomials(LinkedList firstPolynom, LinkedList secondPolynom) {
        LinkedList result = firstPolynom;
        Node currentNodeToSubtract = secondPolynom.getHead();

        // Traverse the second polynomial and subtract each term from the result polynomial
        while (currentNodeToSubtract != null) {
            boolean containsSameComponent = findTerm(result, currentNodeToSubtract);
            if (containsSameComponent) {
                substractTerm(result, currentNodeToSubtract);
            } else {
                result.insertLast(new Node(-currentNodeToSubtract.getCoefficient(), currentNodeToSubtract.getTerm()));
            }
            currentNodeToSubtract = currentNodeToSubtract.getNext();
        }
        return result; // Return the result polynomial
    }

    /**
     * Multiplies two polynomials.
     *
     * @param firstPolynomial The first polynomial
     * @param secondPolynomial The second polynomial
     * @return The product of the two polynomials
     */
    public static LinkedList multiplyPolynomials(LinkedList firstPolynomial, LinkedList secondPolynomial) {
        LinkedList result = new LinkedList();
        Node currentFirstNode = firstPolynomial.getHead();

        // Multiply each term from the first polynomial with each term from the second polynomial
        while (currentFirstNode != null) {
            Node currentSecondNode = secondPolynomial.getHead();
            LinkedList multipliedNodes = new LinkedList();

            // Multiply each term from the first polynomial with each term from the second polynomial
            while (currentSecondNode != null) {
                Node multipliedNode = multiplyNodes(currentFirstNode, currentSecondNode);
                multipliedNodes.insertLast(multipliedNode);
                currentSecondNode = currentSecondNode.getNext();
            }

            // Add the multiplied terms to the result polynomial
            addPolynomials(result, multipliedNodes);
            currentFirstNode = currentFirstNode.getNext();
        }

        return result; // Return the result polynomial
    }

    /**
     * Multiplies two nodes representing terms.
     *
     * @param node1 The first term node
     * @param node2 The second term node
     * @return The product of the two terms as a new node
     */
    private static Node multiplyNodes(Node node1, Node node2) {
        int newCoefficient = node1.getCoefficient() * node2.getCoefficient();
        String newTerm = multiplyTerms(node1.getTerm(), node2.getTerm(), newCoefficient);

        return new Node(newCoefficient, newTerm);
    }

    /**
     * Multiplies two term strings and returns the result as a new term string.
     *
     * @param term1 The first term string
     * @param term2 The second term string
     * @param newCoefficient The coefficient of the product term
     * @return The product term string
     */
    private static String multiplyTerms(String term1, String term2, int newCoefficient) {
        StringBuilder resultTerm = new StringBuilder();
        int[] exponents1 = extractExponents(term1);
        int[] exponents2 = extractExponents(term2);
        int[] newExponents = calculateNewExponents(exponents1, exponents2);

        appendVariableWithExponent(resultTerm, "x", newExponents[0]);
        appendVariableWithExponent(resultTerm, "y", newExponents[1]);
        appendVariableWithExponent(resultTerm, "z", newExponents[2]);

        return resultTerm.toString();
    }

    /**
     * Appends a variable with its exponent to the given StringBuilder if the exponent is not zero.
     *
     * @param result The StringBuilder to which the variable with exponent will be appended
     * @param variable The variable to be appended
     * @param exponent The exponent associated with the variable
     */
    private static void appendVariableWithExponent(StringBuilder result, String variable, int exponent) {
        if (exponent != 0) {
            result.append(variable).append((exponent == 1) ? "" : exponent);
        }
    }

    /**
     * Extracts exponents of variables 'x', 'y', and 'z' from the given term.
     *
     * @param term The term from which exponents will be extracted
     * @return An array containing the exponents of 'x', 'y', and 'z'
     */
    private static int[] extractExponents(String term) {
        int[] exponents = new int[3];
        exponents[0] = getExponentFromTerm(term, "x");
        exponents[1] = getExponentFromTerm(term, "y");
        exponents[2] = getExponentFromTerm(term, "z");

        return exponents;
    }

    /**
     * Calculates new exponents by adding corresponding exponents from two arrays.
     *
     * @param exponents1 The first array of exponents
     * @param exponents2 The second array of exponents
     * @return An array containing the sum of corresponding exponents
     */
    private static int[] calculateNewExponents(int[] exponents1, int[] exponents2) {
        int newXExponent = exponents1[0] + exponents2[0];
        int newYExponent = exponents1[1] + exponents2[1];
        int newZExponent = exponents1[2] + exponents2[2];

        return new int[]{newXExponent, newYExponent, newZExponent};
    }

    /**
     * Gets the exponent of a variable from the given term string.
     *
     * @param term The term string from which to extract the exponent
     * @param variable The variable for which to find the exponent
     * @return The exponent of the variable in the term string, or 0 if not found
     */
    private static int getExponentFromTerm(String term, String variable) {
        int startIndex = term.indexOf(variable);
        if (startIndex == -1) {
            return 0; // Variable not found in the term
        }

        int endIndex = startIndex + variable.length();
        StringBuilder exponentString = new StringBuilder();

        // Extract the exponent from the term string
        for (int i = endIndex; i < term.length(); i++) {
            char currentChar = term.charAt(i);
            if (Character.isDigit(currentChar)) {
                exponentString.append(currentChar);
            } else {
                break; // Reached the end of the exponent
            }
        }

        return exponentString.length() > 0 ? Integer.parseInt(exponentString.toString()) : 1; // Parse the exponent string or default to 1 if not found
    }

}