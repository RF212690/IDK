package com.company;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import static java.lang.Integer.parseInt;

public class Main {
    public static int[][] board=new int[8][8];
    public static Hashtable<Integer,String> Pieces=new Hashtable<>();



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
            } catch (Exception e) {
                System.out.println("Error: " + e);
                input.next();
            }
            loggedIn = login(username, password);
        }
        }
        System.out.println("Press S to change the settings and G to start a game against a bot,\nenter EXIT at anytime to exit program");
        while(true){
        String answer=getInput();
        if(answer.equalsIgnoreCase("s")){
            settings();

        }else if(answer.equalsIgnoreCase("g")){
            break;
        }
        else{
            System.out.println("not an option, try again.");
        }
        }
        int elo=getElo(username);
        setBoard(0);
        setHashTable();
        Player player1=new Player(random(1), elo,username);
        if(player1.getPlayerColor()==0){
        Player player2=new Player(1,1000,"Bot");
            displayBoard(player1,player2);
        } else{
            Player player2=new Player(0,1000,"Bot");
            displayBoard(player2,player1);
        }
        //game loop
    }
    public static void settings(){
        //make settings
    }
    public static String getInput(){
        Scanner input=new Scanner(System.in);
        try{
            String answer= input.next();
            if(answer.equalsIgnoreCase("exit")){
                System.exit(0);
            }
            return answer;
        }catch(Exception e){
            return "Error";
        }
    }
    public static void setBoard(int setup){
        if(setup==0) {
            for (int i = 0; i < 8; i++) {
                board[i][1] = 0;
                board[i][6] = 1;
                board[i][5] = -1;
                board[i][4] = -1;
                board[i][3] = -1;
                board[i][2] = -1;
                if (i < 3) {
                    board[i][0] = i * 2 + 2;
                    board[i][7] = i * 2 + 3;
                } else {
                    board[i][0] = (7 - i) * 2 + 2;
                    board[i][7] = (7 - i) * 2 + 3;
                }
            }
        }
    }
    public static void displayBoard(Player White, Player Black){
        System.out.println(Black.getPlayerName()+"     "+Black.getPlayerRating());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(Pieces.get(board[j][7-i])+" ");
            }
            System.out.println();
        }
        System.out.println(White.getPlayerName()+"     "+White.getPlayerRating());
    }
    public static void setHashTable(){
        Pieces.put(0,"WP");Pieces.put(1,"BP");Pieces.put(2,"WR");Pieces.put(3,"BR");Pieces.put(4,"WN");
        Pieces.put(5,"BN");Pieces.put(6,"WB");Pieces.put(7,"BB");Pieces.put(8,"WK");Pieces.put(9,"BK");
        Pieces.put(10,"WQ");Pieces.put(11,"BQ");Pieces.put(-1,"VO");
    }
    public static void PlayerMove(Player Player){
        boolean moveIsLegal=false;
        Scanner input=new Scanner(System.in);
        int xCor=-1;
        int yCor=-1;
        do {
            do {
                System.out.println("Select X coordinate of piece you want to move: ");
                try {
                    xCor = input.nextInt();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                if (xCor < 1 || xCor > 9) {
                    System.out.println("Not a valid input");
                }
            } while (xCor < 1 || xCor > 9);
            do {
                System.out.println("Select Y coordinate of piece you want to move: ");
                try {
                    yCor = input.nextInt();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                if (yCor < 1 || yCor > 9) {
                    System.out.println("Not a valid input");
                }
            } while (yCor < 1 || yCor > 9);
            if(board[xCor-1][8-yCor]==-1){
                System.out.println("no piece in that square");
            }else{
                int piece=board[xCor-1][8-yCor];
                if(piece%2!= Player.getPlayerColor()){
                    System.out.println("This is an enemy piece");
                }
            }
            //ask player where he wants to move that piece, then check if its legal(check for castle, check if any next enemy move can kill king)
        }while(!moveIsLegal);

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
    public static boolean register(String username,String password){
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
    public static void createDatabase(){
        String URL="C:\\Users\\rf212690\\IdeaProjects\\IDK\\";
        String username="username";
        String password="password";
        String rating="rating";//add rating to database
        try{
        Connection connection=DriverManager.getConnection(URL,username,password);}catch(SQLException e){
            System.out.println("Error: "+e);
        }
    }
}
