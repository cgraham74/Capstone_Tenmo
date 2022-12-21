package com.techelevator.tenmo;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class TenmoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TenmoApplication.class, args);
    }

}
