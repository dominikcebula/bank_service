package com.dominikcebula.bank.service.rest.processors;

public interface RequestProcessor<T> {
    void process(T requestObject) throws Exception;
}
