package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.repository.Repository;
import ro.ubbcluj.map.utils.NetworkGraph;
import ro.ubbcluj.map.utils.Pair;

import java.util.List;

public class Service {
    private final Repository<Pair, Friendship> friendshipsRepo;
    private final Repository<Long, User> userRepo;

    /**
     * service constructor
     *
     * @param friendshipsRepo - repository for friendships
     * @param userRepo        - repository for users
     */
    public Service(Repository<Pair, Friendship> friendshipsRepo, Repository<Long, User> userRepo) {
        this.friendshipsRepo = friendshipsRepo;
        this.userRepo = userRepo;
        //connectUsersFriendships();
    }

    /**
     * methode to connect the friendships to the users
     */
    public void connectUsersFriendships() {
        for (Friendship friendship : friendshipsRepo.findAll()) {
            User u1 = userRepo.findOne(friendship.getPair().getId1());
            User u2 = userRepo.findOne(friendship.getPair().getId2());
            if (u1 == null || u2 == null) {
                throw new ServiceException("No user for friendship!\n");
            }
            u1.addFriend(u2.getId());
            u2.addFriend(u1.getId());
        }
    }

    /**
     * methode for the number of social communities
     *
     * @return - an int
     */
    public int communitiesNumber() {
        NetworkGraph<Long, User> network = new NetworkGraph(userRepo.getAllData());
        return network.connectedComponents();
    }

    /**
     * methode for the largest social community
     *
     * @return - a list of users
     */
    public List<Long> largestCommunity() {
        NetworkGraph<Long, User> network = new NetworkGraph(userRepo.getAllData());
        return network.longestPath();
    }

    /**
     * finds the user with the id
     *
     * @param id - id of the user
     * @return - a user if the user is fond
     * @throws ServiceException if the user is not fond
     */
    public User findOneUser(Long id) {
        User us = userRepo.findOne(id);
        if (us == null)
            throw new ServiceException("User does not exist!\n");
        return us;
    }

    /**
     * adds a user
     *
     * @param -         id of the user
     * @param firstName - first nema of the user
     * @param lastName  - last name of the user
     * @param email     - email of the user
     * @return - the user if it was successfully added
     * @throws ServiceException if the user already exists
     */
    public User addUser(Long id, String firstName, String lastName, String email) {
        User us = new User(firstName, lastName, email);
        us.setId(id);
        us = userRepo.save(us);
        if (us != null)
            throw new ServiceException("User already exists!\n");
        return us;
    }

    /**
     * uppdates a user
     *
     * @param -         id of the user
     * @param firstName - first nema of the user
     * @param lastName  - last name of the user
     * @param email     - email of the user
     * @return - the user if it was successfully updates
     * @throws ServiceException if the user does not exist
     */
    public User updateUser(Long id, String firstName, String lastName, String email) {
        User us = new User(firstName, lastName, email);
        us.setId(id);
        us = userRepo.update(us);
        if (us != null)
            throw new ServiceException("User does not exist!\n");
        return us;
    }

    /**
     * deletes the user with the id
     *
     * @param id - id of the user
     * @return - a user if the user is fond
     * @throws ServiceException if the user is not fond
     */
    public User deleteUser(Long id) {
        User us = userRepo.findOne(id);
        if (us == null)
            throw new ServiceException("User does not exist!\n");
        else {
            if (us.getFriends() != null) {
                for (Long i : us.getFriends()) {
                    friendshipsRepo.delete(new Pair(id, i));
                    User friend = userRepo.findOne(i);
                    friend.removeFriend(id);
                    userRepo.update(friend);
                }
            }
            us = userRepo.delete(id);
        }
        return us;
    }

    public Iterable<User> findAllUsers() {
        return userRepo.findAll();
    }

    public Friendship findOneFriendship(Long id1, Long id2) {
        Friendship friendship = friendshipsRepo.findOne(new Pair(id1, id2));
        if (friendship == null)
            throw new ServiceException("Friendship does not exist!\n");
        return friendship;
    }

    public void addFriendship(Long id1, Long id2) {
        Friendship friendship = new Friendship(id1, id2);
        friendship.setId(new Pair(id1, id2));
        friendship = friendshipsRepo.save(friendship);
        if (friendship != null)
            throw new ServiceException("Friendship already exists!\n");
        User u1 = userRepo.findOne(id1);
        User u2 = userRepo.findOne(id2);
        u1.addFriend(u2.getId());
        u2.addFriend(u1.getId());
        userRepo.update(u1);
        userRepo.update(u2);
    }

    public void deleteFriendship(Long id1, Long id2) {
        Friendship friendship = friendshipsRepo.delete(new Pair(id1, id2));
        if (friendship == null)
            throw new ServiceException("Friendship does not exist!\n");
        User u1 = userRepo.findOne(id1);
        User u2 = userRepo.findOne(id2);
        u1.removeFriend(u2.getId());
        u2.removeFriend(u1.getId());
        userRepo.update(u1);
        userRepo.update(u2);
    }


    public Iterable<Friendship> findAllFriendships() {
        return friendshipsRepo.findAll();
    }
}
