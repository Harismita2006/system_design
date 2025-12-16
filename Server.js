const express = require("express");
const mongoose = require("mongoose");
const WebSocket = require("ws");
const Message = require("./models/Message");

const app = express();
app.use(express.static("public"));

mongoose.connect(
  "YOUR_MONGODB_ATLAS_URL",
  { useNewUrlParser: true, useUnifiedTopology: true }
).then(() => console.log("MongoDB Connected"));

const server = app.listen(3000, () =>
  console.log("Server running on http://localhost:3000")
);

const wss = new WebSocket.Server({ server });

wss.on("connection", async (ws) => {

  // Send old messages to new user
  const messages = await Message.find().sort({ time: 1 });
  ws.send(JSON.stringify(messages));

  ws.on("message", async (data) => {
    const msg = JSON.parse(data);

    // Save message to MongoDB
    const newMsg = new Message(msg);
    await newMsg.save();

    // Broadcast message to all clients
    wss.clients.forEach(client => {
      if (client.readyState === WebSocket.OPEN) {
        client.send(JSON.stringify([newMsg]));
      }
    });
  });
});
