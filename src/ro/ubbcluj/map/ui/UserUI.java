package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.ReplyMessage;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validator.ValidatorException;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.service.Service;
import ro.ubbcluj.map.service.ServiceException;
import ro.ubbcluj.map.utils.Pair;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

public class UserUI extends AbstractUI {
    private final String userEmail;

    /**
     * ui constructor
     *
     * @param serv  - a service object
     * @param email - an email of a user, string
     */
    public UserUI(Service serv, String email) {
        super(serv);
        userEmail = email;
        commands = "1.Update name\n" +
                "2.Delete account\n" +
                "3.Add friend\n" +
                "4.Delete friend\n" +
                "5.Show friends\n" +
                "6.Show friends by month\n" +
                "7.Send message\n" +
                "8.Reply message\n" +
                "9.ReplyAll message\n" +
                "10.Delete message\n" +
                "11.Show conversation\n" +
                "12.Show friends requests\n" +
                "13.Accept one request\n" +
                "14.Accept all requests\n" +
                "15.Reject one request\n" +
                "16.Reject all requests\n";
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
        int cmd;
        while (true) {
            try {
                printMenu();
                cmd = readCmd();
                if (cmd == 0)
                    return;
                else {
                    if (cmd == 2) {
                        execute(cmd);
                        return;
                    } else
                        execute(cmd);
                }
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
        super.printMenu();
    }

    @Override
    public void execute(int cmd) {
        switch (cmd) {
            case 1 -> {
                updateUser();
            }
            case 2 -> {
                deleteUser();
            }
            case 3 -> {
                addFriendship();
            }
            case 4 -> {
                deleteFriendship();
            }
            case 5 -> {
                findFriendsForUser();
            }
            case 6 -> {
                findFriendsByMonth();
            }
            case 7 -> {
                sendMessage();
            }
            case 8 -> {
                replyMessage();
            }
            case 9 -> {
                replyAllMessage();
            }
            case 10 -> {
                deleteMessage();
            }
            case 11 -> {
                showConversation();
            }
            case 12 -> {
                showAllFriendRequests();
            }
            case 13 -> {
                acceptOneRequest();
            }
            case 14 -> {
                acceptAllRequests();
            }
            case 15 -> {
                rejectOneRequest();
            }
            case 16 -> {
                rejectAllRequests();
            }
            default -> System.out.println("Command not found!\n");
        }
    }

    /**
     * update name for current user
     */
    private void updateUser() {
        String firstName, lastName;
        firstName = readString("Enter first name : ");
        lastName = readString("Enter last name : ");
        serv.findOneUser(serv.getIdFromEmail(userEmail));
        User us = serv.updateUser(serv.getIdFromEmail(userEmail), firstName, lastName, userEmail);
        System.out.println("User successfully updated!\n");
    }

    /**
     * delete current user
     */
    private void deleteUser() {
        User us = serv.deleteUser(userEmail);
        System.out.println("User successfully deleted!\n");
    }

    /**
     * send a friend request for current user
     */
    private void addFriendship() {
        String friendEmail;
        friendEmail = readString("Enter friend to be add : ");
        serv.addFriendship(userEmail, friendEmail);
        System.out.println("Request was sent!\n");
    }

    /**
     * delete a friend for current user
     */
    private void deleteFriendship() {
        String friendEmail;
        friendEmail = readString("Enter friend to be deleted : ");
        serv.deleteFriendship(userEmail, friendEmail);
        System.out.println("Friend successfully deleted!\n");
    }

    /**
     * find friends for current user
     */
    private void findFriendsForUser() {
        serv.findFriendsForUser(serv.getIdFromEmail(userEmail))
                .forEach(System.out::println);
    }

    /**
     * find friends by month for current user
     */
    private void findFriendsByMonth() {
        Long month = readLong("Enter month: ");
        serv.findFriendsByMonth(userEmail, month).forEach(System.out::println);
    }

    /**
     * send message from current user
     */
    private void sendMessage() {
        String emailTo = readString("Enter receiver email or 0 to end receivers:");
        List<String> to = new ArrayList<>();
        while (!Objects.equals(emailTo, "0")) {
            to.add(emailTo);
            emailTo = readString("Enter receiver email or 0 to end receivers:");
        }
        String message = readString("Write message: \n");
        serv.sendMessage(userEmail, to, message);
        System.out.println("Message sent!\n");
    }

    /**
     * send reply from current user
     */
    private void replyMessage() {
        for (ReplyMessage replyMessage : serv.getToReplyForUser(userEmail)) {
            System.out.print("Message: "+ replyMessage.getId()+"\n" +
                    "From: " + serv.getEmailFromId(replyMessage.getFrom()) + "\n" +
                    "To: " );
            for(Long id: replyMessage.getTo())
                System.out.print(serv.getEmailFromId(id)+" ");
            System.out.println("\nData: " + replyMessage.getData() + "\n\n"
                    + replyMessage.getMessage() + "\n");
        }
        Long idMsg = readLong("Enter id of message to reply: ");
        String message = readString("Write message: \n");
        serv.replyMessage(userEmail, idMsg, message);
        System.out.println("Message sent!\n");
    }

    /**
     * send reply all from current user
     */
    private void replyAllMessage() {
        for (ReplyMessage replyMessage : serv.getToReplyForUser(userEmail)) {
            System.out.print("Message: "+ replyMessage.getId()+"\n" +
                    "From: " + serv.getEmailFromId(replyMessage.getFrom()) + "\n" +
                    "To: " );
            for(Long id: replyMessage.getTo())
                System.out.print(serv.getEmailFromId(id)+" ");
            System.out.println("\nData: " + replyMessage.getData() + "\n\n"
                    + replyMessage.getMessage() + "\n");
        }
        Long idMsg = readLong("Enter id of message to reply: ");
        String message = readString("Write message: \n");
        serv.replyAllMessage(userEmail, idMsg, message);
        System.out.println("Message sent!\n");
    }

    /**
     * delete message for current user
     */
    private void deleteMessage() {
        for (ReplyMessage replyMessage : serv.getMessagesForUser(userEmail)) {
            System.out.print("Message: "+ replyMessage.getId()+"\n" +
                    "From: " + serv.getEmailFromId(replyMessage.getFrom()) + "\n" +
                    "To: " );
            for(Long id: replyMessage.getTo())
                System.out.print(serv.getEmailFromId(id)+" ");
            System.out.println("\nData: " + replyMessage.getData() + "\n\n"
                    + replyMessage.getMessage() + "\n");
        }
        Long idMsg = readLong("Enter id of message to delete: ");
        serv.deleteMessage(idMsg);
        System.out.println("Message successfully deleted!\n");
    }

    /**
     * show conversation for current user
     */
    private void showConversation() {
        String friendEmail = readString("Enter user email: ");
        for (ReplyMessage replyMessage : serv.showConversation(userEmail, friendEmail)) {
            System.out.print("Message:\n" +
                    "From: " + serv.getEmailFromId(replyMessage.getFrom()) + "\n" +
                    "To: " );
            for(Long id: replyMessage.getTo())
                System.out.print(serv.getEmailFromId(id)+" ");
            System.out.println("\nData: " + replyMessage.getData() + "\n\n"
                    + replyMessage.getMessage() + "\n");
        }
    }

    /**
     * show friendship requests for current user
     */
    private void showAllFriendRequests() {
        List<Long> friends = serv.findFriendRequests(serv.getIdFromEmail(userEmail));
        friends.stream()
                .map(x -> serv.findOneUser(x))
                .filter(x -> serv.FriendshipStatus(x.getId(), serv.getIdFromEmail(userEmail)).equals("pending"))
                .map(x -> x.getFirstName() + " " + x.getLastName() + " wants to be your friend!")
                .forEach(System.out::println);
    }

    /**
     * accept one request for current user
     */
    private void acceptOneRequest() {
        String emailRequest;
        emailRequest = readString("Enter friend to be added : ");
        Pair pair = new Pair(serv.getIdFromEmail(userEmail), serv.getIdFromEmail(emailRequest));
        serv.updateFriendship(pair, "approved");
        System.out.println("Friendship accepted!");
    }

    /**
     * accept all requests for current user
     */
    private void acceptAllRequests() {
        List<Long> friends = serv.findFriendRequests(serv.getIdFromEmail(userEmail));
        for (Long id : friends) {
            Pair pair = new Pair(serv.getIdFromEmail(userEmail), id);
            serv.updateFriendship(pair, "approved");
        }

    }

    /**
     * reject one request for current user
     */
    private void rejectOneRequest() {
        String emailRequest;
        emailRequest = readString("Enter friend to be reject : ");
        Pair pair = new Pair(serv.getIdFromEmail(userEmail), serv.getIdFromEmail(emailRequest));
        serv.updateFriendship(pair, "rejected");
        System.out.println("Friendship rejected!");
    }

    /**
     * reject all requests for current user
     */
    private void rejectAllRequests() {
        List<Long> friends = serv.findFriendRequests(serv.getIdFromEmail(userEmail));
        for (Long id : friends) {
            Pair pair = new Pair(serv.getIdFromEmail(userEmail), id);
            serv.updateFriendship(pair, "rejected");
        }
    }
}
