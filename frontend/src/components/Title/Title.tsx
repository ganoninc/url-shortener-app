import type { JSX } from "react";

export type TitleProps = {
  label: string;
  level?: "1" | "2" | "3" | "4" | "5" | "6";
};

export default function Title({ label, level = "1" }: TitleProps) {
  const Tag = `h${level}` as keyof JSX.IntrinsicElements;
  return <Tag>{label}</Tag>;
}
