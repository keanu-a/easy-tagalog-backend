package org.alouastudios.easytagalogbackend.service;

import lombok.RequiredArgsConstructor;
import org.alouastudios.easytagalogbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
}
