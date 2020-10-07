var reg_error = document.getElementById("reg_errors");
// Register Validation //
function registerValidation(){
    "use strict";
    var     firstName   = document.getElementById("first-name").value;
    var     lastName    = document.getElementById("last-name").value;
    var     alias       = document.getElementById("alias").value;
    var     email       = document.getElementById("email").value;
    var     password    = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    var err = "";
    if (chk_empty(firstName) || chk_empty(lastName)){
        err += "<li>Please, Enter your First Name & Last Name.</li>";
    }
    else if ( (!chk_Letters(firstName))  ||  (!chk_Letters(lastName)) ){
        err += "<li>Please, Your Name should contain only letters.</li>";
    }
    else if ( (firstName.length > 32)    ||  (lastName.length > 32) ){
        err += "<li>Please, Your Name cannot contain more than 32 letters.</li>";
    }
    if (chk_empty(alias)){
        err += "<li>Please, Enter your Alias.</li>";
    }
    else if (!chk_Letters_numbers(alias)){
        err += "<li>Please, Alias can contain letters & numbers & underscore only, but cannot end or start with underscore.</li>";
    }
    else if ( (alias.length < 4) || (alias.length > 64) ){
        err += "<li>Please, Alias size should be between 4 and 64.</li>";
    }
    if (chk_empty(email)){
        err += "<li>Please, Enter your Email.</li>";
    }
    else if (!chk_email(email)){
        err += "<li>Please, Enter valid Email => user@test.com.</li>";
    }
    else if ( (email.length < 8) || (email.length > 64)){
        err += "<li>Please, Email size should be between 8 and 64.</li>";
    }
    
    if (chk_empty(password) || chk_empty(confirmPassword)){
        err += "<li>Please, Enter Password & Password Confirmation.</li>";
    }
    else if ( (password.length < 8) || (password.length > 128) ){
        err += "<li>Please, Password size should be between 8 and 128.</li>";
    }
    else if ( (!chk_password(password))){
        err += "<li>Please, Check that the a password contain <br> At least one digit. <br> At least one lowercase character. <br> At least one uppercase character.</li>";
    }
    else if ( password !== confirmPassword){
            err += "<li>Please, Password and Password Confirmation should be the same.</li>";
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
    var reg = /^(?![_])(?!.*[_]{2})[a-zA-Z0-9_]+(?<![_])$/;
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
    var reg = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
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
    var pass=  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$/;
    if(inputTxt.match(pass)){
        return true;
    }
    else{
        return false;
    }
}

/* ------------------------------- */
/* Search */
