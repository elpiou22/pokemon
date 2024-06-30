<#ftl encoding="utf-8">

<!doctype html>
<html lang="fr">
    <head>
        <meta charset="utf-8" >
        <title> POKEDEX </title>
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


    <div class="pokedex_container">


      <#list 1..1010 as index>
      
        <div class="pokemon_pkdx ${PokemonCore.getInfos(index).getRarity()}">
      
          <p class="pokemonName">${PokemonCore.getInfos(index).getName()}</p>
          
          <img src="${PokemonCore.getInfos(index).getImageUrl()}" alt="pokemon Image" class="pokemonIMG">
          
          <p class="pokemonTypes">
            <#assign count = 0>
            <#list PokemonCore.getInfos(index).getTypes() as type>
              ${type}
              <br>
              <#assign count = count + 1>
            </#list>
            <#if count == 1>
                <br>
            </#if>
          </p>
          
          <p class="pokemonId">${index}</p>
          

        </div>
        
      </#list>


    </div>
  </body>
      
<script src="../js/deconnect.js"></script>
</html>
