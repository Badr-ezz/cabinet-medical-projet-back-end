package ma.cabinet.rendezvous_service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.cabinet.rendezvous_service.response.AuthResponse;
import ma.cabinet.rendezvous_service.service.RdvValidations;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
public class RoleAuthorizationInterceptor implements HandlerInterceptor {

    private final RdvValidations rdvValidations;

    // @Lazy résout la dépendance circulaire
    public RoleAuthorizationInterceptor(@Lazy RdvValidations rdvValidations) {
        this.rdvValidations = rdvValidations;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("🔍 Interceptor déclenché pour: " + request.getRequestURI());

        // Ignorer si ce n'est pas une méthode de contrôleur
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // Vérifier si la méthode a l'annotation @RequireRole
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);

        if (requireRole == null) {
            System.out.println("⚠️ Pas d'annotation @RequireRole, accès autorisé");
            return true;
        }

        System.out.println("🔐 Validation du rôle requise: " + String.join(", ", requireRole.value()));

        // Extraire et valider le token
        String authHeader = request.getHeader("Authorization");
        AuthResponse authResponse = rdvValidations.validateToken(authHeader);

        if (authResponse == null) {
            System.err.println("❌ Token absent ou invalide");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token absent ou invalide");
            return false;
        }

        if (authResponse.isTokenExpired()) {
            System.err.println("❌ Token expiré");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expiré");
            return false;
        }

        String userRole = authResponse.getUserRole();
        if (userRole == null) {
            System.err.println("❌ Impossible d'extraire le rôle");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Impossible d'extraire le rôle: " + authResponse.getError());
            return false;
        }

        System.out.println("👤 Rôle utilisateur: " + userRole);

        // Vérifier si le rôle est autorisé
        String[] allowedRoles = requireRole.value();
        boolean isAuthorized = false;

        for (String allowedRole : allowedRoles) {
            if (userRole.equalsIgnoreCase(allowedRole)) {
                isAuthorized = true;
                break;
            }
        }

        if (!isAuthorized) {
            System.err.println("❌ Rôle non autorisé: " + userRole);
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Rôle non autorisé: " + userRole + ". Rôles requis: " + String.join(", ", allowedRoles));
            return false;
        }

        System.out.println("✅ Accès autorisé pour le rôle: " + userRole);

        // Stocker le rôle dans les attributs de la requête
        request.setAttribute("userRole", userRole);
        request.setAttribute("authResponse", authResponse);

        return true;
    }

    /**
     * Annotation pour spécifier les rôles autorisés sur une méthode
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireRole {
        String[] value();
    }
}