var express = require('express');

var app = express();
var server = app.listen(3000);
app.use(express.static('public'));

var socket = require('socket.io');
var io = socket(server);
io.sockets.on('connection', newConnection);

var fs = require('fs');

function newConnection(socket){
  console.log("New connection: " + socket.id);
  socket.on('coordinate', coordinateMsg);
  socket.on('savedata', saveToFile);
  socket.on('loaddata', loadData);
  function coordinateMsg(coordinate){
    socket.broadcast.emit('coordinate', coordinate)
  //    console.log(coordinate);
  }
  function saveToFile(data, filename){
    console.log("save to file");
    console.log(data);
    console.log(filename);
    var jsonData = JSON.stringify(data);
    
    fs.writeFile(filename + ".json", jsonData, uploadFinished);
   
    function uploadFinished(err){

      console.log("Uploaded file");
    }
  }

  function loadData(filename){
    console.log("Opening file: " + filename + ".json");
    try{
      var jsonData = fs.readFileSync(filename + ".json");
    }catch(err){
      console.log("No such file exists");
      return;
    }
    var data = JSON.parse(jsonData);
    console.log(data);
    socket.emit('loaddata', data);
  }
}

