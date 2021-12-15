// Copyright (c) 2020 Boomi, Inc.
package com.boomi.ootw.connector;

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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

public class OOTWBrowser extends BaseBrowser {

    private static final String CONTACT_SCHEMA = "/contact-schema.json";
    private static final String UTF8 = "UTF-8";
    private static final String CONTACT = "Contact";

    protected OOTWBrowser(BrowseContext context) {
        super(context);
    }

    /**
     * Returns the objects available from the service
     * @return objectTypes
     */
    public ObjectTypes getObjectTypes() {
        ObjectTypes types = new ObjectTypes();
        ObjectType type = new ObjectType();
        type.setId(CONTACT);
        type.setLabel(CONTACT);
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

            case GET:
                //Output has incoming data, no outgoing data
                definitions.getDefinitions().add(
                        new ObjectDefinition()
                                .withInputType(ContentType.NONE)
                                .withOutputType(ContentType.JSON)
                                .withJsonSchema(getJsonSchema())
                                .withElementName(""));
                    
                break;
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
        InputStream is = ClassUtil.getResourceAsStream(CONTACT_SCHEMA);
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
