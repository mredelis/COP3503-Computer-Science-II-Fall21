/* Name: Edelis Molina
 * Dr. Andrew Steinberg
 * COP3503 Fall 2021
 * Programming Assignment 4
 */

package sequencealignment;

public class SequenceAlignment {
    private final String str1;
    private final String str2;
    private final int n;
    private final int m;
    private final int[][] A;
    private final int[][] B;

    public SequenceAlignment(String str1, String str2) {
        //if str1 or str1 OR str2 does not contain only letters, throw an exception
        //cause computeAlignment() only checks for vowels and consonants
        if (!str1.toLowerCase().matches("[a-z]+") || !str2.toLowerCase().matches("[a-z]+")) {
            System.out.println("Yes");
            throw new IllegalArgumentException("Constructor invoked: Strings should only contain letters (vowels and consonants)");
        } else {
            //converting strings to lower case
            this.str1 = str1.toLowerCase();
            this.str2 = str2.toLowerCase();

            this.m = str1.length(); //rows
            this.n = str2.length(); //columns

            A = new int[m + 1][n + 1]; //cost matrix
            B = new int[m + 1][n + 1];
            //table B[0...m,0...n] to help in constructing an optimal solution
            //B[i,j] = 1 "↖"
            //B[i,j] = 2 "↑"
            //B[i,j] = 3 "←"
        }
    }

    public void computeAlignment(int delta) {
        int i, j, cost;

        //Initialize the table
        for (i = 1; i <= m; i++) {
            A[i][0] = i * delta; //first column
            B[i][0] = 2;
        }
        for (j = 0; j <= n; j++) {
            A[0][j] = j * delta; //first row
            B[0][j] = 3;
        }

        //dynamic programming algorithm
        for (j = 1; j <= n; j++) {
            for (i = 1; i <= m; i++) {

                //same symbol: vowel or consonant
                if (str1.charAt(i - 1) == str2.charAt(j - 1))
                    cost = 0;
                    //different symbol:
                    //vowel and different vowel: cost = 1
                    //consonant and different consonant: cost = 1
                    //vowel and consonant: cost = 3;
                else {
                    if (isVowel(str1.charAt(i - 1)) && isVowel(str2.charAt(j - 1)))
                        cost = 1;
                    else if (!isVowel(str1.charAt(i - 1)) && !isVowel(str2.charAt(j - 1)))
                        cost = 1;
                    else
                        cost = 3;
                }

                A[i][j] = min(cost + A[i - 1][j - 1], delta + A[i - 1][j], delta + A[i][j - 1], i, j);
            }
        }
//        printBMatrix();
//        System.out.println("----------------------------------------");
//        printAMatrix();
    }

    //Finds the minimum of 3 numbers and fills the helper matrix
    private int min(int n1, int n2, int n3, int i, int j) {
        int min = Math.min(Math.min(n1, n2), n3);

        //Fill B matrix
        if (min == n1)
            B[i][j] = 1; //"↖"
        else if (min == n2)
            B[i][j] = 2; //"↑"
        else
            B[i][j] = 3; //"←"

        return min;
    }

    //Return true if character is a vowel, false if it is a consonant
    private boolean isVowel(char character) {
        return character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u';
    }

    //For testing purposes. Print B Matrix
    private void printBMatrix() {
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (B[i][j] == 1)
                    System.out.print("↖ ");
                else if (B[i][j] == 2)
                    System.out.print("↑ ");
                else if (B[i][j] == 3)
                    System.out.print("← ");
                else
                    System.out.print("0 ");
            }
            System.out.println();
        }
    }

    //For testing purposes. Print A Matrix
    private void printAMatrix() {
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
    }


    public String getAlignment() {
        StringBuilder xStr = new StringBuilder();
        StringBuilder yStr = new StringBuilder();

        printAlignment(xStr, yStr, m, n); //recursive method.

//        System.out.println("String X: " + xStr);
//        System.out.println("String Y: " + yStr);

        return xStr + " " + yStr;
    }

    private void printAlignment(StringBuilder xString, StringBuilder yString, int i, int j) {
        if (i == 0 || j == 0)
            return;
        if (B[i][j] == 1) //"↖"
        {
            printAlignment(xString, yString, i - 1, j - 1);
            xString.append(str1.charAt(i - 1));
            yString.append(str2.charAt(j - 1));
        } else if (B[i][j] == 2) { //"↑"
            printAlignment(xString, yString, i - 1, j);
            xString.append(str1.charAt(i - 1));
            yString.append('-');
        } else { //"←"
            printAlignment(xString, yString, i, j - 1);
            xString.append('-');
            yString.append(str2.charAt(j - 1));
        }
    }
}
