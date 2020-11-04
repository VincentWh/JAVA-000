package sky.week03.outbound.httpclient4;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sky.week03.filter.MyHttpRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class MyHttpOutboundHandler {
    private String backendUrl;
    private CloseableHttpClient httpClient;

    public MyHttpOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl;
        this.httpClient = HttpClients.createDefault();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {

        HttpGet httpGet = new HttpGet(backendUrl);
        for (Map.Entry<String, String> entry : fullRequest.headers().entries()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }
        System.out.println("copied gateway header to backend request:" + Arrays.toString(httpGet.getAllHeaders()));

        FullHttpResponse response = null;

        try (CloseableHttpResponse backendResponse = httpClient.execute(httpGet)) {
            byte[] body = EntityUtils.toByteArray(backendResponse.getEntity());
            String content = new String(body, StandardCharsets.UTF_8);
            System.out.println("Gateway received content from backend:" + content);

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", Integer.parseInt(backendResponse.getFirstHeader("Content-Length").getValue()));
        } catch (IOException e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(response);
                }
            }
        }
    }
}
