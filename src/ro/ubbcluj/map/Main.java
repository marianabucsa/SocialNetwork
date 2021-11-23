package ro.ubbcluj.map;

import ro.ubbcluj.map.domain.validator.*;
import ro.ubbcluj.map.repository.DB.FriendshipsDBRepository;
import ro.ubbcluj.map.repository.DB.MessagesDBRepository;
import ro.ubbcluj.map.repository.DB.UserDBRepository;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.service.Service;
import ro.ubbcluj.map.service.ServiceException;
import ro.ubbcluj.map.ui.FrontUI;

import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
        try {
            UserValidator userValidator = new UserValidator();
            FriendshipValidator friendshipValidator = new FriendshipValidator();
            MessageValidator messageValidator = new MessageValidator();
            EmailValidator emailValidator = new EmailValidator();
            UserDBRepository userDBRepository = new UserDBRepository("jdbc:postgresql://localhost:5432/Network", "postgres", "luceafarul1", userValidator);
            FriendshipsDBRepository friendshipsDBRepository = new FriendshipsDBRepository("jdbc:postgresql://localhost:5432/Network", "postgres", "luceafarul1", friendshipValidator);
            MessagesDBRepository messagesDBRepository = new MessagesDBRepository("jdbc:postgresql://localhost:5432/Network", "postgres", "luceafarul1", messageValidator);
            Service service = new Service(friendshipsDBRepository, userDBRepository, messagesDBRepository, emailValidator);
            FrontUI frontUI=new FrontUI(service);
            frontUI.run();
        } catch (ValidatorException ve) {
            System.out.println(ve.getMessage());
        } catch (ServiceException se) {
            System.out.println(se.getMessage());
        } catch (RepositoryException re) {
            System.out.println(re.getMessage());
        } catch (InputMismatchException ime) {
            System.out.println("\nData type not valid!\n");
        }

    }
}
