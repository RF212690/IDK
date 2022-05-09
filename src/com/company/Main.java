package com.company;
import java.util.Hashtable;
import java.util.Map;

public class Main {
    public static int[][] board=new int[8][8];
    public static Hashtable<Integer,String> Pieces=new Hashtable<Integer,String>();



    public static void main(String[] args){
        setHashTable();
        setBoard(0);
        displayBoard();

    }
    public static void setBoard(int setup){
        for (int i = 0; i < 8; i++) {
            board[i][1]=0;
            board[i][6]=1;
            board[i][5]=1;
            board[i][4]=1;
            if(i<3){board[i][0]=i*2+2;board[i][7]=i*2+3;}
            else {board[i][0]=(7-i)*2+2;board[i][7]=(7-i)*2+3;}
        }
    }
    public static void displayBoard(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(Pieces.get(board[j][7-i])+" ");
            }
            System.out.println();
        }

    }



























    public static void setHashTable(){
        Pieces.put(0,"WP");Pieces.put(1,"BP");Pieces.put(2,"WR");Pieces.put(3,"BR");Pieces.put(4,"WN");
        Pieces.put(5,"BN");Pieces.put(6,"WB");Pieces.put(7,"BB");Pieces.put(8,"WK");Pieces.put(9,"BK");
        Pieces.put(10,"WQ");Pieces.put(11,"BQ");
    }
}
