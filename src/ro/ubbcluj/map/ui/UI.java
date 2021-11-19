package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validator.ValidatorException;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.service.Service;
import ro.ubbcluj.map.service.ServiceException;
import ro.ubbcluj.map.utils.Pair;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {
    private Service serv;

    public UI(Service s) {
        serv = s;
    }

    private void printMeniu() {
        System.out.println("\t\t\t\t\t\t\t\t\t\tSocial Network\t\t\t\t\t\t\t\t\t\t\t\t\n");
        System.out.println("0.  Exit");
        System.out.println("1.Add user\n" +
                "2.Update user\n" +
                "3.Delete user\n" +
                "4.Add friendship to user\n" +
                "5.Delete friendship from user\n" +
                "6.Number of communities\n"+
                "7.Largest community\n"+
                "8.Find friends for user\n"+
                "9.Login");
        System.out.println("Command: ");
    }

    private int readCmd() {

        Scanner scanner = new Scanner(System.in);
        int cmd = scanner.nextInt();
        return cmd;
    }

    private Long readLong(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLong();
    }

    private String readString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLine();
    }

    private void printSubMeniu(User user){
        String userName = user.getFirstName()+" "+user.getLastName();
        System.out.println("\t\t\t\t\t\t\t\t\t\tWelcome ~~~"+userName+"~~~\t\t\t\t\t\t\t\t\t\t\t\t\n");
        System.out.println("0.  Exit");
        System.out.println("1.Show friend requests\n" +
                "2.Accept one\n" +
                "3.Accept all\n" +
                "4.Reject one\n" +
                "5.Reject all\n");
        System.out.println("Command: ");
    }

    private void execute(int cmd){

        switch (cmd) {
            case 1 -> {
                addUser();
            }
            case 2 -> {
                updateUser();
            }
            case 3 -> {
                deleteUser();
            }
            case 4 -> {
                addFriendship();
            }
            case 5 -> {
                deleteFriendship();
            }
            case 6 -> {
                communitiesNumber();
            }
            case 7 -> {
                largestCommunity();
            }
            case 8 -> {
                findFriendsforUser();
            }
            case 9 -> {
                login();
            }
            default ->  System.out.println("Command not found!\n");
        }
    }

    private void execute_user_comand(int cmd,User user){
        switch (cmd) {
            case 1 -> {
                System.out.println("Show all friend requests!");
                showAllFriendRequests(user);
            }
            case 2 -> {
                System.out.println("Accept one friend request!");
                acceptOneRequest(user);
            }
            case 3 -> {
                System.out.println("Accept all friend requests!");
                acceptAllRequests(user);
            }
            case 4 -> {
                System.out.println("Reject one friend request");
                rejectOneRequest(user);
            }
            case 5 -> {
                System.out.println("Reject all friend requests!");
                rejectAllRequests(user);
            }
            default ->  System.out.println("Command not found!\n");
        }
    }

    private void rejectAllRequests(User user){
        List<Long> friends= serv.findFriendRequests(serv.getIdFromEmail(user.getEmail()));
        String email1, email2;
        email1 = user.getEmail();
        for(Long id:friends){
            Pair pair = new Pair(serv.getIdFromEmail(email1),id);
            serv.updateFriendship(pair,"rejected");
        }
    }

    private void acceptAllRequests(User user){
        List<Long> friends= serv.findFriendRequests(serv.getIdFromEmail(user.getEmail()));
        String email1, email2;
        email1 = user.getEmail();
        for(Long id:friends){
            Pair pair = new Pair(serv.getIdFromEmail(email1),id);
            serv.updateFriendship(pair,"approved");
        }

    }

    private void rejectOneRequest(User user){
        String email1,email2;
        email1= user.getEmail();
        email2=readString("Enter friend to be reject : ");
        Pair pair = new Pair(serv.getIdFromEmail(email1),serv.getIdFromEmail(email2));
        serv.updateFriendship(pair,"rejected");
        System.out.println("Friendship rejected!");
    }

    private void acceptOneRequest(User user){
        String email1,email2;
        email1= user.getEmail();
        email2=readString("Enter friend to be add : ");
        Pair pair = new Pair(serv.getIdFromEmail(email1),serv.getIdFromEmail(email2));
        serv.updateFriendship(pair,"approved");
        System.out.println("Friendship accepted!");
    }

    private void showAllFriendRequests(User user){
        List<Long> friends= serv.findFriendRequests(serv.getIdFromEmail(user.getEmail()));
        friends.stream()
                .map(x->serv.findOneUser(x))
                .filter(x->serv.FriendshipStatus(x.getId(), user.getId()).equals("pending"))
                .map(x-> x.getFirstName()+" "+x.getLastName()+" wants to be your friend!")
                .forEach(System.out::println);
    }

    private void login(){
        String email;
        email=readString("Enter user email : ");
        User user = serv.findOneUser(serv.getIdFromEmail(email));
        //printSubMeniu(user);
        int cmd;
        while (true) {
            try {
                printSubMeniu(user);
                cmd = readCmd();
                if (cmd == 0)
                    return;
                execute_user_comand(cmd,user);
            } catch (RepositoryException re) {
                System.out.println(re.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println("\nData type not valid!\n");
            } catch (ValidatorException ve) {
                System.out.println(ve.getMessage());
            } catch (ServiceException se) {
                System.out.println(se.getMessage());
            }
        }
    }

    private void findFriendsforUser() {
        String email;
        email=readString("Enter user email : ");
        serv.findFriendsForUser(serv.getIdFromEmail(email))
                .forEach(System.out::println);
    }

    private void largestCommunity() {
        for(Long id:serv.largestCommunity())
            System.out.println(serv.getEmailFromId(id));
    }

    private void communitiesNumber(){
        int nr =serv.communitiesNumber();
        System.out.println(nr+"\n");
    }
    private void deleteFriendship() {
        String email1,email2;
        email1=readString("Enter user email : ");
        email2=readString("Enter friend to be deleted : ");
        serv.deleteFriendship(serv.getIdFromEmail(email1), serv.getIdFromEmail(email2));
    }

    private void addFriendship() {
        String email1,email2;
        email1=readString("Enter user email : ");
        email2=readString("Enter friend to be add : ");
        serv.addFriendship(serv.getIdFromEmail(email1), serv.getIdFromEmail(email2));
    }

    private void deleteUser() {
        String email;
        email=readString("Enter user email : ");
        User us= serv.deleteUser(serv.getIdFromEmail(email));
    }

    private void updateUser() {
        String firstName,lastName,email;
        email=readString("Enter email : ");
        firstName= readString("Enter first name : ");
        lastName= readString("Enter last name : ");
        serv.findOneUser(serv.getIdFromEmail(email));
        User us= serv.updateUser(serv.getIdFromEmail(email),firstName,lastName,email);
    }

    private void addUser() {
        String firstName,lastName,email;
        firstName= readString("Enter first name : ");
        lastName= readString("Enter last name : ");
        email=readString("Enter email : ");
        User us= serv.addUser(firstName,lastName,email);
    }

    public void run(){
        int cmd;
        while (true) {
            try {
                printMeniu();
                cmd = readCmd();
                if (cmd == 0)
                    return;
                execute(cmd);
            } catch (RepositoryException re) {
                System.out.println(re.getMessage());
            } catch (InputMismatchException ime) {
                System.out.println("\nData type not valid!\n");
            } catch (ValidatorException ve) {
                System.out.println(ve.getMessage());
            } catch (ServiceException se) {
                System.out.println(se.getMessage());
            }
        }
    }
}