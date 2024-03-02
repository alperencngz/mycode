import java.io.*;

public class Main {
    public static void main(String[] args) {

        String filePath = "/Users/alperencngzz/Desktop/input.txt";
        String newFilePath = "/Users/alperencngzz/Desktop/output.txt";
        int numberOfTestLines = 0;
        LinkedList result = new LinkedList();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            System.out.println("In try block of reader");
            numberOfTestLines = Integer.parseInt(br.readLine());

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
                    result = polynomialAddition(firstPolynomLinkedList, secondPoynomLinkedList);
                    System.out.println("in addition");
                    Node currentNode = result.getHead();

                    while (currentNode != null) {
                        System.out.println(currentNode.getCoefficient() + currentNode.getComponent());
                        currentNode = currentNode.getNext();
                    }

                    System.out.println("Addition finished");
                }

                if (operation.equals("-")) {
                    result = polynomialSubtraction(firstPolynomLinkedList, secondPoynomLinkedList);
                }

                if (operation.equals("*")) {
                    result = polynomialMultiplication(firstPolynomLinkedList, secondPoynomLinkedList);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFilePath))) {
            // Writing content to the file
            int currentlines = 0;

            while (currentlines < numberOfTestLines) {


                writer.write(String.valueOf("output"));
                writer.newLine();
                currentlines++;
            }

            System.out.println("File has been written successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // METHODS
    public static LinkedList stringToLinkedList(String polynom){

        LinkedList polynomLinkedList = new LinkedList();
        String[] parts = polynom.split("[+-]");

        for (int i = 0; i < parts.length; i++){
            polynomLinkedList.insertLast(termToNode(parts[i]));
        }

        return polynomLinkedList;
    }

    public static Node termToNode(String term) {
        int coefficient = 1; // default coefficient if no explicit coefficient is found
        String component = term; // default component if no letter is found

        for (int i = 0; i < term.length(); i++) {
            if (Character.isLetter(term.charAt(i))) {
                // If a letter is found, split the term into coefficient and component
                String coefficientStr = term.substring(0, i);
                coefficient = coefficientStr.isEmpty() ? 1 : Integer.parseInt(coefficientStr);
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

    public static LinkedList polynomialSubtraction(LinkedList firstPolynom, LinkedList secondPolynom){
        return new LinkedList();
    }

    public  static LinkedList polynomialMultiplication(LinkedList firstPolynom, LinkedList secondPolynom) {
        return new LinkedList();
    }
}

// TODO EXCEPTION 1 ILE BASLARSA mesela xyz polinomu BURDA DA AYIRIRKEN COEF-TERM OLARAK COEFE 1 VERMEYI UNUTMA