import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

import Cell from "./Cell";

let client: Client;
let id: string;
//let color: string;

function Canvas() {
  const [grid, setGrid] = useState([[]]);
  const [color, setColor] = useState([[]]);

  const [showRestart, setShowRestart] = useState(false);
  const [hasJoined, setHasJoined] = useState(false);

  useEffect(() => {
    if (!client) {
      //color = "#" + Math.floor(Math.random() * 16777215).toString(16);
      client = new Client({
        brokerURL: "ws://localhost:8080/demo-websocket",

        onConnect: () => {
          client.subscribe("/app/grid", (message) => {
            const body = JSON.parse(message.body);
            setGrid(body["grid"]);
            setColor(body["colorGrid"]);
            setShowRestart(false);
            console.log(body);
          });

          client.subscribe("/topic/grid", (message) => {
            const body = JSON.parse(message.body);
            console.log(message.body);
            setGrid(body["grid"]);
            setColor(body["colorGrid"]);
            const winner = body["winner"];
            if (winner === "x" || winner === "o" || winner === "t") {
              setShowRestart(true);
              setHasJoined(false);
            } else setShowRestart(false);
            console.log(body);
          });
        },
      });

      client.activate();
      id = "" + Math.random();
      console.log(id);
    }
  }, []);

  useEffect(() => {
    if (hasJoined) {
      client.publish({
        destination: "/app/join",
        body: id,
      });
    }
  }, [hasJoined]);

  const mark = (x: number, y: number) => {
    if (client) {
      if (client.connected) {
        client.publish({
          destination: "/app/mark",
          body: JSON.stringify({
            token: id,
            posX: x,
            posY: y,
          }),
        });
      }
    }
  };

  return (
    <div style={{ position: "absolute", left: "33%", bottom: "18%" }}>
      <table
        style={{
          borderCollapse: "collapse",
        }}
      >
        <tbody>
          {grid.map((row, j) => (
            <tr key={j}>
              {Array.from(row).map((col, i) => (
                <Cell
                  x={i}
                  y={j}
                  mark={mark}
                  hasJoined={hasJoined}
                  setHasJoined={setHasJoined}
                  key={`${i}${j}`}
                  symbol={grid[j][i]}
                  color={color[j][i]}
                />
              ))}
            </tr>
          ))}
        </tbody>
      </table>
      {showRestart ? (
        <button
          onClick={() => {
            client.publish({
              destination: "/app/restart",
            });
            setShowRestart(false);
          }}
        >
          RESTART
        </button>
      ) : (
        ""
      )}
    </div>
  );
}

export default Canvas;
