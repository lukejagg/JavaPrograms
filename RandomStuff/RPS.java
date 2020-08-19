package RandomStuff;

import java.util.Scanner;

public class RPS {

    public static int StringToMove(String s) {
        s = s.toLowerCase();

        if (s.equals("rock")) {
            return 0;
        } else if (s.equals("paper")) {
            return 1;
        } else {
            return 2;
        }
    }

    public static void main(String[] args) {

        Scanner scnr = new Scanner(System.in);
        int p1 = 0, p2 = 0;

        System.out.println("Rock, Paper, Scissors!");

        while (true) {

            System.out.print("Player 1: ");
            String in = scnr.nextLine();
            if (in.toLowerCase().equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            p1 = StringToMove(in);
            System.out.print("Player 2: ");
            p2 = StringToMove(scnr.nextLine());

            char win = 'd';

            if (p1 == p2) {

            }
            else {
                if (p1 + 1 == p2) {
                    win = '2';
                } else if (p2 + 1 == p1) {
                    win = '1';
                } else if (p1 - 2 == p2) {
                    win = '2';
                } else if (p2 - 2 == p1) {
                    win = '1';
                }
            }

            if (win == '1')
                System.out.println("Player 1 defeated player 2 in a strenuous battle!");
            else if (win == '2')
                System.out.println("Player 2 cheated and defeated player 1...");
            else
                System.out.println("Both players are stupid and had a tie.");


        }

    }

}
