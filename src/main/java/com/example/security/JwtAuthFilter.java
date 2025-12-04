/*
package com.example.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.security.Principal;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // Получение токена из заголовка
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Валидация токена (упрощённая версия)
            try {
                // Здесь должна быть реальная валидация JWT токена
                JsonWebToken jwt = validateToken(token);

                // Установка SecurityContext
                final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
                requestContext.setSecurityContext(new SecurityContext() {

                    @Override
                    public Principal getUserPrincipal() {
                        return () -> jwt.getName();
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return jwt.getGroups().contains(role);
                    }

                    @Override
                    public boolean isSecure() {
                        return currentSecurityContext.isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "Bearer";
                    }
                });

            } catch (Exception e) {
                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity("Invalid token")
                                .build()
                );
            }
        } else {
            // Защищённые эндпоинты требуют аутентификации
            if (isProtectedEndpoint(requestContext)) {
                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity("Authentication required")
                                .build()
                );
            }
        }
    }

    private boolean isProtectedEndpoint(ContainerRequestContext context) {
        // Определите, какие эндпоинты защищены
        String path = context.getUriInfo().getPath();
        return !path.startsWith("/auth/") && !path.equals("/users/setUser");
    }

    private JsonWebToken validateToken(String token) {
        // Реализуйте реальную валидацию JWT
        // Используйте библиотеку, например: smallrye-jwt
        throw new UnsupportedOperationException("Token validation not implemented");
    }
}
*/
