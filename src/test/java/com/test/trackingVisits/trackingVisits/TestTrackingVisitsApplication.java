package com.test.trackingVisits.trackingVisits;

import org.springframework.boot.SpringApplication;

public class TestTrackingVisitsApplication {

	public static void main(String[] args) {
		SpringApplication.from(TrackingVisitsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
