// Copyright (c) 2020 Boomi, Inc.
package com.boomi.transform.connector;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Operation;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.util.BaseConnector;
import com.boomi.transform.connector.operations.ExecuteOperation;

public class TransformConnector extends BaseConnector {

    public Browser createBrowser(BrowseContext context) {
        return new TransformBrowser(context);
    }

    @Override
    protected Operation createExecuteOperation(OperationContext context) {
        return new ExecuteOperation(new TransformConnection(context));
    }
}

