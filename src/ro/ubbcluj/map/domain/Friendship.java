package ro.ubbcluj.map.domain;

import ro.ubbcluj.map.utils.Pair;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Pair>{

    private Pair pair;
    private String status;

    /**
     * constructor for friendship
     * @param id1 - id for the first user
     * @param id2 - id of the second user
     */
    public Friendship(Long id1,Long id2) {

        pair = new Pair(id1,id2);
        status="pending";
    }

    /**
     * verifies if 2 friendships are equal
     * @param o - a friendship
     * @return - true if the friendships are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship)) return false;
        Friendship that = (Friendship) o;
        return (this.pair.getId1().equals(that.pair.getId1()) && this.pair.getId2().equals(that.pair.getId2())) || (this.pair.getId1().equals(that.pair.getId2()) && this.pair.getId2().equals(that.pair.getId1())) ;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status=status;
    }

    /**
     * getter methode for friendship
     * @return - a pair of id
     */
    public Pair getPair() {
        return pair;
    }

    /**
     * creates a string from the object's attributes
     * @return- a string
     */
    @Override
    public String toString() {
        return "Friendship{" +
                "ID1: " + pair.getId1() + '\'' +
                ", ID2: " + pair.getId2()+ '\''+
                '}';
    }
}