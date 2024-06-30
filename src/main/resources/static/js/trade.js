/* var */
var connected_cookie = getCookie('userConnected');
var userid, user1;
 
/* fin var */

/* fenetre popup */
const popup = document.getElementById('popup');
const gris = document.getElementById("pokedex_container");


try {
    const newtrade = document.getElementById('demande_echange');
    newtrade.addEventListener('click', () => {
        connected_cookie = getCookie('userConnected');
        
        if (connected_cookie == null) {

            document.getElementById("not_connected").innerHTML = "Vous n'êtes pas connecté pour effectuer des opérations sur un des pokémon !";
            
        } else {
            popup.style.display = "block";
            gris.style.opacity = "0.3" ;
        }
    });
} catch (error) {}


if (window.location.href.indexOf("?pokemonid=") > -1) {
    popup.style.display = "block";
    gris.style.opacity = "0.3" ;
}


/* fin fenetre */ 



function updateUrl(id) {
    var url = window.location.href;
    var index = url.indexOf("?");
    if (index == -1) {
        window.location.href += "?pokemonid=" + id;
    } else {
        window.location.href = url.substring(0, url.indexOf("?")) + "?pokemonid=" + id;
    }
}


const popup2 = document.getElementById('inventory_container');

const choose_pokemon = document.getElementById('choose_pokemon');
choose_pokemon.addEventListener('click', () => {
    popup2.style.display = "block";
    popup.style.display = "none";
});

  
function submitForm(user2,p1,p2) {

    if (connected_cookie == null) {
        window.location.href = "/?reconnect_msg=1";

    } else {


            
        var xhttp = new XMLHttpRequest();

        xhttp.open("POST", "/tradesubmitted", true);

        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

        xhttp.onreadystatechange = function() {
            if (this.status == 200) {

                //console.log("Requête envoyée avec succès !");

                let currentUrl = window.location.href;
                let cleanUrl = currentUrl.split("?")[0];
                window.location.href = cleanUrl;

            }
        };

        user1 = connected_cookie;
        var donnees = "user1="+user1+"&user2="+user2+ "&pokemon1=" + p1 + "&pokemon2=" + p2 + "";

        xhttp.send(donnees);
    }
}


// ---------------------------------
// upgrade

function upgrade(pokemonid) {


    connected_cookie = getCookie('userConnected');
    
    if (connected_cookie == null) {
        window.location.href = "/?reconnect_msg=1";

    } else {

        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/upgrade", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4) {
                if (this.status == 200) {
                    //console.log("Requête envoyée avec succès !");
                    document.getElementById("successfully_upgrade_message").style.display = "block";
                    document.getElementById("error_upgrade_message").style.display = "none";
                    document.getElementById("pokemonLevel").innerHTML = "level: " + ((parseInt(document.getElementById("pokemonLevel").textContent.split(" ")[1]) + 1).toString());
                } else if (this.status == 409){

                    document.getElementById("error_upgrade_message2").style.display = "block";
                    document.getElementById("successfully_upgrade_message").style.display = "none";
                } else if (this.status == 429){
                    //console.log("pas d'upgradees disponibles ajd");
                    document.getElementById("error_upgrade_message").style.display = "block";
                    document.getElementById("successfully_upgrade_message").style.display = "none";
    
                } else {
                    console.log("erreur de requete");
                }
            }
        };
    
    
    
        userid = connected_cookie;
        var donnees = "user="+userid+"&pokemon=" + pokemonid + "";
        xhttp.send(donnees);

    }
}



function getCookie(name) {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i].trim();
      if (cookie.startsWith(name + '=')) {
        return cookie.substring(name.length + 1, cookie.length);
      }
    }
    return null;
}


