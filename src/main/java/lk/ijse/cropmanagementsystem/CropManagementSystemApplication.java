package lk.ijse.cropmanagementsystem;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication /* @EnableAutoConfiguration, @Component, @Configuration */
public class CropManagementSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(CropManagementSystemApplication.class, args);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
