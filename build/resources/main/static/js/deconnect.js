function deconnexion() {
    document.cookie = "userConnected=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    //console.log(document.cookie);
    window.location.href = "/";
}