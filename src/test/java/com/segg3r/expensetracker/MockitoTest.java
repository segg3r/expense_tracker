package com.segg3r.expensetracker;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;

public class MockitoTest {

	@BeforeClass
	public void initMockito() {
		MockitoAnnotations.initMocks(this);
	}

}
