package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validator.ValidatorException;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.service.Service;
import ro.ubbcluj.map.service.ServiceException;

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
                "7.Largest community\n");
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
            default ->  System.out.println("Command not found!\n");
        }
    }

    private void largestCommunity() {
        List<Long> path = serv.largestCommunity();
        if(path != null) {
            for (Long u : path) {
                System.out.println(u);
            }
        }
    }

    private void communitiesNumber(){
        int nr =serv.communitiesNumber();
        System.out.println(nr+"\n");
    }
    private void deleteFriendship() {
        Long id1,id2;
        id1=readLong("Enter user id : ");
        id2=readLong("Enter friend to be deleted : ");
        serv.deleteFriendship(id1,id2);
    }

    private void addFriendship() {
        Long id1,id2;
        id1=readLong("Enter user id : ");
        id2=readLong("Enter friend to be added : ");
        serv.addFriendship(id1,id2);
    }

    private void deleteUser() {
        Long id;
        id=readLong("Enter user id : ");
        User us= serv.deleteUser(id);
    }

    private void updateUser() {
        Long id;
        String firstName,lastName,email;
        id=readLong("Enter user id : ");
        firstName= readString("Enter first name : ");
        lastName= readString("Enter last name : ");
        email=readString("Enter email : ");
        User us= serv.updateUser(id,firstName,lastName,email);
    }

    private void addUser() {
        Long id;
        String firstName,lastName,email;
        id=readLong("Enter user id : ");
        firstName= readString("Enter first name : ");
        lastName= readString("Enter last name : ");
        email=readString("Enter email : ");
        User us= serv.addUser(id,firstName,lastName,email);
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
