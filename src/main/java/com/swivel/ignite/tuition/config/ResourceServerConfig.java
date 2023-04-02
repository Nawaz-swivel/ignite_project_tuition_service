package com.swivel.ignite.tuition.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Resource Server Configuration
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String TUITION_ENDPOINT = "/api/v1/tuition/**";
    private final String resourceId;

    @Autowired
    public ResourceServerConfig(@Value("${oauth.resource-id}") String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Configure a resource id for resource server APIs
     *
     * @param resources resource
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceId);
        resources.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        resources.accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    /**
     * This method configure the web security of the resource server
     *
     * @param http http
     * @throws Exception exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, TUITION_ENDPOINT).access("hasAuthority('ADMIN')")
                .antMatchers(HttpMethod.GET, TUITION_ENDPOINT).access("hasAnyAuthority('ADMIN', 'STUDENT')")
                .antMatchers(HttpMethod.DELETE, TUITION_ENDPOINT).access("hasAuthority('ADMIN')")
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                        "/configuration/**", "/swagger-ui.html", "/webjars/**").permitAll()
                .anyRequest().authenticated().and().cors();
    }
}
