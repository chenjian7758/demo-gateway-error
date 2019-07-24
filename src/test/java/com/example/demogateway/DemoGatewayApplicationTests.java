package com.example.demogateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.ProxyProvider;
import reactor.netty.tcp.TcpClient;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
public class DemoGatewayApplicationTests {

    //@LocalServerPort
    protected int port = 8080;

    @Test
    public void testGW() {

        /*
        TcpClient tcpClient = TcpClient.create().proxy(options -> options.type(ProxyProvider.Proxy.HTTP).host("localhost").port(8888));
        HttpClient client = HttpClient.from(tcpClient);
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(client);
        */

        String baseUri = "http://localhost:8080";

        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl(baseUri)
                .build();

        testClient.post()
                .uri("/gw/test/post")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .syncBody(generateBody())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .consumeWith(result -> {
                    assertTrue(result.getResponseBody().contains("data"));
                    assertTrue(result.getResponseBody().contains("base64"));
                });

    }

    @Test
    public void testMicro() {

        /*
        TcpClient tcpClient = TcpClient.create().proxy(options -> options.type(ProxyProvider.Proxy.HTTP).host("localhost").port(8888));
        HttpClient client = HttpClient.from(tcpClient);
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(client);
         */

        String baseUri = "http://localhost:8082";

        WebTestClient testClient = WebTestClient.bindToServer()
                .baseUrl(baseUri)
                .build();

        testClient.post()
                .uri("/test/post")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .syncBody(generateBody())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .consumeWith(result -> {
                    assertTrue(result.getResponseBody().contains("data"));
                    assertTrue(result.getResponseBody().contains("base64"));
                });

    }


    private MultiValueMap<String, HttpEntity<?>> generateBody() {
        ClassPathResource file = new ClassPathResource("/test.txt", MultipartHttpMessageReader.class);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file);
        return builder.build();
    }


}
