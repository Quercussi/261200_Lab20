import React, { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

import Cell from "./Cell";

let client: Client;
let id: string;
//let color: string;

function Canvas() {
  const [grid, setGrid] = useState([[]]);

  useEffect(() => {
    if (!client) {
      //color = "#" + Math.floor(Math.random() * 16777215).toString(16);
      client = new Client({
        brokerURL: "ws://localhost:8080/demo-websocket",

        onConnect: () => {
          client.publish({
            destination: "/app/join",
            body: id,
          });
          client.subscribe("/app/grid", (message) => {
            const body = JSON.parse(message.body);
            setGrid(body["grid"]);
          });

          client.subscribe("/topic/grid", (message) => {
            const body = JSON.parse(message.body);
            console.log(message.body);
            setGrid(body["grid"]);
          });
        },
      });

      client.activate();
      id = "" + Math.random();
      console.log(id);
    }
  }, []);

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
    <div style={{ position: "absolute", left: "37%", bottom: "27%" }}>
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
                  key={`${i}${j}`}
                  symbol={grid[j][i]}
                />
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Canvas;
