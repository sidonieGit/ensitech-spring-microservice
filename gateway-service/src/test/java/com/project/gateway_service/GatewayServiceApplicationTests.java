package com.project.gateway_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// On utilise les propriétés de l'annotation pour configurer l'environnement de test
@SpringBootTest(properties = {
		"spring.cloud.config.enabled=false",        // Désactive le client Config
		"spring.cloud.discovery.enabled=false",      // Désactive le client Eureka
		"eureka.client.enabled=false"                // Désactivation supplémentaire pour être sûr
})
class GatewayServiceApplicationTests {

	@Test
	void contextLoads() {
		// Ce test vérifie simplement que l'application peut démarrer son contexte
		// avec les configurations ci-dessus.
	}

}
