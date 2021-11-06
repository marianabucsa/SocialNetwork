package ro.ubbcluj.map.utils;

import java.util.Objects;

public class Pair {
    private Long id1;
    private Long id2;

    public Pair(Long id1,Long id2) {
        this.id1=id1;
        this.id2=id2;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return (this.id1.equals(pair.id1) && this.id2.equals(pair.id2)) || (this.id1.equals(pair.id2) && this.id2.equals(pair.id1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }
}
