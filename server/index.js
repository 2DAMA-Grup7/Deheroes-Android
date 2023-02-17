const express = require("express");
const app = express();
const server = require("http").Server(app);
const io = require("socket.io")(server);
const port = 3000;

const users = [];
const players = [];

io.on('connection', function(socket){
	console.log("Player Connected!");
	socket.emit('socketID', { id: socket.id });
	socket.emit('getPlayers', players);
	socket.broadcast.emit('newPlayer', { id: socket.id });
  socket.on("playerMoved",function(data){
    data.id=socket.id
    socket.broadcast.emit("playerMoved",data)
        players.x= data.x
        players.y= data.y
  })
	socket.on('disconnect', function(){
		console.log("Player Disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
				players.splice(i, 1);

		}
	});
	players.push(new player(socket.id, 0, 0));
});

/*io.on("connection", (socket) => {
  socket.on("disconnect", () => {
    const index = users.findIndex((user) => user.id === socket.id);
    if (index !== -1) {
      socket.broadcast.emit("userDisconnected", users[index].name);
      console.log(`${users[index].name} has disconnected`);
      users.splice(index, 1);
    }
  });

  socket.on("setNick", (user) => {
    console.log(`User connected: ${user}`);
    socket.broadcast.emit("newUser", user);
    socket.emit("getUsers", users);
    users.push(new User(socket.id, user));
  });

  socket.emit('socketID', { id: socket.id });
  
  socket.on("userMessage", (data) => {
    socket.broadcast.emit("userMessage", data);
    socket.emit("userMessage", data);
  });
});

class User {
  constructor(id, name,x,y) {
    this.id = id;
    this.name = name;
    this.x=300 
    this.y=300 
  }
} */

class player {
  constructor(id,x,y) {
    this.id = id;
    this.x=x 
    this.y=y 
  }
}

server.listen(port, () => {
  console.log(`Server running at ${port}`);
});
