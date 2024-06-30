function accepterEchange(id, tradeId) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/acceptTrade", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4) {
            if (this.status == 200) {
                //console.log("Requête envoyée avec succès !");
                window.location.href = "/user/"+id;
                                
            } else {
                console.log("erreur de requete");
            }
        }
    };

    var donnees = "tradeid="+tradeId+"";
    xhttp.send(donnees);
}

function refuserEchange(id, tradeId) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "/refuseTrade", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4) {
            if (this.status == 200) {
                //console.log("Requête envoyée avec succès !");
                window.location.href = "/user/"+id;
                                
            } else {
                console.log("erreur de requete");
            }
        }
    };

    var donnees = "tradeid="+tradeId+"";
    xhttp.send(donnees);
}


function updateUrlToPokemon(id){
    window.location.href = "/pokemon/"+id;
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


/* fenetre popup */
const popup = document.getElementById('popup_trades');
const popup2 = document.getElementById('popup_trade');
const gris = document.getElementById("page_container");

const trades = document.getElementById('demande_echange');
trades.addEventListener('click', () => {
    

    popup.style.display = "block";
    gris.style.opacity = "0.3" ;
    gris.style.pointerEvents = "none";

});

function closePopup() {
    popup.style.display = "none";
    try {
        popup2.style.display = "none";
    } catch (error) {}
    gris.style.opacity = "1" ;
    gris.style.pointerEvents = "auto";
}

function seeTrade(id) {
    
    var url = window.location.href;
    var index = url.indexOf("?");
    if (index == -1) {
        window.location.href += "?tradeid=" + id;
    } else {
        window.location.href = url.substring(0, url.indexOf("?")) + "?tradeid=" + id;
    }
}

if (window.location.href.indexOf("?tradeid=") > -1) {
    popup.style.display = "none";
    popup2.style.display = "block";
    gris.style.opacity = "0.3" ;
    gris.style.pointerEvents = "none";
}