package com.sitenetsoft.opensource.antidote.services;

import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AntidoteServices {

    public static final String module = AntidoteServices.class.getName();

    static class ZoneSections {
        public static String factures = "https://services.druide.com/client/factures/";
    }

    static class ZoneAuth {
        public static String identifiant = "https://services.druide.com/connexion/identifiant/methode-connexion?app=aw&nomUtilisateur={username}&seSouvenir=false&jeton={jeton}";
        public static String identifiantHttpMethod = "GET";
    }

    static class LoginFormInputs {
        public static String username = "username";
        public static String password = "password";
    }

    public static Map<String, Object> pullDataAntidote(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();

        CustomerCenterSections customerCenterSections = new CustomerCenterSections();
        LoginFormInputs loginFormInputs = new LoginFormInputs();

        try {

            try {
                // Get the Cookie that is initialized in the login form page
                Connection.Response loginForm = Jsoup.connect(customerCenterSections.dashboard)
                        .method(Connection.Method.GET)
                        .execute();

                Document document = Jsoup.connect(customerCenterSections.dashboard)
                        //.data("cookieexists", "false")
                        .data(LoginFormInputs.username, username)
                        .data(LoginFormInputs.password, password)
                        .cookies(loginForm.cookies())
                        .post();
                System.out.println(document);
            } catch (Exception exception) {
                System.out.println(exception);
            }

            /*try {
                Document doc = Jsoup.connect("http://example.com").get();
                doc.select("p").forEach(System.out::println);
                System.out.println(doc.select("p").toString());
            } catch (Exception exception) {
                System.out.println(exception);
            }*/

            GenericValue videotron = delegator.makeValue("AntidoteInvoices");
            // Auto generating next sequence of ofbizDemoId primary key
            antidote.setNextSeqId();
            // Setting up all non primary key field values from context map
            antidote.setNonPKFields(context);
            // Creating record in database for OfbizDemo entity for prepared value
            antidote = delegator.create(antidote);
            result.put("antidoteId", antidote.getString("antidoteId"));
            Debug.log("==========This is my first Java Service implementation in Apache OFBiz. Antidote record created successfully with ofbizDemoId: " + antidote.getString("antidoteId"));
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError("Error in creating record in Antidote Invoice entity ........" +module);
        }
        return result;

        /*Connection.Response loginForm = Jsoup.connect("https://www.desco.org.bd/ebill/login.php")
        .method(Connection.Method.GET)
        .execute();

        Document document = Jsoup.connect("https://www.desco.org.bd/ebill/authentication.php")
        .data("cookieexists", "false")
        .data("username", "32007702")
        .data("login", "Login")
        .cookies(loginForm.cookies())
        .post();
        System.out.println(document);*/
    }

}