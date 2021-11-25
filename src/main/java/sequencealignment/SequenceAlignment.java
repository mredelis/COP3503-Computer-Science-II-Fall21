/* Name: Edelis Molina
 * Dr. Andrew Steinberg
 * COP3503 Fall 2021
 * Programming Assignment 4
 */

package sequencealignment;

public class SequenceAlignment {
    private String str1;
    private String str2;
    private int n;
    private int m;
    private int[][] A;
    private int[][] B;

    public SequenceAlignment(String str1, String str2) {
        this.str1 = str1;
        this.str2 = str2;

        this.m = str1.length(); //rows
        this.n = str2.length(); //columns

        A = new int[m+1][n+1];
        B = new int[m+1][n+1];
    }

    public void computeAlignment(int delta){
        int i, j;

        //Initialize the table
        for (i = 1; i <= m; i++)
            A[i][0] = i*delta; //first column
        for (j = 0; j <= n; j++)
            A[0][j] = j*delta; //first row


    }

    public String getAlignment(){
        return "Edelis";
    }
}
