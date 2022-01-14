package com.example.socialnetworkgui.repository.paging;

import java.util.stream.Stream;

public class Page <E> implements PageInterface<E>{
    private PageableInterface pageable;
    private Stream<E> content;

    public Page(PageableInterface pageable, Stream<E> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public PageableInterface getPageable() {
        return this.pageable;
    }

    @Override
    public PageableInterface nextPageable() {
        return new Pageable(this.pageable.getPageNumber()+1,this.pageable.getPageSize());
    }

    @Override
    public Stream<E> getContent() {
        return this.content;
    }
}
