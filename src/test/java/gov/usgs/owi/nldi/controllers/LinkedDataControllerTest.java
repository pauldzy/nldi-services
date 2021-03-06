package gov.usgs.owi.nldi.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import gov.usgs.owi.nldi.dao.LookupDao;
import gov.usgs.owi.nldi.dao.StreamingDao;
import gov.usgs.owi.nldi.services.LogService;
import gov.usgs.owi.nldi.services.Navigation;
import gov.usgs.owi.nldi.services.Parameters;
import gov.usgs.owi.nldi.springinit.TestSpringConfig;

public class LinkedDataControllerTest {

	private StreamingDao streamingDao;
	@Mock
	private LookupDao lookupDao;
	@Mock
	private Navigation navigation;
	@Mock
	private Parameters parameters;
	@Mock
	private LogService logService;
	private LinkedDataController controller;
	private MockHttpServletResponse response;
	private MockHttpServletRequest request;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		controller = new LinkedDataController(lookupDao, streamingDao, navigation, parameters, TestSpringConfig.TEST_ROOT_URL, logService);
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();

		when(logService.logRequest(any(HttpServletRequest.class))).thenReturn(BigInteger.ONE);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void getComidTest() {
		when(lookupDao.getComid(anyString(), anyMap())).thenReturn(goodFeature(), null, missingFeature());

		assertEquals("12345", controller.getComid("abc", "def"));

		assertNull(controller.getComid("abc", "def"));

		assertNull(controller.getComid("abc", "def"));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void getFlowlinesTest() throws IOException {
		when(lookupDao.getComid(anyString(), anyMap())).thenReturn(null, goodFeature());
		controller.getFlowlines(request, response, null, null, null, null, null, null);
		verify(logService).logRequest(any(HttpServletRequest.class));
		verify(logService).logRequestComplete(any(BigInteger.class), any(int.class));
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

		controller.getFlowlines(request, response, null, null, null, null, null, null);
		verify(logService, times(2)).logRequest(any(HttpServletRequest.class));
		verify(logService, times(2)).logRequestComplete(any(BigInteger.class), any(int.class));
		//this is a BAD_REQUEST because the BaseController.streamFeatures dependencies are not all mocked
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void getFeaturesTest() throws IOException {
		when(lookupDao.getComid(anyString(), anyMap())).thenReturn(null, goodFeature());
		controller.getFeatures(request, response, null, null, null, null, null, null, null);
		verify(logService).logRequest(any(HttpServletRequest.class));
		verify(logService).logRequestComplete(any(BigInteger.class), any(int.class));
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

		controller.getFeatures(request, response, null, null, null, null, null, null, null);
		verify(logService, times(2)).logRequest(any(HttpServletRequest.class));
		verify(logService, times(2)).logRequestComplete(any(BigInteger.class), any(int.class));
		//this is a BAD_REQUEST because the BaseController.streamFeatures dependencies are not all mocked
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
	}

	public static Map<String, Object> goodFeature() {
		Map<String, Object> rtn = new LinkedHashMap<>();
		rtn.put(Parameters.COMID, "12345");
		return rtn;
	}

	public static Map<String, Object> missingFeature() {
		Map<String, Object> rtn = new LinkedHashMap<>();
		return rtn;
	}

}
