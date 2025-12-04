/*
package com.example.rest;

import com.example.models.Book;
import com.example.service.BookService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    private BookService bookService;

    @GET
    public Response getAllBooks(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("20") int size,
            @QueryParam("sortBy") @DefaultValue("title") String sortBy,
            @QueryParam("asc") @DefaultValue("true") boolean ascending) {

        List<Book> books = bookService.findPaginated(page, size, sortBy, ascending);
        return Response.ok(books).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") Long id) {
        return bookService.findById(id)
                .map(book -> Response.ok(book).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createBook(Book book) {
        Book created = bookService.createBook(book);
        return Response
                .created(URI.create("/books/" + created.getId()))
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book bookDetails) {
        try {
            Book updated = bookService.updateBook(id, bookDetails);
            return Response.ok(updated).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        bookService.deleteBook(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    public Response searchBooks(
            @QueryParam("author") String author,
            @QueryParam("title") String titleKeyword) {

        List<Book> books;
        if (titleKeyword != null) {
            books = bookService.searchByTitle(titleKeyword);
        } else if (author != null) {
            books = bookService.findByAuthor(author);
        } else {
            books = bookService.findAll();
        }

        return Response.ok(books).build();
    }

    @GET
    @Path("/stats/count")
    public Response countBooks() {
        Long count = bookService.countAllBooks();
        return Response.ok(Map.of("count", count)).build();
    }

}*/
