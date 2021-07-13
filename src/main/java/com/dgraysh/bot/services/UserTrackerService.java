package com.dgraysh.bot.services;

import com.dgraysh.bot.repositories.UserTrackerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserTrackerService {
    private final UserTrackerRepository userTrackerRepository;

    public Optional<Long> find(String state) {
        return userTrackerRepository.find(state);
    }
}
