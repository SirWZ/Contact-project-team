package com.ws.kafka.common;


public interface IProcessorHandler {

    void process(Object record);
}

