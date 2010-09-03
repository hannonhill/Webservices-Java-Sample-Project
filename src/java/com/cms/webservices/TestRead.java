/*
 * Created on Apr 12, 2007 by Zach Bailey
 *
 * THE PROGRAM IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL, BUT WITHOUT ANY WARRANTY. IT IS PROVIDED "AS IS" 
 * WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE 
 * PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY SERVICING, REPAIR OR 
 * CORRECTION.
 * 
 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW THE AUTHOR WILL BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL, 
 * INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS 
 * OF DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE 
 * WITH ANY OTHER PROGRAMS), EVEN IF THE AUTHOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * Please feel free to distribute this code in any way, with or without this notice.
 */
package com.cms.webservices;

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
     * Main method for running as java console program. From inside
     * Eclipse, run this file using alt+shift+x, j
     * 
     * This should output something similar to: 
     * 
     * "Got read result: com.hannonhill.www.ws.ns.AssetOperationService.ReadResult@145e16fe"
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        Identifier toRead = new Identifier();
        toRead.setId("ROOT");
        toRead.setType(EntityTypeString.folder);

        Read read = new Read();
        read.setIdentifier(toRead);

        Authentication authentication = new Authentication();
        authentication.setPassword("admin");
        authentication.setUsername("admin");

        AssetOperationHandlerServiceLocator serviceLocator = new AssetOperationHandlerServiceLocator();
        AssetOperationHandler handler = serviceLocator.getAssetOperationService();
        ReadResult result = handler.read(authentication, toRead);
        System.out.println("Got read result: " + result);
    }
}
