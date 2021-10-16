package addressbook;

public class Node <T, U> {
    private T name;
    private U office;
    private Node parentNode;
    private Node leftNode;
    private Node rightNode;
    private int color;

    public Node(T name, U office) {
        name = name;
        office = office;
        parentNode = leftNode = rightNode = null;
        this.color = 1; // read color
    }
}
