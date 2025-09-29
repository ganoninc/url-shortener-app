import styles from "./Table.module.css";

type TableProps<TH extends readonly string[]> = {
  headers: TH;
  rows: { [K in keyof TH]: React.ReactNode }[];
};

export default function Table<TH extends readonly string[]>({
  headers,
  rows,
}: TableProps<TH>) {
  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: `repeat(${headers.length}, 1fr)`,
        gap: "1em",
        alignItems: "center",
      }}
    >
      {headers.map((header, index) => (
        <div key={`header-${index}`} className={styles.header}>
          {header}
        </div>
      ))}
      {rows.map((row, rowIndex) =>
        row.map((cell, cellIndex) => (
          <div key={`row-${rowIndex}-cell-${cellIndex}`}>{cell}</div>
        ))
      )}
    </div>
  );
}
