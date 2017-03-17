var gameModel;

$( document ).ready(function() {
  // Handler for .ready() called.
  $.getJSON("model", function( json ) {
  gameModel = json;
    console.log( "JSON Data: " + json );
   });
});


            function openNav() {
                document.getElementById("myNav").style.height = "100%";
            }
            function closePlaceShipsOverlay() {
                //Checks if all the player ships have been placed before closing the overlay
                if (checkAllShipsPlaced() == true) {
                var isHardDifficulty = document.getElementById("hardDifficulty").checked;
                //Checks the selected difficulty, if hard is chosen on the slider then the difficulty is set to hard in the model
                //The default difficulty is Easy
                if ( isHardDifficulty) {
                    gameModel.hardAI = 1;
                    //the following will call a function that will change the computer's ship placement to be random
                    changeComputerShipPlacement();
                }
                document.getElementById("myNav").style.height = "0%";
                }
                //If all ships havent been placed pop up with a notification
                else {
                    //still need to implement notification for failure
                }
            }

            function openDuckAndCoverNav() {
                            document.getElementById("DAC_Nav").style.height = "100%";
                        }
            function closeDuckAndCoverNav() {
                            document.getElementById("DAC_Nav").style.height = "0%";
                        }

function placeShip(x,y) {
   console.log($( "#shipSelec" ).val());
   console.log($( "#orientationSelec" ).val());

   //var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/placeShip/"+$( "#shipSelec" ).val()+"/"+ x +"/"+ y +"/"+$( "#orientationSelec" ).val(),
     method: "post",//file:/usr/share/doc/HTML/index.html
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });

   request.done(function( currModel ) {
     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Request failed: " + textStatus );
   });
}


//Function to check if all the player ships have been placed
function checkAllShipsPlaced() {
    if (gameModel.aircraftCarrier.isPlaced == true && gameModel.battleship.isPlaced == true  && gameModel.clipper.isPlaced == true  && gameModel.dinghy.isPlaced == true  && gameModel.submarine.isPlaced == true){
        return true;
    }
    else {
        return false;
    }
}



function fire(x, y){
   var request = $.ajax({
     url: "/fire/" + x + "/" + y,
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });

   request.done(function( currModel ) {
     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Request failed: " + textStatus );
   });

}

function scan(x,y){
   var request = $.ajax({
     url: "/scan/" + x + "/" + y,
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });

   request.done(function( currModel ) {
     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Request failed: " + textStatus );
   });

}
//IMPORTANT: resolveTileClick() requires scanfire html checkbox (W3schools has a good example of how to turn the checkbox to a slider)

//Function called by click on any tile, checks for scan or fire option, then parses html id for x,y coordinates and calls apt. fct.
function resolveTileClick(btnId){
    var isFire = document.getElementById("scanfire").checked;
    var numSep = btnId.indexOf("_");
    //changed here
    var x = btnId.substring(0,numSep);
    var y = btnId.substring(numSep+1);
    if(isFire){
        fire(x,y);
    } else {
        scan(x,y);
    }
}

function placeShipOnClick(btnId){
    var numSep = btnId.indexOf("_");
    var x = btnId.substring(0,numSep);
    var y = btnId.substring(numSep+1);
    placeShip(x,y);
}

function log(logContents){
    console.log(logContents);
}

function displayGameState(gameModel){
$( '#MyBoard td'  ).css("background-color", "#B0C4DE");
$( '#TheirBoard td'  ).css("background-color", "#B0C4DE");
$( '#PlaceShipsBoard td'  ).css("background-color", "#B0C4DE");


if(gameModel.scanResult && gameModel.scanRequest){
alert("Scan found at least one Ship")}
else if(gameModel.scanRequest){
alert("Scan found no Ships")}

displayShip(gameModel.aircraftCarrier);
displayShip(gameModel.battleship);
displayShip(gameModel.clipper);
displayShip(gameModel.dinghy);
displayShip(gameModel.submarine);

for (var i = 0; i < gameModel.computerMisses.length; i++) {
   $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down ).css("background-color", "green");
}
for (var i = 0; i < gameModel.computerHits.length; i++) {
   $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down ).css("background-color", "red");
}

for (var i = 0; i < gameModel.playerMisses.length; i++) {
    //changing this
   $( '#MyBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-color", "green");
   $( '#PlaceShipsBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-color", "green");
}
for (var i = 0; i < gameModel.playerHits.length; i++) {
    //and this
   $( '#MyBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-color", "red");
   $( '#PlaceShipsBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-color", "red");
}



}



function displayShip(ship){
 startCoordAcross = ship.start.Across;
 startCoordDown = ship.start.Down;
 endCoordAcross = ship.end.Across;
 endCoordDown = ship.end.Down;
 if(startCoordAcross > 0){
    if(startCoordAcross == endCoordAcross){
        for (i = startCoordDown; i <= endCoordDown; i++) {
            $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-color", "yellow");
            $( '#PlaceShipsBoard #'+startCoordAcross+'_'+i  ).css("background-color", "yellow")
        }
    } else {
        for (i = startCoordAcross; i <= endCoordAcross; i++) {
            $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-color", "yellow");
            $( '#PlaceShipsBoard #'+i+'_'+startCoordDown  ).css("background-color", "yellow");
        }
    }
 }
}

function changeComputerShipPlacement() {
    //This is the function that will randomize the placement of the computers ships when it is on difficulty level hard
    //this master number is the number that will determine if we are dealing with horizontal ships or vertical ships
    var masterRandomNumber = Math.floor((Math.random() * 2) + 1);
    if (masterRandomNumber == 1) {
        //placing the aircraft career by getting a random number and using that number to determine the row to place the ship on
        var randomNumber1 = Math.floor((Math.random() * 10) + 1);
        gameModel.computer_aircraftCarrier.start.Across = randomNumber1;
        gameModel.computer_aircraftCarrier.start.Down = 1;
        gameModel.computer_aircraftCarrier.end.Across = randomNumber1;
        gameModel.computer_aircraftCarrier.end.Down = 6;

        //same as for aircraft but for battleship except we make sure we are not in the same row as the aircraft
        randomNumber2 = randomNumber1;
        while (randomNumber1 == randomNumber2) {
            var randomNumber2 = Math.floor((Math.random() * 10) + 1);
        }
        gameModel.computer_battleship.start.Across = randomNumber2;
        gameModel.computer_battleship.start.Down = 3;
        gameModel.computer_battleship.end.Across = randomNumber2;
        gameModel.computer_battleship.end.Down = 7;

        //now we do the same for clipper but we check to make sure it's not in the same row as battleship and aircraft
        randomNumber3 = randomNumber1;
        while ((randomNumber3 == randomNumber1) || (randomNumber3 == randomNumber2)) {
            var randomNumber3 = Math.floor((Math.random() * 10) + 1);
        }
        gameModel.computer_clipper.start.Across = randomNumber3;
        gameModel.computer_clipper.start.Down = 8;
        gameModel.computer_clipper.end.Across = randomNumber3;
        gameModel.computer_clipper.end.Down = 10;

        //this is for the submarine, it checks against the aircraft and the battleship but it is short enough to line share with clipper
        randomNumber4 = randomNumber1;
        while ((randomNumber4 == randomNumber1) || (randomNumber4 == randomNumber2)) {
            var randomNumber4 = Math.floor((Math.random() * 10) + 1);
        }
        gameModel.computer_submarine.start.Across = randomNumber4;
        gameModel.computer_submarine.start.Down = 3;
        gameModel.computer_submarine.end.Across = randomNumber4;
        gameModel.computer_submarine.end.Down = 5;

        //this is for the little baby dingy
        randomNumber5 = randomNumber1;
        while ((randomNumber5 == randomNumber1) || (randomNumber5 == randomNumber2)) {
            var randomNumber5 = Math.floor((Math.random() * 10) + 1);
        }
        gameModel.computer_dinghy.start.Across = randomNumber5;
        gameModel.computer_dinghy.start.Down = masterRandomNumber;
        gameModel.computer_dinghy.end.Across = randomNumber5;
        gameModel.computer_dinghy.end.Down = masterRandomNumber;
    }
    else {
        //the following is idential just the orientation shanges
        var randomNumber1 = Math.floor((Math.random() * 10) + 1);
        gameModel.computer_aircraftCarrier.start.Down = randomNumber1;
        gameModel.computer_aircraftCarrier.start.Across = 1;
        gameModel.computer_aircraftCarrier.end.Down = randomNumber1;
        gameModel.computer_aircraftCarrier.end.Across = 6;

        randomNumber2 = randomNumber1;
        while (randomNumber1 == randomNumber2) {
            var randomNumber2 = Math.floor((Math.random() * 10) + 1);
        }
        gameModel.computer_battleship.start.Down = randomNumber2;
        gameModel.computer_battleship.start.Across = 3;
        gameModel.computer_battleship.end.Down = randomNumber2;
        gameModel.computer_battleship.end.Across = 7;

        randomNumber3 = randomNumber1;
        while ((randomNumber3 == randomNumber1) || (randomNumber3 == randomNumber2)) {
            var randomNumber3 = Math.floor((Math.random() * 10) + 1);
        }
        gameModel.computer_clipper.start.Down = randomNumber3;
        gameModel.computer_clipper.start.Across = 8;
        gameModel.computer_clipper.end.Down = randomNumber3;
        gameModel.computer_clipper.end.Across = 10;

        randomNumber4 = randomNumber1;
        while ((randomNumber4 == randomNumber1) || (randomNumber4 == randomNumber2)) {
            var randomNumber4 = Math.floor((Math.random() * 10) + 1);
        }
        gameModel.computer_submarine.start.Down = randomNumber4;
        gameModel.computer_submarine.start.Across = 3;
        gameModel.computer_submarine.end.Down = randomNumber4;
        gameModel.computer_submarine.end.Across = 5;

        randomNumber5 = randomNumber1;
        while ((randomNumber5 == randomNumber1) || (randomNumber5 == randomNumber2)) {
            var randomNumber5 = Math.floor((Math.random() * 10) + 1);
        }
        gameModel.computer_dinghy.start.Down = randomNumber5;
        gameModel.computer_dinghy.start.Across = masterRandomNumber;
        gameModel.computer_dinghy.end.Down = randomNumber5;
        gameModel.computer_dinghy.end.Across = masterRandomNumber;
    }
}
