package com.segg3r.expensetracker;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;

public class MvcTestUtils {

	public static HttpServletResponse givenMockedResponse() {
		return mock(HttpServletResponse.class);
	}

}
