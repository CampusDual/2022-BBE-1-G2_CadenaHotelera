package com.ontimize.hr.model.core.service.utils.entitys.airportapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApiAirport {
	public static String getTokenAccess(String urlAuth, String clientId, String clientSecret,
			CloseableHttpClient client) {

		HttpPost httppost = new HttpPost("https://test.api.amadeus.com/v1/security/oauth2/token");

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("grant_type", "client_credentials"));
		params.add(new BasicNameValuePair("client_id", "O4ZCSzgcEBAyPrAdigjgRVMRmB4Abcwp"));
		params.add(new BasicNameValuePair("client_secret", "RhOesY44h3LvgiWY"));

		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		CloseableHttpResponse response = null;
		try {
			response = client.execute(httppost);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JsonObject serviceObject = null;
		try {
			serviceObject = Json.createReader(response.getEntity().getContent()).readObject();
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}

		String token = serviceObject.getString("access_token");
		try {
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return token;
	}

	public static List<Airport> getList(String token, String latitude, String longitude, String radius,
			CloseableHttpClient client) {

		HttpGet httpGet = new HttpGet("https://test.api.amadeus.com/v1/reference-data/locations/airports");
		httpGet.addHeader("Authorization", "Bearer " + token);
		URI uri = null;
		try {
			uri = new URIBuilder(httpGet.getURI()).addParameter("latitude", latitude)
					.addParameter("longitude", longitude).addParameter("sort", "distance")
					.addParameter("radius", radius).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		((HttpRequestBase) httpGet).setURI(uri);
		CloseableHttpResponse responseAir = null;
		try {
			responseAir = client.execute(httpGet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JsonObject airObject = null;
		try {
			airObject = Json.createReader(responseAir.getEntity().getContent()).readObject();
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}
		try {
			responseAir.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String aeropuertosJson = (airObject.get("data").toString());

		Gson gson = new Gson();
		final Type tipoListaAeropuertos = new TypeToken<List<Airport>>() {
		}.getType();

		return gson.fromJson(aeropuertosJson, tipoListaAeropuertos);
	}

	public static CloseableHttpClient getClient() {
		CloseableHttpClient client = null;
		try {
			client = HttpClients.custom()
					.setSSLSocketFactory(new SSLConnectionSocketFactory(
							SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
							NoopHostnameVerifier.INSTANCE))
					.build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}

		return client;

	}
}
