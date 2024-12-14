package com.example.repo2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Service2Test {
    @Test
    public void testGetServiceMessage() {
        Service2 service = new Service2();
        assertEquals("Hello from Repo1! Called by Repo2.", service.getServiceMessage());
    }
}
