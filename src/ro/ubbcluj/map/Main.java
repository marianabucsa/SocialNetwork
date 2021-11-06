package ro.ubbcluj.map;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validator.FriendshipValidator;
import ro.ubbcluj.map.domain.validator.UserValidator;
import ro.ubbcluj.map.domain.validator.ValidatorException;
import ro.ubbcluj.map.repository.Repository;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.repository.file.FriendshipsFileRepository;
import ro.ubbcluj.map.repository.file.UserFileRepository;
import ro.ubbcluj.map.service.Service;
import ro.ubbcluj.map.service.ServiceException;
import ro.ubbcluj.map.ui.UI;
import ro.ubbcluj.map.utils.Pair;

import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
        try {
            UserValidator userValidator = new UserValidator();
            FriendshipValidator friendshipValidator = new FriendshipValidator();
            Repository<Long, User> userFileRepository = new UserFileRepository(userValidator, "data/users.csv");
            Repository<Pair, Friendship> friendshipsFileRepository = new FriendshipsFileRepository(friendshipValidator, "data/friendships.csv");
            Service service = new Service(friendshipsFileRepository, userFileRepository);
            UI ui = new UI(service);
            ui.run();
        } catch (RepositoryException re) {
            System.out.println(re.getMessage());
        } catch (InputMismatchException ime) {
            System.out.println("\nData type not valid!\n");
        } catch (ValidatorException ve) {
            System.out.println(ve.getMessage());
        } catch (ServiceException se) {
            System.out.println(se.getMessage());
        } catch (NullPointerException ne) {
            System.out.println(ne.getMessage());
        }
    }
}
