﻿
$personalForm = null;

function setEventListener() {
    ($('#idAcceptUpdatePersonalInfo')[0]).addEventListener("click", updatePersonalInfo);
    ($('#idLogOutUser')[0]).addEventListener("click", LogOut);
}

function setPersonalInfo() {
    var editBody = document.getElementById('mainFormPersonalInfo');
    if($personalForm==null)
        $personalForm = editBody;
    var i = 1;
    console.log(editBody);
    $($personalForm).each(function(){
        $("div",this).each(function(){
            if((this.className=='col-sm-9') && i<9) {
                if((this.firstElementChild).childNodes.length==0)
                    $(this.firstElementChild).val(currentUser[(Object.keys(currentUser))[i]]);

            i++;
            }
        });
    });
}

function loadTemplate() {
    var request = new XMLHttpRequest();
    request.open('GET', '/templates/pages/signin/personalInfo.html');
    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                $('#entry').html(request.responseText);
                setEventListener();
                setPersonalInfo();
            } else {
                alert('Network error, code: ' + request.status);
            }
        }
    };
    request.send(null);
}

function getUpdateDataUser() {
    var editBodyAdd = $('#mainFormPersonalInfo');
    var result = '';
    console.log(editBodyAdd);
    $(editBodyAdd).each(function(){
        $("div",this).each(function() {
            if(this.className=='col-sm-9') {
                var key = $(this.firstElementChild).attr('name');
                var value = $(this.firstElementChild).val();
                if(key == 'id' && value == ''){
                    value ="0";
                }else{
                    if($(this.firstElementChild).get(0).tagName == 'SELECT'){
                        value = value.substr(0,value.indexOf(' '))
                    }
                }
                result = result.concat('&',key,'=', value);
            }
        });
    });
    return result;
}

function sendUpdatePersonalInfo() {
    $.ajax({
        type: 'POST',
        url: '/servlet?action=UPDATE' + getUpdateDataUser()+'&tableName=USER',

        success: function(data) {

        }
    });
}

function updatePersonalInfo() {
    var name = document.getElementById("name");
    var surname = document.getElementById("surname");
    var email = document.getElementById("email");
    var mobilePhone = document.getElementById("mobilePhone");
    var login = document.getElementById("login");
    var passportNumber = document.getElementById("passportNumber");
    var pass = document.getElementById("pass");

    if(!validName(name.value) || !validName(surname.value) || !validPhone(mobilePhone.value) || !validEmail(email.value) ||
        !validLogin(login.value) || !validPassport(passportNumber.value)|| !validPassword(pass.value))
    {
        alert ("Данные заполнены неверно!");
        return  false;
    }

    sendUpdatePersonalInfo();
}

function setNewValueEntryDiv(textDiv) {
    var entry = document.getElementById("idEntryA");
    entry.innerHTML = textDiv;
}

function sendUserDataRegistration(login,email,pass,phone,name,surname,passport) {
    $.ajax({
        type: 'POST',
        url: '/servlet?action=REGISTRATION',
        data:{"login":login,"email":email,"password":pass,"mobilePhone":phone,"name":name,"surname":surname,"passportNumber":passport,"id":0,"id_role":1},
        success: function(data) {
            if(typeof data =='object') {
                currentUser = data;
                loadTemplate();
                setNewValueEntryDiv(currentUser.name);
            }
        }
    });
}

function sendUserDataLogin(email,pass){
     $.ajax({
         type: 'POST',
         url: '/servlet?action=AUTHORIZATION',
         data:{"email":email,"password":pass},
         success: function(data) {
             if(typeof data =='object') {
                 currentUser = data;
                 loadTemplate();

                 setNewValueEntryDiv(currentUser.name);
             }
         }
     });
}

function validateUpForm (){
    var name = document.getElementById("name");
    var surname = document.getElementById("surname");
    var passport = document.getElementById("passportNumber");

    var login = document.getElementById("login");
    var email = document.getElementById("emailUp");
    var password = document.getElementById("passUp");
    var phone = document.getElementById("mobilePhone");

    if (!validEmail(email.value) || !validPassword(password.value) || !validLogin(login.value)
        || !validPhone(phone.value)  || !validName(name.value)
        || !validName(surname.value) || !validPassport(passport.value)){
        alert ("Данные заполнены неверно!");
        return  false;
    }
    alert ("Данные успешно отправлены на сервер!");
    sendUserDataRegistration(login.value,email.value,password.value,phone.value,name.value,surname.value,passport.value);
}

function validateInForm (){
    var email = document.getElementById("emailIn");
    var passw = document.getElementById("passIn");

    if (!validEmail(email.value) || !validPassword(passw.value)){
        alert ("Данные заполнены неверно!");
        return  false;
    }
    alert ("Данные успешно отправлены на сервер!");
    sendUserDataLogin(email.value,passw.value);
}

function LogOut() {
    $.ajax({
        type: 'POST',
        url: '/servlet?action=LOGOUT',
        success: function(data) {
            console.log(data);
            currentUser = null;
            loadTemplate();
            setNewValueEntryDiv("Вход","#entry");
        }
    });
}

function validPassport(passport) {
    return (/(?=[a-zA-Z]{2}[0-9]{7})/).test(passport);
}
function validPhone(phone) {
    return (/(?=^\d[\d\(\)\ -]{4,14}\d$)/).test(phone);
}
function validName(name) {
    return (/(?=^[A-ZЁА-Я][a-zёа-я-_\.]{3,20}$)/).test(name);
}
function validLogin	(login) { //с ограничением 2-20 символов, которыми могут быть буквы и цифры, первый символ обязательно буква
    return (/^[a-zA-z]{1}[a-zA-Z1-9]{3,20}$/).test(login);
}
function validEmail(email) {
    return (/^(?:[-a-z\d\+\*\/\?!{}`~_%&'=^$#]+(?:\.[-a-z\d\+\*\/\?!{}`~_%&'=^$#]+)*)@(?:[-a-z\d_]+\.){1,60}[a-z]{2,6}$/).test(email);
}
function validPassword(passw) {
    return (/(?=^.{8,}$)/).test(passw);
}

