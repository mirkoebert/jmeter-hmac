package com.ebertp.jmeter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.util.XMLUtils;
import org.apache.wss4j.dom.message.WSSecHeader;
import org.apache.wss4j.dom.message.WSSecSignature;
import org.w3c.dom.Document;

public final class WssecApiSec {


	public String getSignedWsMessageFromWsMessage(String soapMsg, String username, String password){
		String output = "";
		try {
			Document doc = toSOAPPart(soapMsg);

			WSSecSignature builder = new WSSecSignature();
			builder.setUserInfo(username, password);
			builder.setIncludeSignatureToken(true);

			WSSecHeader secHeader = new WSSecHeader(doc);
			secHeader.insertSecurityHeader();

			Crypto crypto = CryptoFactory.getInstance();
			Document signedDoc = builder.build(doc, crypto, secHeader);
			output = XMLUtils.PrettyDocumentToString(signedDoc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Convert an SOAP Envelope from a String to a org.w3c.dom.Document.
	 */
	private org.w3c.dom.Document toSOAPPart(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		try (InputStream in = new ByteArrayInputStream(xml.getBytes())) {
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(in);
		}
	}	
	
	public static void main(String[] args) {
		final String SAMPLE_SOAP_MSG = 
		        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
		        + "<SOAP-ENV:Envelope "
		        +   "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "
		        +   "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
		        +   "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" 
		        +   "<SOAP-ENV:Body>" 
		        +       "<add xmlns=\"http://ws.apache.org/counter/counter_port_type\">" 
		        +           "<value xmlns=\"\">15</value>" 
		        +       "</add>" 
		        +   "</SOAP-ENV:Body>" 
		        + "</SOAP-ENV:Envelope>";
		WssecApiSec t = new WssecApiSec();
		String signedDoc = t.getSignedWsMessageFromWsMessage(SAMPLE_SOAP_MSG, "b2cclient","changeit");
		System.out.println(signedDoc);

	}

}
