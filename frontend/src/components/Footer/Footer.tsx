import styles from "./Footer.module.css";

export default function Footer() {
  return (
    <div className={styles.footer}>
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
