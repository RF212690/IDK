package com.company;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;
import static java.lang.Integer.parseInt;

public class Main {
    public static int[][] board=new int[8][8];
    public static Hashtable<Integer,String> Pieces=new Hashtable<Integer,String>();
    int whiteQueenCastle;
    int whiteKingCastle;
    int blackQueenCastle;
    int blackKingCastle;



    public static void main(String[] args){
        //createDatabase();
        String username="";
        boolean loggedIn=false;
        while(!loggedIn){
            if(regOrLog()=='r'){
                Scanner input = new Scanner(System.in);
                System.out.println("Insert username: ");
                try {
                    username = input.next();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    input.next();
                }
                System.out.println("Insert password: ");
                String password = "";
                try {
                    password = input.next();
                    //hash password
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    input.next();
                }
                loggedIn=register(username,password);
            }else {
                Scanner input = new Scanner(System.in);
                System.out.println("Insert username: ");
                try {
                    username = input.next();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    input.next();
                }
                System.out.println("Insert password: ");
                String password = "";
                try {
                    password = input.next();
                    //hash password
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                    input.next();
                }
                loggedIn = login(username, password);
            }
        }
        //ask if user wants to start game first or change settings
        int elo=getElo(username);
        Player player1=new Player(random(1), elo,username);
        if(player1.getPlayerColor()==0){
            Player player2=new Player(1,1000,"Bot");
        } else{
            Player player2=new Player(0,1000,"Bot");
        }
        setHashTable();
        setBoard(0);
        displayBoard();//make it print elo and name of players
        //game loop
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
            else {board[i][0]=(7-i)*2+2;board[i][7]=(7-i)*2+3;
            }
        }
    }
    public static void displayBoard(){
        for (int i = 0; i < 8; i++) {
            System.out.print(8-i+"  ");
            for (int j = 0; j < 8; j++) {
                System.out.print(Pieces.get(board[j][7-i])+" ");
            }
            System.out.println();
        }
        System.out.println("   1  2  3  4  5  6  7  8");
    }
    public static void setHashTable(){
        Pieces.put(0,"WP");Pieces.put(1,"BP");Pieces.put(2,"WR");Pieces.put(3,"BR");Pieces.put(4,"WN");
        Pieces.put(5,"BN");Pieces.put(6,"WB");Pieces.put(7,"BB");Pieces.put(8,"WK");Pieces.put(9,"BK");
        Pieces.put(10,"WQ");Pieces.put(11,"BQ");Pieces.put(-1,"VO");
    }
    public static void PlayerMove(int playerColor){  Scanner input=new Scanner(System.in);
        int piece=0;
        int xPiece =-1;
        int yPiece =0;
        int xCor=0;
        int yCor=0;
        while(true) {
            do {
                System.out.println("Select X coordinate of piece you want to move: ");
                try {
                    xPiece = input.nextInt();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                if (xPiece < 1 || xPiece > 8) {
                    System.out.println("Not a valid input");
                }
            } while (xPiece < 1 || xPiece > 8);
            do {
                System.out.println("Select y coordinate of piece you want to move: ");
                try {
                    yPiece = input.nextInt();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                if (yPiece < 1 || yPiece > 8) {
                    System.out.println("Not a valid input");
                }
            } while (yPiece < 1 || yPiece > 8);
            piece = board[xPiece][yPiece - 1];
            if (piece % 2 != playerColor) {
                System.out.println("Invalid Piece position");
            } else break;
            ArrayList<int[]>moves=getLegalMoves(xPiece,yPiece);
        }
        do{
            System.out.println("Select X coordinate of where you want to move the piece: ");
            try{
                xCor=input.nextInt();}
            catch(Exception e){
                System.out.println("Error: "+e);
            }
            if(xCor<1 || xCor>8){
                System.out.println("Not a valid input");
            }
        }while(xCor<0 || xCor>8);
        do{
            System.out.println("Select y coordinate of where you want to move the piece: ");
            try{
                yCor=input.nextInt();}
            catch(Exception e){
                System.out.println("Error: "+e);
            }
            if(yCor<1 || yCor>8){
                System.out.println("Not a valid input");
            }
        }while(yCor<0 || yCor>8);
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
    public static boolean register(String username,String password){
        boolean found=false;
        if(username.contains("-") || password.contains("-")){
            System.out.println("Invalid username/password,usernames and passwords must not contain the symbol \"-\" ");
            return false;
        }
        File userFile=new File("user.txt");
        try{
            FileWriter writer=new FileWriter(userFile.getName(),true);
            if(userFile.createNewFile()){
                System.out.println("n");
                writer.write("username---password---elo\n");
            }
            Scanner userInfo= new Scanner(userFile);
            userInfo.next();
            while (userInfo.hasNextLine()){
                String[] userinfo=userInfo.nextLine().split("---");
                if(userinfo[0].equals(username)){
                    System.out.println("username already in use");
                    return false;
                }
            }
            writer.write(username+"---"+password+"---1000\n");
            writer.close();
            return true;
        }
        catch(IOException e){
            System.out.println("Error: "+e);
        }
        return false;
    }
    public static boolean login(String username,String password){
        File userFile=new File("user.txt");
        try{
            FileWriter writer=new FileWriter(userFile.getName(),true);
            if(userFile.createNewFile()){
                writer.write("username---password---elo");
            }
            writer.close();
        }
        catch(IOException e){
            System.out.println("Error: "+e);
        }
        try{
            Scanner userInfo= new Scanner(userFile);
            userInfo.next();
            while (userInfo.hasNextLine()){
                String[] userinfo=userInfo.nextLine().split("---");
                if(userinfo[0].equals(username) && userinfo[1].equals(password)){
                    System.out.println("login successful");
                    return true;
                }
            }
        }
        catch(IOException e){
            System.out.println("Error: "+e);
        }
        System.out.println("account not found/password does not match username. Try again");
        return false;
    }
    public static int getElo(String username){
        File userFile=new File("user.txt");
        try{
            Scanner userInfo= new Scanner(userFile);
            userInfo.next();
            while (userInfo.hasNextLine()){
                String[] userinfo=userInfo.nextLine().split("---");
                if(userinfo[0].equals(username)){
                    return parseInt(userinfo[2]);
                }
            }
        }catch(IOException e){
            System.out.println("Error: "+e);
        }
        return -1;
    }
    public static ArrayList<int[]> getLegalMoves(int xPosition,int yPosition){
        ArrayList<int[]>moves=new ArrayList<>();
        int Piece=board[xPosition][yPosition];
        int PlayerColor=Piece%2;
        int direction;
        if(Piece-PlayerColor==0){
            if (PlayerColor==1){
                direction=-1;
            }else{direction=1;}
            if(board[xPosition-1][yPosition+direction]%2+PlayerColor==1){
                moves.add(new int[]{xPosition-1, yPosition+direction});
            }
            if(board[xPosition+1][yPosition+direction]%2+PlayerColor==1){
                moves.add(new int[]{xPosition+1, yPosition+direction});
            }
            if(board[xPosition][yPosition+direction]==-1){
                moves.add(new int[]{xPosition,yPosition+direction});
            }
            if(direction<0){
                if(yPosition==6 && board[xPosition][yPosition+2*direction]==-1){
                    moves.add(new int[]{xPosition,yPosition+2*direction});
                }
            }else{
                if(yPosition==6 && board[xPosition][yPosition+2*direction]==-1){
                    moves.add(new int[]{xPosition,yPosition+2*direction});
                }
            }
        }
        if(Piece-PlayerColor==2){
            int topStop=8;
            int bottomStop=8;
            int rightStop=8;
            int leftStop=8;
            for (int i = 1; i < 7; i++) {
                if(i+xPosition<rightStop && board[xPosition+i][yPosition]%2+PlayerColor==1){
                    moves.add(new int[]{xPosition+i,yPosition});
                    if(board[xPosition+i][yPosition]!=-1){
                        rightStop=xPosition+i+1;
                    }
                }
                if(xPosition+i<leftStop && board[xPosition-i][yPosition]%2+PlayerColor==1){
                    moves.add(new int[]{xPosition+i,yPosition});
                    if(board[xPosition-i][yPosition]!=-1){
                        leftStop=xPosition+i+1;
                    }
                }
                if(i+xPosition<rightStop && board[xPosition+i][yPosition]%2+PlayerColor==1){
                    moves.add(new int[]{xPosition+i,yPosition});
                    if(board[xPosition+i][yPosition]!=-1){
                        rightStop=xPosition+i+1;
                    }
                }
                if(i+xPosition<rightStop && board[xPosition+i][yPosition]%2+PlayerColor==1){
                    moves.add(new int[]{xPosition+i,yPosition});
                    if(board[xPosition+i][yPosition]!=-1){
                        rightStop=xPosition+i+1;
                    }
                }
            }
        }
        return moves;
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