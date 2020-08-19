package RandomStuff;

import java.util.Scanner;

public class WordForLetter {

    public static void print(String arg) {
        System.out.println(arg);
    }

    public static void main(String[] args) {

        Scanner scnr = new Scanner(System.in);

        print("Input string: ");
        String input = scnr.nextLine();
        //input = input.replaceAll(" ", "").replaceAll(".", "").replaceAll(",", "");
        String output = "";
        int index = 0;

        for (Character s : input.toCharArray()) {

            if (Character.isAlphabetic(s)) {

                print(Character.toUpperCase(s) + "");

            }

        }

        //print(output);

    }

}
