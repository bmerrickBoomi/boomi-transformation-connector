// Copyright (c) 2020 Boomi, Inc.
package com.boomi.ootw.connector;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Operation;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.util.BaseConnector;
import com.boomi.ootw.connector.operations.ExecuteOperation;
import com.boomi.ootw.connector.operations.GetOperation;

public class OOTWConnector extends BaseConnector {

    public Browser createBrowser(BrowseContext context) {
        return new OOTWBrowser(context);
    }


    @Override
    public Operation createGetOperation(OperationContext context) {
        return new GetOperation(new OOTWConnection(context));
    }

    @Override
    protected Operation createExecuteOperation(OperationContext context) {
        return new ExecuteOperation(new OOTWConnection(context));
    }
}

