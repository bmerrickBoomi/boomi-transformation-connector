// Copyright (c) 2020 Boomi, Inc.
package com.boomi.transform.connector.operations;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;

import com.boomi.connector.api.ObjectData;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.PayloadUtil;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.api.UpdateRequest;
import com.boomi.connector.util.BaseUpdateOperation;
import com.boomi.transform.connector.TransformConnection;
import com.boomi.util.IOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            	ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> json = objectMapper.readValue(data.getData(), HashMap.class);
                
                // Figure out the operation based on the op field
                String op = json.get("op");
                String resp;
                
                // Upper-case data
                if (op.equalsIgnoreCase("upper")) {
                    json.replace("data", json.get("data").toUpperCase());
                    resp = objectMapper.writeValueAsString(json);	
                }
                // Upper-camel case data
                else if (op.equalsIgnoreCase("upper-camel")) {
                	String str = json.get("data").toLowerCase();
                	str = str.substring(0, 1).toUpperCase() + str.substring(1);
                	json.replace("data", str);
                    resp = objectMapper.writeValueAsString(json);
                }
                else {
                	// No operation specified, do nothing
                	resp = objectMapper.writeValueAsString(json);
                }
                
                ResponseUtil.addResultWithHttpStatus(
                		operationResponse, 
                		data,
                        200, 
                        "Success",
                        PayloadUtil.toPayload(resp));
            } catch (Exception e) {
                //Exception occurred, add failure.
                ResponseUtil.addExceptionFailure(operationResponse, data, e);
            } finally {
                IOUtil.closeQuietly(response, dataStream);
            }
        }
    }
}
