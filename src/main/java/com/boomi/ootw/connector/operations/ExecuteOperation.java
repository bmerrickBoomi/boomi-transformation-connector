// Copyright (c) 2020 Boomi, Inc.
package com.boomi.ootw.connector.operations;

import com.boomi.connector.api.ObjectData;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.api.UpdateRequest;
import com.boomi.connector.util.BaseUpdateOperation;
import com.boomi.ootw.connector.OOTWConnection;
import com.boomi.ootw.connector.client.RESTClient;
import com.boomi.ootw.connector.client.RepeatableInputStreamEntity;
import com.boomi.util.IOUtil;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import java.io.InputStream;

public class ExecuteOperation extends BaseUpdateOperation {

    public ExecuteOperation(OOTWConnection connection) {
        super(connection);
    }

    @Override
    public OOTWConnection getConnection() {
        return (OOTWConnection) super.getConnection();
    }

    protected void executeUpdate(UpdateRequest updateRequest, OperationResponse operationResponse) {
        // For all of the JSON documents, go ahead and send it over 
        RESTClient client = null;
        for (ObjectData data : updateRequest) {
            CloseableHttpResponse response = null;
            InputStream dataStream = null;
            try {
                // fetch the document data as a stream
                dataStream = data.getData();

                String uri = getConnection().getBaseURL() + "/" + getContext().getObjectTypeId();
                HttpUriRequest request = RequestBuilder.create("POST").setUri(uri).setEntity(
                        new RepeatableInputStreamEntity(dataStream, data.getDataSize()) {
                        }).build();
                client = getConnection().getRESTClient();
                response = client.executeRequest(request);                
                int statusCode = response.getStatusLine().getStatusCode();
                //Successful call, but no data is returned from this service.
                if (statusCode >= 200 && statusCode < 300) {
                    ResponseUtil.addEmptySuccess(operationResponse, data, String.valueOf(statusCode));
                } else {
                    // Unsuccessful call, return status code of why
                    ResponseUtil.addEmptyFailure(operationResponse, data, String.valueOf(statusCode));
                }
            } catch (Exception e) {
                //Exception occurred, add failure.
                ResponseUtil.addExceptionFailure(operationResponse, data, e);
            } finally {
                IOUtil.closeQuietly(response, dataStream);
            }
        }
        IOUtil.closeQuietly(client);
    }
}
