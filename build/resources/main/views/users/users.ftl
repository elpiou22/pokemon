<#ftl encoding="utf-8">

<!doctype html>
<html lang="fr">
    <head>
        <meta charset="utf-8" >
        <title> USERS </title>
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
        <br> 
        <table class="users_tb">
            <thead>
                <tr class="tr_tb">
                    <th class="th_tb">#</th>
                    <th class="th_tb">Pseudo</th>
                    <th class="th_tb">Titre</th>
                    <th class="th_tb">Nombre de Pokémons</th>
                    <th class="th_tb">Dernière Connexion</th>
                    <th class="th_tb">User ID</th>
                    <th class="th_tb">Voir l'inventaire</th>
                </tr>
            </thead>
            <tbody>
                <#assign count = 1>
                <#list users as user>
                    <tr class="tr_tb">
                        <td class="td_tb">${count}</td>
                        <td class="td_tb">${user.pseudo}</td>
                        <td class="td_tb">${user.titre}</td>
                        <td class="td_tb">${InventoryDAO.getNbPokemons(user.id)}</td>
                        <td class="td_tb">${user.getLastConnectionStr()}</td>
                        <td class="td_tb">${user.id}</td>
                        <td class="td_tb"><a href="/user/${user.id}">    
                            <div class="container_btn">
                                <div class="button-container">
                                    <button id="voir">Inventaire</button>
                                </div>
                            </div></a>
                        </td>
                    </tr>
                    <#assign count = count + 1>
                </#list>
            </tbody>
        </table>
  </body>
      
    <script src="../js/deconnect.js"></script>
</html> 
