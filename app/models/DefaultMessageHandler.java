package models;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultMessageHandler implements SOAPHandler<SOAPMessageContext> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultMessageHandler.class);
	@Override
	public boolean handleMessage(SOAPMessageContext context) {

		SOAPMessage message = context.getMessage();
		boolean isOutboundMessage = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		try {
			if (isOutboundMessage) {

				SOAPEnvelope envelope;
				envelope = message.getSOAPPart().getEnvelope();
				if (envelope != null) {
					SOAPBody body = envelope.getBody();
					if (body != null) {
						Node node = body.getFirstChild();
						if (null != node && "createPolicy".equalsIgnoreCase(node.getLocalName())) {
							node = node.getFirstChild();
							if (null != node && "intIO".equalsIgnoreCase(node.getLocalName())) {
								NodeList nodeList = node.getChildNodes();
								for (int i = nodeList.getLength() - 1; i >= 0; i--) {
									node = nodeList.item(i);
									if ("errorLists".equalsIgnoreCase(node.getLocalName())
											|| "listErrorListList".equalsIgnoreCase(node.getLocalName())) {
										node.getParentNode().removeChild(node);
										//System.out.println("Removing:::::::::::::: " + node.getLocalName());
									}
								}
							}
						}
					}
				}
			}
			message.saveChanges();
			System.out.println("------------------------------------------------------------------------------------");
			message.writeTo(System.out); // Line 3
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			message.writeTo(baos);  
			logger.debug("------------------------------------------------------------------------------------" +baos.toString());  
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}
	

	public static void printMessage(BindingProvider bindingProvider) {
		Binding binding = bindingProvider.getBinding();
		List<Handler> handlerChain = binding.getHandlerChain();
		handlerChain.add(new DefaultMessageHandler());
		binding.setHandlerChain(handlerChain);
	}
	
//	public static void printCDataMessage(BindingProvider bindingProvider) {
//	    Binding binding = bindingProvider.getBinding();
//	    List<Handler> handlerChain = binding.getHandlerChain();
//	    handlerChain.add(new CDataHandler());
//	    handlerChain.add(new DefaultMessageHandler());
//	    binding.setHandlerChain(handlerChain);
//	}
}
