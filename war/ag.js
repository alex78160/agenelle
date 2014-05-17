/*
 * ag.js
 * gestion de la page principale
 * 
 */

    try {
      if (! window.badBrowser) {
        console.log("browser ok");
      }
    }
    catch(error){}

    ag$ = jQuery.noConflict();
    ag$(document).ready(function(){
    	
        ag$.ajaxSetup({
    	    timeout: 10000
    	});
    	
        ag$("#resume").click(function(){
            document.location.href = "/agenelle/dlservlet?index=2";
        });
        ag$("#cv").click(function(){
            document.location.href = "/agenelle/dlservlet?index=3";
        });

    ag$(function() {
        var name = ag$("#name"),
          email = ag$("#email"),
          msg = ag$("#msg"),
          allFields = ag$([]).add(name).add(email).add(msg),
          tips = ag$(".validateTips");
     
        function updateTips(t) {
          tips
            .text(t)
            .addClass("ui-state-highlight");
          setTimeout(function() {
            tips.removeClass("ui-state-highlight", 1500);
          }, 500 );
        }
     
        function checkLength( o, n, min, max ) {
          if ( o.val().length > max || o.val().length < min ) {
            o.addClass("ui-state-error");
            updateTips("la longueur du champ " + n + " doit \352tre entre " +
              min + " et " + max + ".");
            return false;
          } else {
            return true;
          }
        }
     
        function checkRegexp(o, regexp, n) {
          if ( !(regexp.test(o.val())) ) {
            o.addClass("ui-state-error");
            updateTips(n);
            return false;
          } else {
            return true;
          }
        }
     
        ag$("#dialog-form").dialog({
          autoOpen: false,
          /*
          show: {
            effect: "blind",
            duration: 500
          },
          hide: {
            effect: "blind",
            duration: 1000
          },
          */
          height: "auto",
          width: 500,
          modal: true,
          buttons: {
            "Envoyer": function() {
              var bValid = true;
              allFields.removeClass("ui-state-error");
     
              bValid = bValid && checkLength(name, "nom", 3, 30);
              bValid = bValid && checkLength(email, "email", 6, 80);
     
              bValid = bValid && checkRegexp(email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "format invalide. exemple : ui@jquery.com");
              bValid = bValid && checkLength(msg, "message", 1, 5000);
              
              if (bValid) {
                ag$("#dialog-message").dialog({
            	  modal: true,            		  
                  close: function(event, ui) {
                    ag$("#erreur").hide();
                    ag$("#envoye").hide();
                    ag$("#encours").show();
                    ag$("#dialog-form").dialog("close");
                  },
                  buttons: {
                    Ok: function() {
                      ag$(this).dialog("close");
                      ag$("#dialog-form").dialog("close");
                    }
                  }            	  		
            	});
                ag$("#dialog-form").nextAll(".ui-dialog-buttonpane").find("button:contains('Envoyer')").attr("disabled",true).addClass("ui-state-disabled");
                
                ag$("#dialog-message").nextAll(".ui-dialog-buttonpane").find("button:contains('Ok')").attr("disabled",true).addClass("ui-state-disabled");
                ag$("#erreur").hide();
                ag$("#envoye").hide();
                ag$("#encours").show();

            	ag$.post( "/agenelle/msgservlet"
                  ,{name: ag$("#name").val(), email: ag$("#email").val(), msg: ag$("#msg").val()}
                  ,function(data) {
                    try {
                      console.log(data.status);
                    }
                    catch(error){}
                    if (! ag$("#dialog-message").is(":visible")) {
                      ag$("#dialog-message").dialog("open");
                    }
            		ag$("#dialog-message").nextAll(".ui-dialog-buttonpane").find("button:contains('Ok')").attr("disabled",false).removeClass("ui-state-disabled");
            		if(data.status == 'ok') {
            			ag$("#erreur").hide();
            			ag$("#encours").hide();
            			ag$("#envoye").show();
            		}
            		else {
            			ag$("#encours").hide();
            			ag$("#envoye").hide();
            			ag$("#erreur").show();            			
            		}}
            	  ,"json")
                      .fail(function() {
                        ag$("#encours").hide();
                        ag$("#envoye").hide();
                		ag$("#erreur").show();
                        ag$("#dialog-message").nextAll(".ui-dialog-buttonpane").find("button:contains('Ok')").attr("disabled",false).removeClass("ui-state-disabled");
                      });
              }
            },
            "Annuler": function() {
              try {
                console.log("annuler");
              }
              catch(error){}
              ag$(this).dialog("close");
            }
          },
          close: function() {
            allFields.val("").removeClass("ui-state-error");
            updateTips("Tous les champs sont obligatoires");
            ag$("#dialog-form").nextAll(".ui-dialog-buttonpane").find("button").attr("disabled",false).removeClass("ui-state-disabled").removeClass("ui-state-hover").removeClass("ui-state-focus");
            ag$("#send-message").removeClass("ui-state-hover").removeClass("ui-state-focus");
            ag$("#dialog-form").dialog( "option", "position", {my: "center", at: "center", of: window});
          }
        });
        
        if (! window.badBrowser) {
          ag$("#dialog-form").dialog("option", "show", {effect: "blind", duration: 500});
          ag$("#dialog-form").dialog("option", "hide", {effect: "clip", duration: 500});
        }

        ag$("#send-message")
          .button()
          .click(function() {
        	  ag$("#dialog-form").dialog("open");
          });
      });
    });
