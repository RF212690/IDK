package com.company;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;

public class Main {
    public static int[][] board=new int[8][8];
    public static Hashtable<Integer,String> Pieces=new Hashtable<Integer,String>();



    public static void main(String[] args){
        createDatabase();

        if(regOrLog()=='r'){
            register();
        }else{
            login();
        }
        Player player1=new Player(random(1),1000,/*insert username*/"A");
        if(player1.getPlayerColor()==0){
        Player player2=new Player(1,1000,"Bot");
        } else{
            Player player2=new Player(0,1000,"Bot");
        }
        setHashTable();
        setBoard(0);
        displayBoard();

    }
    public static void setBoard(int setup){
        for (int i = 0; i < 8; i++) {
            board[i][1]=0;
            board[i][6]=1;
            board[i][5]=-1;
            board[i][4]=-1;
            board[i][3]=-1;
            board[i][2]=-1;
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
        Pieces.put(10,"WQ");Pieces.put(11,"BQ");Pieces.put(-1,"VO");
    }
    public static void PlayerMove(){
        Scanner input=new Scanner(System.in);
        int xCor=-1;
        int yCor=0;
        do{
        System.out.println("Select X coordinate of piece you want to move: ");
        try{
        xCor=input.nextInt();}
            catch(Exception e){
                System.out.println("Error: "+e);
            }
        if(xCor<0 || xCor>0){
            System.out.println("Not a valid input");
        }
        }while(xCor<0 || xCor>8);
        System.out.println("Select Y coordinate of piece you want to move: ");
        //continue
    }
    public static int random(int max){
        Random random= new Random();
        return random.nextInt(max+1);
    }
    public static char regOrLog(){
        Scanner input=new Scanner(System.in);
        while(true){
        System.out.println("Select R to register and L to login: ");
        String regOrLog=input.next();
        if(regOrLog.equalsIgnoreCase("r")){
            return 'r';
        }else if(regOrLog.equalsIgnoreCase("l")){
            return 'l';
        }else{
            System.out.println("invalid input");
        }
        }

    }
    public static void register(){

    }
    public static void login(){

    }
    public static void createDatabase(){
        String URL="C:\\Users\\rf212690\\IdeaProjects\\IDK\\";
        String username="username";
        String password="password";
        String rating="rating";///add rating to table
        try{
        Connection connection=DriverManager.getConnection(URL,username,password);}catch(SQLException e){
            System.out.println("Error: "+e);
        }
    }
}
