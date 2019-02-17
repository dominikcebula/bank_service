package com.dominikcebula.bank.service.rest.processors;

public interface ResponseProcessor<T> {
    void process(T requestObject) throws Exception;
}
