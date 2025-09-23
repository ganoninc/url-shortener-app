import type { JSX } from "react";
import styles from "./Title.module.css";

export type TitleProps = {
  content: string;
  level?: "1" | "2" | "3" | "4" | "5" | "6";
};

export default function Title({ content, level = "1" }: TitleProps) {
  const Tag = `h${level}` as keyof JSX.IntrinsicElements;
  return <Tag className={styles.title}>{content}</Tag>;
}
