package ro.ubbcluj.map.ui;

public interface UI {
    /**
     * reads a command from console
     *
     * @return - an int
     */
    int readCmd();

    /**
     * reads a number from console
     *
     * @return - a long
     */
    Long readLong(String message);

    /**
     * reads a text from console
     *
     * @return - a string
     */
    String readString(String message);

    /**
     * starts the application
     */
    void run();

    /**
     * prints the menu
     */
    void printMenu();

    /**
     * executes a command from console
     */
    void execute(int cmd);

}
