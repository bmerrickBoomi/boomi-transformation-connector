// Copyright (c) 2020 Boomi, Inc.
package com.boomi.transform.connector.operations;

import java.io.InputStream;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.boomi.connector.api.ObjectData;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.api.UpdateRequest;
import com.boomi.connector.util.BaseUpdateOperation;
import com.boomi.transform.connector.TransformConnection;
import com.boomi.util.IOUtil;

public class ExecuteOperation extends BaseUpdateOperation {

    public ExecuteOperation(TransformConnection connection) {
        super(connection);
    }

    @Override
    public TransformConnection getConnection() {
        return (TransformConnection) super.getConnection();
    }

    protected void executeUpdate(UpdateRequest updateRequest, OperationResponse operationResponse) {
        // For all of the JSON documents, go ahead and send it over 
        for (ObjectData data : updateRequest) {
            CloseableHttpResponse response = null;
            InputStream dataStream = null;
            try {
                // fetch the document data as a stream
                dataStream = data.getData();
            } catch (Exception e) {
                //Exception occurred, add failure.
                ResponseUtil.addExceptionFailure(operationResponse, data, e);
            } finally {
                IOUtil.closeQuietly(response, dataStream);
            }
        }
    }
}
