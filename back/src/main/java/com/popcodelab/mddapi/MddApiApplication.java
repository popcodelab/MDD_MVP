package com.popcodelab.mddapi;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
class MddApiApplication {

    public static void main(final String[] args) {
        log.debug("Starting MDD - Minimum Viable Product version");
        SpringApplication.run(MddApiApplication.class, args);
    }

}
