import type { ReactNode } from "react";

export type PageProps = {
  children: ReactNode;
};

export default function Page({ children }: PageProps) {
  return <div className="page">{children}</div>;
}
