package com.mastercard.send;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class SendReferenceApplicationIT {

	@Test
	void contextLoads() {
		try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {

			mocked.when(() -> {
				SpringApplication.run(SendReferenceApplication.class,
						new String[] { "foo", "bar" });
			})
					.thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

			SendReferenceApplication.main(new String[] { "foo", "bar" });

			mocked.verify(() -> {
				SpringApplication.run(SendReferenceApplication.class,
						new String[] { "foo", "bar" });
			});

		}
	}

}
