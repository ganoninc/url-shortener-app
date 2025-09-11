import type { ReactNode } from "react";
import styles from "./Page.module.css";

export type PageProps = {
  children: ReactNode;
};

export default function Page({ children }: PageProps) {
  return <div className={styles.page}>{children}</div>;
}
