import java.io.*;

public class Main {
    public static void main(String[] args) {

        String filePath = "C:\\Users\\co028953\\Desktop\\input.txt";
        String newFilePath = "C:\\Users\\co028953\\Desktop\\output.txt";
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

                // now our linkedLists are ready
                LinkedList firstPolynomLinkedList = stringToLinkedList(firstPolynom);
                LinkedList secondPoynomLinkedList = stringToLinkedList(secondPolynom);

                if (operation == "+") {
                    result = polynomialAddition(firstPolynomLinkedList, secondPoynomLinkedList);
                }

                if (operation == "-") {
                    result = polynomialSubstraction(firstPolynomLinkedList, secondPoynomLinkedList);
                }

                if (operation == "*") {
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

    public static NodePolynom termToNode(String term) {

        String[] parts = term.split("x", 2);

        // left side of x is coefficient
        int coefficient = Integer.parseInt(parts[0]);

        // rightside of x is our component (x3y2 etc.)
        String component = parts[1];

        return new NodePolynom(coefficient, component);
    }

    public static LinkedList polynomialAddition(LinkedList firstPolynom, LinkedList secondPolynom){
        LinkedList result = new LinkedList();
        NodePolynom currentNode = (NodePolynom) firstPolynom.getHead();

        return result;
    }

    public static LinkedList polynomialSubstraction(LinkedList firstPolynom, LinkedList secondPolynom){
        return new LinkedList();
    }

    public  static LinkedList polynomialMultiplication(LinkedList firstPolynom, LinkedList secondPolynom) {
        return new LinkedList();
    }
}