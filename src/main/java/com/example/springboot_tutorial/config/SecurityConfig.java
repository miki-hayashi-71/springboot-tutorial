package com.example.springboot_tutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration  // @Beanとセットで用いる
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // URLごとのアクセス制御ルール
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/swagger-ui/**","/v3/api-docs/**","/h2-console/**") .permitAll() // 開発ツールは認証の対象外
                        .anyRequest() .authenticated()  // 上記以外は認証が必要
                )
                // 上記で許可した開発ツールついてはCSRFチェックを行わない
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/swagger-ui/**","/v3/api-docs/**","/h2-console/**")
                )
                // デフォルトではH2コンソールが表示されないため設定を追加
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                // HTTPBasic認証を有効にする
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    // パスワード暗号化方式を定義
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // テスト用ユーザーの定義
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
