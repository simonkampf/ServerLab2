var socket;
function setup() {
  createCanvas(400, 400);
  background(200);
  socket = io.connect('http://127.0.0.1:3000');
  socket.on('coordinate', newDrawing);
}
function newDrawing(coordinate){
  noStroke();
  fill(255, 0, 100);
  ellipse(coordinate.x, coordinate.y, 20, 20);
}

function mouseDragged(){
  console.log("Sending: " + mouseX + ", " + mouseY);
  fill(255, 0, 100);
  ellipse(mouseX, mouseY, 20, 20);
  var coordinate = {
    x: mouseX,
    y: mouseY
  }
  socket.emit('coordinate', coordinate);
}
function draw() {
//  noStroke();
//  fill(220);
//  ellipse(mouseX, mouseY, 20, 20);
}
