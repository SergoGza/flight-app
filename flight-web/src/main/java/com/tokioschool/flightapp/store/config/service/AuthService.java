package com.tokioschool.flightapp.store.config.service;

import jakarta.annotation.Nullable;

public interface AuthService {

    @Nullable
    String getAccesToken();

}
