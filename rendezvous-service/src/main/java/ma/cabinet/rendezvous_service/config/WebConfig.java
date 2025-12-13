package ma.cabinet.rendezvous_service.config;

import ma.cabinet.rendezvous_service.security.RoleAuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RoleAuthorizationInterceptor roleAuthorizationInterceptor;

    public WebConfig(RoleAuthorizationInterceptor roleAuthorizationInterceptor) {
        this.roleAuthorizationInterceptor = roleAuthorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("🔧 Enregistrement de l'interceptor RoleAuthorizationInterceptor");

        registry.addInterceptor(roleAuthorizationInterceptor)
                .addPathPatterns("/api/rendezvous/**") // Tous les endpoints /api/rendezvous/*
                .excludePathPatterns("/api/rendezvous/public/**"); // Exclure les endpoints publics (si nécessaire)

        System.out.println("✅ Interceptor enregistré avec succès");
    }
}