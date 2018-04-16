package it.zeroics.strgshell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.content.fs.config.EnableFilesystemStores;

@SpringBootApplication
//@EnableFilesystemStores(basePackageClasses=it.zeroics.strg.api.MediumContentStore.class)
public class StrgShellApplication {

	public static void main(String[] args) {
		SpringApplication.run(StrgShellApplication.class, args);
	}
}
