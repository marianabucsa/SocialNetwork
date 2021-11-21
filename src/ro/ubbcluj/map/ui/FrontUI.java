package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.service.Service;

public class FrontUI extends AbstractUI {

    /**
     * ui constructor
     * @param serv - a service object
     */
    public FrontUI(Service serv) {
        super(serv);
        commands = "1.Admin\n" +
                "2.Login user\n" +
                "3.Sign up user\n";
    }

    @Override
    public int readCmd() {
        return super.readCmd();
    }

    @Override
    public Long readLong(String message) {
        return super.readLong(message);
    }

    @Override
    public String readString(String message) {
        return super.readString(message);
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public void printMenu() {
        super.printMenu();
    }

    @Override
    public void execute(int cmd) {
        switch (cmd) {
            case 1 -> {
                AdminUI adminUI = new AdminUI(serv);
                adminUI.run();
            }
            case 2 -> {
                UserUI userUI = new UserUI(serv, readString("Enter user email: "));
                userUI.run();
            }
            case 3 -> {
                addUser();
            }
            default -> System.out.println("Command not found!\n");
        }
    }

    /**
     * reads data and adds a user to the database
     */
    private void addUser() {
        String firstName, lastName, email;
        firstName = readString("Enter first name : ");
        lastName = readString("Enter last name : ");
        email = readString("Enter email : ");
        User us = serv.addUser(firstName, lastName, email);
    }
}
