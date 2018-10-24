package eca.data.migration.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.inject.Inject;
import java.time.LocalDateTime;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * Configuration for swagger.
 *
 * @author Roman Batygin
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    private static final String CONTROLLER_PACKAGE = "eca.data.migration.controller";

    /**
     * Returns swagger configuration bean.
     *
     * @return {@link Docket} bean
     */
    @Bean
    @Inject
    public Docket docket(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDateTime.class, String.class)
                .alternateTypeRules(
                        newRule(typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                                typeResolver.resolve(WildcardType.class))
                );
    }
}
