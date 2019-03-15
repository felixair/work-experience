package com.felix.proxy.cglib;

public class TestCglib {

    public static void main(String[] args) {
        BookFacadeCglib cglib = new BookFacadeCglib();
        BookFacade bookFacade = (BookFacade)cglib.getInstance(new BookFacade());
        bookFacade.addBook();
        bookFacade.deleteBook();
    }

}
