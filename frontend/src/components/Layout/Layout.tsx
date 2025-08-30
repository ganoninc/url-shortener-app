export type LayoutProps = {
  header: React.ReactElement;
  body: React.ReactElement;
  footer: React.ReactElement;
};

export default function Layout({ header, body, footer }: LayoutProps) {
  return (
    <div className="layout">
      <div className="header-container">{header}</div>
      <div className="body-container">{body}</div>
      <div className="footer-container">{footer}</div>
    </div>
  );
}
