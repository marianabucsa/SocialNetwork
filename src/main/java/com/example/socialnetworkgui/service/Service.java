package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.ReplyMessage;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.UserDto;
import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.domain.validator.EmailValidator;
import com.example.socialnetworkgui.repository.DB.EventsDBRepository;
import com.example.socialnetworkgui.repository.DB.FriendshipsDBRepository;
import com.example.socialnetworkgui.repository.DB.MessagesDBRepository;
import com.example.socialnetworkgui.repository.DB.UserDBRepository;
import com.example.socialnetworkgui.repository.RepositoryException;
import com.example.socialnetworkgui.repository.paging.Page;
import com.example.socialnetworkgui.repository.paging.Pageable;
import com.example.socialnetworkgui.repository.paging.PageableInterface;
import com.example.socialnetworkgui.repository.paging.Paginator;
import com.example.socialnetworkgui.utils.NetworkGraph;
import com.example.socialnetworkgui.utils.Pair;
import com.example.socialnetworkgui.utils.event.ServiceEvent;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Service implements Observable<ServiceEvent> {
    private final FriendshipsDBRepository friendshipsRepo;
    private final UserDBRepository userRepo;
    private final MessagesDBRepository messagesRepo;
    private final EmailValidator emailValidator;
    public final EventsDBRepository eventsRepo;
    public List<Observer<ServiceEvent>> observers = new ArrayList<>();

    /**
     * service constructor
     *
     * @param friendshipsRepo - repository for friendships
     * @param userRepo        - repository for users
     * @param messagesRepo    - repository for messages
     * @param eventsRepo
     */
    public Service(FriendshipsDBRepository friendshipsRepo, UserDBRepository userRepo, MessagesDBRepository messagesRepo, EventsDBRepository eventsRepo, EmailValidator emailValidator) {
        this.friendshipsRepo = friendshipsRepo;
        this.userRepo = userRepo;
        this.messagesRepo = messagesRepo;
        this.emailValidator = emailValidator;
        //connectUsersFriendships();
        this.eventsRepo = eventsRepo;
    }


    /**
     * adds an event
     *
     * @param name
     * @param startDate
     * @param endDate
     * @param description
     * @param location
     * @param organizer
     * @param participants
     * @return
     */
    public Event addEvent(String name, LocalDateTime startDate, LocalDateTime endDate, String description, String location, Long organizer, List<Long> participants) {
        Event event = new Event(name, startDate, endDate, description, location, organizer, participants);
        event = eventsRepo.save(event);
        System.out.println(startDate + " " + endDate);
        if (event != null)
            throw new ServiceException("Event already exists!\n");
        return event;
    }

    /**
     * Find all friend requests for an user
     *
     * @param id user id
     * @return list of id's
     */
    public List<UserDto> findFriendRequests(Long id) {
        User us = userRepo.findOne(id);
        List<Long> friends = friendshipsRepo.getFriendRequests(id);
        return friends.stream()
                .map(this::findOneUser)
                .map(x -> new UserDto(x.getFirstName(), x.getLastName(),
                        x.getEmail())).collect(Collectors.toList());
    }

    /**
     * Find sent friend requests for an user
     *
     * @param id user id
     * @return list of id's
     */
    public List<UserDto> findSentFriendRequests(Long id) {
        User us = userRepo.findOne(id);
        List<Long> friends = friendshipsRepo.getSentFriendRequests(id);
        return friends.stream()
                .map(this::findOneUser)
                .map(x -> new UserDto(x.getFirstName(), x.getLastName(),
                        x.getEmail())).collect(Collectors.toList());
    }

    /**
     * Find sent friend requests for an user
     *
     * @param id user id
     * @return list of id's
     */
    public List<UserDto> findReceivedFriendRequests(Long id) {
        User us = userRepo.findOne(id);
        List<Long> friends = friendshipsRepo.getReceivedFriendRequests(id);
        return friends.stream()
                .map(this::findOneUser)
                .map(x -> new UserDto(x.getFirstName(), x.getLastName(),
                        x.getEmail())).collect(Collectors.toList());
    }

    /**
     * Find all friends for a user
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
     * Find all friends for a user
     *
     * @param id user id
     * @return last name, first name and email of user's friends
     */
    public List<UserDto> findFriends(Long id) {
        User us = userRepo.findOne(id);
        List<Long> friends = friendshipsRepo.getFriendsUser(id);
        return friends.stream()
                .map(this::findOneUser)
                .map(x -> new UserDto(x.getFirstName(), x.getLastName(),
                        x.getEmail())).collect(Collectors.toList());
    }

    public List<MessageDto> findMessages(Long id) {
        User us = userRepo.findOne(id);
        List<ReplyMessage> messages = messagesRepo.findMessagesUser(id);
        return messages.stream()
                .map(x -> new MessageDto(x.getFrom(), x.getTo(), x.getMessage(), x.getData()))
                .collect(Collectors.toList());
    }

    /**
     * Find the status of a friendship
     *
     * @param u1 first user id
     * @param u2 second user id
     * @return friendship status
     */
    public String FriendshipStatus(Long u1, Long u2) {
        String status = null;
        try {
            status = friendshipsRepo.getFriendshipStatus(new Pair(u1, u2));
        } catch (RepositoryException re) {
            try {
                status = friendshipsRepo.getFriendshipStatus(new Pair(u2, u1));
            } catch (RepositoryException re2) {

            }
        }
        return status;
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

    /**
     * gets friends that were made in a specified month for a user
     *
     * @param email - email of the user, long
     * @param month - month to search for, long
     * @return - a stream of users
     */
    public Stream<String> findFriendsByMonth(String email, Long month) {
        User us = findOneUser(getIdFromEmail(email));
        List<Long> friends = friendshipsRepo.getFriendsUser(getIdFromEmail(email));
        return friends.stream()
                .map(x -> findOneUser(x))
                .filter(x -> FriendshipDate(x.getId(), getIdFromEmail(email)).getMonth() == month - 1)
                .map(x -> x.getFirstName() + " " + x.getLastName() + " "
                        + FriendshipDate(x.getId(), getIdFromEmail(email)).toString());
    }

    /**
     * saves a message in repository
     *
     * @param from    - an email, string
     * @param to      - a list of emails, list of strings
     * @param message - a message, string
     */
    public void sendMessage(String from, List<String> to, String message) {
        List<Long> toList = new ArrayList<>();
        for (String email : to) {
            emailValidator.validate(email);
            toList.add(getIdFromEmail(email));
        }
        ReplyMessage rm = new ReplyMessage(getIdFromEmail(from), toList, message, null);
        ReplyMessage r = messagesRepo.save(rm);
        if (r != null)
            throw new ServiceException("Message already exists!\n");
    }

    /**
     * replies to a message, saves message in repository
     *
     * @param from    - a email, string
     * @param idMsg   - id of the message to reply, long
     * @param message - the reply message, string
     */
    public void replyMessage(String from, Long idMsg, String message) {
        ReplyMessage m = messagesRepo.findOne(idMsg);
        List<Long> to = new ArrayList<>();
        to.add(m.getFrom());
        ReplyMessage replyMessage = new ReplyMessage(getIdFromEmail(from), to, message, idMsg);
        messagesRepo.save(replyMessage);
    }

    /**
     * replies to a message to all users involved, saves message in repository
     *
     * @param from    - a email, string
     * @param idMsg   - id of the message to reply, long
     * @param message - the reply message, string
     */
    public void replyAllMessage(String from, Long idMsg, String message) {
        ReplyMessage m = messagesRepo.findOne(idMsg);
        List<Long> to = new ArrayList<>();
        to.add(m.getFrom());
        for (Long id : m.getTo())
            if (!id.equals(getIdFromEmail(from)))
                to.add(id);
        ReplyMessage replyMessage = new ReplyMessage(getIdFromEmail(from), to, message, idMsg);
        messagesRepo.save(replyMessage);
    }

    /**
     * deletes a message from repository
     *
     * @param idMsg - id of the message
     * @return - the message that was deleted
     */
    public ReplyMessage deleteMessage(Long idMsg) {
        ReplyMessage rm = messagesRepo.findOne(idMsg);
        if (rm == null)
            throw new ServiceException("Message does not exist!\n");
        messagesRepo.delete(idMsg);
        return rm;
    }

    /**
     * gets a conversation between 2 users
     *
     * @param email1 - an email, string
     * @param email2 - an email, string
     * @return - a list of reply messages
     */
    public List<ReplyMessage> showConversation(String email1, String email2) {
        return messagesRepo.showConversation(getIdFromEmail(email1), getIdFromEmail(email2));
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
    public User addUser(String firstName, String lastName, String email, String password) {
        User us = new User(firstName, lastName, email);
        us.setPassword(password);
        us = userRepo.save(us);
        if (us != null)
            throw new ServiceException("User already exists!\n");
        return us;
    }

    /**
     * updates a user
     *
     * @param firstName - first nema of the user
     * @param lastName  - last name of the user
     * @param email     - email of the user
     * @return - the user if it was successfully updates
     * @throws ServiceException if the user does not exist
     */
    public User updateUser(Long id, String firstName, String lastName, String email, String password) {
        User us = new User(firstName, lastName, email);
        us.setId(id);
        us.setPassword(password);
        us = userRepo.update(us);
        if (us != null)
            throw new ServiceException("User does not exist!\n");
        return us;
    }

    /**
     * update friendship in repository
     *
     * @param pair   - an id for friendship, pair
     * @param status - status of the friendship, string
     * @return - null if the friendship in updated
     */
    public Friendship updateFriendship(Pair pair, String status) {
        Friendship fr = new Friendship(pair.getId2(), pair.getId1());
        fr.setStatus(status);
        fr = friendshipsRepo.update(fr);
        if (fr != null)
            throw new ServiceException("Friendship not found!\n");
        return fr;
    }

    /**
     * deletes the user with the id
     *
     * @param email - email of the user
     * @return - a user if the user is fond
     * @throws ServiceException if the user is not fond
     */
    public User deleteUser(String email) {
        Long id = getIdFromEmail(email);
        User us = userRepo.findOne(id);
        if (us == null)
            throw new ServiceException("User does not exist!\n");
        else {
            if (friendshipsRepo.getAllData().size() != 0) {
                HashMap<Pair, Friendship> hashMap = friendshipsRepo.getAllData();
                for (Pair p : hashMap.keySet()) {
                    if (Objects.equals(p.getId1(), id) || Objects.equals(p.getId2(), id))
                        friendshipsRepo.delete(p);
                }

            }
            if (messagesRepo.findMessagesUser(id).size() != 0) {
                for (ReplyMessage rm : messagesRepo.findMessagesUser(id)) {
                    messagesRepo.delete(rm.getId());
                }
            }
            if (eventsRepo.getEventsUser(id).size() != 0) {
                for (Event event : eventsRepo.getEventsUser(id)) {
                    eventsRepo.delete(event.getId());
                }
            }
            us = userRepo.delete(id);
        }
        return us;
    }

    /**
     * gets all users from repository
     *
     * @return - an iterable of users
     */
    public Iterable<User> findAllUsers() {
        return userRepo.findAll();
    }

    /**
     * finds a friendship
     *
     * @param email1 - an email, string
     * @param email2 - an email, string
     * @return - a friendship
     */
    public Friendship findOneFriendship(String email1, String email2) {
        Friendship friendship = friendshipsRepo.findOne(new Pair(getIdFromEmail(email1), getIdFromEmail(email2)));
        if (friendship == null)
            throw new ServiceException("Friendship does not exist!\n");
        return friendship;
    }

    /**
     * saves friendship in repository
     *
     * @param email1 - an email, string
     * @param email2 - an email, string
     */
    public void addFriendship(String email1, String email2) {
        Friendship exists = null;
        try {
            exists = findOneFriendship(email1, email2);
        } catch (RepositoryException repositoryException) {

        }
        if (exists != null)
            throw new ServiceException("Friendship already exists!\n");
        try {
            exists = findOneFriendship(email2, email1);
        } catch (RepositoryException repositoryException) {

        }
        if (exists != null)
            throw new ServiceException("Friendship already exists!\n");
        Long id1 = getIdFromEmail(email1);
        Long id2 = getIdFromEmail(email2);
        Friendship friendship = new Friendship(id1, id2);
        friendship.setId(new Pair(id1, id2));
        friendship = friendshipsRepo.save(friendship);
        if (friendship != null)
            throw new ServiceException("Friendship already exists!\n");
    }

    /**
     * deletes friendship in repository
     *
     * @param email1 - an email, string
     * @param email2 - an email, string
     */
    public void deleteFriendship(String email1, String email2) {
        Long id1 = getIdFromEmail(email1);
        Long id2 = getIdFromEmail(email2);
        Friendship friendship = friendshipsRepo.delete(new Pair(id1, id2));
        if (friendship == null)
            throw new ServiceException("Friendship does not exist!\n");
    }


    /**
     * gets all friendship from repository
     *
     * @return - an iterable of friendships
     */
    public Iterable<Friendship> findAllFriendships() {
        return friendshipsRepo.findAll();
    }

    /**
     * gets messages to reply for user from repository
     *
     * @return - a list of reply messages
     */
    public List<ReplyMessage> getToReplyForUser(String email) {
        return messagesRepo.getToReplyForUser(getIdFromEmail(email));
    }

    /**
     * gets messages sent by user from repository
     *
     * @return - a list of reply messages
     */
    public List<ReplyMessage> getSentForUser(String email) {
        return messagesRepo.getSentForUser(getIdFromEmail(email));
    }

    /**
     * gets messages sent and received by user from repository
     *
     * @return - a list of reply messages
     */
    public List<ReplyMessage> getMessagesForUser(String email) {
        return messagesRepo.findMessagesUser(getIdFromEmail(email));
    }

    /**
     * gets id from a user email
     *
     * @param email - an email, string
     * @return - an id, long
     */
    public Long getIdFromEmail(String email) {
        emailValidator.validate(email);
        return userRepo.getIdFromEmail(email);
    }

    /**
     * gets email from a user id
     *
     * @param id - an id, long
     * @return - an email, string
     */
    public String getEmailFromId(Long id) {
        return userRepo.getEmailFromId(id);
    }

    @Override
    public void addObserver(Observer<ServiceEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<ServiceEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(ServiceEvent e) {
        observers.stream().forEach(x -> {
            try {
                x.update(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public List<Event> findUserEvents(Long id) {
        User user = userRepo.findOne(id);
        return eventsRepo.getEventsUser(id);
    }

    public Event updateEvent(Long id, String name, LocalDateTime startTime, LocalDateTime endTime, String description, String location, Long organizer, List<Long> participants) {
        Event event = new Event(name, startTime, endTime, description, location, organizer, participants);
        event.setId(id);
        event = eventsRepo.update(event);
        if (event != null)
            throw new ServiceException("Event does not exist!\n");
        return event;
    }

    public void deleteEvent(Event workingEvent) {
        Event event = eventsRepo.delete(workingEvent.getId());
        if (event == null)
            throw new ServiceException("Event does not exist!\n");
    }

    public Iterable<Event> findAllEvents() {
        return eventsRepo.findAll();
    }

    public void SubscribeEvent(Event event, Long user) {
        List<Long> participants = event.getParticipants();
        if (participants == null) {
            participants = new ArrayList<>();
            participants.add(user);
        } else {
            for (Long id : participants) {
                if (id == user)
                    throw new ServiceException("You are already subscribed!");
            }
            participants.add(user);
        }
        event.setParticipants(participants);
        eventsRepo.update(event);
    }

    public void UnsubscribeEvent(Event event, Long user) {
        List<Long> participants = event.getParticipants();
        if (participants == null) {
            throw new ServiceException("You were no subscribed to this event!");
        } else {
            List<Long> part = new ArrayList<>();
            for (Long id : participants) {
                if (!Objects.equals(id, user)) {
                    part.add(id);
                }

            }
            if (part.size() == participants.size()) {
                throw new ServiceException("You were not subscribed to this event!");
            } else {
                event.setParticipants(part);
                eventsRepo.update(event);
            }
        }
    }

    public List<Event> findSubscribedEventsUser(Long idFromEmail) {
        return eventsRepo.findSubscribedEventsUser(idFromEmail);
    }

    public List<Event> findFriendsEvents(Long user) {
        List<Long> friends=friendshipsRepo.getFriendsUser(user);
        List<Event> events=new ArrayList<>();
        for (Long id: friends){
            events.addAll(eventsRepo.getEventsUser(id));
        }
        return events;
    }

    public Set<Event> getAllEventsOnPage(int pageIndex,int size) {
        PageableInterface pageable = new Pageable(pageIndex, size);
        Page<Event> eventPage = eventsRepo.findAll(pageable);
        return eventPage.getContent().collect(Collectors.toSet());
    }

    public Set<Event> getSubscribedEventsUserOnPage(int pageIndex,int size,Long id) {
        PageableInterface pageable = new Pageable(pageIndex, size);
        Page<Event> eventPage = eventsRepo.findSubscribedEventsUser(pageable,id);
        return eventPage.getContent().collect(Collectors.toSet());
    }

    public Set<Event> getEventsUserOnPage(int pageIndex,int size,Long id) {
        PageableInterface pageable = new Pageable(pageIndex, size);
        Page<Event> eventPage = eventsRepo.getEventsUser(pageable,id);
        return eventPage.getContent().collect(Collectors.toSet());
    }

    public Set<Event> getFriendsEventsOnPage(int pageIndex,int size,Long id) {
        PageableInterface pageable = new Pageable(pageIndex, size);
        List<Long> friends=friendshipsRepo.getFriendsUser(id);
        List<Event> events=new ArrayList<>();
        for (Long id1: friends){
            events.addAll(eventsRepo.getEventsUser(id1));
        }
        Paginator<Event> paginator = new Paginator<Event>(pageable, events);
        Page<Event> eventPage = paginator.paginate();
        return eventPage.getContent().collect(Collectors.toSet());
    }
/*
    public Set<User> getNextUsers() {
        this.page++;
        return getUsersOnPage(this.page);
    }

    public Set<User> getUsersOnPage(int page) {
        this.page=page;
        PageableInterface pageable = new Pageable(page, this.size);
        Page<User> eventPage = userRepo.findAll(pageable);
        return eventPage.getContent().collect(Collectors.toSet());
    }

    public Set<Friendship> getNextFriendship() {
        this.page++;
        return getFriendshipsOnPage(this.page);
    }

    public Set<Friendship> getFriendshipsOnPage(int page) {
        this.page=page;
        PageableInterface pageable = new Pageable(page, this.size);
        Page<Friendship> eventPage = friendshipsRepo.findAll(pageable);
        return eventPage.getContent().collect(Collectors.toSet());
    }

    public Set<ReplyMessage> getNextMessages() {
        this.page++;
        return getMessagesOnPage(this.page);
    }

    public Set<ReplyMessage> getMessagesOnPage(int page) {
        this.page=page;
        PageableInterface pageable = new Pageable(page, this.size);
        Page<ReplyMessage> eventPage = messagesRepo.findAll(pageable);
        return eventPage.getContent().collect(Collectors.toSet());
    }
*/

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
    }*/
}