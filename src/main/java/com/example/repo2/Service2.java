package com.example.repo2;

import com.example.repo1.Utility;

public class Service2 {
    public String getServiceMessage() {
        return Utility.getMessage() + " Called by Repo2.";
    }
}

