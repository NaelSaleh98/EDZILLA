var reg_error = document.getElementById("reg_errors");
function register_validation(){
    "use strict";
    var     firstName   = document.getElementById("first-name").value;
    var     lastName    = document.getElementById("last-name").value;
    var     alias       = document.getElementById("alias").value;
    var     email       = document.getElementById("email").value;
    var     password    = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    var err = "";
    if (chk_empty(firstName)){
        err += "<li>You must enter First Name.</li>";
    }
    if(chk_empty(lastName)){
        err += "<li>You must enter Last Name.</li>";
    }
    if ( (!chk_Letters(firstName))  ||  (!chk_Letters(lastName)) ){
        err += "<li>Name field must contain only letters.</li>";
    }
    if ( (firstName.length > 22)    ||  (lastName.length > 22) ){
        err += "<li>Name field should contain less than 22 letters.</li>";
    }
    if (chk_empty(alias)){
        err += "<li>You must enter Alias.</li>";
    }
    if (!chk_Letters_numbers(alias)){
        err += "<li>Alias field can contain letters & numbers only.</li>";
    }
    if ( (alias.length < 4) || (alias.length > 44) ){
        err += "<li>Alias size must be between 4 and 44.</li>";
    }
    if (chk_empty(email)){
        err += "<li>You must enter Email.</li>";
    }
    if (!chk_email(email)){
        err += "<li>Enter valid email , Please.</li>";
    }
    if ( (email.length < 8) || (email.length > 22)){
        err += "<li>Email size must be between 8 and 22.</li>";
    }
    
    if (chk_empty(password)){
        err += "<li>You must enter Password.</li>";
    }
    if(chk_empty(confirmPassword)){
        err += "<li>You must enter Password Confirmation.</li>";
    }
    if ( (!chk_password(password)) || (!chk_password(confirmPassword)) ){
        err += "<li>check a password between 7 to 16 characters which contain only characters, numeric digits, underscore and first character must be a letter.</li>";
    }
    if ( password !== confirmPassword){
            err += "<li>Password and password confirmation should be the same.</li>";
    }
    if (err == ""){
        return true ;
    }
    reg_error.innerHTML = err;
    return false;
}

// check if string is empty
function chk_empty(val){
    "use strict";
    return val == "" ? true : false;
}

// check if string contain only letters
function chk_Letters(inputTxt){
    "use strict";
    var letters = /^[A-Za-z]+$/;
    if(inputTxt.match(letters)) {
        return true;
    }
     else {
        return false;
     }
}

// check if string contain letters and numbers only
function chk_Letters_numbers(inputTxt){
    "use strict";
    var reg = /^[a-zA-Z][a-zA-Z0-9_]*/;
    if(inputTxt.match(reg)) {
        return true;
    }
     else {
        return false;
     }
}
// email pattern
function chk_email(email){
    "use strict";
    var reg = /^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$/;
    if(email.match(reg)) {
        return true;
    }
     else {
        return false;
     }
}
// password check
function chk_password(inputTxt){
    "use strict";
    var pass=  /^[A-Za-z]\w{7,16}$/;
    if(inputTxt.match(pass)){
        return true;
    }
    else{
        return false;
    }
}