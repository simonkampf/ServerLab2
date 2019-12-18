var socket;
var data = [];

function setup() {
  createCanvas(400, 400);
  background(200);
  socket = io.connect('http://127.0.0.1:3000');
  socket.on('coordinate', newDrawing);
  socket.on('loaddata', fillCanvasWithData);
}
function newDrawing(coordinate){
  data.push(coordinate);
 // console.log(coordinate);
  noStroke();
  fill(255, 0, 100);
  ellipse(coordinate.x, coordinate.y, 20, 20);
}

function fillCanvasWithData(newData){
  console.log("Received new data");
  data = newData;
  var i;
  
  for(i = 0; i < data.length; i++){
    fill(255, 0, 100);
    ellipse(data[i].x, data[i].y, 20, 20);
  }
}


function mouseDragged(){
 // console.log("Sending: " + mouseX + ", " + mouseY);
  fill(255, 0, 100);
  ellipse(mouseX, mouseY, 20, 20);
  var coordinate = {
    x: mouseX,
    y: mouseY
  }
  data.push(coordinate);
  socket.emit('coordinate', coordinate);
}
function draw() {
//  noStroke();
//  fill(220);
//  ellipse(mouseX, mouseY, 20, 20);
}

function openFile(){
  var filename = document.forms["openfileform"]["filename"].value;
  //console.log();
  if(filename == "" ){
    alert("Name must be filled out");
  }else{
    socket.emit('loaddata', filename);
  }
}

function saveToFile(){
  var filename = document.forms["filenameform"]["filename"].value;
  //console.log();
  if(filename == "" ){
    alert("Name must be filled out");
  }else{
    socket.emit('savedata', data, filename);
    
  }
}

function submitForm() {
  var http = new XMLHttpRequest();
  http.open("POST", "openFile()", true);
  http.setRequestHeader("Content-type","application/x-www-form-urlencoded");
  var params = "filename=" + document.getElementById(filename).value; // probably use document.getElementById(...).value
  http.send(params);
  http.onload = function() {
      alert(http.responseText);
  }
}