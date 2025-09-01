import "./Footer.css";

export default function Footer() {
  return (
    <div className="footer">
      © {new Date().getFullYear()} Romain Giovanetti —{" "}
      <a
        href="https://github.com/ganoninc/url-shortener-app"
        target="_blank"
        rel="noopener noreferrer"
      >
        See how it’s built
      </a>
    </div>
  );
}
