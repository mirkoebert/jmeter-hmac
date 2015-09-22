package com.ebertp.jmeter;

import java.io.Serializable;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.log.Logger;

public class JmeterWssecSampler  extends AbstractJavaSamplerClient implements Serializable {
	
	private static final String SECRET = "PASSWORD";
	private static final String USER = "USER";
	private static final long serialVersionUID = 1L;
	private static final String BODY = "BODY";


	// set up default arguments for the JMeter GUI
	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument(USER, "b2cclient");
		defaultParameters.addArgument(SECRET, "changeit");
		defaultParameters.addArgument(BODY, "");
		return defaultParameters;
	}

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult result = new SampleResult();
		result.sampleStart(); // start stopwatch

		// pull parameters
		String username = context.getParameter( USER );
		String soapMsg = context.getParameter(BODY);
		String password = context.getParameter(SECRET);
		try {
			WssecApiSec apisec = new WssecApiSec();
			String signSoapMeg = apisec.getSignedWsMessageFromWsMessage(soapMsg, username, password);
			System.out.println(signSoapMeg);
			Logger l = getLogger();
			l.info(signSoapMeg);
			JMeterContext jmctx = JMeterContextService.getContext();
			JMeterVariables hmacVar = new JMeterVariables();
			hmacVar.put("hmac", signSoapMeg);
			jmctx.setVariables(hmacVar);
			result.sampleEnd(); 
			result.setSuccessful( true );
			result.setResponseMessage( "Successfully performed action" );
			result.setResponseCodeOK(); 

		} catch (Exception e1) {
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