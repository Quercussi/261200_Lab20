import React from "react";

export type CellProps = {
  x: number;
  y: number;
  symbol: string;
  mark: (x: number, y: number) => void;
};

const Cell = ({ x, y, symbol, mark }: CellProps) => {
  return (
    <td
      draggable="true"
      style={{
        backgroundColor: "#ffffff",
        width: "175px",
        height: "175px",
        cursor: "pointer",
        border: "1px solid",
        fontSize: "60px",
        fontWeight: "bold",
      }}
      onClick={() => mark(x, y)}
      onDragEnter={() => mark(x, y)}
    >
      {symbol}
    </td>
  );
};

export default React.memo(Cell);
