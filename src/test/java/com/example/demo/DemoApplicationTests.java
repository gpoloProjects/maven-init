package com.example.demo;

import com.example.demo.repositories.DogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class DemoApplicationTests {

	@MockitoBean
	private DogRepository dogRepository;

	@Test
	void contextLoads() {
	}

}
