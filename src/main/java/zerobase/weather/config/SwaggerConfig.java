package zerobase.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // any()가 아닌 basePackage()를 지정할경우 error 에 대해서는 나오지 않음
                .apis(RequestHandlerSelectors.basePackage("zerobase.weather"))
                .paths(PathSelectors.any()) // none, ant(pattern)
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        String description = "날씨 일기를 CRUD 할 수 있는 백엔드 API 입니다.";
        return new ApiInfoBuilder()
                .title("날씨 일기 프로젝트 :)")
                .description(description)
                .version("2.0")
                .build();
    }
}
