/* Name: Edelis Molina
 * Dr. Andrew Steinberg
 * COP3503 Fall 2021
 * Programming Assignment 1
 */

package lottery;

import java.util.Random;

public class Lottery {

    private String ticket;
    final static int SIZE = 6;

    public Lottery() {
        this.ticket = "";
    }

    public Lottery(Random rn) {
        for(int i = 0; i < SIZE; i++)
            this.ticket += rn.nextInt(10);
      }

    // Accessor method to retrieve the ticket attribute
    public String GetTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    // Method to sort the tickets. Implements QuickSort
    public static void Sort(Lottery[] ticketsColl) {
        QuickSort(ticketsColl, 0, ticketsColl.length - 1);
    }

    private static void QuickSort(Lottery[] ticketColl, int p, int r) {
        if(p < r) {
            int q = Partition(ticketColl, p, r);
            QuickSort(ticketColl, p, q - 1);
            QuickSort(ticketColl, q + 1, r);
        }
    }

    private static int Partition(Lottery[] ticketColl, int p, int r) {
        String x = ticketColl[r].GetTicket();

        int i = p - 1;
        for(int j = p; j <= (r - 1); j++) {
            if(ticketColl[j].GetTicket().compareTo(x) <= 0) {
                i++;
                Swap(ticketColl, i, j);
            }
        }
        // Final Swap
        Swap(ticketColl, i + 1, r);
        return i + 1;
    }

    public static void Swap(Lottery[] ticketColl, int a, int b) {
        String temp = ticketColl[a].GetTicket();
        ticketColl[a].setTicket(ticketColl[b].GetTicket());
        ticketColl[b].setTicket(temp);
    }

    public static String GenerateRandomWinner(Random rn) {
        String test = "";
        for(int i = 0; i < SIZE; i++)
            test += rn.nextInt(10);

        return test;
    }

    public static int GenerateSelectWinner(int maxIdx, Random rn) {
        return rn.nextInt(maxIdx);
    }

    // Method to find a winning ticket. It should run O(n)
    public static boolean Solution1(Lottery[] ticketsColl, String winningTicket, int maxIdx) {
        for(int i = 0; i <= maxIdx; i++) {
            if(ticketsColl[i].GetTicket().compareTo(winningTicket) == 0)
                return true;
        }
        return false;
    }

    // Another method to find a winning ticket. It should run O(lgn)
    // Implements binary search
    public static boolean Solution2(Lottery[] ticketsColl, int low, int high, String winningTicket) {
         if(high >= low) {
            int mid = (low + high) / 2;

            // Base case
            if(ticketsColl[mid].GetTicket().compareTo(winningTicket) == 0)
                return true;
            // ticket in the mid-element of the array comes lexicographically before than the winning ticket, search to the right
            else if(ticketsColl[mid].GetTicket().compareTo(winningTicket) < 0)
                return Solution2(ticketsColl, mid + 1, high, winningTicket);
            // winning ticket is lexicographically smaller than mid, search to the left
            else
                return Solution2(ticketsColl, low, mid - 1, winningTicket);
        }
        // if there is no return from the recursive calls, the winning ticket is not in the array
        return false;
    }

}


