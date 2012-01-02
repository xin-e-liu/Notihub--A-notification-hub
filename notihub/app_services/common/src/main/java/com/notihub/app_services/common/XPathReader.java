/*
Copyright 2011 codeoedoc

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.notihub.app_services.common;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XPathReader {

    private InputStream is;
    private Document xmlDocument;
    private XPath xPath;

    public XPathReader(InputStream is) {
        this.is = is;
        initObjects();
    }

    private void initObjects() {
        try {
            xmlDocument = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(is);
            xPath = XPathFactory.newInstance().newXPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
    }

    public Object read(String expression, QName returnType) {
        try {
            XPathExpression xPathExpression = xPath.compile(expression);
            return xPathExpression.evaluate(xmlDocument, returnType);
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}