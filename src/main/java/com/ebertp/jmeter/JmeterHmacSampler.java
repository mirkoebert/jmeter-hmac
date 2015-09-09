package com.ebertp.jmeter;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.log.Logger;

public class JmeterHmacSampler  extends AbstractJavaSamplerClient implements Serializable {
	private static final String SECRET = "SECRET";
	private static final long serialVersionUID = 1L;
	private static final String BODY = "BODY";


	// set up default arguments for the JMeter GUI
	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("URL", "http://www.ebertp.com/");
		defaultParameters.addArgument("HTTP Method", "GET");
		defaultParameters.addArgument(SECRET, "password");
		defaultParameters.addArgument(BODY, "");
		return defaultParameters;
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();
		result.sampleStart(); // start stopwatch

		// pull parameters
		String urlString = context.getParameter( "URL" );
		String searchFor = context.getParameter( "HTTP Method" );
		String httpBody = context.getParameter(BODY);
		String secret = context.getParameter(SECRET);
		try {
			ApiSec apisec = new ApiSec(secret);
			String hmac = apisec.getHmacFromMessage(httpBody, urlString, searchFor);
			System.out.println(hmac);
			Logger l = getLogger();
			l.info(hmac);
			JMeterContext jmctx = JMeterContextService.getContext();
			JMeterVariables hmacVar = new JMeterVariables();
			hmacVar.put("hmac", hmac);
			jmctx.setVariables(hmacVar);
			result.sampleEnd(); 
			result.setSuccessful( true );
			result.setResponseMessage( "Successfully performed action" );
			result.setResponseCodeOK(); 

		} catch (InvalidKeyException | NoSuchAlgorithmException e1) {
			result.sampleEnd();
			result.setSuccessful( false );

			e1.printStackTrace();
			java.io.StringWriter stringWriter = new java.io.StringWriter();
			e1.printStackTrace( new java.io.PrintWriter( stringWriter ) );
			result.setResponseData( stringWriter.toString() );
			result.setDataType( org.apache.jmeter.samplers.SampleResult.TEXT );
			result.setResponseCode( "500" );				
		}
		return result;
	}
}