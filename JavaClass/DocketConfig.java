package pe.royalsystems.springerp.ERPSaludRest.config;


import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@ConfigurationProperties("swagger.info")
public class DocketConfig extends Docket{
    
	//@Autowired DocketConfigProperties docketConfigProperties;
		
	//@Value("${swagger.info.title}")
	private String title = "Spring Boot REST API XXX";

	//@Value("${swagger.info.description}")
	private String description = "Spring Boot REST API - Citas - HCE - Salud";
	
	
	
	/**
	 *  Constructor por defecto.... inicializara con la app
	 */
	public DocketConfig() {
		super(DocumentationType.SWAGGER_2);		
		this
	       .select()       	    	       
	       .apis(RequestHandlerSelectors.any())
	       //.apis(RequestHandlerSelectors.basePackage("pe.royalsystems.springerp.ERPSaludRest.controller"))
	       .paths(PathSelectors.regex("/api.*"))
	       .build()
	       .apiInfo(metaData());
	       
	}				

    private ApiInfo metaData() {    	    	
        ApiInfo apiInfo = new ApiInfo(
        		title,
        		description,
                "1.0",
                "Terminos del servicio",
                new Contact("Fábrica de Software - Royal Systems S.A.C.", 
                		"http://www.royalsystems.net/", 
                		"royalsys@royalsystems.net"),
               "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0");
        
//        apiInfo.getTitle();
//        apiInfo.getDescription();
//        apiInfo.getVersion();
//        apiInfo.getTermsOfServiceUrl();
//        apiInfo.getContact();
//        apiInfo.getLicense();
//        apiInfo.getLicenseUrl();                
        
        return apiInfo;
    }

/***MAVEM**

		<dependency>
		    <groupId>io.springfox</groupId>
		    <artifactId>springfox-swagger2</artifactId>
		    <version>2.6.1</version>
		</dependency>
        			
		<dependency>
		    <groupId>io.springfox</groupId>
		    <artifactId>springfox-swagger-ui</artifactId>
		    <version>2.6.1</version>
		</dependency>
*/
    
}
