package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.validator.ValidatorException;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.service.Service;
import ro.ubbcluj.map.service.ServiceException;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class AbstractUI implements UI {
    protected Service serv;
    protected String commands;

    /**
     * ui constructor
     *
     * @param serv - a service object
     */
    public AbstractUI(Service serv) {
        this.serv = serv;
    }

    @Override
    public int readCmd() {
        Scanner scanner = new Scanner(System.in);
        int cmd = scanner.nextInt();
        return cmd;
    }

    @Override
    public Long readLong(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLong();
    }

    @Override
    public String readString(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        return scanner.nextLine();
    }

    @Override
    public void run() {
        int cmd;
        while (true) {
            try {
                printMenu();
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


    @Override
    public void printMenu() {
        System.out.println("\t\t\t\t\t\t\t\t\t\tMENU\t\t\t\t\t\t\t\t\t\t\t\t\n");
        System.out.println("0.Exit");
        System.out.println(this.commands);
        System.out.println("Command: ");
    }

    @Override
    public abstract void execute(int cmd);
}
