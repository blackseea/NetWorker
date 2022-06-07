// function register() {}

window.onload = function callConfirm() {
    var text;
    var confirm = window.confirm("是否注册？");
    if (confirm) {
        requireRegister();
    } else {
        text = "Dont Press button";
    }
    //    document.getElementById("result").innerHTML = text;
}

function requireRegister() {
    var xmlHttp

    xmlHttp = new XmlHttpRequest();

    xmlHttp.onreadystatechange = function() {
        // if (xmlHttp.readState == 4 &&xmlHttp.status == 200)
        // {

        // }
        xmlHttp.open("GET", "/db/register.html");
        xmlHttp.send();
    }
}