<#ftl encoding="utf-8">

<!doctype html>
<html lang="fr">
    <head>
        <meta charset="utf-8" >
        <title> POKEMON </title>
        <link rel="stylesheet" href="../css/reset.css">
        <link rel="stylesheet" href="../css/style.css">
        <link rel="icon" href="../images/pikachu.png" type="x-icon">
    </head>

    <header>

        <!-- Debut navbar -->
        <ul class="navbar"> 
            <li>
                <#if currentUser == -1>
                  <a href="/?reconnect_msg=1" title="Profil" class="  nav-a">
                  <span>Profil </span>
                  </a>
                <#else>

                  <a href="/user/${currentUser}" title="Profil" class="  nav-a">
                  <span>Profil </span>
                  </a>
                </#if>
            </li>

            <li>
                <a href="/pokedex" title="Pokedex" class="  nav-a">
                <span>Pokédex </span>
                </a>
            </li>

            <li>
                <a title="Accueil" href="/user/${currentUser}">
                    <img src="../images/pokemon.png" class="logo" alt="Logo">	
                </a>
            </li>

            <li>
                <a onclick="deconnexion()" title="Déconnection" class="  nav-a">
                <span>Déconnection</span>
                </a>
            </li>

            <li>
                <a href="/users" title="users" class="  nav-a">
                <span>Utilisateurs </span>
                </a>
            </li>
        </ul>
        <!-- Fin navbar -->

    </header>

  <body>



    <#assign isUnofficialPokemon = (PokemonCore.getInfos(pokemonId).getPokemonInstanceId() > 20)>

    <div class="padding"></div>

    <div class="pokedex_container" id="pokedex_container">
      
        <div class="pokemon_page ${PokemonCore.getInfos(pokemonId).getRarity()}" style="text-align: center;">
      
          <p class="pokemonName">${PokemonCore.getInfos(pokemonId).getName()}</p>
          
          <img src="${PokemonCore.getInfos(pokemonId).getImageUrl()}" alt="pokemon Image" class="pokemonIMG">
          
          <p class="pokemonTypes">
            <#assign count = 0>
            <#list PokemonCore.getInfos(pokemonId).getTypes() as type>
              ${type}
              <br>
              <#assign count = count + 1>
            </#list>
            <#if count == 1>
                <br>
            </#if>
          </p>

          <#if isUnofficialPokemon>
            <p class="pokemonLevel" id="pokemonLevel">level: ${PokemonCore.getInfos(pokemonId).getLevel()}  </p>
          </#if>
          <p class="pokemonId"> id : ${PokemonCore.getInfos(pokemonId).getPokemonId()}</p>
          <p class="pokemonId">${pokemonId}</p>

        </div>
    
      <div class="container_btn">
        <div class="button-container">
          <#if userDAO.getOwnerOfPokemonId(pokemonId).getId() == currentUser>
            <p id="err_demande_echange"> Vous ne pouvez pas échanger un pokémon que vous avez déjà!  </p>
            <button >Demander un échange</button>
            <p>   </p>
          <#else>
            <button id="demande_echange">Demander un échange</button>
            <p id="not_connected">  </p>
          </#if>
        </div>
        <div class="button-container">
          <p id= "error_upgrade_message"> Vous avez plus d'amélioration possible aujourd'hui ! </p>
          <p id="error_upgrade_message2"> Le pokemon est déjà au niveau 100 ! </p>
          <p id= "successfully_upgrade_message"> Le pokemon a été amélioré </p>
          <button id="upgrade_button" onclick="upgrade(${pokemonId?string('0')})"> Upgrade </button>
        </div>
      </div>
    </div>



        <div class="popup" id="popup">
          <div class="container_popup">         
              <#if currentUrl?contains("?pokemonid=")>
                <#assign indexStart = currentUrl?indexOf("?pokemonid=") + 11>
                <#assign pokemonid1 = currentUrl?substring(indexStart)?number>
                <img src="${PokemonCore.getInfos(pokemonid1).getImageUrl()}" class="left img pokemonIMG"  alt="pokemon Image" >
                <div class="left">
                  <p> Le pokemon que tu envois: </p> 
                  <p><u>${PokemonCore.getInfos(pokemonid1).getName()}</u></p>
                  <p class="pokemonTypes">
                    <#assign count = 0>
                    <#list PokemonCore.getInfos(pokemonid1).getTypes() as type>
                      <b>${type}</b>
                      <br>
                      <#assign count = count + 1>
                    </#list>
                    <#if count == 1>
                        <br>
                    </#if>
                  </p>
                  <p>level: ${PokemonCore.getInfos(pokemonid1).getLevel()}</p>
                  <p>id: ${PokemonCore.getInfos(pokemonid1).getPokemonId()}</p>
                  <p>instanceid: ${pokemonid1}</p>
                </div> 
              <#else>
                <div class="button_choose_container padding_right" >
                  <button id="choose_pokemon">Choisir</button>
                </div>  
              </#if>
              <img src="../images/fleche.png" class="fleche_echange">
              <div class="right">
                <p> Le pokemon que tu reçois: </p>
                <p><u>${PokemonCore.getInfos(pokemonId).getName()}</u></p>
                <p class="pokemonTypes">
                  <#assign count = 0>
                  <#list PokemonCore.getInfos(pokemonId).getTypes() as type>
                    <b>${type}</b>
                    <br>
                    <#assign count = count + 1>
                  </#list>
                  <#if count == 1>
                      <br>
                  </#if>
                </p>
                <p>level: ${PokemonCore.getInfos(pokemonId).getLevel()}</p>
                <p>id: ${PokemonCore.getInfos(pokemonId).getPokemonId()}</p>
                <p>instanceid: ${PokemonCore.getInfos(pokemonId).getPokemonInstanceId()}</p>  
              </div>
                <img src="${PokemonCore.getInfos(pokemonId).getImageUrl()}" class="right img">
          </div>
          <#if currentUrl?contains("?pokemonid=")>
            <div class="button-container">
              <button id="choose_pokemon">Changer</button>
              <p>   </p>
              <button id="proposer_echange" onclick="submitForm(${userDAO.getOwnerOfPokemonId(pokemonId).getId()}, ${pokemonid1?string('0')}, ${pokemonId?string('0')})"> Proposer l'échange</button>
            </div>   
          </#if>
        </div>


      <#-- popup choose pokemon -->
      <div class="inventory_container_choose" id="inventory_container">
        <#list pokemonsOfCurrentUser as pokemon>
          <div class="pokemon ${PokemonCore.getInfos(pokemon.getidInstancePokemon()).getRarity()}" onclick="updateUrl(${PokemonCore.getInfos(pokemon.getidInstancePokemon()?string('0')?number).getPokemonInstanceId()?string('0')})">
            <p class="pokemonName">${PokemonCore.getInfos(pokemon.getidInstancePokemon()).getName()}</p>

            <img src="${PokemonCore.getInfos(pokemon.getidInstancePokemon()).getImageUrl()}" alt="pokemon Image" class="pokemonIMG">

            <p class="pokemonTypes">

            <#assign count = 0>
            <#list PokemonCore.getInfos(pokemon.getidInstancePokemon()).getTypes() as type>
              ${type}
              <br>
              <#assign count = count + 1>
            </#list>
            <#if count == 1>
              <br>
            </#if>
            </p>
            <p> level: ${PokemonCore.getInfos(pokemon.getidInstancePokemon()).getLevel()} </p>
            <p class="pokemonId"> id: ${PokemonCore.getInfos(pokemon.getidInstancePokemon()).getPokemonId()}</p>
            <p class="pokemonInstanceId">instanceid ${pokemon.getidInstancePokemon()}</p> 
          </div>
        </#list>
      </div>




    

  </body>
<script src="../js/trade.js"></script>
<script src="../js/deconnect.js"></script>
</html>
