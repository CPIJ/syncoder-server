package web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WebSocketConfigTest {

    private WebSocketConfig config;

    @Before
    public void before() {
        config = new WebSocketConfig();
    }

    @Test
    public void configureMessageBroker_isCalled_wasConfiguredCorrectly() {
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);

        config.configureMessageBroker(registry);

        verify(registry).enableSimpleBroker(anyString());
        verify(registry).setApplicationDestinationPrefixes(anyString());
    }

    @Test
    public void registerStompEndPoints_isCalled_wasConfiguredCorrectly() {
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        StompWebSocketEndpointRegistration registration = mock(StompWebSocketEndpointRegistration.class);
        when(registry.addEndpoint(anyString())).thenReturn(registration);
        when(registration.setAllowedOrigins(anyString())).thenReturn(registration);

        config.registerStompEndpoints(registry);

        verify(registry).addEndpoint(anyString());
        verify(registration).withSockJS();
    }
}