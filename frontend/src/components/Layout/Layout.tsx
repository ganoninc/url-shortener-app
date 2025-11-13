import { Toaster } from "react-hot-toast";
import styles from "./Layout.module.css";

export type LayoutProps = {
  header: React.ReactNode;
  body: React.ReactNode;
  footer: React.ReactNode;
};

export default function Layout({ header, body, footer }: LayoutProps) {
  return (
    <div className={styles.layout}>
      <header className={styles.headerContainer}>{header}</header>
      <main className={styles.bodyContainer}>{body}</main>
      <footer className={styles.footerContainer}>{footer}</footer>
      <Toaster toastOptions={{ duration: 2500 }} />
    </div>
  );
}
