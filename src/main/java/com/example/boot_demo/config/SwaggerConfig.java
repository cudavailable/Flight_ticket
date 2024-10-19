//package com.example.boot_demo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//    /*
//    *  配置Swagger2相关的bean
//    * */
//    @Bean
//    public Docket createRestApi(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com"))
//                .paths(PathSelectors.any()).build();
//    }
//
//    /*
//     *  此处是API文档页面的显示信息
//     * */
//    private ApiInfo apiInfo(){
//        return new ApiInfoBuilder()
//                .title("演示项目API")
//                .description("演示项目")
//                .version("1.0")
//                .build();
//    }
//}
