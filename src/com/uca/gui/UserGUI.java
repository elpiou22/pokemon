package com.uca.gui;

import com.uca.core.UserCore;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.uca.dao.*;

import spark.Request;




public class UserGUI {

    /**
     * Affichage de la page FTL users.ftl
     * @return la liste des arguments dont le ftl peut avoir accès
     * @throws IOException
     * @throws TemplateException
     */
    public static String getAllUsers(Request request) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();

        Map<String, Object> input = new HashMap<>();

        input.put("users", UserCore.getAllUsers());
        input.put("InventoryDAO", new InventoryDAO());
        if (request.cookies().isEmpty()) {
            input.put("currentUser", -1);
        } else {
            input.put("currentUser", Integer.parseInt(request.cookies().get("userConnected")));
        }




        
        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/users.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);

        return output.toString();
    }
}
