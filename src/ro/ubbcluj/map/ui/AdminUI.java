package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.service.Service;

public class AdminUI extends AbstractUI {
    /**
     * ui constructor
     * @param serv - a service object
     */
    public AdminUI(Service serv) {
        super(serv);
        commands = "1.Number of communities\n" +
                "2.Largest community\n";
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
                communitiesNumber();
            }
            case 2 -> {
                largestCommunity();
            }
            default -> System.out.println("Command not found!\n");
        }
    }

    /**
     * shows the largest community of users
     */
    private void largestCommunity() {
        for (Long id : serv.largestCommunity())
            System.out.println(serv.getEmailFromId(id));
    }

    /**
     * shows the number of communities
     */
    private void communitiesNumber() {
        int nr = serv.communitiesNumber();
        System.out.println(nr + "\n");
    }
}
