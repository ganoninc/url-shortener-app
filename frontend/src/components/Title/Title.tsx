import type { JSX } from "react";

export type TitleProps = {
  content: string;
  level?: "1" | "2" | "3" | "4" | "5" | "6";
};

export default function Title({ content, level = "1" }: TitleProps) {
  const Tag = `h${level}` as keyof JSX.IntrinsicElements;
  return <Tag>{content}</Tag>;
}
