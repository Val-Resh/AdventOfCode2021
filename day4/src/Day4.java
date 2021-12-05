import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
    --- Day 4: Giant Squid ---
You're already almost 1.5km (almost a mile) below the surface of the ocean, already so deep that you can't see any sunlight.
What you can see, however, is a giant squid that has attached itself to the outside of your submarine.

Maybe it wants to play bingo?

Bingo is played on a set of boards each consisting of a 5x5 grid of numbers. Numbers are chosen at random, and the chosen number is marked on all boards on which it appears.
(Numbers may not appear on all boards.) If all numbers in any row or any column of a board are marked, that board wins. (Diagonals don't count.)

The submarine has a bingo subsystem to help passengers (currently, you and the giant squid) pass the time.
It automatically generates a random order in which to draw numbers and a random set of boards (your puzzle input). For example:

7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7

After the first five numbers are drawn (7, 4, 9, 5, and 11), there are no winners, but the boards are marked as follows (shown here adjacent to each other to save space):

22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7

After the next six numbers are drawn (17, 23, 2, 0, 14, and 21), there are still no winners:

22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7

Finally, 24 is drawn:

22 13 17 11  0         3 15  0  2 22        14 21 17 24  4
 8  2 23  4 24         9 18 13 17  5        10 16 15  9 19
21  9 14 16  7        19  8  7 25 23        18  8 23 26 20
 6 10  3 18  5        20 11 10 24  4        22 11 13  6  5
 1 12 20 15 19        14 21 16 12  6         2  0 12  3  7
At this point, the third board wins because it has at least one complete row or column of marked numbers (in this case, the entire top row is marked: 14 21 17 24 4).

The score of the winning board can now be calculated. Start by finding the sum of all unmarked numbers on that board; in this case, the sum is 188.
Then, multiply that sum by the number that was just called when the board won, 24, to get the final score, 188 * 24 = 4512.

To guarantee victory against the giant squid, figure out which board will win first. What will your final score be if you choose that board?
ANSWER: 41668
 */

public class Day4 {
    static int[] digits;
    static ArrayList<Digit[][]> boards = new ArrayList<>();
    static Digit[][] winningBoard;

    private static class Digit {
        private int digit;
        private boolean marked;
        public Digit(int digit){
            this.digit = digit;
            marked = false;
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        extractData();
        int unmarkedSum = 0;
        int winningNum = drawNumbers();
        if(winningNum != -1) {
                for (Digit[] x : winningBoard) {
                    for (Digit y : x) {
                        if(!y.marked){
                            unmarkedSum += y.digit;
                        }
                    }
                }
        }
        else System.out.println("No winner.");

        System.out.printf("Answer: %d%n", unmarkedSum * winningNum);
    }


    private static void extractData() throws FileNotFoundException {
        String[] digits;
        try {
            Scanner read = new Scanner(new File("src\\data.txt"));
            digits = read.nextLine().split(",");
            Day4.digits = new int[digits.length];
            for(int i = 0; i < digits.length; i++){
                Day4.digits[i] = Integer.parseInt(digits[i]);
            }
            read.nextLine();
            int counter = 0;
            Digit[][] temp = new Digit[5][5];
            while(read.hasNextLine()){
                if (counter == 5){
                    boards.add(temp);
                    counter = 0;
                    temp = new Digit[5][5];
                    read.nextLine();
                }
                else {
                    String[] numbers = read.nextLine().trim().split(" +");
                    Digit[] tempDigits = new Digit[5];
                    for(int i = 0; i < tempDigits.length; i++) {
                            tempDigits[i] = new Digit(Integer.parseInt(numbers[i]));
                        }
                    temp[counter] = tempDigits;
                    counter++;
                }
            }
            boards.add(temp);



        } catch (FileNotFoundException e) {
            System.out.println("Error with file handling.");
            e.printStackTrace();
        }
    }
    private static int drawNumbers(){
        int winningNum;
        for(int i = 0; i < digits.length; i++){
            for(int k = 0; k < boards.size(); k++){
                Digit[][] board = boards.get(k);
                for(Digit[] x : board){
                    for (Digit y : x){
                        if(y.digit == digits[i]){
                            y.marked = true;
                            if(hasWon(board)) {
                                winningNum = digits[i];
                                return winningNum;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    private static boolean hasWon(Digit[][] board){
        int trueCountH = 0, trueCountV = 0;
        for(int i = 0; i < board.length; i++){
            for(int k = 0; k < board.length; k++){
                if(board[i][k].marked) trueCountH++;
                if(board[k][i].marked) trueCountV++;
            }
            if(trueCountH == 5 || trueCountV == 5) {
                winningBoard = board;
                return true;
            }
            else {
                trueCountH = 0;
                trueCountV = 0;
            }
        }
        return false;
    }
}
