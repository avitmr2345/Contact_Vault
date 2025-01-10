package com.scv.contact_vault.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.scv.contact_vault.entity.Providers;
import com.scv.contact_vault.entity.User;
import com.scv.contact_vault.helpers.AppConstants;
import com.scv.contact_vault.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private UserRepo userRepo;

    public OAuthAuthenticationSuccessHandler(UserRepo theUserRepo) {
        userRepo = theUserRepo;
    }

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenicationSuccessHandler");

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        // logger.info(user.getName());
        // user.getAttributes().forEach((key, value) -> {
        // logger.info("{} => {}", key, value);
        // });
        // logger.info(user.getAuthorities().toString());

        String email = user.getAttribute("email").toString();
        String name = user.getAttribute("name").toString();
        String picture = user.getAttribute("picture").toString();

        if (email != null && name != null && picture != null) {
            User user1 = new User();
            user1.setEmail(email);
            user1.setName(name);
            user1.setProfilePic(picture);
            user1.setPassword("password");
            user1.setUserId(UUID.randomUUID().toString());
            user1.setProvider(Providers.GOOGLE);
            user1.setEnabled(true);
            user1.setEmailVerified(true);
            user1.setProviderUserId(user.getName());
            user1.setRoleList(List.of(AppConstants.ROLE_USER));
            user1.setAbout("This account is created using google.");

            User existingUser = userRepo.findByEmail(email).orElse(null);
            if (existingUser == null) {
                userRepo.save(user1);
                logger.info("User saved:" + email);
            }
        } else {
            logger.error("OAuth attributes missing. User creation aborted.");
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
