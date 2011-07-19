/*
 * Created on Jul 19, 2011 by Bradley Wagner
 * 
 * Copyright(c) 2000-2010 Hannon Hill Corporation. All rights reserved.
 */
package com.cms.webservices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hannonhill.www.ws.ns.AssetOperationService.Asset;
import com.hannonhill.www.ws.ns.AssetOperationService.AssetOperationHandler;
import com.hannonhill.www.ws.ns.AssetOperationService.AssetOperationHandlerServiceLocator;
import com.hannonhill.www.ws.ns.AssetOperationService.Authentication;
import com.hannonhill.www.ws.ns.AssetOperationService.EntityTypeString;
import com.hannonhill.www.ws.ns.AssetOperationService.Identifier;
import com.hannonhill.www.ws.ns.AssetOperationService.OperationResult;
import com.hannonhill.www.ws.ns.AssetOperationService.Page;
import com.hannonhill.www.ws.ns.AssetOperationService.Path;
import com.hannonhill.www.ws.ns.AssetOperationService.Read;
import com.hannonhill.www.ws.ns.AssetOperationService.ReadResult;

/**
 * Example of an Edit using the Cascade SOAP Web Services API
 * 
 * @author Bradley Wagner
 */
public class TestEdit
{

    @Test
    public void testPageEdit() throws Exception
    {
        Identifier toRead = new Identifier();
        Path path = new Path();

        // set your page path and site name
        path.setPath("/about");
        path.setSiteName("example.com");
        toRead.setPath(path);
        toRead.setType(EntityTypeString.page);

        Read read = new Read();
        read.setIdentifier(toRead);

        Authentication authentication = new Authentication();
        authentication.setUsername("admin");
        authentication.setPassword("admin");

        // read the asset
        AssetOperationHandlerServiceLocator serviceLocator = new AssetOperationHandlerServiceLocator();
        AssetOperationHandler handler = serviceLocator.getAssetOperationService();
        ReadResult result = handler.read(authentication, toRead);
        assertTrue("Read was not successful", result.getSuccess().equals("true") ? true : false);

        // edit the title
        Asset readAsset = result.getAsset();
        Page page = readAsset.getPage();
        page.getMetadata().setTitle("new title");

        // null out unnecessary values before submitting edit
        WebservicesUtils.nullPageValues(page);

        OperationResult editResult = handler.edit(authentication, readAsset);
        assertTrue("Edit was not successful", editResult.getSuccess().equals("true") ? true : false);

        // re-read to verify title change
        result = handler.read(authentication, toRead);
        assertTrue("Read was not successful", result.getSuccess().equals("true") ? true : false);
        assertEquals("Title was not changed", "new title", result.getAsset().getPage().getMetadata().getTitle());
    }

}
