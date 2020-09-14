package com.vega.springit.config_Auditing;

import com.vega.springit.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        if(SecurityContextHolder.getContext().getAuthentication() == null ||
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser") ) {
            return Optional.of("master@gmail.com");  /*The problem here is that we are trying to add some links in our DatabaseLoader CommandLineRunner.
At this point in time there is no logged in user. To make this work for our development environment we need to check to see
if there is no authenticated user and if there isn’t hard code an admin user.
            */
        } else {
            return Optional.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        }
    }
}
