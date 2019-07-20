package com.b4pay.order.utils;

import org.apache.commons.ssl.SSLClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpClientUtil {

    /*private PoolingHttpClientConnectionManager cm;

    public HttpClientUtil() {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(10);
    }


    public String doPost(String url, String params, String charset) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        HttpPost httpPost = null;
        String result = null;


            httpPost = new HttpPost(url);
            StringEntity s = new StringEntity(params, charset);
            s.setContentEncoding(charset);
            s.setContentType("application/json");// 发送json格式的数据需要设置contentType
            httpPost.setEntity(s);

            CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            //解析响应，返回结果
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;
        }
        return result;
    }*/

    public String doPost(String url,String params,String charset){
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        CloseableHttpResponse response = null;
        try{
            httpClient = HttpClientBuilder.create().build();
            httpPost = new HttpPost(url);
            StringEntity s = new StringEntity(params, charset);
            s.setContentEncoding(charset);
            s.setContentType("application/json");// 发送json格式的数据需要设置contentType
            httpPost.setEntity(s);
            response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

} 
