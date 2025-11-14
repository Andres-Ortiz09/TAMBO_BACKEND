package com.proy.utp.backend_tambo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    private static final String[] PUBLIC_PATHS = {
            "/users/public",
            "/api/productos"
    };

    public JwtAuthFilter(JwtTokenProvider tokenProvider, UserDetailsService uds) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = uds;
    }

    private boolean isPublicPath(String path) {
        for (String p : PUBLIC_PATHS) {
            if (path.startsWith(p)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("🔍 URL solicitada: " + path);

        String bearer = request.getHeader("Authorization");
        System.out.println("🔍 Authorization raw: [" + bearer + "]");

        String token = getTokenFromRequest(request);
        System.out.println("🔍 Token recibido: " + (token != null ? "Sí" : "No"));

        if (StringUtils.hasText(token)) {
            boolean isValid = tokenProvider.validateToken(token);
            System.out.println("✅ Token válido: " + isValid);

            if (isValid) {
                String username = tokenProvider.getUsername(token);
                System.out.println("✅ Username extraído del token: " + username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("✅ Autoridades del usuario: " + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("✅ Autenticación registrada en el contexto");
            } else {
                System.out.println("❌ Token inválido");
            }
        } else {
            System.out.println("⚠️ No se recibió token en el header Authorization");
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
