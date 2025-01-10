package com.scv.contact_vault.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    @SuppressWarnings("null")
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");

        var oauth2AuthenicationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = oauth2AuthenicationToken.getAuthorizedClientRegistrationId();
        logger.info(authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        // logger.info(oauthUser.getName());
        // oauthUser.getAttributes().forEach((key, value) -> {
        // logger.info("{} => {}", key, value);
        // });
        // logger.info(oauthUser.getAuthorities().toString());

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("password");

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created using google.");

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            user.setEmail(oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
                    : oauthUser.getAttribute("login").toString() + "@gmail.com");
            user.setProfilePic(oauthUser.getAttribute("avatar_url").toString());
            user.setName(oauthUser.getAttribute("login").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GITHUB);
            user.setAbout("This account is created using github");

        } else {
            logger.info("OAuthAuthenicationSuccessHandler: Unknown provider");
        }

        User userExist = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (userExist == null) {
            userRepo.save(user);
            System.out.println("User saved: " + user.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
