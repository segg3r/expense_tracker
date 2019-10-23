package com.segg3r.expensetracker;

import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;

public class MockitoTest {

	@BeforeMethod
	public void initMockito() {
		MockitoAnnotations.initMocks(this);
	}

}
