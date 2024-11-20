package com.aplicacionweb.restaurante.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configura Spring para servir las imágenes desde el directorio especificado
        registry.addResourceHandler("/imagenes/**")
        .addResourceLocations("classpath:/static/images/");  // Accede a la carpeta "images" dentro de /static/
 // Ruta a las imágenes en tu sistema
    }
}

