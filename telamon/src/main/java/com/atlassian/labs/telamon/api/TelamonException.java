package com.atlassian.labs.telamon.api;

public class TelamonException extends RuntimeException
{
    public TelamonException()
    {
    }

    public TelamonException(String s)
    {
        super(s);
    }

    public TelamonException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public TelamonException(Throwable throwable)
    {
        super(throwable);
    }
}
