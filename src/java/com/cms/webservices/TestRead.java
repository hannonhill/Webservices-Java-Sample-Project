/*
 * Created on Apr 12, 2007 by Zach Bailey
 * 
 * Please feel free to distribute this code in any way, with or without this notice.
 */
package com.cms.webservices;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hannonhill.www.ws.ns.AssetOperationService.AssetOperationHandler;
import com.hannonhill.www.ws.ns.AssetOperationService.AssetOperationHandlerServiceLocator;
import com.hannonhill.www.ws.ns.AssetOperationService.Authentication;
import com.hannonhill.www.ws.ns.AssetOperationService.EntityTypeString;
import com.hannonhill.www.ws.ns.AssetOperationService.Identifier;
import com.hannonhill.www.ws.ns.AssetOperationService.Read;
import com.hannonhill.www.ws.ns.AssetOperationService.ReadResult;

/**
 * Tests the webservices framework by doing a read of the ROOT folder.
 * 
 * @author Zach Bailey
 * @since 0.1
 */
public class TestRead
{
    /**
     * From inside Eclipse, run this test using:
     * 
     * - alt+shift+x, t (Windows)
     * - option+command+x, t (Mac)
     * 
     * This should output something similar to:
     * 
     * "Got read result: com.hannonhill.www.ws.ns.AssetOperationService.ReadResult@145e16fe"
     * 
     * @throws Exception
     */
    @Test
    public void testFolderRead() throws Exception
    {
        Identifier toRead = new Identifier();
        toRead.setId("ROOT");
        toRead.setType(EntityTypeString.folder);

        Read read = new Read();
        read.setIdentifier(toRead);

        Authentication authentication = new Authentication();
        authentication.setUsername("<REDACTED>");
        authentication.setPassword("<REDACTED>");

        AssetOperationHandlerServiceLocator serviceLocator = new AssetOperationHandlerServiceLocator();
        AssetOperationHandler handler = serviceLocator.getAssetOperationService();
        ReadResult result = handler.read(authentication, toRead);
        System.out.println("Got read result: " + result);
        System.out.println("Read was " + (result.getSuccess().equals("true") ? "successful." : "unsuccessful. Error is: " + result.getMessage()));

        assertTrue(result.getSuccess().equals("true") ? true : false);
    }
}
