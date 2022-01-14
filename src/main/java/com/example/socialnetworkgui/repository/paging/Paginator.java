package com.example.socialnetworkgui.repository.paging;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Paginator <E>{
    private PageableInterface pageableInterface;
    private Iterable<E> elements;

    public Paginator(PageableInterface pageable, Iterable<E> elements) {
        this.pageableInterface = pageable;
        this.elements = elements;
    }

    public Page<E> paginate() {
        Stream<E> result = StreamSupport.stream(elements.spliterator(), false)
                .skip(pageableInterface.getPageNumber()  * pageableInterface.getPageSize())
                .limit(pageableInterface.getPageSize());
        return new Page<>(pageableInterface, result);
    }
}
