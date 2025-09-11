import styles from "./Layout.module.css";

export type LayoutProps = {
  header: React.ReactElement;
  body: React.ReactElement;
  footer: React.ReactElement;
};

export default function Layout({ header, body, footer }: LayoutProps) {
  return (
    <div className={styles.layout}>
      <header className={styles.headerContainer}>{header}</header>
      <main className={styles.bodyContainer}>{body}</main>
      <footer className={styles.footerContainer}>{footer}</footer>
    </div>
  );
}
