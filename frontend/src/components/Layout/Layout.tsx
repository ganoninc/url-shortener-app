import "./Layout.css";

export type LayoutProps = {
  header: React.ReactElement;
  body: React.ReactElement;
  footer: React.ReactElement;
};

export default function Layout({ header, body, footer }: LayoutProps) {
  return (
    <div className="layout">
      <header className="header-container">{header}</header>
      <main className="body-container">{body}</main>
      <footer className="footer-container">{footer}</footer>
    </div>
  );
}
