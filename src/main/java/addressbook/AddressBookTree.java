/* Name: Edelis Molina
 * Dr. Andrew Steinberg
 * COP3503 Fall 2021
 * Programming Assignment 2
 */

package addressbook;

class Node<T, U> {
    T name;
    U office;
    Node<T, U> parentNode;
    Node<T, U> leftNode;
    Node<T, U> rightNode;
    int color; // 0 for black and 1 for red
}

public class AddressBookTree <T extends Comparable<T>, U> {
    private Node<T, U> root;
    private final Node<T, U> TNULL;

    // Set everything to default values
    public AddressBookTree() {
        System.out.println("Constructor AddressBookTree Invoked");
        //TNULL is sentinel with same attributes as ordinary node. Color is black and rest of attributes can take on arbitrary values
        TNULL = new Node<>();
        TNULL.color = 0;
        TNULL.rightNode = null;
        TNULL.leftNode = null;

        root = TNULL;
    }

    //Accessor
    public Node<T, U> getRoot() {
        return root;
    }

    //Required methods
    public void insert(T fullName, U mainOffice){
        //First do a normal BST insertion
        //newNode = z
        Node<T, U> z = new Node<>();
        z.leftNode = TNULL;
        z.rightNode = TNULL;
        z.name = fullName;
        z.office = mainOffice;
        z.color = 1; //red

        Node<T, U> y = null;
        Node<T, U> x = root;

        while(x != TNULL) {
            y = x; // to traverse
            if(z.name.compareTo(x.name) < 0)
                x = x.leftNode; //z comes before x alphabetically, move to the left
            else
                x = x.rightNode;
        }

        //y is z's parent
        z.parentNode = y;

        //if new node z is the root node
        if(y == null)
            root = z;
        // find if z is left or right child of y
        else if(z.name.compareTo(y.name) < 0)
            y.leftNode = z;
        else
            y.rightNode = z;

        //if newly inserted node is the root, change color to black and return
        //there is no need to call insertFix
        if(z.parentNode == null){
            z.color = 0;
            return;
        }

        //if grandparent is null, the root is black and z is a red child,
        //there is no need to call insertFix
        if(z.parentNode.parentNode == null)
            return;

        //Fix tree
        insertFix(z);
    }

    //First time this function is called it is guarantee that z.parent exists
    private void insertFix(Node<T, U> z)
    {
        Node<T, U> y;

        while(z.parentNode.color == 1)
        {
            //z's parent is a LEFT child
            if(z.parentNode == z.parentNode.parentNode.leftNode) {
                // y is z's uncle
                y = z.parentNode.parentNode.rightNode;

                //if z's uncle is read, CASE 1 recolor
                if(y.color == 1)
                {
                    z.parentNode.color = 0;
                    y.color = 0;
                    z.parentNode.parentNode.color = 1;
                    z = z.parentNode.parentNode; // now new Z it's old Z's grandparent
                }
                //if z's uncle is black
                else{
                    //z's uncle is black and z is a right child CASE 2. Use left rotation to transform into CASE 3
                    if(z == z.parentNode.rightNode) {
                        z = z.parentNode;
                        leftRotation(z);
                    }

                    // z's uncle is black and z is a left child CASE 3
                    z.parentNode.color = 0;
                    z.parentNode.parentNode.color = 1;
                    rightRotation(z.parentNode.parentNode);
                }
            }
            //z's parent is a RIGHT child
            else{
                // y is z's uncle
                y = z.parentNode.parentNode.leftNode;

                //if z's uncle is read, CASE 1 recolor
                if(y.color == 1)
                {
                    z.parentNode.color = 0;
                    y.color = 0;
                    z.parentNode.parentNode.color = 1;
                    z = z.parentNode.parentNode;
                }
                //if z's uncle is black
                else {
                    //z's uncle is black and z is a left child CASE 2. Use right rotation to transform into CASE 3
                    if(z== z.parentNode.leftNode) {
                        z = z.parentNode;
                        rightRotation(z);
                    }

                    // z's uncle is black and z is a right child CASE 3
                    z.parentNode.color = 0;
                    z.parentNode.parentNode.color = 1;
                    leftRotation(z.parentNode.parentNode);
                }
            }
            //this is important. Otherwise, when the function starts fixing the tree from z up to the root if z == 0,
            // then while loop will try to access z.parentNode which it is null and a nullPointException will be thrown
            if(z == root)
            {
                break;
            }

        } //end of while
        root.color = 0;
    }

    public void deleteNode(T name){
        deleteNodeHelper(root, name);

    }

    private void deleteNodeHelper(Node<T, U> node, T key) {
        //find node containing the key
        Node<T, U> z = TNULL;
        Node<T, U> x, y;

        while(node != TNULL)
        {
            //base case. key is in the current root
            if(node.name.compareTo(key) == 0)
                z = node;
            if(key.compareTo(node.name) < 0) //key comes before current node key, move to the left
            {
                node = node.leftNode;
            }
            else //key comes after current node key, move to the right
            {
                node = node.rightNode;
            }
        }

        if(z == TNULL) {
            System.out.println("Name to be deleted is not in the list");
            return;
        }

        //z is node to be deleted
        y = z;
        int yOriginalColor = y.color;

        if (z.leftNode == TNULL) {
            x = z.rightNode;
            RBTransplant(z, z.rightNode);
        }
        else if (z.rightNode == TNULL) {
            x = z.leftNode;
            RBTransplant(z, z.leftNode);
        }
        //if z has both children
        else {
            y = minimum(z.rightNode);
            yOriginalColor = y.color;
            x = y.rightNode;
            if (y.parentNode == z) {
                x.parentNode = y;
            }
            else {
                RBTransplant(y, y.rightNode);
                y.rightNode = z.rightNode;
                y.rightNode.parentNode = y;
            }
            RBTransplant(z, y);
            y.leftNode = z.leftNode;
            y.leftNode.parentNode = y;
            y.color = z.color;
        }

        if (yOriginalColor == 0){
            deleteFix(x);
        }
    }

    private void RBTransplant(Node<T, U> u, Node<T, U> v){
        //u = z and v = z.right

        //z (node to be deleted) is the root
        if (u.parentNode == null) {
            root = v;
        }
        //if z is a left child
        else if (u == u.parentNode.leftNode) {
            u.parentNode.leftNode = v;
        }
        //if z is a right child
        else {
            u.parentNode.rightNode = v;
        }
        v.parentNode = u.parentNode;
    }

    //Node with minimum key of the subtree rooted at node.
   private Node<T, U> minimum(Node<T, U> node)
    {
        while (node.leftNode != TNULL)
            node = node.leftNode;

        return node;
    }


    private void deleteFix(Node<T, U> x){
        Node<T, U> w;
        while (x != root && x.color == 0) {
            if (x == x.parentNode.leftNode) {
                w = x.parentNode.rightNode;
                if (w.color == 1) {
                    w.color = 0;                //Case 1
                    x.parentNode.color = 1;     //Case 1
                    leftRotation(x.parentNode); //Case 1
                    w = x.parentNode.rightNode; //Case 1
                }

                if (w.leftNode.color == 0 && w.rightNode.color == 0) {
                    w.color = 1;               //Case 2
                    x = x.parentNode;          //Case 2
                }
                else {
                    if (w.rightNode.color == 0) {
                        w.leftNode.color = 0;      //Case 3
                        w.color = 1;               //Case 3
                        rightRotation(w);          //Case 3
                        w = x.parentNode.rightNode;//Case 3
                    }

                    w.color = x.parentNode.color; //Case 4
                    x.parentNode.color = 0;       //Case 4
                    w.rightNode.color = 0;        //Case 4
                    leftRotation(x.parentNode);   //Case 4
                    x = root;                     //Case 4
                }
            }
            else {
                w = x.parentNode.leftNode;
                if (w.color == 1) {
                    w.color = 0;
                    x.parentNode.color = 1;
                    rightRotation(x.parentNode);
                    w = x.parentNode.leftNode;
                }

                if (w.rightNode.color == 0 && w.leftNode.color == 0) {
                    w.color = 1;
                    x = x.parentNode;
                } else {
                    if (w.leftNode.color == 0) {
                        w.rightNode.color = 0;
                        w.color = 1;
                        leftRotation(w);
                        w = x.parentNode.leftNode;
                    }

                    w.color = x.parentNode.color;
                    x.parentNode.color = 0;
                    w.leftNode.color = 0;
                    rightRotation(x.parentNode);
                    x = root;
                }
            }
        }
        x.color = 0;
    }

    private void leftRotation(Node<T, U> x){
        Node<T, U> y;
        y = x.rightNode;
        x.rightNode = y.leftNode;

        if(y.leftNode != TNULL)
            y.leftNode.parentNode = x;

        y.parentNode = x.parentNode;

        if(x.parentNode == null)
            root = y;
        else if(x == x.parentNode.leftNode)
            x.parentNode.leftNode = y;
        else
            x.parentNode.rightNode = y;

        y.leftNode = x;
        x.parentNode = y;
    }

    private void rightRotation(Node<T, U> x){
        Node<T, U> y;
        y = x.leftNode;
        x.leftNode = y.rightNode;

        if (y.rightNode != TNULL)
            y.rightNode.parentNode = x;

        y.parentNode = x.parentNode;

        if (x.parentNode == null)
            root = y;
        else if (x == x.parentNode.rightNode)
            x.parentNode.rightNode = y;
        else
            x.parentNode.leftNode = y;

        y.rightNode = x;
        x.parentNode = y;
    }



    //inorder traversal
    public void display(){
        inOrderTraversal(root);
    }

    private void inOrderTraversal(Node<T, U> node) {
        if (node != TNULL) {
            inOrderTraversal(node.leftNode);
            System.out.println(node.name +"\t"+node.office);
            inOrderTraversal(node.rightNode);
        }
    }

    //receives as argument the root of the tree
    public int countBlack(Node<T, U> node){
        int blackCnt = 0;
        if(node != TNULL) {
            if (node.color == 0)
                blackCnt += 1 + countBlack(node.leftNode) + countBlack(node.rightNode);
            else
                blackCnt+= countBlack(node.leftNode) + countBlack(node.rightNode);
        }
        return blackCnt;
    }

    public int countRed(Node<T, U> node) {
        int redCnt = 0;
        if(node != TNULL) {
            if (node.color == 1)
                redCnt += 1 + countRed(node.leftNode) + countRed(node.rightNode);
            else
                redCnt+= countRed(node.leftNode) + countRed(node.rightNode);
        }
        return redCnt;
    }

}
