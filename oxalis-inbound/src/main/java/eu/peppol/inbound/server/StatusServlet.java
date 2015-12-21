/*
 * Copyright (c) 2010 - 2015 Norwegian Agency for Pupblic Government and eGovernment (Difi)
 *
 * This file is part of Oxalis.
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission
 * - subsequent versions of the EUPL (the "Licence"); You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl5
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the Licence
 *  is distributed on an "AS IS" basis,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the Licence for the specific language governing permissions and limitations under the Licence.
 *
 */

package eu.peppol.inbound.server;

import com.google.inject.Inject;
import eu.peppol.security.KeystoreManager;
import eu.peppol.util.GlobalConfiguration;
import eu.peppol.util.OxalisVersion;
import eu.peppol.util.PropertyDef;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * Servlet returning diagnostic information to ease operation, support and debugging.
 * Since this servlet is public accessible, it should NOT contain any sensitive
 * information about it's runtime environment.
 * @author ebe
 * @author thore
 */
public class StatusServlet extends HttpServlet {

    @Inject
    GlobalConfiguration globalConfiguration;

    @Inject
    KeystoreManager keystoreManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        X509Certificate ourCertificate = keystoreManager.getOurCertificate();

        resp.setContentType("text/plain");

        PrintWriter writer = resp.getWriter();
        writer.println("version.oxalis: " + OxalisVersion.getVersion());
        writer.println("version.java: " + System.getProperty("java.version"));
        writer.println(PropertyDef.OPERATION_MODE.getPropertyName() + ": " + globalConfiguration.getModeOfOperation());
        writer.println(PropertyDef.SML_HOSTNAME.getPropertyName() + ": " + globalConfiguration.getSmlHostname());
        writer.println("certificate.subject: " + ourCertificate.getSubjectX500Principal().getName());
        writer.println("certificate.issuer: " + ourCertificate.getIssuerX500Principal().getName());
        writer.println("certificate.expired: " + ourCertificate.getNotAfter().before(new Date()));
        writer.println("build.id: " + OxalisVersion.getBuildId());
        writer.println("build.tstamp: " + OxalisVersion.getBuildTimeStamp());

        // TODO add flag to indicate if OXALIS_HOME is specified or if default is used
        // TODO add info about Metro version installed

    }
}
