package pre_project24.SpringSecurity.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        System.out.println("User roles: " + roles);
        if (roles.contains("ROLE_USER")) {
            System.out.println("Redirecting to /user");
            httpServletResponse.sendRedirect("/user");
        } else {
            System.out.println("Redirecting to /");
            httpServletResponse.sendRedirect("/");
        }
    }
}
