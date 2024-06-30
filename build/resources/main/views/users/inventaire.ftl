<#ftl encoding="utf-8">

<!doctype html>
<html lang="fr">
    <head>
        <meta charset="utf-8" >
        <title> INVENTAIRE </title>
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

 
      <div class="padding"></div>

      <div id ="page_container" >
        <div class="user_container">
          <img src="../images/profil_homme.png" alt="Image" class="image_prf">
          <div class="text_prf">
            <p><b>${userDAO.getUserById(userId).getPseudo()}</b></p>
            <br>
            <p>Titre: <b>${userDAO.getUserById(userId).getTitre()}</b></p>
            <br>
            <p>Compté créé le: ${userDAO.getCreationDate(userId)}</p>
            <br>
            <p>${userDAO.getUserById(userId).getPseudo()}  possède <b>${pokemons?size}</b> pokemons</p>
            <br>
            <#if currentUser == userId>
              <p> Vous avez <b>${nbPendingTrades}</b> demandes en attente </p>
              <br>
              <button id="demande_echange">Demandes d'échange</button>
            </#if>
          </div>
        </div>

        



          




        <div class="inventory_container">
          <br><br>
          <h1>Vos pokémons : </h1>

          <#list pokemons as pokemon>
            <div class="pokemon ${PokemonCore.getInfos(pokemon.getidInstancePokemon()).getRarity()}" onclick="updateUrlToPokemon(${pokemon.getidInstancePokemon()?string('0')})" >
    
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
              <p class="pokemonId">instanceid ${PokemonCore.getInfos(pokemon.getidInstancePokemon()).getPokemonInstanceId()}</p>

            </div>
          </#list>
        </div>

      </div>

      
      <#if currentUser == userId>
       
          <div id="popup_trades">
            <div class="center_content">
              <button id="close_popup" onclick="closePopup()">Fermer</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Demandé par</th>
                        <th>Le</th>
                        <th>Voir</th>
                    </tr>
                </thead>
                <tbody>
                    <#assign count = 1>
                    <#list tradeIds as tradeId>
                        <tr>
                            <td class="center_content">${count}</td>
                            <td class="center_content">${userDAO.getUserById(tradeDAO.getUserId1(tradeId)).getPseudo()}</td>
                            <td class="center_content">${tradeDAO.getDateStr(tradeDAO.getDate(tradeId))}</td>
                            <td class="center_content">
                              <button onclick="seeTrade(${tradeId})">Voir</button>
                            </td>
                        </tr>
                        <#assign count = count + 1>
                    </#list>
                </tbody>
            </table>
          </div>
      
    


      <#if currentTradeId != -1>
        <div id="popup_trade" class="popup">
          <div class="center_content">
            <button id="close_popup" onclick="closePopup()">Fermer</button>
          </div>
          <div class="container_popup">
            <img src="${PokemonCore.getInfos(tradeDAO.getPokemonInstanceId2(currentTradeId)).getImageUrl()}" class="left img pokemonIMG"  alt="pokemon Image" >
            <div class="left">
              <p> Le pokemon que tu envois: </p> 
              <p><u>${PokemonCore.getInfos(tradeDAO.getPokemonInstanceId2(currentTradeId)).getName()}</u></p>
              <p class="pokemonTypes">
                <#assign count = 0>
                <#list PokemonCore.getInfos(tradeDAO.getPokemonInstanceId2(currentTradeId)).getTypes() as type>
                  <b>${type}</b>
                  <br>
                  <#assign count = count + 1>
                </#list>
                <#if count == 1>
                    <br>
                </#if>
              </p>
              <p>level: ${PokemonCore.getInfos(tradeDAO.getPokemonInstanceId2(currentTradeId)).getLevel()}</p>
              <p>id: ${PokemonCore.getInfos(tradeDAO.getPokemonInstanceId2(currentTradeId)).getPokemonId()}</p>
              <p>instanceid: ${tradeDAO.getPokemonInstanceId2(currentTradeId)}</p>
            </div> 
          
            <img src="../images/fleche.png" class="fleche_echange">
            <div class="right">
              <p> Le pokemon que tu reçois: </p>
              <p><u>${PokemonCore.getInfos(tradeDAO.getPokemonInstanceId1(currentTradeId)).getName()}</u></p>
              <p class="pokemonTypes">
                <#assign count = 0>
                <#list PokemonCore.getInfos(tradeDAO.getPokemonInstanceId1(currentTradeId)).getTypes() as type>
                  <b>${type}</b>
                  <br>
                  <#assign count = count + 1>
                </#list>
                <#if count == 1>
                    <br>
                </#if>
              </p>
              <p>level: ${PokemonCore.getInfos(tradeDAO.getPokemonInstanceId1(currentTradeId)).getLevel()}</p>
              <p>id: ${PokemonCore.getInfos(tradeDAO.getPokemonInstanceId1(currentTradeId)).getPokemonId()}</p>
              <p>instanceid: ${tradeDAO.getPokemonInstanceId1(currentTradeId)}</p>  
            </div>
            <img src="${PokemonCore.getInfos(tradeDAO.getPokemonInstanceId1(currentTradeId)).getImageUrl()}" class="right img">
          </div>
          <div class="button-container">
            <button id="accepter_echange" onclick="accepterEchange(${userId}, ${currentTradeId})">Accepter</button>
            <p>   </p>
            <button id="refuser_echange" class="button_non" onclick="refuserEchange(${userId}, ${currentTradeId})">Refuser</button>
          </div> 
        </div>
      </#if>
    </#if>



    <script src="../js/accept&refuseTrade.js"></script>
    <script src="../js/deconnect.js"></script>
  </body>
      

</html>
