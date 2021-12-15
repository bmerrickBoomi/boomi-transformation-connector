// Copyright (c) 2020 Boomi, Inc.
package com.boomi.transform.connector;

import com.boomi.connector.api.ConnectorContext;
import com.boomi.connector.util.BaseConnection;

public class TransformConnection<C extends ConnectorContext> extends BaseConnection<C> {

    public TransformConnection(C context) {
        super(context);
    }
}
