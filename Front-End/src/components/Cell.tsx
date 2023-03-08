import React from "react";

export type CellProps = {
  x: number;
  y: number;
  hasJoined: boolean;
  setHasJoined: (b: boolean) => void;
  symbol: string;
  color: string;
  mark: (x: number, y: number) => void;
};

const Cell = ({
  x,
  y,
  hasJoined,
  setHasJoined,
  symbol,
  color,
  mark,
}: CellProps) => {
  return (
    <td
      draggable="true"
      style={{
        backgroundColor: color,
        width: "175px",
        height: "175px",
        cursor: "pointer",
        border: "1px solid",
        fontSize: "60px",
        fontWeight: "bold",
      }}
      onClick={() => {
        if (!hasJoined) setHasJoined(true);
        mark(x, y);
      }}
      onDragEnter={() => mark(x, y)}
    >
      {symbol === "e" ? " " : symbol}
    </td>
  );
};

export default React.memo(Cell);
