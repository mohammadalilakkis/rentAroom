package com.rentaroom.service;

import com.rentaroom.model.HostProfile;
import com.rentaroom.model.User;
import com.rentaroom.repository.HostProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class HostProfileService {
    
    @Autowired
    private HostProfileRepository hostProfileRepository;
    
    public HostProfile save(HostProfile hostProfile) {
        return hostProfileRepository.save(hostProfile);
    }
    
    public Optional<HostProfile> findByUser(User user) {
        return hostProfileRepository.findByUser(user);
    }
    
    public boolean existsByUser(User user) {
        return hostProfileRepository.existsByUser(user);
    }
    
    public HostProfile createOrUpdate(HostProfile hostProfile) {
        return hostProfileRepository.save(hostProfile);
    }
}
