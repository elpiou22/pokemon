package com.uca.gui;

import com.uca.core.UserCore;
import com.uca.core.InventoryCore;
import com.uca.core.PokemonCore;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;



import com.uca.*;
import com.uca.entity.InventoryEntity;




import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import spark.Request;

public class PokedexGUI {


    /**
     * Affichage de la page FTL pokedex.ftl
     * @return la liste des arguments dont le ftl peut avoir accès
     * @throws IOException
     * @throws TemplateException
     */
    public static String getPokedex(Request request) throws IOException, TemplateException {


        Configuration configuration = _FreeMarkerInitializer.getContext();
        Map<String, Object> input = new HashMap<>();



        
        input.put("PokemonCore", new PokemonCore());
        if (request.cookies().isEmpty()) {
            input.put("currentUser", -1);
        } else {
            input.put("currentUser", Integer.parseInt(request.cookies().get("userConnected")));
        }



        Writer output = new StringWriter();
        Template template = configuration.getTemplate("pokemons/pokedex.ftl"); 

        template.setOutputEncoding("UTF-8");
        template.process(input, output);



        return output.toString();
    }

    
}
