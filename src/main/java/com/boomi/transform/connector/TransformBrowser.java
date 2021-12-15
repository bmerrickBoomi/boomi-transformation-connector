// Copyright (c) 2020 Boomi, Inc.
package com.boomi.transform.connector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.ContentType;
import com.boomi.connector.api.ObjectDefinition;
import com.boomi.connector.api.ObjectDefinitionRole;
import com.boomi.connector.api.ObjectDefinitions;
import com.boomi.connector.api.ObjectType;
import com.boomi.connector.api.ObjectTypes;
import com.boomi.connector.util.BaseBrowser;
import com.boomi.util.ClassUtil;
import com.boomi.util.IOUtil;
import com.boomi.util.StreamUtil;

public class TransformBrowser extends BaseBrowser {

    private static final String TRANSFORM_SCHEMA = "/transform-schema.json";
    private static final String UTF8             = "UTF-8";
    private static final String TRANSFORM        = "Transform";

    protected TransformBrowser(BrowseContext context) {
        super(context);
    }

    /**
     * Returns the objects available from the service
     * @return objectTypes
     */
    public ObjectTypes getObjectTypes() {
        ObjectTypes types = new ObjectTypes();
        ObjectType type = new ObjectType();
        type.setId(TRANSFORM);
        type.setLabel(TRANSFORM);
        types.getTypes().add(type);
        return types;
    }

    /**
     * Returns the object definition for the selected object type. The roles is the roles available for this specific 
     * operation.
     * @param objectTypeId
     * @param roles
     * @return object definition
     */
    public ObjectDefinitions getObjectDefinitions(String objectTypeId, Collection<ObjectDefinitionRole> roles) {
        ObjectDefinitions definitions = new ObjectDefinitions();
        switch (getContext().getOperationType()) {

            // output and input
            case EXECUTE:
                
                ObjectDefinition inputDef = new ObjectDefinition()
                        .withInputType( ContentType.JSON )
                        .withOutputType( ContentType.NONE)
                        .withJsonSchema(getJsonSchema())
                        .withElementName("");
                definitions.getDefinitions().add(inputDef);

                definitions.getDefinitions().add(new ObjectDefinition()
                        .withInputType(ContentType.NONE)
                        .withOutputType(ContentType.NONE));
                break;
            default:
                throw new UnsupportedOperationException();
        }
        return definitions;
    }

    private static String getJsonSchema() {
        String schema;
        InputStream is = ClassUtil.getResourceAsStream(TRANSFORM_SCHEMA);
        try {
            schema = StreamUtil.toString(is, Charset.forName(UTF8));
        } catch (IOException ex) {
            throw new ConnectorException("Error reading schema", ex);
        } finally {
            IOUtil.closeQuietly(is);
        }
        return schema;
    }
}
