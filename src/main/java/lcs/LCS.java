/* Name: Edelis Molina
 * Dr. Andrew Steinberg
 * COP3503 Fall 2021
 * Programming Assignment 3
 */

package lcs;

public class LCS {
    private final String str1;
    private final String str2;
    private final int m;
    private final int n;
    private final int[][] B;
    private final int[][] C;

    public LCS(String str1, String str2) {
        this.str1 = str1;
        this.str2 = str2;

        m = str1.length();
        n = str2.length();

        //table b[1...m,1...n] to help in constructing an optimal solution
        //b[i,j] = 1 "↖"
        //b[i,j] = 2 "↑"
        //b[i,j] = 3 "←"
        B = new int[m + 1][n + 1]; //row at i = 0 and column at j = 0 are not used

        //C[i,j] length of an LCS of the sequences Xi and Yj. Table C[0...m, 0...n]
        C = new int[m + 1][n + 1]; //row at i = 0 and column at j = 0 are filled with zeros
    }

    //Method to invoke the dynamic programming solution
    public void lcsDynamicSol() {
        int i, j;

        // Fill each cell corresponding to first row and first column with 0
        for (i = 1; i < m; i++)
            C[i][0] = 0; //first column
        for (j = 0; j < n; j++)
            C[0][j] = 0; //first row

        for (i = 1; i <= m; i++) {
            for (j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) //strings start at idx 0 that's why the i-1 and j-1
                {
                    C[i][j] = C[i - 1][j - 1] + 1; //pull from diagonal
                    B[i][j] = 1; //"↖"
                } else if (C[i - 1][j] >= C[i][j - 1]) {
                    C[i][j] = C[i - 1][j];
                    B[i][j] = 2; //"↑"
                } else {
                    C[i][j] = C[i][j - 1];
                    B[i][j] = 3; //"←"
                }
            }
        }

//        printBMatrix();
//        printCMatrix();
    }

    public String getLCS() {
        StringBuilder stringLCS = new StringBuilder();

        printLCS(stringLCS, m, n); //recursive method.

        return stringLCS.toString();
    }

    private void printLCS(StringBuilder stringLCS, int i, int j)
    {
        if(i == 0 || j == 0)
            return;
        if(B[i][j] == 1) //"↖"
        {
            printLCS(stringLCS,i - 1, j - 1);
            stringLCS.append(str1.charAt(i - 1));
        }
        else if(B[i][j] == 2) //"↑"
            printLCS(stringLCS,i - 1, j);
        else //"←"
            printLCS(stringLCS, i, j - 1);
    }

    //For testing purposes. Print B Matrix
    private void printBMatrix() {
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (B[i][j] == 1)
                    System.out.print("↖ ");
                else if (B[i][j] == 2)
                    System.out.print("↑ ");
                else if (B[i][j] == 3)
                    System.out.print("← ");
                else
                    System.out.print("error");
            }
            System.out.println();
        }
    }

    //For testing purposes. Print C Matrix
    private void printCMatrix() {
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                System.out.print(C[i][j] + " ");
            }
            System.out.println();
        }
    }


}
