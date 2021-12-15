// Copyright (c) 2020 Boomi, Inc.
package com.boomi.ootw.connector;

import com.boomi.connector.api.ConnectorContext;
import com.boomi.connector.util.BaseConnection;
import com.boomi.ootw.connector.client.RESTClient;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

public class OOTWConnection<C extends ConnectorContext> extends BaseConnection<C> {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String BASE_URL_FIELD = "url";

    public OOTWConnection(C context) {
        super(context);
    }

    /**
     * Fetch the base url field, this is defined in the descriptor. the id should always match to fetch the correct field.
     * @return the baseURL defined by the user
     */
    public String getBaseURL() {
        return getContext().getConnectionProperties().getProperty(BASE_URL_FIELD);
    }

    /**
     * transport library, this can be anything you want it to be.
     * @return the restClient
     */
    public RESTClient getRESTClient() {
        return new RESTClient(HttpClientBuilder.create().build(), getHttpContext());
    }

    private HttpClientContext getHttpContext() {
        HttpClientContext httpContext = HttpClientContext.create();
        httpContext.setCredentialsProvider(getCredentialsProvider());
        return httpContext;
    }

    private CredentialsProvider getCredentialsProvider() {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(
                        getContext().getConnectionProperties().getProperty(USERNAME_FIELD),
                        getContext().getConnectionProperties().getProperty(PASSWORD_FIELD)));
        return credsProvider;
    }
}
