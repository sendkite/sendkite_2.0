package com.sy.board.exception;

/**
 * status => 400
 * */
public class PostNotFound extends SendkiteException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
