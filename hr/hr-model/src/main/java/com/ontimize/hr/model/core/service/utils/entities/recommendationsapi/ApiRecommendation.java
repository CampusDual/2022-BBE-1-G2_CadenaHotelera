package com.ontimize.hr.model.core.service.utils.entities.recommendationsapi;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Configuration
public class ApiRecommendation {

	public String getTokenAccess(String urlAuth, String clientId, String clientSecret,
			CloseableHttpClient client) throws ClientProtocolException, IOException {

		HttpPost httppost = new HttpPost("https://test.api.amadeus.com/v1/security/oauth2/token");

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("grant_type", "client_credentials"));
		params.add(new BasicNameValuePair("client_id", "O4ZCSzgcEBAyPrAdigjgRVMRmB4Abcwp"));
		params.add(new BasicNameValuePair("client_secret", "RhOesY44h3LvgiWY"));

		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		CloseableHttpResponse response = client.execute(httppost);

		JsonObject serviceObject = Json.createReader(response.getEntity().getContent()).readObject();

		String token = serviceObject.getString("access_token");

		response.close();

		return token;
	}

	public List<Recommendation> getList(String token, String latitude, String longitude, String radius,
			CloseableHttpClient client) throws URISyntaxException, ClientProtocolException, IOException {

		HttpGet httpGet = new HttpGet("https://test.api.amadeus.com/v1/shopping/activities");
		httpGet.addHeader("Authorization", "Bearer " + token);
		URI uri = new URIBuilder(httpGet.getURI()).addParameter("latitude", latitude)
				.addParameter("longitude", longitude).addParameter("sort", "distance").addParameter("radius", radius)
				.build();

		((HttpRequestBase) httpGet).setURI(uri);
		CloseableHttpResponse responseAir = client.execute(httpGet);

		JsonObject airObject = Json.createReader(responseAir.getEntity().getContent()).readObject();

		responseAir.close();

		String recomendationsJson = (airObject.get("data").toString());

		Gson gson = new Gson();
		final Type tipoListaRecomendations = new TypeToken<List<Recommendation>>() {
		}.getType();
		
		client.close();
		return gson.fromJson(recomendationsJson, tipoListaRecomendations);
	}

	public CloseableHttpClient getClient()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		return HttpClients.custom()
				.setSSLSocketFactory(new SSLConnectionSocketFactory(
						SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
						NoopHostnameVerifier.INSTANCE))
				.build();

	}
}
