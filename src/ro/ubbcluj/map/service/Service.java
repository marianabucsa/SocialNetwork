package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.ReplyMessage;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.repository.DB.FriendshipsDBRepository;
import ro.ubbcluj.map.repository.DB.MessagesDBRepository;
import ro.ubbcluj.map.repository.DB.UserDBRepository;
import ro.ubbcluj.map.repository.RepositoryException;
import ro.ubbcluj.map.utils.NetworkGraph;
import ro.ubbcluj.map.utils.Pair;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class Service {
    private final FriendshipsDBRepository friendshipsRepo;
    private final UserDBRepository userRepo;
    private final MessagesDBRepository messagesRepo;

    /**
     * service constructor
     *
     * @param friendshipsRepo - repository for friendships
     * @param userRepo        - repository for users
     * @param messagesRepo    - repository for messages
     */
    public Service(FriendshipsDBRepository friendshipsRepo, UserDBRepository userRepo, MessagesDBRepository messagesRepo) {
        this.friendshipsRepo = friendshipsRepo;
        this.userRepo = userRepo;
        this.messagesRepo = messagesRepo;
        //connectUsersFriendships();
    }

    /**
     * methode to connect the friendships to the users used for in memory and file repository
     */
    /*
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
    */

    /**
     * Find all friends for an user
     *
     * @param id user id
     * @return last name, first name of user's friends
     */
    public Stream<String> findFriendsForUser(Long id) {
        User us = findOneUser(id);
        List<Long> friends = friendshipsRepo.getFriendsUser(id);
        return friends.stream()
                .map(this::findOneUser)
                .filter(x -> FriendshipDate(x.getId(), id) != null)
                .map(x -> x.getFirstName() + "|" + x.getLastName() + "|"
                        + FriendshipDate(x.getId(), id).toString());


    }

    /**
     * Find a friendship date
     *
     * @param u1 first user id
     * @param u2 second user id
     * @return Friendship date between the two users
     */
    public Timestamp FriendshipDate(Long u1, Long u2) {
        Timestamp timestamp = null;
        try {
            timestamp = friendshipsRepo.getFriendshipDate(new Pair(u1, u2));
        } catch (RepositoryException re) {
            try {
                timestamp = friendshipsRepo.getFriendshipDate(new Pair(u2, u1));
            } catch (RepositoryException re2) {
                throw re2;
            }
        }
        return timestamp;
    }

    public Stream<String> findFriendsByMonth(Long id, Long month) {
        User us = findOneUser(id);
        List<Long> friends = friendshipsRepo.getFriendsUser(id);
        return friends.stream()
                .map(x -> findOneUser(x))
                .filter(x -> FriendshipDate(x.getId(), id).getMonth() == month - 1)
                .map(x -> x.getFirstName() + " " + x.getLastName() + " "
                        + FriendshipDate(x.getId(), id).toString());
    }

    public void sendMessage(String from, List<String> to, String message) {
        List<Long> toList = new ArrayList<>();
        for (String email : to) {
            toList.add(getIdFromEmail(email));
        }
        ReplyMessage rm = new ReplyMessage(getIdFromEmail(from), toList, message, null);
       ReplyMessage r= messagesRepo.save(rm);
        if (r != null)
            throw new ServiceException("Message already exists!\n");
    }

    public void replyMessage(String from, Long idMsg, String message) {
        ReplyMessage m = messagesRepo.findOne(idMsg);
        List<Long> to=new ArrayList<>();
        to.add(m.getFrom());
        ReplyMessage replyMessage= new ReplyMessage(getIdFromEmail(from),to,message,idMsg);
        messagesRepo.save(replyMessage);
    }

    public void replyAllMessage(String from, Long idMsg, String message) {
        ReplyMessage m = messagesRepo.findOne(idMsg);
        List<Long> to=new ArrayList<>();
        to.add(m.getFrom());
        for(Long id : m.getTo())
            if(!id.equals(getIdFromEmail(from)))
                to.add(id);
        ReplyMessage replyMessage= new ReplyMessage(getIdFromEmail(from),to,message,idMsg);
        messagesRepo.save(replyMessage);
    }

    public ReplyMessage deleteMessage(Long idMsg) {
        ReplyMessage rm = messagesRepo.findOne(idMsg);
        if (rm == null)
            throw new ServiceException("Message does not exist!\n");
        messagesRepo.delete(idMsg);
        return rm;
    }

    public List<ReplyMessage> showConversation(String email1,String email2){
        return messagesRepo.showConversation(getIdFromEmail(email1),getIdFromEmail(email2));
    }

    /**
     * methode for the number of social communities
     *
     * @return - an int
     */
    public int communitiesNumber() {
        HashMap<Long, User> users = userRepo.getAllData();
        for (User us : users.values())
            us.setFriends(friendshipsRepo.getFriendsUser(us.getId()));
        NetworkGraph<Long, User> network = new NetworkGraph(users);
        return network.connectedComponents();
    }

    /**
     * methode for the largest social community
     *
     * @return - a list of users
     */
    public List<Long> largestCommunity() {
        HashMap<Long, User> users = userRepo.getAllData();
        for (User us : users.values())
            us.setFriends(friendshipsRepo.getFriendsUser(us.getId()));
        NetworkGraph<Long, User> network = new NetworkGraph(users);
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
     * @param firstName - first name of the user
     * @param lastName  - last name of the user
     * @param email     - email of the user
     * @return - the user if it was successfully added
     * @throws ServiceException if the user already exists
     */
    public User addUser(String firstName, String lastName, String email) {
        User us = new User(firstName, lastName, email);
        us = userRepo.save(us);
        if (us != null)
            throw new ServiceException("User already exists!\n");
        return us;
    }

    /**
     * uppdates a user
     *
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
            if (friendshipsRepo.getFriendsUser(id).size() != 0) {
                for (Long i : friendshipsRepo.getFriendsUser(id)) {
                    friendshipsRepo.delete(new Pair(id, i));
                }
            }
            if(messagesRepo.findMessagesUser(id).size()!=0){
                for (ReplyMessage rm : messagesRepo.findMessagesUser(id)) {
                    messagesRepo.delete(rm.getId());
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
    }

    public void deleteFriendship(Long id1, Long id2) {
        Friendship friendship = friendshipsRepo.delete(new Pair(id1, id2));
        if (friendship == null)
            throw new ServiceException("Friendship does not exist!\n");
    }


    public Iterable<Friendship> findAllFriendships() {
        return friendshipsRepo.findAll();
    }

    public Long getIdFromEmail(String email) {
        return userRepo.getIdFromEmail(email);
    }

    public String getEmailFromId(Long id) {
        return userRepo.getEmailFromId(id);
    }
}