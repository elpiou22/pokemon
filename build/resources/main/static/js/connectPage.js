



var donnees, firstname, lastname, pseudo, email, password, donnees, xhttp, response, id;
var hash;




function signin(event) {
    event.preventDefault();

    firstname = document.getElementById("firstname").value;
    lastname = document.getElementById("lastname").value;
    pseudo = document.getElementById("pseudo").value;
    email = document.getElementById("email").value;
    password = document.getElementById("password").value;

    if ((validerPrenom(firstname) !=1 ) || (validerNom(lastname) !=1 ) || (validerPseudo(pseudo) !=1 ) || (validerEmail(email) !=1 ) || validerPwd(password) !=1 ) {

        document.getElementById("err_firstname").innerHTML = "";
        document.getElementById("err_lastname").innerHTML = "";
        document.getElementById("err_pseudo").innerHTML = "";
        document.getElementById("err_email").innerHTML = "";
        document.getElementById("err_password").innerHTML = "";

        if (validerPrenom(firstname) !=1 ) {
            document.getElementById("err_firstname").innerHTML = validerPrenom(firstname);
        }

        if (validerNom(lastname) !=1 ) {
            document.getElementById("err_lastname").innerHTML = validerNom(lastname);
        }

        if (validerPseudo(pseudo) !=1 ) {
            document.getElementById("err_pseudo").innerHTML = validerPseudo(pseudo);
        }

        if (validerEmail(email) !=1 ) {
            document.getElementById("err_email").innerHTML = validerEmail(email);
        }

        if (validerPwd(password) !=1 ) {
            document.getElementById("err_password").innerHTML = validerPwd(password);
        } 

        
    } else {
    
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/signin", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4) {
                if (this.status == 200) {
                    response = JSON.parse(this.responseText);
                    id = response.id;
                    //console.log("ID utilisateur: " + id);
                    let date = new Date();
                    date.setTime(date.getTime() + (2 * 60 * 60 * 1000)); // ajout de 2 heures en millisecondes
                    let expires = "expires=" + date.toUTCString();

                    document.cookie = "userConnected=" + id + ";" + expires + "; path=/";

                    window.location.href = "/user/"+id;
                                    
                } else if (this.status == 409) {
                    document.getElementById("err_pseudo").innerHTML = "Le pseudo choisit n'est plus disponible";

                } else {
                    console.log("erreur de requete");
                }
            }
        };
        
        password = password + "$" + pseudo;
        password = CryptoJS.SHA256(password);

        donnees = "firstname=" + firstname + "&lastname=" + lastname + "&pseudo=" + pseudo + "&email=" + email + "&password=" + password;
        xhttp.send(donnees);
    }

}



function validerNom(nom) {
    if (nom.length > 2) {
        return 1;
    } else {
        return "Le nom doit faire au moins 2 caractères";
    }
}
  

function validerPrenom(prenom) {
    if (prenom.length > 2) {
        return 1;
    } else {
        return "Le prenom doit faire au moins 2 caractères";
    }
}

function validerPseudo(pseudo) {
    if (pseudo.length > 2) {
        return 1;
    } else {
        return "Le pseudo doit faire au moins 2 caractères";
    }
}
  

function validerUtilisateur(utilisateur){
    if (utilisateur == ""){
        return "Le nom d'utilisateur est vide";
    } else if (utilisateur.length < 5 ){
        return "Le nom d'utilisateur est trop petit";
    } else {
        return 1;
    }
}

function validerPwd(pwd){
    if (pwd === ""){
        return "Le mot de passe est vide";
    } else if (pwd.length < 6){
        return "Le mot de passe est trop petit";
    } else {
        return 1;
    }
}


function validerEmail(email){
    if (!email.includes("@")){
        return "L'email ne contient pas de '@'";
    }else if (!email.includes(".")){
        return "L'email ne contient pas de nom de domaine";
    } else {
        return 1;
    }
}



function login(event) {
    event.preventDefault();

    pseudo = document.getElementById("log_pseudo").value;
    password = document.getElementById("log_password").value;
    
    if ((validerPseudo(pseudo) !=1 ) || validerPwd(password) !=1 ) {
       
        document.getElementById("err_log_pseudo").innerHTML = "";
        document.getElementById("err_log_password").innerHTML = "";

      


        if (validerPseudo(pseudo) !=1 ) {
            document.getElementById("err_log_pseudo").innerHTML = validerPseudo(pseudo);
        }

        if (validerPwd(password) !=1 ) {
            //console.log("03");
            document.getElementById("err_log_password").innerHTML = validerPwd(password);
        } 
        
    } else {
    
        xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/login", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4) {
                if (this.status == 200) {

                    response = JSON.parse(this.responseText);
                    id = response.id;

                    let date = new Date();
                    date.setTime(date.getTime() + (2 * 60 * 60 * 1000)); // ajout de 2 heures en millisecondes
                    let expires = "expires=" + date.toUTCString();

                    document.cookie = "userConnected=" + id + ";" + expires + "; path=/";

                    window.location.href = "/user/"+id;
                                    
                } else if (this.status == 401) {
                    document.getElementById("login_error_message").innerHTML = "Le pseudo ou le mot de passe est incorrect";

                } else {
                    console.log("erreur de requete");
                }
            }
        };
        
        

        password = password + "$" + pseudo;
        password = CryptoJS.SHA256(password);

        donnees = "pseudo=" + pseudo + "&password=" + password;
        //console.log(donnees);
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

var connected_cookie = getCookie('userConnected');
if (connected_cookie != null) {
    userid = connected_cookie;
    window.location.href = "/user/"+userid;
}

function getUserConnected() {
    var connected_cookie = getCookie('userConnected');
    if (connected_cookie != null) {
        userid = connected_cookie;
        return userid;
    }
}



function showSignUp() {
    document.querySelector(".login-box").style.display = "none";
    document.querySelector(".signup-box").style.display = "block";
}

function showLogin() {
    document.querySelector(".signup-box").style.display = "none";
    document.querySelector(".login-box").style.display = "block";
}
